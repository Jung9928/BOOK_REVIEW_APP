<template>
  <div class="search-container">
    <input v-model="searchQuery" type="text" placeholder="검색어를 입력하세요...">
    <button @click="search" type="button" class="search-btn">검색</button>
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
    const searchQuery = ref(''); // 검색어를 담을 ref 변수

    // 검색 버튼을 클릭했을 때 호출되는 메소드
    const search = () => {props.onSearch(searchQuery.value); // 검색어를 부모 컴포넌트로 전달
    };

    return { searchQuery, search }; // 외부로 노출할 변수와 메소드 반환
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

.search-container input[type="text"] {
  width: 300px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 10px;
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