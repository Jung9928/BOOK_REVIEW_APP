<template>
  <div class="post-registration">
    <!-- 제목 입력란 -->
    <input v-model="postTitle" type="text" placeholder="제목을 입력하세요" class="form-control mb-3">

    <!-- 내용 입력란 -->
<!--    <textarea v-model="postContent" rows="5" placeholder="내용을 입력하세요" class="form-control mb-3"></textarea>-->
    <ckeditor :editor="editor" v-model="postContent" :config="editorConfig"></ckeditor>

    <!-- 에디터 영역 -->
<!--    <editor-content :editor="editor" style="height: 90%" />-->

    <!-- 이미지 업로드 -->
    <input type="file" @change="handleFileUpload" class="form-control mb-3">

    <!-- 등록 버튼 -->
    <button @click="registerPost" class="btn btn-outline-dark">등록</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import CKEditor from "@ckeditor/ckeditor5-vue";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import axios from "axios";
import router from "@/scripts/router";


export default {
  components: {
    ckeditor: CKEditor.component
  },
  setup() {
    const postTitle = ref('');
    const postContent = ref('');
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

    const handleFileUpload = (event) => {
      uploadedImage.value = event.target.files[0];
    };

    const registerPost = () => {
      // 게시물 등록 예외처리

      // 게시물 등록 처리 로직
      axios.post('/api/v1/posts/postCreate', {
        title: postTitle.value,
        content: postContent.value
      })
          .then(response => {
            // 게시물 등록 완료
            console.log(response.data);
            window.alert("게시물 등록 완료");
            router.push({path:"/generalForum"})
          })
          .catch(error => {
            console.error(error.response.data);
            window.alert("게시물 등록에 실패했습니다. 다시 시도해주세요");
          });
    };

    return {
      postTitle,
      postContent,
      uploadedImage,
      handleFileUpload,
      registerPost,

      editor,
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