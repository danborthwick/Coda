package scenes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;

import static uk.co.badgersinfoil.metaas.dom.Visibility.PROTECTED;
import static uk.co.badgersinfoil.metaas.dom.Visibility.PUBLIC;

public class SceneClassGenerator {
    private final ActionScriptProject project;

    public SceneClassGenerator(ActionScriptProject project) {
        this.project = project;
    }

    public void addClassFromXMLDocument(String className, Document document) {
        ASCompilationUnit unit = project.newClass(className);
        ASClassType clazz = (ASClassType) unit.getType();

        ASMethod constructor = clazz.newMethod(clazz.getName(), PUBLIC, null);
        constructor.addParam("rootObject", "engine.SceneObject");

        // Use XPath or similar to find named nodes?
        NodeList sceneObjects = document.getElementsByTagName("SceneObject");
        for (int i=0; i < sceneObjects.getLength(); i++)
        {
            Node sceneObject = sceneObjects.item(i);
            String name = nameAttribute(sceneObject);
            if (name != null)
            {
                String capitalizedName = capitalize(name);
                ASField field = clazz.newField("_so" + capitalizedName, PROTECTED, "engine.ButtonSceneObject");
                clazz.newMethod("get" + capitalizedName, PUBLIC, field.getType()).newReturn(field.getName());
                constructor.addStmt(field.getName() + " = rootObject.find(\"" + name + "\")");
            }
        }

    }

    private static String nameAttribute(Node node)
    {
        Node nameNode = node.getAttributes().getNamedItem("name");
        return nameNode != null ? nameNode.getNodeValue() : null;
    }

    private static String capitalize(String s)
    {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
