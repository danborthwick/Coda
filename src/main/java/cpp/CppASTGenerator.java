package cpp;

import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.*;

import java.util.List;

public class CppASTGenerator
{
    private CppFactory factory;
    private CppStatementTranslator cppStatementTranslator;

    public CppASTGenerator(CppBuilder builder, CppFactory factory)
    {
        this.factory = factory;
        this.cppStatementTranslator = new CppStatementTranslator(builder);
    }

    public CppASTGenerator(CppBuilder builder)
    {
        this(builder, new CppFactory(builder));
    }

    public CppASTGenerator()
    {
        this(new CppBuilder());
    }

    @SuppressWarnings("unchecked")
    public ASCompilationUnit translateCompilationUnit(ASCompilationUnit asUnit)
    {
        ASTASClassType asClass = (ASTASClassType) asUnit.getType();
        String packageName = asUnit.getPackageName();
        String qualifiedClassName = ((packageName == null) ? "" : (packageName + ".")) + asClass.getName();
        ASCompilationUnit cppUnit = factory.newClass(qualifiedClassName);
        ASTASClassType cppClass = (ASTCppClassType) cppUnit.getType();

        for (String importName : (List<String>)asUnit.getPackage().findImports())
        {
            cppUnit.getPackage().addImport(importName);
        }

        for (ASField asField : (List<ASField>) asClass.getFields()) {
            cppClass.newField(asField.getName(), asField.getVisibility(), asField.getType());
        }

        for (ASMethod asMethod : (List<ASMethod>) asClass.getMethods()) {
            ASTCppMethod cppMethod = (ASTCppMethod) cppClass.newMethod(asMethod.getName(), asMethod.getVisibility(), asMethod.getType());

            for (ASTASArg argument : (List<ASTASArg>) asMethod.getArgs()) {
                cppMethod.addParam(argument.getName(), argument.getType());
            }

            for (ASTScriptElement asStatement : (List<ASTScriptElement>) asMethod.getStatementList()) {
                Statement cppStatement = cppStatementTranslator.translateStatement(asStatement);
                cppMethod.addStatement(cppStatement);
            }
        }

        return cppUnit;
    }
}
