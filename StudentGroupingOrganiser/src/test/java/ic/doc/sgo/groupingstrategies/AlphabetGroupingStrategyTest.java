package ic.doc.sgo.groupingstrategies;

import com.neovisionaries.i18n.CountryCode;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlphabetGroupingStrategyTest {

    private final static Student STUDENT_A = new Student("100", "Aaa Aaa", CountryCode.UK, ZoneId.of("UTC+1"),
            "Male", 40, "Technology", "Master", 15, "2019J");
    private final static Student STUDENT_B = new Student("101", "Aaa Bbb", CountryCode.CN, ZoneId.of("UTC+8"),
            "Female", 43, "Technology", "Master", 16, "2019J");
    private final static Student STUDENT_C = new Student("102", "aaa Caa", CountryCode.UK, ZoneId.of("UTC+1"),
            "Male", 42, "Technology", "Master", 15, "2019J");

    @Test
    public void canGroupStudentAlphabetically() {
        List<Student> students = Arrays.asList(STUDENT_C, STUDENT_B, STUDENT_A);
        List<Group> groups = new AlphabetGroupingStrategy(1).apply(students);

        List<Group> expected = Arrays.asList(Group.of(STUDENT_A), Group.of(STUDENT_B), Group.of(STUDENT_C));
        assertThat(groups, is(expected));
    }

    @Test
    public void canGroupStudentAlphabeticallyWhenNotExactDivision() {
        List<Student> students = Arrays.asList(STUDENT_C, STUDENT_B, STUDENT_A);
        List<Group> groups = new AlphabetGroupingStrategy(2).apply(students);

        List<Group> expected = Arrays.asList(Group.of(STUDENT_A, STUDENT_B), Group.of(STUDENT_C));
        assertThat(groups, is(expected));
    }
}