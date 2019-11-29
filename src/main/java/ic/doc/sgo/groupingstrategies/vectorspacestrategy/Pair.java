package ic.doc.sgo.groupingstrategies.vectorspacestrategy;

import java.util.Objects;

public class Pair<S, T> {

    private S fst;
    private T snd;

    public Pair(S fst, T snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Pair pair = (Pair) obj;
        return Objects.equals(this.fst, pair.fst) && Objects.equals(this.snd, pair.snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
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
