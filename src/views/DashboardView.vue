<template>
    <!-- This is the dashboard header -->
    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col-12 col-sm-8 col-lg-5 col-xl-3">
            <h4>My Dashboard</h4>
        </div>
        <div class="d-none d-lg-block col-lg col-xl" />
        <div class="col text-start col-sm-4 col-lg-5 col-xl-3 text-sm-end">
            <h5>{{ name }}</h5>
        </div>
    </div>

    <!-- This is the status filter buttons  -->
    <div class="row mx-2 mx-sm-5 pad-f">
        <div class="py-3 py-sm-0 col-sm-6 col-lg-6 col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('ToDo')">
                <h4>To Do</h4>
                <h2 class="pad-c">{{ toDo }}</h2>
            </div>
        </div>
        <div class="py-3 py-sm-0 col-sm-6 col-lg-6 col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('Pending')">
                <h4>Pending</h4>
                <h2 class="pad-c">{{ pending }}</h2>
            </div>
        </div>
        <div class="py-3 py-sm-0 col-sm-6 col-lg-6 mt-sm-3 col-xl-3 mt-xl-0">
            <div class="button-like-div py-4" @click="filterByStatus('Approved')">
                <h4>Approved</h4>
                <h2 class="pad-c">{{ approved }}</h2>
            </div>
        </div>
        <div class="py-3 py-sm-0 col-sm-6 col-lg-6 mt-sm-3 col-xl-3 mt-xl-0">
            <div class="button-like-div py-4" @click="filterByStatus('Rejected')">
                <h4>Rejected</h4>
                <h2 class="pad-c">{{ rejected }}</h2>
            </div>
        </div>
        <div v-if="filtered" class="col-xl-2 py-2">
            <button class="btn-bg-secondary" @click="getAllApplications()">
                <div class="caption">Reset</div>
            </button>
        </div>
    </div>

    <!-- This is the Application Submission Table -->
    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col dark-container pb-5">
            <div class="row mx-sm-2 mx-lg-5">
                <div class="col pad-e"><h5>Application Submissions</h5></div>
            </div>

            <!-- This is the Header Row -->
            <div class="row mx-0 mx-sm-2 mx-lg-5 pad-d">
                <div class="col-6 col-sm-8 col-lg-7 tableHeader">Name</div>
                <div class="d-none d-sm-block col-sm-4 col-lg-2 tableHeader">Status</div>
                <div class="d-none d-xl-block col-xl-1" />
                <div class="d-none d-lg-block col-lg-2 tableHeader">Actions</div>
            </div>

            <!-- This is the For-loop of all the records-->
            <div
                v-for="item in data"
                :key="item.id"
                class="row tableRow justify-content-center align-items-center mx-sm-2 mx-lg-5 pad-e"
            >
                <div class="col-sm-8 col-lg-7">
                    <div class="row justify-content-center align-items-center">
                        <div class="col-12">{{ item.formName }} - {{ item.company }}</div>
                        <div class="col-12 tableCaption">{{ item.applicationUuid }}</div>
                    </div>
                </div>
                <div class="col-sm-4 col-lg-2" :class="getStatusColour(item.status)">
                    {{ getStatus(item.status) }}
                </div>
                <div class="d-none d-xl-block col-xl-1" />
                <!-- Conditional Rending for Not Started & In Progress -->
                <div
                    v-if="item.status == 'InProgress' || item.status == 'NotStarted'"
                    class="col-sm-12 text-center py-sm-2 col-lg-3 pt-y-0 text-lg-start col-xl-2"
                >
                    <router-link :to="`/formRender/${item.applicationUuid}`">
                        <button class="btn-bg-primary mx-2">
                            <!-- Edit Icon -->
                            <img
                                src="../assets/icons/note-edit-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>

                    <button class="btn-bg-outline mx-2" @click="archive(item.applicationUuid)">
                        <!-- Delete Icon -->
                        <img
                            src="../assets/icons/delete-outline.svg"
                            alt=""
                            width="24"
                            height="24"
                        />
                    </button>
                </div>
                <!-- Conditional Rending for Approved -->
                <div
                    v-else-if="item.status == 'Approved'"
                    class="col-sm-12 text-center py-sm-2 col-lg-3 pt-y-0 text-lg-start col-xl-2"
                >
                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
                            <!-- Note-Outline Icon -->
                            <img
                                src="../assets/icons/file-eye-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>

                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
                            <!-- Download Icon -->
                            <img src="../assets/icons/download.svg" alt="" width="24" height="24" />
                        </button>
                    </router-link>
                </div>
                <!-- Conditional Rending for Rejected -->
                <div
                    v-else-if="item.status == 'rejected'"
                    class="col-sm-12 text-center py-sm-2 col-lg-3 pt-y-0 text-lg-start col-xl-2"
                >
                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
                            <!-- File View Icon -->
                            <img
                                src="../assets/icons/file-eye-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>
                </div>
                <!-- Conditional Rending for Pending -->
                <div
                    v-else
                    class="col-sm-12 text-center py-sm-2 col-lg-3 pt-y-0 text-lg-start col-xl-2"
                >
                    <router-link to="/">
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

                    <button class="btn-bg-outline mx-2" @click="archive(item.applicationUuid)">
                        <!-- Delete Icon -->
                        <img
                            src="../assets/icons/delete-outline.svg"
                            alt=""
                            width="24"
                            height="24"
                        />
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

let filtered = false
const name = ref()
const data = ref([])
const toDo = ref()
const pending = ref()
const approved = ref()
const rejected = ref()

onMounted(async () => {
    // Retrieve userName by referencing LocalStorage
    const userId = localStorage.getItem('userID')
    name.value = await (await axios.get(`http://localhost:8080/api/users/${userId}`)).data.name

    // Retrieve all application
    const response = await axios.get('http://localhost:8080/api/applications')
    data.value = response.data

    // Display filter numberss
    data.value = data.value.filter((obj) => !obj.disabledStatus)
    toDo.value = data.value.filter((item) => item.status === 'InProgress' || 'NotStarted').length
    pending.value = data.value.filter((item) => item.status === 'Pending').length
    approved.value = data.value.filter((item) => item.status === 'Approved').length
    rejected.value = data.value.filter((item) => item.status === 'Rejected').length
})

function archive(aId) {
    axios
        .put(`http://localhost:8080/api/applications/archive/${aId}`)
        .then(function (response) {
            console.log(response)
        })
        .catch(function (error) {
            console.log(error)
        })
}

function getStatusColour(status) {
    if (status == 'NotStarted') {
        return 'text-info'
    } else if (status == 'InProgress') {
        return 'text-warning'
    } else if (status == 'Approved') {
        return 'text-success'
    } else if (status == 'Rejected') {
        return 'text-danger'
    } else {
        return 'text-orange'
    }
}

function getStatus(status) {
    if (status == 'NotStarted') {
        return 'Not Started'
    } else if (status == 'InProgress') {
        return 'In Progress'
    } else {
        return status
    }
}

const filterByStatus = async (param) => {
    filtered = true
    data.value = []
    if (param == 'ToDo') {
        try {
            const notStartedResponse = await axios.get(
                `http://localhost:8080/api/applications/status/NotStarted`
            )
            data.value = notStartedResponse.data

            const inProgressResponse = await axios.get(
                `http://localhost:8080/api/applications/status/inProgress`
            )
            data.value = data.value.concat(inProgressResponse.data)
        } catch (error) {
            console.error(error)
        }
    } else {
        try {
            const response = await axios.get(
                `http://localhost:8080/api/applications/status/${param}`
            )
            data.value = response.data
        } catch (error) {
            console.error(error)
        }
    }
}

async function getAllApplications() {
    filtered = false
    try {
        const response = await axios.get(`http://localhost:8080/api/applications`)
        data.value = response.data
    } catch (error) {
        console.error(error)
    }
}
</script>

<style></style>
