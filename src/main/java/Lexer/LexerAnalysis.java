package Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//词法分析
//
public class LexerAnalysis {
    private boolean valid = true;
    private ArrayList<Token> TokenList;
    private String s = "";
    private int index = 0;
    private int indent = 2; // 缩进为2个空格
    int lineNo=0;

    public LexerAnalysis(){


    }

    public BufferedReader readFile(String path){
        BufferedReader buffer = null;
        try {
             buffer = new BufferedReader(new FileReader(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    public String readLine(BufferedReader buffer){

        String line= null;
        try {
            line = buffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(line!=null){
            lineNo+=1;
           // line+="\n";
        }
        return line;
    }

    private void checkLine(String line) {



    }


}
