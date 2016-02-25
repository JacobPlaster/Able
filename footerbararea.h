#ifndef FOOTERBARAREA_H
#define FOOTERBARAREA_H

#include <QObject>
#include <QWidget>

#include <QComboBox>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QLineEdit>
#include <QLabel>
#include <QPushButton>

#include "codeeditor.h"
#include "assetmanager.h"

class QWidget;
class QPaintEvent;
class QResizeEvent;

class FooterBarArea : public QWidget
{
    Q_OBJECT

    public:
        FooterBarArea(CodeEditor *editor);
        ~FooterBarArea();
        QComboBox * comboBox;
        QLineEdit * searchBox;
        QLabel * filePathLabel;
        QPushButton * resizeButton;
        QLineEdit * replaceBox;

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

                QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightCollapsed)+"px;}";
                codeEditor->setStyleSheet(style);
            } else
            {
                isExpanded = true;
                height = heightExpanded;

                 QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightExpanded)+"px;}";
                codeEditor->setStyleSheet(style);
            }
            codeEditor->setFooterHeight(height);
        }
};


#endif // FOOTERBARAREA_H
