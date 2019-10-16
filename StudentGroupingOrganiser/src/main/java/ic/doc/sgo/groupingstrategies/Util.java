package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Group;
import ic.doc.sgo.Student;

import java.util.Random;

public class Util {
    public static Pair<Integer, Integer> getNumberInterval(int size, int lowerBound, int upperBound) {
        return new Pair<> ((int) Math.ceil(1.0 * size / upperBound),
                           (int) Math.floor(1.0 * size / lowerBound));
    }
    public static int getRandomIntegerBetween(int a, int b) {
        Random r = new Random();
        return r.nextInt((b - a) + 1) + a;
    }

    public static void assignStudentToGroup(Student student, Group group) {
        group.add(student);
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
