<template>
  <div class="post-registration">
    <!-- 제목 입력란 -->
    <input v-model="postTitle" type="text" placeholder="제목을 입력하세요" class="form-control mb-3">

    <!-- 내용 입력란 -->
    <!--    <textarea v-model="postContent" rows="5" placeholder="내용을 입력하세요" class="form-control mb-3"></textarea>-->
    <ckeditor :editor="editor" v-model="postContent" :config="editorConfig"></ckeditor>

    <!-- 이미지 업로드 -->
<!--    <input type="file" @change="handleFileUpload" class="form-control mb-3">-->

    <!-- 수정 버튼 -->
    <button @click="modifyPost" class="btn btn-outline-dark">수정</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import CKEditor from "@ckeditor/ckeditor5-vue";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import {useRoute} from "vue-router";
import axios from "axios";
import router from "@/scripts/router";


export default {
  components: {
    ckeditor: CKEditor.component
  },
  setup() {
    const postId = ref(useRoute().query.postId);
    const postTitle = ref(useRoute().query.postTitle);
    const postContent = ref(useRoute().query.postContent);
    const uploadedImage = ref(null);

    const editor = ClassicEditor;
    const editorConfig = {
      toolbar: [
        'heading',
        '|', 'bold', 'italic', 'link',
        '|', 'bulletedList', 'numberedList', 'blockQuote', 'uploadImage',
        '|', 'undo', 'redo'
      ],
    };

    // const handleFileUpload = (event) => {
    //   uploadedImage.value = event.target.files[0];
    // };

    const modifyPost = () => {
      // 게시물 수정 처리 로직
      axios.put(`/api/v1/posts/generalForum/${postId.value}`, {
        title : postTitle.value,
        content: postContent.value
      })
          .then(() => {
            // 가져온 게시글 삭제
            router.push({path:"/generalForum"});
          })
          .catch((err) => {
            console.error("err message : " + err);
            window.alert("해당 글의 작성자가 아니거나 수정할 글이 존재하지 않습니다.");
          });
    };

    return {
      postTitle,
      postContent,
      uploadedImage,
      // handleFileUpload,

      editor,
      modifyPost,
      editorConfig
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

.ck-editor__editable {
  min-height: 400px;
  max-height: 400px;
}
</style>