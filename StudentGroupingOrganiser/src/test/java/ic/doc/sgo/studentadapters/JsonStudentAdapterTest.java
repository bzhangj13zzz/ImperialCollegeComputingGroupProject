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
    public void canInterpretStudentFromJSONWithAllBasicFields() {
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
            Student student = new Student.Builder("12345", "Dummy")
                    .setCountryCode(CountryCode.CN)
                    .setTimeZone(ZoneId.of("UTC+8"))
                    .setGender("Male")
                    .setAge(40)
                    .setCareer("Biology")
                    .setDegree("Master")
                    .setWorkYearNum(15)
                    .setCohort("2018J")
                    .createStudent();
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void canInterpretStudentFromJSONWithOnlyNecessaryFields() {
        try {
            JSONObject json = new JSONObject("{\n" +
                    "  \"id\": \"12345\",\n" +
                    "  \"name\": \"Dummy\"\n" +
                    "}");
            Student student = new Student.Builder("12345", "Dummy")
                    .createStudent();
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
        } catch (JSONException e) {
            fail();
        }
    }


    @Test
    public void canInterpretStudentFromJSONWithAdditionalFields() {
        try {
            JSONObject json = new JSONObject("{\n" +
                    "  \"id\": \"12345\",\n" +
                    "  \"name\": \"Dummy\",\n" +
                    "  \"religion\": \"No Religion\"\n" +
                    "}");
            Student student = new Student.Builder("12345", "Dummy")
                    .addAttribute("religion", "No Religion")
                    .createStudent();
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void canInterpretStudentFromJSONWithAllFieldsAndAdditionalFields() {
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
                    "  \"cohort\": \"2018J\",\n" +
                    "  \"religion\": \"No Religion\"\n" +
                    "}");
            Student student = new Student.Builder("12345", "Dummy")
                    .setCountryCode(CountryCode.CN)
                    .setTimeZone(ZoneId.of("UTC+8"))
                    .setGender("Male")
                    .setAge(40)
                    .setCareer("Biology")
                    .setDegree("Master")
                    .setWorkYearNum(15)
                    .setCohort("2018J")
                    .addAttribute("religion", "No Religion")
                    .createStudent();
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
                    "  \"countryCode\": \"CHN\",\n" +
                    "  \"timeZone\": \"UTC+8\"\n" +
                    "}");
            assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.empty()));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }


}