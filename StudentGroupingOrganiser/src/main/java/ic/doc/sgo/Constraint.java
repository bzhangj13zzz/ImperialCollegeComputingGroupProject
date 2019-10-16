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
        int timeZoneDifference = group.getTimeZoneDifference();
        return 1.0*(12 - timeZoneDifference)/12;
    }

    public boolean isValidGroup(Group group) {
        int timeZoneDifference = group.getTimeZoneDifference();
        if (group.size() < groupSizeLowerBound) {
            return false;
        }

        if (group.size() > groupSizeUpperBound) {
            return false;
        }

        if (group.getTimeZoneDifference() > timeZoneDifference) {
            return false;
        }

        return true;
    }
}
