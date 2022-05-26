import frontend.MyVisitor;
import frontend.SysYLexer;
import frontend.SysYParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        CharStream input = null;
        try {
            input = CharStreams.fromFileName("testcases/functional_test/000_main.sy");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SysYParser parser = new SysYParser(tokens);
        ParseTree tree = parser.program();

        MyVisitor myVisitor = new MyVisitor();
        myVisitor.visit(tree);
    }
}
