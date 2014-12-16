package cpp;

import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;

public class CppFactory extends ActionScriptFactory
{
    private CppBuilder builder;

    public CppFactory(CppBuilder builder)
    {
        this.builder = builder;
    }

    @Override
    public ASCompilationUnit newClass(String qualifiedClassName)
    {
        return builder.synthesizeClass(qualifiedClassName);
    }
}
