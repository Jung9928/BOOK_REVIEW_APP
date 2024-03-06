import {createApp} from 'vue'
import router from "@/scripts/router";
import App from './App.vue'
import store from "@/scripts/store";


createApp(App).use(store).use(router).mount('#app')