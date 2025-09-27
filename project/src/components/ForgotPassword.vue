<template>
  <div class="forgot-wrapper">
    <div class="forgot-box">
      <h2>🔑重設密碼</h2>
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
  position: relative;
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.forgot-box {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  border-radius: 20px;
  padding: 40px 30px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  text-align: center;
  animation: fadeIn 0.6s ease-out;
}

.forgot-box h2 {
  font-size: 26px;
  margin-bottom: 25px;
  color: #333;
  font-weight: bold;
}

.forgot-form input {
  display: block;
  width: 100%;
  box-sizing: border-box;
  padding: 14px;
  margin: 12px 0;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
  background-color: #fdfdfd;
  transition: 0.3s;
}

.forgot-form input:focus {
  border-color: #667eea;
  outline: none;
  box-shadow: 0 0 4px rgba(102, 126, 234, 0.4);
}

.forgot-form button {
  width: 100%;
  padding: 14px;
  margin-top: 10px;
  background-color: #e67e22;
  color: white;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.forgot-form button:hover {
  background-color: #d35400;
}

.message {
  color: #e74c3c;
  margin-top: 12px;
  font-size: 14px;
  animation: shake 0.3s;
}

.back-link {
  margin-top: 20px;
  font-size: 14px;
}
.back-link a {
  color: #667eea;
  cursor: pointer;
  font-weight: bold;
  text-decoration: underline;
}
.back-link a:hover {
  color: #5a67d8;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.98); }
  to { opacity: 1; transform: scale(1); }
}
</style>