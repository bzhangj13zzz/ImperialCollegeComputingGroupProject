package ic.doc.sgo;

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
        if (getGroupTimezoneDiff(group) > timezoneDiff) {
            return false;
        }
        return true;
    }

    private int getGroupTimezoneDiff(Group group) {
        List<Student> students = group.getStudents();
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < i; j++) {
            }
        }
        return -1;
    }

    //evaluation value is between 0~1, higher the value, higher the matchness.
    public double evaluateGroup(Group group) {
        int timeZoneDifference = getTimezoneDiffOfGroup(group);
        return 1.0*(12 - timeZoneDifference)/12;
    }

    private int getTimezoneDiffOfGroup(Group group) {
        int res = 0;
        for (Student s1: group.getStudents()) {
            for (Student s2: group.getStudents()) {
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
}
