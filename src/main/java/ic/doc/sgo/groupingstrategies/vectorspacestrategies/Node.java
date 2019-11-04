package ic.doc.sgo.groupingstrategies.vectorspacestrategies;

import ic.doc.sgo.Attributes;

import java.util.HashMap;
import java.util.Map;

class Node {

    private String id;
    private Map<Attributes, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;
    private Map<Attributes, String> discreteAttributeType = new HashMap<>();

    Node() {}

    public Node(String id, Map<Attributes, Double> coordinateMap, Cluster cluster, Map<Attributes, String> discreteAttributeType) {
        this.id = id;
        this.coordinateMap = coordinateMap;
        this.cluster = cluster;
        this.discreteAttributeType = discreteAttributeType;
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

    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public static void swapCluster(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        c1.add(n2);
        c2.add(n1);
    }

    public String getId() {
        return this.id;
    }

    public String getTypeOfDiscreteAttributeOf(Attributes attributes) {
        assert discreteAttributeType.containsKey(attributes);
        return discreteAttributeType.get(attributes);

    }

    public String getGender() {
        return getTypeOfDiscreteAttributeOf(Attributes.GENDER);
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
        return this.coordinateMap.keySet();
    }

    public void putValueOfDimension(Attributes dimension, Double value) {
        this.coordinateMap.put(dimension, value);
    }
}
