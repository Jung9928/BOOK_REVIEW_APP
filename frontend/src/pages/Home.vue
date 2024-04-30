<template>
  <div class="home">

    <div class="flex-shrink-0 p-3 bg-white" style="width: 280px;">
      <a href="/" class="d-flex align-items-center pb-3 mb-3 link-dark text-decoration-none border-bottom">
        <svg class="bi pe-none me-2" width="30" height="24"><use xlink:href="#bootstrap"/></svg>
      </a>
      <ul class="list-unstyled ps-0">
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('')" data-bs-toggle="collapse" data-bs-target="#home-collapse" aria-expanded="false">
            전체
          </button>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003031')" data-bs-toggle="collapse" data-bs-target="#computerEngineering-collapse" aria-expanded="false">
            컴퓨터 공학
          </button>
          <div class="collapse" id="computerEngineering-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '001')">컴퓨터교육</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '002')">네트워크/데이터 통신</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '003')">마이크로 프로세서</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '004')">소프트웨어 공학</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '015')">블록체인</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '005')">자료구조/알고리즘</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '006')">전산수학(SPSS/MATLAB)</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '007')">정보통신 공학</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '008')">취미 공학</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '009')">인공지능</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '010')">컴퓨터구조 일반</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '011')">운영체제 일반</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '012')">데이터베이스 일반</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003031', '013')">개발방법론</a></li>

            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003019')" data-bs-toggle="collapse" data-bs-target="#beginner-collapse" aria-expanded="false">
            컴퓨터 입문/활용
          </button>
          <div class="collapse" id="beginner-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003019', '001')">어른을 위한 컴퓨터</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003019', '002')">어린이 컴퓨터</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003019', '004')">윈도우 입문서/활용</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003019', '003')">인터넷 입문서</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003019', '005')">컴퓨터 조립/수리</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003023')" data-bs-toggle="collapse" data-bs-target="#mobile-collapse" aria-expanded="false">
            모바일 프로그래밍
          </button>
          <div class="collapse" id="mobile-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003023', '001')">아이폰</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003023', '002')">안드로이드폰</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003023', '003')">윈도우폰</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003023', '004')">모바일 게임</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003022')" data-bs-toggle="collapse" data-bs-target="#PL-collapse" aria-expanded="false">
            프로그래밍 언어
          </button>
          <div class="collapse" id="PL-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '003')">자바</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '007')">C</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '009')">C++</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '010')">JavaScript/CGI</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '004')">파이썬</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '018')">프로그래밍 교육</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '008')">C#</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '001')">.NET</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '002')">델파이</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '005')">Ajax</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '006')">ASP</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '011')">JSP</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '012')">Perl</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '013')">PHP</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '014')">Ruby/Rails</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '015')">Visual Basic</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '016')">Visual C++</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '017')">XML</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003022', '019')">프로그래밍 언어 기타</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003020')" data-bs-toggle="collapse" data-bs-target="#website-collapse" aria-expanded="false">
            웹사이트
          </button>
          <div class="collapse" id="website-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003020', '001')">HTML/JavaScript/CSS/JQuery</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003020', '002')">웹디자인</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003020', '003')">웹기획</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003020', '004')">UI/UX</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003020', '005')">블로그/홈페이지 만들기</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003025')" data-bs-toggle="collapse" data-bs-target="#os-db-collapse" aria-expanded="false">
            OS/데이터베이스
          </button>
          <div class="collapse" id="os-db-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '009')">클라우드/빅데이터</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '010')">시스템관리/서버</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '003')">리눅스</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '007')">Oracle</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '001')">윈도우</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '008')">SQL Server</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '002')">MAC OS</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '004')">유닉스</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '005')">Access</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003025', '006')">MySQL</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003027')" data-bs-toggle="collapse" data-bs-target="#game-collapse" aria-expanded="false">
            게임
          </button>
          <div class="collapse" id="game-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003027', '003')">게임 개발</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003027', '005')">모바일 게임</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-1">
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" @click="selectMainCategory('001001003024')" data-bs-toggle="collapse" data-bs-target="#network-hacking-security-collapse" aria-expanded="false">
            네트워크/해킹/보안
          </button>
          <div class="collapse" id="network-hacking-security-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003024', '001')">네트워크 일반</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003024', '002')">TCP/IP</a></li>
              <li><a href="#" class="link-dark d-inline-flex text-decoration-none rounded" @click="selectMainCategory('001001003024', '003')">보안/해킹</a></li>
            </ul>
          </div>
        </li>
      </ul>
    </div>

    <div class="content">
      <SearchBox @search="searchBooks"/>
      <div class="album py-5 bg-light">
        <div class="container">
          <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <div class="col" v-for="(book, idx) in bookList" :key="idx">
              <Card :book="book"/>
            </div>
          </div>
          <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link" @click="goFirstPage">&laquo;</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <button class="page-link" @click="prevPage">&lt;</button>
              </li>
              <li class="page-item" v-for="page in displayedPages" :key="page" :class="{ active: currentPage === page }">
                <button class="page-link" @click="gotoPage(page)">{{ page }}</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link" @click="nextPage">&gt;</button>
              </li>
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <button class="page-link" @click="goLastPage">&raquo;</button>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {ref, computed} from "vue";
import Card from "@/components/Card";
import SearchBox from "@/components/SearchBox";

export default {
  name: "Home",
  components: {SearchBox, Card},

  setup() {
    const bookList = ref([]);
    const currentPage = ref(1);
    const totalPages = ref(0);
    const selectedMainCategory = ref("");
    const selectedSubCategory = ref("");

    // 사이드 바 메뉴 선택 시
    const selectMainCategory = (category, subCategory) => {
      selectedMainCategory.value = category;    // 선택한 main 카테고리 값 셋팅
      selectedSubCategory.value = subCategory;  // 선택한 sub 카테고리 값 셋팅
      fetchBooks();                             // 선택한 카테고리에 해당하는 도서 데이터 get
    }

    // 검색 버튼 클릭 시, 카테고리, 검색 데이터 전달
    const searchBooks = (searchValue, searchDetailCategory) => {
      fetchBooks(searchValue, searchDetailCategory);
    };

    // 도서 리스트 get
    const fetchBooks = (searchValue, searchDetailCategory) => {
      const params = {
        page: currentPage.value,
        size: 9, // 페이지당 아이템 수
        searchValue: searchValue,
        searchDetailCategory: searchDetailCategory,          // 선택한 상세 카테고리 전달 (제목, 저자, 출판사)
        searchSubCategory: selectedSubCategory.value,        // 선택한 서브 카테고리 전달
        searchMainCategory: selectedMainCategory.value       // 선택한 메인 카테고리 전달
      };

      axios.get(`/api/v1/book/list`, { params })
          .then((res) => {
            if (res.data.resultCode === "OK") {
              bookList.value = res.data.data;
              totalPages.value = res.data.bookPaginationDTO.totalPageCnt;
            }
          })
          .catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n 잠시 후, 다시 시도해주세요.');
            }
          });
    };

    // 맨 첫페이지 이동
    const goFirstPage = () => {
      if (currentPage.value !== 1) {
        currentPage.value = 1;
        fetchBooks();
      }
    };

    // 맨 마지막 페이지 이동
    const goLastPage = () => {
      if (currentPage.value !== totalPages.value) {
        currentPage.value = totalPages.value;
        fetchBooks();
      } else {
        window.alert("현재 페이지가 마지막 페이지 입니다");
      }
    };

    // 이전 페이지 이동
    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchBooks();
      }
    };

    // 다음 페이지 이동
    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchBooks();
      }
    };

    // 페이지 선택하여 이동
    const gotoPage = (page) => {
      currentPage.value = page;
      fetchBooks();
    };

    // computed 속성을 통해 현재 페이지의 표시할 페이지 수 계산
    const displayedPages = computed(() => {
      const numPages = [];
      const numDisplayed = 5; // 표시할 페이지 번호 개수
      const halfDisplayed = Math.floor(numDisplayed / 2);
      let startPage = Math.max(1, currentPage.value - halfDisplayed);
      let endPage = Math.min(totalPages.value, startPage + numDisplayed - 1);

      // 도서 리스트 총 페이지 수가 화면에 기본적으로 표시할 페이지 버튼 번호인 5보다 작을 경우
      // if (totalPages.value <= numDisplayed) {
      //   startPage = 1;
      //   endPage = totalPages.value;
      // } else if (currentPage.value <= halfDisplayed) {
      //   endPage = numDisplayed;
      // } else if (currentPage.value >= totalPages.value - halfDisplayed) {
      //   startPage = totalPages.value - numDisplayed + 1;
      //   endPage = totalPages.value;
      // }

      // 마지막 페이지를 초과하는 경우 수정
      // if (endPage - startPage < numDisplayed - 1 && totalPages.value > numDisplayed) {
      //   startPage = Math.max(1, totalPages.value - numDisplayed + 1);
      // }

      if (endPage > totalPages.value) {
        endPage = totalPages.value;
        startPage = Math.max(1, endPage - numDisplayed + 1);
      }

      for (let i = startPage; i <= endPage; i++) {
        numPages.push(i);
      }

      console.log("halfDisplayed : " + halfDisplayed)
      console.log("endPage : " + endPage)
      console.log("startPage : " + startPage)

      return numPages;
    });


    fetchBooks(); // 페이지가 로드될 때 처음에도 데이터를 가져옴

    return {
      bookList,
      currentPage,
      totalPages,
      goFirstPage,
      goLastPage,
      prevPage,
      nextPage,
      searchBooks,
      displayedPages,
      gotoPage,
      selectMainCategory,
      selectedSubCategory
    };
  },
}
</script>

<style scoped>
.home {
  display: flex;
}

.content {
  flex: 1;
}

/* 페이지네이션 css */
.page-link {
  color: #000;
  background-color: #fff;
  border: 1px solid #ccc;
}

.page-item.active .page-link {
  z-index: 1;
  color: #555;
  font-weight:bold;
  background-color: #f1f1f1;
  border-color: #ccc;

}

.page-link:focus, .page-link:hover {
  color: #000;
  background-color: #fafafa;
  border-color: #ccc;
}

/* 사이드바 */
body {
  min-height: 100vh;
  min-height: -webkit-fill-available;
}

html {
  height: -webkit-fill-available;
}

main {
  height: 100vh;
  height: -webkit-fill-available;
  max-height: 100vh;
  overflow-x: auto;
  overflow-y: hidden;
}

.btn-toggle {
  padding: .25rem .5rem;
  font-weight: 600;
  color: rgba(0, 0, 0, .65);
  background-color: transparent;
}
.btn-toggle:hover,
.btn-toggle:focus {
  color: rgba(0, 0, 0, .85);
  background-color: #d2f4ea;
}

.btn-toggle::before {
  width: 1.25em;
  line-height: 0;
  content: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='rgba%280,0,0,.5%29' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M5 14l6-6-6-6'/%3e%3c/svg%3e");
  transition: transform .35s ease;
  transform-origin: .5em 50%;
}

.btn-toggle[aria-expanded="true"] {
  color: rgba(0, 0, 0, .85);
}
.btn-toggle[aria-expanded="true"]::before {
  transform: rotate(90deg);
}

.btn-toggle-nav a {
  padding: .1875rem .5rem;
  margin-top: .125rem;
  margin-left: 1.25rem;
}
.btn-toggle-nav a:hover,
.btn-toggle-nav a:focus {
  background-color: #d2f4ea;
}


</style>