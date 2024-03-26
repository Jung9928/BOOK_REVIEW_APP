import { createStore } from 'vuex'

// Create a new store instance
const store = createStore({
    state: {
        isLoggedIn: false,                                                      // 로그인 상태를 저장하는 변수
        accessToken : localStorage.getItem('accessToken') || '',            // accessToken 저장 변수         // 토큰 만료 시간 저장 변수
    },

    mutations: {
        setLoggedIn(state, value) {
            state.isLoggedIn = value;           // 로그인 상태를 변경
        },

        setAccessToken(state, accessToken) {
            state.accessToken = accessToken;
        }
    },

    actions: {
        // 로그인 처리
        login({ commit }, { accessToken }) {
            commit('setLoggedIn', true);                    // 로그인 상태를 true로 변경
            commit('setAccessToken', accessToken);          // 액세스 토큰 저장
        },

        // 로그아웃 처리
        logout({ commit }) {
            commit('setLoggedIn', false);                   // 로그인 상태를 false로 변경
            commit('setAccessToken', '');                   // 액세스 토큰 초기화
        }
    },

    getters: {
        isLoggedIn: state => !!state.isLoggedIn            // 로그인 상태 여부를 반환
    }
})

export default store;