package p2project;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int currentPos;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        currentPos = 0;
        currentToken = tokens.get(currentPos);
    }

    public ASTNode parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (currentToken != null) {
            statements.add(statement());
            if (currentToken != null && currentToken.type == Token.Type.SEMICOLON) {
                consume(Token.Type.SEMICOLON);
            }
        }
        return new Block(statements);
    }

    private ASTNode statement() {
        if (currentToken.type == Token.Type.LBRACE) {
            return block();
        }
        if (currentToken.type == Token.Type.VAR) {
            return declaration();
        }
        if (currentToken.type == Token.Type.IDENTIFIER) {
            return assignment();
        }
        if (currentToken.type == Token.Type.IF) {
            return ifStatement();
        }
        if (currentToken.type == Token.Type.PRINT) {
            return printStatement();
        }
        return expression();
    }

    private ASTNode assignment() {
        Var varNode = var();
        consume(Token.Type.ASSIGN);
        ASTNode right = expression();
        return new Assign(varNode, right);
    }

    private ASTNode declaration() {
        consume(Token.Type.VAR);
        Var varNode = var();
        consume(Token.Type.ASSIGN);
        ASTNode right = expression();
        return new VarDecl(varNode, right);
    }

    private Var var() {
        Token token = currentToken;
        consume(Token.Type.IDENTIFIER);
        return new Var(token);
    }

    private ASTNode block() {
        consume(Token.Type.LBRACE);
        List<ASTNode> statements = new ArrayList<>();
        while (currentToken.type != Token.Type.RBRACE) {
            statements.add(statement());
            if (currentToken.type == Token.Type.SEMICOLON) {
                consume(Token.Type.SEMICOLON);
            }
        }
        consume(Token.Type.RBRACE);
        return new Block(statements);
    }

    private ASTNode expression() {
        ASTNode node = term();

        while (currentToken != null && (currentToken.type == Token.Type.PLUS || currentToken.type == Token.Type.MINUS || currentToken.type == Token.Type.OPERATOR)) {
            Token token = currentToken;
            consume(token.type);
            node = new BinaryOpNode(node, term(), token);
        }
        return node;
    }

    private ASTNode term() {
        ASTNode node = factor();

        while (currentToken != null && (currentToken.type == Token.Type.MULTIPLY || currentToken.type == Token.Type.DIVIDE)) {
            Token token = currentToken;
            consume(token.type);
            node = new BinaryOpNode(node, factor(), token);
        }
        return node;
    }

  private ASTNode factor() {
        Token token = currentToken;

        if (token.type == Token.Type.NUMBER) {
            consume(Token.Type.NUMBER);
            return new NumberNode(token);
        } else if (token.type == Token.Type.IDENTIFIER) {
            Var varNode = var(); // Handle the variable case
            return varNode;
        } else if (token.type == Token.Type.LPAREN) {
            consume(Token.Type.LPAREN);
            ASTNode node = expression();
            consume(Token.Type.RPAREN);
            return node;
        }

        throw new ParserException("Unexpected token found for Factor: " + token);
    }

    /*
   private ASTNode factor() {
        Token token = currentToken;

        if (token.type == Token.Type.NUMBER) {
            consume(Token.Type.NUMBER);
            return new NumberNode(token);
        } else if (token.type == Token.Type.IDENTIFIER) {
            // Check if the next token is '=' for assignment
            Token nextToken = currentPos + 1 < tokens.size() ? tokens.get(currentPos + 1) : null;
            if (nextToken != null && nextToken.type == Token.Type.ASSIGN) {
                Var varNode = var(); // Handle the variable case
                consume(Token.Type.ASSIGN);
                ASTNode right = expression();
                return new Assign(varNode, right);
            } else {
                Var varNode = var(); // Handle the variable case
                return varNode;
            }
        } else if (token.type == Token.Type.LPAREN) {
            consume(Token.Type.LPAREN);
            ASTNode node = expression();
            consume(Token.Type.RPAREN);
            return node;
        } else if (token.type == Token.Type.OPERATOR && token.value.equals("+")) {
            consume(Token.Type.OPERATOR);
            ASTNode node = expression();
            return node;
        }  else if (token.type == Token.Type.OPERATOR && token.value.equals("*")) {
            consume(Token.Type.OPERATOR);
            return new BinaryOpNode(factor(), factor(), token);
        } else {
            throw new ParserException("Unexpected token found for Factor: " + token);
        }
    }
*/
    private ASTNode ifStatement() {
        consume(Token.Type.IF);  // Consume IF token
        consume(Token.Type.LPAREN);  // Expect and consume LPAREN

        // Parse condition expression
        ASTNode condition = expression();

        // Check for additional tokens before RPAREN
        while (currentToken != null &&
               (currentToken.type == Token.Type.IDENTIFIER ||
                currentToken.type == Token.Type.NUMBER ||
                currentToken.type == Token.Type.STRING ||
                currentToken.type == Token.Type.RELATIONAL_OPERATOR)) {

            // Additional handling can go here if needed

            // Consume the current token and move to the next one
            consume(currentToken.type);
        }

        // Expect and consume RPAREN after parsing condition
        consume(Token.Type.RPAREN);

        // Parse if block
        ASTNode ifBlock = block();

        ASTNode elseBlock = null;
        // Check for else block
        if (currentToken != null && currentToken.type == Token.Type.ELSE) {
            consume(Token.Type.ELSE);  // Consume ELSE token
            elseBlock = block();  // Parse else block
        }

        return new IfStatement(condition, ifBlock, elseBlock);
    }


    private ASTNode printStatement() {
        consume(Token.Type.PRINT);
        ASTNode expression = expression();
        return new PrintStatement(expression);
    }

    private void consume(Token.Type type) {
        if (currentToken.type == type) {
            currentPos++;
            if (currentPos < tokens.size()) {
                currentToken = tokens.get(currentPos);
            } else {
                currentToken = null;
            }
        } else {
            throw new ParserException("Unexpected token: " + currentToken + ", expected: " + type);
        }
    }
}
