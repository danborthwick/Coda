package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.impl.ASTASClassType;
import uk.co.badgersinfoil.metaas.impl.ASTASInterfaceType;
import uk.co.badgersinfoil.metaas.impl.ASTASPackage;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppPackage extends ASTASPackage
{
    private CppBuilder builder;

    public ASTCppPackage(LinkedListTree pkg, CppBuilder builder)
    {
        super(pkg);
        this.builder = builder;
    }

    @Override
    public ASType getType()
    {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        LinkedListTree type = ASTUtils.findChildByType(block, AS3Parser.CLASS_DEF);
        if (type != null) {
            return new ASTCppClassType(type, builder);
        }
        type = ASTUtils.findChildByType(block, AS3Parser.INTERFACE_DEF);
        if (type != null) {
            return new ASTASInterfaceType(type);
        }
        return null;
    }
}
