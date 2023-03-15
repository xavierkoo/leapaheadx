import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import FormView from '../views/FormView.vue'
import AdminDashboard from '../views/DashboardView.vue'
import SubFormDashboard from '../views/SubFormDashboard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/formRender',
      name: 'formRender',
      component: FormView
    },
    {
      path: '/adminDashboard',
      name: 'adminDashboard',
      component: AdminDashboard
    },
    {
      path: '/subFormDashboard',
      name: 'subFormDashboard',
      component: SubFormDashboard
    }
  ]
})

export default router
