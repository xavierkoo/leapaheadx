<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">My Dashboard</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
            <div class="sub-header-container">
                <h5>All Sub-Form Canvas</h5>
                <button class="blue-button">New</button>
            </div>
            <table class="table text-white" border="0">
                <thead>
                    <tr style="color: grey">
                        <th scope="col">Name</th>
                        <th scope="col">Date Created</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in data" :key="item.canvasUuid">
                        <th>
                            <span style="font-weight: 500">{{ item.name }}</span
                            ><br />
                            <span style="font-size: smaller; color: grey; font-weight: 400">{{
                                item.canvasUuid
                            }}</span>
                        </th>
                        <td class="align-middle">{{ item.dateCreated }}</td>
                        <td class="align-middle">
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

onMounted(async () => {
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
