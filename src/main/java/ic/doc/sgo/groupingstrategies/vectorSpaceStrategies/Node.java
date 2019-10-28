package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.*;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private Map<String, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;
    private Map<String, String> discreteAttributeType = new HashMap<>();

    public Node() {}

    private Node(Student student, Constraint constraint) {
        this.id = student.getId();

        if (constraint.getTimezoneDiff().isPresent()) {
            coordinateMap.put("timeZone", getTimeZoneInteger(student));
        }

        if (constraint.getAgeDiff().isPresent()) {
            coordinateMap.put("age", (double) student.getAge().orElse(0));
        }

        if (constraint.isGenderMatter()) {
            discreteAttributeType.put("gender",  student.getGender().orElse("male"));
        }
    }

    public Node(String id, Map<String, Double> coordinateMap, Map<String, String> discreteAttributeType) {
        this.id = id;
        this.coordinateMap = coordinateMap;
        this.discreteAttributeType =discreteAttributeType;
    }

    public static Node fromNode(Node node) {
        return new Node(node.getId(), node.getCoordinateMap(), node.getDiscreteAttributeType());
    }

    public Map<String, Double> getCoordinateMap() {
        return coordinateMap;
    }

    public Map<String, String> getDiscreteAttributeType() {
        return discreteAttributeType;
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
