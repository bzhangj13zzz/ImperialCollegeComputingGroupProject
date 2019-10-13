package ic.doc.sgo;

import com.neovisionaries.i18n.CountryCode;

import java.time.ZoneId;
import java.util.Objects;

public class Student {
    private final String id;
    private final String name;
    private final CountryCode countryCode;
    private final ZoneId timeZone;
    private final String gender;
    private final int age;
    private final String career;
    private final String degree;
    private final int workYearNum;
    private final String cohort;
    private int groupId;

    public Student(String id, String name, CountryCode countryCode, ZoneId timeZone, String gender,
                   int age, String career, String degree, int workYearNum, String cohort) {
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
        this.groupId = -1;
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
                Objects.equals(cohort, student.cohort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryCode, timeZone, gender, age, career, degree, workYearNum, cohort);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getCareer() {
        return career;
    }

    public String getDegree() {
        return degree;
    }

    public int getWorkYearNum() {
        return workYearNum;
    }

    public String getCohort() {
        return cohort;
    }

    public void setGroupId(int id) {
        this.groupId = id;
    }
}
