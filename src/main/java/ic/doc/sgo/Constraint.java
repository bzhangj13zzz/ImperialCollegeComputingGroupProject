package ic.doc.sgo;


import ic.doc.sgo.groupingstrategies.vectorspacestrategy.Pair;

import java.time.ZoneId;
import java.util.*;
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
    private final Map<String, Pair<Integer, Integer>> discreteAttributeConstraints;

    private Constraint(int groupSizeLowerBound, int groupSizeUpperBound, Integer timezoneDiff,
        Integer ageDiff,
        Integer minMale, Integer minFemale, Double genderRatio, Double genderErrorMargin,
        Boolean isSameGender, Map<String, Pair<Integer, Integer>> additionalDiscreteAttribute) {
        this.groupSizeLowerBound = groupSizeLowerBound;
        this.groupSizeUpperBound = groupSizeUpperBound;
        this.timezoneDiff = timezoneDiff;
        this.ageDiff = ageDiff;
        this.minMale = minMale;
        this.minFemale = minFemale;
        this.genderRatio = genderRatio;
        this.genderErrorMargin = genderErrorMargin;
        this.isSameGender = isSameGender;
        this.discreteAttributeConstraints = additionalDiscreteAttribute;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Constraint constraint = (Constraint) obj;
        return this.groupSizeLowerBound == constraint.groupSizeLowerBound
            && this.groupSizeUpperBound == constraint.groupSizeUpperBound
            && Objects.equals(this.timezoneDiff, constraint.timezoneDiff)
            && Objects.equals(this.ageDiff, constraint.ageDiff)
            && Objects.equals(this.minMale, constraint.minMale)
            && Objects.equals(this.minFemale, constraint.minFemale)
            && Objects.equals(this.genderRatio, constraint.genderRatio)
            && Objects.equals(this.genderErrorMargin, constraint.genderErrorMargin)
            && Objects.equals(this.isSameGender, constraint.isSameGender)
            && Objects
            .equals(this.discreteAttributeConstraints, constraint.discreteAttributeConstraints);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(groupSizeLowerBound, groupSizeUpperBound, timezoneDiff, ageDiff, minMale,
                minFemale,
                genderRatio, genderErrorMargin, isSameGender, discreteAttributeConstraints);
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
            ", additionalDiscreteAttribute=" + discreteAttributeConstraints +
            '}';
    }

    public int getMinMale() {
        assert isGenderNumberMatter();
        if (minMale == null) {
            return 0;
        }
        return minMale;
    }

    public int getMinFemale() {
        assert isGenderNumberMatter();
        if (minFemale == null) {
            return 0;
        }
        return minFemale;
    }

    public double getGenderRatio() {
        assert isGenderRatioMatter();
        if (genderRatio == null) {
            return 0.5;
        }

        return genderRatio;
    }

    public double getGenderErrorMargin() {
        assert isGenderRatioMatter();
        if (genderErrorMargin == null) {
            return 0;
        }
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
                res = Math
                    .max(res, TimeZoneCalculator.timeBetween(s1TimeZone.get(), s2TimeZone.get()));
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

        if (isGenderNumberMatter() && (getMinFemale() > getFemaleNumberOfGroup(group)
            || getMinMale() > getMaleNumberOfGroup(group))) {
            return false;
        }

        if (isGenderRatioMatter()) {
            double currentRatio = 1.0 * getMaleNumberOfGroup(group) / group.size();
            if (currentRatio < getGenderRatio() - getGenderErrorMargin() ||
                currentRatio > getGenderRatio() + getGenderErrorMargin()) {
                System.out.println("The current gender ratio is " + currentRatio);
                return false;
            }
        }

        if (isSameGender() && !isGroupSameGender(group)) {
            return false;
        }

        if (isDiscreteAttributesConstraintsMatter()) {
            for (String attribute : discreteAttributeConstraints.keySet()) {
                Pair<Integer, Integer> range = discreteAttributeConstraints.get(attribute);
                for (String type : getTypesOfDiscreteAttributeFromGroup(group, attribute)) {
                    int number = getNumberOfAdditionalAttributeTypeInGroup(group, attribute, type);
                    if (number < range.first() || number > range.second()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private List<String> getTypesOfDiscreteAttributeFromGroup(Group group, String attribute) {
        List<String> res = new ArrayList<>();
        for (Student student : group.getStudents()) {
            String type = student.getAttribute(attribute).orElse("");
            if (!type.equals("") && !type.equals("false") && !res.contains(type)) {
                res.add(type);
            }
        }
        return res;
    }

    private Integer getNumberOfAdditionalAttributeTypeInGroup(Group group, String attribute,
        String type) {
        int res = 0;
        for (Student student : group.getStudents()) {
            if (student.getAttribute(attribute).orElse("").equals(type)) {
                res++;
            }
        }
        return res;
    }


    public boolean isGroupSameGender(Group group) {
        List<Student> students = group.getStudents();
        for (Student student : students) {
            if (!student.getGender().orElse("male")
                .equals(students.get(0).getGender().orElse("male"))) {
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

    public boolean isSameGender() {
        return isSameGender != null && isSameGender;
    }

    public boolean isGenderNumberMatter() {
        return minFemale != null || minMale != null;
    }

    public boolean isGenderRatioMatter() {
        return genderRatio != null || genderErrorMargin != null;
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

    public Map<String, Pair<Integer, Integer>> getDiscreteAttributes() {
        return this.discreteAttributeConstraints;
    }

    public Pair<Integer, Integer> getRangeOfDiscreteAttributeConstraints(String attribute) {
        assert this.discreteAttributeConstraints.containsKey(attribute);
        return this.discreteAttributeConstraints.get(attribute);
    }

    public boolean isDiscreteAttributesConstraintsMatter() {
        return this.discreteAttributeConstraints != null;
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
        private Map<String, Pair<Integer, Integer>> discreteAttributeConstraints = new HashMap<>();

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
            return new Constraint(this.groupSizeLowerBound, this.groupSizeUpperBound,
                this.timezoneDiff, this.ageDiff,
                this.minMale, this.minFemale, this.genderRatio, this.genderErrorMargin,
                this.isSameGender,
                this.discreteAttributeConstraints);
        }

        public Builder setIsSameGender() {
            assert this.genderErrorMargin == null;
            assert this.genderRatio == null;
            assert this.minMale == null;
            assert this.minFemale == null;

            this.isSameGender = true;
            return this;
        }

        public Builder addDiscreteAttributeConstraint(String key, Pair<Integer, Integer> value) {
            this.discreteAttributeConstraints.put(key, value);
            return this;
        }

        public Builder setDiscreteAttributeConstraints(
            Map<String, Pair<Integer, Integer>> discreteAttributeConstraints) {
            this.discreteAttributeConstraints = discreteAttributeConstraints;
            return this;
        }
    }
}
