<template>
  <div class="card shadow-sm">
    <span class="img" :style="{backgroundImage: `url(${book.imgPath})`}"/>
    <div class="card-body">
      <p class="card-text">
        <span class="bold">{{book.title}}</span>
      </p>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-primary" @click="fetchReviews(`${book.book_id}`, `${book.site}`)" data-bs-toggle="modal" href="#exampleModalToggle">리뷰보기</button>
        <small class="price text-muted">
          {{lib.getNumberFormatted(book.regular_price)}}
        </small>
        <small class="discount text-danger">
          {{lib.getNumberFormatted(book.selling_price)}}원
        </small>
      </div>
    </div>

    <!-- ReviewTitleListModal 컴포넌트를 동적으로 렌더링하기 위한 조건문입니다. -->
    <ReviewTitleListModal v-if="showReviewTitleListModal" @close="closeReviewTitleListModal" v-bind:book="book"/>
  </div>
</template>

<script>
import lib from "@/scripts/lib";
import ReviewTitleListModal from "@/components/ReviewTitleListModal";
import {ref} from "vue";
import axios from "axios";

export default {
  name: "Card",
  props: {
    book: Object
  },

  components: {
    ReviewTitleListModal
  },

  setup() {

    const showReviewTitleListModal = ref(false);                                   // 모달 표시 여부 관리 변수
    // const openReviewTitleListModal = () => {showReviewTitleListModal.value = true;}     // ReviewTitleListModal 팝업을 위한 메소드
    // const closeReviewTitleListModal = () => {showReviewTitleListModal.value = false;}   // ReviewTitleListModal 팝업을 닫기 위한 메소드

    const reviewList = ref([]);                                                    // 리뷰 데이터
    const currentPage = ref(1);
    const totalPages = ref(0);

    // ReviewTitleListModal 팝업을 위한 메소드
    const openReviewTitleListModal = () => {
      showReviewTitleListModal.value = true;
    }

    // ReviewTitleListModal 팝업을 닫기 위한 메소드
    const closeReviewTitleListModal = () => {
      showReviewTitleListModal.value = false;
    }

    // 리뷰 데이터 가져오기
    const fetchReviews = (book_id, site) => {
      const params = {
        page: currentPage.value,
        size: 10, // 페이지당 아이템 수
        bookId: book_id,
        reviewSite: site
      };

      axios.get(`/api/v1/book/reviewList`, { params })
          .then((res) => {
            if (res.data.resultCode === "OK") {
              reviewList.value = res.data.data;
              totalPages.value = res.data.ReviewPaginationDTO.totalPageCnt;
            }
          })
          .catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n 잠시 후, 다시 시도해주세요.')
            }
          });

      openReviewTitleListModal(); // 모달 열기
    };

    return {
      lib,
      showReviewTitleListModal,
      openReviewTitleListModal,
      closeReviewTitleListModal,
      reviewList,
      fetchReviews
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
</style>