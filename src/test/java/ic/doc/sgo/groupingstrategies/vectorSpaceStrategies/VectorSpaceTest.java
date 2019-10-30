package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Attributes;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.time.ZoneId;
import java.util.Arrays;

import static org.junit.Assert.*;

public class VectorSpaceTest {
    Double eps = 1e-6;
    @Test
    public void createCorrectlyFromConstrain() {

        //Timezone
        Constraint constraint = new Constraint.Builder(4, 5)
                .setTimezoneDiff(2).createConstrain();
        assertTrue(constraint.getTimezoneDiff().isPresent());
        VectorSpace vectorSpace = new VectorSpace(constraint);
        assertEquals(4, vectorSpace.getClusterSizeLowerBound());
        assertEquals(5, vectorSpace.getClusterSizeUpperBound());
        Double timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE);
        assertTrue( 2-eps <= timeZoneDiff && timeZoneDiff <= 2+eps);

        //Age
        constraint = new Constraint.Builder(4 , 4)
                .setAgeDiff(3).createConstrain();
        vectorSpace = new VectorSpace(constraint);
        Double ageDiff = vectorSpace.getDimensionValue(Attributes.AGE);
        assertTrue( 3-eps <= ageDiff && ageDiff <= 3 + eps);

        //Gender
        constraint = new Constraint.Builder(4, 4)
                .setMinFemale(3).createConstrain();
        vectorSpace = new VectorSpace(constraint);
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(0, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));

        constraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .createConstrain();
        vectorSpace = new VectorSpace(constraint);
        assertEquals(1, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));

        //ALERT: every time add an new Attribute, add corresponding test here.

        //Mix
        constraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .setTimezoneDiff(2)
                .setAgeDiff(3)
                .createConstrain();
        vectorSpace = new VectorSpace(constraint);
        assertEquals(5, vectorSpace.getClusterSizeLowerBound());
        assertEquals(6, vectorSpace.getClusterSizeUpperBound());
        assertEquals(1, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));
        timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE);
        assertTrue( 2-eps <= timeZoneDiff && timeZoneDiff <= 2+eps);
        ageDiff = vectorSpace.getDimensionValue(Attributes.AGE);
        assertTrue( 3-eps <= ageDiff && ageDiff <= 3 + eps);

    }

    @Test
    public void testIsValidCluster() {
        Constraint constraint = new Constraint.Builder(2, 3)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .setTimezoneDiff(2)
                .setAgeDiff(3)
                .createConstrain();
        VectorSpace vectorSpace = new VectorSpace(constraint);
        Student s1 = new Student.Builder("1").setTimeZone(ZoneId.of("UTC+1")).createStudent();
        Student s2 = new Student.Builder("2").setTimeZone(ZoneId.of("UTC+2")).createStudent();
        Student s3 = new Student.Builder("3").setTimeZone(ZoneId.of("UTC+2")).createStudent();
        Student s4 = new Student.Builder("4").setTimeZone(ZoneId.of("UTC+2")).createStudent();
        Student s5 = new Student.Builder("5").setTimeZone(ZoneId.of("UTC+2")).createStudent();

        Node n1 = Node.createFromStudentWithConstraint(s1, constraint);
        Node n2 = Node.createFromStudentWithConstraint(s2, constraint);
        Node n3 = Node.createFromStudentWithConstraint(s1, constraint);
        Node n4 = Node.createFromStudentWithConstraint(s2, constraint);
        Node n5 = Node.createFromStudentWithConstraint(s1, constraint);
        Node n6 = Node.createFromStudentWithConstraint(s2, constraint);


        Cluster cluster = Cluster.from(Arrays.asList(n1, n2));
        assertTrue(vectorSpace.isValidCluster(cluster));
    }

}