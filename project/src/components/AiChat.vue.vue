<template>
  <div class="ai-chat-container">
    <h3>智能訂單查詢助理</h3>
    <div class="chat-window">
      <div v-for="(msg, index) in messages" :key="index" :class="msg.role">
        <p>{{ msg.text }}</p>
      </div>
    </div>

    <input
      v-model="userQuestion"
      @keyup.enter="sendQuestion"
      placeholder="請輸入您的訂單查詢問題..."
    />
    <button @click="sendQuestion">送出</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      userQuestion: "",
      messages: []
    };
  },
  methods: {
    async sendQuestion() {
      if (!this.userQuestion.trim()) return;

      // 加入使用者訊息
      this.messages.push({ role: "user", text: this.userQuestion });

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          alert("請先登入！");
          this.$router.push("/login");
          return;
        }

        const response = await axios.post(
          "/api/ai/query-order",
          { question: this.userQuestion },
          { headers: { Authorization: `Bearer ${token}` } }
        );

        this.messages.push({ role: "ai", text: response.data.answer });
        this.userQuestion = "";

      } catch (error) {
        console.error(error);
        this.messages.push({
          role: "ai",
          text: "系統發生錯誤，請稍後再試。"
        });
      }
    }
  }
};
</script>

<style scoped>
.ai-chat-container {
  border: 1px solid #ccc;
  padding: 12px;
  max-width: 500px;
  margin: 1rem auto;
  background: #f9f9f9;
  border-radius: 10px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.chat-window {
  max-height: 250px;
  overflow-y: auto;
  margin-bottom: 10px;
  background: white;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ddd;
}

.user {
  text-align: right;
  margin-bottom: 8px;
  color: #2c3e50;
  font-weight: 600;
}

.ai {
  text-align: left;
  margin-bottom: 8px;
  color: #2980b9;
  font-style: italic;
}

input {
  width: 70%;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
  margin-right: 10px;
  box-sizing: border-box;
}

button {
  padding: 8px 14px;
  border: none;
  background-color: #667eea;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

button:hover {
  background-color: #5562cc;
}
</style>