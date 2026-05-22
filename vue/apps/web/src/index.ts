import { createApp } from 'vue';
import App from './App.vue';
import './index.css';

// import 'element-plus/dist/index.css';
import pinia from '@frontier/shared/pinia';
import router, { scanRouting } from './router/router.tsx';
import { setAppContext } from '@frontier/shared/context';

const elementApp = createApp(App);

setAppContext(elementApp._context);

await scanRouting();

elementApp.use(router);

elementApp.use(pinia);

elementApp.mount('#root');
