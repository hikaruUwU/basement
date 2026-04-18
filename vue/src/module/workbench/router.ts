import type { RouteRecordRaw } from 'vue-router';

export const route: RouteRecordRaw[] = [
  {
    path: '/workbench',
    name: 'workbench',
    component: () => import('./index.vue'),
  },
];

export { route as ModuleWorkBenchRouting };
