export const API_ModuleWorkbench = {
    getWorkbenchData: '/api/getWorkbenchData',
    getWorkbenchData2: '/api/getWorkbenchData2',
    getWorkbenchData3: '/api/getWorkbenchData3',
    getGoods: (cart_id: string | number) => `/api/getGoods/${cart_id}`,
} satisfies Record<string, Function | string>;