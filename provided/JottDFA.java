package provided;

/**
 * @file: JottDFA.java
 * @description: Implementation of a DFA for tokening
 */

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JottDFA {

     /* JottDFA's states, which is hard-coded in (with transitions to avoid needing
      * to waste time and resources on reading and constructing from a file */
    private static final Map<StateID, State> STATES = initializeJottDFA();

    private static Map<StateID, State> initializeJottDFA() {
        Map<StateID, State> states = new HashMap<>();

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

        // Add states to STATES Map
        states.put(START.id, START);
        states.put(COMMENT.id, COMMENT);
        states.put(COMMA.id, COMMA);
        states.put(R_BRACKET.id, R_BRACKET);
        states.put(L_BRACKET.id, L_BRACKET);
        states.put(R_BRACE.id, R_BRACE);
        states.put(L_BRACE.id, L_BRACE);
        states.put(ASSIGN.id, ASSIGN);
        states.put(REL_OP_1.id, REL_OP_1);
        states.put(REL_OP_2.id, REL_OP_2);
        states.put(MATH_OP.id, MATH_OP);
        states.put(SEMICOLON.id, SEMICOLON);
        states.put(PERIOD.id, PERIOD);
        states.put(NUMBER_1.id, NUMBER_1);
        states.put(NUMBER_2.id, NUMBER_2);
        states.put(ID_KEYWORD.id, ID_KEYWORD);
        states.put(COLON.id, COLON);
        states.put(FC_HEADER.id, FC_HEADER);
        states.put(BANG.id, BANG);
        states.put(QUOTE.id, QUOTE);
        states.put(STRING.id, STRING);

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

        return states;
    }
    
    private State previousState;
    private State currentState;
    private StringBuilder currentToken;

    public JottDFA() {
        // previousState is initially null
        // current state is initally the start state
        this.currentState = STATES.get(StateID.START);
        this.currentToken = new StringBuilder();
    }

    /**
     * Gets the current state of JottDFA and returns it
     * @return current State enum value of JottDFA
     */
    public State getCurrentState() {
        return currentState;
    }

    public StateID getPreviousStateID() {
        return previousState.id;
    }

    public String getPreviousExpectedNext() {
        return previousState.getExpectedNext();
    }

    /**
     * Gets whether JottDFA is currently in an accepting state
     * @return
     */
    public boolean wasInAccept() {
        return previousState.isAccept;
    }

    /**
     * Moves JottDFA to the next state based on a character c
     * @param c int representing the current character being processed
     * @return token string if token is recognized
     *         null if token is rejected
     *         empty string otherwise
     */
    public String getNextState(int c) {

        // TODO: REWRITE THIS METHOD!!!

        System.out.println(String.format("%s\t\t%s\t%d", currentState.id, (char)c, c));

        State nextState = currentState.getNextState(getCharClass(c)); 
        System.out.println(String.format("\t\t\t%s\t%s", previousState == null ? null : previousState.id, currentState == null ? null : currentState.id));

        if (nextState == null) {
            // handle reject
            // get rejected token
            String token = currentToken.toString();
            // set previous state to current state
            previousState = currentState;
            // reset JottDFA to start state and navigate 
            currentState = STATES.get(StateID.START);
            // clear the current token
            currentToken.setLength(0);
            // append the current character to current token
            currentToken.append((char)c);
            System.out.println(String.format("\t\t\t%s\t%s", previousState == null ? null : previousState.id, currentState == null ? null : currentState.id));

        
            // return the token
            return token;
        } else {
            // continue processing characters
            // set previous state to current state
            previousState = currentState;
            // move JottDFA to next state
            currentState = nextState;
            // clear tokens if current state is the start state
            if (currentState.id == StateID.START) currentToken.setLength(0);
            // append character to current token
            currentToken.append((char)c);
            System.out.println(String.format("\t\t\t%s\t%s", previousState == null ? null : previousState.id, currentState == null ? null : currentState.id));

        
            // no token accepted yet, so return null
            return null;
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

        private State getNextState(CharClass cc) {
            return delta.get(cc);
        }

        private void put(CharClass cc, State state) {
            delta.put(cc, state);
        }

        private String getExpectedNext() {
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

    private enum CharClass {
        WHITESPACE("whitespace"),
        NEWLINE("newline"),
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
        DIGIT("digit"),
        LETTER("letter"),
        COLON(":"),
        BANG("!"),
        QUOTE("\""),
        INVALID("valid char");

        private String description;

        private CharClass(String description) {
            this.description = description;
        }
    }
    
}
