package ic.doc.sgo;

import java.time.ZoneId;
import java.util.Optional;

public final class TimeZoneCalculator {
    //TODO: Find a way to calculate
    public static int timeBetweenWithTimeZone(Optional<ZoneId> timeZone, Optional<ZoneId> timeZone1) {
        return 0;
    }

    public static int timeBetweenWithInteger(int a, int b) {
        int diff = Math.abs(a-b);
        return Math.min(diff, 24-diff);
    }
}
