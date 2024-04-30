<template>
  <div class="post-comments">
    <div class="comment-input-container">
      <!-- 댓글 입력란 -->
      <div class="comment-input">
        <div class="form-floating">
          <textarea class="form-control" id="floatingTextarea2" style="height: 100px"></textarea>
        </div>
      </div>

      <div class="button-container">
        <button @click="addComment" class="btn btn-outline-dark btn-sm">댓글 달기</button>
      </div>

    </div>

    <div v-if="commentList.length == 0" class="no-comments">
      댓글이 없습니다.
    </div>

    <div v-else>
      <div v-for="comment in commentList" :key="comment.id" class="comment">
        <div class="comment-author">{{ comment.author }}</div>
        <div class="comment-content">{{ comment.content }}</div>
      </div>
    </div>

  </div>
</template>

<script>
import { ref, watchEffect } from 'vue';

export default {
  name: "CommentList",
  props: {
    comments: Array
  },

  setup(props) {
    const commentList = ref([]);

    watchEffect(() => {
      commentList.value = props.comments.map(comment => ({
        id: comment.id,
        author: comment.memberId,
        content: comment.comment
      }));
    });

    console.log("commentList : " + commentList.value);

    return { commentList };
  }
};
</script>

<style>
.post-comments {
  /* 스타일 설정 */
}

.comment-input-container {
  position: relative;
  background-color: #f5f5f5; /* 회색 배경 적용 */
  padding: 10px; /* 간격 조정 */
  border-radius: 5px; /* 모서리 둥글게 */
  margin-bottom: 20px; /* 아래 여백 추가 */
}

.comment {
  margin-bottom: 10px;
}
.comment-author {
  font-weight: bold;
}
.comment-content {
  margin-top: 5px;
}

.button-container {
  font-size: 12px; /* 버튼 텍스트 크기 */
  padding: 0.25rem 0.5rem; /* 버튼 패딩 */
}
</style>
