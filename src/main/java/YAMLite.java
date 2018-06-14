import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YAMLite {



    enum options{prase,json,find};


    public YAMLite(String file){

    }
    public static void main(String[] args) throws IOException{
        options op =options.prase;
        String filepath=System.getProperty("user.dir");

        if(args.length==1){
            filepath=args[0];
        }else if(args.length==2){
            if(args[0]=="-json"){
                op=options.json;
            } else if(args[0]=="-find"){
                op=options.find;
            }
            filepath=args[0];
        }else {
            System.out.println("Usage: json [-pretty][filepath][query]");
            System.exit(0);
        }

        try{
            File file=new File(filepath);
            if(!file.isFile() || !file.exists()) {
                System.out.println("No file exists!");
            }else {
                //具体实现
                
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
