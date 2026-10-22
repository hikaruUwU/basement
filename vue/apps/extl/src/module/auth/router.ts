import type { RouteRecordRaw } from 'vue-router';
import { I18NMessageLoader } from '@/core/internationalization/loader';

export const authPage = Symbol('auth');

export default {
  path: '/',
  name: authPage,
  component: () => import('./index.vue'),
  beforeEnter: async () => await I18NMessageLoader.load(authPage, () => import('./locale'))
} satisfies RouteRecordRaw;
