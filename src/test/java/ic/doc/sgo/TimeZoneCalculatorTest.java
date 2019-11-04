package ic.doc.sgo;

import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class TimeZoneCalculatorTest {
    ;

    @Test
    public void timeBetween() {
        assertEquals(0, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC+1")));
        assertEquals(2, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC+3")));
        assertEquals(4, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC-3")));
        assertEquals(7, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+4"), ZoneId.of("UTC-3")));
        assertEquals(0, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+12"), ZoneId.of("UTC-12")));
        assertEquals(12, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+6"), ZoneId.of("UTC-6")));
        assertEquals(10, TimeZoneCalculator.timeBetween(ZoneId.of("UTC+7"), ZoneId.of("UTC-7")));
        assertEquals(2, TimeZoneCalculator.timeBetween(ZoneId.of("UTC-5"), ZoneId.of("UTC-3")));
    }

    @Test
    public void testZoneToInteger() {
        assertEquals(0, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC")));
        assertEquals(0, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+0")));
        assertEquals(0, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC-0")));
        assertEquals(1, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+1")));
        assertEquals(5, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+5")));
        assertEquals(-1, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC-1")));
        assertEquals(-2, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC-2")));
        assertEquals(-11, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+13")));
        assertEquals(-10, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+14")));

        assertEquals(-4, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC-4")));
        assertEquals(-3, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC-3")));
        assertEquals(12, TimeZoneCalculator.timeZoneInInteger(ZoneId.of("UTC+12")));
    }
}