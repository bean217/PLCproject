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
        try (PushbackReader pbr = new PushbackReader(new FileReader(filename))) {
            int lineNumber = 1;
            int c = pbr.read();
            String currentToken = new String();

            // Read character stream until the end of the file
            // pbr returns -1 at the end of file stream
            while (c != -1) {
                if (c == '\n') lineNumber++;  // increment line number when a newline is read

                // Set the current state of JottDFA to the next state based on character input c
                boolean isReject = jdfa.getNextStateType((char)c);

                // Determine state type (reject, accept, or neither)
                if (!isReject) {
                    // accept state
                    tokens.add(
                        new Token(
                            currentToken,
                            filename,
                            lineNumber,
                            getTokenType()
                        )
                    );
                }

            }

        } catch(IOException ioe) {
            // error opening .jott file
            System.err.println(String.format("Error opening file '%s'", filename));
        }

		    return null;
	  }

    /**
     * Gets a token's type based on a JottDFA accept state
     * @param state JottDFA state accessed prior to accepting (AKA the JottDFA accept state)
     * @rit.pre state is always an accepting state
     * @rit.post returned TokenType is always one of the defined enum values
     * @return TokenType related to state
     */
    private static TokenType getTokenType(JottDFA.State state) {
      if (state == JottDFA.State.COMMA) return TokenType.COMMA;
      else if (state == JottDFA.State.R_BRACKET) return TokenType.R_BRACKET;
      else if (state == JottDFA.State.L_BRACKET) return TokenType.L_BRACKET;
      else if (state == JottDFA.State.R_BRACE) return TokenType.R_BRACE;
      else if (state == JottDFA.State.L_BRACE) return TokenType.L_BRACE;
      else if (state == JottDFA.State.ASSIGN) return TokenType.ASSIGN;
      else if (state == JottDFA.State.REL_OP_1 || state == JottDFA.State.REL_OP_2) return TokenType.REL_OP;
      else if (state == JottDFA.State.MATH_OP) return TokenType.MATH_OP;
      else if (state == JottDFA.State.SEMICOLON) return TokenType.SEMICOLON;
      else if (state == JottDFA.State.NUMBER_1 || state == JottDFA.State.NUMBER_2) return TokenType.NUMBER;
      else if (state == JottDFA.State.ID_KEYWORD) return TokenType.ID_KEYWORD;
      else if (state == JottDFA.State.COLON) return TokenType.COLON;
      else return TokenType.STRING;
    }
}