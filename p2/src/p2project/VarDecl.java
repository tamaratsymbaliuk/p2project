package p2project;

public class VarDecl extends  ASTNode {
    Var varNode;
    ASTNode expr;

    public  VarDecl(Var varNode, ASTNode expr) {
        this.varNode = varNode;
        this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "VarDecl{" + varNode.name + "}");
        expr.print(indent + indent);
    }


}