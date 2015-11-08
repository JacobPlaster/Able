#include "fileviewwidget.h"
#include <QMessageBox>
#include <QDir>
#include <QDebug>

FileViewWidget::FileViewWidget(QWidget *parent) : QTreeWidget(parent)
{
    projectLoaded = false;
    this->setHeaderHidden(true);

    connect(this, SIGNAL(doubleClicked(const QModelIndex&)), this, SLOT(loadToEditor(const QModelIndex&)));
}

void FileViewWidget::loadToEditor(const QModelIndex &index)
{
    for(int i = 0; i < fileItems.size(); i++)
    {
        if(fileItems[i]->isSelected())
        {
            QFileInfo file = fileItems[i]->getFile();
            tabWidget->addCodeTab(file.absoluteFilePath());
        }
    }
}

bool FileViewWidget::isProjectLoaded()
{
    return projectLoaded;
}

void FileViewWidget::resizeEvent(QResizeEvent *e)
{
    QTreeWidget::resizeEvent(e);
}

void FileViewWidget::load(AssetManager * am, TextEditTabWidget * tw)
{
    assetManager = am;
    tabWidget = tw;
}

void FileViewWidget::loadFolder(const QString &fileString)
{
    // create top level
    TreeFileItem * parent = new TreeFileItem();
    parent->setIsFolder(true);

    QFileInfo fInfo(fileString);
    parent->setText(0, fInfo.fileName());

    this->addTopLevelItem(parent);
    attachDir(parent, fileString);

    projectLoaded = true;
}

void FileViewWidget::attachDir(QTreeWidgetItem * parent, const QString &fileString)
{
    QDir lsDir(fileString);
    QFileInfoList allLs = lsDir.entryInfoList();
    foreach (QFileInfo fileI, allLs){
        TreeFileItem * tmp = new TreeFileItem();
        if (!fileI.isDir())
        {
            // is a file
            tmp->setText(0, fileI.fileName());
            tmp->setFile(fileI);
            parent->addChild(tmp);
            fileItems.append(tmp);
        } else
        {
            // is a folder
            // remove base dirs and backtracks (otherwise stuck in endless loop)
            if(fileI.fileName() != "." && fileI.fileName() != "..")
            {
                // if folder contains files
                if(QDir(fileI.absoluteFilePath()).count() > 2)
                {
                    tmp->setText(0, fileI.fileName());
                    tmp->setFile(fileI);
                    tmp->setIsFolder(true);
                    parent->addChild(tmp);
                    // recurse
                    attachDir(tmp, fileI.absoluteFilePath());
                }
            }
        }
    }
}
