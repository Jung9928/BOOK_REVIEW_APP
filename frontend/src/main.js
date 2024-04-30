import { createApp } from 'vue'
import router from "@/scripts/router";
import App from './App.vue'
import store from "@/scripts/store";
// import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import CKEditor from "@ckeditor/ckeditor5-vue";

// import {VueCookieNext} from 'vue-cookie-next';

createApp(App).use(store).use(router).use(CKEditor).mount('#app')
