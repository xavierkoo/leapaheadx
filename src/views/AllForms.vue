<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="row">
            <h3 class="col-6">My Dashboard</h3>
            <button
                type="button"
                class="col-2 me-5 btn btn-light"
                @click="() => (isAssign = !isAssign)"
            >
                Assign
            </button>
            <button type="button" class="col-2 btn btn-info" @click="createNewEntry">
                Create
            </button>


        </div>
        <div v-if="isAssign" class="overlay">
            <AssignWorkflow @is-close="() => (isAssign = !isAssign)" />
        </div>
        <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
            <h6>All Form/Workflows Templates</h6>
            <table class="table text-white" border="0">
                <thead>
                    <tr style="color: grey">
                        <th scope="col">Name</th>
                        <th scope="col">Date Created</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item, index in data" :key="item.id">
                        <th>
                            <span style="font-weight: 500">{{ item.name }}</span
                            ><br />
                            <span style="font-size: smaller; color: grey; font-weight: 400">{{
                                item.formUuid
                            }}</span>
                        </th>
                        <td class="align-middle">{{ item.dateCreated }}</td>

                        <td class="align-middle">
                            <button @click="editWorkflow(item.formUuid)" class="rounded bg-white me-4">
                                <img src="../assets/images/editIcon.png" height="25" />
                            </button>
                            <button class="rounded bg-white">
                                <img src="../assets/images/deleteIcon.png" height="25" @click="deleteWorkflow(item.formUuid, index )" />
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import axios from 'axios'
import { ref, onMounted } from 'vue'
import AssignWorkflow from './AssignWorkflow.vue'


const router = useRouter()

const data = ref([])
const isAssign = ref(false)
// const newEntryId = ref(null);

onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/formWorkflows')
    data.value = response.data
})

function editWorkflow(formUuid) {
  router.push({ name: 'editWorkflow', params: { formUuid: formUuid } })
}

async function createNewEntry() {


    router.push({ name: 'newWorkflow' })

}
function deleteWorkflow(formUuid, index){

    axios.get('http://localhost:8080/api/formSteps/byParentForm/'+formUuid)
    .then(response => {
        const fullFormdata = response.data
        console.log(fullFormdata[0])
        const deletePromises = [];
        for (let index = 0; index < fullFormdata[0].formSteps.length; index++) {
            const stepUuid =  fullFormdata[0].formSteps[index].stepUuid;

            const associatedSubform = fullFormdata[0].formSteps[index].associatedSubform;
            for (let index = 0; index < associatedSubform.length; index++) {
                const associatedSubformsId = associatedSubform[index].associatedSubformsId;
                deletePromises.push(axios.delete('http://localhost:8080/api/associatedSubform/'+associatedSubformsId));
            }
            deletePromises.push(axios.delete('http://localhost:8080/api/formSteps/'+stepUuid));
        }
        Promise.all(deletePromises)
            .then(() => {
                axios.delete('http://localhost:8080/api/formWorkflows/'+formUuid)
                    // eslint-disable-next-line no-unused-vars
                    .then(response => {
                        console.log('formUuid deleted successfully');
                        data.value.splice(index,1)
                    })
                    .catch(error => {
                        console.error('Error deleting formUuid', error);
                    });
            })
            .catch(error => {
                console.error('Error deleting associatedSubformsId or stepUuid', error);
            });
    })
    .catch(error => {
        console.error('errorgetting', error);
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
