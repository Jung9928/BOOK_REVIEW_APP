import { createStore } from 'vuex'

// Create a new store instance
const store = createStore({
    state: {
        isLoggedIn: localStorage.getItem('isLoggedIn') || false,            // 로그인 상태 저장 변수
        accessToken : localStorage.getItem('accessToken') || '',            // accessToken 저장 변수
        memberId : localStorage.getItem('memberId') || '',                  // memberId 저장 변수
        postData : {
            postId : null,
            title : null,
            content : null,
            memberId : null,
            registeredAt : null,
            viewCount : 0
        }
    },

    mutations: {
        setLoggedIn(state, value) {
            state.isLoggedIn = value;           // 로그인 상태를 변경
        },

        setAccessToken(state, accessToken) {
            state.accessToken = accessToken;
        },

        setMemberId(state, value) {
            state.memberId = value;
        },

        setPostData(state, data) {
            state.postData = data;
        }
    },

    actions: {
        // 로그인 처리
        login({ commit }, { accessToken, memberId }) {
            commit('setLoggedIn', true);                    // 로그인 상태를 true로 변경
            commit('setAccessToken', accessToken);          // 액세스 토큰 저장
            commit('setMemberId', memberId);                // 사용자 id 저장

            // 로컬스토리지에 회원정보 저장
            localStorage.setItem('isLoggedIn', "true");
            localStorage.setItem("memberId", memberId);
            localStorage.setItem("accessToken", accessToken);
        },

        // 로그아웃 처리
        logout({ commit }) {
            commit('setLoggedIn', false);                   // 로그인 상태를 false로 변경
            commit('setAccessToken', '');                   // 액세스 토큰 초기화
            commit('setMemberId', '');                      // 사용자 id 초기화

            // 로컬 스토리지에서 로그인 정보 제거
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('memberId');
        },

        // 게시글 상세 화면 이동 시, 헤당 게시글 데이터 저장
        fetchPostData({ commit }, postData) {
            commit('setPostData', postData);
        }
    },

    getters: {
        isLoggedIn: state => !!state.isLoggedIn,            // 로그인 상태 여부를 반환

        getPostData(state) {
            return state.postData;
        }
    }
})

export default store;