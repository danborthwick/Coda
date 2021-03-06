package cpp;

import uk.co.badgersinfoil.metaas.ActionScriptWriter;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.io.IOException;
import java.io.Writer;

public class CppWriter implements ActionScriptWriter
{
    public void write(Writer writer, ASCompilationUnit cu) throws IOException
    {
        new ASTPrinter(writer).print(((CppASTCompilationUnit)cu).getAST());
    }
}
