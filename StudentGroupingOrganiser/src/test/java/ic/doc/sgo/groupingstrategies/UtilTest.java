package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ic.doc.sgo.groupingstrategies.Util.*;
import static org.junit.Assert.*;

public class UtilTest {
    Student s1 = new Student.Builder(String.valueOf(1), "Test1").createStudent();
    Student s2 = new Student.Builder(String.valueOf(2), "Test2").createStudent();
    Group g1 = Group.from(new ArrayList<>());
    Group g2 = Group.from(new ArrayList<>());

    @Test
    public void testGetNumberInterval() {
        Util.Pair<Integer, Integer> interval = getNumberInterval(100, 5, 10);
        assertTrue(interval.first() == 10);
        assertTrue(interval.second() == 20);

        interval = getNumberInterval(100, 13, 21);
        assertTrue(interval.first() == 5);
        assertTrue(interval.second() == 7);

        interval = getNumberInterval(100, 10, 10);
        assertTrue(interval.first() == 10);
        assertTrue(interval.second() == 10);

        //TODO: Return UnvalidSubSizeInterval
        interval = getNumberInterval(100, 13, 13);

        //TODO: Return UnvalidSetSize
        interval = getNumberInterval(2, 3, 4);
    }

    @Test
    public void testGetRandomIntegerBetween() {
        int a = 23;
        int b = 342;
        int t = 1000;
        while (t > 0) {
            int r = getRandomIntegerBetween(a, b);
            assertTrue(a <= r);
            assertTrue(r <= b);
            t--;
        }
    }

    @Test
    public void testAssignStudentToGroup() {
        assignStudentToGroup(s1, g1);
        assertTrue(s1.getGroup() == g1);
        assertTrue(g1.contains(s1));

        assignStudentToGroup(s1, g2);
        assertTrue(s1.getGroup() == g2);
        assertTrue(g2.contains(s1));
        assertTrue(!g1.contains(s1));
    }

    @Test
    public void testSwapGroup() {

        assignStudentToGroup(s1, g1);
        assignStudentToGroup(s2, g2);

        swapGroup(s1, s2);
        assertTrue(s1.getGroup() == g2);
        assertTrue(s2.getGroup() == g1);
        assertTrue(g1.contains(s2));
        assertTrue(g2.contains(s1));
        assertFalse(g1.contains(s1));
        assertFalse(g2.contains(s2));
    }

    @Test
    public void testIsBetterSwapIfSwap() {
        //TODO: find a way to test it
    }

    @Test
    public void testIsSameGroup() {

        assignStudentToGroup(s1, g1);
        assignStudentToGroup(s2, g1);
        assertTrue(isSameGroup(s1, s2));
        assignStudentToGroup(s1, g2);
        assertFalse(isSameGroup(s1, s2));

    }
}