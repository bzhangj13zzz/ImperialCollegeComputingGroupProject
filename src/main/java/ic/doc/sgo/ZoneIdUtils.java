package ic.doc.sgo;


import java.time.Instant;
import java.time.ZoneId;

public final class ZoneIdUtils {

    private ZoneIdUtils() {
    }

    public static int zoneIdToInteger(ZoneId zoneId) {
        if (zoneId == null) {
            return 0;
        }
        int res = Math.floorMod(zoneIdToOffsetInt(zoneId, Instant.now()), 24);
        if (res > 12) {
            res -= 24;
        }
        return res;
    }

    public static int timeBetween(ZoneId zoneId1, ZoneId zoneId2) {
        Instant now = Instant.now();
        int offset1 = zoneIdToOffsetInt(zoneId1, now);
        int offset2 = zoneIdToOffsetInt(zoneId2, now);
        int diff = Math.abs(Math.floorMod(offset1, 24) - Math.floorMod(offset2, 24));
        return Math.min(diff, 24 - diff);
    }

    private static int zoneIdToOffsetInt(ZoneId zoneId, Instant now) {
        String offset = zoneId.getRules().getStandardOffset(now).getId();
        if (offset.equals("Z")) {
            return 0;
        }
        int sign = offset.charAt(0) == '+' ? 1 : -1;
        int hours = Integer.valueOf(offset.substring(1, 3));
        return sign * hours;
    }
}
