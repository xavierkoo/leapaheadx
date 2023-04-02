<template>
    <header>
        <div>
            <nav
                class="navbar navbar-expand pb-3"
                style="text-shadow: 0px 0px 5px rgba(239, 239, 239, 1)"
            >
                <div class="container-fluid" style="height: 50px">
                    <router-link class="navbar-brand nav-margin-left nav-brand" to="/">
                        <div style="width: 127px; height: auto">
                            <img
                                v-if="lightMode == 'true'"
                                src="../assets/img/Logo.png"
                                alt="Light mode logo"
                            />
                            <img v-else src="../assets/img/Logo.png" alt="Dark mode logo" />
                        </div>
                    </router-link>
                </div>
            </nav>
        </div>
    </header>
</template>

<script setup>
// Script Portion
</script>

<style lang="scss" scoped></style>
