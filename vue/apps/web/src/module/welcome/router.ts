import type {RouteRecordRaw} from "vue-router";

const route: RouteRecordRaw = {
        path: '/',
        name: 'welcome',
        component: () => import('./index.vue'),
}

export default route