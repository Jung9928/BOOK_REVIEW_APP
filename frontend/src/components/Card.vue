<template>
  <div class="card shadow-sm">
    <span class="img" :style="{backgroundImage: `url(${book.imgPath})`}"/>
    <div class="card-body">
      <p class="card-text"><span class="bold">{{book.title}}</span></p>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-primary">알라딘</button>
        <button class="btn btn-primary">yes24</button>
        <button class="btn btn-primary">교보문고</button>
        <small class="price text-muted">
          {{lib.getNumberFormatted(book.regular_price)}}
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

  .bold {
    font-weight: bold;
  }

</style>