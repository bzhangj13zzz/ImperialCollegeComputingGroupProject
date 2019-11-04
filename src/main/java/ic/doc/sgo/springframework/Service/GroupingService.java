package ic.doc.sgo.springframework.Service;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.VectorizedFixedPointStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupingService {

    public List<Group> groupStudent(List<Student> studentList, Constraint constraint) {
        return new VectorizedFixedPointStrategy().apply(studentList, constraint);
    }
}
