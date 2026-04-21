import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig, ResponseType } from 'axios';
import axios from 'axios';
import axiosRetry from 'axios-retry';
import { $notify } from '@shared/notification/notify.ts';

export const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.PUBLIC_AXIOS_BASE_URL as string,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  responseType: 'json' as ResponseType,
});

axiosRetry(instance, {
  retries: 1,
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
    $notify.call({
      message: error,
      ...$notify.preset.error,
    });
    return Promise.reject(error);
  },
);
