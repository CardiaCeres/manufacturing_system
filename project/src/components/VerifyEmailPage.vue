<template>
  <div class="verify-wrapper">
    <div class="verify-card">
      <h2>📧 Email 驗證</h2>

      <div v-if="loading" class="status">⏳ 驗證中，請稍候...</div>
      <div v-else-if="success" class="status success">✅ {{ success }}</div>
      <div v-else-if="error" class="status error">❌ {{ error }}</div>

      <button v-if="success" @click="goToLogin">前往登入</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "VerifyEmailPage",
  data() {
    return {
      loading: true,
      success: "",
      error: ""
    };
  },
  methods: {
    async verifyEmail(token) {
      try {
        const response = await axios.post('/api/verify-email', { token });
        this.success = response.data.message || "Email 驗證成功！";
        this.error = "";
      } catch (err) {
        this.error = err.response?.data?.message || "驗證失敗，Token 無效或已過期。";
        this.success = "";
      } finally {
        this.loading = false;
      }
    },
    goToLogin() {
      this.$router.push("/login");
    }
  },
  mounted() {
    // 從 URL 取得 token，例如: /verify-email?token=xxxxx
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    if (token) {
      this.verifyEmail(token);
    } else {
      this.error = "Token 缺失，無法驗證。";
      this.loading = false;
    }
  }
};
</script>

<style scoped>
.verify-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f6fa;
}

.verify-card {
  background: white;
  padding: 40px 30px;
  border-radius: 20px;
  box-shadow: 0 12px 24px rgba(0,0,0,0.15);
  text-align: center;
  max-width: 400px;
  width: 100%;
}

.status {
  margin: 20px 0;
  font-size: 16px;
}

.success {
  color: #27ae60;
}

.error {
  color: #e74c3c;
}

button {
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 10px;
  border: none;
  background-color: #4caf50;
  color: white;
  cursor: pointer;
  transition: 0.3s;
}

button:hover {
  background-color: #43a047;
}
</style>
