public class BinaryOpNode extends ASTNode {
        private final String operator;
        private final ASTNode left;
        private final ASTNode right;

    public BinaryOpNode(String operator, ASTNode left, ASTNode right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public void print(String indent) {
            System.out.println(indent + "BinaryOp(" + left + " " + operator + " " + right + ")");
        }

        @Override
        public String toString() {
            return "BinaryOpNode{" + "operator='" + operator + '\'' + ", left=" + left + ", right=" + right + '}';
        }
}
