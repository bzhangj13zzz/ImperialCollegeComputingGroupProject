package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ic.doc.sgo.groupingstrategies.StrategyUtils.getRandomIntegerBetween;

public class RandomGroupingStrategy implements GroupingStrategy {

    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        int size = students.size();
        List<Group> groups = new ArrayList<>();
        if (size < constraint.getGroupSizeLowerBound()) {
            groups.add(Group.from(students));
            groups.get(0).setId(0);
            return groups;
        }
        StrategyUtils.Pair<Integer, Integer> numberIntervalOfGroups = StrategyUtils.getNumberInterval(students.size(),
                constraint.getGroupSizeLowerBound(), constraint.getGroupSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.first(), numberIntervalOfGroups.second());

        Collections.shuffle(students);
        for (int i = 0; i <= number; i++) {
            groups.add(Group.of(i));
        }

        for (int i = 0; i < constraint.getGroupSizeLowerBound() * number; i++) {
            groups.get((i / constraint.getGroupSizeLowerBound()) + 1).add(students.get(i));
            assert students.get(i).getGroup() != null;
        }


        if (constraint.getGroupSizeLowerBound() == constraint.getGroupSizeUpperBound()) {
            for (Student student : students.subList(constraint.getGroupSizeLowerBound() * number, students.size())) {
                groups.get(0).add(student);
            }
            return groups;
        }

        for (int i = constraint.getGroupSizeLowerBound() * number; i < size; i++) {
            int groupId = getRandomIntegerBetween(1, number);
            while (groups.get(groupId).size() >= constraint.getGroupSizeUpperBound()) {
                groupId = getRandomIntegerBetween(1, number);
            }
            groups.get(groupId).add(students.get(i));
        }

        return groups;
    }
}
