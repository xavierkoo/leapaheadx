<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">My Dashboard</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div id="status_tracker" class="row gap-5 text-center">
            <div class="col rounded-4 p-3 button-like-div" @click="filterByStatus('NotStarted')">
                <h6>Not Started</h6>
                <h5>{{ notStarted }}</h5>
            </div>
            <div class="col rounded-4 p-3 button-like-div" @click="filterByStatus('InProgress')">
                <h6>In Progress</h6>
                <h5>{{ inProgress }}</h5>
            </div>
            <div class="col rounded-4 p-3 button-like-div" @click="filterByStatus('Approved')">
                <h6>Approved</h6>
                <h5>{{ approved }}</h5>
            </div>
            <div class="col rounded-4 p-3 button-like-div" @click="filterByStatus('Rejected')">
                <h6>Rejected</h6>
                <h5>{{ rejected }}</h5>
            </div>
        </div>

        <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
            <h6>Application Submission</h6>
            <table class="table text-white" border="0">
                <thead>
                    <tr style="color: grey">
                        <th scope="col">Name</th>
                        <th scope="col">Status</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in data" :key="item.id">
                        <th>
                            <span style="font-weight: 500">{{ item.formName }}</span
                            ><br />
                            <span style="font-size: smaller; color: grey; font-weight: 400">{{
                                item.company
                            }}</span>
                        </th>
                        <td class="align-middle" :class="getStatusColour(item.status)">
                            {{ getStatus(item.status) }}
                        </td>

                        <td v-if="item.status == 'Approved'" class="align-middle">
                            <button class="rounded bg-white me-4 border-none">
                                <img src="../assets/images/previewIcon.png" height="25" />
                            </button>
                            <button class="rounded bg-white">
                                <img src="../assets/images/downloadIcon.png" height="25" />
                            </button>
                        </td>
                        <td v-else-if="item.status == 'Rejected'" class="align-middle">
                            <button class="rounded bg-white me-4">
                                <img src="../assets/images/previewIcon.png" height="25" />
                            </button>
                        </td>
                        <td v-else-if="item.status == 'Pending'" class="align-middle">
                            <button class="rounded bg-white me-4">
                                <img src="../assets/images/previewIcon.png" height="25" />
                            </button>
                            <button class="rounded bg-white">
                                <img src="../assets/images/deleteIcon.png" height="25" />
                            </button>
                        </td>
                        <td v-else class="align-middle">
                            <button class="rounded bg-white me-4">
                                <img src="../assets/images/editIcon.png" height="25" />
                            </button>
                            <button class="rounded bg-white">
                                <img src="../assets/images/deleteIcon.png" height="25" />
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
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
        console.log(response.data)
    } catch (error) {
        console.error(error)
    }
}
</script>

<style>
table,
th,
td {
    border: none;
}
</style>
