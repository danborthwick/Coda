package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppMethod extends ASTASMethod {

    private CppBuilder builder;

    public ASTCppMethod(LinkedListTree ast, CppBuilder builder) {
        super(ast);
        this.builder = builder;
    }

    @Override
    public ASArg addParam(String name, String type) {

        ASTASArg param = builder.newParam(name, type);

        LinkedListTree params = ASTUtils.findChildByType(ast, AS3Parser.PARAMS);

        if (params.getChildCount() > 0) {
            params.appendToken(TokenBuilder.newComma());
            params.appendToken(TokenBuilder.newSpace());
        }
        params.addChildWithTokens(param.getAST());

        return param;
    }


    public void addStatement(Statement statement) {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        ASTUtils.addChildWithIndentation(block, ast(statement));
    }
}
