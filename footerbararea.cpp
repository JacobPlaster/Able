#include "footerbararea.h"

FooterBarArea::FooterBarArea(CodeEditor *editor) : QWidget(editor)
{
    codeEditor = editor;
    this->setObjectName("codeEditorFooter");

    mainLayout = new QHBoxLayout();
    this->setLayout(mainLayout);
    mainLayout->setObjectName("footerMainLayout");
    layout = new QVBoxLayout();
    mainLayout->addLayout(layout);
    layout2 = new QVBoxLayout();
    layout2H = new QHBoxLayout();
    mainLayout->addLayout(layout2);
    layout3 = new QVBoxLayout();
    mainLayout->addLayout(layout3);
    layout3H = new QHBoxLayout();
    layout3->addLayout(layout3H);



    comboBox = new QComboBox();
    searchBox = new QLineEdit();
    filePathLabel = new QLabel();
    resizeButton = new QPushButton();
    moreButton = new QPushButton();
    replaceButton = new QPushButton();
    replaceBox = new QLineEdit();
    cursorInfoLabel = new QLabel();

    languagesSupported = codeEditor->assetManager->getLoadedSupportFileNames();
    comboBox->addItems(languagesSupported);
    comboBox->addItem("None");
    filePathLabel->setText(codeEditor->currentFile->absoluteFilePath());

    comboBox->setObjectName("footerComboBox");
    searchBox->setObjectName("footerSearchBox");
    searchBox->setPlaceholderText("RegExp search...");
    filePathLabel->setObjectName("footerFilePathLabel");
    filePathLabel->setAlignment(Qt::AlignRight);
    cursorInfoLabel->setObjectName("footerCursorInfoLabel");
    moreButton->setObjectName("footerMoreButton");
    replaceButton->setObjectName("footerReplaceButton");

    resizeButton->setObjectName("footerResizeButton");
    resizeButton->setText("▴");

    layout->setObjectName("footerComboBoxLayout");
    layout->addWidget(comboBox);

    layout2->setObjectName("footerComboBoxLayout2");
    layout2->addWidget(searchBox);
    layout2->addLayout(layout2H);

    layout3H->addWidget(cursorInfoLabel);
    layout3H->addWidget(resizeButton);
    layout3H->setAlignment(resizeButton, Qt::AlignRight);
    layout3H->setAlignment(cursorInfoLabel, Qt::AlignRight);


    replaceBox->setPlaceholderText("Raplace with...");
    replaceBox->setObjectName("footerReplaceBox");
    cursorInfoLabel->setText("31:22");
    moreButton->setText("More");
    replaceButton->setText("Replace");

    connect(comboBox, SIGNAL(currentIndexChanged(int)), this, SLOT(comboChanged(int)));
    connect(searchBox, SIGNAL(textChanged(const QString &)), this, SLOT(searchTextChanged(const QString &)));
    connect(resizeButton, SIGNAL(clicked()), this, SLOT(toggleResize()));
    connect(replaceButton, SIGNAL(clicked()), this, SLOT(replaceMatchedText()));

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
        comboBox->setCurrentIndex(comboBox->count()-1);
    }

    heightCollapsed = 40;
    heightExpanded = 80;
    height = heightCollapsed;
    isExpanded = false;
}

void FooterBarArea::toggleResize()
{
    if(isExpanded)
    {
        isExpanded = false;
        height = heightCollapsed;

        layout2H->removeWidget(replaceBox);
        layout2H->removeWidget(replaceButton);
        layout3->removeWidget(filePathLabel);
        layout->removeWidget(moreButton);
        resizeButton->setText("▴");

        QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightCollapsed)+"px;}";
        codeEditor->setStyleSheet(style);
    } else
    {
        isExpanded = true;
        height = heightExpanded;

        layout2H->addWidget(replaceBox);
        layout2H->addWidget(replaceButton);
        layout3->addWidget(filePathLabel);
        layout->addWidget(moreButton);
        resizeButton->setText("▾");

         QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightExpanded)+"px;}";
        codeEditor->setStyleSheet(style);
    }
    codeEditor->setFooterHeight(height);
}

void FooterBarArea::setCursorInfoText(int charIndex, int lineIndex)
{
     cursorInfoLabel->setText(QString::number(charIndex)+":"+QString::number(lineIndex));
}

void FooterBarArea::codeCursorChanged()
{
    // text edit changed, update line number and char
    int lineNumber = codeEditor->textCursor().blockNumber() + 1;
    int charNumber = codeEditor->textCursor().columnNumber();
    setCursorInfoText(charNumber, lineNumber);
}

void FooterBarArea::replaceMatchedText()
{
    QRegExp exp(searchBox->text());
    codeEditor->replaceSearchMatchedText(replaceBox->text(), exp);
}


FooterBarArea::~FooterBarArea()
{

    delete layout3H;
    layout3H = NULL;
    delete layout3;
    layout3 = NULL;
    delete layout2H;
    layout2H = NULL;
    delete layout2;
    layout2 = NULL;
    delete layout;
    layout = NULL;

    delete comboBox;
    comboBox = NULL;
    delete searchBox;
    searchBox = NULL;
    delete filePathLabel;
    filePathLabel = NULL;
    delete resizeButton;
    resizeButton = NULL;
    delete replaceBox;
    replaceBox = NULL;
    delete cursorInfoLabel;
    cursorInfoLabel = NULL;
    delete moreButton;
    moreButton = NULL;
    delete replaceButton;
    replaceButton = NULL;
}

