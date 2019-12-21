package ic.doc.sgo;

import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class ZoneIdUtilsTest {
    ;

    @Test
    public void timeBetween() {
        assertEquals(0, ZoneIdUtils.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC+1")));
        assertEquals(2, ZoneIdUtils.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC+3")));
        assertEquals(4, ZoneIdUtils.timeBetween(ZoneId.of("UTC+1"), ZoneId.of("UTC-3")));
        assertEquals(7, ZoneIdUtils.timeBetween(ZoneId.of("UTC+4"), ZoneId.of("UTC-3")));
        assertEquals(0, ZoneIdUtils.timeBetween(ZoneId.of("UTC+12"), ZoneId.of("UTC-12")));
        assertEquals(12, ZoneIdUtils.timeBetween(ZoneId.of("UTC+6"), ZoneId.of("UTC-6")));
        assertEquals(10, ZoneIdUtils.timeBetween(ZoneId.of("UTC+7"), ZoneId.of("UTC-7")));
        assertEquals(2, ZoneIdUtils.timeBetween(ZoneId.of("UTC-5"), ZoneId.of("UTC-3")));
    }

    @Test
    public void testZoneToInteger() {
        assertEquals(0, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC")));
        assertEquals(0, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+0")));
        assertEquals(0, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC-0")));
        assertEquals(1, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+1")));
        assertEquals(5, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+5")));
        assertEquals(-1, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC-1")));
        assertEquals(-2, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC-2")));
        assertEquals(-11, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+13")));
        assertEquals(-10, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+14")));

        assertEquals(-4, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC-4")));
        assertEquals(-3, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC-3")));
        assertEquals(12, ZoneIdUtils.zoneIdToInteger(ZoneId.of("UTC+12")));
    }
}