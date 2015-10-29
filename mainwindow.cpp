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

    menu_bar = new QMenuBar(this);
    menu_bar->setNativeMenuBar(true);
    loadMenuBar();
}

void MainWindow::loadMenuBar()
{
    QMenu *oneMenu = new QMenu("File");

    oneMenu->addAction("New");
    QAction *fileAction = oneMenu->addAction("Open File");
    connect(fileAction, SIGNAL(triggered()), this, SLOT(loadFile()));

    oneMenu->addAction("Save");
    oneMenu->addAction("Save as");

    menu_bar->addAction(oneMenu->menuAction());
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

    ui->fileViewTreeBox->setAttribute(Qt::WA_MacShowFocusRect, 0);
    ui->fileViewTreeBox->setFont(QFont("Times", 20, QFont::Bold));

    // Create code edit tab area
    textEditTab->setTabShape(QTabWidget::Triangular);
    textEditTab->setDocumentMode(false);
    textEditTab->setMovable(true);
    textEditTab->setTabsClosable(true);
    ui->editArea->setWidget(textEditTab);

    runUnitTests();
}

MainWindow::~MainWindow()
{
    delete ui;
    delete textEditTab;
}

// Resize the code text area and fileListBox to fit the screen responsively
void MainWindow::resizeEvent(QResizeEvent *event)
{
    ui->fileViewTreeBox->resize(ui->fileViewTreeBox->width(), ui->centralWidget->height()+1);
    textEditTab->resize((ui->centralWidget->width() - ui->fileViewTreeBox->width()) +1, ui->centralWidget->height());
    ui->editArea->resize((ui->centralWidget->width() - ui->fileViewTreeBox->width()) +1, ui->centralWidget->height());
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
    string date_time_now = date_time_now = now.toString("dd.MM.yyyy h:m:s ap").toStdString();
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

    // open newly created file
    textEditTab->addCodeTab(date_time_now.c_str());
}


/*
    QMessageBox Msgbox;
    Msgbox.setText("Resized.");
    Msgbox.exec();
*/
