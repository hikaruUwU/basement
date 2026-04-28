import type { AxiosRequestConfig } from 'axios';
import type { UserLogin } from '@/src/type/User.ts';

export type API = Partial<AxiosRequestConfig<unknown>> &
  Required<Pick<AxiosRequestConfig<unknown>, 'url' | 'method'>>;

export const API = {
  authenticate: (parameter: Required<UserLogin>) => ({
    url: '/authenticate',
    method: 'post',
    data: parameter,
  }),
  logout: () => ({ url: '/logout', method: 'post' }),
} satisfies Record<string, (...args: any[]) => API>;
