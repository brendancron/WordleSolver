package wordle;

public class Tuple<F, S> {

    public final F fst;
    public final S snd;

    public Tuple(F fst, S snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public String toString() {
        return "<" + fst.toString() + ", " + snd.toString() + ">";
    }

}
