<template>
  <div class="login-wrapper">
    <div class="login-box">
      <h2>🔐 智慧訂單管理系統</h2>
      <form @submit.prevent="login" class="login-form">
        <input v-model="username" placeholder="使用者名稱" required />
        
        <div class="password-field">
          <input
            :type="showPassword ? 'text' : 'password'"
            v-model="password"
            placeholder="密碼"
            required
          />
          <span class="toggle-eye" @click="togglePassword">
            👁️
          </span>
        </div>

        <button type="submit">登入</button>

        <transition name="fade">
          <p v-if="error" class="error">{{ error }}</p>
        </transition>
      </form>

      <p class="register-link">
        還沒有帳號？<a @click="goToRegister">立即註冊</a>
      </p>
      <p class="forgot-link">
        忘記密碼？<a @click="goToForgot">忘記密碼</a>
      </p>
      <p class="home-link">
        <a @click="goToHome">🏠 返回首頁</a>
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "LoginPage",
  data() {
    return {
      username: "",
      password: "",
      showPassword: false,
      error: ""
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/login', {
          username: this.username,
          password: this.password
        });

        const token = response.data.token;
        const user = response.data.user;

        localStorage.setItem("token", token);
        localStorage.setItem("user", JSON.stringify(user));
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        this.$router.push("/orders");
      } catch (err) {
        this.error = "登入失敗，請確認帳號與密碼是否正確。";
      }
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
    },
    goToRegister() {
      this.$router.push("/register");
    },
    goToForgot() {
      this.$router.push("/forgot-password");
    },
    goToHome() {
      this.$router.push("/");
    }
  }
};
</script>

<style scoped>
.login-wrapper {
  position: relative;
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
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

.login-box h2 {
  font-size: 26px;
  margin-bottom: 25px;
  color: #333;
  font-weight: bold;
}

.login-form input {
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

.password-field {
  position: relative;
  width: 100%;
}

.toggle-eye {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  user-select: none;
  font-size: 18px;
  color: #666;
}

.login-form input:focus {
  border-color: #667eea;
  outline: none;
  box-shadow: 0 0 4px rgba(102, 126, 234, 0.4);
}

.login-form button {
  width: 100%;
  padding: 14px;
  margin-top: 10px;
  background-color: #667eea;
  color: white;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.login-form button:hover {
  background-color: #5a67d8;
}

.error {
  color: #e74c3c;
  margin-top: 12px;
  font-size: 14px;
  animation: shake 0.3s;
}

.register-link {
  margin-top: 20px;
  font-size: 14px;
  color: #555;
}
.register-link a {
  color: #667eea;
  cursor: pointer;
  font-weight: bold;
  text-decoration: underline;
}
.register-link a:hover {
  color: #5a67d8;
}

.forgot-link {
  margin-top: 10px;
  font-size: 14px;
  color: #555;
}
.forgot-link a {
  color: #e67e22;
  cursor: pointer;
  font-weight: bold;
  text-decoration: underline;
}
.forgot-link a:hover {
  color: #d35400;
}
.home-link {
  margin-top: 15px;
  font-size: 14px;
  color: #555;
  text-align: center;
}
.home-link a {
  color: #1abc9c;
  cursor: pointer;
  font-weight: bold;
  text-decoration: underline;
}
.home-link a:hover {
  color: #16a085;
}
@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  50% { transform: translateX(5px); }
  75% { transform: translateX(-5px); }
  100% { transform: translateX(0); }
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.98); }
  to { opacity: 1; transform: scale(1); }
}
</style>