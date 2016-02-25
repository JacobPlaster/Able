#include "syntaxhighlighter.h"

SyntaxHighlighter::SyntaxHighlighter(QTextDocument* document): QSyntaxHighlighter(document)
{
    languageSet = false;
    searchExpression = QRegExp("");
}

void SyntaxHighlighter::load(AssetManager *am, QString lan)
{
    assetManager = am;

    ruleSet = am->getLanguageSupportRuleSet(lan);
    if(ruleSet != NULL)
    {
        languageSet = true;
        autoCompleteSuggestions += ruleSet->getConstantKeywords();
    }
}

void SyntaxHighlighter::setSyntaxHighlightingRules(SyntaxHighlightingRuleSet * in)
{
    ruleSet = in;
}

SyntaxHighlightingRuleSet * SyntaxHighlighter::getRuleSet() const
{
    return ruleSet;
}

void SyntaxHighlighter::highlightMatch(const QString &text, QRegExp &exp)
{
    searchExpression = exp;
    this->rehighlight();
}

void SyntaxHighlighter::highlightBlock(const QString &text)
{
    if(languageSet)
    {
        // stops syntax highlight code running when searching for reg exp query
        // search line for any autocomplete suggestions
        autoCompleteSuggestions += searchInputForAutocompleteRules(text);

        foreach (const SyntaxHighlightingRuleSet::HighlightingRule rule, ruleSet->highlightingRules) {
            QRegExp expression(rule.pattern);
            int index = expression.indexIn(text);
            while (index >= 0) {
                int length = expression.matchedLength();
                setFormat(index, length, rule.format);
                index = expression.indexIn(text, index + length);
            }
        }
        setCurrentBlockState(0);

        int startIndex = 0;
        if (previousBlockState() != 1)
            startIndex = ruleSet->commentStartExpression.indexIn(text);

        while (startIndex >= 0) {
            int endIndex = ruleSet->commentEndExpression.indexIn(text, startIndex);
            int commentLength;
            if (endIndex == -1) {
                setCurrentBlockState(1);
                commentLength = text.length() - startIndex;
            } else {
                commentLength = endIndex - startIndex
                        + ruleSet->commentEndExpression.matchedLength();
            }
            setFormat(startIndex, commentLength, ruleSet->multiLineCommentFormat);
            startIndex = ruleSet->commentStartExpression.indexIn(text, startIndex + commentLength);
        }


        // if the search box is populated with an expression
        if(searchExpression.pattern() != "")
        {
            highlightSearchExpression(searchExpression, text);
        }
    }
}

void SyntaxHighlighter::highlightSearchExpression(QRegExp expr, const QString &text)
{
    QTextCharFormat fmt;
    fmt.setBackground(Qt::yellow);

    if(expr.pattern() != "")
    {
        int index = expr.indexIn(text);
        while(index >= 0)
        {

            int length = expr.matchedLength();
            this->setFormat(index, length, fmt);
            index = expr.indexIn(text, index+length);
        }
    }
}

 QStringList SyntaxHighlighter::searchInputForAutocompleteRules(const QString &text)
 {
    QStringList suggestions;

    QRegExp rx(ruleSet->autocorrectFormat);
    QRegExp rxTrimmer(ruleSet->autocorrectTrimFormat);

    int pos = rx.indexIn(text);
    if(pos != -1)
    {
        QStringList suggestionsUnTrimmed = rx.capturedTexts();
        for(int i = 0; i < suggestionsUnTrimmed.length(); i++)
        {
            int pos2 = rxTrimmer.indexIn(suggestionsUnTrimmed[i]);
            if(pos2 != -1 && !autoCompleteSuggestions.contains(rxTrimmer.capturedTexts()[0]))
                suggestions << rxTrimmer.capturedTexts()[0];
        }
    }
    return suggestions;
 }

 QStringList & SyntaxHighlighter::getAutoCompleteRules()
 {
     return autoCompleteSuggestions;
 }

SyntaxHighlighter::~SyntaxHighlighter()
{

}
