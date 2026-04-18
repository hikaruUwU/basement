import type { RouteRecordRaw } from 'vue-router';

const prefix: string = '/';

export const route: RouteRecordRaw[] = [
  {
    path: prefix,
    name: 'root',
    component: () => import('./index.vue'),
  },
];
