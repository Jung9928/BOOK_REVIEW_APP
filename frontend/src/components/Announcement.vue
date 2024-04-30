<template>
  <div class="modal fade" id="announcementModal" tabindex="-1" aria-labelledby="announcementModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">

        <div class="modal-header">
          <h1 class="modal-title fs-5" id="announcementModalLabel">공지사항</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <p>특정 도서 사이트들의 리뷰데이터 수집이 막혀있어 가까운 시일 내에 타 사이트의 리뷰 데이터를 업데이트 하도록 하겠습니다.</p>


          <p>이 외에 커뮤니티 기능들은 추후 개발 및 UI 개선 작업을 진행할 예정입니다.</p>


        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-outline-dark" data-bs-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Announcement"
}
</script>

<style scoped>

</style>