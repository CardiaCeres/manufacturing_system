module.exports = {
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: 'https://manufacturing-system-springboot.onrender.com',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}