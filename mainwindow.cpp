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

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
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
    oneMenu->addAction("New File");
    QAction *fileOpenAction = oneMenu->addAction("Open File");
    connect(fileOpenAction, SIGNAL(triggered()), this, SLOT(loadFile()));
    QAction *folderOpenAction = oneMenu->addAction("Open Folder");
    connect(folderOpenAction, SIGNAL(triggered()), this, SLOT(loadFolder()));

    oneMenu->addAction("Save");
    oneMenu->addAction("Save as");

    menu_bar->addAction(oneMenu->menuAction());
}

void MainWindow::loadFolder()
{
    QString dir = QFileDialog::getExistingDirectory(this, tr("Open Directory"),
                                                 "/home",
                                                 QFileDialog::ShowDirsOnly
                                                 | QFileDialog::DontResolveSymlinks);
    fileView->loadFolder(dir);
    // resize to fit with folder view
    resizeWithFileView();
}

void MainWindow::loadFile()
{
    QString fileName = QFileDialog::getOpenFileName(this, tr("Open File"),
                                                    "/home/documents");
    textEditTab->addCodeTab(fileName);
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

    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/bootstrap.css");
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/libs/tests/TestDatasets/team.html");
    //runUnitTests();
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
            qDebug() << fileI.absoluteFilePath();
            testFile << fileI.completeSuffix().toStdString() << " test: \n";
            testFile << "File: " << fileI.absoluteFilePath().toStdString() << "\n";
            timer.restart();
            textEditTab->addCodeTab(fileI.absoluteFilePath());
            testFile << "Time to load and process: " << timer.elapsed() << " milliseconds \n";
            testFile << "File size: " << fileI.size() << "bytes";
            testFile << "\n\n\n";
        }
    }
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
