package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.Random;

import ic.doc.sgo.Constraint;

public class Util {
    public static Pair<Integer, Integer> getNumberInterval(int size, int lowerBound, int upperBound) {
        return new Pair<>((int) Math.ceil(1.0 * size / upperBound),
                (int) Math.floor(1.0 * size / lowerBound));
    }

    public static int getRandomIntegerBetween(int a, int b) {
        Random r = new Random();
        return r.nextInt((b - a) + 1) + a;
    }

    public static void assignStudentToGroup(Student student, Group group) {
        group.add(student);
        student.setGroup(group);
    }

    public static void swapGroup(Student s1, Student s2) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        moveStudentToGroup(s1, g2);
        moveStudentToGroup(s2, g1);
    }

    public static boolean isBetterFitIfSwap(Student s1, Student s2, Constraint constraint) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        double pv1 = constraint.evaluateGroup(g1);
        double pv2 = constraint.evaluateGroup(g2);
        swapGroup(s1, s2);
        double cv1 = constraint.evaluateGroup(g1);
        double cv2 = constraint.evaluateGroup(g2);
        boolean res = pv1 + pv2 < cv1 + cv2;
        swapGroup(s1, s2);
        return res;
    }

    public static boolean isSameGroup(Student s1, Student s2) {
        return s1.getGroup() == s2.getGroup();
    }

    public static void moveStudentToGroup(Student student, Group targetGroup) {
        Group originalGroup = student.getGroup();
        originalGroup.remove(student);
        assignStudentToGroup(student, targetGroup);
    }

    static class Pair<S, T> {
        private S fst;
        private T snd;

        public Pair(S fst, T snd) {
            this.fst = fst;
            this.snd = snd;
        }

        public S first() {
            return fst;
        }

        public void setFirst(S fst) {
            this.fst = fst;
        }

        public T second() {
            return snd;
        }

        public void setSecond(T snd) {
            this.snd = snd;
        }

    }
}