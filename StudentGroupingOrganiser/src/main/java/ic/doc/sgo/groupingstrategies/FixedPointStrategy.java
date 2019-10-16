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
        return groups;
    }
}
