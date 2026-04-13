package design.pattern.project.model;


public class ActionItem {
    private final String title;
    private final String assignee;
    private final String deadline;
    private final String priority;
    private final String status;


    protected ActionItem(String title, String assignee, String deadline, String priority, String status) {
        this.title = title;
        this.assignee = assignee;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
    }
    
    public String getTitle() {
        return title;
    }
    public String getAssignee() {
        return assignee;
    }
    public String getDeadline() {
        return deadline;
    }
    public String getPriority() {
        return priority;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ActionItem [title=" + title + ", assignee=" + assignee + ", deadline=" + deadline + ", priority="
                + priority + ", status=" + status + "]";
    }

}