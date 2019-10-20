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
        List<Group> bestGroups = new ArrayList<>();
        bestGroups.add(Group.from(students));
        for (int i = 1; i <= 100; i++) {
            List<Student> newStudents = cloneStudents(students);
            List<Group> groups = new RandomGroupingStrategy().apply(newStudents, constraint);
            if (groups.size() == 1) {
                return groups;
            }

            FixedPointToBest(newStudents, groups, constraint);

            validifyGroups(groups, constraint);

            AdjustGroupsAfterValidation(groups, constraint);

            if (groups.get(0).size() == 0) {
                return groups;
            }

            if (groups.get(0).size() < bestGroups.get(0).size()) {
                bestGroups = groups;
            }
        }

        return bestGroups;
    }

    private List<Student> cloneStudents(List<Student> students) {
        List<Student> newStudents =  new ArrayList<>();
        for (Student student: students) {
            newStudents.add(new Student.Builder(student).createStudent());
        }
        return newStudents;
    }

    private void FixedPointToBest(List<Student> students, List<Group> groups, Constraint constraint) {
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Student s1 : students) {
                for (Student s2 : students) {
                    if (Util.isSameGroup(s1, s2)) {
                        continue;
                    }
                    if (constraint.isBetterFitIfSwap(s1, s2)) {
                        Util.swapGroup(s1, s2);
                        isChanged = true;
                    }
                }
            }

            if (!isChanged) {
                for (Student s1: students) {
                    for (Group group: groups) {
                        if (groups.contains(s1)) continue;
                        if (constraint.canbeBetterFit(s1, group)) {
                            group.add(s1);
                            isChanged = true;
                        }
                    }
                }
            }

        }
    }

    private void AdjustGroupsAfterValidation(List<Group> groups, Constraint constraint) {
        List<Student> invalidStudents = new ArrayList<>(groups.get(0).getStudents());

        for (Student student: invalidStudents) {
            for (Group group: groups.subList(1, groups.size())) {
                if (constraint.canBeFit(student, group)) {
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
