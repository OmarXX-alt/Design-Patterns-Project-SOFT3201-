package design.pattern.project.service;

/**
 * OutputStructure - Defines the JSON schema structure for Azure OpenAI responses
 * 
 * This class represents the expected JSON structure that the Azure OpenAI API
 * should return, enforced through the system prompt.
 */
public class OutputStructure {
    
    /**
     * JSON Schema for the expected response format
     * Enforces structured output from the Azure OpenAI API
     */
    private static final String JSON_SCHEMA = """
        {
          "type": "object",
          "properties": {
            "response": {
              "type": "string",
              "description": "The main response text"
            },
            "confidence": {
              "type": "number",
              "description": "Confidence level of the response (0-1)",
              "minimum": 0,
              "maximum": 1
            },
            "tags": {
              "type": "array",
              "items": {
                "type": "string"
              },
              "description": "Relevant tags or categories"
            }
          },
          "required": ["response", "confidence"]
        }
        """;
    
    private String response;
    private Double confidence;
    private java.util.List<String> tags;
    
    public OutputStructure() {
    }
    
    public OutputStructure(String response, Double confidence, java.util.List<String> tags) {
        this.response = response;
        this.confidence = confidence;
        this.tags = tags;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public java.util.List<String> getTags() {
        return tags;
    }
    
    public void setTags(java.util.List<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Get the JSON schema that defines the expected output structure
     * @return The JSON schema as a string
     */
    public static String getJsonSchema() {
        return JSON_SCHEMA;
    }
}
