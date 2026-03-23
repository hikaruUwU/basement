import {ref, type Ref, shallowRef, watch} from 'vue';

interface SearchOptions<T> {
    debounceMs?: number;
    chunkSize?: number;
    searchFields: (item: T) => string[];
}

export function asyncSearcher<T>(
    sourceData: Ref<T[]>,
    searchKey: Ref<string>,
    options: SearchOptions<T>,
) {
    const filteredData = shallowRef<T[]>([]);
    const isSearching = ref(false);
    let currentSearchId = 0;

    const {debounceMs = 300, chunkSize = 200} = options;

    const performSearch = async (query: string, data: T[]) => {
        const searchId = ++currentSearchId;

        if (!query.trim()) {
            filteredData.value = data;
            isSearching.value = false;
            return;
        }

        isSearching.value = true;
        const results: T[] = [];
        const lowerKey = query.toLowerCase();

        for (let i = 0; i < data.length; i++) {
            const item = data[i];
            const fields = options.searchFields(item);
            const isMatch = fields.some((f) =>
                String(f || '').toLowerCase().includes(lowerKey),
            );

            if (isMatch) results.push(item);

            if (i > 0 && i % chunkSize === 0) {
                await new Promise((resolve) => setTimeout(resolve, 0));
                if (searchId !== currentSearchId) return;
            }
        }

        if (searchId === currentSearchId) {
            filteredData.value = results;
            isSearching.value = false;
        }
    };

    const trigger = () => {
        performSearch(searchKey.value, sourceData.value).then();
    };

    let timer: ReturnType<typeof setTimeout>;

    watch(
        [searchKey, sourceData],
        () => {
            clearTimeout(timer);
            timer = setTimeout(trigger, debounceMs);
        },
        {immediate: true},
    );

    return {
        results: filteredData,
        isSearching,
        trigger,
    };
}