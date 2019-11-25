package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Attributes;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import ic.doc.sgo.TimeZoneCalculator;
import ic.doc.sgo.groupingstrategies.FixedPointStrategy;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.Cluster;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.Node;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.VectorSpace;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

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

    private Constraint constraintForAdditionalDiscreteAttribute = new Constraint.Builder(3, 4)
            .setAdditionalDiscreteAttribute(new HashMap<String, Integer>() {{
                put("quant", 2);
            }})
            .createConstrain();

    private VectorSpace vectorSpaceForSameGender = FixedPointStrategy.Converters.VectorSpaceFromConstraint(constraintForSameGender);

    private VectorSpace vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(constraint);

    private VectorSpace vectorSpaceForAdditionalAttribute = FixedPointStrategy.Converters
            .VectorSpaceFromConstraint(constraintForAdditionalDiscreteAttribute);

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

    private Student s8 = new Student.Builder("8")
            .setAge(5)
            .setAdditionalDiscreteAttributeWithType("quant", "true")
            .createStudent();

    private Student s9 = new Student.Builder("9")
            .setAge(5)
            .setAdditionalDiscreteAttributeWithType("quant", "true")
            .createStudent();

    private Student s10 = new Student.Builder("10")
            .setAge(5)
            .setAdditionalDiscreteAttributeWithType("quant", "true")
            .createStudent();

    private Node n1 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s1, constraint);
    private Node n2 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s2, constraint);
    private Node n3 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s3, constraint);
    private Node n4 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s4, constraint);
    private Node n5 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s5, constraint);
    private Node n6 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s6, constraint);
    private Node n7 = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s7, constraint);

    private Node n8 = FixedPointStrategy.Converters
            .NodeFromStudentAndConstraint(s8, constraintForAdditionalDiscreteAttribute);
    private Node n9 = FixedPointStrategy.Converters
            .NodeFromStudentAndConstraint(s1, constraintForAdditionalDiscreteAttribute);

    private Node nodeForSameGender = FixedPointStrategy.Converters.NodeFromStudentAndConstraint(s1, constraintForSameGender);

    private final Cluster c1 = Cluster.from(new ArrayList<>());
    private final Cluster c2 = Cluster.from(new ArrayList<>());


    @Test
    public void vectorSpaceFromConstraint() {
        //Timezone
        Constraint testConstraint = new Constraint.Builder(4, 5)
                .setTimezoneDiff(2).createConstrain();
        assertTrue(testConstraint.getTimezoneDiff().isPresent());
        VectorSpace vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(4, vectorSpace.getClusterSizeLowerBound());
        assertEquals(5, vectorSpace.getClusterSizeUpperBound());
        Double timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE.getName());
        assertTrue(2 - eps <= timeZoneDiff && timeZoneDiff <= 2 + eps);

        //Age
        testConstraint = new Constraint.Builder(4, 4)
                .setAgeDiff(3).createConstrain();
        vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        Double ageDiff = vectorSpace.getDimensionValue(Attributes.AGE.getName());
        assertTrue(3 - eps <= ageDiff && ageDiff <= 3 + eps);

        //Gender
        testConstraint = new Constraint.Builder(4, 4)
                .setMinFemale(3).createConstrain();
        vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(3, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER.getName(), "female"));
        assertEquals(0, vectorSpace.getDiscreteAttributeValue(Attributes.GENDER.getName(), "male"));

        testConstraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .createConstrain();
        vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(0.4, vectorSpace.getRatioAttributeValue(Attributes.GENDER.getName(), "female").first(), 0.0);
        assertEquals(0.6, vectorSpace.getRatioAttributeValue(Attributes.GENDER.getName(), "male").first(), 0.0);
        assertEquals(0.1, vectorSpace.getRatioAttributeValue(Attributes.GENDER.getName(), "female").second(), 0.0);
        assertEquals(0.1, vectorSpace.getRatioAttributeValue(Attributes.GENDER.getName(), "male").second(), 0.0);
        //ALERT: every time add an new Attribute, add corresponding test here.

        //Additional Attribute
        testConstraint = new Constraint.Builder(3, 4)
                .setAdditionalDiscreteAttribute(new HashMap<String, Integer>() {{
                    put("quant", 2);
                }})
                .createConstrain();
        vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(2, vectorSpace.getDiscreteAttributeValue("quant", "true"));

        //Mix
        testConstraint = new Constraint.Builder(5, 6)
                .setGenderRatio(0.6)
                .setGenderErrorMargin(0.1)
                .setTimezoneDiff(2)
                .setAgeDiff(3)
                .createConstrain();
        vectorSpace = FixedPointStrategy.Converters.VectorSpaceFromConstraint(testConstraint);
        assertEquals(5, vectorSpace.getClusterSizeLowerBound());
        assertEquals(6, vectorSpace.getClusterSizeUpperBound());
        assertFalse(vectorSpace.containDimension(Attributes.GENDER.getName()));
        timeZoneDiff = vectorSpace.getDimensionValue(Attributes.TIMEZONE.getName());
        assertTrue(2 - eps <= timeZoneDiff && timeZoneDiff <= 2 + eps);
        ageDiff = vectorSpace.getDimensionValue(Attributes.AGE.getName());
        assertTrue(3 - eps <= ageDiff && ageDiff <= 3 + eps);


        assertFalse(constraintForSameGender.isTimeMatter());


    }

    @Test
    public void testNodeFromStudentAndConstraint() {
        //time zone
        Double nodeTime = n1.getValueOfDimensionOf(Attributes.TIMEZONE.getName());
        Double studentTime = (double) TimeZoneCalculator.timeZoneInInteger(s1.getTimeZone().orElse(null));
        assertTrue(Math.abs(nodeTime - studentTime) <= eps);

        //age
        Double nodeAge = n1.getValueOfDimensionOf(Attributes.AGE.getName());
        Double studentAge = (double) s1.getAge().orElse(-1);
        assertTrue(Math.abs(nodeAge - studentAge) <= eps);

        //same gender
        Double nodeGenderValue = nodeForSameGender.getValueOfDimensionOf(Attributes.GENDER.getName());
        Double studentGenderValue = s1.getGender().orElse("male").equals("male")? 0.0: 1.0;
        assertFalse(nodeForSameGender.containDimension(Attributes.TIMEZONE.getName()));
        assertFalse(nodeForSameGender.containDimension(Attributes.AGE.getName()));
        assertTrue(Math.abs(nodeGenderValue - studentGenderValue) <= eps);

        //gender
        assertEquals(n1.getTypeOfDiscreteAttributeOf(Attributes.GENDER.getName()), s1.getGender().orElse("male"));
        assertEquals(n3.getTypeOfDiscreteAttributeOf(Attributes.GENDER.getName()), s3.getGender().orElse("male"));

        //additional attribute
        assertEquals(n8.getTypeOfDiscreteAttributeOf("quant"), "true");
        assertEquals(n8.getTypeOfDiscreteAttributeOf("quant"), s8.getAttribute("quant").orElse(""));
        assertEquals(n9.getTypeOfDiscreteAttributeOf("quant"), "");
        assertEquals(n9.getTypeOfDiscreteAttributeOf("quant"), s1.getAttribute("quant").orElse(""));
    }
}