#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "assetmanager.h"

#include <QMessageBox>
#include <QtGui>
#include <QSplitter>
#include <QFontDatabase>
#include <QHBoxLayout>
#include <QFileDialog>
#include <QDateTime>

#include <string>
#include <sstream>
#include <iostream>
#include <fstream>
using namespace std;

MainWindow::MainWindow(QWidget *parent, QApplication *main) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    mainApp(main)
{
    ui->setupUi(this);
    textEditTab = new TextEditTabWidget();
    fileView = new FileViewWidget();
    loadMenuBar();
}

void MainWindow::loadMenuBar()
{
    menu_bar = new QMenuBar(this);
    menu_bar->setNativeMenuBar(true);

    QMenu *oneMenu = new QMenu("File");
    QAction *newFileLaunch = oneMenu->addAction("New File");
    connect(newFileLaunch, SIGNAL(triggered()), this, SLOT(createFile()));
    QAction *fileOpenAction = oneMenu->addAction("Open File");
    connect(fileOpenAction, SIGNAL(triggered()), this, SLOT(loadFile()));
    QAction *folderOpenAction = oneMenu->addAction("Open Folder");
    connect(folderOpenAction, SIGNAL(triggered()), this, SLOT(loadFolder()));

    QAction *fileSaveAction = oneMenu->addAction("Save");
    connect(fileSaveAction, SIGNAL(triggered()), this, SLOT(saveFile()));

    QMenu *windowMenu = new QMenu("Window");
    QAction *launchSettingsAction = windowMenu->addAction("More");
    connect(launchSettingsAction, SIGNAL(triggered()), this, SLOT(launchSettingsWindow()));


    menu_bar->addMenu(oneMenu);
    menu_bar->addMenu(windowMenu);
}

void MainWindow::launchSettingsWindow()
{
    settingsWindow = new SettingsWindow();
    settingsWindow->load(assetManager, this);
    connect(settingsWindow, SIGNAL(settingsChanged(AppConfigObject)),
                         this, SLOT(updateSettings(AppConfigObject)));
    settingsWindow->show();
}

void MainWindow::updateSettings(AppConfigObject cfg)
{
    mainApp->setStyleSheet(assetManager->loadStyleSheetByFilename(cfg.getStyleSheetLocation()));
}

void MainWindow::saveFile()
{
    // saves the current open editor to its filepath
    textEditTab->saveCurrentEditor();
}

void MainWindow::loadFolder()
{
    QString dir = QFileDialog::getExistingDirectory(this, tr("Open Directory"),
                                                 "/home",
                                                 QFileDialog::ShowDirsOnly
                                                 | QFileDialog::DontResolveSymlinks);
    loadFileToTree(dir);
}

void MainWindow::loadFileToTree(QString dir)
{
    if(dir != "")
    {
        fileView->loadFolder(dir);
        // resize to fit with folder view
        resizeWithFileView();
        ui->welcomeScreen->hide();
    }
}

void MainWindow::createFile()
{
    QString fileName = QFileDialog::getSaveFileName(this, tr("Save File"),
                                                    "/home/documents");
    QFile file( fileName );
    file.open( QIODevice::WriteOnly );
    file.close();
    loadFileByName(fileName);

}

void MainWindow::loadFileByName(QString &name)
{
    textEditTab->addCodeTab(name);
    ui->welcomeScreen->hide();
}


void MainWindow::loadFile()
{
    QString fileName = QFileDialog::getOpenFileName(this, tr("Open File"),
                                                    "/home/documents");
   if(fileName != "")
    {
        loadFileByName(fileName);
    }
}

void MainWindow::load(AssetManager *inAssetManager)
{
    // load static files
    assetManager = inAssetManager;
    textEditTab->load(assetManager);
    fileView->load(assetManager, textEditTab);

    // Create code edit tab area
    textEditTab->setTabShape(QTabWidget::Triangular);
    textEditTab->setDocumentMode(false);
    textEditTab->setMovable(true);
    textEditTab->setTabsClosable(true);
    ui->editArea->setWidget(textEditTab);

    ui->projectViewArea->setWidget(fileView);
    fileViewWidth = ui->projectViewArea->width();

    //fileView->loadFolder("/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets");
    //ui->welcomeScreen->hide();
    //runUnitTests();

    if(textEditTab->numOfTabsOpen != 0)
    {
        ui->welcomeScreen->hide();
    }

    loadUserCfg();
}

void MainWindow::loadUserCfg()
{
    QStringList cfg = assetManager->loadUserCfg();
    for(int i = 0; i < cfg.length(); i++)
    {
        if(i-1 >= 0)
        {
            // if load tab command found
            if(cfg[i-1] == "CT:")
            {
                loadFileByName(cfg[i]);
            }
            // if load file directory found
            if(cfg[i-1] == "PD:")
            {
                loadFileToTree(cfg[i]);
            }
        }
    }
}

void MainWindow::closeEvent(QCloseEvent *event)
{
    QStringList userCfg;
    // save user details here
    for(int i= 0; i < textEditTab->numOfTabsOpen; i++)
    {
        userCfg << "CT:" << textEditTab->getEditorAtIndex(i)->getCurrentFileInfo()->absoluteFilePath();
    }
    QStringList openDirs = fileView->getMainParentFolders();
    for(int i2 = 0; i2 < openDirs.length(); i2++)
    {
        userCfg << "PD:" << openDirs[i2];
    }
    assetManager->saveUserCfg(userCfg);
}

MainWindow::~MainWindow()
{
    delete ui;
    delete textEditTab;
    textEditTab = NULL;
    delete fileView;
    fileView = NULL;
}

// Resize the code text area and fileListBox to fit the screen responsively
void MainWindow::resizeEvent(QResizeEvent *event)
{
    // project loaded into file view
    if(fileView->isProjectLoaded())
    {
        resizeWithFileView();
    } else
    {
        ui->projectViewArea->resize(0, ui->centralWidget->height());
        fileView->resize(0, ui->projectViewArea->height());
        ui->editArea->move(0, 0);
        ui->editArea->resize(ui->centralWidget->width(), ui->centralWidget->height());
        textEditTab->resize(ui->editArea->width(), ui->centralWidget->height());

        ui->welcomeScreen->resize(ui->centralWidget->width(), ui->centralWidget->height()+30);
        ui->welcomeLabel->resize(ui->welcomeScreen->width(), ui->welcomeLabel->height());
        ui->welcomeButtonWidget->resize(ui->welcomeScreen->width(), ui->welcomeButtonWidget->height());
        ui->welcomeLabelLogo->resize(ui->welcomeScreen->width(), ui->welcomeLabelLogo->height());
    }
}


void MainWindow::runUnitTests()
{
    // create timer
    QElapsedTimer timer;
    // current dat/time for saving
    QDateTime now = QDateTime::currentDateTime();
    timer.start();
    // open file
    ofstream testFile;
    // get current time to print ot file name
    string date_time_now = now.toString("dd.MM.yyyy h:m:s ap").toStdString();
    date_time_now = "/Users/jacobplaster/Documents/Able/libs/tests/speedTests/speedTest-(" + date_time_now + ").txt";
    testFile.open (date_time_now.c_str());

    QStringList dirsToTest;
    dirsToTest << "/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/Zompage-Game";
    dirsToTest << "/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/Nether-Game";
    dirsToTest << "/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/ACW-08338-Student";
    dirsToTest << "/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/folder3";
    dirsToTest << "/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/folder4";

    // load initial tab so all resources get initiated
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/mainwindow.cpp");

    testFile << date_time_now << "\n";


    testFile << "------ SPEED TESTING FOR LOADING FUNCTIONS ------\n\n\n";
    // Run tests on all files in this dir
    QDir lsDir("/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets");
    QFileInfoList allLs = lsDir.entryInfoList();
    foreach (QFileInfo fileI, allLs){
        // if is a file and is a cfg file
        if (!fileI.isDir())
        {
            qDebug() << "Loading: " << fileI.absoluteFilePath();
            testFile << fileI.completeSuffix().toStdString() << " test: \n";
            testFile << "File: " << fileI.absoluteFilePath().toStdString() << "\n";
            timer.restart();
            textEditTab->addCodeTab(fileI.absoluteFilePath());
            testFile << "Time to load and process: " << timer.elapsed() << " milliseconds \n";
            testFile << "File size: " << fileI.size() << "bytes";
            testFile << "\n\n\n";
        }
    }

    testFile << "------ SPEED TESTING FOR LOADING FOLDERS ------\n\n\n";
    foreach(QFileInfo fileI, dirsToTest)
    {
        qDebug() << "Loading: " << fileI.absoluteFilePath();
        testFile << fileI.fileName().toStdString() << " test: \n";
        testFile << "File: " << fileI.absoluteFilePath().toStdString() << "\n";
        timer.restart();
        fileView->loadFolder(fileI.absoluteFilePath());
        testFile << "Time to load directory: " << timer.elapsed() << " milliseconds \n";
        testFile << "Folder size: " << fileI.size() << "bytes";
        testFile << "\n\n\n";
    }

    testFile.flush();
    testFile.close();

    textEditTab->clear();
    // open newly created file
    textEditTab->addCodeTab(date_time_now.c_str());
}

void MainWindow::resizeWithFileView()
{
    ui->projectViewArea->resize(fileViewWidth, ui->centralWidget->height());

    fileView->resize(ui->projectViewArea->width(), ui->projectViewArea->height());
    ui->editArea->move(ui->projectViewArea->width(), 0);
    ui->editArea->resize((ui->centralWidget->width() - ui->projectViewArea->width()) +1, ui->centralWidget->height());
    textEditTab->resize(ui->editArea->width(), ui->centralWidget->height());

     fileViewWidth = ui->projectViewArea->width();
}

/*
    QMessageBox Msgbox;
    Msgbox.setText("Resized.");
    Msgbox.exec();
*/

void MainWindow::on_welcomeCreateButton_clicked()
{
    createFile();
}

void MainWindow::on_welcomeOpenProjectButton_clicked()
{
    loadFolder();
}
