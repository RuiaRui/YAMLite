package Lexer;
//单词分析
public class Token {
    private TokenType type;
    private String value;
    private int LineNo;
    private int pos;
    private int level;
    private boolean isArray;

    public  Token(TokenType type, String value,int level, int lineNo, int pos,boolean isArray){
        this.type=type;
        this.value=value;
        this.LineNo=lineNo;
        this.pos=pos;
        this.level=level;
        this.isArray=isArray;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

}
