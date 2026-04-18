import type { RouteRecordRaw } from 'vue-router';

export const route: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'root',
    component: () => import('./index.vue'),
  },
];

export { route as ModuleLoginRouting };
