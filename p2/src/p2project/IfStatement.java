package p2project;

public class IfStatement extends ASTNode {
    public final ASTNode condition;
    public final ASTNode ifBlock;
    public final ASTNode elseBlock;

    public IfStatement(ASTNode condition, ASTNode ifBlock, ASTNode elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "IfStatement:");
        System.out.println(indent + "|- Condition:");
        condition.print(indent + "  ");
        System.out.println(indent + "|- If Block:");
        ifBlock.print(indent + "  ");
        if (elseBlock != null) {
            System.out.println(indent + "|- Else Block:");
            elseBlock.print(indent + "  ");
        }
    }
}
