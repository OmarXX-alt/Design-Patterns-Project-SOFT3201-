package design.pattern.project.pattern;

/**
 * Strategy Pattern — ExtractionStrategy interface.
 * Defines the contract for all prompt-building strategies.
 * The Facade depends only on this interface, not on any concrete class.
 */
public interface ExtractionStrategy {
    /**
     * Builds a user-facing prompt string from raw meeting notes.
     * @param notes raw meeting notes pasted by the user
     * @return prompt string to be passed to StructuredPromptBuilder
     */
    String buildPrompt(String notes);
}
