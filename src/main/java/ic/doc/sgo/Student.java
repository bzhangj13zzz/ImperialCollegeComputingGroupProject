package ic.doc.sgo;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.util.*;

public class Student {
    private final String id;
    private final ZoneId timeZone;
    private final String gender;
    private final int age;
    private final String career;
    private final String degree;
    private final double workYearNum;
    private final String cohort;
    private final Map<String, String> additionalAttributes; // never null
    private Group group;

    private Student(String id, @Nullable ZoneId timeZone,
                    @Nullable String gender, int age, @Nullable String career, @Nullable String degree,
                    double workYearNum, @Nullable String cohort, Map<String, String> additionalAttributes) {
        this.id = id;
        this.timeZone = timeZone;
        this.gender = gender;
        this.age = age;
        this.career = career;
        this.degree = degree;
        this.workYearNum = workYearNum;
        this.cohort = cohort;
        this.additionalAttributes = additionalAttributes;
    }

    public static Student fromStudent(Student student) {
        return new Student(student.id, student.timeZone, student.gender, student.age, student.career, student.degree,
                student.workYearNum, student.cohort, new HashMap<>(student.additionalAttributes));
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", timeZone=" + timeZone +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", career='" + career + '\'' +
                ", degree='" + degree + '\'' +
                ", workYearNum=" + workYearNum +
                ", cohort='" + cohort + '\'' +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age &&
                workYearNum == student.workYearNum &&
                Objects.equals(id, student.id) &&
                Objects.equals(timeZone, student.timeZone) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(career, student.career) &&
                Objects.equals(degree, student.degree) &&
                Objects.equals(cohort, student.cohort) &&
                Objects.equals(additionalAttributes, student.additionalAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeZone, gender, age, career, degree,
                workYearNum, cohort, additionalAttributes);
    }

    public String getId() {
        return id;
    }

    public Optional<ZoneId> getTimeZone() {
        return Optional.ofNullable(timeZone);
    }

    public Optional<String> getGender() {
        return Optional.ofNullable(gender);
    }

    public OptionalInt getAge() {
        return age == -1 ? OptionalInt.empty() : OptionalInt.of(age);
    }

    public Optional<String> getCareer() {
        return Optional.ofNullable(career);
    }

    public Optional<String> getDegree() {
        return Optional.ofNullable(degree);
    }

    public OptionalDouble getWorkYearNum() {
        return workYearNum == -1.0 ? OptionalDouble.empty() : OptionalDouble.of(workYearNum);
    }

    public Optional<String> getCohort() {
        return Optional.ofNullable(cohort);
    }

    public Optional<String> getAttribute(String key) {
        return Optional.ofNullable(additionalAttributes.get(key));
    }

    public void addAttribute(String key, String value) {
        additionalAttributes.put(key, value);
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public static boolean isSameGroup(Student s1, Student s2) {
        return s1.getGroup() == s2.getGroup();
    }

    public static void swapGroup(Student s1, Student s2) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        g2.add(s1);
        g1.add(s2);
    }


    public static class Builder {
        private final String id;
        private ZoneId timeZone = null;
        private String gender = null;
        private int age = 30;
        private String career = null;
        private String degree = null;
        private double workYearNum = 12;
        private String cohort = null;
        private final Map<String, String> additionalAttributes = new HashMap<>();

        public Builder(String id) {
            this.id = id;
        }

        public Builder setTimeZone(ZoneId timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setCareer(String career) {
            this.career = career;
            return this;
        }

        public Builder setDegree(String degree) {
            this.degree = degree;
            return this;
        }

        public Builder setWorkYearNum(double workYearNum) {
            this.workYearNum = workYearNum;
            return this;
        }

        public Builder setCohort(String cohort) {
            this.cohort = cohort;
            return this;
        }

        public Builder addAttribute(String key, String value) {
            additionalAttributes.put(key, value);
            return this;
        }

        public Student createStudent() {
            return new Student(id, timeZone, gender, age, career, degree,
                    workYearNum, cohort, additionalAttributes);
        }

        public Builder setAdditionalDiscreteAttributeWithType(String attribute, String type) {
            this.additionalAttributes.put(attribute, type);
            return this;
        }

        public String getAddtionalAttribute(String attribute) {
            if (this.additionalAttributes.containsKey(attribute)) {
                return this.additionalAttributes.get(attribute);
            }
            return "";
        }
    }
}