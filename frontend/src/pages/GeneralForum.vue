<template>
  <div class="search-container">
    <!-- 검색 카테고리 선택 드롭다운 메뉴 -->
    <select v-model="searchCategory" class="search-category">
      <option value="title">제목</option>
      <option value="memberId">작성자ID</option>
      <option value="content">내용</option>
      <option value="comment">댓글</option>
    </select>

    <!-- 검색어 입력란 -->
    <input v-model="searchValue" type="text" placeholder="검색어를 입력하세요..." @keyup.enter="search" class="search-input">

    <!-- 검색 버튼 -->
    <button @click="fetchGeneralForums" type="button" class="btn btn-outline-dark" style="width: 70px; height: 46px">검색</button>
  </div>

  <!-- "글 작성하기" 버튼 -->
  <button @click="navigateToWritePage" type="button" class="btn btn-outline-dark btn-write-post">
    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
      <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
      <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
    </svg>
    글 작성하기
  </button>

  <!-- 게시글 목록 컴포넌트 삽입 -->
<!--  <PostView :items="generalForumList" :fields="fields" @click="getContent"/>-->
  <div class="post-table">
<!--    <b-table class="post-table" :items="generalForumList" @row-clicked="getContent"></b-table>-->
    <table class="table table-hover">
      <colgroup>
        <col width="15%">
        <col width="50%">
        <col width="15%">
        <col width="10%">
        <col width="10%">
      </colgroup>
      <thead class="table-light">
      <tr>
        <th scope="col">번호</th>
        <th scope="col">제목</th>
        <th scope="col">글쓴이</th>
        <th scope="col">날짜</th>
        <th scope="col">조회수</th>
      </tr>
      </thead>
      <tbody class="table-group-divider">
      <tr v-for="item in generalForumList" :key="item.post_id" @click="getContent(item)">
        <td>{{ item.번호 }}</td>
        <td>{{ item.제목 }}</td>
        <td>{{ item.글쓴이 }}</td>
        <td>{{ item.날짜 }}</td>
        <td>{{ item.조회수 }}</td>
      </tr>
      </tbody>
    </table>
  </div>

  <!-- 페이지네이션 -->
  <nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <button class="page-link" @click="goFirstPage">&laquo;</button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <button class="page-link" @click="prevPage">&lt;</button>
      </li>
      <li class="page-item" v-for="page in displayedPages" :key="page" :class="{ active: currentPage === page }">
        <button class="page-link" @click="gotoPage(page)">{{ page }}</button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === totalPages }">
        <button class="page-link" @click="nextPage">&gt;</button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === totalPages }">
        <button class="page-link" @click="goLastPage">&raquo;</button>
      </li>
    </ul>
  </nav>
</template>

<script>
import {ref, computed} from 'vue';
import axios from "axios";
import router from "@/scripts/router";
import store from "@/scripts/store";

export default {

  setup() {
    const generalForumList = ref([]);
    const currentPage = ref(1);
    const totalPages = ref(1);
    const searchCategory = ref('title');
    const searchValue = ref('');

    const commentList = ref([]);
    let postId = null;

    const searchGeneralForums = (searchValue, searchCategory) => {
      fetchGeneralForums(searchValue.value, searchCategory.value);
    }

    const fetchGeneralForums = (searchValue, searchCategory) => {
      const params = {
        page: currentPage.value,
        size: 20,                         // 페이지 당 게시글 수
        searchValue: searchValue,         // 검색내용
        searchCategory: searchCategory    // 카테고리
      };

      axios.get(`/api/v1/posts/generalForumList`, { params })
          .then((res) => {
            if(res.data.resultCode == "OK") {

              console.log("searchValue : " + searchValue);
              console.log("searchCategory : " + searchCategory);

              generalForumList.value = res.data.data.map(item => ({
                번호 : item.post_id,
                제목 : item.title,
                글쓴이 : item.member_id,
                날짜 : formatDate(item.registeredAt),
                조회수 : item.vw_cnt
              }));
              console.log("generalForumList : " );
              totalPages.value = res.data.generalForumPaginationDTO.totalPageCnt;
            }
          })
          .catch((err) => {
            console.log("searchValue : " + searchValue);
            console.log("searchCategory : " + searchCategory);
            if(err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다. \n 잠시 후, 다시 시도해주세요.');
            }
          });
    };

    // YYYY.MM.DD 형식으로 날짜 변환하는 함수
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}.${month}.${day}`;
    };

    // YYYY.MM.DD 형식으로 날짜 변환하는 함수
    const formatDateTime = (dateString) => {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${year}.${month}.${day} ${hours}:${minutes}`.toString();
    };


    // 맨 첫페이지 이동
    const goFirstPage = () => {
      if (currentPage.value !== 1) {
        currentPage.value = 1;
        fetchGeneralForums();
      }
    };

    // 맨 마지막 페이지 이동
    const goLastPage = () => {
      if (currentPage.value !== totalPages.value) {
        currentPage.value = totalPages.value;
        fetchGeneralForums();
      } else {
        window.alert("현재 페이지가 마지막 페이지 입니다");
      }
    };

    // 이전 페이지 이동
    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchGeneralForums();
      }
    };

    // 다음 페이지 이동
    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchGeneralForums();
      }
    };

    // 페이지 선택하여 이동
    const gotoPage = (page) => {
      currentPage.value = page;
      fetchGeneralForums();
    };

    // computed 속성을 통해 현재 페이지의 표시할 페이지 수 계산
    const displayedPages = computed(() => {
      const numPages = [];
      const numDisplayed = 5; // 표시할 페이지 번호 개수
      const halfDisplayed = Math.floor(numDisplayed / 2);
      let startPage = Math.max(1, currentPage.value - halfDisplayed);
      let endPage = Math.min(totalPages.value, startPage + numDisplayed - 1);

      if (totalPages.value <= numDisplayed) {
        startPage = 1;
        endPage = totalPages.value;
      } else if (currentPage.value <= halfDisplayed) {
        endPage = numDisplayed;
      } else if (currentPage.value >= totalPages.value - halfDisplayed) {
        startPage = totalPages.value - numDisplayed + 1;
      }

      for (let i = startPage; i <= endPage; i++) {
        numPages.push(i);
      }

      return numPages;
    });

    const navigateToWritePage = () => {
      router.push({path:"/generalForumPost"});
    }

    // 게시글 목록을 클릭해서 게시글 상세내용 가져오기
    const getContent = (item) => {

      // const postId = JSON.stringify(item['번호']);
      postId = JSON.stringify(item['번호']);
      console.log("postId : " + postId);

      // 게시글 번호를 기반으로 해당 게시글의 내용을 가져오는 로직
      axios.get(`/api/v1/posts/generalForum/${postId}`)
          .then((res) => {
            // 가져온 게시글 데이터 처리
            // console.log("title : " + JSON.stringify(res.data.result.title));
            const title = res.data.result.title;
            // console.log("content : " + JSON.stringify(res.data.result.content));

            const content = res.data.result.content;
            const memberId = res.data.result.member_id;
            const registeredAt = formatDateTime(res.data.result.registeredAt);
            const viewCount = res.data.result.vw_cnt;
            // router.push({path:"/generalForumPostDetail", query: {postId : postId, title: title, content: content, memberId: memberId}});

            const postData = {
              postId : postId,
              title : title,
              content : content,
              memberId : memberId,
              registeredAt : registeredAt,
              viewCount : viewCount
            }

            // Vuex 액션 호출하여 데이터 저장
            store.dispatch('fetchPostData', postData);

            router.push({path:`/generalForumPostDetail`});
          })
          .catch((err) => {
            console.error('게시글을 가져오는 중 오류 발생:', err);
          });
    }

    fetchGeneralForums(); // 페이지가 로드될 때 처음에도 데이터를 가져옴

    return {
      generalForumList,
      commentList,
      searchCategory,
      searchValue,
      currentPage,
      totalPages,
      goFirstPage,
      goLastPage,
      prevPage,
      nextPage,
      gotoPage,
      navigateToWritePage,
      displayedPages,

      searchGeneralForums,
      getContent,
      fetchGeneralForums,
    };
  },
}
</script>

<style scoped>
.search-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}

.search-container select, .search-container input[type="text"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 10px;
}

.search-input {
  width: calc(15%); /* 입력란 좌우 너비 조절 */
}

.search-container .search-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.post-table {
  max-width: 1000px;
  margin: 0 auto;
}

.post-table tbody tr {
  background-color: transparent !important;
}

.search-container .search-btn:hover {
  background-color: #0056b3;
}

/* 글 작성하기 버튼을 우측으로 이동 */
.btn-write-post {
  margin-left: 68%; /* 현재 위치로부터 우측으로 이동 */
  border: none;
}

/* 페이지네이션 css */
.page-link {
 color: #000;
 background-color: #fff;
 border: 1px solid #ccc;
}

.page-item.active .page-link {
  z-index: 1;
  color: #555;
  font-weight:bold;
  background-color: #f1f1f1;
  border-color: #ccc;

}

.page-link:focus, .page-link:hover {
  color: #000;
  background-color: #fafafa;
  border-color: #ccc;
}

</style>