package ic.doc.sgo.studentadapters;

import com.neovisionaries.i18n.CountryCode;
import ic.doc.sgo.Student;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.time.ZoneId;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class JsonStudentAdapterTest {

    @Test
    public void canInterpretStudentFromJSON() {
        try {
            JSONObject json = new JSONObject("{\n" +
                    "  \"id\": \"12345\",\n" +
                    "  \"name\": \"Dummy\",\n" +
                    "  \"countryCode\": \"CHN\",\n" +
                    "  \"timeZone\": \"UTC+8\",\n" +
                    "  \"gender\": \"Male\",\n" +
                    "  \"age\": 40,\n" +
                    "  \"career\": \"Biology\",\n" +
                    "  \"degree\": \"Master\",\n" +
                    "  \"workYearNum\": 15,\n" +
                    "  \"cohort\": \"2018J\"\n" +
                    "}");
            Student student = new Student(
                    "12345",
                    "Dummy",
                    CountryCode.CN,
                    ZoneId.of("UTC+8"),
                    "Male",
                    40,
                    "Biology",
                    "Master",
                    15,
                    "2018J"
            );
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void returnOptionalEmptyIfJSONInvalid() {
        try {
            JSONObject json = new JSONObject("{\n" +
                    "  \"id\": \"12345\",\n" +
                    "  \"name\": \"Dummy\"\n" +
                    "}");
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.empty()));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }


}