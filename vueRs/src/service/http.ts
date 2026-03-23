import {AxiosExchange, type ExchangeConfig} from './axiosInstance.ts';
import type {MaybeRefOrGetter, Ref} from "vue";

enum HttpMethod {
    GET = "GET",
    POST = "POST",
    PUT = "PUT",
    DELETE = "DELETE"
}

export const fetchData =  <T = any, R = T>(config: Partial<MaybeRefOrGetter<ExchangeConfig<T, R>>>, dataRefContainer?: Ref<R> | ((value: R) => void)) => {
    return AxiosExchange<any>(() => {
        return {
            url: '',
            method: HttpMethod.GET,
            ignoreEmptyParameter: true,
            ...config
        }
    },dataRefContainer);
};