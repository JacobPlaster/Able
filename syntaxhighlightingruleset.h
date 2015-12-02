#ifndef SYNTAXHIGHLIGHTINGRULESET_H
#define SYNTAXHIGHLIGHTINGRULESET_H

#include <QTextCharFormat>

class SyntaxHighlightingRuleSet
{
public:
    SyntaxHighlightingRuleSet();
    ~SyntaxHighlightingRuleSet();
    QStringList & getConstantKeywords();

    struct HighlightingRule
    {
        QRegExp pattern;
        QTextCharFormat format;
    };
    QVector<HighlightingRule> highlightingRules;

    QStringList languageSupport;
    QStringList keywordPatterns;
    QStringList constantKeywords;

    QRegExp commentStartExpression;
    QRegExp commentEndExpression;

    QRegExp autocorrectFormat;

    QTextCharFormat multiLineCommentFormat;
};

#endif // SYNTAXHIGHLIGHTINGRULESET_H
