package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constrain;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.List;

public interface GroupingStrategy {
    List<Group> apply(List<Student> students, Constrain constrain);
}
