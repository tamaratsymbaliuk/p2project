package p2project;

public class Assign extends ASTNode {
    Var left;
    ASTNode right;

    Assign (Var left, ASTNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public void print(String indent) {

    }

}
