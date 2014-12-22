package cpp;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class CppBuilder
{
    public AS3ASTCompilationUnit synthesizeClass(String qualifiedName) {
        LinkedListTree unit = ASTUtils.newImaginaryAST(AS3Parser.COMPILATION_UNIT);
        LinkedListTree pkg = ASTUtils.newAST(AS3Parser.PACKAGE, "namespace");
        pkg.appendToken(TokenBuilder.newSpace());
        unit.addChildWithTokens(pkg);
        pkg.appendToken(TokenBuilder.newSpace());
        String packageName = packageNameFrom(qualifiedName);
        if (packageName != null) {
            pkg.addChildWithTokens(AS3FragmentParser.parseIdent(packageName));
            pkg.appendToken(TokenBuilder.newSpace());
        }
        LinkedListTree packageBlock = newBlock();
        pkg.addChildWithTokens(packageBlock);
        String className = typeNameFrom(qualifiedName);

        LinkedListTree clazz = synthesizeCppClass(className);
        ASTUtils.addChildWithIndentation(packageBlock, clazz);
        return new CppASTCompilationUnit(unit, this);
    }

    private LinkedListTree synthesizeCppClass(String className) {
        LinkedListTree clazz = ASTUtils.newImaginaryAST(AS3Parser.CLASS_DEF);
        LinkedListTree modifiers = ASTUtils.newImaginaryAST(AS3Parser.MODIFIERS);
        clazz.addChildWithTokens(modifiers);
        clazz.appendToken(TokenBuilder.newClass());
        clazz.appendToken(TokenBuilder.newSpace());
        clazz.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, className));
        clazz.appendToken(TokenBuilder.newSpace());
        clazz.addChildWithTokens(newTypeBlock());
        return clazz;
    }

    public LinkedListTree newBlock() {
        return newBlock(AS3Parser.BLOCK);
    }

    public LinkedListTree newTypeBlock() {
        return newBlock(AS3Parser.TYPE_BLOCK);
    }

    private LinkedListTree newBlock(int type) {
        LinkedListTree ast = ASTUtils.newParentheticAST(type,
                AS3Parser.LCURLY, "{",
                AS3Parser.RCURLY, "}");
        LinkedListToken nl = TokenBuilder.newNewline();
        ast.getInitialInsertionAfter().afterInsert(nl);
        ast.setInitialInsertionAfter(nl);
        return ast;
    }

    public ASTCppField newField(String name, Visibility visibility, String type)
    {
        if (name.indexOf('.') != -1) {
            throw new SyntaxException("field name must not contain '.'");
        }
        LinkedListTree decl = ASTUtils.newImaginaryAST(AS3Parser.VAR_DEF);
        decl.addChildWithTokens(ModifierUtils.toModifiers(visibility));
        decl.appendToken(TokenBuilder.newSpace());
        decl.appendToken(new LinkedListToken(AS3Parser.IDENT, type));
        decl.appendToken(TokenBuilder.newSpace());
        decl.addChildWithTokens(ASTUtils.newAST(AS3Parser.IDENT, name));
        decl.appendToken(TokenBuilder.newSemi());
        return new ASTCppField(decl);

    }

    private static String typeNameFrom(String qualifiedName) {
        int p = qualifiedName.lastIndexOf('.');
        if (p == -1) {
            return qualifiedName;
        }
        return qualifiedName.substring(p+1);
    }

    private static String packageNameFrom(String qualifiedName) {
        int p = qualifiedName.lastIndexOf('.');
        if (p == -1) {
            return null;
        }
        return qualifiedName.substring(0, p).replace(".", "::");
    }

    public ASTASMethod newClassMethod(String name, Visibility visibility, String returnType)
    {
        LinkedListTree def = ASTUtils.newImaginaryAST(AS3Parser.METHOD_DEF);
        def.addChildWithTokens(ModifierUtils.toModifiers(visibility));
        def.appendToken(TokenBuilder.newSpace());
        if (returnType != null) {   // Should just be for constructors, may need to handle typeless functions
            def.appendToken(new LinkedListToken(AS3Parser.IDENT, returnType));
            def.appendToken(TokenBuilder.newSpace());
        }
        LinkedListTree methName = ASTUtils.newAST(AS3Parser.IDENT, name);
        def.addChildWithTokens(methName);
        def.addChildWithTokens(ASTUtils.newParentheticAST(AS3Parser.PARAMS, AS3Parser.LPAREN, "(", AS3Parser.RPAREN, ")"));
        def.appendToken(TokenBuilder.newSpace());
        LinkedListTree block = newBlock();
        def.addChildWithTokens(block);

        return new ASTASMethod(def);

    }
}
