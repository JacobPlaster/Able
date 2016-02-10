#ifndef CODEEDITOR_H
#define CODEEDITOR_H

#include <QPlainTextEdit>
#include <QObject>
#include <QFileInfo>
#include <QCompleter>
#include <QStringListModel>

#include "syntaxhighlighter.h"
#include "assetmanager.h"

class QPaintEvent;
class QResizeEvent;
class QSize;
class QWidget;

class LineNumberArea;


class CodeEditor : public QPlainTextEdit
{
    Q_OBJECT

public:
    CodeEditor(QWidget *parent = 0);
    ~CodeEditor();

    void lineNumberAreaPaintEvent(QPaintEvent *event);
    void footerBarAreaPaintEvent(QPaintEvent *event);
    int lineNumberAreaWidth();
    bool loadFile(const QString &);
    QFileInfo * getCurrentFileInfo() const;
    void load(AssetManager *am);
    void save();
    void setAutoCompleter(QCompleter *inCompleter);
    QCompleter * getCompleter() const;
    QString textUnderCursor() const;
    void setAutoCompleteModel(QStringList &);

protected:
    void keyPressEvent(QKeyEvent *e) Q_DECL_OVERRIDE;
    void focusInEvent(QFocusEvent *e) Q_DECL_OVERRIDE;
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;

private slots:
    void updateLineNumberAreaWidth(int newBlockCount);
    void highlightAndSearchCurrentLine();
    void updateLineNumberArea(const QRect &, int);
    void insertCompletion(const QString &completion);

private:
    QWidget *lineNumberArea;
    QWidget *footerBarArea;
    QFileInfo *currentFile;
    SyntaxHighlighter *syntaxHighlighter;
    AssetManager *assetManager;
    QCompleter *completer;
    QStringListModel * autoCompleteModel;

    QStringList * dynamicAutocompleteSuggestions;
};


class LineNumberArea : public QWidget
{
public:
    LineNumberArea(CodeEditor *editor) : QWidget(editor) {
        codeEditor = editor;
    }

    QSize sizeHint() const Q_DECL_OVERRIDE {
        return QSize(codeEditor->lineNumberAreaWidth(), 0);
    }

protected:
    void paintEvent(QPaintEvent *event) Q_DECL_OVERRIDE {
        codeEditor->lineNumberAreaPaintEvent(event);
    }

private:
    CodeEditor *codeEditor;
};


class FooterBarArea : public QWidget
{
public:
    FooterBarArea(CodeEditor *editor) : QWidget(editor) {
        codeEditor = editor;
    }

protected:
    void paintEvent(QPaintEvent *event) Q_DECL_OVERRIDE {
        codeEditor->footerBarAreaPaintEvent(event);
    }

private:
    CodeEditor *codeEditor;
};



#endif
