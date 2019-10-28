package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RandomGroupingStrategyTest {

    private Map<Student, Boolean> testedStudents = new HashMap<>();

    @Test
    public void allocateStudentsAccordingToRangeConstrainGroupsSize() {
        Constraint constraint = new Constraint.Builder(4, 7).createConstrain();
        List<Student> students = new ArrayList<>();
        int number = 1000;
        for (int i = 0; i < 1000; i++) {
            Student student = new Student.Builder(String.valueOf(i)).createStudent();
            testedStudents.put(student, false);
            students.add(student);
        }
        List<Group> groups = new RandomGroupingStrategy().apply(students, constraint);
        for (int i = 1; i < groups.size(); i++) {
            Group group = groups.get(i);
            assertEquals(group.getId(), i);
            assertTrue(group.size() <= constraint.getGroupSizeUpperBound());
            assertTrue(group.size() >= constraint.getGroupSizeLowerBound());
            for (Student student : group.getStudents()) {
                assertFalse(testedStudents.get(student));
                testedStudents.put(student, true);
            }
        }
    }

    @Test
    public void allocateStudentsAccordingToFixedConstrainGroupsSize() {
        Constraint constraint = new Constraint.Builder(11, 11).createConstrain();
        List<Student> students = new ArrayList<>();
        int number = 1000;
        for (int i = 0; i < 1000; i++) {
            Student student = new Student.Builder(String.valueOf(i)).createStudent();
            testedStudents.put(student, false);
            students.add(student);
        }
        List<Group> groups = new RandomGroupingStrategy().apply(students, constraint);

        assertTrue(groups.get(0).size() < 11);

        for (int i = 1; i < groups.size(); i++) {
            Group group = groups.get(i);
            assertEquals(group.getId(), i);
            assertTrue(group.size() <= constraint.getGroupSizeUpperBound());
            assertTrue(group.size() >= constraint.getGroupSizeLowerBound());
            for (Student student : group.getStudents()) {
                assertFalse(testedStudents.get(student));
                testedStudents.put(student, true);
            }
        }
    }

    @Test
    public void allocateStudentsWhoseNumberSmallerThanSize() {
        Constraint constraint = new Constraint.Builder(4, 4).createConstrain();
        List<Student> students = new ArrayList<>();
        int number = 3;
        for (int i = 0; i < number; i++) {
            Student student = new Student.Builder(String.valueOf(i)).createStudent();
            testedStudents.put(student, false);
            students.add(student);
        }
        List<Group> groups = new RandomGroupingStrategy().apply(students, constraint);
        assertEquals(1, groups.size());
        assertEquals(groups.get(0).size(), number);
    }

}