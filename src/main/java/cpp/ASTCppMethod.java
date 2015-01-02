package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.impl.ASTASMethod;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppMethod extends ASTASMethod {

    public ASTCppMethod(LinkedListTree ast) {
        super(ast);
    }

    public void addStatement(Statement statement) {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        ASTUtils.addChildWithIndentation(block, ast(statement));
    }
}
