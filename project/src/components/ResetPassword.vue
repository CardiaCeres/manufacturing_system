<template>
  <div class="reset-wrapper">
    <div class="reset-box">
      <h2>🔑 重設密碼</h2>
      <form @submit.prevent="submitNewPassword" class="reset-form">
      <div class="password-field">
        <input
          v-model="password"
          :type="showPassword1 ? 'text' : 'password'"
          placeholder="輸入新密碼"
          required
        />
        <span class="toggle-eye" @click="togglePassword">👁️</span>
        </div> 

        <div class="password-field">
        <input
          v-model="confirmPassword"
          :type="showPassword2 ? 'text' : 'password'"
          placeholder="再次輸入新密碼"
          required
        />
        <span class="toggle-eye" @click="togglePassword(2)">👁️</span>
        </div>

        <button type="submit">重設密碼</button>
      </form>

      <transition name="fade">
        <p v-if="message" class="message">{{ message }}</p>
      </transition>

      <p class="back-link">
        <a @click="goBack">返回登入頁</a>
      </p>
    </div>
    <ChatWidget />
  </div>
</template>

<script>
import axios from "axios";
import { useRoute, useRouter } from "vue-router";
import ChatWidget from "@/components/ChatWidget.vue";

export default {
  name: "ResetPassword",
  components: { ChatWidget },
  setup() {
    const route = useRoute();
    const router = useRouter();
    return { route, router };
  },
  data() {
    return {
      password: "",
      confirmPassword: "",
      showPassword1: false,
      showPassword2: false,
      message: ""
    };
  },
  methods: {
    togglePassword(field) {
      if (field === 1) this.showPassword1 = !this.showPassword1; 
      if (field === 2) this.showPassword2 = !this.showPassword2;
    },
    goBack() {
      this.$router.push("/login");
    },
    async submitNewPassword() {
      if (this.password !== this.confirmPassword) {
        this.message = "兩次輸入的密碼不一致";
        return;
      }

      const token = this.route.query.token; // 從 URL 取得 token
      if (!token) {
        this.message = "Token 不存在或無效";
        return;
      }

      try {
        const response = await axios.post("/reset-password", {
          token,
          password: this.password
        });
        this.message = response.data;
        setTimeout(() => this.router.push("/login"), 2000); // 2秒後返回登入
      } catch (err) {
        this.message = err.response?.data || "重設密碼失敗";
      }
    }
  }
};
</script>

<style scoped>
.reset-wrapper {
  position: relative;
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.reset-box {
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

.reset-box h2 {
  font-size: 26px;
  margin-bottom: 25px;
  color: #333;
  font-weight: bold;
}

.reset-form {
  position: relative;
}

.reset-form input {
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

.reset-form input:focus {
  border-color: #667eea;
  outline: none;
  box-shadow: 0 0 4px rgba(102, 126, 234, 0.4);
}

.reset-form button {
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

.reset-form button:hover {
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