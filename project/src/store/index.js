import { createStore } from 'vuex';
import axios from 'axios';  // 引入 axios，用來發送 HTTP 請求
 
export default createStore({
  state: {
    user: null,      // 用來存放使用者資料，初始值是 null
    orders: [],      // 用來存放訂單列表，初始是空陣列
  },
  mutations: {
    setUser(state, user) {
      // 更新 state 裡的 user，這是同步方法
      state.user = user;
    },
    setOrders(state, orders) {
      // 更新 state 裡的 orders，這是同步方法
      state.orders = orders;
    },
  },
  actions: {
    // 非同步方法，從後端取得用戶資料
    async fetchUser({ commit }) {
      try {
        // 用 axios 發 GET 請求到 /api/user，等待回傳結果
        const response = await axios.get('/api/user');
        // 拿到資料後，用 commit 呼叫 mutation 更新 state.user
        commit('setUser', response.data);
      } catch (error) {
        // 如果請求失敗，印錯誤訊息
        console.error('獲取用戶資料失敗', error);
      }
    },
    // 非同步方法，從後端取得訂單資料
    async fetchOrders({ commit }) {
      try {
        // 用 axios 發 GET 請求到 /api/orders，等待回傳結果
        const response = await axios.get('/api/orders');
        // 拿到資料後，用 commit 呼叫 mutation 更新 state.orders
        commit('setOrders', response.data);
      } catch (error) {
        // 如果請求失敗，印錯誤訊息
        console.error('獲取訂單資料失敗', error);
      }
    },
  },
  getters: {
    getUser(state) {
      // 方便組件讀取使用者資料
      return state.user;
    },
    getOrders(state) {
      // 方便組件讀取訂單列表
      return state.orders;
    },
  },
});