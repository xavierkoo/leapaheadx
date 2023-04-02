<script setup>
import { RouterView } from 'vue-router'
import TopNav from './components/TopNav.vue'
import SideNav from './components/SideNav.vue'
import AdminSideNav from './components/AdminSideNav.vue'
</script>

<template>
    <header>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Barlow:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap"
            rel="stylesheet"
        />
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" />

        <!-- * Navigation Bar -->
        <div class="row g-0">
            <div class="col-sm-1 col-xxl-1" />
            <div class="col-sm-10 col-xxl-10">
                <TopNav />
            </div>
            <div class="col-sm-1 col-xxl-1" />
        </div>
    </header>

    <!-- * Body Grid -->
    <div class="row g-2 px-0">
        <div
            v-if="currentRouteName != 'login'"
            class="col-2 col-lg-2 col-xl-1"
            :class="{ 'class-1': class1, 'class-2': class2 }"
        >
            <SideNav v-if="role == 'vendor'" />
            <AdminSideNav v-else />
        </div>
        <div class="d-none d-xxl-block col-xxl-1" style="background-color: #142442" />
        <div class="col col-lg col-xl col-xxl right-section" style="min-height: 100vh">
            <RouterView />
        </div>
        <div class="d-none d-xxl-block col-xxl-1" style="background-color: #142442" />
        <div v-if="currentRouteName != 'login'" class="fix-close">
            <button class="toggle-btn" @click="toggleHidden()">
                <img v-if="class1" src="./assets/icons/menu.svg" height="40" width="40" alt="" />
                <img v-else src="./assets/icons/close.svg" height="40" width="40" alt="" />
            </button>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            class1: false,
            class2: true,
            role: localStorage.getItem('role')
        }
    },
    computed: {
        currentRouteName() {
            return this.$route.name
        }
    },
    methods: {
        toggleHidden() {
            this.class1 = !this.class1
            this.class2 = !this.class2
        }
    }
}
</script>

<style>
.class-1 {
    display: none;
}

.class-2 {
    display: block;
}
</style>
