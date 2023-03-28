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

    <div v-if="isAssign" class="overlay">
        <AssignWorkflow @is-close="() => (isAssign = !isAssign)" />
    </div>

    <div class="row mx-2 mx-sm-5 pad-d">
        <div class="col dark-container pb-5">
            <!-- This is the Title + Btns -->
            <div class="row mx-sm-2 mx-lg-5">
                <div class="col col-lg-5 pad-e"><h5>All Form/Workflow Templates</h5></div>
                <div class="col-lg-1 col-xl-2" />

                <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                    <!-- New Assignment -->
                    <button class="light-button" @click="() => (isAssign = !isAssign)">
                        Assign
                    </button>
                </div>

                <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                    <!-- New Workflow -->
                    <router-link to="/newWorkflow">
                        <button class="blue-button">Create</button>
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
                v-for="(item, index) in data"
                :key="item.id" 
                class="row tableRow justify-content-center align-items-center mx-sm-2 mx-lg-5 pad-e"
            >
                <div class="col-sm-8 col-lg-6">
                    <div class="row justify-content-center align-items-center">
                        <div class="col-12">{{ item.name }}</div>
                        <div class="col-12 tableCaption">{{ item.formUuid }}</div>
                    </div>
                </div>
                <div class="col-sm-4 col-lg-3">
                    <p>{{ item.dateCreated }}</p>
                </div>
                <div class="d-none d-xl-block col-xl-1" />

                <!-- Rendering of the action icons -->
                <div
                    class="pt-2 col-sm-12 text-center py-sm-2 col-lg-3 col-xl-2 pt-y-0 text-lg-start"
                >
                    <button class="btn-bg-primary mx-2" @click="editWorkflow(item.formUuid)">
                        <!-- File view Icon -->
                        <img
                            src="../assets/icons/note-edit-outline.svg"
                            alt=""
                            width="24"
                            height="24"
                        />
                    </button>

                    <button
                        class="btn-bg-outline mx-2"
                        @click="archiveWorkflow(item.formUuid, index)"
                    >
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
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ref, onMounted } from 'vue'
import AssignWorkflow from './AssignWorkflow.vue'

const router = useRouter()
const data = ref([])
const isAssign = ref(false)

// fetch all formWorkflow upon loading
onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/formWorkflows')
    data.value = response.data
    data.value = data.value.filter(obj => !obj.archive);
    console.log(data.value)

})
// edit button brings brings to NewWorkflow vue with id to edit
function editWorkflow(formUuid) {
    router.push({ name: 'editWorkflow', params: { formUuid: formUuid } })
}

// archive workflow button 
async function archiveWorkflow(formUuid, index) {
    const workflowdata = {
    name: data.value[index].name,
    description:data.value[index].description,
    createdBy: data.value[index].createdBy,
    archive: true
    }
    axios.put(`http://localhost:8080/api/formWorkflows/${formUuid}`, workflowdata)
    .then(response => {
        console.log(response.data);
        data.value.splice(data.value[index],1)
    })
    .catch(error => {
        console.error('Error archive workflow', error);
    });
}
</script>

<style>
table,
th,
td {
    border: none;
}
.overlay {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-color: rgba(255, 255, 255, 0.3); /* add tint as background */
    z-index: 1; /* make sure it's on top of other elements */
}
</style>
