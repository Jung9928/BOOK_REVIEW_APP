<template>
  <v-card
      title="Nutrition"
      flat
  >
    <template v-slot:text>
      <v-text-field
          v-model="search"
          label="Search"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          hide-details
          single-line
      ></v-text-field>
    </template>

    <v-data-table
        :headers="headers"
        :items="desserts"
        :search="search"
    ></v-data-table>
  </v-card>
</template>

<script>
import {ref, computed} from 'vue';
import axios from "axios";

export default {
  setup() {
    const generalForumList = ref([]);
    const currentPage = ref(1);
    const totalPages = ref(0);

    const searchGeneralForums = (searchValue, searchCategory) => {
      fetchGeneralForums(searchValue, searchCategory);
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
              generalForumList.value = res.data.data;
              totalPages.value = res.data.generalForumPaginationDTO.totalPageCnt;
            }
          })
          .catch((err) => {
            if(err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다. \n 잠시 후, 다시 시도해주세요.');
            }
          });
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

    fetchGeneralForums(); // 페이지가 로드될 때 처음에도 데이터를 가져옴

    return {
      generalForumList,
      currentPage,
      totalPages,
      goFirstPage,
      goLastPage,
      prevPage,
      nextPage,
      gotoPage,
      searchGeneralForums,
      displayedPages
    };
  },
}
</script>

<style scoped>

</style>