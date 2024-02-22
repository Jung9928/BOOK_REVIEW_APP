<template>
  <div class="home">
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <div class="col" v-for="(book, idx) in state.books" :key="idx">
            <Card :book="book"/>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import Card from "@/components/Card";

export default {
  name: "Home",
  components: {Card},
  setup() {
    const state = reactive({
      books: []
    })

    axios.get("/api/v1/books").then(({data}) => {
      state.books = data;
    })

    return {state}
  }
}
</script>

<style scoped>

</style>