#ifndef FILEVIEWWIDGET_H
#define FILEVIEWWIDGET_H

#include <QObject>
#include <QWidget>
#include <QTreeWidget>

class FileViewWidget : public QTreeWidget
{
public:
    FileViewWidget(QWidget *parent = 0);
    bool isProjectLoaded();

private:
    bool projectLoaded;

protected:
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;

};

#endif // FILEVIEWWIDGET_H
