import Cookies from 'js-cookie'

export const $cookie = () => {
    return {
        set(name: string, value: any, options?: Cookies.CookieAttributes) {
            return Cookies.set(name, value, {...options})
        },

        get(name: string): string | undefined {
            return Cookies.get(name)
        },

        remove(name: string, options?: Cookies.CookieAttributes) {
            return Cookies.remove(name, {...options})
        }
    }
}