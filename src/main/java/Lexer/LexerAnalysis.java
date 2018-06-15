package Lexer;

import java.util.ArrayList;

//词法分析
//
public class LexerAnalysis {
    private boolean valid = true;
    private ArrayList<Token> TokenList;
    private String s = "";
    private int index = 0;
    private int indent = 2; // 缩进为2个空格
}
