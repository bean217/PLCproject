package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Benjamin Piro, brp8396@rit.edu
 **/

import java.util.ArrayList;

import provided.JottDFA.JottDFAException;

import java.io.PushbackReader;
import java.io.FileReader;
import java.io.IOException;
public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
        ArrayList<Token> tokens = new ArrayList<>();
        JottDFA jdfa = new JottDFA();
        
        // Read character stream from file
        int lineNumber = 1;
        try (PushbackReader pbr = new PushbackReader(new FileReader(filename))) {
            int c = pbr.read();

            // Read character stream until the end of the file
            // pbr returns -1 at the end of file stream
            while (c != -1) {
                // increment line number when a newline is read
                if (c == '\n') lineNumber++;
                // process character
                processChar(c, lineNumber, filename, tokens, jdfa);
                // read next character
                c = pbr.read();
            }

            // EOF reached, check for last token (if it exists)
            processChar(c, lineNumber, filename, tokens, jdfa);
		    
            return tokens;
        } catch(JottSyntaxException e) {
            System.err.println(e.getMessage());
        } catch(IOException ioe) {
            // error opening .jott file
            System.err.println(String.format("Error opening file '%s'", filename));
        }

        return null;
	  }

    private static void processChar(int c, int lineNumber, String filename, ArrayList<Token> tokens, JottDFA jdfa) {
        // Set the current state of JottDFA to the next state based on c
        String maybeToken = jdfa.getNextState(c);

        if (maybeToken != null) {
            boolean a = jdfa.wasInAccept();
            if (jdfa.wasInAccept() && !maybeToken.isEmpty()) {
                System.out.println("\t\t\tACCEPT");
                // JottDFA recognized a token
                tokens.add(
                    new Token(maybeToken, filename, lineNumber, getTokenType(jdfa.getPreviousStateID()))
                );
            } else {
                throw new JottSyntaxException(lineNumber, filename, maybeToken, jdfa.getPreviousExpectedNext());
            }
        }
    }

    /**
     * Gets a token's type based on a JottDFA accept state
     * @param state JottDFA state accessed prior to accepting (AKA the JottDFA accept state)
     * @rit.pre state is always an accepting state
     * @rit.post returned TokenType is always one of the defined enum values
     * @return TokenType related to state
     */
    private static TokenType getTokenType(JottDFA.StateID state) {
        if (state == JottDFA.StateID.COMMA) return TokenType.COMMA;
        else if (state == JottDFA.StateID.R_BRACKET) return TokenType.R_BRACKET;
        else if (state == JottDFA.StateID.L_BRACKET) return TokenType.L_BRACKET;
        else if (state == JottDFA.StateID.R_BRACE) return TokenType.R_BRACE;
        else if (state == JottDFA.StateID.L_BRACE) return TokenType.L_BRACE;
        else if (state == JottDFA.StateID.ASSIGN) return TokenType.ASSIGN;
        else if (state == JottDFA.StateID.REL_OP_1 || state == JottDFA.StateID.REL_OP_2) return TokenType.REL_OP;
        else if (state == JottDFA.StateID.MATH_OP) return TokenType.MATH_OP;
        else if (state == JottDFA.StateID.SEMICOLON) return TokenType.SEMICOLON;
        else if (state == JottDFA.StateID.NUMBER_1 || state == JottDFA.StateID.NUMBER_2) return TokenType.NUMBER;
        else if (state == JottDFA.StateID.ID_KEYWORD) return TokenType.ID_KEYWORD;
        else if (state == JottDFA.StateID.COLON) return TokenType.COLON;
        else return TokenType.STRING;
    }

    private static class JottSyntaxException extends RuntimeException {
        private static final String MSG_TEMPLATE = "Syntax Error\nInvalid token \"%s\". \"%s\" expects following \"%s\"\n%s:%d";

        private JottSyntaxException(int lineNumber, String filename, String token, String expectedNext) {
            super(String.format(MSG_TEMPLATE, token, token, expectedNext, filename, lineNumber));
        }
    }
}