import type { AxiosRequestConfig } from 'axios';

export type API = Partial<AxiosRequestConfig<any>> & Required<Pick<AxiosRequestConfig<any>, 'url' | 'method'>>;

export const API = {
  authenticate: (parameter: { username: string; password: string }) => ({
    url: '/authenticate',
    method: 'post',
    data: parameter,
  }),
  logout: () => ({ url: '/logout', method: 'post' }),
} satisfies Record<string, (...args: any[]) => API>;
