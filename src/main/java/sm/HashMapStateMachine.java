package sm;

import java.util.*;

public class HashMapStateMachine<E extends Enum<E>, S extends Stateful<E>> implements StateMachine<E, S> {

    private final Map<E, EnumSet<E>> states = new HashMap<>();
    private final Map<E, Map<E, Guard<S>>> guards = new HashMap<>();
    private final Map<E, Map<E, Action<S>>> actions = new HashMap<>();
    private final Map<E, Map<E, Action<S>>> errorActions = new HashMap<>();

    private EnumSet<E> beginStates = null;
    private EnumSet<E> endStates = null;

    @Override
    public boolean isBegin(E e) {
        return beginStates != null && beginStates.contains(e);
    }

    @Override
    public boolean isEnd(E e) {
        return endStates != null && endStates.contains(e);
    }

    @Override
    public StateMachine<E, S> addState(E e) {
        states.put(e, EnumSet.of(e));
        return this;
    }

    @Override
    public StateMachine<E, S> addStates(E[] es) {
        Arrays.stream(es).forEach(this::addState);
        return this;
    }

    @Override
    public StateMachine<E, S> beginStates(EnumSet<E> es) {
        beginStates = Objects.requireNonNull(es);
        return this;
    }

    @Override
    public StateMachine<E, S> endStates(EnumSet<E> es) {
        endStates = Objects.requireNonNull(es);
        return this;
    }

    @Override
    public StateMachine<E, S> addTransition(E src, E dest, Guard<S> guard, Action<S> action, Action<S> errorAction) {
        states.computeIfAbsent(src, e -> EnumSet.of(src)).add(dest);
        guards.computeIfAbsent(src, e -> new HashMap<>()).put(dest, guard);
        actions.computeIfAbsent(src, e -> new HashMap<>()).put(dest, action);
        errorActions.computeIfAbsent(src, e -> new HashMap<>()).put(dest, errorAction);
        return this;
    }

    @Override
    public void move(S stateful, E dest) {
        E src = stateful.getState();
        if (!transitionExists(src, dest)) {
            throw new IllegalArgumentException("The transition is not registered: " + src + " -> " + dest);
        } else {
            if (!guards.get(src).get(dest).check(stateful)) {
                errorActions.get(src).get(dest).apply(stateful);
            } else {
                stateful.setState(dest);
                actions.get(src).get(dest).apply(stateful);
            }
        }
    }

    @Override
    public EnumSet<E> destinations(E e) {
        return states.computeIfAbsent(e, EnumSet::of);
    }

    @Override
    public EnumSet<E> states() {
        return EnumSet.copyOf(states.keySet());
    }

    @Override
    public boolean transitionExists(E src, E dest) {
        return destinations(src).contains(dest);
    }
}
