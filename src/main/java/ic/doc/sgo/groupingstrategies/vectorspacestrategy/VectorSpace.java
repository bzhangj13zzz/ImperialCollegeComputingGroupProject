package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class VectorSpace {

    private final Map<Attributes, Property> dimensions;
    private final int clusterSizeLowerBound;
    private final int clusterSizeUpperBound;
    private final Map<Attributes, HashMap<String, Integer>> discreteAttribute;

    private final double eps = 0.00001;

    VectorSpace(Map<Attributes, Property> dimensions, int clusterSizeLowerBound, int clusterSizeUpperBound,
                Map<Attributes, HashMap<String, Integer>> discreteAttribute) {
        this.dimensions = dimensions;
        this.clusterSizeLowerBound = clusterSizeLowerBound;
        this.clusterSizeUpperBound = clusterSizeUpperBound;
        this.discreteAttribute = discreteAttribute;
    }

    int getClusterSizeLowerBound() {
        return clusterSizeLowerBound;
    }

    int getClusterSizeUpperBound() {
        return clusterSizeUpperBound;
    }

    int getDiscreteAttributeValue(Attributes attribute, String type) {
        assert discreteAttribute.containsKey(attribute);
        return discreteAttribute.get(attribute).get(type);
    }

    boolean isBetterFitIfSwap(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        int v1 = booleanToInt(isValidCluster(c1)) + booleanToInt(isValidCluster(c2));
        double pv1 = evaluateCluster(c1);
        double pv2 = evaluateCluster(c2);

        Node.swapCluster(n1, n2);
        int v2 = booleanToInt(isValidCluster(c1)) + booleanToInt(isValidCluster(c2));
        double cv1 = evaluateCluster(c1);
        double cv2 = evaluateCluster(c2);
        ;
        Node.swapCluster(n1, n2);

        if (v2 > v1) {
            return true;
        }
        if (v2 < v1) {
            return false;
        }
        return cv1 + cv2 + eps < pv1 + pv2;
    }

    // get average distance to center node
    private double evaluateCluster(Cluster cluster) {
        Node centerNode = getCenterNode(cluster);
        double sum = 0.0;
        for (Node node : cluster.getNodes()) {
            sum += getDistanceBetween(node, centerNode);
        }
        return sum / cluster.size();
    }

    double getDistanceBetween(Node node, Node centerNode) {
        double sum = 0.0;
        for (Attributes dimension : node.getDimensions()) {
            double value = Math.abs(node.getValueOfDimensionOf(dimension) - centerNode.getValueOfDimensionOf(dimension));
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum);
    }

    Node getCenterNode(Cluster cluster) {
        Node res = new Node();
        for (Node node : cluster.getNodes()) {
            for (Attributes dimension : node.getDimensions()) {
                double lastValue = res.getValueOfDimensionOf(dimension);
                res.putValueOfDimension(dimension, lastValue + node.getValueOfDimensionOf(dimension));
            }
        }

        for (Attributes dimension : res.getDimensions()) {
            double lastValue = res.getValueOfDimensionOf(dimension);
            res.putValueOfDimension(dimension, lastValue / cluster.size());
        }
        return res;
    }

    boolean isValidCluster(Cluster cluster) {
        if (cluster.size() < clusterSizeLowerBound || cluster.size() > clusterSizeUpperBound) {
            return false;
        }

        for (Attributes attributeName : dimensions.keySet()) {
            Property property = dimensions.get(attributeName);
            if (property.getValidDifference() < getBiggestDifferenceInClusterOf(attributeName, cluster)) {
                return false;
            }
        }

        for (Attributes attributeName : discreteAttribute.keySet()) {
            HashMap<String, Integer> valueMap = discreteAttribute.get(attributeName);
            for (String type : valueMap.keySet()) {
                if (valueMap.get(type) > cluster.getNumberOf(attributeName, type)) {
                    return false;
                }
            }
        }
        return true;
    }

    Double getBiggestDifferenceInClusterOf(Attributes attributeName, Cluster cluster) {
        assert dimensions.containsKey(attributeName);
        Property property = dimensions.get(attributeName);
        double res = 0.0;
        for (Node n1 : cluster.getNodes()) {
            for (Node n2 : cluster.getNodes()) {
                res = Math.max(res, property.getDifferenceBetween(n1.getValueOfDimensionOf(attributeName),
                        n2.getValueOfDimensionOf(attributeName)));
            }
        }
        return res;
    }

    boolean canBeBetterFit(Node n, Cluster c2) {
        Cluster c1 = n.getCluster();
        int v1 = booleanToInt(isValidCluster(c1)) + booleanToInt(isValidCluster(c2));
        c2.add(n);
        int v2 = booleanToInt(isValidCluster(c1)) + booleanToInt(isValidCluster(c2));
        c1.add(n);

        return v2 > v1;
    }

    List<Node> getInvalidNodesFromCluster(Cluster cluster) {
        List<Node> removeStudents = new ArrayList<>();
        boolean isChange = true;
        while (isChange) {
            isChange = false;
            List<Node> nodes = new ArrayList<>(cluster.getNodes());
            for (Node node : nodes) {
                if (isBetterFitIfRemove(node, cluster)) {
                    removeStudents.add(node);
                    cluster.remove(node);
                    isChange = true;
                    if (isValidCluster(cluster)) {
                        return removeStudents;
                    }
                }
            }
        }
        List<Node> nodes = new ArrayList<>(cluster.getNodes());
        for (Node node : nodes) {
            cluster.remove(node);
            removeStudents.add(node);
        }

        return removeStudents;
    }

    private boolean isBetterFitIfRemove(Node node, Cluster cluster) {
        assert cluster.contains(node);
        double p = evaluateCluster(cluster);
        int v1 = booleanToInt(isValidCluster(cluster));

        cluster.remove(node);
        double q = evaluateCluster(cluster);
        int v2 = booleanToInt(isValidCluster(cluster));

        cluster.add(node);

        if (v2 > v1) {
            return true;
        }

        return q < p;
    }

    private int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }

    boolean canBeFit(Node node, Cluster cluster) {
        Cluster originalCluster = node.getCluster();
        cluster.add(node);
        boolean res = isValidCluster(cluster);
        originalCluster.add(node);
        return res;
    }

    Double getDimensionValue(Attributes key) {
        assert dimensions.containsKey(key);
        return dimensions.get(key).getValidDifference();
    }

    Property getPropertyOfDimension(Attributes attribute) {
        assert dimensions.containsKey(attribute);
        return dimensions.get(attribute);
    }


    static class Property {
        private final Double limit;
        private final Type type;
        private final Double validDifference;

        Property(Double limit, Type type, Double validDifference) {
            this.limit = limit;
            this.type = type;
            this.validDifference = validDifference;
        }

        Double getLimit() {
            return limit;
        }

        Type getType() {
            return type;
        }

        double getDifferenceBetween(Double v1, Double v2) {
            double res = Math.abs(v1 - v2);
            if (type == Type.CIRCLE) {
                res = Math.min(res, limit * 2 - res);
            }
            return res;
        }

        Double getValidDifference() {
            return validDifference;
        }
    }

    enum Type {
        LINE,
        CIRCLE;
    }
}
