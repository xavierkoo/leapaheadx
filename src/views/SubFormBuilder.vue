<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">Sub Form Builder</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div class="row">
            <div class="col-md-8">
                <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
                    <div class="row align-items-center">
                        <div class="col-md-9 col-lg-8">
                            <input 
                                v-model="subFormName" 
                                class="custom-input" 
                                placeholder="ENTER SUB-FORM TITLE" 
                                style="height: 60px; width: 400px;"
                            />
                        </div>
                        <div class="col-md-3 col-lg-2">
                        </div>
                        <div class="col-md-3 col-lg-2 text-end">
                            <button class="save-btn btn btn-bg-primary w-100" style="font-size: 25px;">
                                Save
                            </button>
                        </div>
                    </div>
                    <div class="rounded-4 p-5 mt-4" style="background-color: #1A263C;">

                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
                    <h4 class="mb-5">Form Field Components</h4>
                    <div
                        v-for="(fieldType, index) in allFormFieldTypes"
                        :key="index"
                        :draggable="true"
                        class="form-field-box text-center rounded-3 mb-4 p-3"
                        @dragstart="dragStartHandler"
                    >
                        {{ fieldType }} Field Component
                        <br />
                        <span style="color: #5EBBE9;">Drag & Drop</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

const data = ref([])
const subFormName = ref('')
const allFormFieldTypes = ref([
    'Text Only',
    'Numbers Only',
    'Email', 
    'Phone Number', 
    'Address', 
    'Date Picker', 
    'Time Picker', 
    'Drop-Down Menu', 
    'Radio Button', 
    'Check Box', 
    'File Upload', 
    'Signature',
])

onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/subformcanvas')
    data.value = response.data
})

const dragStartHandler = (event) => {
    event.dataTransfer.setData('text/plain', event.target.id)
    event.dataTransfer.effectAllowed = 'move'
}
</script>

<style>
table,
th,
td {
    border: none;
}
</style>
