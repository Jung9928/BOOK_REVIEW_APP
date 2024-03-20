import {createApp} from 'vue'
import router from "@/scripts/router";
import App from './App.vue'
import store from "@/scripts/store";

import {BootstrapVue3} from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

createApp(App).use(BootstrapVue3).use(store).use(router).mount('#app')