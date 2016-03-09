#ifndef APPCONFIGOBJECT_H
#define APPCONFIGOBJECT_H

#include <QString>

class AppConfigObject
{
public:
    AppConfigObject()
    {
        styleSheetLocation = "";
    }

    QString getStyleSheetLocation()
    {
        return styleSheetLocation;
    }
    void setStyleSheetLocation(QString loc)
    {
        this->styleSheetLocation = loc;
    }

private:
    QString styleSheetLocation;
};

#endif // APPCONFIGOBJECT_H
