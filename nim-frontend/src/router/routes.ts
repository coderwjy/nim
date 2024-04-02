import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/main'
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/Login.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('@/views/register/Register.vue')
  },
  {
    path: '/main',
    name: 'main',
    component: () => import('@/views/main/Main.vue'),
    redirect: '/main/menu',
    children: [
      {
        path: 'menu',
        name: 'game-menu',
        component: () => import('@/views/main/game-menu/GameMenu.vue')
      },
      {
        path: 'local-game',
        name: 'local-game',
        component: () => import('@/views/main/local-game/LocalGame.vue')
      },
      {
        path: 'online',
        name: 'online',
        component: () => import('@/views/main/create-game/CreateGame.vue')
      },
      {
        path: 'game',
        name: 'game',
        component: () => import('@/views/main/game/Game.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('@/views/not-found/NotFound.vue')
  }
]

export default routes
