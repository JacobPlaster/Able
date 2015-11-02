#ifndef FILEVIEWWIDGET_H
#define FILEVIEWWIDGET_H

#include <QObject>
#include <QWidget>
#include <QTreeWidget>

#include "assetmanager.h"
#include "textedittabwidget.h"
#include "treefileitem.h"

class FileViewWidget : public QTreeWidget
{
    Q_OBJECT
public:
    FileViewWidget(QWidget *parent = 0);
    bool isProjectLoaded();
    void loadFolder(const QString &);
    void load(AssetManager *, TextEditTabWidget *);

    void attachDir(QTreeWidgetItem * parent, const QString &);

private:
    bool projectLoaded;
    AssetManager * assetManager;
    TextEditTabWidget * tabWidget;
    QVector<TreeFileItem*> fileItems;

private slots:
    void loadToEditor(const QModelIndex&);

protected:
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;

};

#endif // FILEVIEWWIDGET_H
