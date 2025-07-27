import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import axios from 'axios';

const app = createApp(App);

// ➤ 設定 Axios baseURL
app.config.globalProperties.$apiBaseUrl = "https://manufacturing-system-latest.onrender.com";  // ← 換成你實際後端 API 網址

// ➤ 每次刷新都從 localStorage 設定 JWT Token
const token = localStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}