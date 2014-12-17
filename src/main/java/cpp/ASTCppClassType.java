package cpp;

import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.ASTASClassType;
import uk.co.badgersinfoil.metaas.impl.ASTASField;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
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
