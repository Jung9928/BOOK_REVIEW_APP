<template>

  <!-- 게시글 목록 컴포넌트 삽입 -->
  <div class="post-table">

    <div class="my-post-list-title">
      작성 게시글 보기
    </div>

    <div class="my-post-list-count">
      Total : {{myPostTotalCount}}, Page : {{myPostCurrentPage}} / {{myPostTotalPage}}
    </div>

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
      <tbody v-if="myGeneralForumList" class="table-group-divider">
      <tr v-for="item in myGeneralForumList" :key="item.post_id" @click="getContent(item)">
        <td>{{ item.번호 }}</td>
        <td>{{ item.제목 }}</td>
        <td>{{ item.글쓴이 }}</td>
        <td>{{ item.날짜 }}</td>
        <td>{{ item.조회수 }}</td>
      </tr>
      </tbody>
      <tbody v-else>
        로딩 중...
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

import {computed, ref} from "vue";
import axios from "axios";
import store from "@/scripts/store";
import router from "@/scripts/router";

export default {
  name: "MyGeneralForumList",

  setup() {

    const myGeneralForumList = ref([]);
    const myPostTotalCount = ref(0);
    const myPostTotalPage = ref(0);
    const myPostCurrentPage = ref(0);
    let postId = null;

    const currentPage = ref(1);
    const totalPages = ref(1);

    const getMyGeneralForumList = () => {
      const {accessToken} = store.state;

      axios.get(`api/v1/posts/generalForum/my-post-list`, {
        headers: {
          Authorization: 'Bearer ' + `${accessToken}`
        },
        data: {accessToken}
      })
          .then((res) => {
            // 작성한 게시글 데이터 가져오기 성공
            console.log("res.content : " + res.data.content);
            myGeneralForumList.value = res.data.content.map(item => ({
              번호 : item.post_id,
              제목 : item.title,
              글쓴이 : item.member_id,
              날짜 : formatDate(item.registeredAt),
              조회수 : item.vw_cnt
            }));

            myPostTotalCount.value = res.data.totalElements;
            myPostTotalPage.value = res.data.totalPages;
            console.log("myPostTotalCount : " + myPostTotalCount.value);
            console.log("myPostCurrentPage : " + res.data.size);
            console.log("cal : " + (myPostTotalCount.value / res.data.size));
            myPostCurrentPage.value = Math.trunc(myPostTotalCount.value / res.data.size) + 1;

          })
          .catch((err) => {
            console.error("error message : " + err);
            window.alert("해당 유저의 작성 게시글 목록 정보를 가져오는데 실패했습니다");
            // router.push({path: "/"});
          })
    };

    // YYYY.MM.DD 형식으로 날짜 변환하는 함수
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}.${month}.${day}`;
    };

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
            // router.push({path:"/generalForumPostDetail", query: {postId : postId, title: title, content: content, memberId: memberId}});

            const postData = {
              postId : postId,
              title : title,
              content : content,
              memberId : memberId
            }

            // Vuex 액션 호출하여 데이터 저장
            store.dispatch('fetchPostData', postData);

            router.push({path:`/generalForumPostDetail`});
          })
          .catch((err) => {
            console.error('게시글을 가져오는 중 오류 발생:', err);
          });
    };

    // 맨 첫페이지 이동
    const goFirstPage = () => {
      if (currentPage.value !== 1) {
        currentPage.value = 1;
        getMyGeneralForumList();
      }
    };

    // 맨 마지막 페이지 이동
    const goLastPage = () => {
      if (currentPage.value !== totalPages.value) {
        currentPage.value = totalPages.value;
        getMyGeneralForumList();
      } else {
        window.alert("현재 페이지가 마지막 페이지 입니다");
      }
    };

    // 이전 페이지 이동
    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        getMyGeneralForumList();
      }
    };

    // 다음 페이지 이동
    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        getMyGeneralForumList();
      }
    };

    // 페이지 선택하여 이동
    const gotoPage = (page) => {
      currentPage.value = page;
      getMyGeneralForumList();
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

    getMyGeneralForumList();

    return {
      getMyGeneralForumList,
      myGeneralForumList,
      myPostTotalCount,
      myPostTotalPage,
      myPostCurrentPage,
      formatDate,
      getContent,

      goFirstPage,
      goLastPage,
      prevPage,
      nextPage,
      gotoPage,
      displayedPages,
      currentPage,
      totalPages
    }
  }
}
</script>

<style scoped>
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

.my-post-list-title {
  margin-top: 10px;
  margin-bottom: 10px;
  font-weight: bold;
  font-size: 25px;
}

.my-post-list-count {
  font-size: 15px;
  font-weight: bold;
}
</style>