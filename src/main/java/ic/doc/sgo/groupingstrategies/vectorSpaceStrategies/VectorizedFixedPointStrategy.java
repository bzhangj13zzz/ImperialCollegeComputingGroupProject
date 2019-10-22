package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.GroupingStrategy;

import java.util.*;

public class VectorizedFixedPointStrategy implements GroupingStrategy {


    private Map<String, Student> idToStudents = new HashMap<>();

    @Override
    public List<Group> apply(List<Student> students, Constraint constraint) {
        VectorSpace vectorSpace = new VectorSpace(constraint);
        students.forEach(student -> idToStudents.put(student.getId(), student));
        List<Node> nodes = convertToNodes(students, constraint);
        List<Group> groups = allocateNodesInSpace(nodes, vectorSpace);
        return convertBackStudentsInGroups(groups);
    }

    private List<Group> convertBackStudentsInGroups(List<Group> groups) {
        List<Group> res = new ArrayList<>();
        for (Group group: groups) {
            Group newGroup = Group.of(group.getId());
            for (Student member: group.getStudents()) {
                newGroup.add(idToStudents.get(member.getId()));
            }
            res.add(newGroup);
        }
        return res;
    }

    private List<Group> allocateNodesInSpace(List<Node> nodes, VectorSpace vectorSpace) {
        return new ArrayList<>();
    }

    private List<Node> convertToNodes(List<Student> students, Constraint constraint) {
        List<Node> res = new ArrayList<>();
        for (Student student: students) {
            Node node = Node.createFromStudentWithConstraint(student, constraint);
            res.add(node);
        }
        return res;
    }
}
