import {
  createRouter,
  createWebHashHistory,
  type Router,
  type RouteRecordRaw,
} from 'vue-router';
import NProgress from 'nprogress';

import('nprogress/nprogress.css');

export const $router = () => router as Router;

export const routes: Readonly<RouteRecordRaw[]> = [
  // {
  //   path: '/:pathMatch(.*)*',
  //   name: 'fallback',
  //   component: () => import('../component/demonstrator/index.vue'),
  // },
];

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes,
});

router.beforeEach((_t, _f, next) => {
  NProgress.start();
  next(undefined);
});
router.afterEach(() => {
  NProgress.done();
});

export default router;
