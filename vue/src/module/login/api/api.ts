export const API_ModuleLogin = {
  login: '/api/login',
  logout: '/api/logout',
  getUserInfo: '/api/getUserInfo',
  getPermissions: '/api/getPermissions',
  getGoods: (cart_id: string | number) => `/api/getGoods/${cart_id}`,
} satisfies Record<string, Function | string>;