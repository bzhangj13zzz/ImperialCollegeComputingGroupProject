package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.GroupingStrategy;
import ic.doc.sgo.groupingstrategies.StrategyUtil;

import java.util.*;
import java.util.stream.Collectors;

import static ic.doc.sgo.groupingstrategies.StrategyUtil.getRandomIntegerBetween;

public class VectorizedFixedPointStrategy {

    public static List<Cluster> apply(List<Node> nodes, VectorSpace vectorSpace) {
        List<Cluster> bestClusters = null;
        for (int i = 1; i <= 10; i++) {
            List<Node> newNodes = cloneNodes(nodes);
            List<Cluster> clusters = randomAllocateNodes(newNodes, vectorSpace);
            //List<Cluster> clusters = randomClusterWithDiscreteAttribute(newNodes, vectorSpace);
            //TODO: If there is no constraint, get out
            fixedPointToBest(newNodes, clusters, vectorSpace);

            validifyCluster(clusters, vectorSpace);
            for (Cluster cluster : clusters.subList(1, clusters.size())) {
                assert vectorSpace.isValidCluster(cluster);
            }

            AdjustClusterAfterValidation(clusters, vectorSpace);
            for (Cluster cluster : clusters.subList(1, clusters.size())) {
                assert vectorSpace.isValidCluster(cluster);
            }

            if (bestClusters == null) {
                bestClusters = clusters;
                continue;
            }

            if (clusters.get(0).size() == 0) {
                bestClusters = clusters;
                break;
            }

            if (clusters.get(0).size() < bestClusters.get(0).size()) {
                bestClusters = clusters;
            }
        }
        return bestClusters;
    }

    private static List<Cluster> randomAllocateNodes(List<Node> nodes, VectorSpace vectorSpace) {
        int size = nodes.size();
        List<Cluster> cluster = new ArrayList<>();
        if (size < vectorSpace.getClusterSizeLowerBound()) {
            cluster.add(Cluster.from(nodes));
            cluster.get(0).setId(0);
            return cluster;
        }
        StrategyUtil.Pair<Integer, Integer> numberIntervalOfGroups = StrategyUtil.getNumberInterval(nodes.size(),
                vectorSpace.getClusterSizeLowerBound(), vectorSpace.getClusterSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.first(), numberIntervalOfGroups.second());

        Collections.shuffle(nodes);
        for (int i = 0; i <= number; i++) {
            cluster.add(Cluster.of(i));
        }

        for (int i = 0; i < Math.min(vectorSpace.getClusterSizeLowerBound() * number, nodes.size()); i++) {
            cluster.get((i / vectorSpace.getClusterSizeLowerBound()) + 1).add(nodes.get(i));
            assert nodes.get(i).getCluster() != null;
        }


        if (vectorSpace.getClusterSizeLowerBound() == vectorSpace.getClusterSizeUpperBound()) {
            for (Node node: nodes.subList(vectorSpace.getClusterSizeLowerBound() * number, nodes.size())) {
                cluster.get(0).add(node);
            }
            return cluster;
        }

        for (int i = Math.min(vectorSpace.getClusterSizeLowerBound() * number, nodes.size()); i < nodes.size(); i++) {
            int groupId = getRandomIntegerBetween(1, number);
            while (cluster.get(groupId).size() >= vectorSpace.getClusterSizeUpperBound()) {
                groupId = getRandomIntegerBetween(1, number);
            }
            assert  i < nodes.size();
            cluster.get(groupId).add(nodes.get(i));
        }

        return cluster;
    }

    private static List<Node> cloneNodes(List<Node> nodes) {
        List<Node> newNodes = new ArrayList<>();
        for (Node node : nodes) {
            newNodes.add(Node.fromNode(node));
        }
        return newNodes;
    }

    private static void AdjustClusterAfterValidation(List<Cluster> clusters, VectorSpace vectorSpace) {

        boolean isChange = true;
        while (isChange) {
            isChange = false;
            List<Node> invalidNodes = new ArrayList<>(clusters.get(0).getNodes());
            for (Node node : invalidNodes) {
                for (Cluster cluster : clusters.subList(1, clusters.size())) {
                    if (vectorSpace.canBeFit(node, cluster)) {
                        cluster.add(node);
                        isChange = true;
                    }
                }
            }
        }

//        List<Node> leftNodes = new ArrayList<>(clusters.get(0).getNodes());
//        List<Cluster> leftClusters = randomAllocateNodes(leftNodes, vectorSpace);
//        fixedPointToBest(leftNodes, leftClusters, vectorSpace);
//        validifyCluster(leftClusters, vectorSpace);
//        clusters.addAll(leftClusters.subList(1, leftClusters.size()));
//        clusters.remove(0);
//        clusters.add(0, leftClusters.get(0));
//
//        for (int i = 0; i < clusters.size(); i++) {
//            clusters.get(i).setId(i);
//        }

        if (vectorSpace.isValidCluster(clusters.get(0))) {
            Cluster newCluster = Cluster.from(new ArrayList<>());
            List<Node> nodes = new ArrayList<>(clusters.get(0).getNodes());
            for (Node node : nodes) {
                newCluster.add(node);
            }
            clusters.add(newCluster);
            newCluster.setId(clusters.indexOf(newCluster));
        }
    }

    private static void validifyCluster(List<Cluster> clusters, VectorSpace vectorSpace) {
        List<Cluster> removeClusterList = new ArrayList<>();
        for (Cluster cluster : clusters.subList(1, clusters.size())) {
            if (!vectorSpace.isValidCluster(cluster)) {
                for (Node node : vectorSpace.getInvalidNodesFromCluster(cluster)) {
                    clusters.get(0).add(node);
                }
            }
            if (cluster.size() == 0) {
                removeClusterList.add(cluster);
            }
        }

        for (Cluster cluster : removeClusterList) {
            clusters.remove(cluster);
        }

        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).setId(i);
        }
    }

    private static void fixedPointToBest(List<Node> nodes, List<Cluster> clusters, VectorSpace vectorSpace) {
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Node n1 : nodes) {
                for (Node n2 : nodes) {
                    if (n1.getCluster().equals(n2.getCluster())) {
                        continue;
                    }
                    if (vectorSpace.isBetterFitIfSwap(n1, n2)) {
                        Node.swapCluster(n1, n2);
                        Cluster c1 = n1.getCluster();
                        Cluster c2 = n2.getCluster();
                        c1.setCurrentValue(c1.getTargetValue());
                        c2.setCurrentValue(c2.getTargetValue());
                        isChanged = true;
                    }
                }
            }

            if (!isChanged) {
                for (Node n : nodes) {
                    for (Cluster cluster : clusters) {
                        if (clusters.contains(n)) continue;
                        if (vectorSpace.canBeBetterFit(n, cluster)) {
                            cluster.add(n);
                            isChanged = true;
                        }
                    }
                }
            }

        }
    }

    private static List<Cluster> randomClusterWithDiscreteAttribute(List<Node> nodes, VectorSpace vectorSpace) {
        List<Node> maleNodes = nodes.stream().
                filter(node -> node.getTypeOfDiscreteAttributeOf(Attributes.GENDER).equals("male"))
                .collect(Collectors.toList());
        List<Node> femaleNode = nodes.stream()
                .filter(node -> node.getTypeOfDiscreteAttributeOf(Attributes.GENDER).equals("female"))
                .collect(Collectors.toList());


        StrategyUtil.Pair<Integer, Integer> numberIntervalOfGroups = StrategyUtil.getNumberInterval(nodes.size(),
                vectorSpace.getClusterSizeLowerBound(), vectorSpace.getClusterSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.first(), numberIntervalOfGroups.second());

        List<Cluster> maleCluster = randomCluster(maleNodes,
                vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "male"),
                number);
        List<Cluster> femaleCluster = randomCluster(femaleNode,
                vectorSpace.getDiscreteAttributeValue(Attributes.GENDER, "female"),
                number);

        // combine cluster id smaller than numbers
        List<Cluster> res = new ArrayList<>();
        for (int i = 0; i <= number; i++) {
            res.add(Cluster.of(i));
        }
        for (int i = 0; i <= number; i++) {
            Cluster cluster = res.get(i);
            cluster.setId(i);
            cluster.addAll(maleCluster.get(i).getNodes());
            cluster.addAll(femaleCluster.get(i).getNodes());
        }

        // put rest of the nodes to cluster 0
        for (int i = number + 1; i < maleCluster.size(); i++) {
            res.get(0).addAll(maleCluster.get(i).getNodes());
        }
        for (int i = number + 1; i < femaleCluster.size(); i++) {
            res.get(0).addAll(femaleCluster.get(i).getNodes());
        }

        // put cluster 0 nodes to cluster 1~number to suffice the size lower bound
        Collections.shuffle(res.get(0).getNodes());
        for (int i = 1; i <= number; i++) {
            while (res.get(i).size() < vectorSpace.getClusterSizeLowerBound()) {
                Node node = res.get(0).getNodes().remove(0);
                res.get(i).add(node);
            }
        }

        return res;
    }

    private static List<Cluster> randomCluster(List<Node> nodes, int clusterSize, int number) {
        //assert clusterSize*number <= nodes.size();
        List<Cluster> res = new ArrayList<>();
        for (int i = 0; i <= number; i++) {
            res.add(Cluster.of(i));
        }


        for (int i = 0; i < Math.min(number * clusterSize, nodes.size()); i++) {
            res.get((i / clusterSize) + 1).add(nodes.get(i));
        }

        for (int i = number * clusterSize; i < nodes.size(); i++) {
            res.get(0).add(nodes.get(i));
        }

        return res;
    }

}
