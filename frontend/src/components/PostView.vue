<template>
  <div>
    <b-table class="post-table" :items="items" :fields="fields"></b-table>
  </div>
</template>

<script>

export default {
  name: "PostView",
  props: {
    items: {
      generalForum: Array
    }
  },
};
</script>


<style scoped>
.post-table {
  max-width: 1000px;
  margin: 0 auto;
}

.post-table tbody tr {
  background-color: transparent !important;
}

</style>