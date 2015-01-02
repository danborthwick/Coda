package cpp;

import uk.co.badgersinfoil.metaas.dom.ASConstants;

import java.util.HashMap;
import java.util.Map;

public class CppTypeTranslator
{
    public static final Map<String, String> typeMap;
    static {
        typeMap = new HashMap<>();
        typeMap.put(ASConstants.TYPE_NUMBER, "double");
        typeMap.put(ASConstants.TYPE_STRING, "std::string");
    }

    public String translate(String asType)
    {
        return typeMap.getOrDefault(asType, asType);
    }
}
