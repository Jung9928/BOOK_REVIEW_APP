<template>
  <div>
    <div v-for="(review, index) in reviewList" :key="index">
      <!-- 각 아코디언 아이템의 부모 요소 -->
      <div class="accordion">
        <div class="accordion-item">
          <h2 class="accordion-header" :id="'heading' + index">
            <button class="accordion-button" type="button" @click="toggleAccordion(index)">
              {{ review.review_title }}
              <span class="stars">{{ calculateStars(review.review_rating) }}</span>
              <span style="font-size: 13px">({{review.review_rating}})</span>
            </button>
          </h2>
          <div v-if="isActiveAccordion(index)" class="accordion-collapse collapse show" :id="'collapse' + index" :aria-labelledby="'heading' + index">
            <div class="accordion-body">
              {{ review.review_content }}
              <p><br>[작성자 : {{review.reviewer}}] <br>[작성일자 : {{formatDate(review.review_date)}}] <br> [출처 : {{review.review_site}}] </p>
            </div>
          </div>
          <div v-else class="accordion-collapse collapse" :id="'collapse' + index" :aria-labelledby="'heading' + index">
            <div class="accordion-body">
              {{ review.review_content }}
              <p><br>[작성자 : {{review.reviewer}}] <br>[작성일자 : {{formatDate(review.review_date)}}] <br> [출처 : {{review.review_site}}] </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReviewTitleList",

  props: {
    book: Object,
    reviewList: Array
  },

  data() {
    return {
      activeAccordionIndices: [] // 여러 아코디언 요소의 펼쳐진 상태를 관리하는 배열
    };
  },

  methods: {
    calculateStars(rating) {
      // "평점10점"에서 "평점"과 "점"을 제거하고 숫자값으로 변환
      const numericRating = parseFloat(rating.replace("평점", "").replace("점", ""));
      // 만점을 10으로 가정하여 5개의 별 중 몇 개를 채울 것인지 계산
      const filledStars = Math.round((numericRating / 10) * 5);
      // 별표를 표시하는 문자열을 반환
      return "★".repeat(filledStars) + "☆".repeat(5 - filledStars);
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    toggleAccordion(index) {
      const currentIndex = this.activeAccordionIndices.indexOf(index);
      if (currentIndex === -1) {
        // 현재 아코디언 요소가 열려있지 않은 경우
        this.activeAccordionIndices.push(index); // 해당 아코디언 요소의 인덱스를 추가하여 열린 상태로 변경
      } else {
        // 현재 아코디언 요소가 이미 열려있는 경우
        this.activeAccordionIndices.splice(currentIndex, 1); // 해당 아코디언 요소의 인덱스를 배열에서 제거하여 닫힌 상태로 변경
      }
    },

    isActiveAccordion(index) {
      // 현재 아코디언 요소가 열려있는지 여부를 반환
      return this.activeAccordionIndices.includes(index);
    }
  }
}
</script>

<style scoped>
.stars {
  margin-left: 10px; /* 별표와 아코디언 버튼 사이의 간격 조정 */
  color: orange; /* 별표 색상 */
}
</style>
