package ic.doc.sgo;


import ic.doc.sgo.groupingstrategies.Util;

import java.util.ArrayList;
import java.util.List;

public class Constraint {
    private int groupSizeLowerBound;
    private int groupSizeUpperBound;
    private int timezoneDiff;

    public void setGroupSizeLowerBound(int groupSizeLowerBound) {
        this.groupSizeLowerBound = groupSizeLowerBound;
    }

    public void setGroupSizeUpperBound(int groupSizeUpperBound) {
        this.groupSizeUpperBound = groupSizeUpperBound;
    }

    public int getGroupSizeLowerBound() {
        return groupSizeLowerBound;
    }

    public int getGroupSizeUpperBound() {
        return groupSizeUpperBound;
    }

    public int getTimezoneDiff() {
        return timezoneDiff;
    }

    public void setTimezoneDiff(int timezoneDiff) {
        this.timezoneDiff = timezoneDiff;
    }

    public boolean validGroup(Group group) {
        if (group.size() > groupSizeUpperBound) {
            return false;
        }
        if (group.size() < getGroupSizeLowerBound()) {
            return false;
        }
        if (getTimezoneDiffOfGroup(group) > timezoneDiff) {
            return false;
        }
        return true;
    }

    //evaluation value is between 0~1, higher the value, higher the matchness.
    public double evaluateGroup(Group group) {
        int timeZoneDifference = getTimezoneDiffOfGroup(group);
        return 1.0 * (12 - timeZoneDifference) / 12;
    }


    private int getTimezoneDiffOfGroup(Group group) {
        int res = 0;
        for (Student s1 : group.getStudents()) {
            for (Student s2 : group.getStudents()) {
                res = Math.max(res, TimeZoneCalculator.timeBetween(s1.getTimeZone(), s2.getTimeZone()));
            }
        }
        return res;
    }

    public boolean isValidGroup(Group group) {
        int timeZoneDifference = getTimezoneDiffOfGroup(group);
        if (group.size() < groupSizeLowerBound) {
            return false;
        }

        if (group.size() > groupSizeUpperBound) {
            return false;
        }

        if (getTimezoneDiffOfGroup(group) > timeZoneDifference) {
            return false;
        }

        return true;
    }

    public boolean studentCanBeFitInGroup(Student student, Group group) {
        Group originalGroup = student.getGroup();
        Util.assignStudentToGroup(student, group);
        boolean res = isValidGroup(group);
        Util.assignStudentToGroup(student, originalGroup);
        return res;
    }

    public List<Student> getUnvalidStudentsFromGroup(Group group) {
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
}
