import org.junit.Test;
import scenes.DialogProjectFactory;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.ActionScriptWriter;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASMethod;

import java.io.IOException;
import java.io.Writer;

import static uk.co.badgersinfoil.metaas.dom.Visibility.PUBLIC;

public class ExploratoryTest
{
    public static final String OUTPUT_LOCATION = "target/test-output";

    @Test
    public void testExampleFromOverview() throws Exception
    {
        ActionScriptFactory fact = new ActionScriptFactory();
        ActionScriptProject proj = fact.newEmptyASProject(OUTPUT_LOCATION);
        ASCompilationUnit unit = proj.newClass("Test");
        ASClassType clazz = (ASClassType) unit.getType();
        ASMethod meth = clazz.newMethod("test", PUBLIC, "void");
        meth.addStmt("trace('Hello world')");
        proj.writeAll();
    }

    @Test
    public void testSomethingLikeADialogWrapperCanBeGenerated() throws Exception
    {
        ActionScriptFactory factory = new ActionScriptFactory();
        ActionScriptProject project = factory.newEmptyASProject(OUTPUT_LOCATION);
        ASCompilationUnit unit = project.newClass("MyDialogView");
        ASClassType clazz = (ASClassType) unit.getType();
        ASMethod method = clazz.newMethod("okButton", PUBLIC, "SceneObject");
        method.addStmt("return _okButton");
        project.writeAll();
    }

    @Test
    public void testDialogWrapperCanBeGeneratedFromXML() throws Exception
    {
        ActionScriptFactory factory = new ActionScriptFactory();
        ActionScriptProject project = new DialogProjectFactory().build(factory);
        writeProject(project, OUTPUT_LOCATION + "/default");
    }

    @Test
    public void testACustomWriterCanBeUsed() throws Exception
    {
        ActionScriptFactory factory = new ActionScriptFactory() {
            @Override
            public ActionScriptWriter newWriter()
            {
                return new ActionScriptWriter()
                {
                    @Override
                    public void write(Writer writer, ASCompilationUnit cu) throws IOException
                    {
                        writer.write(cu.getType().getName());
                    }
                };
            }
        };

        ActionScriptProject project = new DialogProjectFactory().build(factory);
        writeProject(project, OUTPUT_LOCATION + "/custom");
    }

    private void writeProject(ActionScriptProject project, String outputLocation) throws IOException
    {
        project.setOutputLocation(outputLocation);
        project.performAutoImport();
        project.writeAll();
    }
}