package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cluster {
    private final List<Node> nodes;
    private int id;
    private  Double currentValue;
    private Double targetValue;
    private Boolean isValid;
    private Boolean targetValid;

    Cluster(List<Node> nodes) {
        this.nodes = nodes;
        for (Node node: nodes) {
            node.setCluster(this);
        }
    }

    Cluster(int id, ArrayList<Node> nodes) {
        this.nodes = nodes;
        this.id = id;
    }

    public static Cluster of(int id, Node... members) {
        return new Cluster(id, new ArrayList<Node>(Arrays.asList(members)));
    }

    public static Cluster from(List<Node> nodes) {
        return new Cluster(nodes);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int size() {
        return nodes.size();
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public int getId() {
        return id;
    }

    boolean add(Node member) {
        if (!nodes.contains(member)) {
            Cluster origin = member.getCluster();
            if (origin != null) {
                origin.remove(member);
            }
            nodes.add(member);
            member.setCluster(this);
            this.currentValue = null;
            this.isValid = null;
            return true;
        }
        return true;
    }

    boolean remove(Node member) {
        if (nodes.contains(member)) {
            nodes.remove(member);
            member.setCluster(null);
            this.currentValue = null;
            this.isValid = null;
            return true;
        }
        return false;
    }

    public void addAll(List<Node> nodes) {
        List<Node> nodeList = new ArrayList<>(nodes);
        nodeList.forEach(this::add);
    }

    Integer getNumberOf(String attribute, String type) {
        int res = 0;
        for (Node node : nodes) {
            if (node.isTypeOfAttribute(attribute, type)) {
                res++;
            }
        }
        return res;
    }

    void clear() {
        nodes.clear();
        this.currentValue = null;
        this.isValid = null;
    }

    boolean contains(Node n1) {
        return nodes.contains(n1);
    }

    public Double getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(Double v) {
        this.currentValue = v;
    }


    public void setTargetValue(double v) {
        this.targetValue = v;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public void setTargetValid(Boolean valid) {
        this.targetValid = valid;
    }

    public Boolean getTargetValid() {
        return this.targetValid;
    }
}
