import {createApp} from 'vue'
import router from "@/scripts/router";
import App from './App.vue'
import store from "@/scripts/store";

import {BootstrapVue3} from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
// import {VueCookieNext} from 'vue-cookie-next';
import VueCookies from "vue-cookies";

createApp(App).use(store).use(router).use(BootstrapVue3).use(VueCookies).mount('#app')