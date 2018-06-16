package Parse;

public class ParseError {

    String str;
    public ParseError() {
        str = "";
    }

    public ParseError(String str) {
        this.str = str;
        System.out.println(str);
    }

}

