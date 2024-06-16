public class AssignmentNode extends ASTNode {
    private final String variable;
    private final ASTNode value;

    public AssignmentNode(String variable, ASTNode value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Assignment{" + variable + " = " + value + "}");
    }

    @Override
    public String toString() {
        return "AssignmentNode{" + "variable='" + variable + '\'' + ", value=" + value + '}';
    }
}
