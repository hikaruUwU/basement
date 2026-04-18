import { type AppContext, createApp } from 'vue';
import App from './App.vue';
import './index.css';

// import 'element-plus/dist/index.css';
import pinia from '@shared/pinia/pinia.ts';
import router, { scanRouting } from '@/src/router/router.tsx';

const elementApp = createApp(App);

export const $applicationContext: AppContext = elementApp._context;

await scanRouting();

elementApp.use(router);

elementApp.use(pinia);

elementApp.mount('#root');