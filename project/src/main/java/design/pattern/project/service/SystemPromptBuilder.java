package design.pattern.project.service;

/**
 * SystemPromptBuilder - Builds system prompts that enforce JSON schema output
 * 
 * This builder pattern class constructs system prompts that instruct the Azure OpenAI model
 * to return structured JSON responses according to a defined schema.
 * This is the output structuring pattern implementation for the prototype.
 */
public class SystemPromptBuilder {
    
    private StringBuilder promptBuilder;
    
    /**
     * Create a new SystemPromptBuilder
     */
    public SystemPromptBuilder() {
        this.promptBuilder = new StringBuilder();
    }
    
    /**
     * Add the core instruction for JSON formatted responses
     * @return this builder for method chaining
     */
    public SystemPromptBuilder withJsonFormatting() {
        promptBuilder.append("You MUST respond with valid JSON only. ");
        return this;
    }
    
    /**
     * Add schema enforcement instructions
     * @return this builder for method chaining
     */
    public SystemPromptBuilder withSchemaEnforcement() {
        promptBuilder.append("Your response MUST strictly follow this JSON schema: ")
                .append(OutputStructure.getJsonSchema())
                .append(" ");
        return this;
    }
    
    /**
     * Add required fields instructions
     * @return this builder for method chaining
     */
    public SystemPromptBuilder withRequiredFields() {
        promptBuilder.append("Required fields: 'response' (string) and 'confidence' (number 0-1). ")
                .append("Optional fields: 'tags' (array of strings). ");
        return this;
    }
    
    /**
     * Add validation instructions
     * @return this builder for method chaining
     */
    public SystemPromptBuilder withValidation() {
        promptBuilder.append("Ensure all required fields are present and properly typed. ")
                .append("Do not include any text before or after the JSON object. ");
        return this;
    }
    
    /**
     * Add confidence scoring instructions
     * @return this builder for method chaining
     */
    public SystemPromptBuilder withConfidenceGuidance() {
        promptBuilder.append("Assign a confidence value from 0 to 1 based on how certain you are about the response. ")
                .append("Higher values indicate higher confidence. ");
        return this;
    }
    
    /**
     * Build the complete system prompt with all instructions
     * @return the complete system prompt string
     */
    public String buildCompletely() {
        return withJsonFormatting()
                .withSchemaEnforcement()
                .withRequiredFields()
                .withValidation()
                .withConfidenceGuidance()
                .build();
    }
    
    /**
     * Build the system prompt from accumulated instructions
     * @return the final system prompt string
     */
    public String build() {
        return promptBuilder.toString();
    }
}
