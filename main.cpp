#include "mainwindow.h"
#include "assetmanager.h"
#include "codeeditor.h"

#include <QApplication>
#include <QDir>
#include <QFontDatabase>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    // load assets in manager
    AssetManager *am = new AssetManager();
    am->loadAssets();

    // Load files into app
    a.setStyleSheet(am->getStyle("MAIN_STYLE"));
    a.setFont(am->getFont("MAIN_CODE_FONT"));


    // create main window
    MainWindow w;
    w.setWindowTitle("Able");
    // pass asset manager into load
    w.load(am);
    w.show();

    return a.exec();
}
