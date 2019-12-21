package ic.doc.sgo;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupTest {
    private Group g1 = Group.from(new ArrayList<>());
    private Group g2 = Group.from(new ArrayList<>());
    private Student s1 = new Student.Builder(String.valueOf(1)).createStudent();
    private Student s2 = new Student.Builder(String.valueOf(2)).createStudent();

    @Test
    public void ableToAddStudent() {
        assertEquals(0, g1.size());
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
        assertFalse(g1.remove(s1));
    }

    @Test
    public void studentIsConsistentWithGroup() {
        g1.add(s1);
        assertTrue(Group.getGroup(s1).isPresent());
        assertSame(Group.getGroup(s1).get(), g1);
        g2.add(s1);
        assertTrue(Group.getGroup(s1).isPresent());
        assertSame(Group.getGroup(s1).get(), g2);
        assertFalse(g1.contains(s1));
        g2.remove(s1);
        assertFalse(Group.getGroup(s1).isPresent());
    }

    @Test
    public void testIsSameGroup() {
        g1.add(s1);
        g1.add(s2);
        assertTrue(Group.isSameGroup(s1, s2));
        g2.add(s1);
        assertFalse(Group.isSameGroup(s1, s2));
    }

}