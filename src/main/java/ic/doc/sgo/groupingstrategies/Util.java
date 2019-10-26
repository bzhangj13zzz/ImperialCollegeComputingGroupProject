package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import ic.doc.sgo.groupingstrategies.vectorSpaceStrategies.Cluster;
import ic.doc.sgo.groupingstrategies.vectorSpaceStrategies.Node;

import java.util.Random;

public final class Util {

    private Util() {
    }

    public static Pair<Integer, Integer> getNumberInterval(int size, int lowerBound, int upperBound) {
        return new Pair<>((int) Math.ceil(1.0 * size / upperBound),
                (int) Math.floor(1.0 * size / lowerBound));
    }

    public static int getRandomIntegerBetween(int a, int b) {
        Random r = new Random();
        if (a > b) {
            return b;
        }
        return r.nextInt((b - a) + 1) + a;
    }

    public static void swapGroup(Student s1, Student s2) {
        Group g1 = s1.getGroup();
        Group g2 = s2.getGroup();
        g2.add(s1);
        g1.add(s2);
    }


    public static boolean isSameGroup(Student s1, Student s2) {
        return s1.getGroup() == s2.getGroup();
    }

    public static void swapCluster(Node n1, Node n2) {
        Cluster c1 = n1.getCluster();
        Cluster c2 = n2.getCluster();
        c1.add(n1);
        c2.add(n2);
    }

    public static int booleanToInt(boolean value) {
        return value? 1: 0;
    }

    public static class Pair<S, T> {
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