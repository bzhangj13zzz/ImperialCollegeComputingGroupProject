package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ClusterTest {

    private Cluster c1 = Cluster.from(new ArrayList<>());
    private Cluster c2 = Cluster.from(new ArrayList<>());

    private Constraint constraint = new Constraint.Builder(3, 4)
            .setMinFemale(1)
            .setMinMale(2)
            .setTimezoneDiff(2)
            .setAgeDiff(3)
            .createConstrain();

    private VectorSpace vectorSpace = Converters.VectorSpaceFromConstraint(constraint);

    private Student s1 = new Student.Builder("1")
            .setTimeZone(ZoneId.of("UTC+1"))
            .setAge(1)
            .setGender("male").createStudent();

    private Student s2 = new Student.Builder("2")
            .setTimeZone(ZoneId.of("UTC+2"))
            .setAge(2)
            .setGender("male").createStudent();

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

    private Node n1 = Converters.NodeFromStudentAndConstraint(s1, constraint);
    private Node n2 = Converters.NodeFromStudentAndConstraint(s2, constraint);
    private Node n3 = Converters.NodeFromStudentAndConstraint(s3, constraint);
    private Node n4 = Converters.NodeFromStudentAndConstraint(s4, constraint);
    private Node n5 = Converters.NodeFromStudentAndConstraint(s5, constraint);
    private Node n6 = Converters.NodeFromStudentAndConstraint(s6, constraint);
    private Node n7 = Converters.NodeFromStudentAndConstraint(s7, constraint);


    @Test
    public void ableToAddStudent() {
        assertEquals(0, c1.size());
        assertFalse(c1.contains(n1));

        c1.add(n1);
        assertTrue(c1.contains(n1));
        assertEquals(1, c1.size());
    }

    @Test
    public void avoidAddingSameStudent() {
        c1.add(n1);
        c1.add(n1);
        assertTrue(c1.contains(n1));
        assertEquals(1, c1.size());
    }

    @Test
    public void ableToAddMultipleStudents() {
        c1.add(n1);
        c1.add(n2);
        assertEquals(2, c1.size());
    }

    @Test
    public void ableToRemoveStudent() {
        c1.add(n1);
        c1.add(n2);
        c1.remove(n1);
        assertEquals(1, c1.size());
        assertTrue(c1.contains(n2));
        assertFalse(c1.contains(n1));
        assertFalse(c1.remove(n1));
    }

    @Test
    public void studentIsConsistentWithGroup() {
        c1.add(n1);
        assertSame(n1.getCluster(), c1);
        c2.add(n1);
        assertSame(n1.getCluster(), c2);
        assertFalse(c1.contains(n1));
        c2.remove(n1);
        assertNull(n1.getCluster());
    }

    @Test
    public void testDiscreteAttributeNumber() {
        c1.addAll(new ArrayList<>(Arrays.asList(n1, n2, n3, n4, n5, n6, n7)));
        assertEquals(3, (int) c1.getNumberOf(Attributes.GENDER, "male"));
        assertEquals(4, (int) c1.getNumberOf(Attributes.GENDER, "female"));
    }
}