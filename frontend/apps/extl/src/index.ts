import { createApp } from 'vue';
import App from './App.vue';
import { $router, scanRouting } from '@/core/router/$router';

const system = createApp(App);

await scanRouting();

window.addEventListener('unhandledrejection', (event) => {
  alert(event.reason.message ?? 'Error');

  if (process.env.NODE_ENV === 'development')
    console.log(event.reason);
  event.preventDefault();
});

system.config.errorHandler = (err, instance, info) => {
  alert((err as Error).message ?? 'Error');

  if (process.env.NODE_ENV === 'development')
    console.log(err, instance, info);
};

system.use($router).mount('#root');
