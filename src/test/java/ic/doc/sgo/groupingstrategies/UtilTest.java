package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ic.doc.sgo.groupingstrategies.Util.*;
import static org.junit.Assert.*;

public class UtilTest {
    Student s1 = new Student.Builder(String.valueOf(1)).createStudent();
    Student s2 = new Student.Builder(String.valueOf(2)).createStudent();
    Group g1 = Group.from(new ArrayList<>());
    Group g2 = Group.from(new ArrayList<>());

    @Test
    public void testGetNumberInterval() {
        Util.Pair<Integer, Integer> interval = getNumberInterval(100, 5, 10);
        assertEquals(10, (int) interval.first());
        assertEquals(20, (int) interval.second());

        interval = getNumberInterval(100, 13, 21);
        assertEquals(5, (int) interval.first());
        assertEquals(7, (int) interval.second());

        interval = getNumberInterval(100, 10, 10);
        assertEquals(10, (int) interval.first());
        assertEquals(10, (int) interval.second());

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
    public void testSwapGroup() {
        g1.add(s1);
        g2.add(s2);

        swapGroup(s1, s2);
        assertSame(s1.getGroup(), g2);
        assertSame(s2.getGroup(), g1);
        assertTrue(g1.contains(s2));
        assertTrue(g2.contains(s1));
        assertFalse(g1.contains(s1));
        assertFalse(g2.contains(s2));
    }

    @Test
    public void testIsSameGroup() {
        g1.add(s1);
        g1.add(s2);
        assertTrue(isSameGroup(s1, s2));
        g2.add(s1);
        assertFalse(isSameGroup(s1, s2));

    }
}