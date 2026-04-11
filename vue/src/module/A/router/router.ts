import { type RouteRecordRaw, RouterView } from 'vue-router';
import { h } from 'vue';

const prefix = '/A';

export const ModuleARoutes: RouteRecordRaw[] = [
  {
    path: prefix,
    component: () => h(RouterView),
    children: [
      {
        path: 'first',
        name: 'fallback',
        component: () => import('../index.vue'),
      },
      {
        path: 'second',
        component: () => import('../index.vue'),
      },
    ],
  },
];
