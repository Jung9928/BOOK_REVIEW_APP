<template>
  <div class="home">
    <div class="sidebar">
      <hr> <!-- 가로줄 추가 -->
      <ul>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003031')">컴퓨터 공학</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003019')">컴퓨터 입문/활용</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003023')">모바일 프로그래밍</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003022')">프로그래밍 언어</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003020')">웹사이트</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003025')">OS/데이터베이스</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003027')">게임</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003024')">네트워크/해킹/보안</a></li>
        <li><a href="#" @mouseover="increaseFontSize" @mouseleave="decreaseFontSize" @click="searchBooks('001001003028')">그래픽/디자인/멀티미디어</a></li>
      </ul>
    </div>
    <div class="content">
      <SearchBox @search="searchBooks"/>
      <div class="album py-5 bg-light">
        <div class="container">
          <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <div class="col" v-for="(book, idx) in bookList" :key="idx">
              <Card :book="book"/>
            </div>
          </div>
          <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link" @click="prevPage">이전</button>
              </li>
              <li class="page-item" v-for="page in displayedPages" :key="page" :class="{ active: currentPage === page }">
                <button class="page-link" @click="gotoPage(page)">{{ page }}</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link" @click="nextPage">다음</button>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {ref, computed} from "vue";
import Card from "@/components/Card";
import SearchBox from "@/components/SearchBox";

export default {
  name: "Home",
  components: {SearchBox, Card},

  setup() {
    const bookList = ref([]);
    const currentPage = ref(1);
    const totalPages = ref(0);
    const seletedCategory = ref("");

    // 검색 버튼 클릭 시, 카테고리, 검색 데이터 전달
    const searchBooks = (searchValue, searchSubCategory, searchMainCategory) => {
      fetchBooks(searchValue, searchSubCategory, searchMainCategory);
    };

    const fetchBooks = (searchValue, searchSubCategory, searchMainCategory) => {
      const params = {
        page: currentPage.value,
        size: 10, // 페이지당 아이템 수
        searchValue: searchValue,
        searchSubCategory: searchSubCategory,
        searchMainCategory: searchMainCategory
      };

      axios.get(`/api/v1/book/list`, { params })
          .then((res) => {
            if (res.data.resultCode === "OK") {
              bookList.value = res.data.data;
              totalPages.value = res.data.bookPaginationDTO.totalPageCnt;
            }
          })
          .catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n 잠시 후, 다시 시도해주세요.')
            }
          });
    };

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchBooks();
      }
    };

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchBooks();
      }
    };

    const gotoPage = (page) => {
      currentPage.value = page;
      fetchBooks();
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

    fetchBooks(); // 페이지가 로드될 때 처음에도 데이터를 가져옴

    return { bookList, currentPage, totalPages, prevPage, nextPage, searchBooks, displayedPages, gotoPage };
  },
}
</script>

<style scoped>
.home {
  display: flex;
}

.sidebar {
  width: 200px;
  background-color: #f0f0f0;
  padding: 20px;
}

.sidebar h2 {
  margin-bottom: 10px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 5px;
}

.sidebar ul li a {
  text-decoration: none;
  color: #333;
  transition: font-size 0.2s; /* 글씨 크기 변화에 대한 전환 효과 */
}

.sidebar ul li a:hover {
  cursor: pointer; /* 마우스 호버 시 커서 모양 변경 */
  font-weight: bold; /* 마우스 호버 시 글씨 굵게 */
}

.content {
  flex: 1;
}
</style>