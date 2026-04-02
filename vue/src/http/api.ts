export const $api = {
    module: {
        user: {
            login: '/login'
        },

        good: {
            list: (id: string | number) => `/good/list/${id}`
        },

        cart: {
            list: '/cart/list/unpurchased'
        }
    }
}