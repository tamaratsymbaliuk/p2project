package p2project;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SemanticAnalyzer {
    private final Stack<Set<String>> scopes = new Stack<>();


    void visit(ASTNode node) {
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
            for (ASTNode statement: block.statements) {
                visit(statement);
            }
            scopes.pop(); // exit scope
            /*
            var x = 5
            var x = 4   Scope (set { x } )
            { // push Scope 2              Scope (Set { x } )+ Scope2 (Set { } )
              var y = 5         Scope2 (Set { y } )
              y = 1 + 5

            } // pop Scope 2
                       Scope (Set { x } )
              y = 5

              *
             /\
            x  3

             */

        } else {
            throw new ParserException("Unexpected AST Node: " + node.getClass().getCanonicalName());
        }
    }

    private boolean isVariableDefined(String varName) {
        for (Set<String> scope: scopes) {
            if (scope.contains(varName)) return true; // checking if scope contains the variable
        }
        return false;
    }
}
