package playground;

import lombok.Data;
import sm.HashMapStateMachine;
import sm.StateMachine;
import sm.Stateful;

import java.util.EnumSet;

public class Playground {

    @Data
    public static class Task implements Stateful<TaskState> {
        private TaskState state = TaskState.TODO;
    }

    public enum TaskState {
        BACKLOG, TODO, IN_PROGRESS, DONE, ARCHIVED
    }

    public static void main(String[] args) {

        //configuring the transitions
        StateMachine<TaskState, Task> taskStateMachine = new HashMapStateMachine<TaskState, Task>()
                .beginStates(EnumSet.of(TaskState.BACKLOG))
                .endStates(EnumSet.of(TaskState.ARCHIVED))
                .addStates(TaskState.values())
                .addTransition(TaskState.BACKLOG,   // source state
                        TaskState.TODO,             // target state
                        task -> true,               // provide extra validations on transition
                        task -> {},                 // what to do on success
                        task -> {});                // what to do on error


        Task task = new Task();
        taskStateMachine.move(task, TaskState.IN_PROGRESS);
    }
}
