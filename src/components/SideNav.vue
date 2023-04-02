<template>
    <nav>
        <div class="d-none d-lg-block text-center">
            <ul>
                <li v-for="item in menuItems" :key="item.id">
                    <router-link :to="item.url">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" height="55">
                            <path :d="item.path" /></svg
                        ><br />
                        <div class="caption">{{ item.label }}</div>
                    </router-link>
                </li>

                <li>
                    <button class="nav-btn" style="background: none" @click="logout()">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" height="55">
                            <path
                                d="M13.34,8.17C12.41,8.17 11.65,7.4 11.65,6.47A1.69,1.69 0 0,1 13.34,4.78C14.28,4.78 15.04,5.54 15.04,6.47C15.04,7.4 14.28,8.17 13.34,8.17M10.3,19.93L4.37,18.75L4.71,17.05L8.86,17.9L10.21,11.04L8.69,11.64V14.5H7V10.54L11.4,8.67L12.07,8.59C12.67,8.59 13.17,8.93 13.5,9.44L14.36,10.79C15.04,12 16.39,12.82 18,12.82V14.5C16.14,14.5 14.44,13.67 13.34,12.4L12.84,14.94L14.61,16.63V23H12.92V17.9L11.14,16.21L10.3,19.93M21,23H19V3H6V16.11L4,15.69V1H21V23M6,23H4V19.78L6,20.2V23Z"
                            /></svg
                        ><br />
                        <div class="caption nav-btn">Log out</div>
                    </button>
                </li>
            </ul>
        </div>
        <div class="d-block d-lg-none text-center">
            <ul>
                <li v-for="item in menuItems" :key="item.id" class="py-4">
                    <router-link :to="item.url">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" height="40">
                            <path :d="item.path" />
                        </svg>
                    </router-link>
                </li>
                <button style="background: none" class="py-4" @click="logout()">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" height="40">
                        <path
                            d="M13.34,8.17C12.41,8.17 11.65,7.4 11.65,6.47A1.69,1.69 0 0,1 13.34,4.78C14.28,4.78 15.04,5.54 15.04,6.47C15.04,7.4 14.28,8.17 13.34,8.17M10.3,19.93L4.37,18.75L4.71,17.05L8.86,17.9L10.21,11.04L8.69,11.64V14.5H7V10.54L11.4,8.67L12.07,8.59C12.67,8.59 13.17,8.93 13.5,9.44L14.36,10.79C15.04,12 16.39,12.82 18,12.82V14.5C16.14,14.5 14.44,13.67 13.34,12.4L12.84,14.94L14.61,16.63V23H12.92V17.9L11.14,16.21L10.3,19.93M21,23H19V3H6V16.11L4,15.69V1H21V23M6,23H4V19.78L6,20.2V23Z"
                        /></svg
                    ><br />
                </button>
            </ul>
        </div>
    </nav>
</template>

<script>
export default {
    data() {
        return {
            menuItems: [
                {
                    id: 1,
                    path: 'M12,3L2,12H5V20H19V12H22L12,3M12,8.75A2.25,2.25 0 0,1 14.25,11A2.25,2.25 0 0,1 12,13.25A2.25,2.25 0 0,1 9.75,11A2.25,2.25 0 0,1 12,8.75M12,15C13.5,15 16.5,15.75 16.5,17.25V18H7.5V17.25C7.5,15.75 10.5,15 12,15Z',
                    label: 'Home',
                    url: '/'
                }
            ]
        }
    },
    methods: {
        logout() {
            localStorage.clear()
            this.$router.replace({ path: '/login' })
        }
    }
}
</script>

<style>
a {
    text-decoration: none;
}

ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    height: 100vh !important;
    overflow-y: hidden !important;
}

li {
    padding: 10px;
    border-bottom: 1px solid #ddd;
}

li:last-child {
    border-bottom: none;
}

router-link {
    color: #333;
    text-decoration: none;
}

router-link:hover {
    color: #000;
}
</style>
