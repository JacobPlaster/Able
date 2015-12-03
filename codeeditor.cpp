#include <QtWidgets>
#include <QMessageBox>
#include <QTextBlock>
#include <QTextCursor>

#include <iostream>
#include <fstream>

#include "codeeditor.h"


CodeEditor::CodeEditor(QWidget *parent) : QPlainTextEdit(parent), completer(0)
{
    lineNumberArea = new LineNumberArea(this);
    syntaxHighlighter = new SyntaxHighlighter(document());
    autoCompleteModel = NULL;

    connect(this, SIGNAL(blockCountChanged(int)), this, SLOT(updateLineNumberAreaWidth(int)));
    connect(this, SIGNAL(updateRequest(QRect,int)), this, SLOT(updateLineNumberArea(QRect,int)));
    connect(this, SIGNAL(cursorPositionChanged()), this, SLOT(highlightAndSearchCurrentLine()));

    updateLineNumberAreaWidth(0);
    highlightAndSearchCurrentLine();

    completer = new QCompleter(this);
    completer->setWrapAround(false);
    setAutoCompleter(completer);
}

void CodeEditor::setAutoCompleteModel(QStringList  &wordList)
{
    if(autoCompleteModel)
        delete autoCompleteModel;
    autoCompleteModel = new QStringListModel;
    autoCompleteModel->setStringList(wordList);
    completer->setModel(autoCompleteModel);
}

void CodeEditor::setAutoCompleter(QCompleter *inCompleter)
{

    // if already exists
    if (completer)
        QObject::disconnect(completer, 0, this, 0);

    completer = inCompleter;
    if (!completer)
       return;

    // ties to current widget and opens signals
    completer->setWidget(this);
    completer->setCompletionMode(QCompleter::PopupCompletion);
    QObject::connect(completer, SIGNAL(activated(QString)),
                    this, SLOT(insertCompletion(QString)));

}

void CodeEditor::focusInEvent(QFocusEvent *e)
{
    if (completer)
        completer->setWidget(this);
     QPlainTextEdit::focusInEvent(e);
}

void CodeEditor::keyPressEvent(QKeyEvent *e)
{
    // update autocorrect if enter pressed
    if(e->key() == Qt::Key_Return && !completer->popup()->isVisible())
       this->setAutoCompleteModel(syntaxHighlighter->getAutoCompleteRules());

    if (completer && completer->popup()->isVisible()) {
        // keys that are returned to the codeEditor from the completer
        // we have to do this since the codeEditor is now out of focus
       switch (e->key()) {
       case Qt::Key_Enter:
       case Qt::Key_Return:
       case Qt::Key_Escape:
       case Qt::Key_Tab:
       case Qt::Key_Backtab:
            e->ignore();
            return;
       default:
           break;
       }
    }

    bool isShortcut = ((e->modifiers() & Qt::ControlModifier) && e->key() == Qt::Key_E); // CTRL+E
        if (!completer || !isShortcut) // do not process the shortcut when we have a completer
            QPlainTextEdit::keyPressEvent(e);

    const bool ctrlOrShift = e->modifiers() & (Qt::ControlModifier | Qt::ShiftModifier);
        if (!completer || (ctrlOrShift && e->text().isEmpty()))
            return;

    // signals that the autocomplete is at the end of a word
    static QString eow("~!@#$%^&*()_+{}|:\"<>?,./;'[]\\-="); // end of word
    bool hasModifier = (e->modifiers() != Qt::NoModifier) && !ctrlOrShift;
    QString completionPrefix = textUnderCursor();

    // popup the completer after atleast 2 characters have been typed
    if (!isShortcut && (hasModifier || e->text().isEmpty()|| completionPrefix.length() < 2
                      || eow.contains(e->text().right(1)))) {
        completer->popup()->hide();
        return;
    }

    if (completionPrefix != completer->completionPrefix()) {
        completer->setCompletionPrefix(completionPrefix);
        completer->popup()->setCurrentIndex(completer->completionModel()->index(0, 0));
    }
    QRect cr = cursorRect();
    cr.setWidth(completer->popup()->sizeHintForColumn(0)
                + completer->popup()->verticalScrollBar()->sizeHint().width());

    // open completer window
    completer->complete(cr);
}

// gets the text under the cursors position
QString CodeEditor::textUnderCursor() const
{
    QTextCursor tc = textCursor();
    tc.select(QTextCursor::WordUnderCursor);
    return tc.selectedText();
}

// finds the insertion location and replaces it with in QString
void CodeEditor::insertCompletion(const QString& completion)
{
    if (completer->widget() != this)
        return;
    QTextCursor tc = textCursor();
    int extra = completion.length() - completer->completionPrefix().length();
    tc.movePosition(QTextCursor::Left);
    tc.movePosition(QTextCursor::EndOfWord);
    tc.insertText(completion.right(extra));
    setTextCursor(tc);
}

QCompleter * CodeEditor::getCompleter() const
{
    return completer;
}

void CodeEditor::load(AssetManager *am)
{
    assetManager = am;
}

void CodeEditor::save()
{
    // open file
    std::ofstream testFile;
    testFile.open (currentFile->absoluteFilePath().toStdString().c_str());
    testFile << this->toPlainText().toStdString();
    testFile.flush();
    testFile.close();
}

bool CodeEditor::loadFile(const QString &fileString)
{
    clear();

    // load file
    QFile file(fileString);
    // check if file exists
    if(file.exists())
    {
        // check if file is readable
        if(!file.open(QIODevice::ReadOnly)) {
            qDebug() << "file not readable: " << fileString;
            QMessageBox::information(0, "error", file.errorString());
            return false;
        }
    } else
    {
        qDebug() << "file does not exist: " << fileString;
        QMessageBox::information(0, "Error", file.errorString());
        return false;
    }
    QTextStream in(&file);
    if(currentFile == NULL)
        delete currentFile;
    currentFile = new QFileInfo(file);

    syntaxHighlighter->load(assetManager, currentFile->completeSuffix());
    if(syntaxHighlighter->getRuleSet())
        this->setAutoCompleteModel(syntaxHighlighter->getAutoCompleteRules());

    // read line by line
    while(!in.atEnd()) {
        QString line = in.readLine();
        appendPlainText(line);
    }
    // close file (ALWAYS)
    file.close(); 

    return true;
}

QFileInfo * CodeEditor::getCurrentFileInfo() const
{
    return currentFile;
}


int CodeEditor::lineNumberAreaWidth()
{
    int digits = 1;
    int max = qMax(1, blockCount());
    while (max >= 10) {
        max /= 10;
        ++digits;
    }

    int space = 10 + fontMetrics().width(QLatin1Char('9')) * digits;

    return space;
}



void CodeEditor::updateLineNumberAreaWidth(int /* newBlockCount */)
{
    setViewportMargins(lineNumberAreaWidth(), 0, 0, 0);
}



void CodeEditor::updateLineNumberArea(const QRect &rect, int dy)
{
    if (dy)
        lineNumberArea->scroll(0, dy);
    else
        lineNumberArea->update(0, rect.y(), lineNumberArea->width(), rect.height());

    if (rect.contains(viewport()->rect()))
        updateLineNumberAreaWidth(0);
}



void CodeEditor::resizeEvent(QResizeEvent *e)
{
    QPlainTextEdit::resizeEvent(e);
    QRect cr = contentsRect();
    lineNumberArea->setGeometry(QRect(cr.left()-10, cr.top()-1, lineNumberAreaWidth()+10, cr.height()));
}

void CodeEditor::highlightAndSearchCurrentLine()
{
    QList<QTextEdit::ExtraSelection> extraSelections;

    if (!isReadOnly()) {
        QTextEdit::ExtraSelection selection;

        QColor lineColor = QColor("#f7f3f7");

        selection.format.setBackground(lineColor);
        selection.format.setProperty(QTextFormat::FullWidthSelection, true);
        selection.cursor = textCursor();
        selection.cursor.clearSelection();
        extraSelections.append(selection);
    }

    setExtraSelections(extraSelections);
}



void CodeEditor::lineNumberAreaPaintEvent(QPaintEvent *event)
{
    QPainter painter(lineNumberArea);
    painter.fillRect(event->rect(), QColor("#E6E6E6"));


    QTextBlock block = firstVisibleBlock();
    int blockNumber = block.blockNumber();
    int top = (int) blockBoundingGeometry(block).translated(contentOffset()).top();
    int bottom = top + (int) blockBoundingRect(block).height();

    while (block.isValid() && top <= event->rect().bottom()) {
        if (block.isVisible() && bottom >= event->rect().top()) {
            QString number = QString::number(blockNumber + 1);
            painter.setPen(QColor("#A7A3A7"));
            painter.drawText(0, top, lineNumberArea->width(), fontMetrics().height(),
                             Qt::AlignCenter, number);
        }

        block = block.next();
        top = bottom;
        bottom = top + (int) blockBoundingRect(block).height();
        ++blockNumber;
    }
}

CodeEditor::~CodeEditor()
{
    if(completer)
    {
        QObject::disconnect(completer, 0, this, 0);
        delete completer;
        completer = NULL;
    }
    if(autoCompleteModel)
        delete autoCompleteModel;
    delete currentFile;
    currentFile = NULL;
    delete syntaxHighlighter;
    syntaxHighlighter = NULL;
    delete lineNumberArea;
    lineNumberArea = NULL;
}
