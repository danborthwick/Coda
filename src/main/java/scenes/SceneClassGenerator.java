package scenes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;

import java.util.*;

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

        Collection<String> names = namedNodeNamesFromScene(document);
        Map<String, ASField> fieldByName = new HashMap<String, ASField>();

        for (String name : names) {
            ASField field = addField(clazz, name);
            fieldByName.put(name, field);
        }

        ASMethod constructor = clazz.newMethod(clazz.getName(), PUBLIC, null);
        constructor.addParam("rootObject", SCENE_OBJECT_QNAME);

        for (String name : names) {
            ASField field = fieldByName.get(name);
            addGetMethod(clazz, name, field);
            addInitialisationStatement(constructor, name, field);
        }
    }

    private void addInitialisationStatement(ASMethod constructor, String nodeName, ASField field) {
        constructor.addStmt(field.getName() + " = rootObject.find(\"" + nodeName + "\")");
    }

    private Collection<String> namedNodeNamesFromScene(Document document) {
        // Use XPath or similar to find named nodes?
        NodeList sceneObjects = document.getElementsByTagName("SceneObject");
        // LinkedHashSet to avoid duplicates but maintain order
        Set<String> names = new LinkedHashSet<String>();

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

    private ASField addField(ASClassType clazz, String name) {
        return clazz.newField("_so" + capitalize(name), PROTECTED, SCENE_OBJECT_QNAME);
    }

    private void addGetMethod(ASClassType clazz, String name, ASField field) {
        ASMethod method = clazz.newMethod("get" + capitalize(name), PUBLIC, field.getType());
        method.newReturn(field.getName());
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
