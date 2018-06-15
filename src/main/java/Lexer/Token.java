package Lexer;
//单词分析
public class Token {
    private TokenType type;
    private String value;
    private int LineNo = 1;
    private int pos;

    public  Token(TokenType type, String value, int lineNo, int pos){
        this.type=type;
        this.value=value;
        this.LineNo=lineNo;
        this.pos=pos;
    }

    public int getLineNo() {
        return LineNo;
    }

    public int getPos() {
        return pos;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public void setLineNo(int lineNo) {
        LineNo = lineNo;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "line:" +getLineNo()+", position :"+getPos();
    }
}
