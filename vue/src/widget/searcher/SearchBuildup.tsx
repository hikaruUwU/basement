import {ref} from 'vue';
import type {InputProps} from 'element-plus';

export type SearchFieldType =
    | 'input'
    | 'radio'
    | 'select'
    | 'textarea'
    | 'number';

export interface SearchFieldSchema {
    field: string;
    label?: string;
    attr?: Partial<InputProps>;
    type: SearchFieldType;
    initialValue?: any;
    required?: boolean;
    margin?: string;
    width?: string | number;
    height?: string | number;
    options?: { label: string; value: any }[];
    placeholder?: string;
    customizedRender?: (form: Record<string, any>) => any;
}

export interface SearchBarOptions {
    schema: SearchFieldSchema[];
    gutter?: number;
}

export function useSearch(options: SearchBarOptions) {
    const formData = ref<Record<string, any>>({});

    options.schema.forEach((item: SearchFieldSchema) => {
        formData.value[item.field] =
            item.initialValue ?? (item.type === 'number' ? 0 : '');
    });


    const resetForm = (field?: string) => {
        if (field) {
            const item = options.schema.find(i => i.field === field);
            if (item) {
                formData.value[field] = item.initialValue ?? (item.type === 'number' ? 0 : '');
            }
        } else {
            options.schema.forEach((item) => {
                formData.value[item.field] = item.initialValue ?? (item.type === 'number' ? 0 : '');
            });
        }
    };

    const schema = options.schema;

    return {
        schema,
        formData,
        resetForm,
    };
}
