#include <QtWidgets>
#include <QDebug>
#include <QMessageBox>

#include "textedittabwidget.h"

TextEditTabWidget::TextEditTabWidget(QWidget *parent) : QTabWidget(parent)
{
    connect(this, SIGNAL(tabCloseRequested(int)), this, SLOT(closeTab(int)));
}

TextEditTabWidget::~TextEditTabWidget()
{

}

void TextEditTabWidget::closeTab(int tabIndex)
{
    if (tabIndex == -1)
            return;

    // Removes but, the page widget itself is not deleted.
    QWidget* tab = this->widget(tabIndex);
    this->removeTab(tabIndex);

    // delete page
    delete(tab);
    tab = NULL;
}

void TextEditTabWidget::saveCurrentEditor()
{
    int selectedIndex = this->currentIndex();
    getEditorAtIndex(selectedIndex)->save();
}

CodeEditor * TextEditTabWidget::getEditorAtIndex(int i) const
{
    QList<CodeEditor *> allEditors = this->findChildren<CodeEditor *>();
    return allEditors[i];
}

QList<CodeEditor *> TextEditTabWidget::getAllEditors() const
{
    return this->findChildren<CodeEditor *>();
}

void TextEditTabWidget::load(AssetManager *inAssetManager)
{
    assetManager = inAssetManager;
}

void TextEditTabWidget::resizeEvent(QResizeEvent *e)
{
    QTabWidget::resizeEvent(e);
}

bool TextEditTabWidget::addCodeTab(const QString &fileString)
{
    // check if code tab exists
    QList<CodeEditor *> allEditors = this->findChildren<CodeEditor *>();
    for(int ie = 0; ie < allEditors.count(); ie++)
    {
        // if file already open then set it to focus
        if(allEditors[ie]->getCurrentFileInfo()->absoluteFilePath() == fileString)
        {
            this->setCurrentIndex(this->indexOf(allEditors[ie]));
            return false;
        }
    }

    // create new code editor widget
    CodeEditor * newCodeEditor = new CodeEditor();
    newCodeEditor->load(assetManager);

    // \/ causes asset manager variable to crash
    if(!newCodeEditor->loadFile(fileString))
        return false;

    // move cursor back to top of page
    QTextCursor cursor(newCodeEditor->textCursor());
    cursor.movePosition(QTextCursor::Start);
    newCodeEditor->setTextCursor(cursor);

    newCodeEditor->setTabStopWidth(30);
    newCodeEditor->setWordWrapMode(QTextOption::NoWrap);
    newCodeEditor->setFont(assetManager->getFont("MAIN_CODE_FONT"));

    // add new widget
    insertTab(0, newCodeEditor, newCodeEditor->getCurrentFileInfo()->fileName());
    // set focus to newly created tab
    this->setCurrentIndex(0);

    return true;
}
