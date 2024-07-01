package p2project;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Map<String, Integer> valueTable = new HashMap<>();

    /*
    var x = {
        var y = 5
        y - 5
    }
    // x -> 0
    var z = 6 // x -> 0, z -> 6
    z = 5 // x -> 0, z -> 5
     */

    int visit(ASTNode node) {
        if (node instanceof BinaryOpNode binaryOpNode) {
            int left = visit(binaryOpNode.left);
            int right = visit(binaryOpNode.right);
            if (binaryOpNode.operationToken.type == Token.Type.MINUS) {
                return left - right;
            } else if (binaryOpNode.operationToken.type == Token.Type.PLUS) {
                return left + right;
            } else if (binaryOpNode.operationToken.type == Token.Type.DIVIDE) {
                return left / right;
            } else if (binaryOpNode.operationToken.type == Token.Type.MULTIPLY) {
                return left * right;
            }  else if (binaryOpNode.operationToken.type == Token.Type.OPERATOR && binaryOpNode.operationToken.value.equals("+")) {
                    return left + right;
            } else {
                throw new ParserException("Unexpected token: " + binaryOpNode.operationToken);
            }



        } else if (node instanceof NumberNode numberNode) {
            return numberNode.value;

        } else if (node instanceof VarDecl varDecl) {
            int rightExpressionResult = visit(varDecl.expr);
            valueTable.put(varDecl.varNode.name, rightExpressionResult);
            return rightExpressionResult;

        } else if (node instanceof Var var) {
            String varName = var.name;
            if (!valueTable.containsKey(varName)) {
                throw new ParserException("Variable not found: " + varName);
            }
            return valueTable.get(varName);

        } else if (node instanceof Assign assignNode) {
            int rightExpressionResult = visit(assignNode.right);
            valueTable.put(assignNode.left.name, rightExpressionResult);
            return rightExpressionResult;

        } else if (node instanceof Block block) {
            int result = 0;
            for (ASTNode statement : block.statements) {
                result = visit(statement);
            }
            return result;
        }  else if (node instanceof IfStatement ifStatement) {
                int conditionResult = visit(ifStatement.condition);
                if (conditionResult != 0) { // assuming non-zero values are true
                    return visit(ifStatement.ifBlock);
                } else if (ifStatement.elseBlock != null) {
                    return visit(ifStatement.elseBlock);
                }
                return 0;

            } else if (node instanceof PrintStatement printStatement) {
                int value = visit(printStatement.expression);
                System.out.println(value);
                return value;
        } else {
            throw new ParserException("Unexpected AST Node: " + node.getClass().getCanonicalName());
        }

    }

}
