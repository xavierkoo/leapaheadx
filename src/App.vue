<script setup>
import { RouterLink, RouterView } from 'vue-router'
import TopNav from './components/TopNav.vue'
import SideNav from './components/SideNav.vue'
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

        <!-- !TODO - Remove before moving to PRODUCTION -->
        <nav>
            <RouterLink to="/">Home</RouterLink>
            &nbsp;
            <RouterLink to="/adminDashboard">Admin Dashboard</RouterLink>
            &nbsp;
            <RouterLink to="/subFormDashboard">SubForm Dashboard</RouterLink>
            &nbsp;
            <RouterLink to="/workFlowDashboard">All Workflow/Forms</RouterLink>
            &nbsp;
            <RouterLink to="/formRender">Form Render</RouterLink>
        </nav>
    </header>

    <!-- * Body Grid -->
    <div class="row g-2 px-0">
        <div class="col-2 col-lg-2 col-xl-1"><SideNav></SideNav></div>
        <div class="col-10 col-lg-10 col-xl-11 right-section"><RouterView /></div>
    </div>
</template>

<style scoped></style>
