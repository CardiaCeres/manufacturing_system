import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import axios from 'axios';

const app = createApp(App);

// 1. 設定 axios 基礎網址 (請換成你的後端 API 網址)
axios.defaults.baseURL = "https://manufacturing-system-latest.onrender.com/api";

// 2. 每次刷新都從 localStorage 讀取 token 並設置 Authorization header
const token = localStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

// 3. 如果你的後端需要跨域 cookie，打開此選項
axios.defaults.withCredentials = true;

// 4. 全局 axios 攔截器（可選），攔截 401 自動登出或跳轉
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      // 例如清除 token，跳轉到登入頁
      localStorage.removeItem('token');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

// 5. 將 axios 掛載到 Vue 實例（方便在組件中用 this.$axios）
app.config.globalProperties.$axios = axios;

// 6. 可選：方便在組件中拿到 baseURL
app.config.globalProperties.$apiBaseUrl = axios.defaults.baseURL;

app.use(store);
app.use(router);
app.mount('#app');