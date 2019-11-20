package ic.doc.sgo;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupTest {
    private Group g1 = Group.from(new ArrayList<>());
    private Group g2 = Group.from(new ArrayList<>());
    private Student s1 = new Student.Builder(String.valueOf(1)).createStudent();
    private Student s2 = new Student.Builder(String.valueOf(2)).createStudent();
    private Student s3 = new Student.Builder("3")
        .setTimeZone(ZoneId.of("UTC+1"))
        .setAge(1)
        .setGender("female")
        .createStudent();


    private Student s4 = new Student.Builder("4")
        .setTimeZone(ZoneId.of("UTC+2"))
        .setAge(2)
        .setGender("female")
        .createStudent();

    private Student s5 = new Student.Builder("5")
        .setTimeZone(ZoneId.of("UTC+2"))
        .setAge(2)
        .setGender("male")
        .createStudent();

    private Student s6 = new Student.Builder("6")
        .setAge(3)
        .setGender("female")
        .setTimeZone(ZoneId.of("UTC+7"))
        .createStudent();

    private Student s7 = new Student.Builder("7")
        .setAge(8)
        .setGender("female")
        .setTimeZone(ZoneId.of("UTC+1"))
        .createStudent();

    @Test
    public void sizeIsZZeroWhenEmpty() {
        assertEquals(0, g1.size());
    }

    @Test
    public void ableToAddStudent() {
        assertFalse(g1.contains(s1));

        g1.add(s1);
        assertTrue(g1.contains(s1));
        assertEquals(1, g1.size());
    }

    @Test
    public void avoidAddingSameStudent() {
        g1.add(s1);
        g1.add(s1);
        assertTrue(g1.contains(s1));
        assertEquals(1, g1.size());
    }

    @Test
    public void ableToAddMultipleStudents() {
        g1.add(s1);
        g1.add(s2);
        assertEquals(2, g1.size());
    }

    @Test
    public void ableToRemoveStudent() {
        g1.add(s1);
        g1.add(s2);
        g1.remove(s1);
        assertEquals(1, g1.size());
        assertTrue(g1.contains(s2));
        assertFalse(g1.contains(s1));
    }

    @Test
    public void returnsFalseIfStudentNotRemoved() {
        assertFalse(g1.remove(s1));
    }

    @Test
    public void studentIsConsistentWithGroup() {
        g1.add(s1);
        assertSame(s1.getGroup(), g1);
        g2.add(s1);
        assertSame(s1.getGroup(), g2);
        assertFalse(g1.contains(s1));
        g2.remove(s1);
        assertNull(s1.getGroup());
    }

    @Test
    public void returnsFalseIfStudentWasNotAdded() {
        g1.add(s1);
        assertFalse(g1.add(s1));
    }

    @Test
    public void canAddListOfStudents() {
        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        g1.addAll(students);
        assertEquals(3, g1.size());
        assertTrue(g1.contains(s1));
        assertTrue(g1.contains(s2));
        assertTrue(g1.contains(s3));
    }

    @Test
    public void correctAgeDifference() {
        Student[] students = new Student[]{s3, s4, s5, s6, s7};
        g1.addAll(Arrays.asList(students));

        assertEquals(7, g1.getAgeDiffOfGroup());
    }

    @Test
    public void correctGenderInfo() {
        Student[] students = new Student[]{s3, s4, s5, s6, s7};
        g1.addAll(Arrays.asList(students));

        assertEquals(4, g1.getFemaleNumberOfGroup());
        assertEquals(1, g1.getMaleNumberOfGroup());
        assertFalse(g1.isGroupSameGender());
    }

    @Test
    public void correctTimeZoneDiff() {
        Student[] students = new Student[]{s3, s4, s5, s6, s7};
        g1.addAll(Arrays.asList(students));

        assertEquals(6, g1.getTimezoneDiffOfGroup());
    }


}