package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.*;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private Map<Attributes, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;
    private Map<Attributes, String> discreteAttributeType = new HashMap<>();

    public Node() {}

    private Node(Student student, Constraint constraint) {
        this.id = student.getId();

        if (constraint.getTimezoneDiff().isPresent()) {
            coordinateMap.put(Attributes.TIMEZONE, getTimeZoneInteger(student));
        }

        if (constraint.getAgeDiff().isPresent()) {
            coordinateMap.put(Attributes.AGE, (double) student.getAge().orElse(0));
        }

        discreteAttributeType.put(Attributes.GENDER,  "male");
        if (constraint.isGenderMatter()) {
            discreteAttributeType.put(Attributes.GENDER,  student.getGender().orElse("male"));
        }
    }

    public Node(String id, Map<Attributes, Double> coordinateMap, Map<Attributes, String> discreteAttributeType) {
        this.id = id;
        this.coordinateMap = coordinateMap;
        this.discreteAttributeType =discreteAttributeType;
    }

    public static Node fromNode(Node node) {
        return new Node(node.getId(), node.getCoordinateMap(), node.getDiscreteAttributeType());
    }

    public Map<Attributes, Double> getCoordinateMap() {
        return coordinateMap;
    }

    public Map<Attributes, String> getDiscreteAttributeType() {
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
        assert discreteAttributeType.containsKey(Attributes.GENDER);
        return this.discreteAttributeType.get(Attributes.GENDER);
    }

    public Double getValueOfDimensionOf(Attributes dimensionName) {
        if (!coordinateMap.containsKey(dimensionName)) {
            putValueOfDimension(dimensionName, 0.0);
        }
        return coordinateMap.get(dimensionName);
    }

    public boolean isTypeOfAttribute(Attributes attribute, String type) {
        return discreteAttributeType.get(attribute).equals(type);
    }

    public Iterable<Attributes> getDimensions() {
        return this.discreteAttributeType.keySet();
    }

    public void putValueOfDimension(Attributes dimension, Double value) {
        this.coordinateMap.put(dimension, value);
    }
}
