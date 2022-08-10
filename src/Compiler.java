import frontend.SysYVisitorImpl;
import ir.Module;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import util.frontend.SysYLexer;
import util.frontend.SysYParser;

import java.io.File;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        if(args[0].endsWith(".sy")){
            test(args[0]);
        }else{
            tests(args[0]);
        }
        /*try {
            input = CharStreams.fromFileName(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SysYParser parser = new SysYParser(tokens);

        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));
        SysYVisitorImpl visitor = new SysYVisitorImpl(new Module());
        visitor.visit(tree);
        System.out.println(visitor.module.toString());*/
    }

    public static void test(String filename){
        CharStream input = null;
        try {
            input = CharStreams.fromFileName(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SysYParser parser = new SysYParser(tokens);

        ParseTree tree = parser.program();
        //System.out.println(tree.toStringTree(parser));
        SysYVisitorImpl visitor = new SysYVisitorImpl(new Module());
        visitor.visit(tree);
        System.out.println(visitor.module.toString());

    }
    public static void tests(String dictionary_name){
        CharStream input = null;
        //String target=new String(args[0]+"\\target");
        File file=new File(dictionary_name);
        File fa[]=file.listFiles();
        for(int i=0;i<fa.length;i++){
            if(!fa[i].isDirectory()&&fa[i].getName().endsWith(".sy")){
                try {
                    input=CharStreams.fromFileName(dictionary_name+"\\"+fa[i].getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(fa[i].getName());

                SysYLexer lexer = new SysYLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);

                SysYParser parser = new SysYParser(tokens);

                ParseTree tree = parser.program();
                //System.out.println(tree.toStringTree(parser));
                SysYVisitorImpl visitor = new SysYVisitorImpl(new Module());
                visitor.visit(tree);

                System.out.println(visitor.module.toString());
            }
        }
    }
    public static void write_to_target(String filename,String result){

    }

}
