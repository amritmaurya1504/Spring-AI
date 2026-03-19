# AI Agent Guidelines for Memory Project

## Architecture Overview
This is a Spring Boot application integrating Spring AI for conversational chat with memory and Retrieval-Augmented Generation (RAG) capabilities. Key components:
- **Chat Flow**: `ChatController` → `ChatServiceImpl` → `ChatClient` (OpenAI gpt-4o-mini)
- **Memory**: In-memory `MessageWindowChatMemory` for conversation history (JDBC variant commented out)
- **RAG**: MariaDB vector store for semantic search, using cosine similarity on 1536-dimension embeddings
- **Advisors**: `MessageChatMemoryAdvisor` for history, `QuestionAnswerAdvisor` for RAG, custom `TokenPrint` for logging

## Key Patterns
- **Prompt Templates**: Use StringTemplate (.st) files in `src/main/resources/prompts/` for system/user messages (e.g., `system-message.st` restricts answers to retrieved documents only)
- **Vector Store Integration**: Populate via `ChatService.saveData()` with `Document` objects; search with `SearchRequest` (topK=3, similarityThreshold=0.5-0.6)
- **Configuration**: AI settings in `AiConfig.java` (model: gpt-4o-mini, temp=0.3, maxTokens=200); DB in `application.properties` (MariaDB on port 3308)
- **Entity/Model**: Simple POJOs like `Tut.java` for data structures; no JPA entities used

## Developer Workflows
- **Build & Run**: Use `./mvnw.cmd spring-boot:run` (requires `OPENAI_API_KEY` env var and MariaDB running)
- **Populate Vector DB**: Run `MemoryApplicationTests.saveDataToVectorDatabase()` test to load static Java facts from `Helper.getData()`
- **Debugging**: Enable `logging.level.org.springframework.ai.chat.client.advisor=DEBUG` for advisor logs; `TokenPrint` advisor logs requests/responses/tokens
- **Testing**: Standard Spring Boot tests; integration tests for AI calls require API key and DB

## Conventions
- **Package Naming**: Standard Maven structure, but note typo: `enitity` instead of `entity`
- **Dependencies**: Spring AI BOM managed; vector store uses MariaDB (not MySQL despite commented config)
- **Advisors**: Custom advisors implement `CallAdvisor`/`StreamAdvisor` for pre/post-processing (e.g., `TokenPrint` for observability)
- **RAG Implementation**: Prefer `QuestionAnswerAdvisor` over manual similarity search (as in `chatTemplate2` vs `chatTemplate`)

## Integration Points
- **External APIs**: OpenAI for chat/embedding (configurable to Gemini/Ollama via commented properties)
- **Database**: MariaDB for vector storage; schema auto-initialized
- **Cross-Component**: `ChatMemory` injected into `ChatClient`; `VectorStore` used in service layer for RAG</content>
<parameter name="filePath">D:\Java\SpringAI\memory\AGENTS.md
