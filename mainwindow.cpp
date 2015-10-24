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
    ui->fileViewTreeBox->setAttribute(Qt::WA_MacShowFocusRect, 0);
    ui->fileViewTreeBox->setFont(QFont("Times", 20, QFont::Bold));

    codeEditor = new CodeEditor();
    codeEditor->setTabStopWidth(40);
    codeEditor->setWordWrapMode(QTextOption::NoWrap);
     ui->codeAreaTabWidget->addTab(codeEditor, "Unkown");
}

void MainWindow::load(AssetManager *inAssetManager)
{
    assetManager = inAssetManager;

 //   ui->codeEditorArea->setFont(assetManager->getFont("MAIN_CODE_FONT"));
}

MainWindow::~MainWindow()
{
    delete ui;
    delete codeEditor;
}

// Resize the code text area and fileListBox to fit the screen responsively
void MainWindow::resizeEvent(QResizeEvent *event)
{
    ui->fileViewTreeBox->resize(ui->fileViewTreeBox->width(), ui->centralWidget->height()+1);
    ui->codeAreaTabWidget->resize(ui->centralWidget->width() - ui->fileViewTreeBox->width()+1, ui->centralWidget->height()+1);
    codeEditor->resize(ui->codeAreaTabWidget->width(), ui->codeAreaTabWidget->height()-24);
}


/*
    QMessageBox Msgbox;
    Msgbox.setText("Resized.");
    Msgbox.exec();
*/
