<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card">
          <div class="card-body">
            <h2 class="text-center mb-4">회원 가입</h2>
            <form>
              <!-- 아이디 입력란 -->
              <div class="mb-3">
                <label for="username" class="form-label">아이디</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="username" placeholder="아이디를 입력하세요" v-model="id" @input="validateUsername">
                  <button class="btn btn-outline-secondary" type="button" @click="checkDuplicateUsername">중복 확인</button>
                </div>
                <div v-if="usernameValidationMessage" :class="{ 'text-success': isUsernameValid, 'text-danger': !isUsernameValid }">{{ usernameValidationMessage }}</div>
              </div>

              <!-- 이메일 입력란 -->
              <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <div class="input-group">
                  <input type="email" class="form-control" id="email" placeholder="이메일을 입력하세요" v-model="email" @input="validateEmail">
                  <button type="button" class="btn btn-outline-secondary" @click="sendEmailVerification">이메일 인증</button>
                </div>
                <div v-if="emailValidationMessage" :class="{ 'text-success': isEmailValid, 'text-danger': !isEmailValid }">{{ emailValidationMessage }}</div>
              </div>

              <!-- 비밀번호 입력란 -->
              <div class="mb-3">
                <label for="password" class="form-label">비밀번호</label>
                <div class="input-group">
                  <input type="password" class="form-control" id="password" placeholder="비밀번호를 입력하세요" v-model="password" @input="validatePassword">
                  <div v-if="passwordValidationMessage" class="invalid-feedback">{{ passwordValidationMessage }}</div>
                </div>
              </div>

              <!-- 비밀번호 확인 입력란 -->
              <div class="mb-3">
                <label for="confirmPassword" class="form-label">비밀번호 확인</label>
                <div class="input-group">
                  <input type="password" class="form-control" id="confirmPassword" placeholder="비밀번호를 다시 입력하세요" v-model="confirmPassword" @input="validateConfirmPassword">
                  <div v-if="confirmPasswordValidationMessage" class="password-match-feedback">{{ confirmPasswordValidationMessage }}</div>
                </div>
              </div>

              <!-- 나머지 입력 폼들 ... -->

              <div class="mb-3">
                <button type="submit" class="btn btn-primary w-100">회원 가입</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      id: "",
      email: "",
      password: "",
      confirmPassword: "",
      usernameValidationMessage: "",
      emailValidationMessage: "",
      passwordValidationMessage: "",
      confirmPasswordValidationMessage: ""
    };
  },
  methods: {
    validateUsername() {
      // 아이디가 6자 이상, 20자 이하이며 영문자 또는 숫자를 포함하는지 여부 확인
      const regex = /^[a-zA-Z][a-zA-Z0-9]{5,14}$/;
      this.isUsernameValid = regex.test(this.id);
      if (!this.isUsernameValid) {
        this.usernameValidationMessage = "ID는 6자 이상 20자 이하이며 영문자 또는 영문 + 숫자로 구성되어야 합니다";
      } else {
        this.usernameValidationMessage = "사용 가능한 아이디입니다";
      }
    },
    validateEmail() {
      // 이메일 형식 확인
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.isEmailValid = emailRegex.test(this.email);
      if (!this.isEmailValid) {
        this.emailValidationMessage = "올바른 이메일 형식이 아닙니다";
      } else {
        this.emailValidationMessage = "사용 가능한 이메일 형식입니다";
      }
    },
    validatePassword() {
      // 비밀번호가 6자 이상인지 확인
      if (this.password.length < 6) {
        this.passwordValidationMessage = "비밀번호는 6자 이상이어야 합니다";
      } else {
        this.passwordValidationMessage = "";
      }
      // 비밀번호 확인란도 함께 검증
      this.validateConfirmPassword();
    },
    validateConfirmPassword() {
      // 비밀번호와 비밀번호 확인이 일치하는지 확인
      if (this.password === this.confirmPassword) {
        this.confirmPasswordValidationMessage = "비밀번호가 일치합니다";
      } else {
        this.confirmPasswordValidationMessage = "비밀번호가 일치하지 않습니다";
      }
    }
  }
}
</script>

<style scoped>
.password-match-feedback {
  font-size: 6px;
  margin-top: 5px;
}

.text-success {
  color: green;
}

.text-danger {
  color: red;
}
/* 추가적인 스타일링은 여기에 작성하세요 */
</style>
