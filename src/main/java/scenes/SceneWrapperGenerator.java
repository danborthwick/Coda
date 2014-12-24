package scenes;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class SceneWrapperGenerator
{
    public static final String[] SUPPORTED_EXTENSIONS = new String[]{"xml"};

    public ActionScriptProject generate(ActionScriptFactory factory, File scenesDirectory) throws ParserConfigurationException, IOException, SAXException {

        ActionScriptProject project = factory.newEmptyASProject(null);
        project.addClasspathEntry("libs/swc/spaceland-0.43.19.swc");
        SceneClassGenerator classGenerator = new SceneClassGenerator(project);

        Iterator iterator = FileUtils.iterateFiles(scenesDirectory, SUPPORTED_EXTENSIONS, true);
        while (iterator.hasNext())
        {
            File sceneFile = (File) iterator.next();
            String relativePath = scenesDirectory.toURI().relativize(sceneFile.toURI()).toString();
            String className = relativePath.replace("/", ".");
            className = className.substring(0, className.length() - 4);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(sceneFile);

            classGenerator.addClassFromXMLDocument(className, document);
        }

        return project;
    }
}
