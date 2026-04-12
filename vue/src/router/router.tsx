import {
  createRouter,
  createWebHashHistory,
  type Router,
  type RouteRecordRaw,
} from 'vue-router';
import NProgress from 'nprogress';
import { ModuleARoutes } from '@/src/module/A/router/router.ts';
import { ModuleBRoutes } from '@/src/module/B/router/router.ts';
import { ElEmpty } from 'element-plus';

import('nprogress/nprogress.css');

const Fallback = () => <ElEmpty description="Not Found" />;
Fallback.displayName = 'Fallback';

export const routes: Readonly<RouteRecordRaw[]> = [
  ...ModuleARoutes,
  ...ModuleBRoutes,

  {
    path: '/:pathMatch(.*)*',
    name: 'fallback',
    component: Fallback,
  },
];

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes,
});

router.beforeEach(() => {
  NProgress.start();

});
router.afterEach(() => {
  NProgress.done();
});

export const $router = () => router as Router;

export default router;