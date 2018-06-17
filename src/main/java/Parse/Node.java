package Parse;

import Lexer.TokenType;

import java.util.ArrayList;

public class Node {


    private ArrayList<Node> child;
    private String value;
    private TokenType type;
    private NodeType nodeType;
    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<Node> getChild() {
        return child;
    }

    public void addChild(Node child) {
        this.child.add(child);
    }

    public Node(TokenType type, String value, NodeType nodeType) {
        this.type = type;
        this.value = value;
        this.nodeType=nodeType;
        child = new ArrayList<Node>();
    }

    @Override
    public String toString() {
        return getValue();
    }
    //
//    char left;
//    String right;
}
