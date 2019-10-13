package ic.doc.sgo.studentadapters;

import com.neovisionaries.i18n.CountryCode;
import ic.doc.sgo.Student;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.util.Optional;

public class JsonStudentAdapter implements StudentAdapter {
    private final JSONObject studentJson;

    public JsonStudentAdapter(JSONObject studentJson) {
        this.studentJson = studentJson;
    }

    @Override
    public Optional<Student> toStudent() {
        try {
            return Optional.of(new Student(
                    studentJson.getString("id"),
                    studentJson.getString("name"),
                    CountryCode.getByCodeIgnoreCase(studentJson.getString("countryCode")),
                    ZoneId.of(studentJson.getString("timeZone")),
                    studentJson.getString("gender"),
                    studentJson.getInt("age"),
                    studentJson.getString("career"),
                    studentJson.getString("degree"),
                    studentJson.getInt("workYearNum"),
                    studentJson.getString("cohort")
            ));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}
