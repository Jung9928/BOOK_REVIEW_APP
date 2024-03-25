<template>
  <div class="form-signin w-100 m-auto">
    <form @submit.prevent>
      <h1 class="h3 mb-3 fw-normal"></h1>

      <div class="form-floating">
        <input type="memberId" class="form-control" id="floatingInput" placeholder="name@example.com" @keypress.enter="submit()" v-model="state.form.memberId">
        <label for="floatingInput">Id</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="floatingPassword" placeholder="Password" @keypress.enter="submit()" v-model="state.form.password">
        <label for="floatingPassword">Password</label>
      </div>

      <button class="btn btn-outline-dark w-100 py-2" @click="submit">Log in</button>

      <span><a href="#">ID/PW 찾기</a></span>

    </form>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import store from "@/scripts/store";
// import {saveAuthToCookie, saveUserToCookie} from "@/utils/cookies";

export default {
  setup() {
    const state = reactive({
      form :{
        memberId: "",
        password: ""
      }
    });

    // JWT access 토큰 디코딩 및 exp 클레임 추출 함수
    function decodeJWT(accessToken) {
      const base64Url = accessToken.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload);
    }

    const submit = ()=> {
      axios.post("/api/v1/members/login", state.form, {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true
      })
          .then((res) => {
            // 성공했을 경우
            const accessToken = res.headers.get("Authorization");
            console.log("accessToken : " + accessToken);

            // 토큰 디코딩 및 유효 시간 확인
            const decodedToken = decodeJWT(accessToken);
            console.log("토큰 유효 시간 : ", new Date(decodedToken.exp * 1000));

            // 로그인 액션 호출하여 로그인 상태 변경
            store.dispatch('login', true);

            router.push({path:"/"});
            window.alert("로그인하셨습니다.");

            // saveAuthToCookie(accessToken);
            // saveUserToCookie(refreshToken);
          })
          .catch((res) => {
            // 실패했을 경우
            console.error("Error message : " + res);
            window.alert("회원이 아닙니다. 회원가입 후, 로그인 바랍니다.", res);
          })
    };
    return {state, submit}
  }
}
</script>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 1rem;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="id"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

a {
  text-decoration: none;
  color: #333333;
}
</style>