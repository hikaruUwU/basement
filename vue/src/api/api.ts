import type {AxiosRequestConfig} from "axios";

export type API<D = any> = Partial<AxiosRequestConfig<D>>

export const API = {
    authenticate: (parameter: {username: string; password: string}) => ({
        url: "/authenticate",
        data: parameter,
    })
} satisfies Record<string, (...args: any[]) => API>;
