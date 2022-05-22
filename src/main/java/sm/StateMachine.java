package sm;

import java.util.EnumSet;

public interface StateMachine<E extends Enum<E>, S extends Stateful<E>> {
    boolean isBegin(E e);

    boolean isEnd(E e);

    StateMachine<E, S> addState(E e);

    StateMachine<E, S> addStates(E[] es);

    StateMachine<E, S> beginStates(EnumSet<E> es);

    StateMachine<E, S> endStates(EnumSet<E> es);

    StateMachine<E, S> addTransition(E src, E dest, Guard<S> guard, Action<S> action, Action<S> errorAction);

    void move(S stateful, E dest);

    EnumSet<E> destinations(E e);

    EnumSet<E> states();

    boolean transitionExists(E src, E dest);
}
