import axios from "axios";
import store from "@/scripts/store";
import router from "@/scripts/router";

const axiosInterceptors = axios.create()

axiosInterceptors.interceptors.response.use(
    function(response) {
        // 200대 response를 받아 응답 데이터를 가공처리
        return response;
    },

    async (error) => {
        console.log("interceptors 시작")

        if(error.response && error.response.status === 401) {
            console.log("status : " + error.response.status);
            const {config} = error;

            // 1. local storage에 저장된 accessToken, memberId, isLoggedIn 삭제
            await store.dispatch('logout');

            // 2. refresh token을 사용하여 새로운 access token을 요청
            try {
                // access token 만료로 access token 재발급 요청
                const response = await axios.post("/api/v1/members/reissue-token");

                // refresh token이 아직 유효한 경우, 새로 발급받은 access token과 memberId를 사용하여 로그인 처리
                const newAccessToken = response.data.accessToken;
                const memberId = response.data.memberId;
                await store.dispatch('login', {accessToken: newAccessToken, memberId});

                // 재요청을 위한 original request의 header에 새로 발급받은 access token 설정
                config.headers.Authorization = `Bearer ${newAccessToken}`;

                // 3. 재요청 수행
                return axios(config);

            } catch (refreshError) {
                // refresh token이 유효하지 않을 경우 로그인 페이지로 리디렉션
                console.error("로그인 후, 이용 바랍니다");
                router.push({path: "/login"});
            }

        } else {
            // 기타 401 에러 처리
            // console.log("status : " + status);
            // console.log("config.method : " + config.method);
            // console.log("config.url : " + config.url);
            // console.log("config.headers : " + config.headers);
            // console.log("config.params : " + config.params);
            // console.log("config.data : " + config.data);
            console.error("기타 401 에러 처리")
        }
        return Promise.reject(error)
    }
)

export default axiosInterceptors