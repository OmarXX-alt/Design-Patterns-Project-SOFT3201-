package design.pattern.project.service;

public class AzureOpenAIClient {
    private String endpoint;
    private String deployment;
    private String apiKey;
    private String modelName;

    public AzureOpenAIClient() {
        this.endpoint = "https://60099-m1xc2jq0-australiaeast.openai.azure.com/";
        this.deployment = "gpt-5-mini-vanilson";
        this.modelName = "gpt-5-mini";
        this.apiKey = System.getenv("AZURE_API_KEY");
    }

    public String sendPrompt(String prompt) {
        String url = endpoint + "openai/deployments/" + deployment + "/chat/completions?api-version=2024-02-01";
        String body = "{\n" +
                "  \"messages\": [{\n" +
                "    \"role\": \"user\",\n" +
                "    \"content\": \"" + escapeJson(prompt) + "\"\n" +
                "  }]\n" +
                "}";
        return sendRequest(url, body);
    }

    private String sendRequest(String url, String body) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("Azure API key not configured");
        }
        // Implementation for sending HTTP request would go here
        return "Response from Azure OpenAI";
    }

    private String escapeJson(String value) {
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getDeployment() {
        return deployment;
    }
}