import { type RouteRecordRaw} from 'vue-router';

const prefix = '/A';

export const ModuleARoutes: RouteRecordRaw[] = [
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
