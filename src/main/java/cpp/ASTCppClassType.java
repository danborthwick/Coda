package cpp;

import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTCppClassType extends ASTASClassType
{
    private CppBuilder builder;

    public ASTCppClassType(LinkedListTree clazz, CppBuilder builder)
    {
        super(clazz);
        this.builder = builder;
    }

    @Override
    public ASMethod newMethod(String name, Visibility visibility, String returnType) {
        ASTASMethod method = builder.newClassMethod(name, visibility, returnType);
        addMethod(method);
        return method;
    }

    @Override
    public ASField newField(String name, Visibility visibility, String type)
    {
        ASTCppField field = builder.newField(name, visibility, type);
        addField(field);
        return field;
    }

    @Override
    public void addField(ASTASField field)
    {
        ASTUtils.addChildWithIndentation(findTypeBlock(), field.getAST());
    }
}
