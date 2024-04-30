<template>
  <div class="post-registration">
    <!-- 제목 입력란 -->
<!--    <input v-model="postTitle" type="text" placeholder="제목을 입력하세요" readonly class="form-control mb-3">-->

    <div class="post-title">
        <span class="title">{{postTitle}}</span>
        <span class="regAt">{{postRegisteredAt}}</span>
    </div>
    <div class="post-memberId">
      <span class="memberId">작성자 : {{postMemberId}}</span>
      <span class="view-count">조회 수<span>{{viewCount}}</span></span>
      <span class="recommend-count">추천 수<span>{{recommendCount}}</span></span>
      <span class="comment_count">댓글<span>{{commentList.length}}</span></span>
    </div>

    <!-- 내용 입력란 -->
    <div class="textarea-container">
      <textarea v-model="postContent" rows="5" placeholder="내용을 입력하세요" readonly class="form-control mb-3"></textarea>

      <!-- 수정 및 삭제 버튼 -->
      <div class="button-container">
        <button v-if="isShowable" @click="goToModifyPage" class="btn btn-outline-dark btn-xs">수정</button>
        <button v-if="isShowable" @click="deletePost" class="btn btn-outline-dark btn-xs">삭제</button>
      </div>
    </div>

    <div class="recommend-container">
      <button @click="goBackToForum" class="btn btn-outline-dark recommend-button">목록</button>
      <button @click="[recommendPost(), getRecommendCounting()]" class="btn btn-outline-dark recommend-button">
        <i class="bi bi-hand-thumbs-up-fill">추천 {{recommendCount}}</i>
      </button>
    </div>


    <div class="post-comments">
      <div class="comment-input-container">
        <!-- 댓글 입력란 -->
        <div class="comment-input">
          <i class="bi bi-check2-square"></i>댓글 쓰기
          <div class="form-floating">
            <textarea v-model="comment" class="form-control" id="floatingTextarea2" style="height: 100px"></textarea>
          </div>

          <p>
            도배 및 무성의 댓글을 자제해주시길 바라며 소중한 댓글 부탁드립니다.
          </p>
        </div>

        <div class="button-container">
          <button @click="addComment" class="btn btn-outline-dark btn-sm">등록</button>
        </div>
      </div>
    </div>

    <div class="reply-container">
<!--        <p>Comments <strong>'{{totalCommentCount}}'</strong></p>-->
      <p>Comments <strong>'{{commentList.length}}'</strong></p>
    </div>

    <div v-for="comment in commentList" :key="comment.id" class="comment">
      <div class="comment-author">
        {{ comment.author }}
        <span class="comment-reg-at">{{ getRelativeTime(comment.registeredAt) }}</span>
        <button class="comment-modify-btn" @click="toggleModifyComment(comment.id, comment.content)"><i class="bi bi-pencil-fill"></i>수정</button>
        <button class="comment-delete-btn" @click="deleteComment(comment.id)"><i class="bi bi-eraser"></i>삭제</button>

        <textarea v-if="comment.modifyReplying" v-model="modifyReply" class="form-control mb-3" placeholder="댓글을 입력하세요"></textarea>
        <button v-if="comment.modifyReplying" @click="modifyComment(comment)" class="btn btn-outline-dark btn-sm">댓글 수정</button>
      </div>

      <div class="comment-content">
        {{ comment.content }}
        <button class="reply-btn" @click="toggleReply(comment.id)">↳답글</button>
      </div>

      <textarea v-if="comment.replying" v-model="reply" class="form-control mb-3" placeholder="답글을 입력하세요"></textarea>
      <button v-if="comment.replying" @click="postReply(comment)" class="btn btn-outline-dark btn-sm">답글 등록</button>

      <!-- childComments (대댓글) 렌더링 -->
      <div v-if="comment.childComments && comment.childComments.length > 0" class="child-comments">
        <hr/>
        <div v-for="childComment in comment.childComments" :key="childComment.id" class="child-comment">
          <div class="comment-author">
            <i class="bi bi-arrow-return-right"></i> {{ childComment.memberId }}
            <span class="comment-reg-at">{{ getRelativeTime(childComment.registeredAt) }}</span>
            <button class="childComment-modify-btn" @click="toggleModifyChildComment(childComment.id, childComment.content)"><i class="bi bi-pencil-fill"></i>수정</button>
            <button class="childComment-delete-btn" @click="deleteComment(childComment.id)"><i class="bi bi-eraser"></i>삭제</button>

            <textarea v-if="childComment.modifyChildReplying" v-model="modifyChildReply" class="form-control mb-3" placeholder="댓글을 입력하세요"></textarea>
            <button v-if="childComment.modifyChildReplying" @click="modifyChildComment(childComment)" class="btn btn-outline-dark btn-sm">답글 수정</button>
          </div>
          <div class="childComment-content">
            {{ childComment.comment }}
            <button class="reply-btn" @click="toggleChildReply(childComment.id)">↳답글</button>
          </div>

          <textarea v-if="childComment.childReplying" v-model="reply" class="form-control mb-3" placeholder="답글을 입력하세요"></textarea>
          <button v-if="childComment.childReplying" @click="postReply(childComment)" class="btn btn-outline-dark btn-sm">답글 등록</button>
        </div>
      </div>

      <hr/>
    </div>
  </div>
<!--    <ckeditor :editor="editor" v-model="postContent" :config="editorConfig"></ckeditor>-->

</template>

<script>
import {computed, onMounted, ref} from 'vue';
// import { useRoute } from 'vue-router';
import axios from "axios";
import router from "@/scripts/router";
import store from "@/scripts/store";
import cookies from "vue-cookie";

export default {

  setup() {

    // const postId = ref(useRoute().query.postId);
    // const postTitle = ref(useRoute().query.title.replace(/<[^>]*>/g, ""));
    //
    // const postContent = ref(useRoute().query.content.replace(/<[^>]*>/g, ""));
    //
    // const postMemberId = ref(useRoute().query.memberId.replace(/^"|"$/g, ''));

    // store.js에 저장한 게시글 상세 정보를 불러옴.
    const postData = computed(() => store.getters.getPostData);
    const postId = ref(postData.value.postId);
    const postTitle = ref(postData.value.title.replace(/<[^>]*>/g, ""));
    const postContent = ref(postData.value.content.replace(/<[^>]*>/g, ""));
    const postMemberId = ref(postData.value.memberId);
    const postRegisteredAt = ref(postData.value.registeredAt);
    const viewCount = ref(postData.value.viewCount);

    const storageMemberId = ref(store.state.memberId);

    const isShowable = computed(() => postMemberId.value === storageMemberId.value);
    console.log("isShowable : " + isShowable.value);

    let recommendCount = ref(0);
    const commentList = ref([]);
    const childComments = ref([]);

    // 댓글 작성
    const comment = ref('');
    const registeredAt = ref('');
    const totalCommentCount = ref(0);

    // 댓글 관련
    const replyId = ref(0);
    const replying = ref(false);
    const reply = ref('');

    // 댓글 수정 입력 관련
    const modifyReplying = ref(false);
    const modifyReply = ref('');

    // 대댓글 수정 입력 관련
    const modifyChildReplying = ref(false);
    const modifyChildReply = ref('');

    // 대댓글 작성 입력 관련
    const childReplying = ref(false);



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

    // 게시글 클릭 시, 해당 게시글의 (대)댓글 가져오기
    // const getCommentList = () => {
    //   console.log("postId2 : " + postId.value);
    //
    //   axios.get(`/api/v1/comment/list`, {
    //     params: {
    //       generalForumEntity: postId.value
    //     }
    //   })
    //       .then((res) => {
    //         // 코멘트 리스트의 각 항목에서 필요한 속성들만 가져와서 commentList에 할당
    //         commentList.value = res.data.content.map(comment => ({
    //           id: comment.id,
    //           author: comment.memberId,
    //           content: comment.comment,
    //           registeredAt: comment.registeredAt
    //         }))
    //
    //         // 작성된 댓글 갯수
    //         totalCommentCount.value = res.data.totalElements;
    //       })
    //       .catch((err) => {
    //         console.error('댓글 리스트를 가져오는 중 오류 발생:', err);
    //       });
    // };

    const getCommentList = () => {
      console.log("postId2 : " + postId.value);

      axios.get(`/api/v1/comment/reply/${postId.value}`)
          .then((res) => {
            // 코멘트 리스트의 각 항목에서 필요한 속성들만 가져와서 commentList에 할당
            commentList.value = res.data.map(comment => ({
              id: comment.id,
              author: comment.memberId,
              content: comment.comment,
              registeredAt: comment.registeredAt,
              childComments: comment.childComments
            }))

            // 작성된 댓글 갯수
            totalCommentCount.value = res.data.totalElements;
          })
          .catch((err) => {
            console.error('댓글 리스트를 가져오는 중 오류 발생:', err);
          });
    };


    // 댓글 달기 작성
    const addComment = () => {
      axios.post(`/api/v1/comment/create`, {
        postId: postId.value,
        content: comment.value,
        memberId: storageMemberId.value,
      })
          .then(() => {
            window.alert("댓글 작성 완료");
          })
          .catch(() => {
            window.alert("작성 중 오류가 발생했습니다");
          })

      // 댓글 작성 후, 댓글 리스트 최신화
      getCommentList();
    };


    // 댓글 수정 입력 폼 토글 함수
    const toggleModifyComment = (commentId, content) => {
      const targetComment = commentList.value.find(comment => comment.id === commentId);
      modifyReply.value = content;
      targetComment.modifyReplying = !targetComment.modifyReplying;
    }

    // 대댓글 수정 입력 폼 토글 함수
    const toggleModifyChildComment = (childCommentId, content) => {
      const targetChildComment = childComments.value.find(childComment => childComment.id === childCommentId);
      modifyChildReply.value = content;
      targetChildComment.modifyChildReplying = !targetChildComment.modifyChildReplying;
    }

    // 답글 토글 함수
    const toggleReply = (commentId) => {
      const targetComment = commentList.value.find(comment => comment.id === commentId);
      targetComment.replying = !targetComment.replying;
    };

    // 대댓글 토글 함수
    const toggleChildReply = (childCommentId) => {
      // commentList 배열을 순회하면서 대댓글을 찾음
      commentList.value.forEach(comment => {

        // 각 댓글의 childComments 배열에서 대댓글을 찾음
        const targetChildComment = comment.childComments.find(childComment => childComment.id === childCommentId);

        // 대댓글을 찾았을 경우에만 childReplying 속성을 토글
        if (targetChildComment) {
          targetChildComment.childReplying = !targetChildComment.childReplying;
        }
      });
    };

    // 답글 버튼 클릭 시, 답글 작성
    const postReply = (comment) => {
      // comment.replyContent로 답글 내용에 접근하여 서버로 전송 등의 작업 수행
      axios.post(`/api/v1/comment/create`, {
        parentId: comment.id,
        postId: postId.value,
        content: reply.value,
        memberId: storageMemberId.value
      })
          .then(() => {
            window.alert("답글 작성 완료");
          })
          .catch(() => {
            window.alert("답글 작성 중 오류가 발생했습니다");
          })

      // 댓글 작성 후, 댓글 리스트 최신화
      getCommentList();

      // 작업 완료 후 답글 입력창 숨기기
      comment.replying = false;
    };


    // 댓글 수정
    const modifyComment = (comment) => {
      axios.put(`/api/v1/comment/modify`, {
        id: comment.id,
        postId: postId.value,
        content: comment.value,
        memberId: storageMemberId.value,
      })
          .then(() => {
            window.alert("댓글 수정 완료");
          })
          .catch(() => {
            window.alert("댓글 수정 중 오류가 발생했습니다");
          })

      // 댓글 작성 후, 댓글 리스트 최신화
      getCommentList();

      // 작업 완료 후 답글 입력창 숨기기
      comment.modifyReplying = false;
    }

    // 대댓글 수정
    const modifyChildComment = (childComment) => {
      axios.put(`/api/v1/comment/modify`, {
        id: childComment.id,
        postId: postId.value,
        content: comment.value,
        memberId: storageMemberId.value,
      })
          .then(() => {
            window.alert("댓글 수정 완료");
          })
          .catch(() => {
            window.alert("댓글 수정 중 오류가 발생했습니다");
          })

      // 댓글 작성 후, 댓글 리스트 최신화
      getCommentList();

      // 작업 완료 후 답글 입력창 숨기기
      childComment.modifyChildReplying = false;
    }


    // 댓글 삭제
    const deleteComment = (commentId) => {
      axios.delete(`/api/v1/comment/delete/${commentId}`, {
        headers: {
          Authorization: "Bearer " + store.state.accessToken
        },
      })
          .then(() => {
            window.alert("댓글 삭제 완료");
          })
          .catch(() => {
            window.alert("댓글 삭제 중 오류가 발생했습니다");
          })

      // 댓글 작성 후, 댓글 리스트 최신화
      getCommentList();
    }


    // 목록 버튼 클릭 시, 게시글 목록 페이지로 이동
    const goBackToForum = () => {
      router.push({path: "/generalForum"});
    };

    // 조회수 증가(쿠키로 중복방지)
    const getCookie = () => {
      axios.get(`/api/v1/posts/generalForum/viewCount/${postId.value}`, {
        headers: {
          Authorization: "Bearer " + store.state.accessToken
        },
      })

      const cookie = cookies.get('viewCount');
      console.log(cookie);
    }

    const getRelativeTime = (targetTime) => {
      const currentTime = new Date();
      const targetDate = new Date(targetTime);
      const diffInMillis = currentTime - targetDate;

      // 밀리초를 시간으로 변환
      const diffInHours = diffInMillis / (1000 * 60 * 60);

      if (diffInHours < 24) {
        // 24시간 이내의 경우 상대적인 표현 사용
        const diffInMinutes = Math.round(diffInHours * 60);
        if (diffInMinutes < 60) {
          return `${diffInMinutes}분 전`;
        } else {
          const diffInHoursRounded = Math.round(diffInHours);
          return `${diffInHoursRounded}시간 전`;
        }
      } else {
        // 24시간을 넘어갈 경우 절대 시간 표현 사용
        const year = targetDate.getFullYear();
        const month = String(targetDate.getMonth() + 1).padStart(2, '0');
        const day = String(targetDate.getDate()).padStart(2, '0');
        const hours = String(targetDate.getHours()).padStart(2, '0');
        const minutes = String(targetDate.getMinutes()).padStart(2, '0');
        return `${year}.${month}.${day} ${hours}:${minutes}`;
      }
    };

    onMounted(() => {
      getRecommendCounting();
      getCommentList();
      getCookie();
    });

    return {
      postTitle,
      postContent,
      postMemberId,
      postRegisteredAt,
      viewCount,
      storageMemberId,
      comment,
      replyId,
      replying,
      childReplying,
      reply,
      modifyReply,
      modifyChildReply,
      registeredAt,

      deletePost,
      goToModifyPage,
      isShowable,

      recommendPost,
      getRecommendCounting,
      getCommentList,
      commentList,
      childComments,
      recommendCount,
      totalCommentCount,
      postReply,
      toggleReply,
      toggleChildReply,
      modifyReplying,
      modifyChildReplying,

      addComment,
      toggleModifyComment,
      toggleModifyChildComment,
      modifyComment,
      modifyChildComment,
      deleteComment,

      goBackToForum,

      getRelativeTime,
      getCookie
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

/* postTitle css */
.post-title {
  border-style: solid;
  border-color: #d7d4d4 #ffffff #d7d4d4 #ffffff;
  border-top-width: 1px;
  border-bottom-width: 1px;
  border-left: none;
  border-right: none;
  background-color: #fafafa;
  font-weight: bold;
  font-size: 20px;
}

.title {
  margin-left: 10px;
}

.regAt {
  margin-left: 300px;
  font-size: 13px !important;
  font-weight: lighter;
}

/* post memberId css */
.post-memberId {
  border-style: solid;
  border-top: none;
  border-left: none;
  border-right: none;
  border-bottom-width: 1px;
  border-color: #d7d4d4 #ffffff #d7d4d4 #ffffff;
}

/* 조회 수, 추천 수, 댓글 텍스트 css */
.view-count {
  margin-left: 230px;
}

.view-count span {
  margin-left: 5px;
  font-weight: bold;
}

.recommend-count {
  margin-left: 15px;
}

.recommend-count span {
  margin-left: 5px;
  font-weight: bold;
}

.comment_count {
  margin-left: 15px;
}

.comment_count span {
  margin-left: 5px;
  font-weight: bold;
}

/*==================================*/


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

/* 댓글 관련 css */
.comment-input-container {
  position: relative;
  background-color: #f5f5f5; /* 회색 배경 적용 */
  padding: 10px; /* 간격 조정 */
  border-radius: 5px; /* 모서리 둥글게 */
  margin-bottom: 20px; /* 아래 여백 추가 */
}

.comment {
  margin-bottom: 10px;
  padding-left: 20px;
}

.comment-author {
  font-weight: bold;
}

.comment-content {
  margin-top: 5px;
}

.comment-reg-at {
  font-weight: lighter;
  color: gray;
  font-size: 11px;
}

/* ====================
  댓글 수정, 삭제 버튼 css
======================*/
.comment-modify-btn, .comment-delete-btn {
  background-color: rgba(0, 0, 0, 0);   /* 버튼 배경 투명색 */
  border: none;                         /* 버튼 테두리 제거 */
  font-size: 10px;                      /* 버튼 텍스트 크기 */
}

.comment-modify-btn {
  margin-left: 280px;
}
/*===========================*/

/* 대댓글 목록 css */
.child-comments {
  width: 96%;
  margin-left: auto;
}

/* 대댓글 내용 css */
.childComment-content {
  margin-top: 5px;
  margin-left: 4%;
}

/* 대댓글 수정 버튼 css */
.childComment-modify-btn, .childComment-delete-btn {
  background-color: rgba(0, 0, 0, 0);   /* 버튼 배경 투명색 */
  border: none;                         /* 버튼 테두리 제거 */
  font-size: 10px;                      /* 버튼 텍스트 크기 */
}

.childComment-modify-btn {
  margin-left: 250px;
}

.button-container {
  font-size: 12px; /* 버튼 텍스트 크기 */
  padding: 0.25rem 0.5rem; /* 버튼 패딩 */
}


/* 답글 버튼 css */
.reply-btn {
  background-color: rgba(0, 0, 0, 0);   /* 버튼 배경 투명색 */
  border: none;                         /* 버튼 테두리 제거 */
  font-size: 12px;                      /* 버튼 텍스트 크기 */
  color: crimson;
}

.reply-container {
  border-style: solid;
  border-left: none;
  border-right: none;
  border-top-width: 1px;
  border-bottom-width: 1px;
  border-top-color: #d7d4d4;
  border-bottom-color: #d7d4d4;
  background-color: #f7f7f7;
  height: 50px;
  line-height: 50px;
}

.reply-container p {
  padding-left: 20px;
}

</style>