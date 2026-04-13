package design.pattern.project.pattern;

import design.pattern.project.model.ActionItem;

public interface TaskEventListener {
    void onTaskCreated(ActionItem item);
}