package ic.doc.sgo.studentadapters;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.*;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Utility class that handles time zone.
 **/
final class TimeZoneUtil {
    private static final String JSON_PATH = "src/main/java/ic/doc/sgo/studentadapters/countries_with_timeZones.json";
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");
    private static final Gson gson = new Gson();
    private static final LoadingCache<CityCountry, ZoneId> cache =
            buildCache(200, 30, TimeUnit.DAYS);

    private TimeZoneUtil() {
    }

    static ZoneId getTimeZoneId(String cityName, String countryName) throws Exception {
        Optional<ZoneId> lookup = lookUpTimezoneJson(cityName, countryName);
        if (lookup.isPresent()) {
            return lookup.get();
        }

        return cache.get(new CityCountry(cityName, countryName));
    }

    private static Optional<ZoneId> lookUpTimezoneJson(String cityName, String countryName) {
        try (BufferedReader br = new BufferedReader(new FileReader(JSON_PATH))) {
            JsonArray array = JsonParser.parseReader(br).getAsJsonArray();
            for (JsonElement elem : array) {
                JsonObject jsonObject = elem.getAsJsonObject();
                if (jsonObject.get("CountryName").getAsString().equalsIgnoreCase(countryName)
                        || jsonObject.get("IsoAlpha2").getAsString().equalsIgnoreCase(countryName)
                        || jsonObject.get("IsoAlpha3").getAsString().equalsIgnoreCase(countryName)) {
                    JsonArray timeZones = jsonObject.get("TimeZones").getAsJsonArray();
                    if (timeZones.size() == 1) {
                        return Optional.of(ZoneId.of(timeZones.get(0).getAsString()));
                    }
                    for (JsonElement timeZone : timeZones) {
                        String timeZoneStr = timeZone.getAsString();
                        if (timeZoneStr.toLowerCase().contains(cityName.replace(" ", "_").toLowerCase())) {
                            return Optional.of(ZoneId.of(timeZoneStr));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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

        try {
            JsonObject location = gson
                    .fromJson(getJsonResponse(url), JsonObject.class)
                    .getAsJsonArray("results")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("geometry")
                    .getAsJsonObject("location");
            return new Location(location.get("lat").getAsString(),
                    location.get("lng").getAsString());
        } catch (Exception e) {
            // TODO: Unexpected error, should try to search again
            return new Location("51.5074", "0.1278");
        }
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

    private static LoadingCache<CityCountry, ZoneId> buildCache(int maxSize, long duration,
                                                                @NotNull java.util.concurrent.TimeUnit unit) {
        return CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(duration, unit)
                .build(
                        new CacheLoader<CityCountry, ZoneId>() {
                            public ZoneId load(CityCountry cityCountry) throws Exception {
                                String cityAndCountry = cityCountry.city + " " + cityCountry.country;
                                // Call the API to get the Location.
                                Location location = getLocationFromCityAndCountry(cityAndCountry);
                                return getZoneIdFromLocation(location);
                            }
                        });
    }


    private static class Location {
        private String latitude;
        private String longitude;

        private Location(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static class CityCountry {
        String city;
        String country;

        CityCountry(String city, String country) {
            this.city = city;
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CityCountry that = (CityCountry) o;
            return Objects.equals(city, that.city) &&
                    Objects.equals(country, that.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(city, country);
        }
    }
}
