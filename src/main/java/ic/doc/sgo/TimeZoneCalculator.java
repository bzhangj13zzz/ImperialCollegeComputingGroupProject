package ic.doc.sgo;

import ic.doc.sgo.groupingstrategies.GroupingStrategy;
import ic.doc.sgo.groupingstrategies.RandomGroupingStrategy;
import ic.doc.sgo.groupingstrategies.Util;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class TimeZoneCalculator {

    public static int timeZoneInInteger(ZoneId timeZone) {
        if (timeZone == null) {
            return 0;
        }
        int res = Math.floorMod(zonIdToOffsetInt(timeZone,Instant.now()), 24);
        if (res > 12) {
            res -= 24;
        }
        return res;
    }

    public static int timeBetween(ZoneId timeZone1, ZoneId timeZone2) {
        Instant now = Instant.now();
        int offset1 = zonIdToOffsetInt(timeZone1, now);
        int offset2 = zonIdToOffsetInt(timeZone2, now);
        int diff = Math.abs(Math.floorMod(offset1, 24) - Math.floorMod(offset2, 24));
        return Math.min(diff, 24 - diff);
    }

    public static int timeBetween(int t1, int t2) {
        int diff = Math.abs(t1 - t2);
        return Math.min(diff, 24-diff);
    }

    private static int zonIdToOffsetInt(ZoneId zoneId, Instant now) {
        String offset = zoneId.getRules().getStandardOffset(now).getId();
        if (offset.equals("Z")) {
            return 0;
        }
        int sign = offset.charAt(0) == '+' ? 1 : -1;
        int hours = Integer.valueOf(offset.substring(1, 3));
        return sign * hours;
    }
}
