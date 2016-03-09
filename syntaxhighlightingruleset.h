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
    QString fileName;

    QRegExp commentStartExpression;
    QRegExp commentEndExpression;

    QRegExp autocorrectFormat;
    QRegExp autocorrectTrimFormat;

    QTextCharFormat multiLineCommentFormat;

    QString lineHighlightColor;
    QString searchHighlightColor;
    QString searchHighlightColorForeground;
};

#endif // SYNTAXHIGHLIGHTINGRULESET_H
