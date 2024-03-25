import { createStore } from 'vuex'

// Create a new store instance
const store = createStore({
    state: {
        isLoggedIn: false,                          // 로그인 상태를 저장하는 변수
        accessToken : null,                         // accessToken 저장 변수
        tokenExpiration: null                       // 토큰 만료 시간 저장 변수
    },

    mutations: {
        setLoggedIn(state, value) {
            state.isLoggedIn = value;           // 로그인 상태를 변경
        },

        setAccessToken(state, accessToken) {
            state.accessToken = accessToken;
        },

        setTokenExpiration(state, expiration) {
            state.tokenExpiration = expiration;
        }
    },

    actions: {
        // 로그인 처리
        login({ commit }, { accessToken, expiration }) {
            commit('setLoggedIn', true);                    // 로그인 상태를 true로 변경
            commit('setAccessToken', accessToken);          // 액세스 토큰 저장
            commit('setTokenExpiration', expiration);       // 토큰 만료 시간 저장
        },

        // 로그아웃 처리
        logout({ commit }) {
            commit('setLoggedIn', false);                   // 로그인 상태를 false로 변경
            commit('setAccessToken', null);                 // 액세스 토큰 초기화
            commit('setTokenExpiration', null);             // 토큰 만료 시간 초기화
        }
    },

    getters: {
        isLoggedIn: state => !!state.isLoggedIn            // 로그인 상태 여부를 반환
    }
})

export default store;