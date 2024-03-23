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
        <li class="nav-item"><a href="/" class="nav-link link-dark px-2 active" aria-current="page">공지</a></li>
        <li class="nav-item"><a href="/" class="nav-link link-dark px-2">자유게시판</a></li>
        <li class="nav-item"><a href="/" class="nav-link link-dark px-2">도서 추천게시판</a></li>
        <li class="nav-item"><a href="/" class="nav-link link-dark px-2">FAQ</a></li>
        <li class="nav-item"><a href="/" class="nav-link link-dark px-2">삭제/문의</a></li>
      </ul>
      <ul class="nav">
        <li class="nav-item"><a href="/login" class="nav-link link-dark px-2">로그인</a></li>
        <li class="nav-item"><a href="/signup" class="nav-link link-dark px-2">회원가입</a></li>
      </ul>
    </div>
  </nav>
</template>

<script>
import store from "@/scripts/store";
import router from "@/scripts/router";
import axios from "axios";

export default {
  name: 'Header',
  setup() {
    const logout = ()=> {
      axios.post("/api/v1/members/logout").then(()=> {
        store.commit('setAccessToken', 0);
        router.push({path:"/"});
      })
    }

    return {logout };
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
