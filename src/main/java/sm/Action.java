package sm;

public interface Action<S extends Stateful<?>> {
    void apply(S stateful);
}
