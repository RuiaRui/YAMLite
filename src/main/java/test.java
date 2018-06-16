import java.io.BufferedReader;
import java.io.IOException;
import Lexer.*;

public class test {
    public static void main(String[] args) throws IOException {
        LexerAnalysis lex=new LexerAnalysis();
        BufferedReader b=lex.readFile("sample.yml");
        String s=lex.readLine(b);
        while(s!=null){
            System.out.println(s);
            s=lex.readLine(b);
        }

    }

}
