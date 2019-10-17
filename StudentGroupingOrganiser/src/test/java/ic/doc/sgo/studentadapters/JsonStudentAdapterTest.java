package ic.doc.sgo.studentadapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonStudentAdapterTest {

    @Test
    public void returnEmptyIfJsonNoIdMember() {
        JsonObject json = new JsonObject();
        json.addProperty("gender", "Male");
        assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.empty()));
    }

    @Test
    public void canReturnStudentIfJsonHasIdMember() {
        JsonObject json = new JsonObject();
        json.addProperty("id", "123");
        Student student = new Student.Builder("123").createStudent();
        assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
    }

    @Test
    public void canReturnStudentIfJsonHasMembersWithoutAgeAndTimezone() {
        // exclude Age in test because age is calculated from dob and current time
        // exclude timezone because not implemented yet
        JsonObject json = new JsonObject();
        json.addProperty("id", "123");
        json.addProperty("gender", "Male");
        json.addProperty("career", "Biology");
        json.addProperty("degree", "PhD");
        json.addProperty("workYearNum", "15y");
        json.addProperty("cohort", "18J");
        Student student = new Student.Builder("123")
                .setGender("Male")
                .setCareer("Biology")
                .setDegree("PhD")
                .setWorkYearNum(15)
                .setCohort("18J")
                .createStudent();
        assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
    }

    @Test
    public void canReturnStudentIfJsonHasMembersWithAdditionalAttributes() {
        JsonArray array = new JsonArray();
        array.add(1);
        array.add(2);
        JsonObject obj = new JsonObject();
        JsonObject json = new JsonObject();
        json.addProperty("id", "123");
        json.addProperty("str", "Str");
        json.addProperty("int", 1);
        json.addProperty("float", 1.1);
        json.addProperty("bool", true);
        json.add("array", array);
        json.add("obj", obj);
        Student student = new Student.Builder("123")
                .addAttribute("str", new JsonPrimitive("Str"))
                .addAttribute("int", new JsonPrimitive(1))
                .addAttribute("float", new JsonPrimitive(1.1))
                .addAttribute("bool", new JsonPrimitive(true))
                .addAttribute("array", array)
                .addAttribute("obj", obj)
                .createStudent();
        assertThat(new JsonStudentAdapter(json).toStudent(), is(Optional.of(student)));
    }
}