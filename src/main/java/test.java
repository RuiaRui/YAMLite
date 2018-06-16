import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import Lexer.*;

public class test {
    public static void main(String[] args) throws IOException {
        LexerAnalysis lex=new LexerAnalysis();
        ArrayList<Token> TokenList;
        TokenList=lex.lexer("sample.yml");
        System.out.println(TokenList);


    }

}
