package ic.doc.sgo;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StudentTest {
    private final Student s1 = new Student.Builder(String.valueOf(1)).createStudent();
    private final Student s2 = new Student.Builder(String.valueOf(2)).createStudent();
    private final Group g1 = Group.from(new ArrayList<>());
    private final Group g2 = Group.from(new ArrayList<>());

    @Test
    public void testSwapGroup() {
        g1.add(s1);
        g2.add(s2);

        Student.swapGroup(s1, s2);
        assertSame(s1.getGroup(), g2);
        assertSame(s2.getGroup(), g1);
        assertTrue(g1.contains(s2));
        assertTrue(g2.contains(s1));
        assertFalse(g1.contains(s1));
        assertFalse(g2.contains(s2));
    }

    @Test
    public void testIsSameGroup() {
        g1.add(s1);
        g1.add(s2);
        assertTrue(Student.isSameGroup(s1, s2));
        g2.add(s1);
        assertFalse(Student.isSameGroup(s1, s2));

    }
}