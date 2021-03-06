package cpp;

import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.ASTASClassType;
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

    public LinkedListTree astForUnit(ASCompilationUnit unit)
    {
        ASCompilationUnit cppUnit = translateCompilationUnit(unit);
        return ((ASTASClassType) cppUnit.getType()).getAST();
    }

    public ASCompilationUnit translateCompilationUnit(ASCompilationUnit unit)
    {
        ASTASClassType asClass = (ASTASClassType) unit.getType();
        String packageName = unit.getPackageName();
        String qualifiedClassName = ((packageName == null) ? "" : (packageName + ".")) + asClass.getName();
        ASCompilationUnit cppUnit = factory.newClass(qualifiedClassName);
        ASTASClassType cppClass = (ASTCppClassType) cppUnit.getType();

        for (ASField asField : (List<ASField>) asClass.getFields()) {
            cppClass.newField(asField.getName(), asField.getVisibility(), asField.getType());
        }

        for (ASMethod asMethod : (List<ASMethod>) asClass.getMethods()) {
            ASMethod cppMethod = cppClass.newMethod(asMethod.getName(), asMethod.getVisibility(), asMethod.getType());
            for (Statement asStatement : (List<Statement>) asMethod.getStatementList()) {
                cppMethod.addStmt(asStatement.toString());
            }
        }

        return cppUnit;
    }

    private static String typeNameFrom(String qualifiedName) {
        int p = qualifiedName.lastIndexOf('.');
        if (p == -1) {
            return qualifiedName;
        }
        return qualifiedName.substring(p+1);
    }
}
