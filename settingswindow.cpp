#include "settingswindow.h"
#include "ui_settingswindow.h"

SettingsWindow::SettingsWindow(QWidget * parent) :
    QWidget(parent),
    ui(new Ui::SettingsWindow)
{
    ui->setupUi(this);
}

void SettingsWindow::load(AssetManager *am, QWidget * mainWindow)
{
    main = mainWindow;
    assetManager = am;
    //ui->settingsThemeComboBox->addItem("Test");

    styleSheets = assetManager->getStyleSheets();

    // put stylessheets into combo box
    foreach (QFileInfo fileI, styleSheets)
    {
        ui->settingsThemeComboBox->addItem(fileI.baseName());
    }
    connect(ui->settingsThemeComboBox, SIGNAL(currentIndexChanged(int)), this, SLOT(themeChanged(int)));
}

void SettingsWindow::themeChanged(int i)
{
   // qDebug() << styleSheets[i].absoluteFilePath();

    emit settingsChanged(styleSheets[i].absoluteFilePath());
    //mainApp->setStyleSheet(styleSheets[i].absoluteFilePath());
}

SettingsWindow::~SettingsWindow()
{
    delete ui;
}
