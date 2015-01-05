package cpp;

import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.Statement;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import javax.swing.plaf.nimbus.State;
import java.util.HashMap;
import java.util.List;
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
        translators.put(ASTASForStatement.class, new ForTranslator());
    }

    public CppStatement translateStatement(ASTScriptElement asStatement) {
        StatementTranslator translator = translators.getOrDefault(asStatement.getClass(), fallbackTranslator);
        return translator.toCpp(asStatement);
    }

    private abstract class StatementTranslator<StatementClass extends ASTScriptElement> {
        public final CppStatement toCpp(ASTScriptElement asStatement)
        {
            return translate((StatementClass) asStatement);
        }

        protected abstract CppStatement translate(StatementClass asStatement);
    }

    /**
     * Does no translation, just duplicates the input statement.
     * Of use when AS and Cpp syntax is the same.
     */
    private class PassthroughTranslator extends StatementTranslator<ASTScriptElement> {
        @Override
        protected CppStatement translate(ASTScriptElement asStatement) {
            LinkedListTree ast = ASTBuilder.dup(asStatement.getAST());
            return new CppStatement(ast);
        }
    }

    private class DeclarationTranslator extends StatementTranslator<ASTASDeclarationStatement> {
        @Override
        protected CppStatement translate(ASTASDeclarationStatement asStatement) {
            return builder.newDeclaration(asStatement.getFirstVarTypeName(), asStatement.getFirstVarName(), (ASTExpression) asStatement.getFirstVarInitializer());
        }
    }

    private class ForTranslator extends StatementTranslator<ASTASForStatement> {
        @Override
        protected CppStatement translate(ASTASForStatement asStatement) {

            ASTCppForStatement cppForStatement = builder.newFor(
                    translateStatement((ASTScriptElement) asStatement.getInit()),
                    translateStatement((ASTScriptElement) asStatement.getCondition()),
                    translateStatement((ASTScriptElement) asStatement.getUpdate()));

            for (ASTScriptElement asChildStatement : (List<ASTScriptElement>) asStatement.getStatementList()) {
                cppForStatement.addStatement(translateStatement(asChildStatement));
            }

            return cppForStatement;
        }
    }
}