#ifndef TEXTEDITTABWIDGET_H
#define TEXTEDITTABWIDGET_H

#include <QTabWidget>
#include <QObject>
#include "codeeditor.h"
#include "assetmanager.h"

class QPaintEvent;
class QResizeEvent;
class QSize;
class QWidget;


class TextEditTabWidget : public QTabWidget
{
    Q_OBJECT

public:
    TextEditTabWidget(QWidget *parent = 0);
    ~TextEditTabWidget();
    bool addCodeTab(const QString &);
    void load(AssetManager *inAssetManager);
    void saveCurrentEditor();
    CodeEditor * getEditorAtIndex(int i) const;
    QList<CodeEditor *> getAllEditors() const;
    int numOfTabsOpen = 0;

private:
    AssetManager *assetManager;

protected:
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;

private slots:
    void closeTab(int);

};

#endif
