package sun.tools.tree;

public class StatementAccessor
{
    public static int declarationModifier(DeclarationStatement statement)
    {
        return statement.mod;
    }

    public static Expression declarationType(DeclarationStatement statement)
    {
        return statement.type;
    }
}
