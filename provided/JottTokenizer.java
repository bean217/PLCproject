package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Benjamin Piro, brp8396@rit.edu
 **/

import java.util.ArrayList;
import java.io.PushbackReader;
import java.io.FileReader;
import java.io.IOException;
public class JottTokenizer {

    private static final String SYNTAX_ERR_TEMPLATE = "Syntax Error\nInvalid token \"%s\". \"%s\" expects following \"%s\"\n%s:%d";

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
            // current token being processed
            StringBuilder token = new StringBuilder();
            // read first character
            int c = pbr.read();
            // boolean to indicate if the current character is being reprocessed; occurs after a token is accepted
            boolean reprocessChar = false;
            while (true) {
                // increment line number when a newline is read, but only the first time it is read
                if (c == '\n' && !reprocessChar) lineNumber++;
                // process character
                boolean result = jdfa.getNextState(c);

                if (!result) {
                    // entered invalid state; check for valid token
                    if (jdfa.wasInAccept()) {
                        // accept token
                        tokens.add(new Token(token.toString(), filename, lineNumber, getTokenType(jdfa.getPreviousStateID())));
                        // re-evaluate the current character if not EOF
                        if (c == -1) break;
                        // indicate that the current character should be reprocessed
                        reprocessChar = true;
                    } else {
                        // reject token
                        String tokenStr = token.isEmpty() ? "<empty>" : token.toString();
                        // print syntax error message
                        System.err.println(String.format(SYNTAX_ERR_TEMPLATE, tokenStr, tokenStr, jdfa.getExpectedNext(), filename, lineNumber));
                        // tokenization failed, so return null
                        return null;
                    }
                } else {
                    // entered a valid state; append current character
                    token.append((char)c);
                    // and read next character
                    c = pbr.read();
                    // indicate that the previous character is not being reprocessed
                    reprocessChar = false;
                }

                if (jdfa.isInStartState()) {
                    // clear token if JottDFA is in the start state
                    token.setLength(0);
                }
            }
		    
            return tokens;
        } catch(IOException ioe) {
            // error opening .jott file
            System.err.println(String.format("Error opening file '%s'", filename));
        }

        // Exception occurred, return null
        return null;
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
}