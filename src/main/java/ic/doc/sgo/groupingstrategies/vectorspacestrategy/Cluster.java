package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Cluster {
    private final List<Node> nodes;
    private int id;

    Cluster(List<Node> nodes) {
        this.nodes = nodes;
    }

    Cluster(int id, ArrayList<Node> nodes) {
        this.nodes = nodes;
        this.id = id;
    }

    static Cluster of(int id, Node... members) {
        return new Cluster(id, new ArrayList<Node>(Arrays.asList(members)));
    }

    static Cluster from(List<Node> nodes) {
        return new Cluster(nodes);
    }

    void setId(int id) {
        this.id = id;
    }

    int size() {
        return nodes.size();
    }

    List<Node> getNodes() {
        return this.nodes;
    }

    int getId() {
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
            return true;
        }
        return true;
    }

    boolean remove(Node member) {
        if (nodes.contains(member)) {
            nodes.remove(member);
            member.setCluster(null);
            return true;
        }
        return false;
    }

    void addAll(List<Node> nodes) {
        List<Node> nodeList = new ArrayList<>(nodes);
        nodeList.forEach(this::add);
    }

    Integer getNumberOf(Attributes attribute, String type) {
        int res = 0;
        for (Node node: nodes) {
            if (node.isTypeOfAttribute(attribute, type)) {
                res++;
            }
        }
        return res;
    }

    void clear() {
        nodes.clear();
    }

    boolean contains(Node n1) {
        return nodes.contains(n1);
    }
}
