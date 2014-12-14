package multi;

import cpp.CppASTGenerator;
import cpp.CppWriter;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTActionScriptProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

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
        super.writeAll();
        String cppOutputLocation = getOutputLocation() + File.separator + "cpp";

        for (Iterator i = getCompilationUnits().iterator(); i.hasNext(); ) {
            ASCompilationUnit cu = (ASCompilationUnit)i.next();
            writeCpp(cppOutputLocation, cu);
        }
    }

    private void writeCpp(String destinationDir, ASCompilationUnit cu) throws IOException {
        String filename = filenameFor(cu);
        File destFile = new File(destinationDir, filename);
        destFile.getParentFile().mkdirs();
        FileOutputStream os = new FileOutputStream(destFile);
        OutputStreamWriter out = new OutputStreamWriter(os);
        new CppWriter(cppASTGenerator).write(out, cu);
        out.close();
    }

    private static String filenameFor(ASCompilationUnit unit) {
        String name;
        String pkg = unit.getPackageName();
        if (pkg == null || pkg.equals("")) {
            name = unit.getType().getName();
        } else {
            name = unit.getPackageName() + "." + unit.getType().getName();
        }
        return name.replace('.', File.separatorChar) + ".h";
    }

}
