package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.*;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private final Map<String, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;
    private final Map<String, String> discreteAttributeType = new HashMap<>();

    public Node() {}

    private Node(Student student, Constraint constraint) {
        this.id = student.getId();

        if (constraint.getTimezoneDiff().isPresent()) {
            coordinateMap.put("timeZone", getTimeZoneInteger(student));
        }

        if (constraint.getAgeDiff().isPresent()) {
            coordinateMap.put("Age", (double) student.getAge().orElse(0));
        }

        if (constraint.isGenderMatter()) {
            discreteAttributeType.put("gender",  student.getGender().orElse("male"));
        }
    }

    private double getTimeZoneInteger(Student student) {
        if (student.getTimeZone().isPresent()) {
            return (double) TimeZoneCalculator.timeZoneInInteger(student.getTimeZone().get());
        }
        return 0.0;
    }

    public static Node createFromStudentWithConstraint(Student student, Constraint constraint) {
        return new Node(student, constraint);
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getId() {
        return this.id;
    }

    public String getGender() {
        return this.discreteAttributeType.get("gender");
    }

    public Double getValueOfDimensionOf(String dimensionName) {
        if (!coordinateMap.containsKey(dimensionName)) {
            putValueOfDimension(dimensionName, 0.0);
        }
        return coordinateMap.get(dimensionName);
    }

    public boolean isTypeOfAttribute(String attribute, String type) {
        return discreteAttributeType.get(attribute).equals(type);
    }

    public Iterable<String> getDimensions() {
        return this.discreteAttributeType.keySet();
    }

    public void putValueOfDimension(String dimension, Double value) {
        this.coordinateMap.put(dimension, value);
    }
}
