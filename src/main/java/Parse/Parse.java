package Parse;

import Lexer.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Parse {

    private boolean valid=true;
//    private int index=0;
    private Token current;
    private LexerAnalysis lex;
    private Queue<Token> tokens;
    private ArrayList<Node> nodes;
    private String path;

    public Parse(String filepath){
        lex=new LexerAnalysis();
        path=filepath;

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
        if (!valid){
            System.exit(1);
        }
    }

    public void find(String index) {
        parser();
        ArrayList<Node> results = new ArrayList<Node>();
        String[] indexes = index.split("\\.");
        if(indexes.length == 1){
            String i = indexes[0];
            if (i.matches("^(.*)(\\[[0-9]*\\])$")) {
                String[] arrayIndex = i.split("\\[|\\]");
                String arrayName = arrayIndex[0];
                int arrayPos = Integer.parseInt(arrayIndex[1]);
                results=findNode(arrayName,arrayPos-1,NodeType.ARRAY,nodes);
                for(Node r:results){
                    if(r.getNodeType() == NodeType.VALUE){
                        System.out.println(r.getValue());
                    }
                    else if(r.getNodeType() == NodeType.ARRAY){
                        System.out.println(jsonArray(r));
                    }
                    else if(r.getNodeType() == NodeType.KEYWORD){
                        System.out.println(jsonKey(r));
                    }
                }
            }else{
                results=findNode(i,0,NodeType.KEYWORD,nodes);
                for(Node r:results){
                    System.out.println(r.toString());
                }
            }

        }
        else if (indexes.length == 2){
            String i = indexes[0];
            String j = indexes[1];
            ArrayList<Node> results1 = new ArrayList<Node>();
            if (i.matches("^(.*)(\\[[0-9]*\\])$")) {
                String[] arrayIndex = i.split("\\[|\\]");
                String arrayName = arrayIndex[0];
                int arrayPos = Integer.parseInt(arrayIndex[1]);
                results=findNode(arrayName,arrayPos-1,NodeType.ARRAY,nodes);
                if(j.matches("^(.*)(\\[[0-9]*\\])$")){
                    String[] arrayIndex1 = i.split("\\[|\\]");
                    String arrayName1 = arrayIndex1[1];
                    System.out.println(arrayName1);
                    results1 = findNode(arrayName1,0,NodeType.ARRAY,results);
                }
                else{
                    results1 = findNode1(j,0,NodeType.ARRAY,results);
                }

            }
            for(Node r:results1){
                if(r.getNodeType() == NodeType.VALUE){
                    System.out.println(r.getChild().toString());
                }
                else if(r.getNodeType() == NodeType.ARRAY){
                    System.out.println(r.getChild().toString());
                }
                else if(r.getNodeType() == NodeType.KEYWORD){
                    System.out.println(r.getChild().toString());
                }
            }

        }


    }

    private ArrayList<Node> findNode1(String name,int Pos,NodeType type,ArrayList<Node> source){
        ArrayList<Node> result=new ArrayList<Node>();

        for(Node n :source){
            if((n.getNodeType()==type)&&(n.getChild().toString().equals("["+name+"]"))){
                result.add(n.getChild().get(Pos));
            }
        }
        return result;
    }

    public void json(){
        parser();
        StringBuilder json=new StringBuilder();
        json.append("{\n");

        for(Node n:nodes){
            if(n.getNodeType()==NodeType.ARRAY){
               json.append(jsonArray(n));

            }else {
                json.append(jsonKey(n));
            }
        }
        json.append("}");

        String output=json.toString();

        try {
            String filename = path.substring(0,path.length()-4);
            File out = new File(filename + ".json");
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            fileOutputStream.write(output.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String jsonKey(Node n){
        StringBuilder s = new StringBuilder();
        for(int i=0;i<n.getLevel();i++){
            s.append("  ");
        }
        s.append("\"" + n.getValue() + "\" : ");
        Node c = n.getChild().get(0);
        s.append(c.getValue());
        s.append(", \n");

        return s.toString();
    }

    private String jsonArray(Node n){
        StringBuilder s =new StringBuilder();
        for(int i=0;i<n.getLevel();i++){
            s.append("  ");
        }
        if (n.getValue().equals("-")) {
            s.append("[ \n");
        } else {
            s.append("\"" + n.getValue() + "\":[ \n");
        }
        ArrayList<Node> children=n.getChild();
        for(Node nn:children){
            if(nn.getType()==TokenType.ARRAY){
                s.append(jsonArray(nn));
                s.append(", \n");
            }else if(nn.getType()==TokenType.KEYWORD){
                for(int i=0;i<n.getLevel();i++){
                    s.append("  ");
                }
                s.append("{ \n");
                s.append(jsonKey(nn));
                for(int i=0;i<n.getLevel();i++){
                    s.append("  ");
                }
                s.append("} \n");
            }
            else {
                for(int i=0;i<nn.getLevel();i++){
                    s.append("  ");
                }
                s.append(nn.getValue());
                s.append(", \n");

            }
        }
        for(int i=0;i<n.getLevel();i++){
            s.append("  ");
        }
        int deletecoma=s.lastIndexOf(",");
        s.deleteCharAt(deletecoma);
        s.append("] \n");

        return s.toString();

    }

    private ArrayList<Node> findNode(String name,int Pos,NodeType type,ArrayList<Node> source){

        ArrayList<Node> result=new ArrayList<Node>();
        for(Node n :source){
            if((n.getNodeType()==type)&&(n.getValue().equals(name))){
                result.add(n.getChild().get(Pos));
            }
        }
        return result;
    }

    private void parseArray(Token token, ArrayList<Node> parent, int Level){
        Node node=new Node(token.getType(),token.getValue(),token.getLevel(),NodeType.ARRAY);
        while (!tokens.isEmpty()&&tokens.peek().getLevel() == Level + 1){
            Token tempT = tokens.poll();
            Node tempN = new Node(tempT.getType(),tempT.getValue(),tempT.getLevel(),NodeType.ARRAY);
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
        Node keyNode=new Node(t.getType(),t.getValue(),t.getLevel(),NodeType.KEYWORD);

        if(tokens.peek()!=null){
            t=tokens.poll();
            Node valueNode=new Node(t.getType(),t.getValue(),t.getLevel(),NodeType.VALUE);
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
