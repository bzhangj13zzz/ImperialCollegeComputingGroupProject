package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.ArrayList;
import java.util.List;

//N^3
public class FixedPointStrategy implements GroupingStrategy {

    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        Util.Pair<Integer, Integer> numberIntervalOfGroups = Util.getNumberInterval(students.size(),
                constraint.getGroupSizeLowerBound(), constraint.getGroupSizeUpperBound());

        List<Group> groups = new RandomGroupingStrategy().apply(students, constraint);
        if (groups.size() == 1) {
            return groups;
        }

        FixedPointToBest(students, constraint);

        validifyGroups(groups, constraint);

        AdjustGroupsAfterValidation(groups, constraint);

        return groups;
    }

    private void FixedPointToBest(List<Student> students, Constraint constraint) {
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Student s1 : students) {
                for (Student s2 : students) {
                    if (Util.isSameGroup(s1, s2)) continue;
                    if (constraint.isBetterFitIfSwap(s1, s2)) {
                        Util.swapGroup(s1, s2);
                        isChanged = true;
                    }
                }
            }
        }
    }

    private void AdjustGroupsAfterValidation(List<Group> groups, Constraint constraint) {
        List<Student> invalidStudents = groups.get(0).getStudents();
        for (Student student : invalidStudents) {
            for (Group group : groups.subList(1, groups.size())) {
                if (constraint.studentCanBeFitInGroup(student, group)) {
                    group.add(student);
                }
            }
        }
        if (constraint.isValidGroup(groups.get(0))) {
            Group newGroup = Group.from(new ArrayList<>());
            List<Student> students = new ArrayList<>(groups.get(0).getStudents());
            for (Student student: students) {
                newGroup.add(student);
            }
            groups.add(newGroup);
            newGroup.setId(groups.indexOf(newGroup));
        }
    }

    private void validifyGroups(List<Group> groups, Constraint constraint) {
        List<Group> removeGroupList = new ArrayList<>();
        for (Group group: groups.subList(1, groups.size())) {
            if (!constraint.isValidGroup(group)) {
                for (Student student : constraint.getInvalidStudentsFromGroup(group)) {
                    groups.get(0).add(student);
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
