import {instance} from './axiosInstance.ts';
import type {AxiosRequestConfig, AxiosResponse} from 'axios';
import {type MaybeRefOrGetter, readonly, shallowReactive, toRefs, toValue} from 'vue';
import {tryit} from 'radash';

const axios = instance;

export const $axios = {
    call: <T = any>(
        configuration: MaybeRefOrGetter<AxiosRequestConfig>,
        hooks: {
            onBefore?: (configuration: AxiosRequestConfig) => boolean | void;
            onSuccess?: (data: AxiosResponse<T>) => void;
            onFailure?: (error: Error) => void;
            onFinally?: (result: [AxiosResponse<T> | undefined, Error | undefined]) => void;
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
            const axiosRequestConfig = toValue(configuration);

            if (hooks.onBefore?.(axiosRequestConfig) === false) {
                return [undefined, new Error("Request aborted by onBefore")] as const;
            }

            state.loading = true;
            state.error = undefined;

            const [err, res] = await tryit(axios.request)<T>(axiosRequestConfig);

            [state.error, state.data] = [err as Error, res ?? undefined];
            state.loading = false;

            if (res) {
                hooks.onSuccess?.(res);
            } else if (err) {
                hooks.onFailure?.(err as Error);
            }

            hooks.onFinally?.([res, err]);

            return [res, err] as const;
        };

        return {
            ...toRefs(readonly(state)),
            execute,
        };
    },
};