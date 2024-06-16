public class PrintNode extends ASTNode {
    private final ASTNode value;

    public PrintNode(ASTNode value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Print{" + value + "}");
    }

    @Override
    public String toString() {
        return "PrintNode{" + "value=" + value + '}';
    }
}
