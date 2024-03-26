<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card">
          <div class="card-body">
            <h2 class="text-center mb-4">회원 가입</h2>
            <form @submit.prevent>
              <!-- 아이디 입력란 -->
              <div class="mb-3">
                <label for="username" class="form-label">아이디</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="username" placeholder="아이디를 입력하세요" v-model="memberId" @input="validateUsername">
                  <button class="btn btn-outline-secondary" type="button" @click="checkDuplicateUsername">중복 확인</button>
                </div>
                <div v-if="usernameValidationMessage" :class="{ 'text-success': isUsernameValid, 'text-danger': !isUsernameValid }">{{ usernameValidationMessage }}</div>
              </div>

              <!-- 이메일 입력란 -->
              <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <div class="input-group">
                  <input type="email" class="form-control" id="email" placeholder="이메일을 입력하세요" v-model="email" @input="validateEmail">
                  <button type="button" class="btn btn-outline-secondary" @click="sendVerificationEmail">이메일 인증</button>
                </div>
                <div v-if="emailValidationMessage" :class="{ 'text-success': isEmailValid, 'text-danger': !isEmailValid }">{{ emailValidationMessage }}</div>
              </div>

              <!-- 추가: 정상적으로 인증 메일이 전송된 경우에만 인증코드 입력란 표시 -->
              <div v-if="isSendVerificationEmail">
                <!-- 인증번호 입력란 -->
                <div class="mb-3">
                  <label for="verificationCode" class="form-label">인증코드</label>
                  <div class="input-group">
                    <input type="text" class="form-control" id="verificationCode" placeholder="인증코드를 입력하세요" v-model="verificationCode">

                    <!-- 타이머 표시 -->
<!--                    <div class="input-group-append">-->
<!--                      <span class="input-group-text">{{ timerFormatted }}</span>-->
<!--                    </div>-->
                    <div v-if="showTimer" class="input-group-append">
                      <span class="input-group-text">{{ timerFormatted }}</span>
                    </div>

                    <button type="button" class="btn btn-outline-secondary" @click="sendVerificationCode">인증코드 확인</button>
                  </div>
                  <!-- 인증번호 입력에 대한 유효성 검사 등을 추가할 수 있습니다. -->
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
                <button type="submit" class="btn btn-outline-dark w-100" @click="signup">회원 가입</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onUnmounted } from 'vue';
import axios from "axios";
import router from "@/scripts/router";
// import store from "@/scripts/store";

export default {
  setup() {
    // Data
    const memberId = ref("");
    const email = ref("");
    const password = ref("");
    const confirmPassword = ref("");
    const usernameValidationMessage = ref("");
    const emailValidationMessage = ref("");
    const passwordValidationMessage = ref("");
    const confirmPasswordValidationMessage = ref("");

    const isUsernameValid = ref(false);
    const isEmailValid = ref(false);
    const isPasswordValid = ref(false);
    const isConfirmPasswordValid = ref(false);
    const isMemberIdDuplValid = ref(false);

    // 추가: 인증 메일 전송 여부를 나타내는 변수
    const isSendVerificationEmail = ref(false);
    const isSendVerificationCode = ref(false);
    const verificationCode = ref(""); // 추가: 인증번호 입력란의 모델
    const showTimer = ref(false);

    // 타이머 관련 변수
    const timerMinutes = ref(3);
    const timerSeconds = ref(0);
    const timerInterval = ref(null);

    // 타이머를 표시하기 위한 포맷팅
    const timerFormatted = ref('00:00');


    // 타이머 시작 함수
    const startTimer = () => {
      showTimer.value = true;
      timerInterval.value = setInterval(() => {
        if (timerMinutes.value === 0 && timerSeconds.value === 0) {
          clearInterval(timerInterval.value);

          // 타이머 종료에 따른 추가 작업
          window.alert("인증 유효 시간이 만료되었습니다. 다시 이메일 인증을 진행해 주세요");
          verificationCode.value = '';
          showTimer.value = false;

          // 유효시간 초기화
          timerMinutes.value = 3;
          timerSeconds.value = 0;
          timerFormatted.value = '03:00'; // 초기값으로 다시 설정
        } else {
          updateTimer();
        }
      }, 1000);
    };

    // 타이머 업데이트 함수
    const updateTimer = () => {
      if (timerSeconds.value === 0) {
        timerMinutes.value -= 1;
        timerSeconds.value = 59;
      } else {
        timerSeconds.value -= 1;
      }

      // 타이머 포맷팅 업데이트
      timerFormatted.value = `${String(timerMinutes.value).padStart(2, '0')}:${String(timerSeconds.value).padStart(2, '0')}`;
    };

    // 인증코드 전송시 타이머 시작
    // onMounted(() => {
    //   startTimer();
    // });

    // 컴포넌트 제거 시 타이머 정리
    onUnmounted(() => {
      clearInterval(timerInterval.value);
    });

    // 아이디 입력 형식 검증 메소드
    const validateUsername = () => {
      // 아이디가 6자 이상, 20자 이하이며 영문자 또는 숫자를 포함하는지 여부 확인
      const regex = /^[a-zA-Z][a-zA-Z0-9]{6,20}$/;
      isUsernameValid.value = regex.test(memberId.value);
      if (!isUsernameValid.value) {
        usernameValidationMessage.value = "ID는 6자 이상 20자 이하이며 영문자 또는 영문 + 숫자로 구성되어야 합니다";
      } else {
        usernameValidationMessage.value = "사용 가능한 아이디입니다";
      }
    };

    const validateEmail = () => {
      // 이메일 형식 확인
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      isEmailValid.value = emailRegex.test(email.value);
      if (!isEmailValid.value) {
        emailValidationMessage.value = "올바른 이메일 형식이 아닙니다";
      } else {
        emailValidationMessage.value = "사용 가능한 이메일 형식입니다";
      }
    };

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

    // 아이디 중복 확인 버튼 메소드
    // 엔드포인트 호출하여 ID 중복 예외가 터지면 catch 안터지면 then
    const checkDuplicateUsername = () => {

      // Form에 입력된 아이디 가져옴
      const inputId = memberId.value;

      // 아이디가 비어있거나 조건에 맞지 않으면 팝업
      if(!inputId || inputId.length < 6 || inputId.length > 20 || !/^[a-zA-Z][a-zA-Z0-9]{6,20}$/.test(inputId)) {
        window.alert("올바른 아이디 형식이 아닙니다. 다시 작성 후 버튼을 눌러주세요");
        memberId.value = '';
        return; // 팝업을 띄우고 함수 종료
      }

      // 중복확인을 위해 서버에 아이디를 전송
      axios.get(`/api/v1/members/verifyId/${memberId.value}`)
          .then((res) => {
            // 중복된 아이디인 경우
            console.log(res.data);
            isMemberIdDuplValid.value = false;
            window.alert("이미 사용중인 아이디입니다", res);
          })
          .catch((res) => {
            // 중복되지 않은 아이디인 경우(중복 예외 발생)
            console.log(res);
            console.log(res.data);
            isMemberIdDuplValid.value = true;
            window.alert("사용 가능한 아이디입니다.", res);
          });
    };

    // 추가: 이메일 인증 버튼 클릭 시 인증 메일을 서버에 요청
    const sendVerificationEmail = () => {

      // Form에 입력된 email 가져옴
      const inputEmail = email.value;

      // email이 비어있거나 조건에 맞지 않으면 팝업
      if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(inputEmail)) {
        window.alert("올바른 이메일 형식이 아닙니다. 다시 작성 후 버튼을 눌러주세요");
        email.value = '';
        return; // 팝업을 띄우고 함수 종료
      }

      // 이메일 인증을 위해 서버에 요청
      axios.get(`/api/v1/verifyEmail/${email.value}`)
          .then((response) => {
            // 서버 응답이 성공적일 때
            console.log(response.data);
            if (response.status === 200) {
              // HTTP 상태 코드가 성공(200)이면 인증메일 전송 성공
              window.alert("인증메일을 전송하였습니다");
              isSendVerificationEmail.value = true; // 추가: 인증 메일 전송 성공 시 플래그 설정

              // 유효시간 렌더링 재시작
              startTimer();
            } else {
              // 다른 상태 코드에 대한 처리
              window.alert("인증메일 전송에 오류가 발생했습니다. 다시 전송해주세요");
            }
          })
          .catch((error) => {
            // 서버 응답이 실패했을 때
            console.error(error.response.data);
            isSendVerificationEmail.value = false; // 추가: 인증 메일 전송 성공 시 플래그 설정
            // HTTP 상태 코드에 따라 분기
            if (error.response.status === 401) {
              // 401 Unauthorized: 이메일 형식이 잘못된 경우
              window.alert("이메일 형식이 잘못되었습니다");
            } else {
              // 다른 상태 코드에 대한 처리
              window.alert("인증메일 전송에 오류가 발생했습니다. 다시 전송해주세요");
            }
          });
    };

    // 인증코드 검증 메소드
    const sendVerificationCode = () => {
      // Form에 입력된 인증코드 가져옴

      // 인증코드 검증을 위해 서버에 요청
      axios.post(`/api/v1/verifyEmail/${email.value}`, {
        verificationCode: verificationCode.value,
        email: email.value
      })
          .then((response) => {
            // 서버 응답이 성공적일 때
            console.log(verificationCode);
            console.log(email);
            console.log(response.data);

            // HTTP 상태 코드가 성공(200)이면 인증코드 검증 성공 팝업
            window.alert("인증코드가 일치합니다");
            isSendVerificationCode.value = true; // 추가: 인증코드 검증 성공 시 플래그 설정
          })
          .catch((error) => {
            // 서버 응답이 실패했을 때
            console.log(verificationCode);
            console.log(email);
            console.error(error.response.data);
            isSendVerificationCode.value = false; // 추가: 인증코드 검증 실패 시 플래그 설정

            // HTTP 상태 코드에 따라 분기
            if (error.response.status === 401) {
              // 401 Unauthorized: 이메일 형식이 잘못된 경우
              window.alert("이메일 형식이 잘못되었습니다");
            } else {
              // 다른 상태 코드에 대한 처리
              window.alert("인증메일 전송에 오류가 발생했습니다. 다시 이메일 인증을 진행해주세요");
            }
          });
    }

    // 회원가입 버튼을 눌렀을 시, 실행되는 메소드
    const signup = () => {
      // 모든 조건 검증
      if (!isUsernameValid.value) {
        window.alert("아이디를 확인해주세요");
        return;
      }

      if (!isEmailValid.value) {
        window.alert("이메일을 확인해주세요");
        return;
      }

      if (!isSendVerificationEmail.value) {
        window.alert("이메일 인증을 먼저 진행해주세요");
        return;
      }

      if (!isSendVerificationCode.value) {
        window.alert("인증코드 확인을 먼저 진행해주세요");
        return;
      }

      if (!isPasswordValid.value || !isConfirmPasswordValid.value) {
        window.alert("비밀번호를 확인해주세요");
        return;
      }

      // 모든 조건을 통과한 경우, 실제 회원가입 로직을 수행
      // 여기에 실제 회원가입 로직을 추가하세요.
      axios.post('/api/v1/members/signup', {
        memberId: memberId.value,
        email: email.value,
        password: password.value
      })
        .then(response => {
          // 회원가입 성공
          console.log(response.data);
          window.alert("회원가입이 완료되었습니다");
          router.push({path:"/"});
        })
        .catch(error => {
          // 회원가입 실패
          console.error(error.response.data);
          window.alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요");
        });
    };


    // Return
    return {
      memberId,
      email,
      password,
      confirmPassword,
      usernameValidationMessage,
      emailValidationMessage,
      passwordValidationMessage,
      confirmPasswordValidationMessage,
      validateUsername,
      validateEmail,
      validatePassword,
      validateConfirmPassword,

      isUsernameValid,
      isEmailValid,
      isPasswordValid,
      isConfirmPasswordValid,
      checkDuplicateUsername,

      isSendVerificationEmail,
      verificationCode,
      sendVerificationEmail,
      sendVerificationCode,
      showTimer,

      timerFormatted,
      signup
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
/* 추가적인 스타일링은 여기에 작성하세요 */
</style>
