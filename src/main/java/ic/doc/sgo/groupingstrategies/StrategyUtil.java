package ic.doc.sgo.groupingstrategies;

import java.util.Random;

public final class StrategyUtil {

    private StrategyUtil() {
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