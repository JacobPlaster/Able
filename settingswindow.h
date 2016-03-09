#ifndef SETTINGSWINDOW_H
#define SETTINGSWINDOW_H

#include <QWidget>
#include <QFileInfoList>
#include <QDir>
#include <QApplication>

#include "assetmanager.h"

namespace Ui {
class SettingsWindow;
}

class SettingsWindow : public QWidget
{
    Q_OBJECT

public:
    explicit SettingsWindow(QWidget *parent = 0);
    ~SettingsWindow();
    void load(AssetManager *, QWidget *);

private slots:
    void themeChanged(int);

signals:
    void settingsChanged(QString styleSheet);

private:
    Ui::SettingsWindow *ui;
    AssetManager * assetManager;
    QFileInfoList styleSheets;
    QWidget * main;
};

#endif // SETTINGSWINDOW_H
