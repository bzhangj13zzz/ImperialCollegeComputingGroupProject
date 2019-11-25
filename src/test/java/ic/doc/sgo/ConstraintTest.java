package ic.doc.sgo;

import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


//TODO: need to be tested
public class ConstraintTest {

    private Constraint constraint = new Constraint.Builder(3, 4)
            .setMinFemale(1)
            .setMinMale(2)
            .setTimezoneDiff(2)
            .setAgeDiff(3)
            .createConstrain();
    private Constraint constraintForSameGender = new Constraint.Builder(3, 4)
            .setIsSameGender()
            .createConstrain();
    private Constraint constraintForAdditionalDiscreteAttribute = new Constraint.Builder(3, 4)
            .setAdditionalDiscreteAttribute(new HashMap<String, Integer>() {{
                put("quant", 2);
            }})
            .createConstrain();


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

    private Group g1 = Group.from(new ArrayList<>());

    @Test
    public void testIsValidGroup() {
        // test lower bound
        g1.add(s1);
        assertFalse(constraint.isValidGroup(g1));

        // test upper bound
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s3, s4, s5)));
        assertFalse(constraint.isValidGroup(g1));


        // test invalid timezone
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s3, s6)));
        Integer difference = constraint.getTimezoneDiffOfGroup(g1);
        assertTrue(6 == difference);
        assertFalse(constraint.isValidGroup(g1));

        // test invalid age
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s3, s7)));
        difference = constraint.getAgeDiffOfGroup(g1);
        assertTrue(7 == difference);
        assertFalse(constraint.isValidGroup(g1));

        // test invalid Gender
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s5)));
        assertFalse(constraint.isValidGroup(g1));

        // test is same gender true
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s5)));
        assertTrue(constraint.isGroupSameGender(g1));
        assertTrue(constraintForSameGender.isValidGroup(g1));

        // test is same gender false
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s3)));
        assertFalse(constraintForSameGender.isValidGroup(g1));

        //test valid group size, timezone, age, gender
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s3)));
        assertTrue(constraint.isValidGroup(g1));

        // test valid additional Discrete Attribute
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s8, s9, s10)));
        assertTrue(constraintForAdditionalDiscreteAttribute.isValidGroup(g1));

        //test invalid additional Discrete Attribute
        g1.clear();
        g1.addAll(new ArrayList<>(Arrays.asList(s1, s2, s8)));
        assertFalse(constraintForAdditionalDiscreteAttribute.isValidGroup(g1));
    }

}