package ic.doc.sgo.studentadapters;

import com.neovisionaries.i18n.CountryCode;
import ic.doc.sgo.Student;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.util.Iterator;
import java.util.Optional;

public class JsonStudentAdapter implements StudentAdapter {
    private final JSONObject studentJson;

    public JsonStudentAdapter(JSONObject studentJson) {
        this.studentJson = studentJson;
    }

    @Override
    public Optional<Student> toStudent() {
        Student.Builder studentBuilder;
        try {
            studentBuilder = new Student.Builder(
                    studentJson.getString("id"),
                    studentJson.getString("name")
            );
        } catch (JSONException e) {
            return Optional.empty();
        }
        for (Iterator it = studentJson.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            switch (key) {
                case "id":
                    // do nothing
                case "name":
                    // do nothing
                    break;
                case "countryCode":
                    studentBuilder.setCountryCode(CountryCode.getByCodeIgnoreCase(studentJson.optString(key)));
                    break;
                case "timeZone":
                    studentBuilder.setTimeZone(ZoneId.of(studentJson.optString(key)));
                    break;
                case "gender":
                    studentBuilder.setGender(studentJson.optString(key));
                    break;
                case "age":
                    studentBuilder.setAge(studentJson.optInt(key));
                    break;
                case "career":
                    studentBuilder.setCareer(studentJson.optString(key));
                    break;
                case "degree":
                    studentBuilder.setDegree(studentJson.optString(key));
                    break;
                case "workYearNum":
                    studentBuilder.setWorkYearNum(studentJson.optInt(key));
                    break;
                case "cohort":
                    studentBuilder.setCohort(studentJson.optString(key));
                    break;
                default:
                    studentBuilder.addAttribute(key, studentJson.opt(key));
            }
        }
        return Optional.of(studentBuilder.createStudent());
    }
}
