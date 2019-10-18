package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

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

        for (Group group : groups) {
            if (!constraint.isValidGroup(group)) {
                for (Student student: group.getStudents()) {
                    if
                }
            }
        }
        return groups;
    }
}
