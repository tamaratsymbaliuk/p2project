public class NumberNode extends ASTNode {
    private final int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Number{" + value + "}");
    }

    @Override
    public String toString() {
        return "NumberNode{" + "value=" + value + '}';
    }
}
