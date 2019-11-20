package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import ic.doc.sgo.groupingstrategies.StrategyUtil;

import java.util.*;

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
        List<Cluster> clusters = new ArrayList<>();
        if (size < vectorSpace.getClusterSizeLowerBound()) {
            clusters.add(Cluster.from(nodes));
            clusters.get(0).setId(0);
            return clusters;
        }
        StrategyUtil.Pair<Integer, Integer> numberIntervalOfGroups = StrategyUtil.getNumberInterval(nodes.size(),
                vectorSpace.getClusterSizeLowerBound(), vectorSpace.getClusterSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.first(), numberIntervalOfGroups.second());

        Collections.shuffle(nodes);
        for (int i = 0; i <= number; i++) {
            clusters.add(Cluster.of(i));
        }

        for (int i = 0; i < Math.min(vectorSpace.getClusterSizeLowerBound() * number, nodes.size()); i++) {
            clusters.get((i / vectorSpace.getClusterSizeLowerBound()) + 1).add(nodes.get(i));
            assert nodes.get(i).getCluster() != null;
        }


        if (vectorSpace.getClusterSizeLowerBound() == vectorSpace.getClusterSizeUpperBound()) {
            for (Node node : nodes.subList(vectorSpace.getClusterSizeLowerBound() * number, nodes.size())) {
                clusters.get(0).add(node);
            }
            return clusters;
        }

        for (int i = Math.min(vectorSpace.getClusterSizeLowerBound() * number, nodes.size()); i < nodes.size(); i++) {
            int groupId = getRandomIntegerBetween(1, number);
            while (clusters.get(groupId).size() >= vectorSpace.getClusterSizeUpperBound()) {
                groupId = getRandomIntegerBetween(1, number);
            }
            assert i < nodes.size();
            clusters.get(groupId).add(nodes.get(i));
        }

        return clusters;
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
                        cluster.setValid(cluster.getTargetValid());
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
                        c1.setValid(c1.getTargetValid());
                        c2.setValid(c2.getTargetValid());
                        isChanged = true;
                    }
                }
            }

            if (!isChanged) {
                for (Node n : nodes) {
                    for (Cluster c1 : clusters) {
                        if (clusters.contains(n)) continue;
                        if (vectorSpace.canBeBetterFit(n, c1)) {
                            Cluster c2 = n.getCluster();
                            c1.add(n);
                            c1.setCurrentValue(c1.getTargetValue());
                            c2.setCurrentValue(c2.getTargetValue());
                            c1.setValid(c1.getTargetValid());
                            c2.setValid(c2.getTargetValid());
                            isChanged = true;
                        }
                    }
                }
            }

        }
    }

}
