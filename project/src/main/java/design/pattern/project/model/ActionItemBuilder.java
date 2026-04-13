package design.pattern.project.model;

public class ActionItemBuilder {
    private String title;
    private String assignee = "None";
    private String deadline = "None";
    private String priority = "None";
    private String status = "None";

    public ActionItemBuilder title(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
        return this;
    }
    public ActionItemBuilder assignee(String assignee) {
        this.assignee = assignee;
        return this;
    }
    public ActionItemBuilder deadline(String deadline) {
        this.deadline = deadline;
        return this;
    }
    public ActionItemBuilder priority(String priority) {
        this.priority = priority;
        return this;
    }
    public ActionItemBuilder status(String status) {
        this.status = status;
        return this;
    }
    public ActionItem build() {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return new ActionItem(title, assignee, deadline, priority, status);
    }
}