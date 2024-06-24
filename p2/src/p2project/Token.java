package p2project;

public class Token {
    public enum Type {
        CONFIG, UPDATE, COMPUTE, SHOW, CONFIGS, STRING, NUMBER, IDENTIFIER, ASSIGNMENT, REFERENCES, OPERATOR, MULTIPLY, DIVIDE, PLUS, MINUS, LPAREN, RPAREN, ASSIGN, VAR, LBRACE, RBRACE, SEMICOLON, COLON, IF, ELSE, PRINT, RELATIONAL_OPERATOR

    }
    public final Type type;
    public final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}

