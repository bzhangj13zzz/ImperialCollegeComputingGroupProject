package ic.doc.sgo.studentadapters;

import com.google.gson.JsonObject;
import ic.doc.sgo.Student;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class JsonStudentAdapter implements StudentAdapter {
    private final JsonObject studentJson;
    private final LocalDate now;

    public JsonStudentAdapter(JsonObject studentJson, LocalDate now) {
        this.studentJson = studentJson;
        this.now = now;
    }

    @Override
    public Optional<Student> toStudent() {
        if (!studentJson.has("id")) {
            return Optional.empty();
        }
        Student.Builder studentBuilder;
        studentBuilder = new Student.Builder(studentJson.get("id").getAsString());
        String cityName = "";
        String countryName = "";
        for (String key : studentJson.keySet()) {
            switch (key) {
                case "id":
                    // do nothing
                    break;
                case "country":
                    countryName = studentJson.get(key).getAsString();
                    studentBuilder.addAttribute(key, countryName);
                    break;
                case "currentCity":
                    cityName = studentJson.get(key).getAsString();
                    studentBuilder.addAttribute(key, cityName);
                    break;
                case "gender":
                    studentBuilder.setGender(studentJson.get(key).getAsString().trim().toLowerCase());
                    break;
                case "dob":
                    String dobStr = studentJson.get(key).getAsString();
                    studentBuilder.setAge(JsonFormatUtil.dobToAge(dobStr, now));
                    break;
                case "career":
                    studentBuilder.setCareer(studentJson.get(key).getAsString());
                    break;
                case "degree":
                    studentBuilder.setDegree(studentJson.get(key).getAsString());
                    break;
                case "workYearNum":
                    String yearNumStr = studentJson.get(key).getAsString();
                    double yearNum = JsonFormatUtil.yearNumStringToDouble(yearNumStr);
                    studentBuilder.setWorkYearNum(yearNum);
                    break;
                case "cohort":
                    studentBuilder.setCohort(studentJson.get(key).getAsString());
                    break;
                case "timezone":
                    studentBuilder.setTimeZone(ZoneId.of(studentJson.get(key).getAsString()));
                    break;
                case "age":
                    studentBuilder.setAge(studentJson.get(key).getAsInt());
                    break;
                default:
                    studentBuilder.addAttribute(key, studentJson.get(key).getAsString());
            }
        }
        if (!countryName.equals("")) {
            try {
                ZoneId timeZoneId = TimeZoneUtil.getTimeZoneId(cityName, countryName);
                studentBuilder.setTimeZone(timeZoneId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.of(studentBuilder.createStudent());
    }
}
