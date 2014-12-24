package multilanguage;

import cpp.CppASTGenerator;
import cpp.CppProject;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTActionScriptProject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MultiLanguageProject extends ASTActionScriptProject
{
    private final ActionScriptFactory factory;
    private CppASTGenerator cppASTGenerator;

    public MultiLanguageProject(String outputLocation, ActionScriptFactory factory)
    {
        super(factory);
        this.factory = factory;
        cppASTGenerator = new CppASTGenerator();
        setOutputLocation(outputLocation);
    }

    @Override
    public void writeAll() throws IOException
    {

        ActionScriptProject asProject = new ASTActionScriptProject(factory);
        CppProject cppProject = new CppProject(factory);

        asProject.setOutputLocation(getOutputLocation() + File.separator + "as3");
        cppProject.setOutputLocation(getOutputLocation() + File.separator + "cpp");


        CppASTGenerator generator = new CppASTGenerator();

        for (ASCompilationUnit asUnit : (List<ASCompilationUnit>)getCompilationUnits()) {
            asProject.addCompilationUnit(asUnit);
            ASCompilationUnit cppUnit = generator.translateCompilationUnit(asUnit);
            cppProject.addCompilationUnit(cppUnit);
        }

        asProject.writeAll();
        cppProject.writeAll();
    }

}
