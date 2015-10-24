#include <QtWidgets>

#include "textedittabwidget.h"

TextEditTabWidget::TextEditTabWidget(QWidget *parent) : QTabWidget(parent)
{

}

void TextEditTabWidget::resizeEvent(QResizeEvent *e)
{
    QTabWidget::resizeEvent(e);
}

void TextEditTabWidget::addCodeTab(const QString &string)
{
    CodeEditor * newCodeEditor = new CodeEditor();
    newCodeEditor->setTabStopWidth(35);
    newCodeEditor->setWordWrapMode(QTextOption::NoWrap);

    addTab(newCodeEditor, string);
}
