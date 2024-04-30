// 루트 경로로 접속하면 Home.vue로 이동하도록 설정
import Home from "@/pages/Home";
import Login from "@/pages/Login";
import {createRouter, createWebHistory} from "vue-router";
import Signup from "@/pages/Signup";
import GeneralForum from "@/pages/GeneralForum";
import GeneralForumPost from "@/pages/GeneralForumPost";
import GeneralForumPostDetail from "@/pages/GeneralForumPostDetail";
import GeneralForumModify from "@/pages/GeneralForumModify";
import MemberInfo from "@/pages/MemberInfo";
import MyGeneralForumList from "@/pages/MyGeneralForumList";
import MyCommentList from "@/pages/MyCommentList";

const routes = [
    {path: '/', component: Home},
    {path: '/login', component: Login},
    {path: '/signup', component: Signup},
    {path: '/memberInfo', component: MemberInfo, props: true},
    {path: '/generalForum', component: GeneralForum},
    {path: '/generalForumPost', component: GeneralForumPost},
    {path: '/generalForumModify', component: GeneralForumModify},
    {path: '/generalForumPostDetail', component: GeneralForumPostDetail, props: true},
    {path: '/myGeneralForumList', name: 'MyGeneralForumList', component: MyGeneralForumList, props: true},
    {path: '/myCommentList', name: 'MyCommentList', component: MyCommentList, props: true},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;