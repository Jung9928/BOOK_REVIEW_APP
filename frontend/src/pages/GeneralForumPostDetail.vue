<template>
  <div class="post-registration">
    <!-- 제목 입력란 -->
    <input v-model="postTitle" type="text" placeholder="제목을 입력하세요" readonly class="form-control mb-3">

    <!-- 내용 입력란 -->
    <div class="textarea-container">
      <textarea v-model="postContent" rows="5" placeholder="내용을 입력하세요" readonly class="form-control mb-3"></textarea>

      <!-- 수정 및 삭제 버튼 -->
      <div class="button-container">
        <button v-if="isShowable" @click="goToModifyPage" class="btn btn-outline-dark btn-sm">수정</button>
        <button v-if="isShowable" @click="deletePost" class="btn btn-outline-dark btn-sm">삭제</button>
      </div>
    </div>

    <div class="recommend-container">
      <button @click="goBackToForum" class="btn btn-outline-dark recommend-button">목록</button>
      <button @click="[recommendPost(), getRecommendCounting()]" class="btn btn-outline-dark recommend-button">
        <i class="bi bi-hand-thumbs-up-fill">추천 {{recommendCount}}</i>
      </button>
    </div>

    <CommentList/>

<!--    <ckeditor :editor="editor" v-model="postContent" :config="editorConfig"></ckeditor>-->
  </div>
</template>

<script>
import {computed, onMounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import axios from "axios";
import router from "@/scripts/router";
import {useStore} from "vuex";
import CommentList from "@/components/CommentList";

export default {
  components: {CommentList},
  setup() {

    const store = useStore();

    const postId = ref(useRoute().query.postId);
    const postTitle = ref(useRoute().query.title.replace(/<[^>]*>/g, ""));
    console.log("postTitle : " + postTitle.value);

    const postContent = ref(useRoute().query.content.replace(/<[^>]*>/g, ""));
    console.log("postContent : " + postContent.value);

    const postMemberId = ref(useRoute().query.memberId.replace(/^"|"$/g, ''));
    console.log("postMemberId : " + postMemberId.value);

    const storageMemberId = ref(store.state.memberId);
    console.log("storageMemberId : " + storageMemberId.value);

    const isShowable = computed(() => postMemberId.value === storageMemberId.value);
    console.log("isShowable : " + isShowable.value);

    let recommendCount = ref(0);

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
    };

    // 게시글 추천 버튼
    const recommendPost = () => {
      axios.post(`/api/v1/posts/generalForum/${postId.value}/recommend`)
          .then((res) => {
            console.log("추천 완료");
            console.log("res : " + res);
          })
          .catch((error) => {

            if(error.response.status === 401) {
              console.error("error message : " + error);
              window.alert("해당 게시글에 추천할 수 없습니다");
              // router.push({path: "/"});
            }
            else if(error.response.status == 500) {
              console.error("error message : " + error);
              window.alert("서버 오류 발생");
              // router.push({path: "/"});
            }
          })
    };

    // 게시글 추천 수 가져오기
    const getRecommendCounting = () => {
      axios.get(`/api/v1/posts/generalForum/${postId.value}/recommend`)
          .then((res) => {
            console.log("추천 수 가져오기 완료");
            console.log("res : " + res);
            console.log("추천 수 : " + res.data.result);
            recommendCount.value = res.data.result;
          })
          .catch((error) => {
            console.error("error message : " + error);
            window.alert("오류 발생");
          })
    };

    // 목록 버튼 클릭 시, 게시글 목록 페이지로 이동
    const goBackToForum = () => {
      router.push({path: "/generalForum"});
    }

    onMounted(() => getRecommendCounting());

    return {
      postTitle,
      postContent,
      postMemberId,
      storageMemberId,

      deletePost,
      goToModifyPage,
      isShowable,

      recommendPost,
      getRecommendCounting,
      recommendCount,

      goBackToForum
    };
  },
}
</script>

<style>
.post-registration {
  max-width: 600px;
  margin: 0 auto;
  padding-top: 3%;
  position: relative;
}

/* 수정 및 삭제 버튼의 간격 조절 */
.post-registration button {
  margin-right: 15px; /* 수정 버튼과 삭제 버튼 사이의 간격 조절 */
}

.textarea-container {
  position: relative;
}

.button-container {
  position: absolute;
  bottom: 10px;
  right: 10px;
}

.button-container button {
  margin-right: 5px;
  font-size: 12px; /* 버튼 텍스트 크기 */
  padding: 0.25rem 0.5rem; /* 버튼 패딩 */
}

.recommend-container {
  position: relative;
  left: 93%;
  transform: translateX(-50%);
}

/* 추천 버튼 */
.recommend-button {
  position: relative;

}

</style>