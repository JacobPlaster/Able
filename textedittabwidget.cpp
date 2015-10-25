#include <QtWidgets>
#include <QDebug>
#include <QMessageBox>

#include "textedittabwidget.h"

TextEditTabWidget::TextEditTabWidget(QWidget *parent) : QTabWidget(parent)
{

}

void TextEditTabWidget::resizeEvent(QResizeEvent *e)
{
    QTabWidget::resizeEvent(e);
}

bool TextEditTabWidget::addCodeTab(const QString &titleString, const QString &fileString)
{
    // load file
    QFile file(fileString);
    // check if file exists
    if(file.exists())
    {
        // check if file is readable
        if(!file.open(QIODevice::ReadOnly)) {
            QMessageBox::information(0, "error", file.errorString());
            return false;
        }
    } else
    {
        QMessageBox::information(0, "Error", file.errorString());
        return false;
    }
    QTextStream in(&file);

    // create new code editor widget
    CodeEditor * newCodeEditor = new CodeEditor();
    newCodeEditor->setTabStopWidth(35);
    newCodeEditor->setWordWrapMode(QTextOption::NoWrap);

    // read line by line
    while(!in.atEnd()) {
        QString line = in.readLine();
        newCodeEditor->appendPlainText(line);
    }
    // close file (ALWAYS)
    file.close();

    // add new widget
    addTab(newCodeEditor, titleString);

    return true;
}
