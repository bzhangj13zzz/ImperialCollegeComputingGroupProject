package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import ic.doc.sgo.TimeZoneCalculator;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

final class Converters {

    private Converters() {
    }

    static VectorSpace VectorSpaceFromConstraint(Constraint constraint) {
        Map<Attributes, VectorSpace.Property> dimensions = new HashMap<>();
        int clusterSizeLowerBound;
        int clusterSizeUpperBound;
        Map<Attributes, HashMap<String, Integer>> discreteAttribute = new HashMap<>();

        if (constraint.isTimeMatter()) {
            dimensions.put(Attributes.TIMEZONE, new VectorSpace.Property(12.0, VectorSpace.Type.CIRCLE, (double) constraint.getTimezoneDiff().getAsInt()));
        }

        if (constraint.isAgeMatter()) {
            dimensions.put(Attributes.AGE, new VectorSpace.Property(120.0, VectorSpace.Type.LINE, (double) constraint.getAgeDiff().getAsInt()));
        }

        if (constraint.isSameGender()) {
            dimensions.put(Attributes.GENDER, new VectorSpace.Property(2.0, VectorSpace.Type.LINE, 0.0));
        }

        discreteAttribute.put(Attributes.GENDER, new HashMap<>());
        discreteAttribute.get(Attributes.GENDER).put("male", 0);
        discreteAttribute.get(Attributes.GENDER).put("female", 0);
        if (constraint.isGenderMatter()) {
            assert constraint.getMinMale() + constraint.getMinFemale() <= constraint.getGroupSizeLowerBound();
            discreteAttribute.get(Attributes.GENDER).put("male", constraint.getMinMale());
            discreteAttribute.get(Attributes.GENDER).put("female", constraint.getMinFemale());
        }

        clusterSizeLowerBound = constraint.getGroupSizeLowerBound();
        clusterSizeUpperBound = constraint.getGroupSizeUpperBound();
        return new VectorSpace(dimensions, clusterSizeLowerBound, clusterSizeUpperBound, discreteAttribute);
    }

    static Node NodeFromStudentAndConstraint(Student student, Constraint constraint) {

        String id;
        Map<Attributes, Double> coordinateMap = new HashMap<>();
        Map<Attributes, String> discreteAttributeType = new HashMap<>();

        id = student.getId();

        if (constraint.isTimeMatter()) {
            coordinateMap.put(Attributes.TIMEZONE,
                    (double) TimeZoneCalculator.timeZoneInInteger(student.getTimeZone().orElse(ZoneId.of("UTC+0"))));
        }

        if (constraint.isAgeMatter()) {
            coordinateMap.put(Attributes.AGE, (double) student.getAge().orElse(0));
        }

        if (constraint.isSameGender()) {
            coordinateMap.put(Attributes.GENDER, (double) genderToInteger(student.getGender().orElse("male")));
        }

        discreteAttributeType.put(Attributes.GENDER, "male");
        if (constraint.isGenderMatter()) {
            discreteAttributeType.put(Attributes.GENDER, student.getGender().orElse("male"));
        }

        return new Node(id, coordinateMap, discreteAttributeType);
    }

    private static int genderToInteger(String gender) {
        return gender.equals("male")? 0: 1;
    }

}
