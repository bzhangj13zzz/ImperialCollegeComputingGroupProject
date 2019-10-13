package ic.doc.sgo.groupingstrategies;

import com.google.common.collect.Lists;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.List;
import java.util.stream.Collectors;

public class AlphabetGroupingStrategy implements GroupingStrategy {

    private final int groupSize;

    public AlphabetGroupingStrategy(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public List<Group> apply(List<Student> students) {
        students.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        List<List<Student>> partitioned = Lists.partition(students, groupSize);
        return partitioned.stream().map(Group::from).collect(Collectors.toList());
    }
}
