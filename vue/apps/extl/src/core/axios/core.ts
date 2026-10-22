import axios from 'axios';
import { AcceptLanguage } from '@/core/axios/acceptLanguage';

const instance = axios.create({
  baseURL: '/api',
  timeout: 10_000,
  headers: {
    'Content-Type': 'application/json',
  },
});

instance.interceptors.request.use((config) => {
  config.headers['Accept-Language'] = AcceptLanguage.value;

  return config;
});

instance.interceptors.response.use(
  (response) => response,
  async (error) => Promise.reject(error),
);

export default instance;
