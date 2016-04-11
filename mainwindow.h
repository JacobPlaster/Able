#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMenuBar>
#include <QMainWindow>
#include <QApplication>
#include "assetmanager.h"
#include "codeeditor.h"
#include "textedittabwidget.h"
#include "fileviewwidget.h"
#include "settingswindow.h"
#include "appconfigobject.h"


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0, QApplication *main = 0);
    void load(AssetManager *);
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    void resizeEvent(QResizeEvent* event);
    void loadMenuBar();
    void runUnitTests();
    void resizeWithFileView();
    void loadFileByName(QString &);

    AssetManager * assetManager;
    CodeEditor * codeEditor;
    TextEditTabWidget * textEditTab;
    FileViewWidget * fileView;
    int fileViewWidth;

    QMenuBar* menu_bar;

    QMenu* file_menu;
    QToolBar* file_toolbar;

    SettingsWindow * settingsWindow;
    QApplication * mainApp;
    void loadFileToTree(QString dir);

protected:
    void closeEvent(QCloseEvent *event) Q_DECL_OVERRIDE;

private slots:
    void loadFile();
    void loadFolder();
    void createFile();
    void saveFile();
    void on_welcomeOpenProjectButton_clicked();
    void on_welcomeCreateButton_clicked();
    void launchSettingsWindow();
    void updateSettings(AppConfigObject);
    void loadUserCfg();
};

#endif // MAINWINDOW_H
