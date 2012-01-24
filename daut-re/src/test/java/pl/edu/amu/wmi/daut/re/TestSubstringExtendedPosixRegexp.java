package pl.edu.amu.wmi.daut.re;

import pl.edu.amu.wmi.daut.base.*;
import junit.framework.TestCase;

/**
 *
 * @author Adam Wierzbiński
 */
public class TestSubstringExtendedPosixRegexp extends TestCase {

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test1() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("[a-z]*ab[0-9a-z]+");

        assertTrue(subs.accepts("adamab1991"));
        assertTrue(subs.accepts("ab1991"));
        assertTrue(subs.accepts("aba"));
        assertTrue(subs.accepts("adamab1991wierzbinski"));
        assertFalse(subs.accepts("adamab"));
        assertFalse(subs.accepts("as1"));
    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test2() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp(".");

        assertTrue(subs.accepts("a"));
        assertTrue(subs.accepts("9"));
        assertTrue(subs.accepts("ź"));

    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test3() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("[a-z]?[0-9]*[a-b]?");

        assertTrue(subs.accepts("a0000"));
        assertTrue(subs.accepts("a0909b"));
        assertTrue(subs.accepts("0000000b"));
        assertTrue(subs.accepts("ab"));

    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test4() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("[a-z]?[0-9]+[a-b]?");

        assertTrue(subs.accepts("a0000"));
        assertTrue(subs.accepts("a0909b"));
        assertTrue(subs.accepts("0000000b"));
        assertTrue(subs.accepts("0"));
        assertFalse(subs.accepts("ab"));

    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test5() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("[a-z]?[0-9][a-b]?");

        assertTrue(subs.accepts("a0"));
        assertTrue(subs.accepts("a0b"));
        assertTrue(subs.accepts("0b"));
        assertTrue(subs.accepts("2"));
        assertFalse(subs.accepts("ab"));

    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test6() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("a*b?c*a");

        assertTrue(subs.accepts("abca"));
        assertTrue(subs.accepts("aba"));
        assertTrue(subs.accepts("bca"));
        assertTrue(subs.accepts("aaaaaaaaacca"));
        assertFalse(subs.accepts("abbca"));

    }

    /**
     *
     * @author Adam Wierzbiński
     */
    public void test7() {
        SubstringExtendedPosixRegexp subs = new SubstringExtendedPosixRegexp("a*b?c*");

        assertTrue(subs.accepts("abca"));
    }
}
