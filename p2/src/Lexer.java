import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer implements Iterable<Lexer.Token> {
    private final String input;
    private final List<Token> tokens;
    private int current;

    public Lexer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
        this.current = 0;
        tokenize();
    }

    private void tokenize() {
        while (current < input.length()) {
            char ch = input.charAt(current);
            switch (ch) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    current++;
                    break;
                case '=':
                    tokens.add(new Token(TokenType.ASSIGNMENT, "="));
                    current++;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '>':
                case '<':
                    tokens.add(new Token(TokenType.OPERATOR, Character.toString(ch)));
                    current++;
                    break;
                case '(':
                case ')':
                case '{':
                case '}':
                case ';':
                    tokens.add(new Token(TokenType.PUNCTUATION, Character.toString(ch)));
                    current++;
                    break;
                default:
                    if (Character.isLetter(ch)) {
                        tokens.add(new Token(TokenType.IDENTIFIER, readIdentifier()));
                    } else if (Character.isDigit(ch)) {
                        tokens.add(new Token(TokenType.NUMBER, readNumber()));
                    } else if (ch == '"') {
                        tokens.add(new Token(TokenType.STRING, readString()));
                    } else if (ch == 'p' && input.substring(current, current + 5).equals("print")) {
                        tokens.add(new Token(TokenType.PRINT, "print"));
                        current += 5;
                    } else {
                        throw new LexerException("Unsupported character: " + ch);
                    }
            }
        }
    }

    private String readIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && (Character.isLetterOrDigit(input.charAt(current)) || input.charAt(current) == '_')) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private String readNumber() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && Character.isDigit(input.charAt(current))) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private String readString() {
        StringBuilder builder = new StringBuilder();
        current++; // Move past the opening double quote
        while (current < input.length() && input.charAt(current) != '"') {
            builder.append(input.charAt(current));
            current++;
        }
        current++; // Move past the closing double quote
        return builder.toString();
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    static class Token {
        final TokenType type;
        final String value;

        Token(TokenType type, String value) {
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

    enum TokenType {
        IDENTIFIER, NUMBER, ASSIGNMENT, OPERATOR, PUNCTUATION, STRING, PRINT
    }
}
