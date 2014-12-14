package multi;

import cpp.CppASTGenerator;
import cpp.CppWriter;
import multi.MultiLanguageProject;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.ActionScriptWriter;

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
