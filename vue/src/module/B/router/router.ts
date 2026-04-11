import { type RouteRecordRaw, RouterView } from 'vue-router';
import { h } from 'vue';

const prefix = '/B';

export const ModuleBRoutes: RouteRecordRaw[] = [
  {
    path: prefix,
    component: () => h(RouterView),
    children: [
      {
        path: 'first',
        component: () => import('../index.vue'),
      },
      {
        path: 'second',
        component: () => import('../index.vue'),
      },
    ],
  },
];
