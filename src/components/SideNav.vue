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
                    <button style="background: none" @click="logout()">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" height="55">
                            <path
                                d="M13.34,8.17C12.41,8.17 11.65,7.4 11.65,6.47A1.69,1.69 0 0,1 13.34,4.78C14.28,4.78 15.04,5.54 15.04,6.47C15.04,7.4 14.28,8.17 13.34,8.17M10.3,19.93L4.37,18.75L4.71,17.05L8.86,17.9L10.21,11.04L8.69,11.64V14.5H7V10.54L11.4,8.67L12.07,8.59C12.67,8.59 13.17,8.93 13.5,9.44L14.36,10.79C15.04,12 16.39,12.82 18,12.82V14.5C16.14,14.5 14.44,13.67 13.34,12.4L12.84,14.94L14.61,16.63V23H12.92V17.9L11.14,16.21L10.3,19.93M21,23H19V3H6V16.11L4,15.69V1H21V23M6,23H4V19.78L6,20.2V23Z"
                            /></svg
                        ><br />
                        <div class="caption">Log out</div>
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
                },
                {
                    id: 2,
                    path: 'M9,2V8H11V11H5C3.89,11 3,11.89 3,13V16H1V22H7V16H5V13H11V16H9V22H15V16H13V13H19V16H17V22H23V16H21V13C21,11.89 20.11,11 19,11H13V8H15V2H9Z',
                    label: 'Workflow',
                    url: '/workFlowDashboard'
                },
                {
                    id: 3,
                    path: 'M19 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H19C20.1 21 21 20.1 21 19V5C21 3.9 20.1 3 19 3M7 7H9V9H7V7M7 11H9V13H7V11M7 15H9V17H7V15M17 17H11V15H17V17M17 13H11V11H17V13M17 9H11V7H17V9Z',
                    label: 'Sub-Form',
                    url: '/subFormDashboard'
                },
                {
                    id: 4,
                    path: 'M22 6C22 4.9 21.1 4 20 4H4C2.9 4 2 4.9 2 6V18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6M20 6L12 11L4 6H20M20 18H4V8L12 13L20 8V18Z',
                    label: 'Approved',
                    url: '/approved'
                },
                {
                    id: 5,
                    path: 'M6,19A2,2 0 0,0 8,21H16A2,2 0 0,0 18,19V7H6V19M8,9H16V19H8V9M15.5,4L14.5,3H9.5L8.5,4H5V6H19V4H15.5Z',
                    label: 'Deleted',
                    url: '/deleted'
                },
                {
                    id: 6,
                    path: 'M12,5.5A3.5,3.5 0 0,1 15.5,9A3.5,3.5 0 0,1 12,12.5A3.5,3.5 0 0,1 8.5,9A3.5,3.5 0 0,1 12,5.5M5,8C5.56,8 6.08,8.15 6.53,8.42C6.38,9.85 6.8,11.27 7.66,12.38C7.16,13.34 6.16,14 5,14A3,3 0 0,1 2,11A3,3 0 0,1 5,8M19,8A3,3 0 0,1 22,11A3,3 0 0,1 19,14C17.84,14 16.84,13.34 16.34,12.38C17.2,11.27 17.62,9.85 17.47,8.42C17.92,8.15 18.44,8 19,8M5.5,18.25C5.5,16.18 8.41,14.5 12,14.5C15.59,14.5 18.5,16.18 18.5,18.25V20H5.5V18.25M0,20V18.5C0,17.11 1.89,15.94 4.45,15.6C3.86,16.28 3.5,17.22 3.5,18.25V20H0M24,20H20.5V18.25C20.5,17.22 20.14,16.28 19.55,15.6C22.11,15.94 24,17.11 24,18.5V20Z',
                    label: 'Accounts',
                    url: '/accountsDashboard'
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
