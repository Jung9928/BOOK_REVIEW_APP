<template>
  <header class="p-3 text-bg-dark">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-book me-3 fs-5 fw-bold" viewBox="0 0 16 16">
            <path d="M1 2.828c.885-.37 2.154-.769 3.388-.893 1.33-.134 2.458.063 3.112.752v9.746c-.935-.53-2.12-.603-3.213-.493-1.18.12-2.37.461-3.287.811zm7.5-.141c.654-.689 1.782-.886 3.112-.752 1.234.124 2.503.523 3.388.893v9.923c-.918-.35-2.107-.692-3.287-.81-1.094-.111-2.278-.039-3.213.492zM8 1.783C7.015.936 5.587.81 4.287.94c-1.514.153-3.042.672-3.994 1.105A.5.5 0 0 0 0 2.5v11a.5.5 0 0 0 .707.455c.882-.4 2.303-.881 3.68-1.02 1.409-.142 2.59.087 3.223.877a.5.5 0 0 0 .78 0c.633-.79 1.814-1.019 3.222-.877 1.378.139 2.8.62 3.681 1.02A.5.5 0 0 0 16 13.5v-11a.5.5 0 0 0-.293-.455c-.952-.433-2.48-.952-3.994-1.105C10.413.809 8.985.936 8 1.783"/>
          </svg>
          <span class="fs-5 fw-bold">IT 도서 리뷰 모아</span>
        </a>
      </div>
    </div>
  </header>

  <nav class="py-2 bg-light border-bottom">
    <div class="container d-flex flex-wrap">
      <ul class="nav me-auto">
<!--        <li class="nav-item"><router-link to="/" class="nav-link link-dark px-2 active" aria-current="page">공지</router-link></li>-->
        <li class="nav-item"><router-link to="/" class="nav-link link-dark px-2 active" aria-current="page" data-bs-toggle="modal" data-bs-target="#announcementModal">공지</router-link></li>
        <Announcement/>

        <li class="nav-item"><router-link to="/generalForum" class="nav-link link-dark px-2">자유게시판</router-link></li>
<!--&lt;!&ndash;        <li class="nav-item"><router-link to="/" class="nav-link link-dark px-2">도서 추천게시판</router-link></li>&ndash;&gt;-->
        <li class="nav-item"><router-link to="/" class="nav-link link-dark px-2">FAQ</router-link></li>
        <li class="nav-item"><router-link to="/" class="nav-link link-dark px-2">삭제/문의</router-link></li>
      </ul>
      <ul class="nav">
        <li class="nav-item" v-if="$store.state.isLoggedIn === false">
          <router-link to="/login" class="nav-link link-dark px-2">로그인</router-link>
        </li>

        <li class="nav-item" v-else>
          <div class="dropdown">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
              <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
              <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
            </svg>
            <button class="btn btn-secondary dropdown-toggle border-0 bg-transparent" type="button" data-bs-toggle="dropdown" aria-expanded="false" style="color: black">
              {{ $store.state.memberId }}님
            </button>
            <ul class="dropdown-menu">
              <li><a href="#" class="dropdown-item" @click="goToMemberInfoPage()">회원정보 보기</a></li>
              <li><a href="/myGeneralForumList" class="dropdown-item">작성게시글 보기</a></li>
<!--              <li><a href="/myCommentList" class="dropdown-item">작성댓글 확인</a></li>-->
              <li @click="logout()" ><router-link to="/login" class="dropdown-item">로그아웃</router-link></li>
            </ul>
          </div>
        </li>

        <li class="nav-item"><router-link to="/signup" class="nav-link link-dark px-2">회원 가입</router-link></li>
      </ul>
    </div>
  </nav>
</template>

<script>
import router from "@/scripts/router";
import {computed} from "vue";
import { useStore } from 'vuex';
import axiosInterceptors from "@/common/interceptors";
import axios from "axios";
import Announcement from "@/components/Announcement";

export default {
  name: 'Header',
  components: {Announcement},
  setup() {
    const store = useStore();

    const isAccessToken = computed(() => store.getters.accessToken);

    const logout = ()=> {
      const {accessToken, memberId} = store.state;

      axiosInterceptors.delete("/api/v1/members/logout", {
        headers: {
          Authorization: 'Bearer ' + `${accessToken}`
        },
        data: {accessToken, memberId}}).then(()=> {
        console.log("로그아웃 후 accessToken : " + accessToken);
        console.log("로그아웃 후 memberId : " + memberId);
        store.dispatch('logout');
        router.push({path: "/"});
      });
    };

    const goToMemberInfoPage = () => {
      const {memberId} = store.state;
      console.log("memberId : " + memberId);
      let email = '';
      axios.get(`api/v1/members/email-info/${memberId}`)
          .then((res) => {
            // email 정보 가져오기 성공
            email = res.data.result;
            console.log("email1 : " + email);
            router.push({path: "/memberInfo", query: {memberId: memberId, email: email}});
          })
          .catch((err) => {
            console.error("error message : " + err);
            window.alert("해당 유저의 email 정보를 가져오는데 실패했습니다");
            router.push({path: "/"});
          })
    };

    return { isAccessToken, logout, goToMemberInfoPage };
  },
}
</script>

<style scoped>
header ul li a {
  cursor: pointer;
}

header .navbar {
  margin-left: auto;
  color: #fff;
}
</style>
