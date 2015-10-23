#ifndef ASSETMANAGER_H
#define ASSETMANAGER_H

#include <QFontDatabase>
#include <QDebug>


class AssetManager
{
    public:
        AssetManager();
        QFont getFont(const std::string &font) const;
        QString getStyle(const std::string &style) const;
        void loadAssets();
    private:
        bool isLoaded;

        // fonts
        QFont srcCodeLightFont;

        // styles
        QString defaultStyle;
};

#endif // ASSETMANAGER_H
