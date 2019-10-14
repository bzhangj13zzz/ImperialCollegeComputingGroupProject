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
        Pair<Integer, Integer> numberIntervalOfGroups = Util.getNumberInterval(students.size(),
                constraint.getGroupSizeLowerBound(), constraint.getGroupSizeUpperBound());
        int number = getRandomIntegerBetween(numberIntervalOfGroups.getKey(), numberIntervalOfGroups.getValue());

        Collections.shuffle(students);
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            groups.add(Group.of());
        }

        for (int i = 0; i < constraint.getGroupSizeLowerBound() * number; i++) {
            assignStudentToGroup(students.get(i), groups.get(i / constraint.getGroupSizeLowerBound()));
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
