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

    // const submit = ()=> {
    //   axios.post("/api/v1/members/login", state.form).then((res) => {
    //     store.commit('setAccount', res.data);   // 로그인 시, 로그인한 id를 store.js에 저장.
    //     sessionStorage.setItem("id", res.data);
    //     router.push({path:"/"});
    //     window.alert("로그인하셨습니다.");
    //   }).catch(() => {
    //     window.alert("로그인 정보가 존재하지 않습니다..");
    //   });
    // }

    const submit = ()=> {
      axios.post("/api/v1/members/login", state.form, {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true
      })
          .then((res) => {
            // 성공했을 경우
            const accessToken = res.data;
            const refreshToken = res.data;
            store.commit('setAccessToken', res.data);
            sessionStorage.setItem("accessToken", res.data);
            router.push({path:"/"});
            window.alert("로그인하셨습니다.", res.data);

            // saveAuthToCookie(accessToken);
            // saveUserToCookie(refreshToken);

          })
          .catch((res) => {
            // 실패했을 경우
            console.log(state);
            console.log(res.data);
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