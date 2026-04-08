import type {
    AxiosInstance,
    AxiosResponse,
    InternalAxiosRequestConfig, ResponseType,
} from 'axios';
import axios from 'axios';
import axiosRetry from "axios-retry";

export const instance: AxiosInstance = axios.create({
    baseURL: import.meta.env.DEV ? '/api' : '/',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
    responseType: 'json' as ResponseType,
});

axiosRetry(instance, {
    retries: 2,
    retryDelay: (retryCount: number) => retryCount * 1000,
    retryCondition: (error) => {
        return !error.response || error.code === 'ECONNABORTED';
    },
    shouldResetTimeout: true,
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