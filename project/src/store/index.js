import { createStore } from 'vuex';

export default createStore({
  state: {
    user: null, // 存储登录的用户信息
    orders: [], // 存储订单信息
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    },
    setOrders(state, orders) {
      state.orders = orders;
    },
  },
  actions: {
    fetchUser({ commit }) {
      // 模拟从后端获取用户信息
      const user = { id: 1, username: 'john_doe' }; // 举例数据
      commit('setUser', user);
    },
    fetchOrders({ commit }) {
      // 模拟从后端获取订单信息
      const orders = [
        { orderNumber: '123', customerName: 'John Doe', materialName: 'Material 1', quantity: 10, orderDate: '2025-04-12' },
        { orderNumber: '124', customerName: 'John Doe', materialName: 'Material 2', quantity: 5, orderDate: '2025-04-13' },
      ]; // 举例数据
      commit('setOrders', orders);
    },
  },
  getters: {
    getUser(state) {
      return state.user;
    },
    getOrders(state) {
      return state.orders;
    },
  },
});