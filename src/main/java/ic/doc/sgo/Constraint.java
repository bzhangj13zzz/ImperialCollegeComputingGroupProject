package ic.doc.sgo;


import ic.doc.sgo.groupingstrategies.Util;

import java.time.ZoneId;
import java.util.*;

public class Constraint {
    private final int groupSizeLowerBound;
    private final int groupSizeUpperBound;
    private final int timezoneDiff;
    private final int ageDiff;
    private final Map<String, HashMap<String, Integer>> discreteTypes;

    private Constraint(int groupSizeLowerBound, int groupSizeUpperBound, int timezoneDiff, int ageDiff, Map<String, HashMap<String, Integer>> discreteTypes) {
        this.groupSizeLowerBound = groupSizeLowerBound;
        this.groupSizeUpperBound = groupSizeUpperBound;
        this.timezoneDiff = timezoneDiff;
        this.ageDiff = ageDiff;
        this.discreteTypes = discreteTypes;
    }

    public int getGroupSizeLowerBound() {
        return groupSizeLowerBound;
    }

    public int getGroupSizeUpperBound() {
        return groupSizeUpperBound;
    }

    //evaluation value is between 0~1, higher the value, higher the matchness.
    public double evaluateGroup(Group group) {
        int timeZoneDifference = getTimezoneDiffOfGroup(group);
        return 1.0 * (12 - timeZoneDifference) / 12;
    }


    private int getTimezoneDiffOfGroup(Group group) {
        int res = 0;
        for (Student s1 : group.getStudents()) {
            Optional<ZoneId> s1TimeZone = s1.getTimeZone();
            if (!s1TimeZone.isPresent()) {
                continue;
            }
            for (Student s2 : group.getStudents()) {
                Optional<ZoneId> s2TimeZone = s2.getTimeZone();
                if (!s2TimeZone.isPresent()) {
                    continue;
                }
                res = Math.max(res, TimeZoneCalculator.timeBetween(s1TimeZone.get(), s2TimeZone.get()));
            }
        }
        return res;
    }

    public boolean isValidGroup(Group group) {
        if (group.size() < groupSizeLowerBound) {
            return false;
        }

        if (group.size() > groupSizeUpperBound) {
            return false;
        }

        if (getTimezoneDiffOfGroup(group) > timezoneDiff) {
            return false;
        }

        return true;
    }

    public boolean canBeFit(Student student, Group group) {
        Group originalGroup = student.getGroup();
        group.add(student);
        boolean res = isValidGroup(group);
        originalGroup.add(student);
        return res;
    }

    public List<Student> getInvalidStudentsFromGroup(Group group) {

        List<Student> students = new ArrayList<>(group.getStudents());
        List<Student> removeStudents = new ArrayList<>();
        for (Student student: students) {
            if (isBetterFitIfRemove(student, group)) {
                removeStudents.add(student);
                group.remove(student);
                if (isValidGroup(group)) {
                    return removeStudents;
                }
            }
        }

        return students;
    }

    private boolean isBetterFitIfRemove(Student student, Group group) {
        double p = evaluateGroup(group);
        group.remove(student);
        double q = evaluateGroup(group);
        group.add(student);
        return q > p;
    }

    public boolean isBetterFitIfSwap(Student s1, Student s2) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        int v1 = 0;
        if (isValidGroup(g1)) {
            v1 ++;
        }
        if (isValidGroup(g2)) {
            v1 ++;
        }
        double pv1 = evaluateGroup(g1);
        double pv2 = evaluateGroup(g2);
        Util.swapGroup(s1, s2);
        int v2 = 0;
        if (isValidGroup(g1)) {
            v2 ++;
        }
        if (isValidGroup(g2)) {
            v2 ++;
        }
        double cv1 = evaluateGroup(g1);
        double cv2 = evaluateGroup(g2);;
        Util.swapGroup(s1, s2);

        if (v2 > v1) {
            return true;
        }
        if (v2 < v1) {
            return false;
        }
        return cv1 * cv2 > pv1 * pv2;
    }

    public boolean canBeBetterFit(Student s1, Group g2) {
        Group g1 = s1.getGroup();
        int v1 = 0;
        if (isValidGroup(g1)) {
            v1 ++;
        }
        if (isValidGroup(g2)) {
            v1 ++;
        }
        g2.add(s1);
        int v2 = 0;
        if (isValidGroup(g1)) {
            v2 ++;
        }
        if (isValidGroup(g2)) {
            v2 ++;
        }
        g1.add(s1);

        return v2 > v1;
    }

    public boolean isGenderMatter() {
        return this.discreteTypes.containsKey("gender");
    }

    public Integer getDiscreteAttributeValue(String attribute, String type) {
        return this.discreteTypes.get(attribute).get(type);
    }


    public static class Builder {
        private final int groupSizeLowerBound;
        private final int groupSizeUpperBound;
        private int timezoneDiff = -1;
        private int ageDiff = -1;
        private Map<String, HashMap<String, Integer>> discreteTypes = new HashMap<>();

        public Builder(int groupSizeLowerBound, int groupSizeUpperBound) {
            this.groupSizeLowerBound = groupSizeLowerBound;
            this.groupSizeUpperBound = groupSizeUpperBound;
        }

        public Builder setTimezoneDiff(int timezoneDiff) {
            this.timezoneDiff = timezoneDiff;
            return this;
        }

        public Builder setAgeDiff(int ageDiff) {
            this.ageDiff = ageDiff;
            return this;
        }

        public Constraint createConstrain() {
            return new Constraint(this.groupSizeLowerBound, this.groupSizeUpperBound, this.timezoneDiff, this.ageDiff, this.discreteTypes);
        }

        public Builder setMinimalMale(int number) {
            if (!this.discreteTypes.containsKey("gender")) {
                this.discreteTypes.put("gender", new HashMap<>());
            }
            this.discreteTypes.get("gender").put("male", number);
            return this;
        }

        public Builder setMinimalFemale(int number) {
            if (!this.discreteTypes.containsKey("gender")) {
                this.discreteTypes.put("gender", new HashMap<>());
            }
            this.discreteTypes.get("gender").put("female", number);
            return this;
        }
    }

    public OptionalInt getTimezoneDiff() {
        return this.timezoneDiff == -1? OptionalInt.empty(): OptionalInt.of(this.timezoneDiff);
    }

    public OptionalInt getAgeDiff() {
        return this.ageDiff == -1? OptionalInt.empty(): OptionalInt.of(this.ageDiff);
    }
}
