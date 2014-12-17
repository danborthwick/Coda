package cpp;

import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.impl.ASTActionScriptProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CppProject extends ASTActionScriptProject
{
    public CppProject(ActionScriptFactory fact)
    {
        super(fact);
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

    @Override
    public void writeAll() throws IOException
    {
        for (ASCompilationUnit cu : (List<ASCompilationUnit>) getCompilationUnits()) {
            write(getOutputLocation(), cu);
        }
    }

    private void write(String destinationDir, ASCompilationUnit cu) throws IOException
    {
        String filename = filenameFor(cu);
        File destFile = new File(destinationDir, filename);
        destFile.getParentFile().mkdirs();
        FileOutputStream os = new FileOutputStream(destFile);
        OutputStreamWriter out = new OutputStreamWriter(os);
        new CppWriter().write(out, cu);
        out.close();
    }

}
