package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ic.doc.sgo.groupingstrategies.Util.assignStudentToGroup;
import static ic.doc.sgo.groupingstrategies.Util.getRandomIntegerBetween;

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
        Pair<Integer, Integer> numberIntervalOfGroups = Util.getNumberInterval(students.size(),
                constraint.getGroupSizeLowerBound(), constraint.getGroupSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.getKey(), numberIntervalOfGroups.getValue());

        Collections.shuffle(students);

        for (int i = 0; i <= number; i++) {
            groups.add(Group.of());
            groups.get(i).setId(i);
        }

        for (int i = 0; i < constraint.getGroupSizeLowerBound() * number; i++) {
            assignStudentToGroup(students.get(i), groups.get((i / constraint.getGroupSizeLowerBound())+1));
        }

        for (int i = numberIntervalOfGroups.getKey() * number; i < size; i++) {
            int groupId = getRandomIntegerBetween(0, number - 1);
            while (groups.get(groupId).size() >= numberIntervalOfGroups.getValue()) {
                groupId = getRandomIntegerBetween(0, number - 1);
            }
            assignStudentToGroup(students.get(i), groups.get(groupId));
        }

        return groups;
    }

}
