package sm;

public interface Guard<S extends Stateful<?>> {
    boolean check(S stateful);
}
