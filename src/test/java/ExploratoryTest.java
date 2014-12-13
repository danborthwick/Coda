import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

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
        ASClassType clazz = (ASClassType) unit.getType();
        ASMethod method = clazz.newMethod("okButton", Visibility.PUBLIC, "SceneObject");
        method.addStmt("return _okButton");
        project.writeAll();
    }

    @Test
    public void testDialogWrapperCanBeGeneratedFromXML() throws Exception
    {
        InputStream stream = getClass().getResourceAsStream("PreGameDialog.xml");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(stream);
        Node scene = document.getElementsByTagName("scene").item(0);
        String dialogName = capitalize(nameAttribute(scene));
        assertThat(dialogName, equalTo("PreGameDialog"));

        ActionScriptFactory factory = new ActionScriptFactory();
        ActionScriptProject project = factory.newEmptyASProject(OUTPUT_LOCATION);

        ASCompilationUnit unit = project.newClass(dialogName);
        ASClassType clazz = (ASClassType) unit.getType();

        clazz.newField("_name", Visibility.PROTECTED, "string");
        clazz.newMethod("getName", Visibility.PUBLIC, "string").addStmt("return _name");

        ASMethod constructor = clazz.newMethod(dialogName, Visibility.PUBLIC, null);
        constructor.addParam("rootObject", "SceneObject");

        NodeList buttons = document.getElementsByTagName("button");
        for (int i=0; i < buttons.getLength(); i++)
        {
            String name = nameAttribute(buttons.item(i));
            String capitalizedName = capitalize(name);
            ASField field = clazz.newField("_so" + capitalizedName, Visibility.PROTECTED, "ButtonSceneObject");
            clazz.newMethod("get" + capitalizedName, Visibility.PUBLIC, field.getType()).newReturn(field.getName());
            constructor.addStmt(field.getName() + " = rootObject.find(\"" + name + "\")");
        }

        project.writeAll();
    }

    private static String nameAttribute(Node node)
    {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    private static String capitalize(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}