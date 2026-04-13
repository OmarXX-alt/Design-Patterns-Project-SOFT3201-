package design.pattern.project.service;

/**
 * StructuredPromptBuilder — Output Structuring Pattern for Member 1
 * 
 * This class enforces a specific JSON schema for action item extraction.
 * It wraps user prompts with system instructions that force Azure OpenAI
 * to return structured JSON matching the required schema:
 *   [{ "task": "", "assignee": "", "deadline": "", "priority": "" }]
 * 
 * This is a static utility for building requests that guarantee schema compliance.
 */
public class StructuredPromptBuilder {
    
    /**
     * JSON Schema that MUST be enforced for action item extraction
     * 
     * Schema definition:
     * - Array of action items
     * - Each item has: task (string), assignee (string), deadline (string), priority (string)
     * - All fields are required
     */
    private static final String ACTION_ITEM_SCHEMA = """
        [
          {
            "task": "string - the action item description",
            "assignee": "string - person responsible for this task",
            "deadline": "string - due date in format YYYY-MM-DD",
            "priority": "string - priority level (High, Medium, Low)"
          }
        ]
        """;
    
    /**
     * System prompt that enforces the action item JSON schema
     */
    private static final String SYSTEM_INSTRUCTION = 
        "You are a meeting assistant that extracts action items from notes. " +
        "You MUST respond with ONLY a valid JSON array matching this exact schema and no other text: " +
        ACTION_ITEM_SCHEMA +
        "Important: " +
        "1. Respond with a JSON array ONLY. " +
        "2. Each object must have exactly these fields: task, assignee, deadline, priority. " +
        "3. Do NOT include any markdown code blocks, backticks, or explanatory text. " +
        "4. If a field is missing from notes, infer a reasonable value or use 'TBD'. " +
        "5. Deadline should be in YYYY-MM-DD format. Priority should be High, Medium, or Low.";
    
    /**
     * Wraps a user prompt with system instructions that enforce the action item schema
     * 
     * This method creates a complete request body that, when sent to Azure OpenAI,
     * will force the model to return structured JSON matching the required schema.
     * 
     * @param userPrompt The user's meeting notes to process
     * @return A request body (with system instructions) ready to send to Azure OpenAI
     */
    public static String buildRequest(String userPrompt) {
        // Construct the complete prompt with schema enforcement
        String completePrompt = SYSTEM_INSTRUCTION + "\n\nMeeting Notes:\n" + userPrompt;
        return completePrompt;
    }
    
    /**
     * Get the JSON schema that this builder enforces
     * Useful for documentation and testing
     * 
     * @return The JSON schema string
     */
    public static String getSchema() {
        return ACTION_ITEM_SCHEMA;
    }
    
    /**
     * Get the system instruction template
     * Useful for debugging and logging
     * 
     * @return The system instruction string
     */
    public static String getSystemInstruction() {
        return SYSTEM_INSTRUCTION;
    }
}
