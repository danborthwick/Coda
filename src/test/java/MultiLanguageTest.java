import multilanguage.MultiLanguageFactory;
import org.junit.Test;
import scenes.DialogProjectFactory;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;

import java.io.File;

public class MultiLanguageTest
{
    @Test
    public void testDialogCanBeWritten() throws Exception
    {
        ActionScriptFactory factory = new MultiLanguageFactory();
        ActionScriptProject project = new DialogProjectFactory().build(factory);
        project.setOutputLocation(ExploratoryTest.OUTPUT_LOCATION + File.separator + "multilanguage");
        project.performAutoImport();
        project.writeAll();
    }
}