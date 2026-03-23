/* eslint-disable */
import type {AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from 'axios'
import axios from 'axios'
import { ref, type Ref } from 'vue'

const instance: AxiosInstance = axios.create({baseURL: import.meta.env.DEV ? '/api' : '/', timeout: 10000})

instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        config.withCredentials = true
        return config
    },
    (error) => {
        return Promise.reject(error);
    }
)

instance.interceptors.response.use(
    (response: AxiosResponse): AxiosResponse => {
        return response
    },
    (error) => {
        return Promise.reject(error)
    }
)

export const AxiosExchange = <T = any>({
                                           url,
                                           method = 'GET' as 'GET' | 'POST' | 'PUT' | 'DELETE',
                                           parameter = {},
                                           header = {},
                                           timeout,
                                       }: {
    url: string;
    method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
    parameter?: Record<string, any>;
    header?: Record<string, any>;
    timeout?: number;
}) => {
    const isGet = method.toUpperCase() === 'GET';
    const isLoading = ref(false);
    const request: Promise<AxiosResponse<T>> = instance({
        url,
        method: method.toUpperCase(),
        params: isGet ? parameter : undefined,
        data: !isGet ? parameter : undefined,
        headers: header,
        timeout: timeout || instance.defaults.timeout,
    });
    isLoading.value = true;
    request
        .finally(() => {
            isLoading.value = false;
        });

    return {
        isLoading,
        response: request,
    }
};


// ================== Generated APIs ==================

export const register = (parameter: Record<string, any>): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: '/register',
        method: 'POST',
        parameter: parameter
    })
}

export const self = (parameter: Record<string, any>): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: '/info/me',
        method: 'GET',
        parameter: parameter
    })
}

export const information = (parameter: Record<string, any>): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: '/info',
        method: 'GET',
        parameter: parameter
    })
}

export const logout = (parameter: Record<string, any>): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: '/logout',
        method: 'GET',
        parameter: parameter
    })
}

export const authenticate = (parameter: Record<string, any>): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: '/auth',
        method: 'POST',
        parameter: parameter
    })
}

export const home = (parameter: Record<string, any> & { name: any }): { isLoading: Ref<boolean>, response: Promise<any> } => {
    return AxiosExchange<any>({
        url: `/hello/${parameter.name}`,
        method: 'GET',
        parameter: parameter
    })
}