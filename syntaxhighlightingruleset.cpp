#include "syntaxhighlightingruleset.h"

SyntaxHighlightingRuleSet::SyntaxHighlightingRuleSet()
{
    // defautl colors if not set in language support
    lineHighlightColor = "#EEE";
    searchHighlightColor = "#F3F709";
    searchHighlightColorForeground = "#000000";
}

QStringList & SyntaxHighlightingRuleSet::getConstantKeywords()
{
    return constantKeywords;
}

SyntaxHighlightingRuleSet::~SyntaxHighlightingRuleSet()
{
}
