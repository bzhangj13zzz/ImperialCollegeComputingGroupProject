package ic.doc.sgo.springframework.Service;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.GroupingStrategy;
import ic.doc.sgo.groupingstrategies.RandomGroupingStrategy;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GroupingService {
  private final GroupingStrategy groupingStrategy;

  public GroupingService() {
    this.groupingStrategy = new RandomGroupingStrategy();
  }

  public GroupingService(GroupingStrategy groupingStrategy) {
    this.groupingStrategy = groupingStrategy;
  }

  public List<Group> groupStudent(List<Student> studentList, Constraint constraint) {
    return groupingStrategy.apply(studentList,constraint);
  }
}
