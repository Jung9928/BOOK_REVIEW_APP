<template>
  <div>
    <div v-for="(review, index) in reviewList" :key="index">
      <!-- 각 아코디언 아이템의 부모 요소 -->
<!--      <div class="accordion">-->
<!--        <div class="accordion-item">-->
<!--          <h2 class="accordion-header" :id="'heading' + index">-->
<!--            <button class="accordion-button"  type="button" @click="toggleAccordion(index)">-->
<!--              {{ review.review_title }}-->
<!--              <span class="stars">{{ calculateStars(review.review_rating) }}</span>-->
<!--              <span style="font-size: 13px">({{review.review_rating}})</span>-->
<!--            </button>-->
<!--          </h2>-->
<!--          <div v-if="isActiveAccordion(index)" class="accordion-collapse collapse show" :id="'collapse' + index" :aria-labelledby="'heading' + index">-->
<!--            <div class="accordion-body">-->
<!--              {{ review.review_content }}-->
<!--              <p><br>[작성자 : {{review.reviewer}}] <br>[작성일자 : {{formatDate(review.review_date)}}] <br> [출처 : {{review.review_site}}] </p>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div v-else class="accordion-collapse collapse" :id="'collapse' + index" :aria-labelledby="'heading' + index">-->
<!--            <div class="accordion-body">-->
<!--              {{ review.review_content }}-->
<!--              <p><br>[작성자 : {{review.reviewer}}] <br>[작성일자 : {{formatDate(review.review_date)}}] <br> [출처 : {{review.review_site}}] </p>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

      <div class="accordion" id="'accordionPanelsStayOpenExample' + index">
        <div class="accordion-item">
          <h2 class="accordion-header" id="'panelsStayOpen-headingOne' + index">
            <button class="accordion-button" type="button" :data-bs-toggle="'collapse'" :data-bs-target="'#panelsStayOpen-collapseOne' + index" :aria-expanded="activeIndex === index ? 'true' : 'false'" @click="toggleAccordion(index)">
              {{ review.review_title }}
              <span class="stars">{{ calculateStars(review.review_rating) }}</span>
              <span style="font-size: 13px">({{ review.review_rating }})</span>
            </button>
          </h2>
          <div :id="'panelsStayOpen-collapseOne + index'" class="accordion-collapse collapse" :class="{'show': activeIndex === index }" :aria-labelledby="'panelsStayOpen-headingOne' + index">
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
import {ref} from "vue";

export default {
  name: "ReviewTitleList",

  props: {
    book: Object,
    reviewList: Array
  },

  setup() {
    const activeIndex = ref(null);

    function calculateStars(rating) {
      const numericRating = parseFloat(rating.replace("평점", "").replace("점", ""));
      const filledStars = Math.round((numericRating / 10) * 5);
      return "★".repeat(filledStars) + "☆".repeat(5 - filledStars);
    }

    function formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }

    function toggleAccordion(index) {
      activeIndex.value = activeIndex.value === index ? null : index;
    }

    return { activeIndex, calculateStars, formatDate, toggleAccordion };
  }
}
</script>

<style scoped>
.stars {
  margin-left: 10px; /* 별표와 아코디언 버튼 사이의 간격 조정 */
  color: orange; /* 별표 색상 */
}

</style>
