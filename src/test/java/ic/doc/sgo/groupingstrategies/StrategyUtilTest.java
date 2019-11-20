package ic.doc.sgo.groupingstrategies;

import org.junit.Test;

import static ic.doc.sgo.groupingstrategies.StrategyUtil.getNumberInterval;
import static ic.doc.sgo.groupingstrategies.StrategyUtil.getRandomIntegerBetween;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StrategyUtilTest {
    @Test
    public void testGetNumberInterval() {
        StrategyUtil.Pair<Integer, Integer> interval = getNumberInterval(100, 5, 10);
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
        int r = getRandomIntegerBetween(34, 30);
        assertEquals(30, r);
    }


}