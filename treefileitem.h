#ifndef TREEFILEITEM_H
#define TREEFILEITEM_H

#include <QTreeWidgetItem>
#include <QFileInfo>
class QWidget;

class TreeFileItem : public QTreeWidgetItem
{
public:
    TreeFileItem();
    void setFile(QFileInfo);
    QFileInfo getFile();
    void setIsFolder(const bool &);
    bool isFolder();

private:
    QFileInfo file;
    bool IsFolder;

};

#endif // TREEFILEITEM_H
