#ifndef ASSETMANAGER_H
#define ASSETMANAGER_H

#include <QFontDatabase>
#include <QDebug>
#include "syntaxhighlightingruleset.h"
#include <QFileInfoList>


class AssetManager
{
    public:
        AssetManager();
        QFont getFont(const std::string &font) const;
        QString getStyle(const std::string &style) const;
        void loadAssets();
        SyntaxHighlightingRuleSet * getLanguageSupportRuleSet(QString &language) const;
        QStringList getLoadedSupportFileNames();
        SyntaxHighlightingRuleSet * getLanguageSupportByName(QString &name);
        QFileInfoList getStyleSheets();
        QString loadStyleSheetByFilename(QString filename);
        void saveUserCfg(QStringList cfg);
        QStringList loadUserCfg();

    private:
        bool isLoaded;

        // fonts
        QFont srcCodeLightFont;
        QFont mainAppFont;

        // styles
        QString defaultStyle;
        QFileInfoList styleSheets;

        void loadAllLanguageSupport();
        void loadAllStyleSheets();
        void loadLanguageSupportFile(QTextStream &, const QString &);
        QString LIBS_FILEPATH;
        QString TMP_USER_CFG_FILEPATH;
        QVector<SyntaxHighlightingRuleSet*> syntaxHighlightingRules;

};

#endif // ASSETMANAGER_H
