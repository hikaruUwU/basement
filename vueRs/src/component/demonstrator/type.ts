import type {SearchFieldSchema} from '../../widget/searcher/SearchBuildup.tsx';

export const definedTableOptions = (filteredData: any) => {
    return {
        columns: [
            {field: 'id', column: '编号'},
            {field: 'userId', column: '用户编号'},
            {field: 'title', column: '标题'},
            {field: 'body', column: '内容'},
        ],
        props: {
            data: filteredData,
            border: true,
            stripe: true,
            rowKey: 'id',
            'max-height': '450',
        },
        selector: true,
    };
};

export const definedTableSearchOptions = (): SearchFieldSchema[] => {
    return [
        {
            field: 'id',
            label: 'id',
            type: 'input',
            initialValue: '',
            width: '400px',
            attr: {
                placeholder: '....search via id',
                clearable: true,
            },
        }
    ];
};
