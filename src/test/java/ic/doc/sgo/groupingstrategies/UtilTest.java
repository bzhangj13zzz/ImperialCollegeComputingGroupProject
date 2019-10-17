package ic.doc.sgo.groupingstrategies;

import org.junit.Test;

import static ic.doc.sgo.groupingstrategies.Util.*;
import static org.junit.Assert.*;

public class UtilTest {
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
    public void testAssignStudentToGroup(){
    }

    @Test
    public void testSwapGroup() {

    }
}