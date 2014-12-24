package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.HashMap;
import java.util.Map;

public class ModifierUtils
{
    private static class ModInfo {
        public int tokenType;
        public Visibility vis;
        public String keyword;

        public ModInfo(int tokenType, Visibility vis, String keyword) {
            this.tokenType = tokenType;
            this.vis = vis;
            this.keyword = keyword;
        }
    }


    private static Map modinfoByTokenType = new HashMap();
    private static Map modinfoByVisibility = new HashMap();

    static {
        mapMod(AS3Parser.PRIVATE, Visibility.PRIVATE, "private:");
        mapMod(AS3Parser.PUBLIC, Visibility.PUBLIC, "public:");
        mapMod(AS3Parser.PROTECTED, Visibility.PROTECTED, "protected:");
        mapMod(AS3Parser.INTERNAL, Visibility.INTERNAL, "public:");
        mapMod(Integer.MIN_VALUE, Visibility.DEFAULT, "protected:");
    }

    private static void mapMod(int tokenType, Visibility vis, String keyword) {
        ModInfo inf = new ModInfo(tokenType, vis, keyword);
        modinfoByTokenType.put(new Integer(tokenType), inf);
        modinfoByVisibility.put(vis, inf);
    }

    private static ModInfo getModInfo(int tokenType) {
        return (ModInfo)modinfoByTokenType.get(new Integer(tokenType));
    }

    private static ModInfo getModInfo(Visibility vis) {
        ModInfo result = (ModInfo)modinfoByVisibility.get(vis);
        if (result == null) {
            throw new IllegalArgumentException("unknown kind of visibility: "+vis);
        }
        return result;
    }

    public static LinkedListTree toModifiers(Visibility visibility, Visibility scopeVisibility) {
        if (visibility.equals(scopeVisibility)) {
            return ASTUtils.newPlaceholderAST(AS3Parser.MODIFIERS);
        }
        LinkedListTree modifiers = ASTUtils.newImaginaryAST(AS3Parser.MODIFIERS);
        ModInfo modInfo = getModInfo(visibility);
        LinkedListTree mod = ASTUtils.newAST(modInfo.tokenType, modInfo.keyword);
        mod.appendToken(TokenBuilder.newSpace());
        modifiers.addChildWithTokens(mod);
        mod.appendToken(TokenBuilder.newNewline());
        return modifiers;
    }

}
