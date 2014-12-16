import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static uk.co.badgersinfoil.metaas.dom.ASConstants.TYPE_STRING;
import static uk.co.badgersinfoil.metaas.dom.Visibility.PROTECTED;
import static uk.co.badgersinfoil.metaas.dom.Visibility.PUBLIC;

public class DialogProjectFactory
{
    public ActionScriptProject build(ActionScriptFactory factory) throws Exception
    {
        InputStream stream = getClass().getResourceAsStream("PreGameDialog.xml");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(stream);
        Node scene = document.getElementsByTagName("scene").item(0);
        String dialogName = capitalize(nameAttribute(scene));
        assertThat(dialogName, equalTo("PreGameDialog"));

        ActionScriptProject project = factory.newEmptyASProject(null);

        project.addClasspathEntry("src/main/actionscript");
        ASCompilationUnit unit = project.newClass("dialogs." + dialogName);
        ASClassType clazz = (ASClassType) unit.getType();

        clazz.newField("_name", PROTECTED, TYPE_STRING);

        ASMethod constructor = clazz.newMethod(dialogName, PUBLIC, null);
        constructor.addParam("rootObject", "engine.SceneObject");

        NodeList buttons = document.getElementsByTagName("button");
        for (int i=0; i < buttons.getLength(); i++)
        {
            String name = nameAttribute(buttons.item(i));
            String capitalizedName = capitalize(name);
            ASField field = clazz.newField("_so" + capitalizedName, PROTECTED, "engine.ButtonSceneObject");
            clazz.newMethod("get" + capitalizedName, PUBLIC, field.getType()).newReturn(field.getName());
            constructor.addStmt(field.getName() + " = rootObject.find(\"" + name + "\")");
        }
        return project;
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
