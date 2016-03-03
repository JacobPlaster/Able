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

#include "assetmanager.h"
#include "codeeditor.h"

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
        QPushButton * replaceButton;
        QPushButton * moreButton;
        QPushButton * searchButton;
        QLineEdit * replaceBox;
        QLabel * cursorInfoLabel;

        void setCursorInfoText(int charIndex, int lineIndex);

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
        QHBoxLayout *mainLayout;
        QVBoxLayout *layout;
        QVBoxLayout *layout2;
        QHBoxLayout *layout2H1;
        QHBoxLayout *layout2H;
        QVBoxLayout *layout3;
        QHBoxLayout *layout3H;

    private slots:
        void comboChanged(int index)
        {
            // if the combo box isnt set to 'None'
            if(index < languagesSupported.count())
            {
                // if that rule isnt already applied
                if(codeEditor->syntaxHighlighter->ruleSet != NULL)
                {
                    if(codeEditor->syntaxHighlighter->ruleSet->fileName != languagesSupported[index])
                        codeEditor->changeLanguageSupport(languagesSupported[index]);
                } else
                {
                    codeEditor->changeLanguageSupport(languagesSupported[index]);
                }
            } else
            {
                codeEditor->changeLanguageSupport(NULL);
            }
        }

        void searchButtonClicked()
        {
            QString text = searchBox->text();
            QRegExp exp = QRegExp(text);
            codeEditor->highlightText(exp);
        }
        void codeCursorChanged();

        void toggleResize();
        void replaceMatchedText();
};


#endif // FOOTERBARAREA_H
