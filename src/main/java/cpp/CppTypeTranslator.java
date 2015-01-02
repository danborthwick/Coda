package cpp;

import sun.tools.java.Type;
import uk.co.badgersinfoil.metaas.dom.ASConstants;

public class CppTypeTranslator
{
    public String translate(String asType)
    {
        String cppType = asType;
        if (asType == ASConstants.TYPE_NUMBER)
        {
            cppType = Type.tFloat.getTypeSignature();
        }
        return cppType;
    }
}
