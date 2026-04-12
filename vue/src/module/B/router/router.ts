import { type RouteRecordRaw } from 'vue-router';

const prefix = '/B';

export const ModuleBRoutes: RouteRecordRaw[] = [
  {
    path: prefix,
    redirect: `${prefix}/first`,
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
