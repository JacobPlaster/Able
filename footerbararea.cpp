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
    mainLayout->addLayout(layout2);
    layout3 = new QVBoxLayout();
    mainLayout->addLayout(layout3);
    layout3H = new QHBoxLayout();
    layout3->addLayout(layout3H);



    comboBox = new QComboBox();
    searchBox = new QLineEdit();
    filePathLabel = new QLabel();
    resizeButton = new QPushButton();
    replaceBox = new QLineEdit();
    cursorInfoLabel = new QLabel();

    languagesSupported = codeEditor->assetManager->getLoadedSupportFileNames();
    comboBox->addItems(languagesSupported);
    filePathLabel->setText(codeEditor->currentFile->absoluteFilePath());

    comboBox->setObjectName("footerComboBox");
    searchBox->setObjectName("footerSearchBox");
    searchBox->setPlaceholderText("RegExp search...");
    filePathLabel->setObjectName("footerFilePathLabel");
    filePathLabel->setAlignment(Qt::AlignRight);
    cursorInfoLabel->setObjectName("footerCursorInfoLabel");

    resizeButton->setObjectName("footerResizeButton");
    resizeButton->setText("▴");

    layout->setObjectName("footerComboBoxLayout");
    layout->addWidget(comboBox);

    layout2->setObjectName("footerComboBoxLayout2");
    layout2->addWidget(searchBox);

    layout3H->addWidget(cursorInfoLabel);
    layout3H->addWidget(resizeButton);
    layout3H->setAlignment(resizeButton, Qt::AlignRight);
    layout3H->setAlignment(cursorInfoLabel, Qt::AlignRight);

    //layout->setAlignment(comboBox, Qt::AlignLeft);
    //layout->setAlignment(searchBox, Qt::AlignLeft);
    //layout->setAlignment(filePathLabel, Qt::AlignRight);


    replaceBox->setPlaceholderText("Raplace with...");
    replaceBox->setObjectName("footerReplaceBox");
    cursorInfoLabel->setText("31:22");

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

void FooterBarArea::toggleResize()
{
    if(isExpanded)
    {
        isExpanded = false;
        height = heightCollapsed;

        layout2->removeWidget(replaceBox);
        layout3->removeWidget(filePathLabel);
        resizeButton->setText("▴");

        QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightCollapsed)+"px;}";
        codeEditor->setStyleSheet(style);
    } else
    {
        isExpanded = true;
        height = heightExpanded;

        layout2->addWidget(replaceBox);
        layout3->addWidget(filePathLabel);
        resizeButton->setText("▾");

         QString style = "QPlainTextEdit{padding-bottom: "+QString::number(heightExpanded)+"px;}";
        codeEditor->setStyleSheet(style);
    }
    codeEditor->setFooterHeight(height);
}


FooterBarArea::~FooterBarArea()
{

    delete layout3H;
    layout3H = NULL;
    delete layout3;
    layout3 = NULL;
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
}

