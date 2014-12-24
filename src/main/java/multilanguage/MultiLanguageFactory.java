package multilanguage;

import cpp.CppASTGenerator;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;

public class MultiLanguageFactory extends ActionScriptFactory
{
    private CppASTGenerator cppASTGenerator;

    public MultiLanguageFactory()
    {
        this.cppASTGenerator = new CppASTGenerator();
    }

    @Override
    public ActionScriptProject newEmptyASProject(String outputLocation)
    {
        return new MultiLanguageProject(outputLocation, this);
    }
}
