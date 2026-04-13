package design.pattern.project.service;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Singleton Pattern Implementation for Azure OpenAI Client
 * 
 * This class demonstrates the Singleton design pattern by ensuring only one instance
 * of the AzureOpenAIClient is created and reused throughout the application.
 */
public class AzureOpenAIClient {
    
    private static AzureOpenAIClient instance;
    private final OkHttpClient httpClient;
    private final String apiKey;
    private final String endpoint = "https://60099-m1xc2jq0-australiaeast.openai.azure.com/";
    private final String deployment = "gpt-5-mini-vanilson";
    
    /**
     * Private constructor - prevents instantiation from outside the class
     * Initializes the OkHttp client and retrieves the Azure API key from environment
     */
    private AzureOpenAIClient() {
        this.httpClient = new OkHttpClient();
        this.apiKey = System.getenv("AZURE_API_KEY");
        
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("AZURE_API_KEY environment variable is not set");
        }
        if (this.endpoint == null || this.endpoint.isEmpty()) {
            throw new IllegalStateException("AZURE_OPENAI_ENDPOINT environment variable is not set");
        }
    }
    
    /**
     * getInstance() - Returns the singleton instance of AzureOpenAIClient
     * Uses synchronized block to ensure thread-safe lazy initialization
     * 
     * @return The singleton instance of AzureOpenAIClient
     */
    public static synchronized AzureOpenAIClient getInstance() {
        if (instance == null) {
            instance = new AzureOpenAIClient();
        }
        return instance;
    }
    
    /**
     * sendPrompt(String prompt) - Sends a prompt to the Azure OpenAI endpoint
     * Returns the raw JSON response from the API
     * 
     * Integrates the Output Structuring pattern by enforcing JSON schema through system prompts
     * 
     * @param prompt The prompt string to send to Azure OpenAI
     * @return Raw JSON response as a String
     * @throws IOException If there's an error with the HTTP request
     */
    public String sendPrompt(String prompt) throws IOException {
        // TODO: Integration Point - Member 1 to implement Output Structuring wrap here
        
        // Output Structuring Pattern Integration: Build system prompt enforcing JSON schema
        String systemPrompt = StructuredPromptBuilder.buildRequest(prompt);
        
        // Create the request body with the prompt and system instructions
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{" +
                "\"messages\": [" +
                "{\"role\": \"system\", \"content\": \"" + escapeJson(systemPrompt) + "\"}," +
                "{\"role\": \"user\", \"content\": \"" + escapeJson(prompt) + "\"}" +
                "]," +
                "\"temperature\": 0.7," +
                "\"max_tokens\": 1024" +
                "}";
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        String url = endpoint + "openai/deployments/" + deployment + "/chat/completions?api-version=2024-02-01";
        
        // Build the HTTP request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("api-key", this.apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        
        // Execute the request and return the raw JSON response
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            return response.body().string();
        }
    }
    
    /**
     * Helper method to escape special characters in JSON strings
     * 
     * @param text The text to escape
     * @return Escaped JSON string
     */
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
