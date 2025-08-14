import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

axios.defaults.baseURL = process.env.VUE_APP_BACKEND_URL

axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(router)
app.mount('#app')