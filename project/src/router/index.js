import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import OrderManagement from '../components/OrderManagement.vue';
import RegisterPage from '../components/RegisterPage.vue';
import HomeView from '../components/HomeView.vue';
import ForgotPassword from '@/views/ForgotPassword.vue'

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/orders', name: 'Orders', component: OrderManagement },
  { path: '/register', name: 'Register', component: RegisterPage },
  { path: '/forgot-password', component: ForgotPassword }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;