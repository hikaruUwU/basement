import {createRouter, createWebHashHistory, type Router, type RouteRecordRaw} from 'vue-router';
import NProgress from 'nprogress';
import {ElEmpty} from 'element-plus';
import {defineComponent} from 'vue';

import('nprogress/nprogress.css');

const Fallback = defineComponent(() => {
    return () => <ElEmpty description="Not Found" style={{margin: '100px'}}/>;
});

let _fixed_routing: RouteRecordRaw[] = [];

const router: Router = createRouter({
    history: createWebHashHistory(),
    routes: [
        ..._fixed_routing
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

    for (const key of context.keys()) {
        const mod = (await context(key)) as any;
        const routes: Array<RouteRecordRaw> = mod.route || mod.default;

        if (Array.isArray(routes)) {
            routes.forEach((singleRoute) => {
                router.addRoute(singleRoute);
            });
        } else {
            router.addRoute(routes)
        }
    }

    router.addRoute({
        path: '/:pathMatch(.*)*',
        name: 'fallback',
        component: Fallback,
   })

    return context;
};

export {router as $router};
export default router;
