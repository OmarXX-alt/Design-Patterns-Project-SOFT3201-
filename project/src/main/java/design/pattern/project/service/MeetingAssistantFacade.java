package design.pattern.project.service;

import design.pattern.project.model.ActionItem;
import design.pattern.project.model.ActionItemBuilder;
import design.pattern.project.pattern.ExtractionStrategy;
import design.pattern.project.pattern.TaskEventSystem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade Pattern — MeetingAssistantFacade.
 *
 * Hides the full pipeline complexity behind a single method.
 * The CLI calls processNotes() and receives a List<ActionItem>
 * without knowing anything about HTTP, JSON, or the Observer system.
 *
 * Internal flow:
 *   strategy.buildPrompt()
 *     → StructuredPromptBuilder wraps with JSON schema enforcement
 *     → AzureOpenAIClient.sendPrompt() (Singleton)
 *     → JSON parsed into ActionItem objects via ActionItemBuilder (Builder)
 *     → taskEventSystem.notifyAll() fires for each task (Observer)
 *     → List<ActionItem> returned to CLI
 */
public class MeetingAssistantFacade {

    private final AzureOpenAIClient aiClient;
    private final TaskEventSystem taskEventSystem;

    /**
     * Constructor — TaskEventSystem is injected so listeners can be registered
     * externally in Main.java before any processing starts.
     */
    public MeetingAssistantFacade(TaskEventSystem taskEventSystem) {
        this.aiClient = AzureOpenAIClient.getInstance();
        this.taskEventSystem = taskEventSystem;
    }

    /**
     * Main public method — the only entry point the CLI ever calls.
     *
     * @param notes    raw meeting notes from user input
     * @param strategy the chosen ExtractionStrategy (Formal or Casual)
     * @return list of extracted ActionItem objects
     */
    public List<ActionItem> processNotes(String notes, ExtractionStrategy strategy) {
        List<ActionItem> results = new ArrayList<>();

        try {
            // Step 1: Strategy builds the user message portion of the prompt
            String userPrompt = strategy.buildPrompt(notes);

            // Step 2: StructuredPromptBuilder wraps with JSON schema enforcement
            String fullRequestBody = StructuredPromptBuilder.buildRequest(userPrompt);

            // Step 3: Singleton AI client sends the HTTP request
            String rawJson = aiClient.sendPrompt(fullRequestBody);

            // Step 4: Parse JSON response into ActionItem objects using Builder
            results = parseResponse(rawJson);

            // Step 5: Observer — notify all registered listeners for each task
            for (ActionItem item : results) {
                taskEventSystem.notifyAll(item);
            }

        } catch (Exception e) {
            System.err.println("[Facade] Error during processing: " + e.getMessage());
        }

        return results;
    }

    /**
     * Parses the raw JSON string from the AI into a list of ActionItem objects.
     * Uses ActionItemBuilder (Builder pattern, owned by M2) for construction.
     * Falls back gracefully if JSON is malformed.
     */
    private List<ActionItem> parseResponse(String rawJson) {
        List<ActionItem> items = new ArrayList<>();
        try {
            // The AI response wraps our array inside choices[0].message.content
            JsonObject response = JsonParser.parseString(rawJson).getAsJsonObject();
            String content = response
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            // Now parse the actual JSON array of action items
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();

                String task     = getStrOrNull(obj, "task");
                String assignee = getStrOrNull(obj, "assignee");
                String deadline = getStrOrNull(obj, "deadline");
                String priority = getStrOrNull(obj, "priority");

                if (task != null && !task.isBlank()) {
                    ActionItem item = new ActionItemBuilder()
                            .title(task)
                            .assignee(assignee)
                            .deadline(deadline)
                            .priority(priority)
                            .build();
                    items.add(item);
                }
            }
        } catch (Exception e) {
            System.err.println("[Facade] Failed to parse AI response: " + e.getMessage());
            System.err.println("[Facade] Raw response was: " + rawJson);
        }
        return items;
    }

    private String getStrOrNull(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return null;
    }
}