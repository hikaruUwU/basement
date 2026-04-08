export const $storage = (prefix: string = '') => {
    const getFullKey = (key: string) => prefix ? `${prefix}:${key}` : key;

    return {
        set(key: string, value: string) {
            localStorage.setItem(getFullKey(key), value);
        },

        get(key: string): string | null {
            return localStorage.getItem(getFullKey(key));
        },

        remove(key: string) {
            localStorage.removeItem(getFullKey(key));
        },

        clear() {
            if (!prefix) return localStorage.clear();
            Object.keys(localStorage)
                .filter(k => k.startsWith(`${prefix}:`))
                .forEach(k => localStorage.removeItem(k));
        }
    };
};