import type { RouteRecordRaw } from 'vue-router';

const prefix: string = '/workbench';

export const route: RouteRecordRaw[] = [
  {
    path: prefix,
    name: 'workbench',
    component: () => import('./index.vue'),
  },
];
