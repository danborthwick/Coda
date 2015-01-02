package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppPackage extends ASTASPackage
{
    private CppBuilder builder;

    public ASTCppPackage(LinkedListTree pkg, CppBuilder builder)
    {
        super(pkg);
        this.builder = builder;
    }

    @Override
    public ASType getType()
    {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        LinkedListTree type = ASTUtils.findChildByType(block, AS3Parser.CLASS_DEF);
        if (type != null) {
            return new ASTCppClassType(type, builder);
        }
        type = ASTUtils.findChildByType(block, AS3Parser.INTERFACE_DEF);
        if (type != null) {
            return new ASTASInterfaceType(type);
        }
        return null;
    }

    @Override
    public void addImport(String name) {
        String importPath = name.replace(".", "/");
        LinkedListTree importAST = ASTUtils.newAST(AS3Parser.IMPORT, "#include \"" + importPath + "\";");
        int pos = findNextImportInsertionPoint();
        ASTUtils.addChildWithIndentation(getPkgBlockNode(), pos, importAST);
    }

    private int findNextImportInsertionPoint() {
        ASTIterator i = getPkgBlockIter();
        int index = 0;
        while (i.search(AS3Parser.IMPORT) != null) {
            index = i.getCurrentIndex() + 1;
        }
        return index;
    }

    private LinkedListTree getPkgBlockNode() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        return block;
    }

    private ASTIterator getPkgBlockIter() {
        return new ASTIterator(getPkgBlockNode());
    }

}
