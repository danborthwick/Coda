package cpp;

import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.HashMap;
import java.util.Map;

public class CppStatementTranslator {
    private final CppBuilder builder;
    private final StatementTranslator fallbackTranslator;
    private final Map<Class<? extends Statement>, StatementTranslator> translators;

    public CppStatementTranslator(CppBuilder builder) {
        this.builder = builder;
        fallbackTranslator = new PassthroughTranslator();

        translators = new HashMap<>();
        translators.put(ASTASDeclarationStatement.class, new DeclarationTranslator());
    }

    Statement translateStatement(ASTScriptElement asStatement) {
        StatementTranslator translator = translators.getOrDefault(asStatement.getClass(), fallbackTranslator);
        return translator.toCpp(asStatement);
    }

    interface StatementTranslator {
        public CppStatement toCpp(ASTScriptElement asStatement);
    }

    /**
     * Does no translation, just duplicates the input statement.
     * Of use when AS and Cpp syntax is the same.
     */
    class PassthroughTranslator implements StatementTranslator {
        @Override
        public CppStatement toCpp(ASTScriptElement asStatement) {
            LinkedListTree ast = ASTBuilder.dup(asStatement.getAST());
            return new CppStatement(ast);
        }
    }

    class DeclarationTranslator implements StatementTranslator {
        @Override
        public CppStatement toCpp(ASTScriptElement asStatementRaw) {
            ASTASDeclarationStatement asStatement = (ASTASDeclarationStatement) asStatementRaw;
            return builder.newDeclaration(asStatement.getFirstVarTypeName(), asStatement.getFirstVarName(), (ASTExpression) asStatement.getFirstVarInitializer());
        }
    }
}