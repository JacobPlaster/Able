#-------------------------------------------------
#
# Project created by QtCreator 2015-10-16T15:19:31
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Able
TEMPLATE = app

SOURCES += main.cpp\
        mainwindow.cpp \
    assetmanager.cpp \
    codeeditor.cpp \
    textedittabwidget.cpp \
    syntaxhighlighter.cpp \
    syntaxhighlightingruleset.cpp \
    fileviewwidget.cpp \
    treefileitem.cpp \
    footerbararea.cpp \
    settingswindow.cpp

HEADERS  += mainwindow.h \
    assetmanager.h \
    codeeditor.h \
    textedittabwidget.h \
    syntaxhighlighter.h \
    syntaxhighlightingruleset.h \
    fileviewwidget.h \
    treefileitem.h \
    footerbararea.h \
    settingswindow.h

FORMS    += mainwindow.ui \
    settingswindow.ui

RESOURCES += \
    resources.qrc
