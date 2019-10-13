package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constrain;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RandomGroupingStrategyTest {

    @Test
    public void allocateStudentsAccordingToConstrainGroupsSize() {
        Constrain constrain = new Constrain();
        constrain.setGroupSizeLowerBound(4);
        constrain.setGroupSizeUpperBound(7);
        List<Student> students = new ArrayList<>();
        int number = 1000;
        for (int i = 0; i < 1000; i++) {
            Student student = new Student();
            students.add(student);
        }
        List<Group> groups = new RandomGroupingStrategy().apply(students, constrain);
        for (Group group: groups) {
            assertTrue(group.size() <= constrain.getGroupSizeUpperBound());
            assertTrue( group.size() >= constrain.getGroupSizeLowerBound());
            for (Student student: group.getStudents()) {
                assertTrue(student.getGroupId() == group.getId());
            }
        }
    }

}