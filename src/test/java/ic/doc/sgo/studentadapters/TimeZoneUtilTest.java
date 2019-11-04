package ic.doc.sgo.studentadapters;

import org.junit.Test;

import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeZoneUtilTest {

    @Test
    public void getTimeZoneIdLocalLookUpByCity() throws Exception {
        assertThat(TimeZoneUtil.getTimeZoneId("New York", "United States"), is(ZoneId.of("America/New_York")));
        assertThat(TimeZoneUtil.getTimeZoneId("Shanghai", "China"), is(ZoneId.of("Asia/Shanghai")));
    }

    @Test
    public void getTimeZoneIdLocalLookUpOnlyOneTimezoneNoCityName() throws Exception {
        assertThat(TimeZoneUtil.getTimeZoneId("", "France"), is(ZoneId.of("Europe/Paris")));
    }

    @Test
    public void getTimeZoneIdLocalLookUpOnlyOneTimezone() throws Exception {
        assertThat(TimeZoneUtil.getTimeZoneId("London", "United Kingdom"), is(ZoneId.of("Europe/London")));
        assertThat(TimeZoneUtil.getTimeZoneId("Paris", "France"), is(ZoneId.of("Europe/Paris")));
        assertThat(TimeZoneUtil.getTimeZoneId("Lee", "France"), is(ZoneId.of("Europe/Paris")));
    }

    @Test
    public void getTimeZoneIdNeedAPI() throws Exception {
        //TODO
    }

}