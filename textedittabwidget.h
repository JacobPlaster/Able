#ifndef TEXTEDITTABWIDGET_H
#define TEXTEDITTABWIDGET_H

#include <QTabWidget>
#include <QObject>
#include "codeeditor.h"

class QPaintEvent;
class QResizeEvent;
class QSize;
class QWidget;


class TextEditTabWidget : public QTabWidget
{
    Q_OBJECT

public:
    TextEditTabWidget(QWidget *parent = 0);
    void addCodeTab(const QString &);

protected:
    void resizeEvent(QResizeEvent *event) Q_DECL_OVERRIDE;
};

#endif
