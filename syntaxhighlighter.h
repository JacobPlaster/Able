#ifndef SYNTAXHIGHLIGHTER_H
#define SYNTAXHIGHLIGHTER_H

#include <QSyntaxHighlighter>
#include "assetmanager.h"

class SyntaxHighlighter: public QSyntaxHighlighter
{
    public:
       SyntaxHighlighter(QTextDocument* document);
       ~SyntaxHighlighter();

       void highlightBlock(const QString &text);
       void load(AssetManager *am, QString lan);

    private:
       AssetManager *assetManager;
       SyntaxHighlightingRuleSet *ruleSet;

       bool languageSet;
};

#endif // SYNTAXHIGHLIGHTER_H
