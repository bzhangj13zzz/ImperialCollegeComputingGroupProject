package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import ic.doc.sgo.TimeZoneCalculator;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConvertersTest {

    private Double eps = 1e-6;

    private Constraint constraintForSameGender = new Constraint.Builder(3, 4)
            .setIsSameGender()
            .createConstrain();

    private Constraint constraint = new Constraint.Builder(3, 4)
            .setMinFemale(1)
            .setMinMale(2)
            .setTimezoneDiff(2)
            .setAgeDiff(3)
            .createConstrain();

    private VectorSpace vectorSpaceForSameGender = Converters.VectorSpaceFromConstraint(constraintForSameGender);

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

    private Node nodeForSameGender = Converters.NodeFromStudentAndConstraint(s1, constraintForSameGender);

    private final Cluster c1 = Cluster.from(new ArrayList<>());
    private final Cluster c2 = Cluster.from(new ArrayList<>());


    @Test
    public void vectorSpaceFromConstraint() {
        //Timezone
        Constraint testConstraint = new Constraint.Builder(4, 5)
                .setTimezoneDiff(2).createConstrain();
        assertTrue(testConstraint.getTimezoneDiff().isPresent());
        VectorSpace vectorSpace = Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(4, vectorSpace.getClusterSizeLowerBound());
        assertEquals(5, vectorSpace.getClusterSizeUpperBound());
        Double timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE);
        assertTrue(2 - eps <= timeZoneDiff && timeZoneDiff <= 2 + eps);

        //Age
        testConstraint = new Constraint.Builder(4, 4)
                .setAgeDiff(3).createConstrain();
        vectorSpace = Converters.VectorSpaceFromConstraint(testConstraint);
        Double ageDiff = vectorSpace.getDimensionValue(Attributes.AGE);
        assertTrue(3 - eps <= ageDiff && ageDiff <= 3 + eps);

        //Gender
        testConstraint = new Constraint.Builder(4, 4)
                .setMinFemale(3).createConstrain();
        vectorSpace = Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(0, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));

        testConstraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .createConstrain();
        vectorSpace = Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(1, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));

        //ALERT: every time add an new Attribute, add corresponding test here.

        //Mix
        testConstraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .setTimezoneDiff(2)
                .setAgeDiff(3)
                .createConstrain();
        vectorSpace = Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(5, vectorSpace.getClusterSizeLowerBound());
        assertEquals(6, vectorSpace.getClusterSizeUpperBound());
        assertEquals(1, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"));
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"));
        assertFalse(vectorSpace.containDimension(Attributes.GENDER));
        timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE);
        assertTrue(2 - eps <= timeZoneDiff && timeZoneDiff <= 2 + eps);
        ageDiff = vectorSpace.getDimensionValue(Attributes.AGE);
        assertTrue(3 - eps <= ageDiff && ageDiff <= 3 + eps);


        assertFalse(constraintForSameGender.isTimeMatter());
    }

    @Test
    public void testNodeFromStudentAndConstraint() {
        //time zone
        Double nodeTime = n1.getValueOfDimensionOf(Attributes.TIMEZONE);
        Double studentTime = (double) TimeZoneCalculator.timeZoneInInteger(s1.getTimeZone().orElse(null));
        assertTrue(Math.abs(nodeTime - studentTime) <= eps);

        //age
        Double nodeAge = n1.getValueOfDimensionOf(Attributes.AGE);
        Double studentAge = (double) s1.getAge().orElse(-1);
        assertTrue(Math.abs(nodeAge - studentAge) <= eps);

        //same gender
        Double nodeGenderValue = nodeForSameGender.getValueOfDimensionOf(Attributes.GENDER);
        Double studentGenderValue = s1.getGender().orElse("male").equals("male")? 0.0: 1.0;
        assertFalse(nodeForSameGender.containDimension(Attributes.TIMEZONE));
        assertFalse(nodeForSameGender.containDimension(Attributes.AGE));
        assertTrue(Math.abs(nodeGenderValue - studentGenderValue) <= eps);

        //gender
        assertEquals(n1.getTypeOfDiscreteAttributeOf(Attributes.GENDER), s1.getGender().orElse("male"));
        assertEquals(n3.getTypeOfDiscreteAttributeOf(Attributes.GENDER), s3.getGender().orElse("male"));
    }
}