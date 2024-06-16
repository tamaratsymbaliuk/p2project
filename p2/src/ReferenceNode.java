public class ReferenceNode extends ASTNode {
    private final String name;

    public ReferenceNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Reference{" + name + "}");
    }

    @Override
    public String toString() {
        return "ReferenceNode{" + "name='" + name + '\'' + '}';
    }
}
