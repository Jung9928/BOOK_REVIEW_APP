<template>
  <div class="home">
    <SearchBox @search="searchBooks"/>
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <div class="col" v-for="(book, idx) in state.paginatedBooks" :key="idx">
            <Card :book="book"/>
          </div>
        </div>
        <nav aria-label="Page navigation example">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: state.currentPage === 1 }">
              <button class="page-link" @click="prevPage">이전</button>
            </li>
            <li class="page-item" v-for="page in displayedPages" :key="page" :class="{ active: state.currentPage === page }">
              <button class="page-link" @click="gotoPage(page)">{{ page }}</button>
            </li>
            <li class="page-item" :class="{ disabled: state.currentPage === state.totalPages }">
              <button class="page-link" @click="nextPage">다음</button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {reactive, onMounted, computed} from "vue";
import Card from "@/components/Card";
import SearchBox from "@/components/SearchBox";

export default {
  name: "Home",
  components: {SearchBox, Card},
  setup() {
    const state = reactive({
      books: [],
      title: '',
      paginatedBooks: [],
      currentPage: 1,
      pageSize: 15,
      totalPages: 0,
      numDisplayedPages: 5 // 보여질 페이지 수
    })

    const fetchBooks = async () => {
      try {
        const response = await axios.get(`/api/v1/book/list/${state.title}`, {
          params: {
            page: state.currentPage - 1,
            size: state.pageSize
          }
        });

        const { content, totalPages, number } = response.data;
        state.books = content;
        state.totalPages = totalPages;
        state.currentPage = number + 1;
        updatePaginatedBooks();

      } catch (error) {
        console.error('에러 발생 :', error);
      }
    };

    // 검색어를 SearchBox.vue에서 받아와서 state.title에 셋팅
    const searchBooks = (searchTitle) => {
      state.title = searchTitle;
      fetchBooks();
    }

    const updatePaginatedBooks = () => {
      const startIndex = (state.currentPage - 1) * state.pageSize;
      const endIndex = Math.min(startIndex + state.pageSize, state.books.length);
      state.paginatedBooks = state.books.slice(startIndex, endIndex);
    };

    const nextPage = () => {
      if (state.currentPage < state.totalPages) {
        state.currentPage++;
        updatePaginatedBooks();
      }
    };

    const prevPage = () => {
      if (state.currentPage > 1) {
        state.currentPage--;
        updatePaginatedBooks();
      }
    };

    const gotoPage = (page) => {
      state.currentPage = page;
      updatePaginatedBooks();
    };

    const displayedPages = computed(() => {
      const startPage = Math.max(1, state.currentPage - Math.floor(state.numDisplayedPages / 2));
      const endPage = Math.min(state.totalPages, startPage + state.numDisplayedPages - 1);
      const pages = [];
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      return pages;
    });

    onMounted(() => {
      fetchBooks();
    });

    return { state, nextPage, prevPage, gotoPage, displayedPages, searchBooks };
  },
}
</script>

<style scoped>

</style>