import { type AppContext, createApp } from 'vue';
import App from './App.vue';
import './index.css';
import pinia from './pinia/pinia.ts';
import router from './router/router.ts';
import 'element-plus/dist/index.css';

const elementApp = createApp(App);

export const $applicationContext: AppContext = elementApp._context;

elementApp.use(router).use(pinia).mount('#root');
