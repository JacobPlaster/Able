#include "syntaxhighlighter.h"

SyntaxHighlighter::SyntaxHighlighter(QTextDocument* document): QSyntaxHighlighter(document)
{
    languageSet = false;
}

void SyntaxHighlighter::load(AssetManager *am, QString lan)
{
    assetManager = am;
    ruleSet = am->getLanguageSupportRuleSet(lan);
    if(ruleSet != NULL)
        languageSet = true;
}

void SyntaxHighlighter::highlightBlock(const QString &text)
{
    if(languageSet)
    {
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
    }
}

SyntaxHighlighter::~SyntaxHighlighter()
{

}
