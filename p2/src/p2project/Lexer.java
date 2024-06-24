package p2project;

import p2project.LexerException;
import p2project.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static p2project.Token.Type.*;

public class Lexer implements Iterable<Token> {
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
                    if (current + 1 < input.length() && input.charAt(current + 1) == '=') {
                        tokens.add(new Token(RELATIONAL_OPERATOR, "=="));
                        current += 2;
                    } else {
                        tokens.add(new Token(ASSIGN, "="));
                        current++;
                    }
                    break;
                case '{':
                    tokens.add(new Token(LBRACE, "{"));
                    current++;
                    break;
                case '}':
                    tokens.add(new Token(RBRACE, "}"));
                    current++;
                    break;
                case '(':
                    tokens.add(new Token(LPAREN, "("));
                    current++;
                    break;
                case ')':
                    tokens.add(new Token(RPAREN, ")"));
                    current++;
                    break;
                case ';':
                    tokens.add(new Token(SEMICOLON, ";"));
                    current++;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    tokens.add(new Token(OPERATOR, Character.toString(ch)));
                    current++;
                    break;
                case '>':
                case '<':
                case '!':
                    if (current + 1 < input.length() && input.charAt(current + 1) == '=') {
                        tokens.add(new Token(RELATIONAL_OPERATOR, Character.toString(ch) + "="));
                        current += 2;
                    } else {
                        tokens.add(new Token(RELATIONAL_OPERATOR, Character.toString(ch)));
                        current++;
                    }
                    break;
                default:
                    if (isDigit(ch)) {
                        tokens.add(new Token(NUMBER, readNumber()));
                    } else if (isAlpha(ch)) {
                        String identifier = readIdentifier();
                        tokens.add(new Token(deriveTokenType(identifier), identifier));
                    } else {
                        throw new LexerException("Unsupported character: " + ch);
                    }
            }
        }
    }

    private Token.Type deriveTokenType(String identifier) {
        return switch (identifier) {
            case "config" -> CONFIG;
            case "update" -> UPDATE;
            case "compute" -> COMPUTE;
            case "show" -> SHOW;
            case "configs" -> CONFIGS;
            case "var" -> VAR;
            case "if" -> IF;
            case "else" -> ELSE;
            case "print" -> PRINT;
            default -> IDENTIFIER;
        };
    }

    private String readIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && isAlphaNumeric(input.charAt(current))) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private String readNumber() {
        StringBuilder builder = new StringBuilder();
        while (current < input.length() && isDigit(input.charAt(current))) {
            builder.append(input.charAt(current));
            current++;
        }
        return builder.toString();
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
