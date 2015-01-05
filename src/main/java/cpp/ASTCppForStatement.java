package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.ASTBuilder;
import uk.co.badgersinfoil.metaas.impl.ASTScriptElement;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppForStatement extends CppStatement
{
    public ASTCppForStatement(LinkedListTree init, LinkedListTree condition, LinkedListTree update)
    {
        super(ASTBuilder.newFor(init, condition, update));
    }

    public void addStatement(ASTScriptElement statement)
    {
        LinkedListTree block = ASTUtils.findChildByType(getAST(), AS3Parser.BLOCK);
        ASTUtils.addChildWithIndentation(block, ast(statement));
    }
}
