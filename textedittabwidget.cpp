#include <QtWidgets>
#include <QDebug>
#include <QMessageBox>

#include "textedittabwidget.h"

TextEditTabWidget::TextEditTabWidget(QWidget *parent) : QTabWidget(parent)
{

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
    addTab(newCodeEditor, newCodeEditor->getCurrentFileInfo()->fileName());

    return true;
}
