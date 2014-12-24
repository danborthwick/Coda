package scenes;

import cpp.ASTCppField;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.badgersinfoil.metaas.dom.Visibility.PROTECTED;
import static uk.co.badgersinfoil.metaas.dom.Visibility.PUBLIC;

public class SceneClassGenerator {
    private final ActionScriptProject project;

    public static final String SCENE_OBJECT_QNAME = "com.king.flash.spaceland.scene.SceneObject";

    public SceneClassGenerator(ActionScriptProject project) {
        this.project = project;
    }

    public void addClassFromXMLDocument(String className, Document document) {
        ASCompilationUnit unit = project.newClass(className);
        ASClassType clazz = (ASClassType) unit.getType();

        ASMethod constructor = clazz.newMethod(clazz.getName(), PUBLIC, null);
        constructor.addParam("rootObject", SCENE_OBJECT_QNAME);

        List<String> names = namedNodeNamesFromScene(document);
        Map<String, ASField> fieldByName = new HashMap<String, ASField>();

        for (String name : names) {
            ASField field = addField(clazz, name, constructor);
            fieldByName.put(name, field);
        }

        for (String name : names) {
            addGetMethod(clazz, name, fieldByName.get(name));
        }
    }

    private List<String> namedNodeNamesFromScene(Document document) {
        // Use XPath or similar to find named nodes?
        NodeList sceneObjects = document.getElementsByTagName("SceneObject");
        List<String> names = new ArrayList<String>();
        for (int i=0; i < sceneObjects.getLength(); i++)
        {
            Node sceneObject = sceneObjects.item(i);
            String name = nameAttribute(sceneObject);
            if (name != null)
            {
                names.add(name);
            }
        }
        return names;
    }

    private ASField addField(ASClassType clazz, String name, ASMethod constructor) {
        String capitalizedName = capitalize(name);
        ASField field = clazz.newField("_so" + capitalizedName, PROTECTED, SCENE_OBJECT_QNAME);
        constructor.addStmt(field.getName() + " = rootObject.find(\"" + name + "\")");
        return field;
    }

    private void addGetMethod(ASClassType clazz, String name, ASField field) {
        clazz.newMethod("get" + capitalize(name), PUBLIC, field.getType()).newReturn(field.getName());
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
