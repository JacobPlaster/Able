#include "treefileitem.h"


TreeFileItem::TreeFileItem() : QTreeWidgetItem(0)
{
    IsFolder = false;
}

void TreeFileItem::setFile(QFileInfo inFile)
{
    file = inFile;
}

QFileInfo TreeFileItem::getFile()
{
    return file;
}

void TreeFileItem::setIsFolder(const bool &in)
{
    IsFolder = in;
}

bool TreeFileItem::isFolder()
{
    return IsFolder;
}
