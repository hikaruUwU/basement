import type { AxiosRequestConfig } from 'axios';

export const $api = {
  checkHealthy: {
    assembleArg: (status: string, version: number) => ({
      url: `/api/healthy/${status}/${version}`,
      method: 'GET',
    }),
  },
} satisfies Api;

export type Api<TArgs extends any[] = any[]> = Record<
  string,
  {
    assembleArg: (
      ...args: TArgs
    ) => Omit<AxiosRequestConfig<unknown>, 'url' | 'method'> &
      Required<Pick<AxiosRequestConfig<unknown>, 'url' | 'method'>>;
    validateArg?: (...args: TArgs) => void;
  }
>;
