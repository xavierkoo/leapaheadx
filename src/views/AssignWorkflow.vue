<template>
    <div class="my-overlay p-4 mt-4">
        <!-- Header Row -->
        <div class="row mx-2 mx-sm-5 pad-d">
            <div class="col-9 col-sm-6 col-xl-5"><h5>Workflow Assignment</h5></div>
            <div class="d-none d-sm-block col-sm-2 col-xl-4" />
            <div class="d-none d-sm-block col-sm-4 col-xl-2">
                <button class="light-button" @click="closeForm">Cancel</button>
            </div>
            <div class="d-block d-sm-none col-3">
                <button class="light-button" @click="closeForm">
                    <img src="../assets/icons/close.svg" width="24" height="24" alt="" />
                </button>
            </div>
            <div class="col-xl-1" />
        </div>

        <div class="row mx-2 mx-sm-5 pad-d">
            <!-- Select Workflow to be assigned -->
            <div class="col-xl-6 pad-d">
                <h6>Select a Workflow to be assigned</h6>
                <div class="input-group my-4">
                    <input
                        v-model="uuid"
                        class="form-control"
                        type="text"
                        placeholder="Search by Workflow ID"
                        @keyup="checktext_workflowId()"
                    />
                </div>
                <div class="input-group">
                    <input
                        v-model="searchQuery"
                        class="form-control"
                        type="text"
                        placeholder="Search by Workflow Name"
                        @keyup="checktext_workflowName()"
                    />
                    <button class="btn btn-light p-1" @click="displayform = !displayform">
                        <img src="../assets/icons/menu-down.svg" height="24" />
                    </button>
                </div>
                <ul
                    v-if="
                        displayform ||
                        (filteredItems.length != 0 &&
                            searchQuery != '' &&
                            searchQuery != selectValue)
                    "
                    class="bg-white text-dark no-bullets rounded-bottom dropdown"
                >
                    <li v-for="item in filteredItems" :key="item.id" @click="selectItem(item)">
                        {{ item.name }}
                    </li>
                </ul>
            </div>

            <!-- Select Vendor to be assigned -->
            <div class="col-xl-6 pad-d">
                <h6>Select a Vendor to be assigned</h6>
                <div>
                    <div class="input-group my-4">
                        <input
                            v-model="vendor_uuid"
                            class="form-control"
                            type="text"
                            placeholder="Search by Vendor ID"
                            @keyup="checktext_vendorId()"
                        />
                    </div>
                    <div class="input-group">
                        <input
                            v-model="vendor_searchQuery"
                            class="form-control"
                            type="text"
                            placeholder="Search by Vendor Name"
                            @keyup="checktext_vendorName()"
                        />
                        <button
                            class="btn btn-light p-1"
                            @click="vendor_displayform = !vendor_displayform"
                        >
                            <img src="../assets/icons/menu-down.svg" height="24" />
                        </button>
                    </div>
                    <ul
                        v-if="
                            vendor_displayform ||
                            (vendor_filteredItems.length != 0 &&
                                vendor_searchQuery != '' &&
                                vendor_searchQuery != vendor_selectValue)
                        "
                        class="bg-white text-dark no-bullets rounded-bottom dropdown"
                    >
                        <li
                            v-for="item in vendor_filteredItems"
                            :key="item.id"
                            @click="vendor_selectItem(item)"
                        >
                            {{ item.company }}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row text-center pad-e">
            <div class="col-12">
                <p v-if="showMessage" :class="textColor" class="text-center mt-2">
                    {{ message }}
                </p>
                <button
                    type=" button"
                    class="btn"
                    :class="!canAssign ? '' : 'btn-success'"
                    :disabled="!canAssign"
                    @click="assignFormVendor"
                >
                    {{ assignBtnVal }}
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineEmits, computed } from 'vue'

import axios from 'axios'
import { ref, onMounted } from 'vue'
const emits = defineEmits(['isClose'])

function closeForm() {
    emits('isClose')
}

const uuid = ref('')
const selectValue = ref('')
const searchQuery = ref('')
const displayform = ref(false)
const message = ref('message')
const showMessage = ref(false)
const textColor = ref('')
const formData = ref([])
const vendorData = ref([])
const assignBtnVal = ref('Please assign a form to a vendor')

onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/formWorkflows')
    formData.value = response.data

    const responsevendor = await axios.get('http://localhost:8080/api/vendors')
    vendorData.value = responsevendor.data
})

const canAssign = computed(() => {
    if (
        vendor_searchQuery.value != '' &&
        vendor_uuid.value != '' &&
        uuid.value != '' &&
        searchQuery.value != ''
    ) {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        assignBtnVal.value = 'Assign'
        return true
    } else {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        assignBtnVal.value = 'Please assign a form to a vendor'
        return false
    }
})

const assignFormVendor = async () => {
    console.log(searchQuery.value)
    const postdata = {
        status: 'NotStarted',
        comment: 'this need to be done asap',
        company: vendor_searchQuery.value,
        formName: searchQuery.value,
        currentStepNo: 1
    }
    try {
        // eslint-disable-next-line no-unused-vars
        const response = await axios.post('http://localhost:8080/api/applications', postdata)
        showMessage.value = true
        message.value = 'Successful Assigned!'
        textColor.value = 'text-success'
        selectValue.value = ''
        searchQuery.value = ''
        uuid.value = ''
        vendor_selectValue.value = ''
        vendor_searchQuery.value = ''
        vendor_uuid.value = ''
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    } catch (error) {
        message.value = 'Error with assignment!'
        textColor.value = 'text-danger'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    }
}

// Form --------------------------------------------------------------------------------------------------------------

function checktext_workflowId() {
    if (searchQuery.value != '' && uuid.value != '') {
        searchQuery.value = ''
    }
    if (checkformid(uuid.value) && uuid.value != '') {
        selectValue.value = ''
        searchQuery.value = ''
        showMessage.value = true
        message.value = 'Invalid UUID'
        textColor.value = 'text-danger'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    }
}

function checktext_workflowName() {
    if (searchQuery.value != selectValue.value) {
        uuid.value = ''
        selectValue.value = ''
    }
    if (!searchQuery.value == '' && filteredItems.value.length == 0) {
        showMessage.value = true
        message.value = 'Invalid Workflow'
        textColor.value = 'text-danger'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
        return true
    }
}

function selectItem(item) {
    searchQuery.value = item.name
    selectValue.value = item.name
    uuid.value = item.formUuid
    displayform.value = false
    showMessage.value = true
    message.value = 'Valid Form Name'
    textColor.value = 'text-success'
    setTimeout(() => {
        showMessage.value = false
        message.value = ''
    }, 5000)
}

function checkformid(inuuid) {
    const form = formData.value.find((form) => form.formUuid === inuuid)
    if (form) {
        searchQuery.value = form.name
        uuid.value = form.formUuid
        selectValue.value = form.name
        showMessage.value = true
        message.value = 'Valid UUID'
        textColor.value = 'text-success'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    } else {
        return true
    }
}

const filteredItems = computed(() => {
    if (!searchQuery.value) {
        return formData.value
    }

    return formData.value.filter((item) => {
        return item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    })
})
// End Form --------------------------------------------------------------------------------------------------------------

// Vendor ------------------------------------------------------------------------------------------------------------------------
const vendor_uuid = ref('')
const vendor_selectValue = ref('')
const vendor_searchQuery = ref('')
const vendor_displayform = ref(false)

function checktext_vendorId() {
    if (vendor_searchQuery.value != '' && vendor_uuid.value != '') {
        vendor_selectValue.value = ''
    }
    if (vendor_checkformid(vendor_uuid.value) && vendor_uuid.value != '') {
        vendor_selectValue.value = ''
        vendor_searchQuery.value = ''
        showMessage.value = true
        message.value = 'Invalid Vendor UUID'
        textColor.value = 'text-danger'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    }
}

function checktext_vendorName() {
    if (vendor_searchQuery.value != vendor_selectValue.value) {
        vendor_uuid.value = ''
        vendor_selectValue.value = ''
    }
    if (!vendor_searchQuery.value == '' && vendor_filteredItems.value.length == 0) {
        showMessage.value = true
        message.value = 'Invalid Vendor Name'
        textColor.value = 'text-danger'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
        return true
    }
}

function vendor_selectItem(item) {
    vendor_searchQuery.value = item.company
    vendor_selectValue.value = item.company
    vendor_uuid.value = item.vendorUuid
    vendor_displayform.value = false
    showMessage.value = true
    message.value = 'Valid Vendor Name'
    textColor.value = 'text-success'
    setTimeout(() => {
        showMessage.value = false
        message.value = ''
    }, 5000)
}

function vendor_checkformid(inuuid) {
    const form = vendorData.value.find((form) => form.vendorUuid === inuuid)
    if (form) {
        vendor_searchQuery.value = form.company
        vendor_uuid.value = form.vendorUuid
        vendor_selectValue.value = form.company
        showMessage.value = true
        message.value = 'Valid UUID'
        textColor.value = 'text-success'
        setTimeout(() => {
            showMessage.value = false
            message.value = ''
        }, 5000)
    } else {
        return true
    }
}

const vendor_filteredItems = computed(() => {
    if (!vendor_searchQuery.value) {
        return vendorData.value
    }

    return vendorData.value.filter((item) => {
        return item.company.toLowerCase().includes(vendor_searchQuery.value.toLowerCase())
    })
})

// End Vendor ----------------------------------------------------------------------------------------------------------------
</script>

<style>
ul.no-bullets {
    margin: 0;
    padding-top: 10px;
    padding-left: 10px;
    padding-bottom: 10px;
    left: 0;
    list-style: none;
}
.dropdown {
    height: auto !important;
    overflow-y: scroll !important;
}
.messageStatus {
    position: fixed;
    bottom: 10%;
    left: 0;
    max-width: 100%;
    z-index: -1;
}
</style>
