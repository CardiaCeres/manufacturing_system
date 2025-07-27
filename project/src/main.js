import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import axios from 'axios';

const app = createApp(App);

// ➤ 設定 Axios baseURL
axios.defaults.baseURL = "https://manufacturing-system-latest.onrender.com";  // ← 換成你實際後端 API 網址

// ➤ 每次刷新都從 localStorage 設定 JWT Token
const token = localStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

// （可選）設定 withCredentials
axios.defaults.withCredentials = true;

// ➤ 將 axios 註冊為全域（可選）
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$apiBaseUrl = axios.defaults.baseURL;

app.use(store);
app.use(router);
app.mount('#app');