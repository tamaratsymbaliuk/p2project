package p2project;

public class BinaryOpNode extends ASTNode {
    ASTNode left;
    ASTNode right;
    Token operationToken;

    public BinaryOpNode(ASTNode left, ASTNode right, Token operationToken) {
        this.left = left;
        this.right = right;
        this.operationToken = operationToken;
    }

    public void print(String indent) {
        System.out.println(indent + "Op{" + operationToken.value + "}");
        left.print(indent + indent);
        right.print(indent + indent);

    }

}

