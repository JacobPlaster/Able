#ifndef SYNTAXHIGHLIGHTINGRULESET_H
#define SYNTAXHIGHLIGHTINGRULESET_H

#include <QTextCharFormat>

class SyntaxHighlightingRuleSet
{
public:
    SyntaxHighlightingRuleSet();

    struct HighlightingRule
    {
        QRegExp pattern;
        QTextCharFormat format;
    };
    QVector<HighlightingRule> highlightingRules;

    QStringList languageSupport;
    QStringList keywordPatterns;

    QRegExp commentStartExpression;
    QRegExp commentEndExpression;

    QTextCharFormat multiLineCommentFormat;
};

#endif // SYNTAXHIGHLIGHTINGRULESET_H
