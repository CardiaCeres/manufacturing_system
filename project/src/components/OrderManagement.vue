<template>
  <div class="wrapper">
    <div class="orders">
      <div class="header">
        <button class="logout-button" @click="logout">登出</button>
      </div>
      <h2>訂單管理</h2>
      <div class="create-form">
        <input v-model="newOrder.orderNumber" placeholder="訂單編號" />
        <input v-model="newOrder.customerName" placeholder="客戶名稱" />
        <input v-model="newOrder.materialName" placeholder="材料名稱" />
        <input v-model="newOrder.productName" placeholder="產品名稱" />
        <input v-model.number="newOrder.quantity" placeholder="數量" type="number" />
        <input v-model="newOrder.price" placeholder="單價" type="number" />
        <input v-model="newOrder.orderDate" type="date" />
        <select v-model="newOrder.status">
          <option value="處理中">處理中</option>
          <option value="已完成">已完成</option>
          <option value="已取消">已取消</option>
        </select>
        <button @click="createOrder">{{ isEditing ? '更新訂單' : '新增訂單' }}</button>
      </div>
 
      <div v-if="orders.length === 0">
        <p>目前沒有任何訂單。</p>
      </div>
      <table v-else>
        <thead>
          <tr>
            <th>訂單編號</th>
            <th>客戶名稱</th>
            <th>材料名稱</th>
            <th>產品名稱</th>
            <th>數量</th>
            <th>單價</th>
            <th>總金額</th>
            <th>狀態</th>
            <th>訂單日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.orderNumber }}</td>
            <td>{{ order.customerName }}</td>
            <td>{{ order.materialName }}</td>
            <td>{{ order.productName }}</td>
            <td>{{ order.quantity }}</td>
            <td>{{ order.price }}</td>
            <td>{{ order.totalAmount }}</td>
            <td>{{ order.status }}</td>
            <td>{{ order.orderDate }}</td>
            <td>
              <button @click="editOrder(order)">編輯</button>
              <button @click="deleteOrder(order.id)">刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
 
<script>
import axios from 'axios';
export default {
  name: 'OrderManagement',
  data() {
    return {
      orders: [],
      newOrder: {
        orderNumber: '',
        customerName: '',
        materialName: '',
        productName: '',
        quantity: '',
        price: '',
        totalAmount: 0,
        status: '處理中',
        orderDate: '',
        userId: null,
      },
      isEditing: false,
      currentOrderId: null,
    };
  },
  created() {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user && user.id) {
      this.newOrder.userId = user.id;
      this.fetchOrders(user.id);
    }
  },
  methods: {
    async fetchOrders(userId) {
      try {
        const response = await axios.get(`http://localhost:8080/api/orders/user/${userId}`);
        this.orders = response.data;
      } catch (error) {
        alert("取得訂單資料失敗，請稍後再試。");
        console.error(error);
      }
    },
    async createOrder() {
      try {
        if (
          !this.newOrder.orderNumber ||
          !this.newOrder.customerName ||
          !this.newOrder.orderDate
        ) {
          alert("請填寫完整的訂單資料（包含訂單編號、客戶名稱與訂單日期）");
          return;
        }
 
        this.newOrder.totalAmount = this.newOrder.quantity * this.newOrder.price;
 
        if (this.isEditing) {
          await axios.put(`http://localhost:8080/api/orders/update/${this.currentOrderId}`, this.newOrder);
          const index = this.orders.findIndex(order => order.id === this.currentOrderId);
          this.orders[index] = { ...this.newOrder, id: this.currentOrderId };
          this.isEditing = false;
          this.currentOrderId = null;
        } else {
          const response = await axios.post("http://localhost:8080/api/orders/create", this.newOrder);
          this.orders.push(response.data);
        }
 
        this.resetForm();
      } catch (error) {
        alert("新增/更新訂單失敗，請確認欄位填寫正確並稍後再試！");
        console.error("新增/更新訂單失敗：", error);
      }
    },
    async deleteOrder(orderId) {
      const confirmed = confirm("確定要刪除這筆訂單嗎？此操作無法復原！");
      if (!confirmed) return;
 
      try {
        await axios.delete(`http://localhost:8080/api/orders/delete/${orderId}`);
        this.orders = this.orders.filter(order => order.id !== orderId);
        alert("訂單刪除成功！");
      } catch (error) {
        alert("刪除訂單失敗，請稍後再試！");
        console.error(error);
      }
    },
    editOrder(order) {
      this.newOrder = { ...order };
      this.isEditing = true;
      this.currentOrderId = order.id;
    },
    resetForm() {
      this.newOrder = {
        orderNumber: '',
        customerName: '',
        materialName: '',
        productName: '',
        quantity: '',
        price: '',
        totalAmount: 0,
        status: '處理中',
        orderDate: '',
        userId: this.newOrder.userId,
      };
    },
    logout() {
      if (confirm("⚠️ 確定要登出嗎？")) {
      localStorage.removeItem("user");
      this.$router.push("/login");
     }
   }
  }
};
</script>
 
<style scoped>
.wrapper {
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  background-position: center;
  padding: 2rem;
  box-sizing: border-box;
}
 
.orders {
  max-width: 1000px;
  margin: 0 auto;
  font-family: 'Segoe UI', sans-serif;
  background-color: rgba(255, 255, 255, 0.9); /* 半透明白底 */
  backdrop-filter: blur(8px); /* 模糊背景 */
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.15);
}
 
.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}
 
.logout-button {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
 
.logout-button:hover {
  background-color: #2980b9;
}
 
.create-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}
 
.create-form input,
.create-form select {
  padding: 8px;
  flex: 1 1 150px;
}
 
.create-form button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}
 
.create-form button:hover {
  background-color: #45a049;
}
 
table {
  width: 100%;
  border-collapse: collapse;
  background-color: #f9f9f9;
}
 
th,
td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
}
 
th {
  background-color: #f2f2f2;
}
 
button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 6px 12px;
  cursor: pointer;
}
 
button:hover {
  background-color: #d32f2f;
}
</style>
 
 