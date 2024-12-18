import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import { readFileSync } from 'fs';
import { resolve } from 'path';

export default defineConfig({
  server: {
    open: true,
    host: '0.0.0.0',
    port: 8081,
    //以上的ip和端口是我们本机的;下面为需要跨域的
    proxy: { //配置跨域
      '/api': {
        target: 'http://localhost:9001', //填写请求的目标地址
        changeOrigin: true, //允许跨域
        rejectUnauthorized: false, // 设置为 false 接受自签名证书
        secure:false,
        rewrite: (path) => path.replace('/api', '')
      },
      '/websocket': {
        target: 'ws://localhost:9001', //填写请求的目标地址
        changeOrigin: true, //允许跨域
        rejectUnauthorized: false, // 设置为 false 接受自签名证书
        secure:false,
        rewrite: (path) => path.replace('/websocket', '')
      },
    }
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
