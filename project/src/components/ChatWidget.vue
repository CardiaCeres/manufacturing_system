<template>
  <div class="chat-widget">
    <div class="chat-messages">
      <div v-for="(msg, index) in messages" :key="index" class="chat-message" :class="msg.sender">
        <span v-html="msg.text"></span>
      </div>
    </div>
    <div class="chat-input">
      <input v-model="userInput" @keyup.enter="sendMessage" placeholder="請輸入問題..." />
      <button @click="sendMessage">送出</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userInput: '',
      messages: []
    };
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim()) return;

      const message = this.userInput.trim();
      this.messages.push({ text: message, sender: 'user' });
      this.userInput = '';

      try {
        const response = await fetch('/api/chat', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ message })
        });
        const data = await response.json();

        // 處理 response，移除前綴與轉換換行
        let reply = data.response || '系統沒有回應';
        reply = reply.replace(/^response\s*:\s*/i, ''); // 去除 "response:" 前綴（若有）
        reply = reply.replace(/\\n/g, '<br>'); // 將 \n 轉為 <br>

        this.messages.push({ text: reply, sender: 'bot' });
      } catch (err) {
        console.error(err);
        this.messages.push({ text: '⚠️ 無法取得回應，請稍後再試。', sender: 'bot' });
      }
    }
  }
};
</script>

<style scoped>
.chat-widget {
  width: 100%;
  max-width: 600px;
  border: 1px solid #ccc;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  font-family: Arial, sans-serif;
}

.chat-messages {
  flex: 1;
  padding: 16px;
  max-height: 400px;
  overflow-y: auto;
  background-color: #fafafa;
}

.chat-message {
  margin-bottom: 12px;
  padding: 8px 12px;
  border-radius: 10px;
  max-width: 80%;
  word-wrap: break-word;
}

.chat-message.user {
  background-color: #dcf8c6;
  align-self: flex-end;
}

.chat-message.bot {
  background-color: #e8e8e8;
  align-self: flex-start;
}

.chat-input {
  display: flex;
  border-top: 1px solid #ccc;
}

.chat-input input {
  flex: 1;
  padding: 12px;
  border: none;
  outline: none;
}

.chat-input button {
  padding: 12px 16px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  border-left: 1px solid #ccc;
}
</style>
