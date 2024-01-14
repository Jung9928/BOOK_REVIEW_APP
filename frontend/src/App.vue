<template>
  <Header/>
  <RouterView/>
  <Footer/>
</template>

<script>

import Header from "@/components/Header";
import Footer from "@/components/Footer";
import store from "@/scripts/store";
import axios from "axios";
import {watch} from "vue";
import {useRoute} from "vue-router/dist/vue-router";

export default {
  name: 'App',
  components: {
    Footer,
    Header
  },

  setup() {
    const check = ()=> {
      axios.get("/api/account/check").then(({data}) => {
        console.log(data);
        store.commit("setAccount", data || 0);
      })
    };

    const route = useRoute();

    // 경로가 변경되는지 감시
    watch(route, () => {
      check();
    })
  }
}
</script>

<style>
  .bd-placeholder-img {
    font-size: 1.125rem;
    text-anchor: middle;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;
  }

  @media (min-width: 768px) {
    .bd-placeholder-img-lg {
      font-size: 3.5rem;
    }
  }
</style>
