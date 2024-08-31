package provided;

/**
 * @file: JottDFA.java
 * @description: Implementation of a DFA for tokening
 */

import java.util.Hashtable;

public class JottDFA {

     /* JottDFA's transition function, which is hard-coded in to avoid needing to 
      * waste time and resources on reading and constructing from a file */
    private static final Hashtable<State, State[]> TRANSITIONS = new Hashtable<>(){
        {   //                                  0             1              2            3             4                5                6              7              8               9               10             11               12              13                14                15               16            17            18
            put(State.START, new State[]{       State.START,  State.START,  State.Q1,     State.COMMA,  State.R_BRACKET, State.L_BRACKET, State.R_BRACE, State.L_BRACE, State.ASSIGN,   State.REL_OP_1, State.MATH_OP, State.SEMICOLON, State.Q2,       State.NUMBER_1,   State.ID_KEYWORD, State.COLON,     State.Q3,     State.Q4,     State.REJECT });
            put(State.Q1, new State[]{          State.Q1,     State.START,  State.Q1,     State.Q1,     State.Q1,        State.Q1,        State.Q1,      State.Q1,      State.Q1,       State.Q1,       State.Q1,      State.Q1,        State.Q1,       State.Q1,         State.Q1,         State.Q1,        State.Q1,     State.Q1,     State.Q1 });
            put(State.COMMA, new State[]{       State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.R_BRACKET, new State[]{   State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.L_BRACKET, new State[]{   State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.R_BRACE, new State[]{     State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.L_BRACE, new State[]{     State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.ASSIGN, new State[]{      State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REL_OP_2, State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.REL_OP_1, new State[]{    State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REL_OP_2, State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.REL_OP_2, new State[]{    State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.MATH_OP, new State[]{     State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.SEMICOLON, new State[]{   State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.Q2, new State[]{          State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.NUMBER_2,   State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.NUMBER_1, new State[]{    State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.NUMBER_2, State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.NUMBER_2, new State[]{    State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.NUMBER_2,   State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.ID_KEYWORD, new State[]{  State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.ID_KEYWORD, State.ID_KEYWORD, State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.COLON, new State[]{       State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.FC_HEADER, State.REJECT, State.REJECT, State.REJECT });
            put(State.FC_HEADER, new State[]{   State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.Q3, new State[]{          State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REL_OP_2, State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
            put(State.Q4, new State[]{          State.Q4,     State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.Q4,         State.Q4,         State.REJECT,    State.REJECT, State.STRING, State.REJECT });
            put(State.STRING, new State[]{      State.REJECT, State.REJECT, State.REJECT, State.REJECT, State.REJECT,    State.REJECT,    State.REJECT,  State.REJECT,  State.REJECT,   State.REJECT,   State.REJECT,  State.REJECT,    State.REJECT,   State.REJECT,     State.REJECT,     State.REJECT,    State.REJECT, State.REJECT, State.REJECT });
        }
    };

    private State previousState;
    private State currentState;

    public JottDFA() {
        // previousState is initially null
        // current state is initally the start state
        this.currentState = State.START;
    }

    /**
     * Gets the current state of JottDFA and returns it
     * @return current State enum value of JottDFA
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Gets the previous state of JottDFA and returns it
     * @return previous state enum value of JottDFA
     */
    public State getPreviousState() {
        return previousState;
    }

    /**
     * Gets whether JottDFA is currently in an accepting state
     * @return
     */
    public boolean isInAccept() {
        return currentState.isAccept;
    }

    /**
     * Sets the current state to the next state based on a character c processed by JottDFA
     * @param c Current character being processed
     * @return true if next state is reject, false otherwise
     */
    public boolean getNextStateType(char c) {
        State nextState = TRANSITIONS.get(currentState)[getCharClass(c)];

        // update the previous state
        previousState = currentState;

        if (nextState == State.REJECT) {
            // return false if next state is a REJECT
            currentState = State.START;
            return true;
        } else {
            // return true if next state is a non-reject state
            currentState = nextState;
            return false;
        }
    }

    /**
     * Gets the index value of a character class based on character input, c
     * @param c Current character being processed
     * @return Respective char class index value in JottDFA.TRANSITIONS
     */
    private int getCharClass(char c) {
        // by default, c is an invalid character
        CharClass cc = CharClass.INVALID;
        // if c is a newline
        if (c == '\n') cc = CharClass.NEWLINE;
        // if c is a whitespace
        else if (c == ' ' || c == '\t' || c == '\r') cc = CharClass.WHITESPACE;
        // if c is a pound ('#')
        else if (c == '#') cc = CharClass.POUND;
        // if c is a comma (',')
        else if (c == ',') cc = CharClass.COMMA;
        // if c is a right bracket (']')
        else if (c == ']') cc = CharClass.R_BRACKET;
        // if c is a left bracket ('[')
        else if (c == '[') cc = CharClass.L_BRACKET;
        // if c is a right brace ('}')
        else if (c == '}') cc = CharClass.R_BRACE;
        // if c is a left brace ('{')
        else if (c == '{') cc = CharClass.L_BRACE;
        // if c is an equal sign ('=')
        else if (c == '=') cc = CharClass.ASSIGN;
        // if c is a relational operator ('<', '>')
        else if (c == '<' || c == '>') cc = CharClass.REL_OP;
        // if c is a math operator ('+', '-', '*', '/')
        else if (c == '+' || c == '-' || c == '*' || c == '/') cc = CharClass.MATH_OP;
        // if c is a semicolon (';')
        else if (c == ';') cc = CharClass.SEMICOLON;
        // if c is a period ('.')
        else if (c == '.') cc = CharClass.PERIOD;
        // if c is a digit (0-9) -- checking by ascii value
        else if (c >= 48 && c <= 57) cc = CharClass.DIGIT;
        // if c is a letter (a-bA-B)
        else if ((c >= (int)'a' && c <= (int)'z') || (c >= 'A' && c <= 'Z'))  cc = CharClass.LETTER;
        // if c is a colon (':')
        else if (c == ':') cc = CharClass.COLON;
        // if c is a bang ('!')
        else if (c == '!') cc = CharClass.BANG;
        // if c is a quote (")
        else if (c == '\"') cc = CharClass.QUOTE;

        // return character class value, corresponding to an index in the JottDFA.TRANSITIONS hash table
        return cc.val;
    }

    /**
     * Represents hard-coded transition states in JottDFA
     */
    public enum State {
        START(false),
        Q1(false),
        COMMA(true),
        R_BRACKET(true),
        L_BRACKET(true),
        R_BRACE(true),
        L_BRACE(true),
        ASSIGN(true),
        REL_OP_1(true),
        REL_OP_2(true),
        MATH_OP(true),
        SEMICOLON(true),
        Q2(false),
        NUMBER_1(true),
        NUMBER_2(true),
        ID_KEYWORD(true),
        COLON(true),
        FC_HEADER(true),
        Q3(false),
        Q4(false),
        STRING(true),
        REJECT(false);

        private final boolean isAccept;

        private State(boolean isAccept) {
            this.isAccept = isAccept;
        }
    }

    private enum CharClass {
        WHITESPACE(0),
        NEWLINE(1),
        POUND(2),
        COMMA(3),
        R_BRACKET(4),
        L_BRACKET(5),
        R_BRACE(6),
        L_BRACE(7),
        ASSIGN(8),
        REL_OP(9),
        MATH_OP(10),
        SEMICOLON(11),
        PERIOD(12),
        DIGIT(13),
        LETTER(14),
        COLON(15),
        BANG(16),
        QUOTE(17),
        INVALID(18);

        private final int val;

        private CharClass(int val) {
            this.val = val;
        }
    }
    
}
