<template>
  <div class="card shadow-sm">
    <span class="img" :style="{backgroundImage: `url(${book.imgSrc})`}"/>
    <div class="card-body">
      <p class="card-text">{{book.book_title}}</p>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-primary">알라딘</button>
        <button class="btn btn-primary">yes24</button>
        <button class="btn btn-primary">교보문고</button>
        <small class="price text-muted">
          {{lib.getNumberFormatted(book.regular_price)}}원
        </small>

        <small class="discount text-danger">
          {{lib.getNumberFormatted(book.selling_price)}}원
        </small>
      </div>
    </div>
  </div>
</template>

<script>
import lib from "@/scripts/lib";

export default {
  name: "Card",
  props: {
    book: Object
  },

  setup() {
    return {lib}
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

</style>