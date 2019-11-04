package ic.doc.sgo.groupingstrategies.vectorspacestrategies;

import ic.doc.sgo.Attributes;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Student;
import ic.doc.sgo.TimeZoneCalculator;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

final class Converters {

    private Converters() {}

    static VectorSpace VectorSpaceFromConstraint(Constraint constraint) {
        Map<Attributes, VectorSpace.Property> dimensions = new HashMap<>();
        int clusterSizeLowerBound;
        int clusterSizeUpperBound;
        Map<Attributes, HashMap<String, Integer>> discreteAttribute = new HashMap<>();

        if (constraint.getTimezoneDiff().isPresent()) {
            dimensions.put(Attributes.TIMEZONE, new VectorSpace.Property(12.0, VectorSpace.Type.CIRCLE, (double) constraint.getTimezoneDiff().getAsInt()));
        }

        if (constraint.getAgeDiff().isPresent()) {
            dimensions.put(Attributes.AGE, new VectorSpace.Property(120.0, VectorSpace.Type.LINE, (double) constraint.getAgeDiff().getAsInt()));
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
        Map<Attributes, Double>  coordinateMap = new HashMap<>();
        Map<Attributes, String> discreteAttributeType = new HashMap<>();

        id = student.getId();

        if (constraint.getTimezoneDiff().isPresent()) {
            coordinateMap.put(Attributes.TIMEZONE,
                    (double) TimeZoneCalculator.timeZoneInInteger(student.getTimeZone().orElse(ZoneId.of("UTC+0"))));
        }

        if (constraint.getAgeDiff().isPresent()) {
            coordinateMap.put(Attributes.AGE, (double) student.getAge().orElse(0));
        }

        discreteAttributeType.put(Attributes.GENDER,  "male");
        if (constraint.isGenderMatter()) {
            discreteAttributeType.put(Attributes.GENDER,  student.getGender().orElse("male"));
        }

        return new Node(id, coordinateMap, discreteAttributeType);
    }

}
