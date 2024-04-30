<template>
  <div class="card shadow-sm">
    <span class="img" :style="{backgroundImage: `url(${book.imgPath})`}"/>
    <div class="card-body">
      <p class="card-text">
        <span class="bold">{{book.title}}</span>
        <span class="bold" style="margin-left: 20px; color: gold;">{{ convertToStars(book.rating) }}</span>
        <br>
        <span style="font-size: 12px;">저자 : {{book.author}}</span>
        <br>
        <span style="font-size: 12px;">출판사 : {{book.publisher}}</span>
        <br>
        <span style="font-size: 12px;">평점 : {{book.rating}}</span>
      </p>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-outline-dark" @click="fetchReviews(`${book.book_id}`, `${book.site}`, `${book.isbn}`)" data-bs-toggle="modal" :data-bs-target="'#exampleModalToggle' + book.book_id">리뷰보기</button>
        <small class="price text-muted">
          {{lib.getNumberFormatted(book.regular_price)}}
        </small>
        <small class="discount text-danger">
          {{lib.getNumberFormatted(book.selling_price)}}원
        </small>
      </div>
    </div>

    <!-- Modal 시작 -->
    <div class="modal fade" :id="'exampleModalToggle' + book.book_id" aria-hidden="true" :aria-labelledby="'exampleModalToggleLabel' + book.book_id" tabindex="-1">
      <div class="modal-dialog modal-xl">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5" :id="'exampleModalToggleLabel' + book.book_id">[{{book.title}}] 리뷰</h1>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-body">

              <template v-if="modalPopupStatus === true">
                <!-- accordion 시작 -->
                <ReviewTitleList v-bind:book="book" v-bind:reviewList="reviewList"/>
                <!-- accordion 끝 -->
              </template>

              <template v-else>
                <!-- accordion 시작 -->
                <p>리뷰가 없습니다.</p>
                <!-- accordion 끝 -->
              </template>
            </div>

            <!-- 리뷰 데이터 페이징 처리 시작 -->
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
            <!-- 리뷰 데이터 페이징 처리 끝 -->

          </div>
        </div>
      </div>
    </div>
    <!-- Modal 끝 -->

  </div>
</template>

<script>
import lib from "@/scripts/lib";
import {computed, ref} from "vue";
import axios from "axios";
import ReviewTitleList from "@/components/ReviewTitleList";

export default {
  name: "Card",
  props: {
    book: Array
  },

  components: {
    ReviewTitleList
  },

  setup(props) {

    const reviewList = ref([]);                                        // 리뷰 데이터
    const currentPage = ref(1);
    const totalPages = ref(0);
    const totalReviewCount = ref(0);
    const modalPopupStatus = ref(false);

    // book.rating 값을 별표로 변환하는 메서드
    const convertToStars = (rating) => {
      const maxRating = 10;                                   // 만점
      const stars = Math.round((rating / maxRating) * 5); // 5개 별표로 변환
      return '★'.repeat(stars) + '☆'.repeat(5 - stars); // 별표로 변환된 문자열 반환
    };

    // 리뷰 데이터 가져오기
    const fetchReviews = (book_id, site, isbn) => {

      const params = {
        page: currentPage.value,
        size: 5, // 페이지당 아이템 수
        bookId: book_id,
        isbn: isbn,
        reviewSite: site
      };

      axios.get(`/api/v1/book/reviewList`, { params, headers: {
          "Content-Type": "application/json",
        }, })
          .then((res) => {
            if (res.data.resultCode === "OK") {
              reviewList.value = res.data.data;
              totalPages.value = res.data.reviewPaginationDTO.totalPageCnt;
              totalReviewCount.value = res.data.reviewPaginationDTO.totalListCnt;

              if (totalReviewCount.value == 0) {
                modalPopupStatus.value = false;
                return
              } else {
                modalPopupStatus.value = true;
              }
            }
          })
          .catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n 잠시 후, 다시 시도해주세요.')
            }
          });
    };

    // 맨 첫페이지 이동
    const goFirstPage = () => {
      if (currentPage.value !== 1) {
        currentPage.value = 1;
        fetchReviews(props.book.book_id, props.book.site);
      }
    };

    // 맨 마지막 페이지 이동
    const goLastPage = () => {
      if (currentPage.value !== totalPages.value) {
        currentPage.value = totalPages.value;
        fetchReviews(props.book.book_id, props.book.site);
      } else {
        window.alert("현재 페이지가 마지막 페이지 입니다");
      }
    };

    // 이전 페이지 이동
    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchReviews(props.book.book_id, props.book.site);
      }
    };

    // 다음 페이지 이동
    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchReviews(props.book.book_id, props.book.site);
      }
    };

    // 페이지 선택하여 이동
    const gotoPage = (page) => {
      currentPage.value = page;
      fetchReviews(props.book.book_id, props.book.site);
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

    return {
      lib,
      ReviewTitleList,
      reviewList,
      fetchReviews,
      convertToStars,

      modalPopupStatus,

      goFirstPage,
      goLastPage,
      currentPage,
      totalPages,
      totalReviewCount,
      displayedPages,
      prevPage,
      nextPage,
      gotoPage
    };
  }
}
</script>


<style scoped>
.card .img {
  display: inline-block;
  width: 100%;
  height: 550px;
  background-size: cover;
  background-position: center;
}

.card .card-body .price {
  text-decoration: line-through;
}

.bold {
  font-weight: bold;
}

/* 리뷰 페이지네이션 css */
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