import java.util.*;

public class Parser {
    private final List<Lexer.Token> tokens;
    private int current = 0;

    public Parser(List<Lexer.Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        return parseStatements();
    }

    private ASTNode parseStatements() {
        List<ASTNode> statements = new ArrayList<>();
        while (current < tokens.size() && !match(Lexer.TokenType.PUNCTUATION, "}")) {
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private ASTNode parseStatement() {
        Lexer.Token token = tokens.get(current);
        switch (token.type) {
            case IDENTIFIER:
                return parseAssignment();
            case PRINT:
                return parsePrint();
            case PUNCTUATION:
                if ("{".equals(token.value)) {
                    return parseBlock();
                }
                if ("}".equals(token.value)) {
                    current++; // Move past the '}'
                    break;
                }
                break;
            default:
                throw new ParserException("Unexpected token: " + token);
        }
        throw new ParserException("Unexpected token: " + token);
    }

    private ASTNode parseAssignment() {
        Lexer.Token identifier = tokens.get(current++);
        expect(Lexer.TokenType.ASSIGNMENT, "=");
        ASTNode value = parseExpression();
        expect(Lexer.TokenType.PUNCTUATION, ";");
        return new AssignmentNode(identifier.value, value);
    }

    private ASTNode parsePrint() {
        expect(Lexer.TokenType.PRINT, "print");
        ASTNode value = parseExpression();
        expect(Lexer.TokenType.PUNCTUATION, ";");
        return new PrintNode(value);
    }

    private ASTNode parseBlock() {
        expect(Lexer.TokenType.PUNCTUATION, "{");
        List<ASTNode> statements = new ArrayList<>();
        while (!match(Lexer.TokenType.PUNCTUATION, "}")) {
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private ASTNode parseExpression() {
        return parseBinaryOperation();
    }

    private ASTNode parseBinaryOperation() {
        ASTNode left = parsePrimary();
        while (match(Lexer.TokenType.OPERATOR)) {
            Lexer.Token operator = previous();
            ASTNode right = parsePrimary();
            left = new BinaryOpNode(operator.value, left, right);
        }
        return left;
    }

    private ASTNode parsePrimary() {
        Lexer.Token token = tokens.get(current++);
        switch (token.type) {
            case IDENTIFIER:
                return new ReferenceNode(token.value);
            case NUMBER:
                return new NumberNode(Integer.parseInt(token.value));
            case PUNCTUATION:
                if ("(".equals(token.value)) {
                    ASTNode expression = parseExpression();
                    expect(Lexer.TokenType.PUNCTUATION, ")");
                    return expression;
                }
                break;
            default:
                throw new ParserException("Unexpected token: " + token);
        }
        throw new ParserException("Unexpected token: " + token);
    }

    private void expect(Lexer.TokenType type, String value) {
        Lexer.Token token = tokens.get(current++);
        /*if (token.type != type || !token.value.equals(value)) {
            throw new ParserException("Expected " + value + " but found " + token);
        }*/
    }

    private boolean match(Lexer.TokenType type) {
        if (current < tokens.size() && tokens.get(current).type == type) {
            current++;
            return true;
        }
        return false;
    }

    private boolean match(Lexer.TokenType type, String value) {
        if (current < tokens.size() && tokens.get(current).type == type && tokens.get(current).value.equals(value)) {
            current++;
            return true;
        }
        return false;
    }

    private Lexer.Token previous() {
        return tokens.get(current - 1);
    }

    static class ParserException extends RuntimeException {
        public ParserException(String message) {
            super(message);
        }
    }
}
