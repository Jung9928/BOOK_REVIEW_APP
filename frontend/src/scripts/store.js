import { createStore } from 'vuex'

// Create a new store instance
const store = createStore({
    state() {
        return {payload: {
            accessToken: 0
            }
        }
    },

    mutations: {
        setAccessToken(state, payload) {
            state.payload.accessToken = payload;
        }
    }
})

export default store;