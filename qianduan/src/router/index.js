import {createRouter, createWebHistory} from 'vue-router'
import {useUserStore} from "@/stores/userInfo";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', component: () => import('../views/user/LoginPage.vue')},
    {path: '/login', component: () => import('../views/user/LoginPage.vue')},
    {path: '/Register', component: () => import('../views/user/Register.vue')},
    {
      path: '/front', component: () => import('../views/layout/FrontPage.vue'),
      redirect: '/front/Home',
      children: [
        {path: '/front/Profile', component: () => import('../views/front/Profile.vue')},
        {path: '/front/Home', component: () => import('../views/front/Home.vue')},
        {path: '/front/post/:id', component: () => import('../views/front/PostDetail.vue')},
        {path: '/front/PostSend', component: () => import('../views/front/PostSend.vue')},
        {path: '/front/MyPost', component: () => import('../views/front/MyPost.vue')},
        {path: '/front/MyReply', component: () => import('../views/front/MyReply.vue')},
        {path: '/front/OnlineChat', component: () => import('../views/front/OnlineChat.vue')},
      ]
    },
    {
      path: '/end', component: () => import('../views/layout/EndPage.vue'),
      redirect: '/end/Profile',
      children: [
        {
          path: '/end/UserManage',
          component: () => import('../views/manage/UserManage.vue'),
          meta:{name:'User Management'}
        },
        {
          path: '/end/Post',
          component: () => import('../views/manage/Post.vue'),
          meta:{name:'Post List'}
        },
        {
          path: '/end/Reply',
          component: () => import('../views/manage/Reply.vue'),
          meta:{name:'Reply List'}
        },
        {
          path: '/end/Report',
          component: () => import('../views/manage/Report.vue'),
          meta:{name:'Report List'}
        },
        {
          path: '/end/Profile',
          component: () => import('../views/manage/Profile.vue'),
          meta:{name:'Personal Center'}
        },
      ]
    }
  ]
})




router.beforeEach((to, from, next) => {
  if (to.href === '/login' || to.href === '/Register') {
    next()
  } else {
    if (to.href !== '/login' && useUserStore().user === null) {
      next('/login')
    } else {
      next()
    }
  }
})


export default router
