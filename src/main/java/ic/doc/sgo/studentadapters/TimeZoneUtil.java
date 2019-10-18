package ic.doc.sgo.studentadapters;

import com.google.gson.*;
import com.neovisionaries.i18n.CountryCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Utility class that handles time zone.
 **/
final class TimeZoneUtil {
    private static final String JSON_PATH = "src/main/java/ic/doc/sgo/studentadapters/countries_with_timeZones.json";
    private static final String API_KEY = "";
    private static final Gson gson = new Gson();

    static ZoneId getTimeZoneId(String cityName, String countryName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
        JsonArray array = JsonParser.parseReader(br).getAsJsonArray();
        for (JsonElement elem : array) {
            JsonObject jsonObject = elem.getAsJsonObject();
            if (jsonObject.get("CountryName").getAsString().equalsIgnoreCase(countryName)
            || jsonObject.get("IsoAlpha2").getAsString().equalsIgnoreCase(countryName)
            || jsonObject.get("IsoAlpha3").getAsString().equalsIgnoreCase(countryName)) {
                JsonArray timeZones = jsonObject.get("TimeZones").getAsJsonArray();
                for (JsonElement timeZone : timeZones) {
                    String timeZoneStr = timeZone.getAsString();
                    if (timeZoneStr.toLowerCase().contains(cityName.replace(" ", "").toLowerCase())) {
                        return ZoneId.of(timeZoneStr);
                    }
                }
            }
        }

        String cityAndCountry = cityName + " " + countryName;
        // Call the API to get the Location.
        Location location = getLocationFromCityAndCountry(cityAndCountry);
        return getZoneIdFromLocation(location);
    }

    private static ZoneId getZoneIdFromLocation(Location location) throws Exception {
        URL url = new URL(
                String.format(
                "https://maps.googleapis.com/maps/api/timezone/json?timestamp=%d&location=%s,%s&key=%s",
                // Convert the timestamp from milliseconds into second.
                (new Date()).getTime() / 1000,
                location.latitude,
                location.longitude,
                API_KEY));

        return ZoneId.of(gson
                .fromJson(getJsonResponse(url), JsonObject.class)
                .get("timeZoneId")
                .getAsString());
    }

    private static Location getLocationFromCityAndCountry(String cityAndCountry) throws Exception {
        URL url = new URL(
                String.format(
                        "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                        // Replace all space with '+' so that it can be passed into the url.
                        cityAndCountry.replaceAll("\\s+", "+"),
                        API_KEY));

        JsonObject location = gson
                .fromJson(getJsonResponse(url), JsonObject.class)
                .getAsJsonArray("results")
                .get(0).getAsJsonObject()
                .getAsJsonObject("geometry")
                .getAsJsonObject("location");

        return new Location(location.get("lat").getAsString(), location.get("lng").getAsString());
    }

    private static String getJsonResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();
        System.out.println(status);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }


    private static class Location {
        private String latitude;
        private String longitude;

        private Location(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
