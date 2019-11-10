package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//N^3
public class FixedPointStrategy implements GroupingStrategy {

    private Map<String, Student> idToStudents = new HashMap<>();

    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        VectorSpace vectorSpace = Converters.VectorSpaceFromConstraint(constraint);
        students.forEach(student -> idToStudents.put(student.getId(), student));
        List<Node> nodes = students.stream()
                .map(student -> Converters.NodeFromStudentAndConstraint(student, constraint))
                .collect(Collectors.toList());


        List<Node> unallocatedCluster = new ArrayList<>();
        List<Cluster> validClusters = new ArrayList<>();
        //TODO: need to imporove the following.
        for (int i = 0; i < nodes.size(); i += 150) {
            int t = Math.min(i+150, nodes.size());
            List<Cluster> cluster = VectorizedFixedPointStrategy.apply(new ArrayList<>(nodes.subList(i, t)),
                            vectorSpace);
            if (cluster.size() > 0) {
                validClusters.addAll(cluster.subList(1, cluster.size()));
            }
            unallocatedCluster.addAll(cluster.get(0).getNodes());
        }

        System.out.println(unallocatedCluster.size());
        List<Cluster> bestClusters = VectorizedFixedPointStrategy.apply(unallocatedCluster, vectorSpace);
        bestClusters.addAll(validClusters);
        for (int i = 0; i < bestClusters.size(); i++) {
            bestClusters.get(i).setId(i);
        }

       // List<Cluster>  bestClusters = VectorizedFixedPointStrategy.apply(nodes, vectorSpace);
        for (Cluster cluster : bestClusters.subList(1, bestClusters.size())) {
            assert vectorSpace.isValidCluster(cluster);
        }

        return convertBackStudentsInGroups(bestClusters);
    }


    private List<Group> convertBackStudentsInGroups(List<Cluster> clusters) {
        List<Group> res = new ArrayList<>();
        for (Cluster cluster : clusters) {
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