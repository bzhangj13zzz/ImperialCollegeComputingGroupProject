package ic.doc.sgo.groupingstrategies.vectorspacestrategies;

import ic.doc.sgo.Attributes;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import ic.doc.sgo.TimeZoneCalculator;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {
    private Double eps = 1e-6;

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

    private final Cluster c1 = Cluster.from(new ArrayList<>());
    private final Cluster c2 = Cluster.from(new ArrayList<>());

    @Test
    public void createCorrectNodeFromStudent() {
        Double nodeTime = n1.getValueOfDimensionOf(Attributes.TIMEZONE);
        Double studentTime = (double) TimeZoneCalculator.timeZoneInInteger(s1.getTimeZone().orElse(null));
        assertTrue(Math.abs(nodeTime-studentTime) <= eps);

        Double nodeAge = n1.getValueOfDimensionOf(Attributes.AGE);
        Double studentAge = (double) s1.getAge().orElse(-1);
        assertTrue(Math.abs(nodeAge - studentAge) <= eps);

        assertEquals(n1.getGender(), s1.getGender().orElse(""));
        assertEquals(n3.getGender(), s3.getGender().orElse(""));
    }

    @Test
    public void returnCorrectDimensions() {
        List<Attributes> attributes = new ArrayList<>();
        n1.getDimensions().forEach(attributes::add);

        assertTrue(attributes.contains(Attributes.AGE));
        assertTrue(attributes.contains(Attributes.TIMEZONE));
        assertFalse(attributes.contains(Attributes.GENDER));

    }

    @Test
    public void testSwapCluster() {
        c1.add(n1);
        c2.add(n2);

        Node.swapCluster(n1, n2);
        assertSame(n1.getCluster(), c2);
        assertSame(n2.getCluster(), c1);
        assertTrue(c1.contains(n2));
        assertTrue(c2.contains(n1));
        assertFalse(c1.contains(n1));
        assertFalse(c2.contains(n2));
    }

}