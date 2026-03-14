const chatEl = document.getElementById("chat");
const form = document.getElementById("chat-form");
const messageInput = document.getElementById("message");
const statusEl = document.getElementById("status");

let sessionId = null;

function appendMessage(role, text) {
    const div = document.createElement("div");
    div.className = `msg ${role}`;
    div.textContent = `${role === "user" ? "You" : "Bot"}: ${text}`;
    chatEl.appendChild(div);
    chatEl.scrollTop = chatEl.scrollHeight;
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const message = messageInput.value.trim();
    if (!message) return;

    appendMessage("user", message);
    messageInput.value = "";
    statusEl.textContent = "Thinking...";

    try {
        const response = await fetch("/api/chat", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ message, sessionId }),
        });

        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.detail || "Unknown error");
        }

        sessionId = data.sessionId;
        appendMessage("assistant", data.reply);
        statusEl.textContent = "Ready";
    } catch (error) {
        statusEl.textContent = `Error: ${error.message}`;
    }
});
