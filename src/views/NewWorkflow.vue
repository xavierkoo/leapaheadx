<template>
    <!-- This is the dashboard header -->
    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col-12 col-sm-8 col-lg-5 col-xl-5">
            <h4>Form/Workfow Builder</h4>
        </div>
        <div class="d-none d-lg-block col-lg col-xl" />
        <div class="col text-start col-sm-4 col-lg-5 col-xl-3 text-sm-end">
            <!-- TODO - Change this to variable based on login -->
            <h5>Admin1</h5>
        </div>
    </div>

    <div class="row mx-2 mx-sm-5 pad-d">
        <!-- LEFT - Workflow Creator -->
        <div class="col-xl-7 dark-container pb-5">
            <!-- Header -->
            <div class="row mx-2 pad-d justify-content-center align-items-center">
                <!-- Placeholder Name -->
                <div class="col-8">
                    <label for="workflowName">Workflow Name</label>
                    <input
                        id="workflowName"
                        v-model="workflowname"
                        type="text"
                        class="form-control"
                        placeholder="Name a workflow..."
                    />
                </div>

                <div class="col-4 d-flex flex-column">
                    <button type="button" class="blue-button" @click="save()">Save</button>

                    <button type="button" class="light-button mt-3" @click="addStepcomponent()">
                        Add Step
                    </button>
                </div>
            </div>

            <!-- All the Steps -->
            <div class="row">
                <!-- Loop through Steps -->
                <div v-for="(step, index) in steps" :key="index" class="my-4 pad-c">
                    <!-- Each Step Header -->
                    <div class="row mx-2 pad-d">
                        <div class="col">
                            <h6>Step {{ index + 1 }}</h6>
                        </div>
                        <div class="col caption">Assigned</div>
                        <div class="col">
                            <select v-model="step.assigneeType" class="px-2">
                                <option value="admin">Admin</option>
                                <option value="vendor">Vendor</option>
                            </select>
                        </div>
                        <div class="col caption">to</div>
                        <div class="col">
                            <select v-model="step.action" class="px-2">
                                <option value="Fill Up">Fill Up</option>
                                <option value="Check and Fill Up">Check and Fill Up</option>
                                <option value="Check and Approve">Check and Approve</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mx-1">
                        <div
                            style="background-color: #1a263c"
                            class="col rounded py-4 px-4"
                            @dragover.prevent
                            @drop="onDrop($event, index)"
                        >
                            <h5 v-if="step.droppedItems.length == 0" class="text-secondary">
                                Start by dragging here
                            </h5>
                            <div
                                v-for="(item, itemIndex) in step.droppedItems"
                                :key="item.canvasUuid"
                                class="col rounded py-3 m-2 text-center row glass-component"
                                :style="{
                                    backgroundColor:
                                        item.flash == itemIndex + item.name + index
                                            ? '#5EBBE9'
                                            : '#ffffff80'
                                }"
                            >
                                <div class="col-4 col-xl-2">
                                    <button
                                        type="button"
                                        class="danger-button mx-2"
                                        @click="removeDroppedItem(index, itemIndex)"
                                    >
                                        <img
                                            src="../assets/icons/close.svg"
                                            alt=""
                                            height="24"
                                            width="24"
                                        />
                                    </button>
                                </div>
                                <div class="col-xl-7 pt-2">
                                    <h6 class="">{{ item.name }}</h6>
                                </div>

                                <div class="col-12 col-xl-3">
                                    <button
                                        v-if="itemIndex != 0"
                                        type="button"
                                        class="btn btn-light mx-2"
                                        @click="moveStepUp(index, itemIndex)"
                                    >
                                        ▲
                                    </button>
                                    <div v-if="itemIndex == 0" class=""></div>
                                    <button
                                        v-if="itemIndex != step.droppedItems.length - 1"
                                        type="button"
                                        class="btn btn-light"
                                        @click="moveStepDown(index, itemIndex)"
                                    >
                                        ▼
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row g-2">
                            <div class="col">
                                <button
                                    type="button"
                                    class="btn btn-danger"
                                    @click="removeStep(index)"
                                >
                                    Remove step
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col" />

        <!-- RIGHT - All Form Component Canvas -->
        <div class="col-xl-4 dark-container pb-5 mt-5 mt-xl-0">
            <div class="row mx-2 pad-d">
                <h6 class="col-6 col-xl">All Form components</h6>
                <button type="button" class="btn btn-light col-6 col-end col-xl-3">Create</button>
            </div>
            <div
                v-for="item in subformcanvasData"
                :key="item.canvasUuid"
                class="row mx-2 pad-d text-center"
                draggable="true"
                @dragstart="onDragStart(item.canvasUuid)"
            >
                <div class="col rounded py-3 glass-component">
                    {{ item.name }} <br />
                    <button
                        type="button"
                        class="btn btn-light col-12 mt-2"
                        @click="addtostep(item.canvasUuid)"
                    >
                        Drag
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const subformcanvasData = ref([])
const workflowname = ref('')
const route = useRoute()
const formUuid = route.params.formUuid
const steps = ref([])
const router = useRouter()

async function loadFormData(formUuid) {
    try {
        const response = await axios.get(
            'http://localhost:8080/api/formSteps/byParentForm/' + formUuid
        )
        const oldFormData = response.data
        if (!oldFormData || !oldFormData[0]) {
            console.error('Error: no data found')
            return
        }

        workflowname.value = oldFormData[0].workflowName
        for (const step of oldFormData[0].formSteps) {
            const droppedItems = ref([]) // later add to steps.value
            // console.log(step.associatedSubform)
            for (const associatedSubform of step.associatedSubform) {
                // (associatedSubform.name)
                const subformcanvasDatatemp = subformcanvasData.value
                for (const subCanData of subformcanvasDatatemp) {
                    if (subCanData.canvasUuid == associatedSubform.name) {
                        droppedItems.value.push(subCanData)
                    }
                }
            }
            steps.value.push({
                assigneeType: step.assigneeType,
                orderNo: step.orderNo,
                action: step.action,
                droppedItems: droppedItems
            })
        }
    } catch (error) {
        console.error('Error loading form data', error)
    }
}

if (formUuid) {
    loadFormData(formUuid)
}

// end of editing

function onDragStart(canvasUuid) {
    event.dataTransfer.setData('text/plain', canvasUuid)
}

function moveStepUp(stepIndex, itemIndex) {
    if (itemIndex > 0) {
        const temp = steps.value[stepIndex].droppedItems[itemIndex]
        steps.value[stepIndex].droppedItems[itemIndex] =
            steps.value[stepIndex].droppedItems[itemIndex - 1]
        steps.value[stepIndex].droppedItems[itemIndex - 1] = temp

        steps.value[stepIndex].droppedItems[itemIndex - 1].flash =
            itemIndex - 1 + steps.value[stepIndex].droppedItems[itemIndex - 1].name + stepIndex
        setTimeout(() => {
            steps.value[stepIndex].droppedItems[itemIndex - 1].flash = false
        }, 500)
    }
}

function moveStepDown(stepIndex, itemIndex) {
    if (itemIndex < steps.value[stepIndex].droppedItems.length - 1) {
        const temp = steps.value[stepIndex].droppedItems[itemIndex]
        steps.value[stepIndex].droppedItems[itemIndex] =
            steps.value[stepIndex].droppedItems[itemIndex + 1]
        steps.value[stepIndex].droppedItems[itemIndex + 1] = temp
        steps.value[stepIndex].droppedItems[itemIndex + 1].flash =
            itemIndex + 1 + steps.value[stepIndex].droppedItems[itemIndex + 1].name + stepIndex
        setTimeout(() => {
            steps.value[stepIndex].droppedItems[itemIndex + 1].flash = false
        }, 500)
    }
}

function removeStep(stepIndex) {
    steps.value.splice(stepIndex, 1)
}

function removeDroppedItem(stepIndex, itemIndex) {
    steps.value[stepIndex].droppedItems.splice(itemIndex, 1)
}

function onDrop(event, index) {
    event.preventDefault()
    const canvasUuid = event.dataTransfer.getData('text/plain')
    const droppedItem = subformcanvasData.value.find(
        (item) => item.canvasUuid.toString() === canvasUuid
    )
    // droppedItems.value.push(droppedItem);
    steps.value[index].droppedItems.push(droppedItem)
}

function addStepcomponent() {
    const newStep = {
        // formUuid : `${formUuid}`,
        assigneeType: '',
        orderNo: '',
        action: '',
        droppedItems: []
    }
    steps.value.push(newStep)
}

function save() {
    let isValid = true
    var message = ''

    // Check if workflowname is not empty
    if (workflowname.value.trim() === '') {
        message = 'Workflow name is required'
        isValid = false
    } else {
        // Check if each step has valid assigneeType and action values,
        // and if droppedItems array is not empty
        if (steps.value.length == 0) {
            isValid = false
            message = 'Workflow required to have atleast one step'
        }
        steps.value.forEach((step) => {
            if (step.assigneeType === '' || step.action === '' || step.droppedItems.length === 0) {
                isValid = false
                message =
                    'Please fill all required fields and add at least one form component to each step'
            }
        })
    }
    if (isValid) {
        // Perform save operation
        if (formUuid == null) {
            saving()
        } else {
            deleting()
        }
    } else {
        // Show validation error message
        alert(message)
    }
}
const deleting = async () => {
    axios
        .get('http://localhost:8080/api/formSteps/byParentForm/' + formUuid)
        .then((response) => {
            const fullFormdata = response.data
            console.log(fullFormdata[0])
            const deletePromises = []
            for (let index = 0; index < fullFormdata[0].formSteps.length; index++) {
                const stepUuid = fullFormdata[0].formSteps[index].stepUuid

                const associatedSubform = fullFormdata[0].formSteps[index].associatedSubform
                for (let index = 0; index < associatedSubform.length; index++) {
                    const associatedSubformsId = associatedSubform[index].associatedSubformsId
                    deletePromises.push(
                        axios.delete(
                            'http://localhost:8080/api/associatedSubform/' + associatedSubformsId
                        )
                    )
                }
                deletePromises.push(axios.delete('http://localhost:8080/api/formSteps/' + stepUuid))
            }
            Promise.all(deletePromises)
                .then(() => {
                    axios
                        .delete('http://localhost:8080/api/formWorkflows/' + formUuid)
                        // eslint-disable-next-line no-unused-vars
                        .then((response) => {
                            console.log('formUuid deleted successfully')
                            saving()
                        })
                        .catch((error) => {
                            console.error('Error deleting formUuid', error)
                        })
                })
                .catch((error) => {
                    console.error('Error deleting associatedSubformsId or stepUuid', error)
                })
        })
        .catch((error) => {
            console.error('errorgetting', error)
        })
}

const saving = async () => {
    const workflowdata = {
        name: workflowname.value,
        description: 'this is the process of getting bto',
        createdBy: '79ebaf36-bd58-11ed-afa1-0242ac120002'
    }
    try {
        const response = await axios.post('http://localhost:8080/api/formWorkflows', workflowdata)
        const formUuid = response.data
        console.log('formUuid', formUuid)
        for (let index = 0; index < steps.value.length; index++) {
            const element = steps.value[index]

            console.log(element.assigneeType, element.orderNo, element.action, formUuid)
            const poststep = {
                assigneeType: element.assigneeType,
                orderNo: index + 1,
                action: element.action,
                parentForm: formUuid
            }
            try {
                // eslint-disable-next-line no-unused-vars
                const response = await axios.post(' http://localhost:8080/api/formSteps', poststep)
                const stepUuid = response.data
                console.log('stepuuid', stepUuid)

                for (let index = 0; index < element.droppedItems.length; index++) {
                    const item = element.droppedItems[index]
                    console.log(item.canvasUuid)
                    const associatedSubformData = {
                        stepUuid: stepUuid,
                        canvasUuid: item.canvasUuid,
                        position: index + 1
                    }
                    try {
                        // eslint-disable-next-line no-unused-vars
                        const response = await axios.post(
                            ' http://localhost:8080/api/associatedSubform',
                            associatedSubformData
                        )
                        const associatedSubformDataUuid = response.data
                        console.log('associatedSubformData', associatedSubformDataUuid)
                    } catch (error) {
                        console.log(error)
                        console.log('3 associatedSubformData failed')
                    }
                }
            } catch (error) {
                console.log(error)
                console.log('2 formstep failed')
            }
        }
        alert('Sucessfully added workflow')
        router.push({ name: 'allforms' })
    } catch (error) {
        alert('Workflow name is taken')
    }
}
onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/subformcanvas')
    subformcanvasData.value = response.data
})
</script>

<style></style>
