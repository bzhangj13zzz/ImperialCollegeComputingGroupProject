package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.*;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class Node {

    private String id;
    private final Map<String, Double>  coordinateMap = new HashMap<>();
    private Cluster cluster;
    private String gender;


    private Node(Student student, Constraint constraint) {
        this.id = student.getId();

        if (constraint.getTimezoneDiff().isPresent()) {
            coordinateMap.put("timeZone", getTimeZoneInteger(student));
        }

        if (constraint.getAgeDiff().isPresent()) {
            coordinateMap.put("Age", (double) student.getAge().orElse(0));
        }

        if (constraint.isGenderMatter()) {
            this.gender = student.getGender().orElse("male");
        }
    }

    private double getTimeZoneInteger(Student student) {
        if (student.getTimeZone().isPresent()) {
            return (double) TimeZoneCalculator.timeZoneInInteger(student.getTimeZone().get());
        }
        return 0.0;
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

    public String getGender() {
        return this.gender;
    }
}
