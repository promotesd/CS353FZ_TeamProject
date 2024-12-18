import '@/assets/main.scss'
import 'element-plus/dist/index.css'
import { createApp } from 'vue'
import pinia from "@/stores";
import zhCn from 'element-plus/dist/locale/en.min.mjs'

import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'



const app = createApp(App)
app.config.productionTip = false;

app.use(router).use(pinia).use(ElementPlus, {
    locale: zhCn,
}).mount('#app')
