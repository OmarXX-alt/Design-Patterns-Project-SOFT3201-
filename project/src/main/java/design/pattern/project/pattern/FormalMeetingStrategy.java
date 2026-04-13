package design.pattern.project.pattern;

/**
 * Concrete Strategy for formal, structured meeting minutes.
 * Produces a strict prompt that insists on explicit owners and deadlines.
 */
public class FormalMeetingStrategy implements ExtractionStrategy {

    @Override
    public String buildPrompt(String notes) {
        return "You are processing formal board meeting minutes. "
             + "Extract EVERY action item. For each one you MUST identify: "
             + "a named owner responsible for the task, a concrete deadline, "
             + "and a priority level (HIGH, MEDIUM, or LOW). "
             + "Do not infer or guess — only include items explicitly stated.\n\n"
             + "Meeting minutes:\n" + notes;
    }
}