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
    textedittabwidget.cpp

HEADERS  += mainwindow.h \
    assetmanager.h \
    codeeditor.h \
    textedittabwidget.h

FORMS    += mainwindow.ui