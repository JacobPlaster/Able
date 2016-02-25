#ifndef CODEEDITOR_H
#define CODEEDITOR_H

#include <QPlainTextEdit>
#include <QObject>
#include <QFileInfo>
#include <QCompleter>
#include <QStringListModel>
#include <QComboBox>
#include <QHBoxLayout>
#include <QLineEdit>
#include <QLabel>
#include <QPushButton>

#include "syntaxhighlighter.h"
#include "assetmanager.h"

class QPaintEvent;
class QResizeEvent;
class QSize;
class QWidget;

class LineNumberArea;


class CodeEditor : public QPlainTextEdit
{
    Q_OBJECT

public:
    CodeEditor(QWidget *parent = 0);
    ~CodeEditor();

    void lineNumberAreaPaintEvent(QPaintEvent *event);
    void footerBarAreaPaintEvent(QPaintEvent *event);
    int lineNumberAreaWidth();
    bool loadFile(const QString &);
    QFileInfo * getCurrentFileInfo() const;
    void load(AssetManager *am);
    void save();
    void setAutoCompleter(QCompleter *inCompleter);
    QCompleter * getCompleter() const;
    QString textUnderCursor() const;
    void setAutoCompleteModel(QStringList &);
    void changeLanguageSupport(QString supportFileName);
    void highlightText(QRegExp &);
    void resizeFooter(int height);

    AssetManager *assetManager;
    SyntaxHighlighter *syntaxHighlighter;
    QFileInfo *currentFile;
    void setFooterHeight(int height);

protected:
    void keyPressEvent(QKeyEvent *e) Q_DECL_OVERRIDE;
    void focusInEvent(QFocusEvent *e) Q_DECL_OVERRIDE;
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;

private slots:
    void updateLineNumberAreaWidth(int newBlockCount);
    void highlightAndSearchCurrentLine();
    void updateLineNumberArea(const QRect &, int);
    void insertCompletion(const QString &completion);

private:
    QWidget *lineNumberArea;
    QWidget *footerBarArea;
    QCompleter *completer;
    QStringListModel * autoCompleteModel;
    QStringList * dynamicAutocompleteSuggestions;
    int footerHeight;


};


class LineNumberArea : public QWidget
{
public:
    LineNumberArea(CodeEditor *editor) : QWidget(editor) {
        codeEditor = editor;
    }

    QSize sizeHint() const Q_DECL_OVERRIDE {
        return QSize(codeEditor->lineNumberAreaWidth(), 0);
    }

protected:
    void paintEvent(QPaintEvent *event) Q_DECL_OVERRIDE {
        codeEditor->lineNumberAreaPaintEvent(event);
    }

private:
    CodeEditor *codeEditor;
};









class FooterBarArea : public QWidget
{
    Q_OBJECT

public:
    FooterBarArea(CodeEditor *editor) : QWidget(editor) {
        codeEditor = editor;
        this->setObjectName("codeEditorFooter");

        QHBoxLayout *layout = new QHBoxLayout();
        this->setLayout(layout);

        comboBox = new QComboBox();
        searchBox = new QLineEdit();
        filePathLabel = new QLabel();
        resizeButton = new QPushButton();

        languagesSupported = codeEditor->assetManager->getLoadedSupportFileNames();
        comboBox->addItems(languagesSupported);
        filePathLabel->setText(codeEditor->currentFile->absoluteFilePath());

        comboBox->setObjectName("footerComboBox");
        searchBox->setObjectName("footerSearchBox");
        searchBox->setPlaceholderText("RegExp search...");
        filePathLabel->setObjectName("footerFilePathLabel");
        filePathLabel->setAlignment(Qt::AlignRight);

        resizeButton->setObjectName("footerResizeButton");
        resizeButton->setText("^");


        layout->addWidget(comboBox);
        layout->addWidget(searchBox);
        layout->addWidget(filePathLabel);
        layout->addWidget(resizeButton);
        layout->setObjectName("footerComboBoxLayout");
        layout->setAlignment(comboBox, Qt::AlignLeft);
        layout->setAlignment(searchBox, Qt::AlignLeft);
        layout->setAlignment(filePathLabel, Qt::AlignRight);

        connect(comboBox, SIGNAL(currentIndexChanged(int)), this, SLOT(comboChanged(int)));
        connect(searchBox, SIGNAL(textChanged(const QString &)), this, SLOT(searchTextChanged(const QString &)));
        connect(resizeButton, SIGNAL(clicked()), this, SLOT(toggleResize()));

        if(codeEditor->syntaxHighlighter->ruleSet != NULL)
        {
            QString currentLanguage = codeEditor->syntaxHighlighter->ruleSet->fileName;
            for(int i = 0; i < languagesSupported.length(); i++)
            {
                if(languagesSupported[i] == currentLanguage)
                    comboBox->setCurrentIndex(i);
            }
        } else
        {
            comboBox->addItem("None");
            comboBox->setCurrentIndex(comboBox->count()-1);
        }

        heightCollapsed = 40;
        heightExpanded = 80;
        height = heightCollapsed;
        isExpanded = false;
    }
    QComboBox * comboBox;
    QLineEdit * searchBox;
    QLabel * filePathLabel;
    QPushButton * resizeButton;

protected:
    void paintEvent(QPaintEvent *event) Q_DECL_OVERRIDE {
        codeEditor->footerBarAreaPaintEvent(event);
    }


private:
    CodeEditor *codeEditor;
    QStringList languagesSupported;
    int footerHeight;
    int height;
    int heightCollapsed;
    int heightExpanded;
    bool isExpanded;

private slots:
    void comboChanged(int index)
    {
        // if the combo box isnt set to 'None'
        if(index < languagesSupported.count())
        {
            // if that rule isnt already applied
            if(codeEditor->syntaxHighlighter->ruleSet->fileName != languagesSupported[index])
                codeEditor->changeLanguageSupport(languagesSupported[index]);
        }
    }

    void searchTextChanged(const QString &text)
    {
        QRegExp exp = QRegExp(text);
        codeEditor->highlightText(exp);
    }

    void toggleResize()
    {
        if(isExpanded)
        {
            isExpanded = false;
            height = heightCollapsed;
        } else
        {
            isExpanded = true;
            height = heightExpanded;
        }
        codeEditor->setFooterHeight(height);
    }
};



#endif
