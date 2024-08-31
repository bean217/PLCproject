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
import provided.JottDFA;
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

            // Read character stream until the end of the file
            // pbr returns -1 at the end of file stream
            while (c != -1) {
                if (c == '\n') lineNumber++;  // increment line number when a newline is read

                // Set the current state of JottDFA to the next state based on c
                processChar(c, lineNumber, filename, tokens, jdfa);
                // String maybeToken = jdfa.getNextState(c);

                // if (maybeToken != null) {
                //     // JottDFA recognized a token
                //     tokens.add(new Token(
                //         maybeToken, 
                //         filename, 
                //         lineNumber, 
                //         getTokenType(jdfa.getCurrentStateID())));
                // }

                c = pbr.read();
            }

            // EOF reached, check for last token (if it exists)
            processChar(c, lineNumber, filename, tokens, jdfa);
            // String maybeToken = jdfa.getNextState(c);

            // if (maybeToken != null) {
            //     // JottDFA recognized a token
            //     tokens.add(new Token(
            //         maybeToken, 
            //         filename, 
            //         lineNumber, 
            //         getTokenType(jdfa.getCurrentStateID())));
            // }

        } catch(IOException ioe) {
            // error opening .jott file
            System.err.println(String.format("Error opening file '%s'", filename));
        }

		    return tokens;
	  }

    private static void processChar(int c, int lineNumber, String filename, ArrayList<Token> tokens, JottDFA jdfa) {
        // Set the current state of JottDFA to the next state based on c
        String maybeToken = jdfa.getNextState(c);

        if (maybeToken != null) {
            // JottDFA recognized a token
            tokens.add(new Token(
                maybeToken, 
                filename, 
                lineNumber, 
                getTokenType(jdfa.getCurrentStateID())));
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
}