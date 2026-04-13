package design.pattern.project.pattern;

/**
 * Concrete Strategy for informal / casual team notes.
 * Produces a lenient prompt that infers likely tasks even when
 * no explicit owner or deadline is mentioned.
 */
public class CasualNotesStrategy implements ExtractionStrategy {

    @Override
    public String buildPrompt(String notes) {
        return "You are processing informal team notes. "
             + "Extract likely action items even if no owner or deadline is explicitly stated. "
             + "Make reasonable inferences about priority (HIGH, MEDIUM, LOW) based on context. "
             + "If no owner is mentioned, set assignee to null. "
             + "If no deadline is mentioned, set deadline to null.\n\n"
             + "Team notes:\n" + notes;
    }
}
