<template>
  <div class="search-container">
    <!-- 검색 카테고리 선택 드롭다운 메뉴 -->
    <select v-model="searchDetailCategory" class="search-category">
      <option value="title">제목</option>
      <option value="author">저자</option>
      <option value="publisher">출판사</option>
    </select>

    <!-- 검색어 입력란 -->
    <input v-model="searchValue" type="text" placeholder="도서 제목을 입력하세요..." @keyup.enter="search" class="search-input">

    <!-- 검색 버튼 -->
    <button @click="search" type="button" class="btn btn-outline-dark" style="width: 70px; height: 46px">검색</button>
  </div>
</template>

<script>
import {ref} from "vue";

export default {
  name: "SearchBox",
  props: {
    // 부모 컴포넌트로부터 전달받을 이벤트 핸들러를 props로 받음
    onSearch: {
      type: Function,
      required: true
    }
  },
  setup(props) {
    const searchValue = ref(''); // 검색어를 담을 ref 변수
    const searchDetailCategory = ref('title'); // 검색 카테고리를 담을 ref 변수 (기본값: 제목)

    // 검색 버튼을 클릭 or enter 키를 눌렀을 때 호출되는 메소드
    const search = () => {props.onSearch(searchValue.value, searchDetailCategory.value); // 검색어와 카테고리를 부모 컴포넌트로 전달
    };

    return { searchValue, search, searchDetailCategory }; // 외부로 노출할 변수와 메소드 반환
  }
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

.search-container .search-btn:hover {
  background-color: #0056b3;
}
</style>