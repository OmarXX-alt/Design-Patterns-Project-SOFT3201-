package design.pattern.project.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import design.pattern.project.model.ActionItem;

public class InMemoryTaskRepository implements TaskEventListener {
    private final List<ActionItem> tasks = new ArrayList<>();

    @Override
    public void onTaskCreated(ActionItem item) {
        tasks.add(item);
    }

    public List<ActionItem> getAllTasks() {
        // Return an unmodifiable list to protect internal state
        return Collections.unmodifiableList(tasks);
    }
}