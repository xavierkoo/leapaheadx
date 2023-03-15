<template>
    <!-- This is the dashboard row -->
    <div class="row mx-5 pad-d">
        <div class="col-xl-3">
            <h4>My Dashboard</h4>
        </div>
        <div class="col" />
        <div class="col-xl-3 text-end">
            <!-- TODO - Change this to variable based on login -->
            <h5>Admin1</h5>
        </div>
    </div>

    <!-- This is the status filter buttons  -->
    <div class="row mx-5 pad-f">
        <div class="col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('NotStarted')">
                <h4>Not Started</h4>
                <h2 class="pad-c">{{ notStarted }}</h2>
            </div>
        </div>
        <div class="col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('inProgress')">
                <h4>In Progress</h4>
                <h2 class="pad-c">{{ inProgress }}</h2>
            </div>
        </div>
        <div class="col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('Approved')">
                <h4>Approved</h4>
                <h2 class="pad-c">{{ approved }}</h2>
            </div>
        </div>
        <div class="col-xl-3">
            <div class="button-like-div py-4" @click="filterByStatus('Rejected')">
                <h4>Rejected</h4>
                <h2 class="pad-c">{{ rejected }}</h2>
            </div>
        </div>
    </div>

    <!-- This is the Application Submission Table -->
    <div class="row mx-5 pad-d">
        <div class="col-xl-12 dark-container pb-5">
            <div class="row mx-5">
                <div class="col pad-e"><h5>Application Submissions</h5></div>
            </div>

            <!-- This is the Header Row -->
            <div class="row mx-5 pad-d">
                <div class="col-xl-7 tableHeader">Name</div>
                <div class="col-xl-2 tableHeader">Status</div>
                <div class="col-xl-1" />
                <div class="col-xl 2 tableHeader">Actions</div>
            </div>

            <!-- This is the For-loop of all the records-->
            <div v-for="item in data" :key="item.id" class="row tableRow mx-5 pad-e">
                <div class="col-xl-7">
                    <div class="row justify-content-center align-items-center">
                        <div class="col-12">{{ item.formName }} - {{ item.company }}</div>
                        <div class="col-12 tableCaption">{{ item.applicationUuid }}</div>
                    </div>
                </div>
                <div class="col-xl-2" :class="getStatusColour(item.status)">
                    {{ getStatus(item.status) }}
                </div>
                <div class="col-xl-1" />
                <!-- Conditional Rending for Not Started & In Progress -->
                <div
                    v-if="item.status == 'InProgress' || item.status == 'NotStarted'"
                    class="col-xl 2"
                >
                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
                            <!-- Download Icon -->
                            <img
                                src="../assets/icons/note-edit-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </router-link>

                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
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
                <!-- Conditional Rending for Approved -->
                <div v-else-if="item.status == 'Approved'" class="col-xl 2">
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
                <div v-else-if="item.status == 'rejected'" class="col-xl 2">
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
                <div v-else class="col-xl 2">
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

                    <router-link to="/">
                        <button class="btn-bg-primary mx-2">
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
const inProgress = ref()
const notStarted = ref()
const approved = ref()
const rejected = ref()

onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/applications')
    data.value = response.data

    inProgress.value = data.value.filter((item) => item.status === 'InProgress').length

    notStarted.value = data.value.filter((item) => item.status === 'NotStarted').length

    approved.value = data.value.filter((item) => item.status === 'Approved').length

    rejected.value = data.value.filter((item) => item.status === 'Rejected').length
})

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
    data.value = []
    try {
        const response = await axios.get(`http://localhost:8080/api/applications/status/${param}`)
        data.value = response.data
    } catch (error) {
        console.error(error)
    }
}
</script>

<style></style>
