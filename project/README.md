# Meeting Notes → Action Items Assistant

## Project Overview

This is a **Spring Boot CLI application** (Java 17) that transforms meeting notes into structured action items using Azure OpenAI API integration.

### Key Features
- **Flexible note processing** with two extraction strategies:
  - **Formal Strategy**: Structured board minutes with strict extraction
  - **Casual Strategy**: Informal team notes with lenient extraction
- **Observer Pattern**: Event system tracks task creation in real-time
- **In-Memory Storage**: Persist extracted action items during the session
- **Interactive CLI**: User-friendly menu-driven interface

### Design Patterns Used
1. **Strategy Pattern** - Different extraction strategies for note types
2. **Observer Pattern** - Event listeners for task creation and storage
3. **Facade Pattern** - Unified `MeetingAssistantFacade` interface for simplification

---

## How to Run

### Prerequisites
- **Java 17** or later
- **Maven** (or use the included Maven wrapper)
- **Azure OpenAI API Key** (set as environment variable: `AZURE_OPENAI_API_KEY`)

### Running the Application

From the project root directory, execute:

```bash
mvn clean compile exec:java -Dexec.mainClass="design.pattern.project.cli.Main"
```

**Or using the Maven wrapper:**

```bash
./mvnw clean compile exec:java -Dexec.mainClass="design.pattern.project.cli.Main"
```

**Or**
Simply run the main.java class in the cli folder

### Using the Application

Once running, you'll see the main menu:

```
╔══════════════════════════════════════════════╗
║   Meeting Notes → Action Items Assistant     ║
╚══════════════════════════════════════════════╝

Options:
  1 - Process new meeting notes
  2 - View all stored tasks
  3 - Exit
```

**Option 1 - Process Notes:**
- Select extraction strategy (Formal or Casual)
- Paste your meeting notes
- Press ENTER on a blank line when done
- Results displayed in summary table format

**Option 2 - View Tasks:**
- Display all previously extracted action items

**Option 3 - Exit:**
- Close the application

---

## Project Structure

```
src/main/java/design/pattern/project/
├── cli/
│   └── Main.java                    # CLI entry point
├── model/
│   ├── ActionItem.java              # Action item data model
│   └── ActionItemBuilder.java       # Builder pattern for ActionItem
├── pattern/
│   ├── TaskEventSystem.java         # Observer system
│   ├── ConsoleTaskListener.java     # Event listener (console output)
│   ├── InMemoryTaskRepository.java  # Event listener (storage)
│   ├── ExtractionStrategy.java      # Strategy interface
│   ├── FormalMeetingStrategy.java  # Formal notes strategy
│   └── CasualNotesStrategy.java    # Casual notes strategy
└── service/
    ├── MeetingAssistantFacade.java # Main facade
    ├── AzureClient.java             # Azure OpenAI integration
    └── StructuredPromptBuilder.java # Prompt construction
```

---

## Technical Stack

- **Framework**: Spring Boot 4.0.5
- **Language**: Java 17
- **Build Tool**: Maven
- **Key Dependencies**:
  - OkHttp 4.12.0 (HTTP client)
  - java-dotenv 3.0.0 (Environment variables)
  - Spring Data JPA + H2 Database

