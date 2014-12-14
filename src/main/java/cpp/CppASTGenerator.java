package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.impl.ASTBuilder;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;

public class CppASTGenerator
{
    private static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();

    public CppASTGenerator()
    {
    }

    public LinkedListTree forClass(ASClassType clazz)
    {
        LinkedListTree ast = (LinkedListTree) TREE_ADAPTOR.create(new LinkedListToken(AS3Parser.CLASS, "class"));
        ast.appendToken(TokenBuilder.newSpace());
        ast.appendToken(new LinkedListToken(AS3Parser.IDENT, typeNameFrom(clazz.getName())));
        //TODO: Something with blocks
        ast.appendToken(TokenBuilder.newLBrack());
        ast.appendToken(TokenBuilder.newNewline());

        for (Object fieldObject : clazz.getFields()) {
            ASField field = (ASField)fieldObject;
            ast.appendToken(new LinkedListToken(AS3Parser.TYPE_NAME, field.getType()));
            ast.appendToken(TokenBuilder.newSpace());
            ast.appendToken(new LinkedListToken(AS3Parser.IDENT, field.getName()));
            ast.appendToken(TokenBuilder.newNewline());
        }

        ast.appendToken(TokenBuilder.newRBrack());

        return ast;
    }

    private static String typeNameFrom(String qualifiedName) {
        int p = qualifiedName.lastIndexOf('.');
        if (p == -1) {
            return qualifiedName;
        }
        return qualifiedName.substring(p+1);
    }
}
