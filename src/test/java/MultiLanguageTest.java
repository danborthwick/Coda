import multi.MultiLanguageFactory;
import org.junit.Test;
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
        project.setOutputLocation(ExploratoryTest.OUTPUT_LOCATION + File.separator + "multi");
        project.performAutoImport();
        project.writeAll();
    }
}