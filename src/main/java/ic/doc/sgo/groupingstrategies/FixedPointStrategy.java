package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.ArrayList;
import java.util.List;

public class FixedPointStrategy implements GroupingStrategy {
    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        Util.Pair<Integer, Integer> numberIntervalOfGroups = Util.getNumberInterval(students.size(),
                constraint.getGroupSizeLowerBound(), constraint.getGroupSizeUpperBound());

        List<Group> groups = new RandomGroupingStrategy().apply(students, constraint);
        if (groups.size() == 1) {
            return groups;
        }

        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Student s1 : students) {
                for (Student s2 : students) {
                    if (Util.isSameGroup(s1, s2)) continue;
                    if (Util.isBetterFitIfSwap(s1, s2, constraint)) {
                        Util.swapGroup(s1, s2);
                        isChanged = true;
                    }
                }
            }
        }

        validifyGroups(constraint, groups);


        AdjustGroupsAfterValidation(constraint, groups);


        return groups;
    }

    private void AdjustGroupsAfterValidation(Constraint constraint, List<Group> groups) {
        for (Student student: groups.get(0).getStudents()) {
            for (Group group: groups.subList(1,groups.size())) {
                if (constraint.studentCanBeFitInGroup(student, group)) {
                    Util.assignStudentToGroup(student, group);
                }
            }
        }
        if (constraint.isValidGroup(groups.get(0))) {
            Group newGroup = Group.from(groups.get(0).getStudents());
            groups.add(newGroup);
            newGroup.setId(groups.indexOf(newGroup));
        }
    }

    private void validifyGroups(Constraint constraint, List<Group> groups) {
        List<Group> removeGroupList = new ArrayList<>();
        for (Group group : groups.subList(1, groups.size())) {
            if (!constraint.isValidGroup(group)) {
                for (Student student: constraint.getUnvalidStudentsFromGroup(group)) {
                    Util.assignStudentToGroup(student, groups.get(0));
                }
            }
            if (group.size() == 0) {
                removeGroupList.add(group);
            }
        }

        for (Group group: removeGroupList) {
            groups.remove(group);
        }

        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setId(i);
        }
    }
}
