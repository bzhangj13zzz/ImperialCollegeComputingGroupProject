package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.GroupingStrategy;
import ic.doc.sgo.groupingstrategies.Util;

import java.util.*;
import java.util.stream.Collectors;

import static ic.doc.sgo.groupingstrategies.Util.getRandomIntegerBetween;

public class VectorizedFixedPointStrategy implements GroupingStrategy {


    private Map<String, Student> idToStudents = new HashMap<>();

    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        VectorSpace vectorSpace = new VectorSpace(constraint);
        students.forEach(student -> idToStudents.put(student.getId(), student));
        List<Node> nodes = students.stream().map(student -> Node.createFromStudentWithConstraint(student, constraint))
                .collect(Collectors.toList());

        List<Cluster> bestClusters = randomClusterWithGender(nodes, vectorSpace);

        for (int i = 1; i <= 100; i++) {
            List<Cluster> clusters = randomClusterWithGender(nodes, vectorSpace);

            fixedPointToBest(nodes, clusters, vectorSpace);

            validifyCluster(clusters, vectorSpace);

            AdjustClusterAfterValidation(clusters, vectorSpace);

            if (clusters.get(0).size() == 0) {
                bestClusters = clusters;
                break;
            }

            if (clusters.get(0).size() < bestClusters.get(0).size()) {
                bestClusters = clusters;
            }

        }

        return convertBackStudentsInGroups(bestClusters);
    }

    private void AdjustClusterAfterValidation(List<Cluster> clusters, VectorSpace vectorSpace) {
    }

    private void validifyCluster(List<Cluster> clusters, VectorSpace vectorSpace) {
    }

    private void fixedPointToBest(List<Node> nodes, List<Cluster> clusters, VectorSpace vectorSpace) {
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Node n1 : nodes) {
                for (Node n2 : nodes) {
                    if (n1.getCluster().equals(n2.getCluster())) {
                        continue;
                    }
                    if (vectorSpace.isBetterFitIfSwap(n1, n2)) {
                        Util.swapCluster(n1, n2);
                        isChanged = true;
                    }
                }
            }

            if (!isChanged) {
                for (Node n: nodes) {
                    for (Cluster cluster: clusters) {
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

    private List<Cluster> randomClusterWithGender(List<Node> nodes, VectorSpace vectorSpace) {
        List<Node> maleNodes = nodes.stream().filter(node -> node.getGender() == "male").collect(Collectors.toList());
        List<Node> femaleNode = nodes.stream().filter(node -> node.getGender() == "female").collect(Collectors.toList());


        Util.Pair<Integer, Integer> numberIntervalOfGroups = Util.getNumberInterval(nodes.size(),
                vectorSpace.getClusterSizeLowerBound(), vectorSpace.getClusterSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.first(), numberIntervalOfGroups.second());

        List<Cluster> maleCluster = randomCluster(maleNodes,
                vectorSpace.getDiscreteAttributeValue("gender", "male"),
                number);
        List<Cluster> femaleCluster = randomCluster(femaleNode,
                vectorSpace.getDiscreteAttributeValue("gender", "female"),
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

    private List<Cluster> randomCluster(List<Node> nodes, int clusterSize, int number) {
        assert clusterSize*number < nodes.size();
        List<Cluster> res = new ArrayList<>();
        for (int i = 0; i <= number; i++) {
            res.add(Cluster.of(i));
        }


        for (int i = 0; i < number*clusterSize; i++) {
            res.get((i/clusterSize)+1).add(nodes.get(i));
        }

        for (int i = number*clusterSize; i < nodes.size(); i++) {
            res.get(0).add(nodes.get(i));
        }

        return res;
    }

    private List<Group> convertBackStudentsInGroups(List<Cluster> clusters) {
        List<Group> res = new ArrayList<>();
        for (Cluster cluster: clusters) {
            List<Student> students = cluster.getNodes()
                    .stream()
                    .map(node -> idToStudents.get(node.getId()))
                    .collect(Collectors.toList());
            Group newGroup = Group.from(students);
            newGroup.setId(cluster.getId());
            res.add(newGroup);
        }
        return res;
    }
}
