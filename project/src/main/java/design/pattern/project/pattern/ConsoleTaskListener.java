package design.pattern.project.pattern;

import design.pattern.project.model.ActionItem;

public class ConsoleTaskListener implements TaskEventListener {
    @Override
    public void onTaskCreated(ActionItem item) {
        System.out.println("\n[EVENT] New Task Created Successfully:");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("| %-15s | %-10s | %-10s | %-10s |%n", 
            "Title", "Assignee", "Priority", "Status");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("| %-15s | %-10s | %-10s | %-10s |%n", 
            item.getTitle(), item.getAssignee(), item.getPriority(), item.getStatus());
        System.out.println("------------------------------------------------------------------\n");
    }
}