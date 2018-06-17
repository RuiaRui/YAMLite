package Parse;

import Lexer.*;

import java.lang.reflect.Type;
import java.util.*;

public class Parse {

    private boolean valid=true;
//    private int index=0;
    private Token current;
    private LexerAnalysis lex;
    private Queue<Token> tokens;
    private ArrayList<Node> nodes;

    public Parse(String filepath){
        lex=new LexerAnalysis();
        ArrayList<Token> t=lex.lexer(filepath);
        tokens=new LinkedList<Token>();
        for(Token token:t){
            tokens.offer(token);
        }
        nodes=new ArrayList<Node>();

    }

    public void parser(){
        while (tokens.peek()!=null){
            current=tokens.poll();
            //key：value
            if (!current.isArray()){
                if(current.getType()== TokenType.KEYWORD){
                    parseKeyAndValue(current,nodes);
                }else {
                    ParseError keyErr=new ParseError("Line "+current.getLineNo()+":  missing key");
                    valid=false;
                }
            }
            //array
            else{
                if(current.getType()== TokenType.KEYWORD){
                    int currentLevel=current.getLevel();
                   parseArray(current,nodes,currentLevel);
                }else {
                    ParseError keyErr=new ParseError("Line "+current.getLineNo()+":  missing key");
                    valid=false;
                }
            }
        }
        if (valid) {
            System.out.println("Valid");
        } else {
            System.exit(1);
        }
    }

    public void find(String index) {
        parser();
        ArrayList<Node> results = nodes;
        String[] indexes = index.split(".");
        for (String i : indexes) {
            if (i.matches("^(.*)(\\[[0-9]*\\])$")) {
                String[] arrayIndex = i.split("\\[|\\]");
                String arrayName = arrayIndex[0];
                int arrayPos = Integer.parseInt(arrayIndex[1]);
                results=findNode(arrayName,arrayPos,NodeType.ARRAY,results,results);
            }else{
                results=findNode(i,0,NodeType.ARRAY,results,results);
            }
        }
        for(Node r:results){
            System.out.println(r.getValue());
        }
    }

    public void json(){
        parser();
        StringBuilder json=new StringBuilder();
        json.append("{\n");

        for(Node n:nodes){
            if(n.getNodeType()==NodeType.ARRAY){

            }else {
                jsonKey(n);
            }
        }


    }

    private String jsonKey(Node n){
        StringBuilder s = new StringBuilder();
        s.append("\"" + n.getValue() + "\" : ");
        Node c = n.getChild().get(0);
        s.append(c.getValue());
        s.append(", \n");

        return s.toString();
    }

    private String jsonArray(Node n){
        StringBuilder s =new StringBuilder();
        if (n.getValue().equals("-")) {
            s.append("[");
        } else {
            s.append("\"" + n.getValue() + "\":[");
        }
        ArrayList<Node> children=n.getChild();
        for(Node nn:children){
            if(nn.getValue().equals("-")){
                s.append(jsonArray(nn));
                s.append(", \n");
            }
            else {
                s.append("{ \n");
                s.append(jsonKey(nn));
                s.append("} \n");
            }
        }

        return s.toString();

    }

    private ArrayList<Node> findNode(String name,int Pos,NodeType type,ArrayList<Node> source,ArrayList<Node> result){
        for(Node n :source){
            if(n.getNodeType()==type&&n.getValue()==name){
                result.add(n.getChild().get(Pos));
            }

        }
        return result;
    }

    private void parseArray(Token token, ArrayList<Node> parent, int Level){
        Node node=new Node(token.getType(),token.getValue(),NodeType.ARRAY);
        while (!tokens.isEmpty()&&tokens.peek().getLevel() == Level + 1){
            Token tempT = tokens.poll();
            Node tempN = new Node(tempT.getType(),tempT.getValue(),NodeType.ARRAY);
            if (tempT.getType().equals(TokenType.ARRAY)) {
                parseArray(tempT, node.getChild(), Level + 1);
            }else if(tempT.getType().equals(TokenType.KEYWORD)){
                parseKeyAndValue(tempT,node.getChild());
            }else{
                node.addChild(tempN);
            }
        }
        parent.add(node);

    }

    private void parseKeyAndValue(Token t,ArrayList<Node> n){
        Node keyNode=new Node(t.getType(),t.getValue(),NodeType.KEYWORD);

        if(tokens.peek()!=null){
            t=tokens.poll();
            Node valueNode=new Node(t.getType(),t.getValue(),NodeType.VALUE);
            keyNode.addChild(valueNode);
        }else{
            ParseError valueErr=new ParseError("Line "+t.getLineNo()+":  missing value");
            valid=false;
        }
        n.add(keyNode);


    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
