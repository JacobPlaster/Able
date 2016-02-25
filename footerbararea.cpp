#include "footerbararea.h"

FooterBarArea::FooterBarArea(CodeEditor *editor) : QWidget(editor)
{
    codeEditor = editor;
    this->setObjectName("codeEditorFooter");

    QVBoxLayout *mainLayout = new QVBoxLayout();
    this->setLayout(mainLayout);
    QHBoxLayout *layout = new QHBoxLayout();
    mainLayout->addLayout(layout);
    QHBoxLayout *layout2 = new QHBoxLayout();
    //mainLayout->addLayout(layout2);


    comboBox = new QComboBox();
    searchBox = new QLineEdit();
    filePathLabel = new QLabel();
    resizeButton = new QPushButton();
    replaceBox = new QLineEdit();

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

    layout2->addWidget(replaceBox);
    layout2->setAlignment(replaceBox, Qt::AlignLeft);
    layout2->setObjectName("footerComboBoxLayout2");
    replaceBox->setPlaceholderText("Raplace with...");

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


FooterBarArea::~FooterBarArea()
{

}

