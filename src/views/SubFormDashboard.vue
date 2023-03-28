<template>
    <!-- This is the dashboard header -->
    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col-12 col-sm-8 col-lg-5 col-xl-3">
            <h4>My Dashboard</h4>
        </div>
        <div class="d-none d-lg-block col-lg col-xl" />
        <div class="col text-start col-sm-4 col-lg-5 col-xl-3 text-sm-end">
            <!-- TODO - Change this to variable based on login -->
            <h5>Admin1</h5>
        </div>
    </div>

    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col dark-container pb-5">
            <div class="row mx-sm-2 mx-lg-5">
                <div class="col-sm-7 col-lg-9 pad-e"><h5>All Form Components</h5></div>
                <div class="col-sm-5 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                    <!-- New Sub Form -->
                    <router-link to="/subFormBuilder">
                        <button class="blue-button">New</button>
                    </router-link>
                </div>
                <div class="d-none d-xl-block col-xl-1"></div>
            </div>

            <!-- This is the Header Row -->
            <div class="row mx-0 mx-sm-2 mx-lg-5 pad-d">
                <div class="col-6 col-sm-8 col-lg-6 tableHeader">Name</div>
                <div class="d-none d-sm-block col-sm-4 col-lg-3 tableHeader">Date created</div>
                <div class="d-none d-xl-block col-xl-1" />
                <div class="d-none d-lg-block col-lg-3 col-xl-2 tableHeader">Actions</div>
            </div>

            <!-- This is the For-loop of all the records-->
            <div
                v-for="item in data"
                :key="item.canvasUuid"
                class="row tableRow justify-content-center align-items-center mx-sm-2 mx-lg-5 pad-e"
            >
                <div class="col-sm-8 col-lg-6">
                    <div class="row justify-content-center align-items-center">
                        <div class="col-12">{{ item.name }}</div>
                        <div class="col-12 tableCaption">{{ item.canvasUuid }}</div>
                    </div>
                </div>
                <div class="col-sm-4 col-lg-3">
                    <p>{{ new Date(item.dateCreated).toLocaleString('en-SG', { 
                        day: 'numeric', 
                        month: 'numeric', 
                        year: 'numeric', 
                        hour: 'numeric', 
                        minute: 'numeric', 
                        hour12: false 
                    }) }}</p>
                </div>
                <div class="d-none d-xl-block col-xl-1" />

                <!-- Rendering of the action icons -->
                <div
                    class="pt-2 col-sm-12 text-center py-sm-2 col-lg-3 col-xl-2 pt-y-0 text-lg-start"
                >
                    <router-link :to="'/subformbuilder/' + item.canvasUuid" >
                        <button class="btn-bg-primary mx-2">
                            <!-- File view Icon -->
                            <img
                                src="../assets/icons/file-eye-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>

                    <router-link to="/">
                        <button class="btn-bg-outline mx-2">
                            <!-- Delete Icon -->
                            <img
                                src="../assets/icons/delete-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

const data = ref([])

onMounted(async () => {
    // Retrieve all sub-form canvas from the database
    const response = await axios.get('http://localhost:8080/api/subformcanvas')
    data.value = response.data
})
</script>

<style>
table,
th,
td {
    border: none;
}
</style>
