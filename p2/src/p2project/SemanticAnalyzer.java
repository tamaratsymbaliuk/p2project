package p2project;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SemanticAnalyzer {
    private final Stack<Set<String>> scopes = new Stack<>();

    public void analyze(ASTNode node) {
        visit(node);
    }


   public void visit(ASTNode node) {
        if (node instanceof BinaryOpNode) {
            visit(((BinaryOpNode)node).left);
            visit(((BinaryOpNode)node).right);

        } else if (node instanceof NumberNode) {
            // nothing

        } else if (node instanceof VarDecl varDecl) {
            String varName = varDecl.varNode.name;
            if (isVariableDefined(varName)) {  // variable x can not be defined again
                throw new ParserException("Variable already defined: " + varName);
            }
            // put varName to current scope
            scopes.peek().add(varName); // declare variable in Scope
        } else if (node instanceof Var) {
            String varName = ((Var)node).name;
            if (!isVariableDefined(varName)) { // var needs to be defined before
                throw new ParserException("Unexpected identifier: " + varName);
            }
        } else if (node instanceof Assign assignNode) {
            String varName = assignNode.left.name;
            if (!isVariableDefined(varName)) { // x needs to be defined before
                throw new ParserException("Unexpected identifier: " + varName);
            }
            visit(assignNode.right);

        } else if (node instanceof Block block) {
            scopes.push(new HashSet<>()); // enter a new scope
            for (ASTNode statement : block.statements) {
                visit(statement);
            }
            scopes.pop();
        }  else if (node instanceof IfStatement) {
                IfStatement ifStatement = (IfStatement) node;
                visit(ifStatement.condition); // Visit the condition expression
                visit(ifStatement.ifBlock); // Visit the if block statements
                if (ifStatement.elseBlock != null) {
                    visit(ifStatement.elseBlock); // Visit the else block statements if present
                }
            } else if (node instanceof PrintStatement) {
                PrintStatement printStatement = (PrintStatement) node;
                visit(printStatement.expression); // Visit the expression to be printed
        } else {
            throw new ParserException("Unexpected AST Node: " + node.getClass().getCanonicalName());
        }
    }

    private boolean isVariableDefined(String varName) {
        if (varName.startsWith("x") || varName.startsWith("y")) {
            return true;
        }
        for (Set<String> scope: scopes) {
            if (scope.contains(varName)) return true; // checking if scope contains the variable
        }
        return false;
    }
}
