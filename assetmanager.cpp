#include "assetmanager.h"
#include <QFile>
#include <QDir>
#include <QFileInfo>
#include <QMessageBox>

AssetManager::AssetManager()
{
    LIBS_FILEPATH = "/Users/jacobplaster/Documents/Able/libs";
}

void AssetManager::loadAssets()
{
    isLoaded = true;

    // load default font
    int id1 = QFontDatabase::addApplicationFont(LIBS_FILEPATH + "/fonts/SourceCodePro-Regular.ttf");
    srcCodeLightFont = QFontDatabase::applicationFontFamilies(id1).at(0);

    int id2 = QFontDatabase::addApplicationFont(LIBS_FILEPATH + "/fonts/Muli-Light.ttf");
    mainAppFont = QFontDatabase::applicationFontFamilies(id2).at(0);

    // load css stylesheets
    QFile styleFile(LIBS_FILEPATH + "/css/default.qss");
    //QFile styleFile(LIBS_FILEPATH + "/css/dark.qss");
    if (!styleFile.exists()) {
            qDebug() << "File " << styleFile.fileName() << " does not exist.";
    }
    styleFile.open( QFile::ReadOnly );
    // Apply the loaded stylesheet
    defaultStyle = styleFile.readAll();
    styleFile.close();

    loadAllLanguageSupport();
    loadAllStyleSheets();
}

QString AssetManager::loadStyleSheetByFilename(QString filename)
{
    // load css stylesheets
    QFile styleFile(filename);
    if (!styleFile.exists()) {
            qDebug() << "File " << styleFile.fileName() << " does not exist.";
    }
    styleFile.open( QFile::ReadOnly );
    // Apply the loaded stylesheet
    QString style = styleFile.readAll();
    styleFile.close();

    return style;
}

QString AssetManager::getStyle(const std::string &style) const
{
    if(isLoaded)
    {
        if(style == "MAIN_STYLE")
        {
            return defaultStyle;
        }
    }

    return defaultStyle;
}
QFileInfoList AssetManager::getStyleSheets()
{
    return styleSheets;
}

QFont AssetManager::getFont(const std::string &font) const
{
    if(isLoaded)
    {
        if(font == "MAIN_CODE_FONT")
            return srcCodeLightFont;
        if(font == "MAIN_APP_FONT")
            return mainAppFont;

        qDebug() << "Success, loading font: ";
    } else
    {
        qDebug() << "Error: Attempting to load un-loaded assets. With font: ";
    }

    return srcCodeLightFont;
}

void AssetManager::loadAllLanguageSupport()
{
   QDir lsDir(LIBS_FILEPATH + "/language_support");
   QFileInfoList allLs = lsDir.entryInfoList();
   foreach (QFileInfo fileI, allLs){
       // if is a file and is a cfg file
       if (!fileI.isDir() && fileI.completeSuffix() == "cfg")
       {
            // open and begin reading each file
           QFile file(fileI.filePath());
           if (!file.exists()) {
                   QMessageBox::information(0, "Error", "Unable to load language support file: " + fileI.fileName());
                   continue;
           }
           file.open( QFile::ReadOnly );
           QTextStream fileStream(&file);
           // load into variables
           loadLanguageSupportFile(fileStream, fileI.baseName());
           file.close();
       }
   }
}

SyntaxHighlightingRuleSet * AssetManager::getLanguageSupportRuleSet(QString &language) const
{
    for (int i=0; i < syntaxHighlightingRules.length(); i++)
    {
        for (int i2=0; i2 < syntaxHighlightingRules[i]->languageSupport.length(); i2++)
        {
            if(language == syntaxHighlightingRules[i]->languageSupport[i2])
                return syntaxHighlightingRules[i];
        }
    }
    return NULL;
}

SyntaxHighlightingRuleSet * AssetManager::getLanguageSupportByName(QString &name)
{
    for(int i = 0; i < syntaxHighlightingRules.length(); i++)
    {
        if(syntaxHighlightingRules[i]->fileName == name)
            return syntaxHighlightingRules[i];
    }
}

QStringList AssetManager::getLoadedSupportFileNames()
{
    QStringList list;
    for(int i = 0; i < syntaxHighlightingRules.length(); i++)
    {
        list << syntaxHighlightingRules[i]->fileName;
    }
    return list;
}

void AssetManager::loadAllStyleSheets()
{
    QDir cssDir(LIBS_FILEPATH + "/css");
    QFileInfoList allCss = cssDir.entryInfoList();
    foreach (QFileInfo fileI, allCss){
        // if is a file and is a cfg file
        if (!fileI.isDir() && fileI.completeSuffix() == "qss")
        {
             styleSheets << fileI;
        }
    }
}

// Parses the file into an object that the syntax highlighting algorithm can understand
void AssetManager::loadLanguageSupportFile(QTextStream &in, const QString &inFileName)
{
    int state = -1;
    SyntaxHighlightingRuleSet *sRuleSet = new SyntaxHighlightingRuleSet();
    sRuleSet->fileName = inFileName;
    QString currentColor;
    QString prevColor;
    QString prevPrevColor;

    // read line by line
    while(!in.atEnd()) {
        QString line = in.readLine();
        QTextCharFormat qtf;

        if(line != "")
        {
            // progress state if equal to percent
            if(line[0] == '%')
            {
                state++;
                continue;
            }
            // sets the color for the next expressions
            if(line[0] == '#')
            {
                prevPrevColor = prevColor;
                prevColor = currentColor;
                currentColor = line;
            }

            SyntaxHighlightingRuleSet::HighlightingRule rule;
            // language support
            if(state==0)
            {
                sRuleSet->languageSupport.append(line);
            }
            //comment_Start_Expression
            if(state==1)
            {
                sRuleSet->commentStartExpression = QRegExp(line);
            }
            // comment_End_Expression
            if(state==2)
            {
                sRuleSet->commentEndExpression = QRegExp(line);
            }
            // Operator_Format
            if(state==3)
            {
                qtf.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtf;
                sRuleSet->highlightingRules.append(rule);
            }
            // number_Format
            if(state==4)
            {
                QTextCharFormat qtf;
                qtf.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtf;
                sRuleSet->highlightingRules.append(rule);
            }
            // Keyword formats
            if(state==5)
            {
                // add line to autocompleter
                sRuleSet->constantKeywords << line;
                line = "\\b" + line + "\\b";
                sRuleSet->keywordPatterns << line;
            }
            // function_Format
            if(state==6)
            {
                qtf.setFontItalic(true);
                qtf.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtf;
                sRuleSet->highlightingRules.append(rule);
            }
            // class_Format
            if(state==7)
            {
                // also load keyword format once stream has finished
                qtf.setForeground(QColor(prevPrevColor));
                qtf.setFontWeight(QFont::Bold);
                // load all as seperate expressions
                foreach (const QString &pattern, sRuleSet->keywordPatterns) {
                    rule.pattern = QRegExp(pattern);
                    rule.format = qtf;
                    sRuleSet->highlightingRules.append(rule);
                }
                // handle class format
                qtf.setFontWeight(QFont::Bold);
                qtf.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtf;
                sRuleSet->highlightingRules.append(rule);
            }
            // Other_Format
            if(state==8)
            {
                qtf.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtf;
                sRuleSet->highlightingRules.append(rule);
            }
            // single_Line_Comment_Format
            if(state==9)
            {
                QTextCharFormat qtfs;
                qtfs.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtfs;
                sRuleSet->highlightingRules.append(rule);
            }
            // multi_line_Comment_Format
           if(state==10)
                sRuleSet->multiLineCommentFormat.setForeground(QColor(currentColor));
            // Quotation_Format
            if(state==11)
            {
                QTextCharFormat qtfq;
                qtfq.setFontItalic(true);
                qtfq.setForeground(QColor(currentColor));
                rule.pattern = QRegExp(line);
                rule.format = qtfq;
                sRuleSet->highlightingRules.append(rule);
            }
            if(state==12)
            {
                sRuleSet->autocorrectFormat = QRegExp(line);
            }
            if(state==13)
            {
                sRuleSet->autocorrectTrimFormat = QRegExp(line);
            }
            // Line highlight
            if(state==14)
            {
               sRuleSet->lineHighlightColor = currentColor;
            }
            // Search background
            if(state==15)
            {
               sRuleSet->searchHighlightColor = currentColor;
            }
            // Search foreground
            if(state==16)
            {
               sRuleSet->searchHighlightColorForeground = currentColor;
            }
        }
    }
    // append parsed object file
    syntaxHighlightingRules.append(sRuleSet);
}

