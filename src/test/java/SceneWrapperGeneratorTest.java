import junit.framework.TestCase;
import multilanguage.MultiLanguageFactory;
import org.junit.Ignore;
import org.junit.Test;
import scenes.SceneWrapperGenerator;
import uk.co.badgersinfoil.metaas.ActionScriptProject;

import java.io.File;

@Ignore
public class SceneWrapperGeneratorTest extends TestCase {

    public void testGenerationDoesNotFail() throws Exception {
        File scenesDirectory = new File(getClass().getResource("scenes").toURI());
        assertTrue(scenesDirectory.isDirectory());

        MultiLanguageFactory factory = new MultiLanguageFactory();
        ActionScriptProject project = new SceneWrapperGenerator().generate(factory, scenesDirectory);

        project.setOutputLocation(ExploratoryTest.OUTPUT_LOCATION + File.separator + "scenes");
        project.performAutoImport();
        project.writeAll();
    }
}