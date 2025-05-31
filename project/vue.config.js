module.exports = {
  devServer: {
    port: 8082,
    proxy: {
      '/api': {
        target: "${this.$apiBaseUrl}",
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}