// 루트 경로로 접속하면 Home.vue로 이동하도록 설정
import Home from "@/pages/Home";
import Login from "@/pages/Login";
import {createRouter, createWebHistory} from "vue-router";
import Signup from "@/pages/Signup";
import GeneralForum from "@/pages/GeneralForum";

const routes = [
    {path: '/', component: Home},
    {path: '/login', component: Login},
    {path: '/signup', component: Signup},
    {path: '/generalForum', component: GeneralForum}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;