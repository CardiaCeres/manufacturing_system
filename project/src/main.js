import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

axios.defaults.baseURL = 'https://manufacturing-system-latest.onrender.com/api'

axios.interceptors.request.use(
  config => {
    const publicPaths = ['/login', '/register', '/forgot-password', '/reset-password']
    const isPublic = publicPaths.some(path => config.url.includes(path))
    if (!isPublic) {
      const token = localStorage.getItem('token')
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
    }
    return config
  },
  error => Promise.reject(error)
)

const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(router)
app.mount('#app')