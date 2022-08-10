import backend.IR2ASM;
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
        CharStream input = null;
        try {
            input = CharStreams.fromFileName(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SysYParser parser = new SysYParser(tokens);
        ParseTree tree = parser.program();

        SysYVisitorImpl visitor = new SysYVisitorImpl(new Module());
        visitor.visit(tree);
        System.out.println(visitor.module.toString());

        var g = new IR2ASM(visitor.module);
        System.out.println(g.genAsm());
    }
}
