package design.pattern.project.cli;

import design.pattern.project.model.ActionItem;
import design.pattern.project.pattern.*;
import design.pattern.project.service.MeetingAssistantFacade;

import java.util.List;
import java.util.Scanner;

/**
 * CLI entry point. Wires together all three members' components.
 *
 * Assembly order:
 *   1. Create TaskEventSystem (M2)
 *   2. Register ConsoleTaskListener and InMemoryTaskRepository (M2)
 *   3. Create MeetingAssistantFacade with injected TaskEventSystem (M3/M1)
 *   4. Accept user input and chosen strategy
 *   5. Call processNotes() and display results
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ── 1. Observer setup ──────────────────────────────────────────────
        TaskEventSystem eventSystem = new TaskEventSystem();
        ConsoleTaskListener consoleListener = new ConsoleTaskListener();
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        eventSystem.register(consoleListener);
        eventSystem.register(repository);

        // ── 2. Facade setup ────────────────────────────────────────────────
        MeetingAssistantFacade facade = new MeetingAssistantFacade(eventSystem);

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   Meeting Notes → Action Items Assistant     ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\nOptions:");
            System.out.println("  1 - Process new meeting notes");
            System.out.println("  2 - View all stored tasks");
            System.out.println("  3 - Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> processNotes(scanner, facade);
                case "2" -> viewAllTasks(repository);
                case "3" -> running = false;
                default  -> System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    // ── Paste notes & choose strategy ─────────────────────────────────────
    private static void processNotes(Scanner scanner, MeetingAssistantFacade facade) {
        // ── 3. Strategy selection ──────────────────────────────────────────
        System.out.println("\nSelect extraction strategy:");
        System.out.println("  1 - Formal (structured board minutes — strict extraction)");
        System.out.println("  2 - Casual (informal team notes — lenient extraction)");
        System.out.print("Choice: ");
        String stratChoice = scanner.nextLine().trim();

        ExtractionStrategy strategy;
        if ("2".equals(stratChoice)) {
            strategy = new CasualNotesStrategy();
            System.out.println("Using: Casual Notes Strategy");
        } else {
            strategy = new FormalMeetingStrategy();
            System.out.println("Using: Formal Meeting Strategy");
        }

        // ── 4. Notes input ─────────────────────────────────────────────────
        System.out.println("\nPaste your meeting notes below.");
        System.out.println("Press ENTER on a blank line when done:");
        StringBuilder notes = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).isBlank()) {
            notes.append(line).append("\n");
        }

        if (notes.toString().isBlank()) {
            System.out.println("No notes entered. Returning to menu.");
            return;
        }

        // ── 5. Processing ──────────────────────────────────────────────────
        System.out.println("\nProcessing... (calling Azure OpenAI)\n");
        List<ActionItem> items = facade.processNotes(notes.toString(), strategy);

        // ── 6. Summary table ───────────────────────────────────────────────
        if (items.isEmpty()) {
            System.out.println("No action items extracted. Check AI response or try different notes.");
        } else {
            System.out.println("\n─── Extracted Action Items ─────────────────────────────────");
            System.out.printf("%-4s %-35s %-15s %-12s %-8s%n",
                    "#", "Task", "Assignee", "Deadline", "Priority");
            System.out.println("─".repeat(78));
            int i = 1;
            for (ActionItem item : items) {
                System.out.printf("%-4d %-35s %-15s %-12s %-8s%n",
                        i++,
                        truncate(item.getTitle(), 34),
                        orDash(item.getAssignee()),
                        orDash(item.getDeadline()),
                        orDash(item.getPriority()));
            }
            System.out.println("─".repeat(78));
            System.out.println("Total: " + items.size() + " action item(s) extracted.");
        }
    }

    // ── View stored tasks ──────────────────────────────────────────────────
    private static void viewAllTasks(InMemoryTaskRepository repository) {
        List<ActionItem> all = repository.getAllTasks();
        System.out.println("\n─── All Stored Tasks (" + all.size() + ") ──────────────────────────────");
        if (all.isEmpty()) {
            System.out.println("  No tasks stored yet.");
        } else {
            int i = 1;
            for (ActionItem item : all) {
                System.out.printf("  %d. [%s] %s — %s (due: %s)%n",
                        i++,
                        orDash(item.getPriority()),
                        item.getTitle(),
                        orDash(item.getAssignee()),
                        orDash(item.getDeadline()));
            }
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "-";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String orDash(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}

