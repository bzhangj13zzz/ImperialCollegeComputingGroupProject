package ic.doc.sgo.studentadapters;

import org.junit.Test;

import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TimeZoneUtilTest {

    @Test
    public void getTimeZoneId_London_TimeZone() throws Exception {
        assertThat(TimeZoneUtil.getTimeZoneId("London", "United Kingdom"), is(ZoneId.of("Europe/London")));
    }

//    @Test
//    public void getTimeZoneId_NewYork_TimeZone() throws Exception {
//        assertThat(TimeZoneUtil.getTimeZoneId("New York", "United States"), is(ZoneId.of("America/New_York")));
//    }

}