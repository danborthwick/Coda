package cpp;

import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

class CppStatement extends ASTScriptElement implements Statement
{
    public CppStatement(LinkedListTree ast) {
        super(ast);
    }
}
