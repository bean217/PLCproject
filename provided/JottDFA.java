package provided;

/**
 * @file: JottDFA.java
 * @description: Implementation of a DFA for tokening
 * @author Benjamin Piro, brp8396@rit.edu
 **/

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JottDFA {

     /* JottDFA's start state, which is hard-coded in with transitions to avoid needing
      * to waste time and resources on reading and constructing from a file */
    private static final State START = initializeJottDFA();

    /**
     * Static method for initializing JottDFA states and transitions
     * @return JottDFA's start state
     */
    private static State initializeJottDFA() {
        // initialize states
        State START = new State(StateID.START, false);
        State COMMENT = new State(StateID.COMMENT, false);
        State COMMA = new State(StateID.COMMA, true);
        State R_BRACKET = new State(StateID.R_BRACKET, true);
        State L_BRACKET = new State(StateID.L_BRACKET, true);
        State R_BRACE = new State(StateID.R_BRACE, true);
        State L_BRACE = new State(StateID.L_BRACE, true);
        State ASSIGN = new State(StateID.ASSIGN, true);
        State REL_OP_1 = new State(StateID.REL_OP_1, true);
        State REL_OP_2 = new State(StateID.REL_OP_2, true);
        State MATH_OP = new State(StateID.MATH_OP, true);
        State SEMICOLON = new State(StateID.SEMICOLON, true);
        State PERIOD = new State(StateID.PERIOD, false);
        State NUMBER_1 = new State(StateID.NUMBER_1, true);
        State NUMBER_2 = new State(StateID.NUMBER_2, true);
        State ID_KEYWORD = new State(StateID.ID_KEYWORD, true);
        State COLON = new State(StateID.COLON, true);
        State FC_HEADER = new State(StateID.FC_HEADER, true);
        State BANG = new State(StateID.BANG, false);
        State QUOTE = new State(StateID.QUOTE, false);
        State STRING = new State(StateID.STRING, true);

        // START state transitions
        START.put(CharClass.WHITESPACE, START); 
        START.put(CharClass.NEWLINE, START);
        START.put(CharClass.POUND, COMMENT); 
        START.put(CharClass.COMMA, COMMA); 
        START.put(CharClass.R_BRACKET, R_BRACKET); 
        START.put(CharClass.L_BRACKET, L_BRACKET); 
        START.put(CharClass.R_BRACE, R_BRACE); 
        START.put(CharClass.L_BRACE, L_BRACE); 
        START.put(CharClass.ASSIGN, ASSIGN); 
        START.put(CharClass.REL_OP, REL_OP_1); 
        START.put(CharClass.MATH_OP, MATH_OP); 
        START.put(CharClass.SEMICOLON, SEMICOLON); 
        START.put(CharClass.PERIOD, PERIOD); 
        START.put(CharClass.DIGIT, NUMBER_1); 
        START.put(CharClass.LETTER, ID_KEYWORD); 
        START.put(CharClass.COLON, COLON); 
        START.put(CharClass.BANG, BANG); 
        START.put(CharClass.QUOTE, QUOTE);
        // COMMENT state transitions
        COMMENT.put(CharClass.WHITESPACE, COMMENT); 
        COMMENT.put(CharClass.NEWLINE, START);
        COMMENT.put(CharClass.POUND, COMMENT); 
        COMMENT.put(CharClass.COMMA, COMMENT); 
        COMMENT.put(CharClass.R_BRACKET, COMMENT); 
        COMMENT.put(CharClass.L_BRACKET, COMMENT); 
        COMMENT.put(CharClass.R_BRACE, COMMENT); 
        COMMENT.put(CharClass.L_BRACE, COMMENT); 
        COMMENT.put(CharClass.ASSIGN, COMMENT); 
        COMMENT.put(CharClass.REL_OP, COMMENT); 
        COMMENT.put(CharClass.MATH_OP, COMMENT); 
        COMMENT.put(CharClass.SEMICOLON, COMMENT); 
        COMMENT.put(CharClass.PERIOD, COMMENT); 
        COMMENT.put(CharClass.DIGIT, COMMENT); 
        COMMENT.put(CharClass.LETTER, COMMENT); 
        COMMENT.put(CharClass.COLON, COMMENT); 
        COMMENT.put(CharClass.BANG, COMMENT); 
        COMMENT.put(CharClass.QUOTE, COMMENT);
        COMMENT.put(CharClass.INVALID, COMMENT);
        // ASSIGN state transitions
        ASSIGN.put(CharClass.ASSIGN, REL_OP_2);
        // REL_OP_1 state transitions
        REL_OP_1.put(CharClass.ASSIGN, REL_OP_2);
        // PERIOD state transitions
        PERIOD.put(CharClass.DIGIT, NUMBER_2);
        // NUMBER_1 state transitions
        NUMBER_1.put(CharClass.DIGIT, NUMBER_1);
        NUMBER_1.put(CharClass.PERIOD, NUMBER_2);
        // NUMBER_2 state transitions
        NUMBER_2.put(CharClass.DIGIT, NUMBER_2);
        // ID_KEYWORD state transitions
        ID_KEYWORD.put(CharClass.DIGIT, ID_KEYWORD);
        ID_KEYWORD.put(CharClass.LETTER, ID_KEYWORD);
        // COLON state transitions
        COLON.put(CharClass.COLON, FC_HEADER);
        // BANG state transitions
        BANG.put(CharClass.ASSIGN, REL_OP_2);
        // QUOTE state transitions
        QUOTE.put(CharClass.WHITESPACE, QUOTE);
        QUOTE.put(CharClass.DIGIT, QUOTE);
        QUOTE.put(CharClass.LETTER, QUOTE);
        QUOTE.put(CharClass.QUOTE, STRING);

        return START;
    }
    
    // Previous state
    private State previousState = START;
    // Current state
    private State currentState = START;

    /**
     * Indicates if JottDFA is currently in the start state
     * @return true if current state is start state
     */
    public boolean isInStartState() {
        return currentState.id == StateID.START;
    }

    /**
     * Getter for previous state's ID
     * @return previous state's StateID enum
     */
    public StateID getPreviousStateID() {
        return previousState.id;
    }

    /**
     * Gets string representation of the expected next symbol(s)
     * from the previous state
     * @return String reprentation of next symbol(s)
     */
    public String getExpectedNext() {
        return previousState.getExpectedNext();
    }

    /**
     * Gets whether JottDFA was previously in an accepting state
     * @return previous state's isAccept value
     */
    public boolean wasInAccept() {
        return previousState.isAccept;
    }

    /**
     * Moves JottDFA to the next state based on a character c. If the transition
     * rule is undefined, then JottDFA is moved back to the start state, and c must
     * be processed again on a subsequent call.
     * @param c int representing the current character being processed
     * @return true if transition is defined, false otherwise
     */
    public boolean getNextState(int c) {
        previousState = currentState;
        // get next state
        State nextState = currentState.getNextState(getCharClass(c)); 

        // check if state transition is defined
        if (nextState == null) {
            // invalid transition; go to start state and return false
            currentState = START;
            return false;
        } else {
            // valid transition; go to next state and return true
            currentState = nextState;
            return true;
        }
    }

    /**
     * Gets the index value of a character class based on character input, c
     * @param c int representing the current character being processed
     * @return Respective char class index value in JottDFA.TRANSITIONS
     */
    private static CharClass getCharClass(int c) {
        // if c is a newline
        if (c == '\n') return CharClass.NEWLINE;
        // if c is a whitespace
        else if (c == ' ' || c == '\t' || c == '\r') return CharClass.WHITESPACE;
        // if c is a pound ('#')
        else if (c == '#') return CharClass.POUND;
        // if c is a comma (',')
        else if (c == ',') return CharClass.COMMA;
        // if c is a right bracket (']')
        else if (c == ']') return CharClass.R_BRACKET;
        // if c is a left bracket ('[')
        else if (c == '[') return CharClass.L_BRACKET;
        // if c is a right brace ('}')
        else if (c == '}') return CharClass.R_BRACE;
        // if c is a left brace ('{')
        else if (c == '{') return CharClass.L_BRACE;
        // if c is an equal sign ('=')
        else if (c == '=') return CharClass.ASSIGN;
        // if c is a relational operator ('<', '>')
        else if (c == '<' || c == '>') return CharClass.REL_OP;
        // if c is a math operator ('+', '-', '*', '/')
        else if (c == '+' || c == '-' || c == '*' || c == '/') return CharClass.MATH_OP;
        // if c is a semicolon (';')
        else if (c == ';') return CharClass.SEMICOLON;
        // if c is a period ('.')
        else if (c == '.') return CharClass.PERIOD;
        // if c is a digit (0-9) -- checking by ascii value
        else if (c >= 48 && c <= 57) return CharClass.DIGIT;
        // if c is a letter (a-bA-B)
        else if ((c >= (int)'a' && c <= (int)'z') || (c >= 'A' && c <= 'Z')) return CharClass.LETTER;
        // if c is a colon (':')
        else if (c == ':') return CharClass.COLON;
        // if c is a bang ('!')
        else if (c == '!') return CharClass.BANG;
        // if c is a quote (")
        else if (c == '\"') return CharClass.QUOTE;

        // return invalid character class type by default
        return CharClass.INVALID;
    }

    // /**
    //  * Represents IDs of transition states in JottDFA
    //  */
    public enum StateID {
        START,
        COMMENT,
        COMMA,
        R_BRACKET,
        L_BRACKET,
        R_BRACE,
        L_BRACE,
        ASSIGN,
        REL_OP_1,
        REL_OP_2,
        MATH_OP,
        SEMICOLON,
        PERIOD,
        NUMBER_1,
        NUMBER_2,
        ID_KEYWORD,
        COLON,
        FC_HEADER,
        BANG,
        QUOTE,
        STRING
    }

    /**
     * Represents a JottDFA state node.
     * Encapsulates a traditional DFA state with a name/id, whether state is an accept,
     * and next state transitions based on accepted character classes.
     */
    private static class State {

        // Represents state name
        private final StateID id;

        // Represents whether state is an accepting state or not
        private final boolean isAccept;

        // Represents the transition function, mapping CharClass to 
        private Map<CharClass,State> delta;

        private State(StateID id, boolean isAccept) {
            this.id = id;
            this.isAccept = isAccept;
            delta = new HashMap<>();
        }

        /**
         * Gets next state based on an input character class.
         * @param cc CharClass of character being processed
         * @return State object of the next state, or null if transition undefined
         */
        private State getNextState(CharClass cc) {
            return delta.get(cc);
        }

        /**
         * Adds a new state transition.
         * @param cc CharClass leading to the next state
         * @param state Next state
         */
        private void put(CharClass cc, State state) {
            delta.put(cc, state);
        }

        /**
         * Gets a description for valid transition symbols.
         * @return String of valid transition symbol description
         */
        private String getExpectedNext() {
            // if this is the start state, descriptor is unique
            if (id == StateID.START) return "<valid symbol>";

            // build description based on character classes in delta
            StringBuilder expectedNext = new StringBuilder();
            List<CharClass> ccs = new ArrayList<>(delta.keySet());
            int numCCs = ccs.size();
            for (int i = 0; i < ccs.size(); i++) {
                expectedNext.append(ccs.get(i).description);
                if (i < numCCs-1) expectedNext.append(", ");
            }
            return expectedNext.toString();
        }
    }

    /**
     * Represents class encoding of characters
     */
    private enum CharClass {
        WHITESPACE("<whitespace>"),
        NEWLINE("<newline>"),
        POUND("#"),
        COMMA(","),
        R_BRACKET("]"),
        L_BRACKET("["),
        R_BRACE("}"),
        L_BRACE("{"),
        ASSIGN("="),
        REL_OP("<>"),
        MATH_OP("+/-*"),
        SEMICOLON(";"),
        PERIOD("."),
        DIGIT("<digit>"),
        LETTER("<letter>"),
        COLON(":"),
        BANG("!"),
        QUOTE("\""),
        INVALID("<invalid symbol>");

        // Represents description of character class
        private String description;

        private CharClass(String description) {
            this.description = description;
        }
    }
    
}
