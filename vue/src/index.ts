import { type AppContext, createApp } from 'vue';
import App from './App.vue';
import './index.css';
import pinia from './pinia/pinia.ts';
import router from './router/router.ts';
import { i18n } from './i18n/i18n.ts';
import 'element-plus/dist/index.css';

const elementApp = createApp(App);

export const $applicationContext: AppContext = elementApp._context;

elementApp.use(router).use(pinia).use(i18n).mount('#root');
