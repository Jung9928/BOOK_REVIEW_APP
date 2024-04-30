<template>
  <footer class="text-muted py-5">
    <div class="container">
      <p class="float-end mb-1" style="font-weight: bold; font-size: 12px">
      </p>
      <p class="mb-1" style="font-weight: bold; font-size: 12px">
        ©reviewmoa.gg |
        <a href="#" data-bs-toggle="modal" data-bs-target="#privacyPolicyModal">개인정보처리방침</a> |
        <a href="#" data-bs-toggle="modal" data-bs-target="#announcementModal">공지사항</a>
      </p>
    </div>

    <PrivacyPolicy/>
    <Announcement/>
  </footer>
</template>

<script>
import PrivacyPolicy from "@/components/PrivacyPolicy";
import Announcement from "@/components/Announcement";

export default {
  name: "Footer",
  components: {
    PrivacyPolicy,
    Announcement
  }
}
</script>

<style scoped>
a {
  text-decoration: none;
  color: #333333;
}
</style>