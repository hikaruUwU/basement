import { createRouter, createWebHashHistory, type Router, type RouteRecordRaw } from 'vue-router';
import NProgress from 'nprogress';
import { ElEmpty } from 'element-plus';

import('nprogress/nprogress.css');

const Fallback = () => <ElEmpty description="Not Found" />;

let _fixed_routing: RouteRecordRaw[] = [];

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

export const scanRouting = async () => {
  const context = require.context('../module', true, /router\.ts$/);

  context.keys().forEach((key: string) => {
    const mod = context(key) as any;
    const routes: Array<RouteRecordRaw> = mod.route || mod.default;
    if (Array.isArray(routes)) {
      routes.forEach((singleRoute) => {
        router.addRoute(singleRoute);
      });
    }
  });

  return context;
};

export { router as $router };
export default router;
