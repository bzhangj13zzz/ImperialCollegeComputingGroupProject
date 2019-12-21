package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.*;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
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

        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(1, cores * 2 / 3);
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        List<Future<List<Cluster>>> futures = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i += 150) {
            int t = Math.min(i + 150, nodes.size());
            int finalI = i;
            futures.add(executorService.submit(
                    () -> VectorizedFixedPointStrategy.apply(new ArrayList<>(nodes.subList(finalI, t)), vectorSpace)));
        }

        for (Future<List<Cluster>> future : futures) {
            try {
                List<Cluster> cluster = future.get();

                if (cluster.size() > 0) {
                    validClusters.addAll(cluster.subList(1, cluster.size()));
                }
                unallocatedCluster.addAll(cluster.get(0).getNodes());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
//
//        for (int i = 0; i < nodes.size(); i += 150) {
//            int t = Math.min(i+150, nodes.size());
//            List<Cluster> cluster = VectorizedFixedPointStrategy.apply(new ArrayList<>(nodes.subList(i, t)),
//                            vectorSpace);
//            if (cluster.size() > 0) {
//                validClusters.addAll(cluster.subList(1, cluster.size()));
//            }
//            unallocatedCluster.addAll(cluster.get(0).getNodes());
//        }

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

    public static final class Converters {

        private Converters() {
        }

        public static VectorSpace VectorSpaceFromConstraint(Constraint constraint) {
            Map<String, VectorSpace.Property> dimensions = new HashMap<>();
            int clusterSizeLowerBound;
            int clusterSizeUpperBound;
            Map<String, HashMap<String, Integer>> discreteAttribute = new HashMap<>();

            if (constraint.isTimeMatter()) {
                dimensions.put(Attribute.TIMEZONE.getName(), new VectorSpace.Property(12.0, VectorSpace.Type.CIRCLE, (double) constraint.getTimezoneDiff().getAsInt()));
            }

            if (constraint.isAgeMatter()) {
                dimensions.put(Attribute.AGE.getName(), new VectorSpace.Property(120.0, VectorSpace.Type.LINE, (double) constraint.getAgeDiff().getAsInt()));
            }

            if (constraint.isSameGender()) {
                dimensions.put(Attribute.GENDER.getName(), new VectorSpace.Property(2.0, VectorSpace.Type.LINE, 0.0));
            }

            discreteAttribute.put(Attribute.GENDER.getName(), new HashMap<>());
            discreteAttribute.get(Attribute.GENDER.getName()).put("male", 0);
            discreteAttribute.get(Attribute.GENDER.getName()).put("female", 0);
            if (constraint.isGenderMatter()) {
                assert constraint.getMinMale() + constraint.getMinFemale() <= constraint.getGroupSizeLowerBound();
                discreteAttribute.get(Attribute.GENDER.getName()).put("male", constraint.getMinMale());
                discreteAttribute.get(Attribute.GENDER.getName()).put("female", constraint.getMinFemale());
            }

            clusterSizeLowerBound = constraint.getGroupSizeLowerBound();
            clusterSizeUpperBound = constraint.getGroupSizeUpperBound();
            return new VectorSpace(dimensions, clusterSizeLowerBound, clusterSizeUpperBound, discreteAttribute);
        }

        public static Node NodeFromStudentAndConstraint(Student student, Constraint constraint) {

            String id;
            Map<String, Double> coordinateMap = new HashMap<>();
            Map<String, String> discreteAttributeType = new HashMap<>();

            id = student.getId();

            if (constraint.isTimeMatter()) {
                coordinateMap.put(Attribute.TIMEZONE.getName(),
                        (double) ZoneIdUtils.zoneIdToInteger(student.getTimeZone().orElse(ZoneId.of("UTC+0"))));
            }

            if (constraint.isAgeMatter()) {
                coordinateMap.put(Attribute.AGE.getName(), (double) student.getAge().orElse(0));
            }

            if (constraint.isSameGender()) {
                coordinateMap.put(Attribute.GENDER.getName(), (double) genderToInteger(student.getGender().orElse("male")));
            }

            discreteAttributeType.put(Attribute.GENDER.getName(), "male");
            if (constraint.isGenderMatter()) {
                discreteAttributeType.put(Attribute.GENDER.getName(), student.getGender().orElse("male"));
            }

            return new Node(id, coordinateMap, discreteAttributeType);
        }

        private static int genderToInteger(String gender) {
            return gender.equals("male")? 0: 1;
        }

    }
}