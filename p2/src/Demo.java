import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {

        String input = """
                x = 5;
                if (x > 3) {
                y = x + 2;
                } else {
                y = x * (2 + 3);
                }
                print y;
                """;
        Lexer lexer = new Lexer(input);
        for (Lexer.Token token: lexer) {
            System.out.println(token);
        }
        List<Lexer.Token> tokens = lexer.getTokens(); // Use a method to get tokens
        Parser parser = new Parser(tokens);
        ASTNode root = parser.parse();
        root.print(" ");

    }

}
