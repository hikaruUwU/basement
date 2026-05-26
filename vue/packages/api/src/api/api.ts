import type { AxiosRequestConfig } from 'axios';

export type API = Partial<AxiosRequestConfig<unknown>> & Required<Pick<AxiosRequestConfig<unknown>, 'url' | 'method'>>;

export enum HttpMethod {
    GET = 'GET',
    POST = 'POST',
    PUT = 'PUT',
    DELETE = 'DELETE'
}

export const $api = {
    demo : (username: string) => ({
        url: '/who/am/i',
        method: HttpMethod.POST,
        data: {
            username
        }
    })

} satisfies Record<string | symbol, (...args: any[]) => API>;
