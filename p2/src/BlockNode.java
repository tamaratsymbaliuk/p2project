import java.util.List;

public class BlockNode extends ASTNode{
    private List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    @Override
    public void print(String indent) {
        for (ASTNode statement : statements) {
            statement.print(indent);
        }
    }

    @Override
    public String toString() {
        return "BlockNode{" + statements + "}";
    }
}
