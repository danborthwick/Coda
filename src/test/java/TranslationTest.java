import cpp.CppASTGenerator;
import cpp.CppFactory;
import cpp.CppProject;
import multi.MultiLanguageProject;
import org.junit.Test;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptParser;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTActionScriptProject;

import java.io.InputStreamReader;
import java.io.Reader;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TranslationTest
{

    @Test
    public void testSimpleAS3CanBeParsedAndRegenerated() throws Exception
    {
        ActionScriptFactory fact = new ActionScriptFactory();
        ASCompilationUnit unit = parseMyClass(fact.newParser());

        ActionScriptProject proj = fact.newEmptyASProject(ExploratoryTest.OUTPUT_LOCATION + "/regenerate");
        proj.addCompilationUnit(unit);
        proj.writeAll();
    }

    @Test
    public void testAS3ToCppTranslate() throws Exception
    {
        ActionScriptFactory fact = new CppFactory();
        ASCompilationUnit asUnit = parseMyClass(fact.newParser());

        ActionScriptProject proj = new CppProject(fact);
        proj.setOutputLocation(ExploratoryTest.OUTPUT_LOCATION + "/translate");
        ASCompilationUnit cppUnit = new CppASTGenerator().translateCompilationUnit(asUnit);
        proj.addCompilationUnit(cppUnit);
        proj.writeAll();
    }

    private ASCompilationUnit parseMyClass(ActionScriptParser parser)
    {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("MyClass.as"));
        assertThat(reader, notNullValue());
        return parser.parse(reader);
    }
}
