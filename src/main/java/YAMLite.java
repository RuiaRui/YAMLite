import Parse.Parse;

import java.io.File;
import java.io.IOException;

public class YAMLite {



    enum options{parse,json,find}


    public YAMLite(){



    }
    public static void main(String[] args) throws IOException{
        options op =options.parse;
        String filepath=System.getProperty("user.dir");
        String index="";

        if(args.length==1){
            filepath=args[0];
        }else if(args.length==2&&(args[0].equals("-json")|args[0].equals("-parse"))){
            if(args[0].equals("-json")){
                op=options.json;

            } else {
                op=options.parse;
            }
            filepath=args[1];
        }else if(args.length==3&&args[0].equals("-find")) {
            op = options.find;
            filepath=args[2];
            index=args[1];
        }
        else {
            System.out.println("Usage: yamlite [option [value]] file");
            System.exit(0);
        }

        try{
            File file=new File(filepath);
            if(!file.isFile() || !file.exists()) {
                System.out.println("No such file exists, please check and try again");
            }else {
                YAMLite y=new YAMLite();
                Parse parse=new Parse(filepath);
                switch (op){
                    case parse:
                        parse.parser();
                        System.out.println("vaild");
                        break;
                    case find:
                        parse.find(index);
                        break;
                    case json:
                        parse.json();
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
