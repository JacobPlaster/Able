#include "Fileviewwidget.h"

FileViewWidget::FileViewWidget(QWidget *parent) : QTreeWidget(parent)
{
    projectLoaded = false;
}

bool FileViewWidget::isProjectLoaded()
{
    return projectLoaded;
}

void FileViewWidget::resizeEvent(QResizeEvent *e)
{
    QTreeWidget::resizeEvent(e);
}
