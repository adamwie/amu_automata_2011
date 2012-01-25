package pl.edu.amu.wmi.daut.re;


import java.util.ArrayList;
import pl.edu.amu.wmi.daut.base.Acceptor;
import pl.edu.amu.wmi.daut.base.AnyTransitionLabel;
import pl.edu.amu.wmi.daut.base.AutomatonByRecursion;
import pl.edu.amu.wmi.daut.base.AutomatonSpecification;
import pl.edu.amu.wmi.daut.base.CharClassTransitionLabel;
import pl.edu.amu.wmi.daut.base.CharTransitionLabel;
import pl.edu.amu.wmi.daut.base.NaiveAutomatonSpecification;
import pl.edu.amu.wmi.daut.base.State;


/**
 * SubstringExtendPosixRegexp
 */
public class SubstringExtendedPosixRegexp implements Acceptor {

    SubstringExtendedPosixRegexp(String regexp) {
        PosixRegexp = regexp;
        createAutomaton(PosixRegexp);
    }

    protected void createAutomaton(String regexp) {



        State s0 = automaton.addState();

        automaton.markAsInitial(s0);

        String regexpart = "";

        int lenght = regexp.length();
        int start = 0;
        int end = lenght - 1;
        int state = -1;

        ArrayList<State> states = new ArrayList<State>();

        if (regexp.charAt(0) == '^' && regexp.charAt(lenght - 1) == '$') {
            isSubstring = false;
            start = 1;
            end = lenght - 2;
        } else {
//            states.add(automaton.addState());
//            state++;
//            automaton.addTransition(s0, states.get(state),
            //new BeginningOfTextOrLineTransitionLabel());
            isSubstring = true;
        }

        int index = 0;

        ArrayList<State> questionStates = new ArrayList<State>();
        int questionState = -1;
        char a;

        for (int i = start; i <= end; i++) {

            a = regexp.charAt(i);

            if (a == '[') {

                for (int b = i; b <= end; b++) {
                    if (regexp.charAt(b) == ']') {
                        index = b;
                        break;
                    }
                }

                for (int b = i + 1; b <= index - 1; b++) {
                    regexpart += regexp.charAt(b);
                }

                if (index == end) {
                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1),
                                states.get(state), new CharClassTransitionLabel(regexpart));
                    } else {
                        automaton.addTransition(s0, states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharClassTransitionLabel(regexpart));
                    }
                    break;
                }

                if (regexp.charAt(i = +index + 1) == '*') {

                    if (states.isEmpty()) {
                        automaton.addLoop(s0, new CharClassTransitionLabel(regexpart));
                    } else {
                        automaton.addLoop(states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    }

                    if (isQuestion && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState),
                                s0, new CharClassTransitionLabel(regexpart));
                    } else if (isQuestion && !states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharClassTransitionLabel(regexpart));
                    }

                    i++;
                } else if (regexp.charAt(i = +index + 1) == '+') {

                    states.add(automaton.addState());
                    state++;

                    if (state == 0) {
                        automaton.addLoop(s0, new CharClassTransitionLabel(regexpart));
                        automaton.addTransition(s0, states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    } else {
                        automaton.addLoop(states.get(state - 1),
                                new CharClassTransitionLabel(regexpart));
                        automaton.addTransition(states.get(state - 1), states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    }

                    if (isQuestion) {
                        automaton.addLoop(questionStates.get(questionState),
                                new CharClassTransitionLabel(regexpart));
                        automaton.addTransition(questionStates.get(questionState), states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    }
                    i++;

                } else if (regexp.charAt(i = +index + 1) == '?') {

                    questionStates.add(automaton.addState());
                    questionState++;
                    isQuestion = true;

                    if (!states.isEmpty()) {
                        automaton.addTransition(states.get(state), questionStates.get(questionState),
                                new CharClassTransitionLabel(regexpart));
                    } else {
                        automaton.addTransition(s0, questionStates.get(questionState),
                                new CharClassTransitionLabel(regexpart));
                    }

                    if ((questionStates.size() > 1) && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState - 1),
                                questionStates.get(questionState),
                                new CharClassTransitionLabel(regexpart));
                        isQuestion = false;
                    }
                    if ((i = +index + 1) == end) {
                        automaton.markAsFinal(questionStates.get(questionState));
                        break;
                    }

                    i++;
                } else {

                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1), states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    } else {
                        automaton.addTransition(s0, states.get(state),
                                new CharClassTransitionLabel(regexpart));
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharClassTransitionLabel(regexpart));
                    }
                }

                i = +index;
                regexpart = "";

                continue;
            }

            if (isCharacter(a)) {

                if (i == end) {
                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1),
                                states.get(state), new CharTransitionLabel(a));
                    } else {
                        automaton.addTransition(s0, states.get(state),
                                new CharTransitionLabel(a));
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharTransitionLabel(a));
                    }

                    break;
                }

                if (regexp.charAt(i + 1) == '*') {

                    if (states.isEmpty()) {
                        automaton.addLoop(s0, new CharTransitionLabel(a));
                    } else {
                        automaton.addLoop(states.get(state), new CharTransitionLabel(a));
                    }

                    if (isQuestion && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState),
                                s0, new CharTransitionLabel(a));
                    } else if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharTransitionLabel(a));
                    }

                    i++;
                    continue;

                } else if (regexp.charAt(i + 1) == '+') {
                    states.add(automaton.addState());
                    state++;

                    if (state == 0) {
                        automaton.addTransition(s0, states.get(state),
                                new CharTransitionLabel(a));
                        automaton.addLoop(states.get(state),
                                new CharTransitionLabel(a));
                    } else {
                        automaton.addTransition(states.get(state - 1), states.get(state),
                                new CharTransitionLabel(a));
                        automaton.addLoop(states.get(state), new CharTransitionLabel(a));
                    }
                    if (isQuestion) {
                        automaton.addLoop(questionStates.get(questionState),
                                new CharTransitionLabel(a));
                        automaton.addTransition(questionStates.get(questionState), states.get(state),
                                new CharTransitionLabel(a));
                    }

                    i++;
                    continue;

                } else if (regexp.charAt(i + 1) == '?') {
                    questionStates.add(automaton.addState());
                    questionState++;
                    isQuestion = true;

                    if (!states.isEmpty()) {
                        automaton.addTransition(states.get(state),
                                questionStates.get(questionState), new CharTransitionLabel(a));
                    } else {
                        automaton.addTransition(s0, questionStates.get(questionState),
                                new CharTransitionLabel(a));
                    }

                    if ((questionStates.size() > 1) && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState - 1),
                                questionStates.get(questionState), new CharTransitionLabel(a));
                        isQuestion = false;
                    }
                    if ((i = +index + 1) == end) {
                        automaton.markAsFinal(questionStates.get(questionState));
                        break;
                    }

                    i++;
                    continue;

                } else {
                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1), states.get(state),
                                new CharTransitionLabel(a));
                    } else {
                        automaton.addTransition(s0, states.get(state),
                                new CharTransitionLabel(a));
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState),
                                states.get(state), new CharTransitionLabel(a));
                    }

                }
            }

            if (a == '.') {
                if (i == end) {

                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1),states.get(state), new AnyTransitionLabel());
                    } else {
                        automaton.addTransition(s0, states.get(state),
                                new AnyTransitionLabel());
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState), states.get(state), new AnyTransitionLabel());
                    }

                    break;
                }

                if (regexp.charAt(i + 1) == '*') {

                    if (states.isEmpty()) {
                        automaton.addLoop(s0, new AnyTransitionLabel());
                    } else {
                        automaton.addLoop(states.get(state), new AnyTransitionLabel());
                    }

                    if (isQuestion && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState), s0, new AnyTransitionLabel());
                    } else if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState), states.get(state), new AnyTransitionLabel());
                    }

                    i++;
                    continue;


                } else if (regexp.charAt(i + 1) == '+') {
                    states.add(automaton.addState());
                    state++;

                    if (state == 0) {
                        automaton.addTransition(s0, states.get(state), new AnyTransitionLabel());
                        automaton.addLoop(states.get(state), new AnyTransitionLabel());
                    } else {
                        automaton.addTransition(states.get(state - 1), states.get(state), new AnyTransitionLabel());
                        automaton.addLoop(states.get(state), new AnyTransitionLabel());
                    }
                    if (isQuestion) {
                        automaton.addLoop(questionStates.get(questionState), new AnyTransitionLabel());
                        automaton.addTransition(questionStates.get(questionState), states.get(state), new AnyTransitionLabel());
                    }

                    i++;
                    continue;

                } else if (regexp.charAt(i + 1) == '?') {
                    questionStates.add(automaton.addState());
                    questionState++;
                    isQuestion = true;

                    if (!states.isEmpty()) {
                        automaton.addTransition(states.get(state), questionStates.get(questionState), new AnyTransitionLabel());
                    } else {
                        automaton.addTransition(s0, questionStates.get(questionState), new AnyTransitionLabel());
                    }

                    if ((questionStates.size() > 1) && states.isEmpty()) {
                        automaton.addTransition(questionStates.get(questionState - 1), questionStates.get(questionState), new AnyTransitionLabel());
                        isQuestion = false;
                    }
                    if ((i++) == end) {
                        automaton.markAsFinal(questionStates.get(questionState));
                        break;
                    }

                    i++;
                    continue;

                } else {
                    states.add(automaton.addState());
                    state++;

                    if (state != 0) {
                        automaton.addTransition(states.get(state - 1), states.get(state), new AnyTransitionLabel());
                    } else {
                        automaton.addTransition(s0, states.get(state), new AnyTransitionLabel());
                    }

                    if (isQuestion) {
                        automaton.addTransition(questionStates.get(questionState), states.get(state), new AnyTransitionLabel());
                    }

                }
            }
        }
        if (states.isEmpty()) {
            automaton.markAsFinal(s0);
            for (State statez : questionStates) {
                automaton.markAsFinal(statez);
            }
        } else {
            automaton.markAsFinal(states.get(state));
        }
//        if(isSubstring) {
//            State finals = automaton.addState();
//            automaton.markAsFinal(finals);
//            automaton.addTransition(states.get(state), finals, new EndOfTextOrLineTransitionLabel());
//        }
    }

    protected boolean isCharacter(char a) {
        if (a >= 'a' && a <= 'z' || a >= 'A' && a <= 'Z' || a >= 0 && a <= 9) {
            return true;
        }
        return false;
    }

    @Override
    public boolean accepts(String text) {
            return aut.accepts(text);
    }
    private AutomatonSpecification automaton = new NaiveAutomatonSpecification();
    private AutomatonByRecursion aut = new AutomatonByRecursion(automaton);
    private String PosixRegexp;
    private boolean isSubstring;
    private boolean isQuestion = false;
}
