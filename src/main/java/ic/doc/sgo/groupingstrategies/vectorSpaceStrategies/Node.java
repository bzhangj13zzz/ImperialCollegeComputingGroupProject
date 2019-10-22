package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.*;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private final Map<String, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;


    private Node(Student student, Constraint constraint) {
        this.id = student.getId();

        if (constraint.getTimezoneDiff() != null) {
            coordinateMap.put("timeZone", (double) TimeZoneCalculator.timeZoneInInteger(student.getTimeZone().orElse(null)));
        }

        if (constraint.getAgeDiff() != null) {
            coordinateMap.put("Age", (double) student.getAge().orElse(0));
        }
    }

    public static Node createFromStudentWithConstraint(Student student, Constraint constraint) {
        return new Node(student, constraint);
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getId() {
        return this.id;
    }
}
