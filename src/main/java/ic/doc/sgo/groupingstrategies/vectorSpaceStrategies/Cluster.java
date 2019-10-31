package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cluster {
    private final List<Node> nodes;
    private int id;

    public Cluster(List<Node> nodes) {
        this.nodes = nodes;
    }

    public  Cluster(int id, ArrayList<Node> nodes) {
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

    public boolean add(Node member) {
        if (!nodes.contains(member)) {
            Cluster origin = member.getCluster();
            if (origin != null) {
                origin.remove(member);
            }
            nodes.add(member);
            member.setCluster(this);
            return true;
        }
        return true;
    }

    public boolean remove(Node member) {
        if (nodes.contains(member)) {
            nodes.remove(member);
            member.setCluster(null);
            return true;
        }
        return false;
    }

    public void addAll(List<Node> nodes) {
        List<Node> nodeList = new ArrayList<>(nodes);
        nodeList.forEach(this::add);
    }

    public Integer getNumberOf(Attributes attribute, String type) {
        int res = 0;
        for (Node node: nodes) {
            if (node.isTypeOfAttribute(attribute, type)) {
                res++;
            }
        }
        return res;
    }

    public void clear() {
        nodes.clear();
    }

    public boolean contains(Node n1) {
        return nodes.contains(n1);
    }
}
