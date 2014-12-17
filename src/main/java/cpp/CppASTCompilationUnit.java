package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.impl.AS3ASTCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTASPackage;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.security.cert.CertPathParameters;

public class CppASTCompilationUnit extends AS3ASTCompilationUnit
{
    private CppBuilder builder;

    public CppASTCompilationUnit(LinkedListTree unit, CppBuilder builder)
    {
        super(unit);
        this.builder = builder;
    }

    private LinkedListTree getPkgNode() {
        return ASTUtils.findChildByType(ast, AS3Parser.PACKAGE);
    }

    @Override
    public ASPackage getPackage()
    {
        LinkedListTree pkg = getPkgNode();
        if (pkg == null) {
            return null;
        }
        return new ASTCppPackage(pkg, builder);
    }
}
