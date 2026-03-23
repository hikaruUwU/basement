import type {
    AxiosInstance,
    AxiosResponse,
    InternalAxiosRequestConfig,
} from 'axios';
import axios from 'axios';
import {isRef, type MaybeRefOrGetter, type Ref, ref, toValue} from 'vue';

const instance: AxiosInstance = axios.create({
    baseURL: import.meta.env.DEV ? '/api' : '/',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
    responseType: 'json',
});

instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        config.withCredentials = true;
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

instance.interceptors.response.use(
    (response: AxiosResponse): AxiosResponse => {
        return response;
    },
    (error) => {
        return Promise.reject(error);
    },
);

export interface ExchangeConfig<T = any, R = T> {
    url: string;
    method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
    parameter?: MaybeRefOrGetter<Record<string, any>>;
    header?: MaybeRefOrGetter<Record<string, any>>;
    timeout?: number;
    ignoreEmptyParameter?: boolean;

    beforeGain?: () => boolean | Promise<boolean>;
    afterGain?: () => void;

    beforeResponse?: (data: T) => R;
}

export const AxiosExchange = <T = any, R = T>(
    config: MaybeRefOrGetter<ExchangeConfig<T, R>>,
    dataRefContainer?: Ref<R> | ((value: R) => void)
) => {
    const isLoading = ref<boolean>(false);

    const responseRef = ref<R | null>(null);

    const execute = async () => {
        const cfg = toValue(config);
        const {
            url,
            method = 'GET',
            parameter = {},
            header = {},
            timeout,
            ignoreEmptyParameter = false,
            beforeGain = () => true,
            afterGain,
            beforeResponse
        } = cfg;

        const shouldContinue = await beforeGain();
        if (!shouldContinue) return;

        let resolvedParams = toValue(parameter);

        if (ignoreEmptyParameter && resolvedParams) {
            resolvedParams = Object.fromEntries(
                Object.entries(resolvedParams).filter(([_, v]) =>
                    v !== undefined && v !== null && v !== ''
                )
            );
        }

        const resolvedHeaders = toValue(header);
        const isGet = method.toUpperCase() === 'GET';

        isLoading.value = true;

        try {
            const res = await instance({
                url,
                method: method.toUpperCase(),
                params: isGet ? resolvedParams : undefined,
                data: !isGet ? resolvedParams : undefined,
                headers: resolvedHeaders,
                timeout: timeout || instance.defaults.timeout,
            });

            const rawData = res.data;

            const finalData = beforeResponse ? beforeResponse(rawData) : (rawData as unknown as R);

            responseRef.value = finalData;

            if (dataRefContainer) {
                if (typeof dataRefContainer === 'function') {
                    dataRefContainer(finalData);
                } else if (isRef(dataRefContainer)) {
                    dataRefContainer.value = finalData;
                }
            }
        } catch (err) {
            console.error('Request Failed:', err);
        } finally {
            isLoading.value = false;
            if (afterGain) afterGain();
        }
    };

    return {
        isLoading,
        response: responseRef,
        execute,
    };
};