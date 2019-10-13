package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constrain;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomGroupingStrategy implements GroupingStrategy {

    @Override
    public List<Group> apply(List<Student> students, Constrain constrain) {
        int size = students.size();
        int numberOfGroupsLowerBound = (int) Math.ceil(1.0*size/constrain.getGroupSizeUpperBound());
        int numberOfGroupsUpperBound = (int) Math.floor(1.0* size/constrain.getGroupSizeLowerBound());
        int number = getRandomIntegerBetween(numberOfGroupsLowerBound, numberOfGroupsUpperBound);

        Collections.shuffle(students);
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            groups.add(new Group(new ArrayList<>()));
        }

        for (int i = 0; i < constrain.getGroupSizeLowerBound()*number; i++) {
            assignStudentToGroup(students.get(i), groups.get(i/constrain.getGroupSizeLowerBound()));
        }

        for (int i = numberOfGroupsLowerBound*number; i < size; i++) {
            int groupId = getRandomIntegerBetween(0, number-1);
            while (groups.get(groupId).size() >= numberOfGroupsUpperBound) {
                groupId = getRandomIntegerBetween(0, number-1);
            }
            assignStudentToGroup(students.get(i), groups.get(groupId));
        }

        return groups;
    }

    private void assignStudentToGroup(Student student, Group group) {
        student.setGroupId(group.getId());
        group.add(student);
    }

    private int getRandomIntegerBetween(int a, int b) {
        Random r = new Random();
        return r.nextInt((b - a) + 1) + a;
    }
}
