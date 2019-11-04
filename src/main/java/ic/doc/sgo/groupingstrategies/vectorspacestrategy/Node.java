package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.HashMap;
import java.util.Map;

class Node {

    private String id;
    private Map<Attributes, Double> coordinateMap = new HashMap<>();
    private Cluster cluster;
    private Map<Attributes, String> discreteAttributeType = new HashMap<>();

    Node() {
    }

    Node(String id, Map<Attributes, Double> coordinateMap, Map<Attributes, String> discreteAttributeType) {
        this.id = id;
        this.coordinateMap = coordinateMap;
        this.discreteAttributeType = discreteAttributeType;
    }

    static Node fromNode(Node node) {
        return new Node(node.getId(), node.getCoordinateMap(), node.getDiscreteAttributeType());
    }

    Map<Attributes, Double> getCoordinateMap() {
        return coordinateMap;
    }

    Map<Attributes, String> getDiscreteAttributeType() {
        return discreteAttributeType;
    }

    Cluster getCluster() {
        return this.cluster;
    }

    void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    static void swapCluster(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        c1.add(n2);
        c2.add(n1);
    }

    String getId() {
        return this.id;
    }

    String getTypeOfDiscreteAttributeOf(Attributes attributes) {
        assert discreteAttributeType.containsKey(attributes);
        return discreteAttributeType.get(attributes);

    }

    String getGender() {
        return getTypeOfDiscreteAttributeOf(Attributes.GENDER);
    }

    Double getValueOfDimensionOf(Attributes dimensionName) {
        if (!coordinateMap.containsKey(dimensionName)) {
            putValueOfDimension(dimensionName, 0.0);
        }
        return coordinateMap.get(dimensionName);
    }

    boolean isTypeOfAttribute(Attributes attribute, String type) {
        return discreteAttributeType.get(attribute).equals(type);
    }

    Iterable<Attributes> getDimensions() {
        return this.coordinateMap.keySet();
    }

    void putValueOfDimension(Attributes dimension, Double value) {
        this.coordinateMap.put(dimension, value);
    }
}
