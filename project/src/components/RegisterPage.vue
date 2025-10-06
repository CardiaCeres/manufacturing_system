<template>
  <div class="register-wrapper">
    <div class="register-card">
      <h2>📝立即註冊</h2>
      <form @submit.prevent="register" class="register-form">
        <input v-model="username" placeholder="👤 使用者名稱" required />

        <div class="password-field">
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            placeholder="🔒 密碼"
            required
          />
          <span class="toggle-eye" @click="togglePassword">
            👁️
          </span>
        </div>

        <input v-model="email" type="email" placeholder="📧 電子信箱" required />

        <div class="select-wrapper">
          <select v-model="department" required>
            <option value="">🏢 選擇部門</option>
            <option>管理部</option>
            <option>資訊部</option>
            <option>業務部</option>
            <option>行銷部</option>
            <option>工程部</option>
          </select>
          <!-- inline SVG 箭頭 -->
          <svg class="select-arrow" viewBox="0 0 24 24">
            <path d="M7 10l5 5 5-5H7z" fill="#777"/>
          </svg>
        </div>

        <button type="submit">註冊</button>

        <p v-if="error" class="error">{{ error }}</p>
        <p v-if="success" class="success">{{ success }}</p>
      </form>
      <p class="login-link">
        已有帳號？<a @click="goToLogin">立即登入</a>
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "RegisterPage",
  data() {
    return {
      username: "",
      password: "",
      email: "",
      department: "",
      showPassword: false,
      error: "",
      success: "",
      loading: false
    };
  },
  methods: {
    async register() {
      this.loading = true;
      try {
        await axios.post('/register', {
          username: this.username,
          password: this.password,
          email: this.email,
          department: this.department
        });
        this.success = "🎉 註冊成功，即將導向登入畫面";
        this.error = "";
        setTimeout(() => {
          this.$router.push("/login");
        }, 1500);
      } catch (err) {
        this.error = "⚠️ 註冊失敗，帳號或信箱可能已存在。";
        this.success = "";
      } finally {
        this.loading = false;
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
    }
  }
};
</script>

<style scoped>
.register-wrapper {
  position: relative;
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-card {
  background: white;
  padding: 40px 30px;
  border-radius: 20px;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
  max-width: 420px;
  width: 100%;
  text-align: center;
  animation: fadeIn 0.8s ease;
}

.register-card h2 {
  font-size: 26px;
  margin-bottom: 25px;
  color: #333;
  font-weight: bold;
}

.icon {
  font-size: 28px;
  margin-right: 8px;
}

.register-form input {
  display: block;
  width: 100%;
  box-sizing: border-box;
  margin: 12px 0;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 16px;
  background: #f9f9f9;
  transition: 0.3s;
}

.register-form input:focus {
  outline: none;
  border-color: #66a6ff;
  background: #fff;
}

/* 密碼欄眼睛 */
.password-field {
  position: relative;
}

.toggle-eye {
  position: absolute;
  top: 50%;
  right: 14px;
  transform: translateY(-50%);
  cursor: pointer;
  user-select: none;
  font-size: 18px;
  color: #777;
}

/* 按鈕 */
.register-form button {
  width: 100%;
  margin-top: 20px;
  padding: 12px;
  background: #4CAF50;
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: 0.3s;
}

.register-form button:hover {
  background: #43a047;
}

/* 下拉選單 */
.select-wrapper {
  position: relative;
}

.register-form select {
  display: block;
  width: 100%;
  box-sizing: border-box;
  margin: 12px 0;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 16px;
  color: #555;
  background-color: #f9f9f9;
  appearance: none; /* 移除預設箭頭 */
  cursor: pointer;
}

.register-form select:focus {
  outline: none;
  border-color: #66a6ff;
  background: #fff;
  color: #333;
}

.select-arrow {
  position: absolute;
  top: 50%;
  right: 14px;
  width: 20px;
  height: 20px;
  pointer-events: none;
  transform: translateY(-50%);
}

.error {
  color: #e74c3c;
  margin-top: 15px;
  font-size: 14px;
}

.success {
  color: #27ae60;
  margin-top: 15px;
  font-size: 14px;
}

.login-link {
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #2980b9;
  cursor: pointer;
  font-weight: bold;
  text-decoration: underline;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(25px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
   
   