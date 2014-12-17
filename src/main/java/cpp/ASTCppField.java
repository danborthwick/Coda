package cpp;

import uk.co.badgersinfoil.metaas.impl.ASTASField;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppField extends ASTASField
{
    private static int INDEX_DECL = 3;

    private LinkedListTree findDecl() {
        return (LinkedListTree)ast.getChild(INDEX_DECL);
    }

    @Override
    public String getName() {
        return findDecl().getText();
    }

    @Override
    public String getType()
    {
        LinkedListTree decl = this.findDecl();
        LinkedListTree typeSpec = decl.getFirstChild();
        if (typeSpec == null) return null;
        return ASTUtils.typeSpecText(typeSpec);
    }

    public ASTCppField(LinkedListTree ast)
    {
        super(ast);
    }
}
