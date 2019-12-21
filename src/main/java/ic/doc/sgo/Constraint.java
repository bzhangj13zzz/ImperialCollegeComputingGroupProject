package ic.doc.sgo;


import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Constraint {
    private final int groupSizeLowerBound;
    private final int groupSizeUpperBound;
    private final Integer timezoneDiff;
    private final Integer ageDiff;
    // Either both `minMale` and `minFemale` are passed in.
    private final Integer minMale;
    private final Integer minFemale;
    // Or both `genderRatio` and `genderErrorMargin` are passed in
    private final Double genderRatio;
    private final Double genderErrorMargin;
    private final Boolean isSameGender;

    private Constraint(int groupSizeLowerBound, int groupSizeUpperBound, Integer timezoneDiff, Integer ageDiff,
                       Integer minMale, Integer minFemale, Double genderRatio, Double genderErrorMargin, Boolean isSameGender) {
        this.groupSizeLowerBound = groupSizeLowerBound;
        this.groupSizeUpperBound = groupSizeUpperBound;
        this.timezoneDiff = timezoneDiff;
        this.ageDiff = ageDiff;
        this.minMale = minMale;
        this.minFemale = minFemale;
        this.genderRatio = genderRatio;
        this.genderErrorMargin = genderErrorMargin;
        this.isSameGender = isSameGender;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "groupSizeLowerBound=" + groupSizeLowerBound +
                ", groupSizeUpperBound=" + groupSizeUpperBound +
                ", timezoneDiff=" + timezoneDiff +
                ", ageDiff=" + ageDiff +
                ", minMale=" + minMale +
                ", minFemale=" + minFemale +
                ", genderRatio=" + genderRatio +
                ", genderErrorMargin=" + genderErrorMargin +
                ", isSameGender=" + isSameGender +
                '}';
    }

    public int getMinMale() {
        assert isGenderMatter();
        if (genderRatio != null) {
            assert genderErrorMargin != null;
            return (int) ((genderRatio - genderErrorMargin) * groupSizeUpperBound);
        }
        if (minMale == null) {
            return 0;
        }
        return minMale;
    }

    public int getMinFemale() {
        assert isGenderMatter();
        if (genderRatio != null) {
            assert genderErrorMargin != null;
            return (int) ((1 - genderRatio - genderErrorMargin) * groupSizeUpperBound);
        }
        if (minFemale == null) {
            return 0;
        }
        return minFemale;
    }

    public double getGenderRatio() {
        return genderRatio;
    }

    public double getGenderErrorMargin() {
        return genderErrorMargin;
    }

    public int getGroupSizeLowerBound() {
        return groupSizeLowerBound;
    }

    public int getGroupSizeUpperBound() {
        return groupSizeUpperBound;
    }

    //evaluation value is between 0~1, higher the value, higher the matchness.
    public double evaluateGroup(Group group) {
        int timeZoneDifference = getTimezoneDiffOfGroup(group);
        return 1.0 * (12 - timeZoneDifference) / 12;
    }


    public int getTimezoneDiffOfGroup(Group group) {
        int res = 0;
        for (Student s1 : group.getStudents()) {
            Optional<ZoneId> s1TimeZone = s1.getTimeZone();
            if (!s1TimeZone.isPresent()) {
                continue;
            }
            for (Student s2 : group.getStudents()) {
                Optional<ZoneId> s2TimeZone = s2.getTimeZone();
                if (!s2TimeZone.isPresent()) {
                    continue;
                }
                res = Math.max(res, ZoneIdUtils.timeBetween(s1TimeZone.get(), s2TimeZone.get()));
            }
        }
        return res;
    }

    public boolean isValidGroup(Group group) {
        if (group.size() < groupSizeLowerBound) {
            return false;
        }

        if (group.size() > groupSizeUpperBound) {
            return false;
        }

        if (isTimeMatter() && getTimezoneDiffOfGroup(group) > timezoneDiff) {
            return false;
        }

        if (isAgeMatter() && getAgeDiffOfGroup(group) > ageDiff) {
            return false;
        }

        if (isGenderMatter() && (getMinFemale() > getFemaleNumberOfGroup(group) || getMinMale() > getMaleNumberOfGroup(group))) {
            return false;
        }

        if (isSameGender() && !isGroupSameGender(group)) {
            return false;
        }

        return true;
    }

    public boolean isGroupSameGender(Group group) {
        List<Student> students = group.getStudents();
        for (Student student: students) {
            if (!student.getGender().orElse("male").equals(students.get(0).getGender().orElse("male"))) {
                return false;
            }
        }
        return true;
    }


    private int getMaleNumberOfGroup(Group group) {
        return group.getStudents().stream()
                .filter(student -> student.getGender().orElse("male").equals("male"))
                .collect(Collectors.toList()).size();
    }

    private int getFemaleNumberOfGroup(Group group) {
        return group.getStudents().stream()
                .filter(student -> student.getGender().orElse("male").equals("female"))
                .collect(Collectors.toList()).size();
    }

    public Integer getAgeDiffOfGroup(Group group) {
        Integer res = 0;
        for (Student s1 : group.getStudents()) {
            OptionalInt a1 = s1.getAge();
            if (!a1.isPresent()) {
                continue;
            }
            for (Student s2 : group.getStudents()) {
                OptionalInt a2 = s2.getAge();
                if (!a2.isPresent()) {
                    continue;
                }
                res = Math.max(res, Math.abs(a2.getAsInt() - a1.getAsInt()));
            }
        }
        return res;

    }

    public boolean canBeFit(Student student, Group group) {
        Group originalGroup = student.getGroup();
        group.add(student);
        boolean res = isValidGroup(group);
        originalGroup.add(student);
        return res;
    }

    public List<Student> getInvalidStudentsFromGroup(Group group) {

        List<Student> students = new ArrayList<>(group.getStudents());
        List<Student> removeStudents = new ArrayList<>();
        for (Student student : students) {
            if (isBetterFitIfRemove(student, group)) {
                removeStudents.add(student);
                group.remove(student);
                if (isValidGroup(group)) {
                    return removeStudents;
                }
            }
        }

        return students;
    }

    private boolean isBetterFitIfRemove(Student student, Group group) {
        double p = evaluateGroup(group);
        group.remove(student);
        double q = evaluateGroup(group);
        group.add(student);
        return q > p;
    }

    public boolean isBetterFitIfSwap(Student s1, Student s2) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        int v1 = 0;
        if (isValidGroup(g1)) {
            v1++;
        }
        if (isValidGroup(g2)) {
            v1++;
        }
        double pv1 = evaluateGroup(g1);
        double pv2 = evaluateGroup(g2);
        Student.swapGroup(s1, s2);
        int v2 = 0;
        if (isValidGroup(g1)) {
            v2++;
        }
        if (isValidGroup(g2)) {
            v2++;
        }
        double cv1 = evaluateGroup(g1);
        double cv2 = evaluateGroup(g2);
        ;
        Student.swapGroup(s1, s2);

        if (v2 > v1) {
            return true;
        }
        if (v2 < v1) {
            return false;
        }
        return cv1 * cv2 > pv1 * pv2;
    }

    public boolean canBeBetterFit(Student s1, Group g2) {
        Group g1 = s1.getGroup();
        int v1 = 0;
        if (isValidGroup(g1)) {
            v1++;
        }
        if (isValidGroup(g2)) {
            v1++;
        }
        g2.add(s1);
        int v2 = 0;
        if (isValidGroup(g1)) {
            v2++;
        }
        if (isValidGroup(g2)) {
            v2++;
        }
        g1.add(s1);

        return v2 > v1;
    }

    public boolean isSameGender() {
        return isSameGender != null && isSameGender;
    }

    public boolean isGenderMatter() {
        return minFemale != null || minMale != null || genderRatio != null;
    }

    public boolean isAgeMatter() {
        return ageDiff != null;
    }

    public boolean isTimeMatter() {
        return timezoneDiff != null;
    }

    public OptionalInt getTimezoneDiff() {
        return this.timezoneDiff == null ? OptionalInt.of(12) : OptionalInt.of(this.timezoneDiff);
    }

    public OptionalInt getAgeDiff() {
        return this.ageDiff == null ? OptionalInt.empty() : OptionalInt.of(this.ageDiff);
    }


    public static class Builder {
        private final int groupSizeLowerBound;
        private final int groupSizeUpperBound;
        private Integer timezoneDiff;
        private Integer ageDiff;
        private Integer minMale;
        private Integer minFemale;
        private Double genderRatio;
        private Double genderErrorMargin;
        private Boolean isSameGender;

        public Builder(int groupSizeLowerBound, int groupSizeUpperBound) {
            this.groupSizeLowerBound = groupSizeLowerBound;
            this.groupSizeUpperBound = groupSizeUpperBound;
        }

        public void setSameGender(Boolean sameGender) {
            isSameGender = sameGender;
        }

        public Builder setMinMale(Integer minMale) {
            assert this.genderErrorMargin == null;
            assert this.genderRatio == null;
            assert this.isSameGender == null;

            this.minMale = minMale;
            return this;
        }

        public Builder setMinFemale(Integer minFemale) {
            assert this.genderErrorMargin == null;
            assert this.genderRatio == null;
            assert this.isSameGender == null;

            this.minFemale = minFemale;
            return this;
        }

        public Builder setGenderRatio(Double genderRatio) {
            assert this.isSameGender == null;
            assert this.minFemale == null;
            assert this.minMale == null;

            this.genderRatio = genderRatio;
            return this;
        }

        public Builder setGenderErrorMargin(Double genderErrorMargin) {
            this.genderErrorMargin = genderErrorMargin;
            return this;
        }

        public Builder setTimezoneDiff(int timezoneDiff) {
            this.timezoneDiff = timezoneDiff;
            return this;
        }

        public Builder setAgeDiff(int ageDiff) {
            this.ageDiff = ageDiff;
            return this;
        }

        public Constraint createConstrain() {
            return new Constraint(this.groupSizeLowerBound, this.groupSizeUpperBound, this.timezoneDiff, this.ageDiff,
                    this.minMale, this.minFemale, this.genderRatio, this.genderErrorMargin, this.isSameGender);
        }

        public Builder setIsSameGender() {
            assert this.genderErrorMargin == null;
            assert this.genderRatio == null;
            assert this.minMale == null;
            assert this.minFemale == null;

            this.isSameGender = true;
            return this;
        }
    }
}
