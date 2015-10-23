#include "assetmanager.h"
#include <QFile>

AssetManager::AssetManager()
{

}

void AssetManager::loadAssets()
{
    isLoaded = true;

    // load default font
    int id1 = QFontDatabase::addApplicationFont("/Users/jacobplaster/Documents/Able/libs/fonts/SourceCodePro-Light.ttf");
    QString srcCodeLightFontString = QFontDatabase::applicationFontFamilies(id1).at(0);
    srcCodeLightFont = srcCodeLightFontString;

    // load css stylesheets
    QFile styleFile("/Users/jacobplaster/Documents/Able/libs/css/default.qss");
    if (!styleFile.exists()) {
            qDebug() << "File " << styleFile.fileName() << " does not exist.";
    }
    styleFile.open( QFile::ReadOnly );
    // Apply the loaded stylesheet
    defaultStyle = styleFile.readAll();
}

QString AssetManager::getStyle(const std::string &style) const
{
    if(isLoaded)
    {
        if(style == "MAIN_STYLE")
        {
            return defaultStyle;
        }
    }

    return defaultStyle;
}

QFont AssetManager::getFont(const std::string &font) const
{
    if(isLoaded)
    {
        if(font == "MAIN_CODE_FONT")
            return srcCodeLightFont;

        qDebug() << "Success, loading font: ";
    } else
    {
        qDebug() << "Error: Attempting to load un-loaded assets. With font: ";
    }

    return srcCodeLightFont;
}

