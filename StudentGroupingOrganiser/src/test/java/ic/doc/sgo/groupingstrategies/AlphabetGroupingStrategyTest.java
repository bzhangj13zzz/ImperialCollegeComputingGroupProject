package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constrain;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlphabetGroupingStrategyTest {

    private final static Student STUDENT_A = new Student.Builder("100", "Aaa Aaa").createStudent();
    private final static Student STUDENT_B = new Student.Builder("101", "Aaa bbb").createStudent();
    private final static Student STUDENT_C = new Student.Builder("102", "aaa Ccc").createStudent();

    @Test
    public void canGroupStudentAlphabetically() {
        List<Student> students = Arrays.asList(STUDENT_C, STUDENT_B, STUDENT_A);
        List<Group> groups = new AlphabetGroupingStrategy(1).apply(students, new Constrain());

        List<Group> expected = Arrays.asList(Group.of(STUDENT_A), Group.of(STUDENT_B), Group.of(STUDENT_C));
        assertThat(groups, is(expected));
    }

    @Test
    public void canGroupStudentAlphabeticallyWhenNotExactDivision() {
        List<Student> students = Arrays.asList(STUDENT_C, STUDENT_B, STUDENT_A);
        List<Group> groups = new AlphabetGroupingStrategy(2).apply(students, new Constrain());

        List<Group> expected = Arrays.asList(Group.of(STUDENT_A, STUDENT_B), Group.of(STUDENT_C));
        assertThat(groups, is(expected));
    }
}