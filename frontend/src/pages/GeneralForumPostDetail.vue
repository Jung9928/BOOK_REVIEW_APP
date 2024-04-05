<template>
  <div class="post-registration">
    <!-- 제목 입력란 -->
    <input v-model="postTitle" type="text" placeholder="제목을 입력하세요" readonly class="form-control mb-3">

    <!-- 내용 입력란 -->
    <textarea v-model="postContent" rows="5" placeholder="내용을 입력하세요" readonly class="form-control mb-3"> </textarea>
<!--    <ckeditor :editor="editor" v-model="postContent" :config="editorConfig"></ckeditor>-->

    <!-- 수정 버튼 -->
    <button @click="goToModifyPage" class="btn btn-outline-dark">수정</button>

    <!-- 삭제 버튼 -->
    <button @click="deletePost" class="btn btn-outline-dark">삭제</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import axios from "axios";
import router from "@/scripts/router";

export default {

  setup() {

    const editor = ClassicEditor;
    const editorConfig = {
      toolbar: [
        'heading',
        '|', 'bold', 'italic', 'link',
        '|', 'bulletedList', 'numberedList', 'blockQuote', 'uploadImage',
        '|', 'undo', 'redo'
      ],
    };
    const postId = ref(useRoute().query.postId);
    const postTitle = ref(useRoute().query.title.replace(/<[^>]*>/g, ""));
    console.log("postTitle : " + postTitle.value);

    const postContent = ref(useRoute().query.content.replace(/<[^>]*>/g, ""));
    console.log("postContent : " + postContent.value);

    // 게시글 수정 버튼
    const goToModifyPage = () => {
      // 가져온 게시글 수정 페이지로 이동
      router.push({path:"/generalForumModify", query: {postId: postId.value, postTitle: postTitle.value, postContent: postContent.value}});
    };

    // 게시글 삭제 버튼
    const deletePost = () => {
      axios.delete(`/api/v1/posts/generalForum/${postId.value}`)
          .then(() => {
            // 가져온 게시글 삭제
            router.push({path:"/generalForum"});
          })
          .catch((err) => {
            console.error("err message : " + err);
            window.alert("해당 글의 작성자가 아니거나 삭제할 글이 존재하지 않습니다.");
          });
    }

    return {
      postTitle,
      postContent,

      editor,
      editorConfig,

      deletePost,
      goToModifyPage
    };
  }
}
</script>

<style>
.post-registration {
  max-width: 600px;
  margin: 0 auto;
  padding-top: 3%;
}

/* 수정 및 삭제 버튼의 간격 조절 */
.post-registration button {
  margin-right: 15px; /* 수정 버튼과 삭제 버튼 사이의 간격 조절 */
}

</style>