import { createRouter, createWebHashHistory, type Router, type RouteRecordRaw } from 'vue-router';
import NProgress from 'nprogress';
import { type ExtractPropTypes, h, Teleport } from 'vue';

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: [],
  scrollBehavior: (to, from, savedPosition) => (savedPosition ? savedPosition : { top: 0 }),
});

router.beforeEach(() => void NProgress.start());
router.afterEach(() => void NProgress.done());

export const scanRouting = async () => {
  const modules = import.meta.glob('../../module/**/router.ts');

  for (const path in modules) {
    const mod = (await modules[path]()) as any;
    const routes: Array<RouteRecordRaw> = mod.route || mod.default;

    if (Array.isArray(routes)) routes.forEach((singleRoute) => router.addRoute(singleRoute));
    else router.addRoute(routes);
  }

  router.addRoute({
    path: '/:pathMatch(.*)*',
    name: 'fallback',

    component: {
      setup(_props: ExtractPropTypes<never>) {
        return () => {
          return h(Teleport, { to: 'body' }, [
            h(
              'div',
              {
                style: {
                  position: 'fixed',
                  top: '50%',
                  left: '50%',
                  transform: 'translate(-50%, -50%)',
                  width: '320px',
                  padding: '20px',
                  background: '#fff',
                  borderRadius: '8px',
                  boxShadow: '0 4px 20px rgba(0,0,0,0.15)',
                  border: '1px solid #ebeef5',
                  zIndex: 9999,
                  fontFamily: 'Arial, sans-serif',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      marginBottom: '12px',
                      fontWeight: '600',
                      fontSize: '16px',
                      color: '#303133',
                    },
                  },
                  [
                    h('span', '404'),
                    h(
                      'button',
                      {
                        onClick: async () => await router.push('/'),
                        style: {
                          border: 'none',
                          background: 'transparent',
                          cursor: 'pointer',
                          fontSize: '20px',
                          color: '#909399',
                        },
                      },
                      '×',
                    ),
                  ],
                ),
                h(
                  'div',
                  {
                    style: {
                      fontSize: '14px',
                      lineHeight: '1.6',
                      color: '#606266',
                    },
                  },
                  () => `PATH '${router.currentRoute.value.fullPath}' NOT FOUND`,
                ),
              ],
            ),
          ]);
        };
      },
    },
  });

  return modules;
};

export { router as $router };

export default router;
