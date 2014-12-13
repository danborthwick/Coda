import org.junit.Test;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;

import static org.junit.Assert.*;

public class ExploratoryTest
{

    public static final String OUTPUT_LOCATION = "target/test-output";

    @Test
    public void testExampleFromOverview() throws Exception
    {
        ActionScriptFactory fact = new ActionScriptFactory();
        ActionScriptProject proj = fact.newEmptyASProject(OUTPUT_LOCATION);
        ASCompilationUnit unit = proj.newClass("Test");
        ASClassType clazz = (ASClassType)unit.getType();
        ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
        meth.addStmt("trace('Hello world')");
        proj.writeAll();
    }

    @Test
    public void testSomethingLikeADialogWrapperCanBeGenerated() throws Exception
    {
        ActionScriptFactory factory = new ActionScriptFactory();
        ActionScriptProject project = factory.newEmptyASProject(OUTPUT_LOCATION);
        ASCompilationUnit unit = project.newClass("MyDialogView");
        ASClassType clazz = (ASClassType)unit.getType();
        ASMethod method = clazz.newMethod("okButton", Visibility.PUBLIC, "SceneObject");
        method.addStmt("return _okButton");
        project.writeAll();
    }
}