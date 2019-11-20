package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private Map<String, Double> coordinateMap = new HashMap<>();
    private Cluster cluster;
    private Map<String, String> discreteAttributeType = new HashMap<>();

    Node() {
    }

    public Node(String id, Map<String, Double> coordinateMap, Map<String, String> discreteAttributeType) {
        this.id = id;
        this.coordinateMap = coordinateMap;
        this.discreteAttributeType = discreteAttributeType;
    }

    static Node fromNode(Node node) {
        return new Node(node.getId(), node.getCoordinateMap(), node.getDiscreteAttributeType());
    }

    Map<String, Double> getCoordinateMap() {
        return coordinateMap;
    }

    Map<String, String> getDiscreteAttributeType() {
        return discreteAttributeType;
    }

    Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    static void swapCluster(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        c1.add(n2);
        c2.add(n1);
    }

    public String getId() {
        return this.id;
    }

    public String getTypeOfDiscreteAttributeOf(String String) {
        assert discreteAttributeType.containsKey(String);
        return discreteAttributeType.get(String);

    }

    public Double getValueOfDimensionOf(String dimensionName) {
        if (!coordinateMap.containsKey(dimensionName)) {
            putValueOfDimension(dimensionName, 0.0);
        }
        return coordinateMap.get(dimensionName);
    }

    boolean isTypeOfAttribute(String attribute, String type) {
        return discreteAttributeType.get(attribute).equals(type);
    }

    Iterable<String> getDimensions() {
        return this.coordinateMap.keySet();
    }

    void putValueOfDimension(String dimension, Double value) {
        this.coordinateMap.put(dimension, value);
    }

    public boolean containDimension(String attribute) {
        return coordinateMap.containsKey(attribute);
    }
}
