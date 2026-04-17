import { instance } from './axiosInstance.ts';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';
import {
  type MaybeRefOrGetter,
  readonly,
  shallowReactive,
  toRefs,
  toValue,
} from 'vue';
import { tryit } from 'radash';

const axios = instance;

export const $axios = {
  call: <T = any>(
    configuration: MaybeRefOrGetter<AxiosRequestConfig>,
    hooks: {
      onBefore?: (configuration: AxiosRequestConfig) => void;
      onFinally?: (
        result: [Error | undefined, AxiosResponse<T> | undefined],
      ) => void;
    } = {},
  ) => {
    const state = shallowReactive<{
      data: AxiosResponse<T> | undefined;
      error: Error | undefined;
      loading: boolean;
    }>({
      data: undefined,
      error: undefined,
      loading: false,
    });

    const execute = async () => {
      state.loading = true;
      state.error = undefined;
      hooks.onBefore?.(toValue(configuration));

      const [err, res] = await tryit(axios.request)<T>(toValue(configuration));

      [state.error, state.data] = [err as Error, res ?? undefined];

      state.loading = false;

      hooks.onFinally?.([err, res]);

      return [res, err] as const;
    };

    return {
      ...toRefs(readonly(state)),
      execute,
    };
  },
};
