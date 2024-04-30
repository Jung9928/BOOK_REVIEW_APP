<template>

  <!-- 게시글 목록 컴포넌트 삽입 -->
  <div class="post-table">

    <div class="my-post-list-title">
      작성 댓글 보기
    </div>

    <div class="my-post-list-count">
      Total : {{myPostTotalCount}}, Page : {{myPostCurrentPage}} / {{myPostTotalPage}}
    </div>

    <table class="table table-hover">
      <colgroup>
        <col width="15%">
        <col width="50%">
        <col width="15%">
      </colgroup>
      <thead class="table-light">
      <tr>
        <th scope="col">번호</th>
        <th scope="col">내용</th>
        <th scope="col">날짜</th>
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
export default {
  name: "MyCommentList"
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