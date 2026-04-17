import { createRouter, createWebHashHistory, type Router, type RouteRecordRaw } from 'vue-router';
import NProgress from 'nprogress';

import { ElEmpty } from 'element-plus';

import('nprogress/nprogress.css');

const _external: RouteRecordRaw[] = [];

const Fallback = () => <ElEmpty description="Not Found" />;

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: [
    ..._external,
    {
      path: '/:pathMatch(.*)*',
      name: 'fallback',
      component: Fallback,
    },
  ],
});

router.beforeEach(() => {
  NProgress.start();
});
router.afterEach(() => {
  NProgress.done();
});

export { router as $router };

export default router;
