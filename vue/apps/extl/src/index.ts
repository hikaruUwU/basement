import { createApp } from 'vue';
import App from './App.vue';
import { $router, scanRouting } from '@/core/router/$router';

const system = createApp(App);

await scanRouting();

system.use($router).mount('#root');
