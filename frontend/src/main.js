import { createApp } from 'vue'
import router from "@/scripts/router";
import App from './App.vue'
import store from "@/scripts/store";
import {BootstrapVue3} from 'bootstrap-vue-3';
import CKEditor from "@ckeditor/ckeditor5-vue";

// import {VueCookieNext} from 'vue-cookie-next';

createApp(App).use(store).use(router).use(BootstrapVue3).use(CKEditor).mount('#app')
