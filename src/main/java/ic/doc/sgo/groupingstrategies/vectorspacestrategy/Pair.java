package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

public class Pair<S, T> {
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
