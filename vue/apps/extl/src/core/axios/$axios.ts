import { type MaybeRefOrGetter, readonly, shallowReactive, toRef, toValue } from 'vue';
import instance from '@/core/axios/core';
import type { AxiosResponse } from 'axios';
import { tryit } from 'radash';
import { type Api } from '@/static/api/$api';
import type { Result } from '@/static/type/result';

export const $axios = <A extends Api[string]>(from: A) => {
  return {
    call<T = Result<unknown>>(parameter: MaybeRefOrGetter<Parameters<A['assembleArg']>>) {
      const payload = shallowReactive<{
        loading: boolean;
        data: AxiosResponse<T> | null;
        error: unknown;
      }>({
        loading: false,
        data: null,
        error: null,
      });

      const aborter = new AbortController();

      const execute = async (): Promise<AxiosResponse<T>> => {
        payload.loading = true;
        payload.error = null;

        try {
          const response = await instance.request<T>({
            ...from.assembleArg(...toValue(parameter)),
            signal: aborter.signal,
          });

          payload.data = response;

          return response;
        } catch (e) {
          payload.error = e;
          throw e;
        } finally {
          payload.loading = false;
        }
      };

      return {
        execute,
        tryExecute: tryit(execute),
        aborter,
        loading: readonly(toRef(payload, 'loading')),
        data: readonly(toRef(payload, 'data')),
        error: readonly(toRef(payload, 'error')),
      };
    },
    raw: instance,
    whose: from,
  };
};
