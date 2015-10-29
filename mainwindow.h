#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMenuBar>
#include <QMainWindow>
#include "assetmanager.h"
#include "codeeditor.h"
#include "textedittabwidget.h"


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    void load(AssetManager *inAssetManager);
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    void resizeEvent(QResizeEvent* event);
    void loadMenuBar();
    void runUnitTests();

    AssetManager * assetManager;
    CodeEditor * codeEditor;
    TextEditTabWidget * textEditTab;

    QMenuBar* menu_bar;

    QMenu* file_menu;
    QToolBar* file_toolbar;

private slots:
    void loadFile();
};

#endif // MAINWINDOW_H
