module.exports = {
  devServer: {
    disableHostCheck: true,
    port: 3000,
    proxy: {
      "^/api": {
        target: "http://server:8080", // Use 'server', not 'localhost'
        changeOrigin: true,
      },
    },
  },
};
