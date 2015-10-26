#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "assetmanager.h"

#include <QMessageBox>
#include <QtGui>
#include <QSplitter>
#include <QFontDatabase>
#include <QHBoxLayout>

#include <string>
#include <sstream>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    textEditTab = new TextEditTabWidget();
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
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/assetmanager.cpp");
    textEditTab->addCodeTab("/Users/jacobplaster/Documents/Able/codeeditor.h");
}

MainWindow::~MainWindow()
{
    delete ui;
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
