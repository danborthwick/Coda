package cpp;

import uk.co.badgersinfoil.metaas.ActionScriptWriter;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.io.IOException;
import java.io.Writer;

public class CppWriter implements ActionScriptWriter
{
    private CppASTGenerator astGenerator;

    public CppWriter(CppASTGenerator astGenerator)
    {
        this.astGenerator = astGenerator;
    }

    public void write(Writer writer, ASCompilationUnit cu) throws IOException
    {
        ASClassType clazz = (ASClassType) cu.getType();
        LinkedListTree ast = astGenerator.forClass(clazz);
        new ASTPrinter(writer).print(ast);
    }
}
