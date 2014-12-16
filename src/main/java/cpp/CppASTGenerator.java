package cpp;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.Tree;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.impl.AS3ASTCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTASClassType;
import uk.co.badgersinfoil.metaas.impl.ASTActionScriptParser;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTreeAdaptor;

import java.util.List;

public class CppASTGenerator
{
    private CppBuilder builder;
    private CppFactory factory;

    private static final LinkedListTreeAdaptor TREE_ADAPTOR = new LinkedListTreeAdaptor();

    public CppASTGenerator(CppBuilder builder, CppFactory factory)
    {
        this.builder = builder;
        this.factory = factory;
    }

    public CppASTGenerator(CppBuilder builder)
    {
        this(builder, new CppFactory(builder));
    }

    public CppASTGenerator()
    {
        this(new CppBuilder());
    }

    public LinkedListTree forUnit(ASCompilationUnit unit)
    {
        ASTASClassType asClass = (ASTASClassType) unit.getType();
        String packageName = unit.getPackageName();
        String qualifiedClassName = ((packageName == null) ? "" : (packageName + ".")) + asClass.getName();
        ASCompilationUnit cppUnit = factory.newClass(qualifiedClassName);
        ASTASClassType cppClass = (ASTASClassType) cppUnit.getType();

        for (ASField asField : (List<ASField>) asClass.getFields()) {
            cppClass.newField(asField.getName(), asField.getVisibility(), asField.getType());
        }

        return cppClass.getAST();
    }

    private static String typeNameFrom(String qualifiedName) {
        int p = qualifiedName.lastIndexOf('.');
        if (p == -1) {
            return qualifiedName;
        }
        return qualifiedName.substring(p+1);
    }
}
