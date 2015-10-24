#ifndef MAINWINDOW_H
#define MAINWINDOW_H

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
    AssetManager * assetManager;
    CodeEditor * codeEditor;
    TextEditTabWidget * textEditTab;
};

#endif // MAINWINDOW_H
