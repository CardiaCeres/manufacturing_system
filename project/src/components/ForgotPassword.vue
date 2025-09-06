<template>
  <div class="forgot-wrapper">
    <div class="forgot-box">
      <h2>🔑 重設密碼</h2>
      <form @submit.prevent="submitEmail" class="forgot-form">
        <input v-model="email" placeholder="請輸入註冊信箱" required />
        <button type="submit">送出</button>
      </form>

      <transition name="fade">
        <p v-if="message" class="message">{{ message }}</p>
      </transition>

      <p class="back-link">
        <a @click="goBack">返回登入頁</a>
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ForgotPassword",
  data() {
    return {
      email: "",
      message: ""
    };
  },
  methods: {
    async submitEmail() {
      try {
        await axios.post("/forgot-password", { email: this.email });
        this.message = "已寄送重設密碼連結到您的信箱。";
      } catch (err) {
        this.message = "寄送失敗，請確認信箱是否正確。";
      }
    },
    goBack() {
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.forgot-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f8f9fa;
}
.forgot-box {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0,0,0,0.1);
  text-align: center;
  width: 100%;
  max-width: 400px;
}
.forgot-box h2 {
  margin-bottom: 20px;
  font-size: 22px;
}
.forgot-form input {
  width: 100%;
  padding: 12px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 10px;
}
.forgot-form button {
  width: 100%;
  padding: 12px;
  background: #e67e22;
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
}
.forgot-form button:hover {
  background: #d35400;
}
.message {
  margin-top: 15px;
  color: green;
}
.back-link {
  margin-top: 20px;
  font-size: 14px;
}
.back-link a {
  color: #667eea;
  cursor: pointer;
  text-decoration: underline;
}
</style>