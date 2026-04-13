package design.pattern.project;

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
    private final String azureEndpoint;
    
    /**
     * Private constructor - prevents instantiation from outside the class
     * Initializes the OkHttp client and retrieves the Azure API key from environment
     */
    private AzureOpenAIClient() {
        this.httpClient = new OkHttpClient();
        this.apiKey = System.getenv("AZURE_API_KEY");
        this.azureEndpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
        
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("AZURE_API_KEY environment variable is not set");
        }
        if (this.azureEndpoint == null || this.azureEndpoint.isEmpty()) {
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
     * // TODO: Integration Point - Member 1 to implement Output Structuring wrap here
     * 
     * @param prompt The prompt string to send to Azure OpenAI
     * @return Raw JSON response as a String
     * @throws IOException If there's an error with the HTTP request
     */
    public String sendPrompt(String prompt) throws IOException {
        // TODO: Integration Point - Member 1 to implement Output Structuring wrap here
        
        // Create the request body with the prompt
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{\"prompt\": \"" + escapeJson(prompt) + "\"}";
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        
        // Build the HTTP request
        Request request = new Request.Builder()
                .url(this.azureEndpoint)
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
