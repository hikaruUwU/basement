import { type ComputedRef, type Ref, ref } from 'vue';
import type { TableProps } from 'element-plus';

export interface ProTableColumn {
  field: string;
  column: string;
  hidden?: boolean;
  width?: string | number;
  fixed?: boolean | 'left' | 'right';
  align?: 'left' | 'center' | 'right';
  props?: any;
}

export interface TableOptions<T extends Record<PropertyKey, any>> {
  props?: Omit<Partial<TableProps<T>>, 'data'> & {
    data?: T[] | Ref<T[]> | ComputedRef<T[]>;
  };
  columns: ProTableColumn[];
  selector: boolean;
}

export interface TableAction {
  reload: () => void;
  destroy: () => void;
  getElTableExpose: () => any;
  getSelected: () => any[];
}

export function useTable<T = any>() {
  const tableActions = ref<TableAction | null>(null);

  let rawData: T[] | Ref<T[]> | undefined;

  const register = (options: TableOptions<any>) => {
    rawData = options.props?.data;
    return {
      schema: options,
      onBuildup: (actions: TableAction) => {
        tableActions.value = actions;
      },
    };
  };

  return {
    register,
    rawData,
    reload: () => tableActions.value?.reload(),
    destroy: () => tableActions.value?.destroy(),
    getExpose: () => tableActions.value?.getElTableExpose(),
    getSelected: () => tableActions.value?.getSelected() || [],
  };
}
