package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VectorSpace {

    private final Map<String, Property> dimensions;
    private final int clusterSizeLowerBound;
    private final int clusterSizeUpperBound;
    private final Map<String, HashMap<String, Integer>> discreteAttribute;

    private final double eps = 0.00001;

    public VectorSpace(Map<String, Property> dimensions, int clusterSizeLowerBound, int clusterSizeUpperBound,
                       Map<String, HashMap<String, Integer>> discreteAttribute) {
        this.dimensions = dimensions;
        this.clusterSizeLowerBound = clusterSizeLowerBound;
        this.clusterSizeUpperBound = clusterSizeUpperBound;
        this.discreteAttribute = discreteAttribute;
    }

    public int getClusterSizeLowerBound() {
        return clusterSizeLowerBound;
    }

    public int getClusterSizeUpperBound() {
        return clusterSizeUpperBound;
    }

    public int getDiscreteAttributeValue(String attribute, String type) {
        assert discreteAttribute.containsKey(attribute);
        return discreteAttribute.get(attribute).get(type);
    }

    boolean isBetterFitIfSwap(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();


        Boolean previousValidC1 = isValidCluster(c1);
        Boolean previousValidC2 = isValidCluster(c2);
        int previousValidNumber = booleanToInt(previousValidC1) + booleanToInt(previousValidC2);
        double pv1 = evaluateCluster(c1);
        double pv2 = evaluateCluster(c2);

        Node.swapCluster(n1, n2);
        Boolean currentValidC1 = isValidCluster(c1);
        Boolean currentValidC2 = isValidCluster(c2);
        int currentValidNumber = booleanToInt(currentValidC1) + booleanToInt(currentValidC2);
        double cv1 = evaluateCluster(c1);
        double cv2 = evaluateCluster(c2);

        Node.swapCluster(n1, n2);
        c1.setCurrentValue(pv1);
        c2.setCurrentValue(pv2);
        c1.setTargetValue(cv1);
        c2.setTargetValue(cv2);
        c1.setValid(previousValidC1);
        c2.setValid(previousValidC2);
        c1.setTargetValid(currentValidC1);
        c2.setTargetValid(currentValidC2);


        if (currentValidNumber > previousValidNumber) {
            return true;
        }
        if (currentValidNumber < previousValidNumber) {
            return false;
        }
        return cv1 + cv2 + eps < pv1 + pv2;
    }

    // get average distance to center node
    private double evaluateCluster(Cluster cluster) {
        if (cluster.getCurrentValue() != null) {
            return cluster.getCurrentValue();
        }

        Node centerNode = getCenterNode(cluster);
        Double sum = 0.0;
        for (Node node : cluster.getNodes()) {
            sum += getDistanceBetween(node, centerNode);
        }

        int cnt = 0;
        for (String String: discreteAttribute.keySet()) {
            for (String type: discreteAttribute.get(String).keySet()) {
                int target = getDiscreteAttributeValue(String, type);
                int number = getDiscreteAttributeNumberInCluster(cluster, String, type);
                sum += Math.min(target - number, 0);
                cnt++;
            }
        }

        return  sum/ (cluster.size() + cnt);
    }

    private int getDiscreteAttributeNumberInCluster(Cluster cluster, String String, String type) {
        int res=0;
        for (Node node: cluster.getNodes()) {
            if (node.getTypeOfDiscreteAttributeOf(String).equals(type)) {
                res++;
            }
        }
        return res;
    }

    double getDistanceBetween(Node node, Node centerNode) {
        double sum = 0.0;
        for (String dimension : node.getDimensions()) {
            double value = Math.abs(node.getValueOfDimensionOf(dimension) - centerNode.getValueOfDimensionOf(dimension));
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum);
    }

    Node getCenterNode(Cluster cluster) {
        Node res = new Node();
        for (Node node : cluster.getNodes()) {
            for (String dimension : node.getDimensions()) {
                double lastValue = res.getValueOfDimensionOf(dimension);
                res.putValueOfDimension(dimension, lastValue + node.getValueOfDimensionOf(dimension));
            }
        }

        for (String dimension : res.getDimensions()) {
            double lastValue = res.getValueOfDimensionOf(dimension);
            res.putValueOfDimension(dimension, lastValue / cluster.size());
        }
        return res;
    }

    public Boolean isValidCluster(Cluster cluster) {
        if (cluster.getValid() != null) {
            return cluster.getValid();
        }

        if (cluster.size() < clusterSizeLowerBound || cluster.size() > clusterSizeUpperBound) {
            return false;
        }

        for (String attributeName : dimensions.keySet()) {
            Property property = dimensions.get(attributeName);
            if (property.getValidDifference() < getBiggestDifferenceInClusterOf(attributeName, cluster)) {
                return false;
            }
        }

        for (String attributeName : discreteAttribute.keySet()) {
            HashMap<String, Integer> valueMap = discreteAttribute.get(attributeName);
            for (String type : valueMap.keySet()) {
                if (valueMap.get(type) > cluster.getNumberOf(attributeName, type)) {
                    return false;
                }
            }
        }
        return true;
    }

    Double getBiggestDifferenceInClusterOf(String attributeName, Cluster cluster) {
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
        Double pv1 = c1.getCurrentValue();
        Double pv2 = c2.getCurrentValue();

        Boolean previousValidC1 = isValidCluster(c1);
        Boolean previousValidC2 = isValidCluster(c2);

        int previousValidNumber = booleanToInt(previousValidC1) + booleanToInt(previousValidC2);
        c2.add(n);

        Boolean currentValidC1 = isValidCluster(c1);
        Boolean currentValidC2 = isValidCluster(c2);
        int currentValidNumber = booleanToInt(currentValidC1) + booleanToInt(currentValidC2);

        c1.add(n);
        c1.setCurrentValue(pv1);
        c2.setCurrentValue(pv2);
        c1.setValid(previousValidC1);
        c2.setValid(previousValidC2);
        c1.setTargetValid(currentValidC1);
        c2.setTargetValid(currentValidC2);

        return currentValidNumber > previousValidNumber;
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
                    cluster.setCurrentValue(cluster.getTargetValue());
                    cluster.setValid(cluster.getTargetValid());
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
        Double p = evaluateCluster(cluster);
        Boolean previousValid = isValidCluster(cluster);
        int v1 = booleanToInt(previousValid);

        cluster.remove(node);
        double q = evaluateCluster(cluster);
        Boolean currentValid = isValidCluster(cluster);
        int v2 = booleanToInt(currentValid);

        cluster.add(node);
        cluster.setCurrentValue(p);
        cluster.setTargetValue(q);
        cluster.setValid(previousValid);
        cluster.setTargetValid(currentValid);

        if (v2 > v1) {
            return true;
        }

        return q < p;
    }

    private int booleanToInt(Boolean value) {
        return value ? 1 : 0;
    }

    // Using for add nodes to other cluster from unallocated cluster
    boolean canBeFit(Node node, Cluster c1) {
        Cluster c2 = node.getCluster();
        Double pv1 = c1.getCurrentValue();
        Boolean previousValidC1 = isValidCluster(c1);

        c1.add(node);
        Boolean currentValidC1 = isValidCluster(c1);

        c2.add(node);
        c1.setCurrentValue(pv1);
        c1.setValid(previousValidC1);
        c1.setTargetValid(currentValidC1);

        return currentValidC1;
    }

    public Double getDimensionValue(String key) {
        assert dimensions.containsKey(key);
        return dimensions.get(key).getValidDifference();
    }

    Property getPropertyOfDimension(String attribute) {
        assert dimensions.containsKey(attribute);
        return dimensions.get(attribute);
    }

    public boolean containDimension(String attribute) {
        return dimensions.containsKey(attribute);
    }

    public boolean isNoConstraint() {
        return dimensions.isEmpty();
    }




    public static class Property {
        private final Double limit;
        private final Type type;
        private final Double validDifference;

        public Property(Double limit, Type type, Double validDifference) {
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

    public enum Type {
        LINE,
        CIRCLE;
    }
}
