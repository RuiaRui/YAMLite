package Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

//词法分析
//
public class LexerAnalysis {
    private boolean valid = true;
    private ArrayList<Token> TokenList;
    private String s = "";
    private int indent = 2; // 缩进为2个空格
    private int lineNo=0;


    public ArrayList<Token> lexer(String path){
        BufferedReader bufferedReader=readFile(path);
        String Source=readLine(bufferedReader);
        while (Source!=null){
            checkLine(Source);
            Source=readLine(bufferedReader);

        }
        return TokenList;
    }

    private BufferedReader readFile(String path){
        BufferedReader buffer = null;
        try {
             buffer = new BufferedReader(new FileReader(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    private String readLine(BufferedReader buffer){

        String line= null;
        try {
            line = buffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(line!=null){
            lineNo++;
           // line+="\n";
        }
        return line;
    }

    //
    private void checkLine(String line){
        int currentIndent=0;
        String currentLine=line;
        int level=0;

        while (currentLine.startsWith(" ")) {
            currentIndent++;
            currentLine = currentLine.substring(1);
            if (currentLine.startsWith("#")){
                currentIndent=0;
            }
        }
        //indent check
        if (currentIndent % indent != 0) {
            LexerError Indenterror=new LexerError(+lineNo+" 行: 缩进错误");
        }
        level = currentIndent / indent;

        //comment delete
        if(currentLine.contains("#")){
            int posTemp=currentLine.indexOf("#");
            String content= currentLine.substring(posTemp);
            currentLine=currentLine.substring(0,posTemp);
            Token comment = new Token(TokenType.COMMENT, content, level,  lineNo,posTemp+1,false);
            TokenList.add(comment);
        }
        //key:value
        else if(currentLine.contains(":")){
            String[] temp = currentLine.split(":", 2);
            String key = temp[0];
            String value = temp[1];
            int keypos=getPosition(key,line);
            if(!isIdentifierFormat(key)){
                LexerError keyerror=new LexerError(+lineNo+" 行: "+keypos+"键格式错误");
            }
            if(!value.startsWith(" ")){
                if(value.startsWith("\n")){
                    TokenList.add(new Token(TokenType.KEYWORD,key,level,lineNo,keypos,true));
                }else{
                    LexerError spaceerror=new LexerError(+lineNo+" 行: "+keypos+2+"冒号后缺少空格");
                }
            }else{
                value=value.replace(" ","");
                if(value=="\n"){
                    TokenList.add(new Token(TokenType.KEYWORD,key,level,lineNo,keypos,true));
                }else{
                    int valuepos=getPosition(value,line);
                    addValueToken(value,level,valuepos,level>0);
                }
            }
        }
        //array content
        else if(currentLine.startsWith("-")){
            int pos=getPosition("-",line);
            currentLine=currentLine.substring(1);
            if(!currentLine.startsWith(" ")) {
                if (currentLine == "\n") {
                    TokenList.add(new Token(TokenType.ARRAY, "-", level, lineNo, pos, true));
                } else {
                    LexerError spaceerror2 = new LexerError(+lineNo + " 行: " + pos + 2 + "- 后缺少空格");
                }
            }else {
                currentLine = currentLine.trim();
                if (currentLine == "\n") {
                    TokenList.add(new Token(TokenType.ARRAY, "-", level, lineNo, pos, true));
                } else {
                    int pos2=getPosition(currentLine,line);
                    addValueToken(currentLine,level,pos2,level>0);
                }
            }
        }else if(currentLine=="\n"){
            return;
        }else {
            LexerError lineErr=new LexerError(+lineNo+" 行: 语句不合法");
        }



    }

    private int getPosition(String key,String s){
        return s.indexOf(key);
    }
    private boolean isIdentifierFormat(String key) {
        if (key.matches("^[a-zA-Z](([0-9a-zA-Z_]*[0-9a-zA-Z])|[0-9a-zA-Z])?$")) {
            return true;
        }
        return false;
    }

    private void addValueToken(String content, int level, int pos, boolean isArrayContent){
        Token temp;
        if (content.matches("^(0|-?[1-9]\\d*)$")) {//int
            temp = new Token(TokenType.INT,content, level, lineNo , pos ,isArrayContent);
            TokenList.add(temp);
        } else if (content.matches("^((0\\.0)|-?[1-9]\\d*\\.\\d*)$")) {//double
            temp = new Token(TokenType.FLOAT,content, level, lineNo , pos ,isArrayContent);
            TokenList.add(temp);
        } else if (content.matches("^(\\d\\.\\d*(e|E)(\\+|-)?\\d*)$")) {//decimal
            temp = new Token(TokenType.DECIMAL,content, level, lineNo , pos ,isArrayContent);
            TokenList.add(temp);
        } else if (content.equals("true") || content.equals("false")) {//bool
            temp = new Token(TokenType.BOOL,content, level, lineNo , pos ,isArrayContent);
            TokenList.add(temp);
        } else {
            if (!content.startsWith("\"") || !content.endsWith("\"")) {//string
                LexerError stringErr= new LexerError("expect <\">"+lineNo+pos);
            }
            temp = new Token(TokenType.STRING,content, level, lineNo , pos ,isArrayContent);
            TokenList.add(temp);
        }

    }


}
