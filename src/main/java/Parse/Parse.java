package Parse;

import java.io.File;
import java.io.IOException;
import Lexer.*;
import java.util.*;

public class Parse {

    private boolean valid;
    private int index;
    private ArrayList<Token> tokens;
    private Token current;
   // private TreeNode root;


    private final HashSet<TokenType> follow=new HashSet<TokenType>(){
        {
            add(TokenType.STRING);
            add(TokenType.INT);
            add(TokenType.FLOAT);
            add(TokenType.DECIMAL);//scientific counting
            add(TokenType.BOOL);
            add(TokenType.ERRORNUM);
            add(TokenType.ERRORSTR);
        }
    };

    private void getnext()  {
        if (index+1 < tokens.size())
            current = tokens.get(++index);
        else {
            Token end = new Token(TokenType.EOF, current.getValue(), current.getLineNo(), current.getPos());
            current = end;
            index = tokens.size();
        }
    }

    private boolean match(TokenType in)  {
        String str = current.toString() + ": expected <" ;
        if (current.getType() == in) {
            getnext();
            return true;
        }
        else {
            if (Objects.equals(String.valueOf(in), "QUOTE")){
                str += " \" >";
            }
            else if (Objects.equals(String.valueOf(in), "COMMA")){
                str += " , >";
            }
            else if (Objects.equals(String.valueOf(in), "COLON")){
                str += " : >";
            }
            else
                str += String.valueOf(in)+">";
            System.out.println(str);
            return false;
        }
    }





    protected:
    int T;
    Node analy_str[100]; //输入文法解析

    set<char> first_set[100];//first集
    set<char> follow_set[100];//follow集
    vector<char> ter_copy; //去$终结符
    vector<char> ter_colt;//终结符
    vector<char> non_colt;//非终结符

    private void get_first(char target){
        int tag = 0;
        int flag = 0;
        for (int i = 0; i<T; i++)
        {
            if (analy_str[i].left == target)//匹配产生式左部
            {
                if (!isNotSymbol(analy_str[i].right[0]))//对于终结符，直接加入first
                {
                    first_set[get_index(target)].insert(analy_str[i].right[0]);
                }
                else
                {
                    for (int j = 0; j<analy_str[i].right.length(); j++)
                    {
                        if (!isNotSymbol(analy_str[i].right[j]))//终结符结束
                        {
                            first_set[get_index(target)].insert(analy_str[i].right[j]);
                            break;
                        }
                        get_first(analy_str[i].right[j]);//递归
                        //  cout<<"curr :"<<analy_str[i].right[j];
                        set<char>::iterator it;
                        for (it = first_set[get_index(analy_str[i].right[j])].begin(); it != first_set[get_index(analy_str[i].right[j])].end(); it++)
                        {
                            if (*it == '$')
                            flag = 1;
                        else
                            first_set[get_index(target)].insert(*it);//将FIRST(Y)中的非$就加入FIRST(X)
                        }
                        if (flag == 0)
                            break;
                        else
                        {
                            tag += flag;
                            flag = 0;
                        }
                    }
                    if (tag == analy_str[i].right.length())//所有右部first(Y)都有$,将$加入FIRST(X)中
                        first_set[get_index(target)].insert('$');
                }
            }
        }
    }



}
