package ic.doc.sgo;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return false;
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

    public void clear() {
        students.clear();
    }

    public void addAll(List<Student> studentList) {
        studentList.forEach(this::add);
    }

    public int getAgeDiffOfGroup() {
        int min = Integer.MAX_VALUE;
        int max = -1;
        for (Student s : this.students) {
            OptionalInt optionalAge = s.getAge();
            if (!optionalAge.isPresent()) {
                continue;
            }
            int age = optionalAge.getAsInt();
            min = Math.min(min, age);
            max = Math.max(max, age);
        }

        if (max == -1 && min == Integer.MAX_VALUE) {
            return 0;
        }
        return max - min;
    }

    public int getFemaleNumberOfGroup() {
        return students.stream()
                .filter(student -> student.getGender().orElse("male").equals("female"))
                .collect(Collectors.toList()).size();
    }

    public int getMaleNumberOfGroup() {
        return students.stream()
                .filter(student -> student.getGender().orElse("male").equals("male"))
                .collect(Collectors.toList()).size();
    }

    public boolean isGroupSameGender() {
        List<Student> students = getStudents();
        for (Student student : students) {
            if (!student.getGender().orElse("male").equals(students.get(0).getGender().orElse("male"))) {
                return false;
            }
        }
        return true;
    }

    public int getTimezoneDiffOfGroup() {
        int res = 0;
        for (Student s1 : getStudents()) {
            Optional<ZoneId> s1TimeZone = s1.getTimeZone();
            if (!s1TimeZone.isPresent()) {
                continue;
            }
            for (Student s2 : getStudents()) {
                Optional<ZoneId> s2TimeZone = s2.getTimeZone();
                if (!s2TimeZone.isPresent()) {
                    continue;
                }
                res = Math.max(res, TimeZoneCalculator.timeBetween(s1TimeZone.get(), s2TimeZone.get()));
            }
        }
        return res;
    }
}
