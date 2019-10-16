package ic.doc.sgo;

import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.util.*;

public class Student {
    private final String id; // never null
    private final String name; // never null
    private final CountryCode countryCode;
    private final ZoneId timeZone;
    private final String gender;
    private final int age;
    private final String career;
    private final String degree;
    private final int workYearNum;
    private final String cohort;
    private final Map<String, Object> additionalAttributes; // never null
    private Group group;

    private Student(String id, String name, @Nullable CountryCode countryCode, @Nullable ZoneId timeZone,
                    @Nullable String gender, int age, @Nullable String career, @Nullable String degree,
                    int workYearNum, @Nullable String cohort, Map<String, Object> additionalAttributes) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.timeZone = timeZone;
        this.gender = gender;
        this.age = age;
        this.career = career;
        this.degree = degree;
        this.workYearNum = workYearNum;
        this.cohort = cohort;
        this.additionalAttributes = additionalAttributes;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", countryCode=" + countryCode +
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
                Objects.equals(name, student.name) &&
                countryCode == student.countryCode &&
                Objects.equals(timeZone, student.timeZone) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(career, student.career) &&
                Objects.equals(degree, student.degree) &&
                Objects.equals(cohort, student.cohort) &&
                Objects.equals(additionalAttributes, student.additionalAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryCode, timeZone, gender, age, career, degree,
                workYearNum, cohort, additionalAttributes);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<CountryCode> getCountryCode() {
        return Optional.ofNullable(countryCode);
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

    public OptionalInt getWorkYearNum() {
        return workYearNum == -1 ? OptionalInt.empty() : OptionalInt.of(workYearNum);
    }

    public Optional<String> getCohort() {
        return Optional.ofNullable(cohort);
    }

    public Optional<Object> getAttribute(String key) {
        return Optional.ofNullable(additionalAttributes.get(key));
    }

    public void addAttribute(String key, Object value) {
        additionalAttributes.put(key, value);
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public static class Builder {
        private final String id;
        private final String name;
        private CountryCode countryCode = null;
        private ZoneId timeZone = null;
        private String gender = null;
        private int age = -1;
        private String career = null;
        private String degree = null;
        private int workYearNum = -1;
        private String cohort = null;
        private final Map<String, Object> additionalAttributes = new HashMap<>();

        public Builder(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder setCountryCode(CountryCode countryCode) {
            this.countryCode = countryCode;
            return this;
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

        public Builder setWorkYearNum(int workYearNum) {
            this.workYearNum = workYearNum;
            return this;
        }

        public Builder setCohort(String cohort) {
            this.cohort = cohort;
            return this;
        }

        public Builder addAttribute(String key, Object value) {
            additionalAttributes.put(key, value);
            return this;
        }

        public Student createStudent() {
            return new Student(id, name, countryCode, timeZone, gender, age, career, degree,
                    workYearNum, cohort, additionalAttributes);
        }
    }
}