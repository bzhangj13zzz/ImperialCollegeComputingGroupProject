package ic.doc.sgo;

import java.util.*;

public class Group {

    private int id = -1;
    private final List<Student> studentList;

    private Group(List<Student> students) {
        studentList = students;
    }

    public static Group from(List<Student> students) {
        return new Group(students);
    }

    public static Group of(Student... students) {
        return new Group(new ArrayList<>(Arrays.asList(students)));
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", studentList=" + studentList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(studentList, group.studentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentList);
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

    public int size() {
        return studentList.size();
    }

    public List<Student> getStudents() {
        return this.studentList;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
