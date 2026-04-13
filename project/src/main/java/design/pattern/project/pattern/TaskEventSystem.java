package design.pattern.project.pattern;

import java.util.ArrayList;
import java.util.List;

import design.pattern.project.model.ActionItem;

public class TaskEventSystem {
    private final List<TaskEventListener> listeners = new ArrayList<>();

    public void register(TaskEventListener listener) {
        listeners.add(listener);
    }

    public void remove(TaskEventListener listener) {
        listeners.remove(listener);
    }

    public void notifyAll(ActionItem item) {
        for (TaskEventListener listener : listeners) {
            listener.onTaskCreated(item);
        }
    }
}