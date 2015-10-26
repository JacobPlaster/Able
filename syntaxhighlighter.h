#ifndef SYNTAXHIGHLIGHTER_H
#define SYNTAXHIGHLIGHTER_H

#include <QSyntaxHighlighter>

class SyntaxHighlighter: public QSyntaxHighlighter
{
    public:
       SyntaxHighlighter(QTextDocument* document);
       ~SyntaxHighlighter();

       void highlightBlock(const QString &text);

    private:
       struct HighlightingRule
       {
           QRegExp pattern;
           QTextCharFormat format;
       };
       QVector<HighlightingRule> highlightingRules;

       QRegExp commentStartExpression;
       QRegExp commentEndExpression;

       QTextCharFormat keywordFormat;
       QTextCharFormat classFormat;
       QTextCharFormat numberFormat;
       QTextCharFormat singleLineCommentFormat;
       QTextCharFormat multiLineCommentFormat;
       QTextCharFormat quotationFormat;
       QTextCharFormat functionFormat;
};

#endif // SYNTAXHIGHLIGHTER_H
