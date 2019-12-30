package ic.doc.sgo.studentparsers;

import org.junit.Test;

import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParseTimeZoneUtilsTest {

    @Test
    public void getTimeZoneIdLocalLookUpByCity() throws Exception {
        assertThat(ParseTimeZoneUtils.getTimeZoneId("New York", "United States"), is(ZoneId.of("America/New_York")));
        assertThat(ParseTimeZoneUtils.getTimeZoneId("Shanghai", "China"), is(ZoneId.of("Asia/Shanghai")));
    }

    @Test
    public void getTimeZoneIdLocalLookUpOnlyOneTimezoneNoCityName() throws Exception {
        assertThat(ParseTimeZoneUtils.getTimeZoneId("", "France"), is(ZoneId.of("Europe/Paris")));
    }

    @Test
    public void getTimeZoneIdLocalLookUpOnlyOneTimezone() throws Exception {
        assertThat(ParseTimeZoneUtils.getTimeZoneId("London", "United Kingdom"), is(ZoneId.of("Europe/London")));
        assertThat(ParseTimeZoneUtils.getTimeZoneId("Paris", "France"), is(ZoneId.of("Europe/Paris")));
        assertThat(ParseTimeZoneUtils.getTimeZoneId("Lee", "France"), is(ZoneId.of("Europe/Paris")));
    }

    @Test
    public void getTimeZoneIdNeedAPI() throws Exception {
        //TODO
    }

}