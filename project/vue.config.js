module.exports = {
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: `${import.meta.env.VITE_API_BASE_URL}`,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}