import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('../views/HomeView.vue')
        },
        {
            path: '/formRender',
            name: 'formRender',
            component: () => import('../views/FormView.vue')
        },
        {
            path: '/adminDashboard',
            name: 'adminDashboard',
            component: () => import('../views/DashboardView.vue')
        },
        {
            path: '/subFormDashboard',
            name: 'subFormDashboard',
            component: () => import('../views/SubFormDashboard.vue')
        },
        {
            path: '/workFlowDashboard',
            name: 'allforms',
            component: () => import('../views/AllForms.vue')
        }
    ]
})

export default router
