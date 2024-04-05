<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card">
          <div class="card-body">
            <h2 class="text-center mb-4">회원 정보</h2>
            <form @submit.prevent>
              <!-- 아이디 입력란 -->
              <div class="mb-3">
                <label for="username" class="form-label">아이디</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="username" :value="memberId" readonly>
                </div>
              </div>

              <!-- 이메일 입력란 -->
              <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <div class="input-group">
                  <input type="email" class="form-control" id="email" :value="email" readonly>
                </div>
              </div>

              <!-- 비밀번호 입력란 -->
              <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <div class="input-group">
                  <input type="password" class="form-control" id="password" placeholder="비밀번호를 입력하세요" v-model="password" @input="validatePassword">
                </div>
                <div v-if="passwordValidationMessage" :class="{ 'text-success': isPasswordValid, 'text-danger': !isPasswordValid }">{{ passwordValidationMessage }}</div>
              </div>

              <!-- 비밀번호 확인 입력란 -->
              <div class="mb-3">
                <label for="confirmPassword" class="form-label">비밀번호 확인</label>
                <div class="input-group">
                  <input type="password" class="form-control" id="confirmPassword" placeholder="비밀번호를 다시 입력하세요" v-model="confirmPassword" @input="validateConfirmPassword">
                </div>
                <div v-if="confirmPasswordValidationMessage" :class="{ 'text-success': isConfirmPasswordValid, 'text-danger': !isConfirmPasswordValid }">{{ confirmPasswordValidationMessage }}</div>
              </div>

              <!-- 나머지 입력 폼들 ... -->

              <div class="mb-3">
                <button type="submit" class="btn btn-outline-dark w-100" @click="memberInfoModify">정보 수정하기</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRoute} from "vue-router";
import axios from "axios";
import router from "@/scripts/router";
import {ref, onMounted } from "vue";

export default {

  setup() {

    const memberId = ref('');
    const email = ref('');
    const password = ref("");
    const confirmPassword = ref("");
    const isPasswordValid = ref(false);
    const isConfirmPasswordValid = ref(false);
    const passwordValidationMessage = ref("");
    const confirmPasswordValidationMessage = ref("");

    onMounted(() => {
      memberId.value = useRoute().query.memberId || '';
      email.value = useRoute().query.email || '';

      console.log("memberId2 : " + memberId.value);
      console.log("email2 : " + email.value);
    });

    const validatePassword = () => {
      // 비밀번호가 6자 이상 30자 이하인지 확인
      const passwordRegex = /^.{6,30}$/;
      isPasswordValid.value = passwordRegex.test(password.value);
      if (!isPasswordValid.value) {
        passwordValidationMessage.value = "비밀번호는 6자 이상이어야 합니다";
      } else {
        passwordValidationMessage.value = "사용 가능한 비밀번호 입니다";
      }
      // 비밀번호 확인란도 함께 검증
      validateConfirmPassword();
    };

    const validateConfirmPassword = () => {
      // 비밀번호와 비밀번호 확인이 일치하는지 확인
      isConfirmPasswordValid.value = password.value === confirmPassword.value;
      if (!isConfirmPasswordValid.value) {
        confirmPasswordValidationMessage.value = "비밀번호가 일치하지 않습니다";
      } else {
        confirmPasswordValidationMessage.value = "비밀번호가 일치합니다 ";
      }
    };

    // 회원가입 버튼을 눌렀을 시, 실행되는 메소드
    const memberInfoModify = () => {
      // 모든 조건 검증
      if (!isPasswordValid.value || !isConfirmPasswordValid.value) {
        window.alert("비밀번호를 확인해주세요");
        return;
      }

      axios.put(`/api/v1/members/modify-info`, {
        memberId: memberId.value,
        email: email.value,
        password: password.value
      })
          .then(() => {
            window.alert("수정 완료");
            router.push({path:"/"});
          })
          .catch(() => {
            window.alert("회원 정보 수정 중에 오류가 발생했습니다");
          })
    };

    return {
      memberId,
      email,
      memberInfoModify,
      password,
      confirmPassword,

      validatePassword,
      validateConfirmPassword,
      isPasswordValid,
      isConfirmPasswordValid,
      passwordValidationMessage,
      confirmPasswordValidationMessage
    };
  }
}
</script>

<style scoped>
.password-match-feedback {
  font-size: 12px;
  margin-top: 5px;
}

.text-success {
  color: green;
}

.text-danger {
  color: red;
}
</style>