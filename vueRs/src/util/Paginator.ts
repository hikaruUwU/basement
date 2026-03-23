import { computed, type MaybeRefOrGetter, ref, toRef, watch } from 'vue';

interface PaginatorOptions<T> {
  data: MaybeRefOrGetter<T[]>;
  pageSize: MaybeRefOrGetter<number>;
  currentPage: MaybeRefOrGetter<number>;
}

export function usePresetPaginator<T>(
  dataSource: MaybeRefOrGetter<T[]>,
  options?: {
    initialPageSize?: number;
    initialCurrentPage?: number;
  },
) {
  const currentPage = ref(options?.initialCurrentPage ?? 1);
  const pageSize = ref(options?.initialPageSize ?? 10);

  const sourceDataRef = toRef(dataSource);
  watch(sourceDataRef, (_newData, _oldData) => {
    currentPage.value = 1;
  });

  const { filteredData, totalPages } = usePaginator({
    data: sourceDataRef as MaybeRefOrGetter<T[]>,
    pageSize: pageSize,
    currentPage: currentPage,
  });

  const nextPage = () => {
    if (currentPage.value < totalPages.value) currentPage.value++;
  };
  const prevPage = () => {
    if (currentPage.value > 1) currentPage.value--;
  };
  const setCurrentPage = (page: number) => {
    currentPage.value = Math.max(1, Math.min(page, totalPages.value));
  };
  const setPageSize = (size: number) => {
    pageSize.value = Math.max(1, size);
  };

  return {
    currentPage,
    pageSize,
    filteredData,
    totalPages,
    nextPage,
    prevPage,
    setCurrentPage,
    setPageSize,
  };
}

export function usePaginator<T>(options: PaginatorOptions<T>) {
  const sourceData = toRef(options.data);
  const pageSize = toRef(options.pageSize);
  const currentPage = toRef(options.currentPage);

  const filteredData = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    return sourceData.value.slice(start, end);
  });

  const currentPageSize = computed(() => filteredData.value.length);

  const totalPages = computed(() =>
    Math.ceil(sourceData.value.length / pageSize.value),
  );

  return {
    filteredData,
    pageSize,
    currentPage,
    currentPageSize,
    totalPages,
  };
}
