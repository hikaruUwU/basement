export const $api = Object.freeze(
    {
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
)