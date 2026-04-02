import type {
    AxiosInstance,
    AxiosResponse,
    InternalAxiosRequestConfig,
} from 'axios';
import axios from 'axios';

export const instance: AxiosInstance = axios.create({
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