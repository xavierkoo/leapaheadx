import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('../views/HomeView.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['vendor']
            }
        },

        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginView.vue')
        },

        {
            path: '/formRender/:applicationId',
            name: 'formRender',
            component: () => import('../views/FormView.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['vendor', 'admin', 'approver']
            }
        },
        {
            path: '/adminDashboard',
            name: 'adminDashboard',
            component: () => import('../views/DashboardView.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/subFormDashboard',
            name: 'subFormDashboard',
            component: () => import('../views/SubFormDashboard.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/subFormView/:canvasUuid',
            name: 'subFormView',
            component: () => import('../views/SubFormView.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/subFormBuilder',
            name: 'editSubFormBuilder',
            component: () => import('../views/SubFormBuilder.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/workFlowDashboard',
            name: 'formDashboard',
            component: () => import('../views/FormDashboard.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/newWorkflow',
            name: 'newWorkflow',
            component: () => import('../views/NewWorkflow.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/newWorkflow/:formUuid',
            name: 'editWorkflow',
            component: () => import('../views/NewWorkflow.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        },
        {
            path: '/accountsDashboard',
            name: 'accountsDashboard',
            component: () => import('../views/AccountsView.vue'),
            meta: {
                requiresAuth: true,
                allowedUsers: ['admin', 'approver']
            }
        }
    ]
})

router.beforeEach((to, from, next) => {
    const requiresAuth = to.meta.requiresAuth
    const allowedUsers = to.meta.allowedUsers
    const userType = localStorage.getItem('role')

    if (requiresAuth && !userType) {
        alert('Please login to use the application!')
        next('/login')
    } else if (requiresAuth && !allowedUsers.includes(userType)) {
        alert(
            'Oops! Looks like you entered a page you are not suppose to! Log in to the approriate account'
        )
        next('/login')
    } else {
        next()
    }
})

export default router
