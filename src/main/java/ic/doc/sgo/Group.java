package ic.doc.sgo;

import java.util.*;

public class Group {
    public static final int UNKNOWN_ID = -1;
    public static final int UNALLOC_ID = 0;

    private static final Map<Student, Group> studentGroupMap = new HashMap<>();

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

    public static Group of(Student... members) {
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

    public static Optional<Group> getGroup(Student student) {
        if (studentGroupMap.containsKey(student)) {
            return Optional.of(studentGroupMap.get(student));
        }
        return Optional.empty();
    }

    public static void setGroup(Student student, Group group) {
        if (group == null) {
            studentGroupMap.remove(student);
        } else {
            studentGroupMap.put(student, group);
        }
    }

    public static boolean isSameGroup(Student s1, Student s2) {
        Optional<Group> optionalGroup1 = getGroup(s1);
        Optional<Group> optionalGroup2 = getGroup(s2);
        if (optionalGroup1.isPresent() && optionalGroup2.isPresent()) {
            return optionalGroup1.get().equals(optionalGroup2.get());
        }
        return false;
    }

    public boolean add(Student member) {
        if (!this.students.contains(member)) {
            Optional<Group> origin = getGroup(member);
            origin.ifPresent(group -> group.remove(member));
            this.students.add(member);
            setGroup(member, this);
            return true;
        }
        return false;
    }

    public boolean remove(Student member) {
        if (students.contains(member)) {
            students.remove(member);
            setGroup(member, null);
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

    public void clear() {
        students.clear();
    }

    public void addAll(ArrayList<Student> students) {
        List<Student> studentList = new ArrayList<>(students);
        studentList.forEach(this::add);
    }
}
