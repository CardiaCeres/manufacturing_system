<template>
  <div class="chat-widget" :class="{ open: isOpen }">
    <button class="chat-toggle" @click="toggleChat">
      💬
    </button>
    <div class="chat-window" v-if="isOpen">
      <div class="chat-header">
        <span>智慧客服小幫手</span>
        <button class="close-btn" @click="toggleChat">✖</button>
      </div>
      <div class="chat-messages" ref="messagesContainer">
        <div v-for="(msg, idx) in messages" :key="idx" :class="['message', msg.from]">
          <div class="msg-content">{{ msg.text }}</div>
        </div>
      </div>
      <div class="chat-input">
        <input
          v-model="userInput"
          @keydown.enter.prevent="sendMessage"
          placeholder="請輸入問題..."
        />
        <button @click="sendMessage">送出</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChatWidget",
  data() {
    return {
      isOpen: false,
      userInput: "",
      messages: [
        { from: "bot", text: "您好！我是智慧客服，有什麼可以幫您？" }
      ],
    };
  },
  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen;
      if (this.isOpen) {
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },
    async sendMessage() {
      const msg = this.userInput.trim();
      if (!msg) return;

      // 顯示使用者訊息
      this.messages.push({ from: "user", text: msg });
      this.userInput = "";
      this.scrollToBottom();

      // 呼叫後端 AI API
      try {
        const res = await fetch("/api/chat", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ message: msg }),
        });
        const data = await res.text();

        // 顯示機器人回覆
        this.messages.push({ from: "bot", text: data });
        this.scrollToBottom();
      } catch (err) {
        this.messages.push({
          from: "bot",
          text: "抱歉，系統暫時無法回應，請稍後再試。",
        });
        this.scrollToBottom();
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        container.scrollTop = container.scrollHeight;
      });
    },
  },
};
</script>

<style scoped>
.chat-widget {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
.chat-toggle {
  background-color: #674f4f;
  border: none;
  color: white;
  font-size: 28px;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  cursor: pointer;
  box-shadow: 0 6px 12px rgba(0,0,0,0.2);
  transition: background-color 0.3s ease;
}
.chat-toggle:hover {
  background-color: #563f3f;
}
.chat-window {
  width: 320px;
  max-height: 420px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 6px 18px rgba(0,0,0,0.3);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin-bottom: 10px;
}
.chat-header {
  background-color: #674f4f;
  color: white;
  padding: 12px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.close-btn {
  background: transparent;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
}
.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background: #f9f9f9;
}
.message {
  margin-bottom: 12px;
  max-width: 80%;
  word-wrap: break-word;
}
.message.user {
  color: #2a64f5;
  align-self: flex-end;
  background-color: #e6f0ff;
  border-radius: 10px 10px 0 10px;
  padding: 8px 12px;
}
.message.bot {
  color: #4a4a4a;
  align-self: flex-start;
  background-color: #ddd;
  border-radius: 10px 10px 10px 0;
  padding: 8px 12px;
}
.chat-input {
  display: flex;
  border-top: 1px solid #ccc;
}
.chat-input input {
  flex: 1;
  border: none;
  padding: 10px 12px;
  font-size: 14px;
  outline: none;
}
.chat-input button {
  background-color: #674f4f;
  color: white;
  border: none;
  padding: 0 16px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}
.chat-input button:hover {
  background-color: #563f3f;
}
</style>