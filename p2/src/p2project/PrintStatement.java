package p2project;

public class PrintStatement extends ASTNode {
    public final ASTNode expression;

    public PrintStatement(ASTNode expression) {
        this.expression = expression;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "PrintStatement:");
        System.out.println(indent + "|- Expression:");
        expression.print(indent + "  ");
    }
}
