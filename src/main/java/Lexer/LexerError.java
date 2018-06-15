package Lexer;

public class LexerError {
    String errMessage;

    public LexerError(){
        errMessage="";
    }
    public LexerError(String str) {
        errMessage = str;
        System.out.println(errMessage);
    }
}
