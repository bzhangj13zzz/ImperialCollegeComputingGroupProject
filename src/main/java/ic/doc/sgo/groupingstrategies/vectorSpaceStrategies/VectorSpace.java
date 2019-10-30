package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Attributes;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.groupingstrategies.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VectorSpace {

    private final Map<Attributes, Property> dimensions = new HashMap<>();
    private final int clusterSizeLowerBound;
    private final int clusterSizeUpperBound;
    private final Map<Attributes, HashMap<String, Integer>> discreteAttribute = new HashMap<>();

    public VectorSpace(Constraint constraint) {
        if (constraint.getTimezoneDiff().isPresent()) {
            dimensions.put(Attributes.TIMEZONE, new Property(12.0, Type.CIRCLE, (double) constraint.getTimezoneDiff().getAsInt()));
        }

        if (constraint.getAgeDiff().isPresent()) {
            dimensions.put(Attributes.AGE, new Property(120.0, Type.LINE, (double) constraint.getAgeDiff().getAsInt()));
        }

        discreteAttribute.put(Attributes.GENDER, new HashMap<>());
        discreteAttribute.get(Attributes.GENDER).put("male", 0);
        discreteAttribute.get(Attributes.GENDER).put("female", 0);
        if (constraint.isGenderMatter()) {
            assert constraint.getMinMale() + constraint.getMinFemale() <= constraint.getGroupSizeLowerBound();
            discreteAttribute.get(Attributes.GENDER).put("male", constraint.getMinMale());
            discreteAttribute.get(Attributes.GENDER).put("female", constraint.getMinFemale());
        }

        this.clusterSizeLowerBound = constraint.getGroupSizeLowerBound();
        this.clusterSizeUpperBound = constraint.getGroupSizeUpperBound();
    }

    public int getClusterSizeLowerBound() {
        return clusterSizeLowerBound;
    }

    public int getClusterSizeUpperBound() {
        return clusterSizeUpperBound;
    }

    public int getDiscreteAttributeValue(Attributes attribute, String type) {
        assert discreteAttribute.containsKey(attribute);
        return discreteAttribute.get(attribute).get(type);
    }

    public boolean isBetterFitIfSwap(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        int v1 = Util.booleanToInt(isValidCluster(c1)) + Util.booleanToInt(isValidCluster(c2));
        double pv1 = evaluateCluster(c1);
        double pv2 = evaluateCluster(c2);

        Util.swapCluster(n1, n2);
        int v2 =  Util.booleanToInt(isValidCluster(c1)) + Util.booleanToInt(isValidCluster(c2));
        double cv1 = evaluateCluster(c1);
        double cv2 = evaluateCluster(c2);;
        Util.swapCluster(n1, n2);

        if (v2 > v1) {
            return true;
        }
        if (v2 < v1) {
            return false;
        }
        return cv1 + cv2 < pv1 + pv2;
    }

    // get average distance to center node
    private double evaluateCluster(Cluster cluster) {
        Node centerNode = getCenterNode(cluster);
        double sum = 0.0;
        for (Node node: cluster.getNodes()) {
            sum += getDistanceBetween(node, centerNode);
        }
        return sum/cluster.size();
    }

    public double getDistanceBetween(Node node, Node centerNode) {
        double sum = 0.0;
        for (Attributes dimension: node.getDimensions()) {
            double value = Math.abs(node.getValueOfDimensionOf(dimension) - centerNode.getValueOfDimensionOf(dimension));
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum);
    }

    public Node getCenterNode(Cluster cluster) {
        Node res = new Node();
        for (Node node: cluster.getNodes()) {
            for (Attributes dimension: node.getDimensions()) {
                double lastValue = res.getValueOfDimensionOf(dimension);
                res.putValueOfDimension(dimension, lastValue + node.getValueOfDimensionOf(dimension));
            }
        }

        for (Attributes dimension: res.getDimensions()) {
            double lastValue = res.getValueOfDimensionOf(dimension);
            res.putValueOfDimension(dimension, lastValue/cluster.size());
        }
        return res;
    }

    public boolean isValidCluster(Cluster cluster) {
        if (cluster.size() < clusterSizeLowerBound || cluster.size() > clusterSizeUpperBound) {
            return false;
        }

        for (Attributes attributeName: dimensions.keySet()) {
            Property property = dimensions.get(attributeName);
            if (property.getValidDifference() < cluster.getBiggestDifferenceOf(attributeName, property)) {
                return false;
            }
        }

        for (Attributes attributeName: discreteAttribute.keySet()) {
            HashMap<String, Integer> valueMap = discreteAttribute.get(attributeName);
            for (String type: valueMap.keySet()) {
                if (valueMap.get(type) > cluster.getNumberOf(attributeName, type)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canBeBetterFit(Node n, Cluster c2) {
        Cluster c1 = n.getCluster();
        int v1 = Util.booleanToInt(isValidCluster(c1)) + Util.booleanToInt(isValidCluster(c2));
        c2.add(n);
        int v2 = Util.booleanToInt(isValidCluster(c1)) + Util.booleanToInt(isValidCluster(c2));
        c1.add(n);

        return v2 > v1;
    }

    public List<Node> getInvalidStudentsFromGroup(Cluster cluster) {
        List<Node> nodes = new ArrayList<>(cluster.getNodes());
        List<Node> removeStudents = new ArrayList<>();
        for (Node node: nodes) {
            if (isBetterFitIfRemove(node, cluster)) {
                removeStudents.add(node);
                cluster.remove(node);
                if (isValidCluster(cluster)) {
                    return removeStudents;
                }
            }
        }

        return nodes;
    }

    private boolean isBetterFitIfRemove(Node node, Cluster cluster) {
        double p = evaluateCluster(cluster);
        cluster.remove(node);
        double q = evaluateCluster(cluster);
        cluster.add(node);
        return q < p;
    }

    public boolean canBeFit(Node node, Cluster cluster) {
        Cluster originalCluster = node.getCluster();
        cluster.add(node);
        boolean res = isValidCluster(cluster);
        originalCluster.add(node);
        return res;
    }

    public Double getDimensionValue(Attributes key) {
        assert dimensions.containsKey(key);
        return dimensions.get(key).getValidDifference();
    }


    class Property {
        private final Double limit;
        private final Type type;
        private final Double validDifference;

        public Property(Double limit, Type type, Double validDifference) {
            this.limit = limit;
            this.type = type;
            this.validDifference = validDifference;
        }

        public Double getLimit() {
            return limit;
        }

        public Type getType() {
            return type;
        }

        public double getDifferenceBetween(Double v1, Double v2) {
            double res =  Math.abs(v1 - v2);
            if (type == Type.CIRCLE) {
                res = Math.min(res, limit-res);
            }
            return res;
        }

        public Double getValidDifference() {
            return validDifference;
        }
    }

    enum Type {
        LINE,
        CIRCLE;
    }
}
