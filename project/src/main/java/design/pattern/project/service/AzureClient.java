package design.pattern.project.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Singleton Pattern Implementation for Azure OpenAI Client
 * 
 * This class demonstrates the Singleton design pattern by ensuring only one instance
 * of the AzureClient is created and reused throughout the application.
 */
public class AzureClient {
    
    private static AzureClient instance;
    private final OkHttpClient httpClient;
    private final String apiKey;
    private final String endpoint = "https://60099-m1xc2jq0-australiaeast.openai.azure.com/";
    private final String deployment = "gpt-5-mini-vanilson";
    
    /**
     * Private constructor - prevents instantiation from outside the class
     * Initializes the OkHttp client and retrieves the Azure API key from environment
     */
    private AzureClient() {
        // Load environment variables from .env file using java-dotenv
        // Configure to look for .env in the project subdirectory (same as APIKeyTest)
        Dotenv dotenv = Dotenv.configure()
            .directory("./project")  // Match the directory used in APIKeyTest
            .ignoreIfMissing()  // Don't fail if .env doesn't exist
            .load();
        
        // Initialize OkHttpClient with custom timeout settings for Azure API
        // Default timeout (10s) may be too short for LLM inference, so we increase it
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Time to establish connection
            .readTimeout(60, TimeUnit.SECONDS)     // Time to read response (LLM inference can take time)
            .writeTimeout(30, TimeUnit.SECONDS)    // Time to write request
            .build();
        this.apiKey = dotenv.get("AZURE_API_KEY");
        
        if (this.apiKey == null || this.apiKey.isEmpty() || this.apiKey.equals("key-placeholder")) {
            throw new IllegalStateException(
                "❌ AZURE_API_KEY is not configured!\n" +
                "   Please set a valid API key in the .env file:\n" +
                "   1. Create .env file in project root (same folder as pom.xml)\n" +
                "   2. Add: AZURE_API_KEY=your-actual-api-key\n" +
                "   3. Restart the application"
            );
        }
        if (this.endpoint == null || this.endpoint.isEmpty()) {
            throw new IllegalStateException("AZURE_OPENAI_ENDPOINT is not set");
        }
    }
    
    /**
     * getInstance() - Returns the singleton instance of AzureClient
     * Uses synchronized block to ensure thread-safe lazy initialization
     * 
     * @return The singleton instance of AzureClient
     */
    public static synchronized AzureClient getInstance() {
        if (instance == null) {
            instance = new AzureClient();
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
        
        // Build the complete prompt with schema enforcement (combines user prompt + system instructions)
        String fullPrompt = StructuredPromptBuilder.buildRequest(prompt);
        
        // Create the request body as a proper JSON message format for Azure OpenAI
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        
        // Build JSON using proper structure (not string concatenation)
        String jsonBody = "{\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + escapeJson(fullPrompt) + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"max_completion_tokens\": 2048\n" +
                "}";
        
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        String url = endpoint + "openai/deployments/" + deployment + "/chat/completions?api-version=2024-02-01";
        
        // Build the HTTP request with proper headers
        Request request = new Request.Builder()
                .url(url)
                .addHeader("api-key", this.apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        
        // Execute the request and return the raw JSON response
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("[AzureClient] HTTP Error: " + response.code() + " " + response.message());
                System.err.println("[AzureClient] Response body: " + (response.body() != null ? response.body().string() : "No body"));
                throw new IOException("Azure API error: Code " + response.code() + " - " + response.message());
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
