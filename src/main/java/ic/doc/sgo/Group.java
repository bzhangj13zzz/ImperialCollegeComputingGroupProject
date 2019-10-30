package ic.doc.sgo;

import java.util.*;

public class Group {
    public static final int UNKNOWN_ID = -1;
    public static final int UNALLOC_ID = 0;

    private int id = UNKNOWN_ID;
    private final List<Student> students;

    private Group(List<Student> students) {
        this.students = students;
    }

    private Group(int id, List<Student> students) {
        this.id = id;
        this.students = students;
    }

    public static Group from(List<Student> members) {
        return new Group(members);
    }

    public static Group from(int id, List<Student> members) {
        return new Group(id, members);
    }

    public static Group of(Student...members) {
        return new Group(new ArrayList<>(Arrays.asList(members)));
    }

    public static Group of(int id, Student... members) {
        return new Group(id, new ArrayList<>(Arrays.asList(members)));
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", memberList=" + students +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, students);
    }

    public boolean add(Student member) {
        if (!this.students.contains(member)) {
            Group origin = member.getGroup();
            if (origin != null) {
                origin.remove(member);
            }
            this.students.add(member);
            member.setGroup(this);
            return true;
        }
        return true;
    }

    public boolean remove(Student member) {
        if (students.contains(member)) {
            students.remove(member);
            member.setGroup(null);
            return true;
        }
        return false;
    }


    public int size() {
        return students.size();
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean contains(Student member) {
        return students.contains(member);
    }

}
