import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';

const app = createApp(App);

axios.defaults.baseURL = 'https://manufacturing-system-latest.onrender.com/api';
axios.defaults.withCredentials = true;

// 請求攔截器帶 token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// 回應攔截器攔截 401
axios.interceptors.response.use(
  res => res,
  err => {
    if (err.response && err.response.status === 401) {
      console.log('401 error detected, redirecting...');
      localStorage.removeItem('token');
      setTimeout(() => {
        router.push('/login');
      }, 100);
    }
    return Promise.reject(err);
  }
);

app.config.globalProperties.$axios = axios;

app.use(router);
app.mount('#app');