const cache = new Map<string | symbol, any>();

export const $cache = () => {
    return {
        get: <T = any>(key: string | symbol): T | undefined => cache.get(key),
        set: (key: string | symbol, value: any) => {
            cache.set(key, value);
            return value;
        },
        delete: (key: string | symbol) => cache.delete(key),
        clear: () => cache.clear(),
        has: (key: string | symbol) => cache.has(key),
        size: () => cache.size,
        keys: () => cache.keys(),
        values: () => cache.values(),
        entries: () => cache.entries(),
        forEach: (callback: (value: any, key: string | symbol, map: Map<string | symbol, any>) => void) => cache.forEach(callback),
    };
};