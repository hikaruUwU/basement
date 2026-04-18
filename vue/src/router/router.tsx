import { createRouter, createWebHashHistory, type Router, type RouteRecordRaw } from 'vue-router';
import NProgress from 'nprogress';
import { ElEmpty } from 'element-plus';
import { ModuleLoginRouting } from '@/src/module/login/router.ts';
import { ModuleWorkBenchRouting } from '@/src/module/workbench/router.ts';

import('nprogress/nprogress.css');

let _fixed_routing: RouteRecordRaw[] = [...ModuleLoginRouting, ...ModuleWorkBenchRouting];

const Fallback = () => <ElEmpty description="Not Found" />;

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: [
    ..._fixed_routing,
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
