# Local Chatbot in Java (Spring Boot + Ollama llama3)

A **production-style chatbot** built in **Java + Spring Boot**, designed to run **fully locally** with **zero API cost** using Ollama.
This setup is optimized for your **Mac M1 (8GB RAM)**.

## Why this looks strong on a resume

- Built with **Spring Boot** (REST APIs, validation, config management, MVC UI)
- Integrates with **Ollama llama3** for local inference
- Includes **persistent chat history** with JPA (H2 file DB)
- Has **safety checks**, **input limits**, and **health endpoint**
- Includes **test coverage** for key API behavior

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring MVC + Thymeleaf
- Spring Data JPA + H2 file database
- Ollama (`llama3`)
- JUnit + MockMvc

---

## 1) Prerequisites (Mac M1)

1. Install Java 17 (Temurin preferred)
2. Install Maven 3.9+
3. Install Ollama:
   ```bash
   brew install ollama
   ```
4. Start Ollama:
   ```bash
   ollama serve
   ```
5. Pull model:
   ```bash
   ollama pull llama3
   ```

> On 8GB RAM, close heavy apps/tabs while running model inference for better responsiveness.

---

## 2) Run locally

```bash
mvn spring-boot:run
```

Open: `http://localhost:8080`

### Endpoints
- `GET /` → Chat UI
- `GET /api/health` → health + model
- `POST /api/chat` → chat API

---

## 3) API quick test

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Give me a 4-week DSA plan", "sessionId": null}'
```

---

## 4) Configuration (`src/main/resources/application.yml`)

- `chatbot.ollama-base-url` (default `http://localhost:11434`)
- `chatbot.ollama-model` (default `llama3`)
- `chatbot.max-user-message-chars` (default `2000`)

---

## 5) Tests

```bash
mvn test
```

---

## Resume-ready project bullets

- Built a production-grade local chatbot in Java/Spring Boot with REST APIs, validation, and robust error handling.
- Integrated Ollama llama3 for zero-cost on-device inference and privacy-preserving AI interactions.
- Implemented session-based conversation persistence using Spring Data JPA and structured automated tests with MockMvc.

---

## Troubleshooting

### “LLM backend unavailable”
- Verify Ollama is running: `ollama serve`
- Verify model exists: `ollama list`
- Pull model if missing: `ollama pull llama3`

### Slow responses on M1 8GB
- Keep prompts concise
- Close heavy apps/tabs
- If needed, switch to a smaller model in `application.yml`
