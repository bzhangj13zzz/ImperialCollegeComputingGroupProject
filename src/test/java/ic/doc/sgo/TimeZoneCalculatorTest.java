package ic.doc.sgo;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeZoneCalculatorTest { ;

    @Test
    public void timeBetweenWithTimeZone() {
        //TODO: implement
    }

    @Test
    public void timeBetweenWithInteger() {
        assertEquals(0, TimeZoneCalculator.timeBetweenWithInteger(1, 1));
        assertEquals(2, TimeZoneCalculator.timeBetweenWithInteger(1, 3));
        assertEquals(4, TimeZoneCalculator.timeBetweenWithInteger(1, -3));
        assertEquals(7, TimeZoneCalculator.timeBetweenWithInteger(4, -3));
        assertEquals(0, TimeZoneCalculator.timeBetweenWithInteger(12, -12));
        assertEquals(12, TimeZoneCalculator.timeBetweenWithInteger(6, -6));
        assertEquals(10, TimeZoneCalculator.timeBetweenWithInteger(7, -7));
        assertEquals(2, TimeZoneCalculator.timeBetweenWithInteger(-5, -3));
    }
}