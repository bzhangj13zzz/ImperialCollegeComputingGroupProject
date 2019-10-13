package ic.doc.sgo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Group {

    private final List<Student> studentList;
    private int id;

    public void setId(int id) {
        this.id = id;
    }


    public Group(List<Student> students) {
        studentList = students;
    }

    public static Group from(List<Student> students) {
        return new Group(students);
    }

    public static Group of(Student... students) {
        return new Group(Arrays.asList(students));
    }

    @Override
    public String toString() {
        return "Group{" +
                "studentList=" + studentList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(studentList, group.studentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentList);
    }

    public boolean add(Student student) {
        return studentList.add(student);
    }

    public boolean addAll(Collection<Student> students) {
        return studentList.addAll(students);
    }

    public boolean remove(Student student) {
        return studentList.remove(student);
    }

    public void clear() {
        studentList.clear();
    }

    public int getId() {
        return this.id;
    }

    public int size() {
        return studentList.size();
    }

    public List<Student> getStudents() {
        return this.studentList;
    }
}
