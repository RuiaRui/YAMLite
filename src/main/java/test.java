import java.io.IOException;
import java.util.ArrayList;

import Lexer.*;
import Parse.Parse;

public class test {
    public static void main(String[] args) throws IOException {
        LexerAnalysis lex=new LexerAnalysis();
        Parse parse=new Parse("sample.yml");
        parse.parser();
        System.out.println(parse.getNodes());



    }

}
