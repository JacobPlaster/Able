#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "assetmanager.h"

#include <QMessageBox>
#include <QtGui>
#include <QSplitter>
#include <QFontDatabase>
#include <QHBoxLayout>
#include <QFileDialog>

#include <string>
#include <sstream>

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

    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/mainwindow.cpp");
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/codeeditor.h");
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/simic_website_2/index.html");
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


/*
    QMessageBox Msgbox;
    Msgbox.setText("Resized.");
    Msgbox.exec();
*/
