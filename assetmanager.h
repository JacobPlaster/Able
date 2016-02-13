#ifndef ASSETMANAGER_H
#define ASSETMANAGER_H

#include <QFontDatabase>
#include <QDebug>
#include "syntaxhighlightingruleset.h"


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

    private:
        bool isLoaded;

        // fonts
        QFont srcCodeLightFont;
        QFont mainAppFont;

        // styles
        QString defaultStyle;

        void loadAllLanguageSupport();
        void loadLanguageSupportFile(QTextStream &, const QString &);
        QString LIBS_FILEPATH;
        QVector<SyntaxHighlightingRuleSet*> syntaxHighlightingRules;

};

#endif // ASSETMANAGER_H
