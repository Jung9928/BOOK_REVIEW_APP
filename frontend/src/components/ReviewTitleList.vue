<template>
  <div class="accordion" id="accordionPanelsStayOpenExample">
    <div v-for="(review, index) in reviewList" :key="index" class="accordion-item">
      <h2 class="accordion-header" :id="'panelsStayOpen-heading' + index">
        <button class="accordion-button" type="button" :data-bs-toggle="'#collapse' + index" :data-bs-target="'#panelsStayOpen-collapse' + index" :aria-expanded="index === 0 ? 'true' : 'false'" :aria-controls="'panelsStayOpen-collapse' + index">
          {{ review.review_title }}
          <span class="stars">{{ calculateStars(review.review_rating) }}</span>
          <span style="font-size: 13px">({{review.review_rating}})</span>
        </button>
      </h2>
      <div :id="'panelsStayOpen-collapse' + index" class="accordion-collapse collapse" :class="{ 'show': index === 0 }" :aria-labelledby="'panelsStayOpen-heading' + index" data-bs-parent="#panelsStayOpen-accordionExample">
        <div class="accordion-body">
          {{ review.review_content }}

          <p><br>[작성자 : {{review.reviewer}}] <br>[작성일자 : {{formatDate(review.review_date)}}] <br> [출처 : {{review.review_site}}] </p>
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