package ic.doc.sgo;

import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.*;

public class TimeZoneCalculatorTest { ;

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
}