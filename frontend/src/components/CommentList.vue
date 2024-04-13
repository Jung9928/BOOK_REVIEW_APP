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
    <div v-for="comment in comments" :key="comment.id" class="comment">
      <div class="comment-author">{{ comment.author }}</div>
      <div class="comment-content">{{ comment.content }}</div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue';

export default {
  setup() {
    const comments = reactive([
      { id: 1, author: '사용자1', content: '첫 번째 댓글입니다!' },
      { id: 2, author: '사용자2', content: '두 번째 댓글이에요~' },
      // 댓글 데이터 추가...
    ]);

    return { comments };
  }
};
</script>

<style>
.post-comments {
  /* 스타일 설정 */
}

.comment-input-container {
  position: relative;;
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
