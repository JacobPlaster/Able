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
       SyntaxHighlightingRuleSet * getRuleSet() const;
       QStringList & getAutoCompleteRules();
       void setSyntaxHighlightingRules(SyntaxHighlightingRuleSet *);

        SyntaxHighlightingRuleSet *ruleSet;

    private:
       AssetManager * assetManager;
       QStringList autoCompleteSuggestions;

       QStringList searchInputForAutocompleteRules(const QString &text);

       bool languageSet;
};

#endif // SYNTAXHIGHLIGHTER_H
