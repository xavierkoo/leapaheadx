<template>
    <div class="container-fluid p-4 text-white" style="background-color: #142442">
        <h3>Form/Workflow Builder</h3>

        <div class="row">
            <div class="rounded-4 p-3 mt-4 col-6" style="background-color: #0f1726; ">
                <div class="row">
                    <div class="col-8">
                        <textarea  v-model="workflowname" class="form-control h-100" aria-label="With textarea" placeholder="Name a workflow..."></textarea>
                    </div>
                    <div class="col-4 d-flex flex-column">
                        <button type="button" class="btn btn-success mb-1" @click="save()">Save</button>
          
                        <button type="button" class="btn btn-light" @click="addStepcomponent()">Add Step</button>
                    </div>
                    
                    <div v-for="(step, index) in steps" :key="index" class="my-4">
                        <div class="d-flex justify-content-between">   
                            Step {{ step.orderNo}}
                            <select v-model="step.assigneeType">
                                <option value="admin">admin</option>
                                <option value="vendor">vendor</option>
                            </select>
                            <select v-model="step.action">
                                <option value="Fill Up">Fill Up</option>
                                <option value="Check and Fill Up">Check and Fill Up</option>
                                <option value="Check and Approve">Check and Approve</option>
                            </select>
         
                        </div>
                        <div class="col rounded p-3" style="background-color: #1A263C;" @dragover.prevent @drop="onDrop($event, index)">
                            <h5 class="text-secondary" v-if="step.droppedItems.length == 0">Start by dragging here</h5>
                            <div v-for="item in step.droppedItems" :key="item.canvasUuid" style="background-color: #707279;" class="col rounded py-3 m-2 text-center">
                                {{ item.name }}<br>
                                <button type="button" class="btn btn-danger" @click="step.droppedItems.splice(index, 1)">remove</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-1" >
            </div>

            <div class="rounded-4 p-5 mt-4 col-5" style="background-color: #0f1726;">
                <div class="row">
                    <h6 class="col">All Form components</h6>
                    <button type="button" class="btn btn-light col-2">Create</button>
                </div>
                <div  v-for="item in subformcanvasData" :key="item.canvasUuid" class="row mt-3 text-center" draggable="true" @dragstart="onDragStart(item.canvasUuid)">
                    <div class="col rounded py-3" style="background-color: #707279;">
                        {{ item.name }} <br>
                        <button type="button" class="btn btn-light col-2 mt-2" @click="addtostep(item.canvasUuid)"> Drag </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'
// import { useRoute } from 'vue-router';
const subformcanvasData = ref([])
const workflowname = ref("")
// const route = useRoute();
// const formUuid = route.params.formUuid
const steps = ref([]);

function onDragStart(canvasUuid) {
  event.dataTransfer.setData('text/plain', canvasUuid);
}

function onDrop(event, index) {
    event.preventDefault();
    const canvasUuid = event.dataTransfer.getData('text/plain');
    const droppedItem = subformcanvasData.value.find(item => item.canvasUuid.toString() === canvasUuid);
    // droppedItems.value.push(droppedItem);
    steps.value[index].droppedItems.push(droppedItem);
}

function addStepcomponent() {
    const newStep = { 
        // formUuid : `${formUuid}`, 
        assigneeType: "", 
        orderNo: `${steps.value.length + 1}`, 
        action:"",
        droppedItems:[], 
    };
    steps.value.push(newStep);
}



const save = async () => {
    const workflowdata = {
        "name": workflowname.value,
        "description": "this is the process of getting bto",
        "createdBy": "79ebaf36-bd58-11ed-afa1-0242ac120002"
    }
    try{
        const response = await axios.post('http://localhost:8080/api/formWorkflows', workflowdata);
        const formUuid = response.data;
        console.log("formUuid",formUuid)
        for (let index = 0; index < steps.value.length; index++) {
        const element = steps.value[index];

        console.log(element.assigneeType,element.orderNo,element.action,formUuid)
        const poststep = {
            "assigneeType": element.assigneeType,
            "orderNo": element.orderNo,
            "action": element.action,
            "parentForm": formUuid
        }
        try {
            // eslint-disable-next-line no-unused-vars
            const response = await axios.post(' http://localhost:8080/api/formSteps', poststep)
            const stepUuid = response.data;
            console.log("stepuuid", stepUuid)

            for (let index = 0; index < element.droppedItems.length; index++) {
                const item = element.droppedItems[index];
                console.log( item.canvasUuid)
                const associatedSubformData = {
                    "stepUuid": stepUuid,
                    "canvasUuid": item.canvasUuid,
                    "position": index+1,
                }
                try {
            // eslint-disable-next-line no-unused-vars
                    const response = await axios.post(' http://localhost:8080/api/associatedSubform', associatedSubformData)
                    const associatedSubformDataUuid = response.data;
                    console.log("associatedSubformData",associatedSubformDataUuid)
                } catch (error) {
                    console.log(error)
                    console.log("3 associatedSubformData failed")
                }    
            }
        } catch (error) {
            console.log(error)
            console.log("2 formstep failed")
        }
            
    }

    }catch (error){
        console.log("1 workflow failed")

    }



    // for (let index = 0; index < steps.value.length; index++) {
    //     const element = steps.value[index];
    //     console.log(element)
    //     const poststep = {
    //         "assigneeType": element.assigneeType,
    //         "orderNo": element.orderNo,
    //         "action": element.action,
    //         "parentForm": formUuid
    //     }
    //     try {
    //         // eslint-disable-next-line no-unused-vars
    //         const response = await axios.post(' http://localhost:8080/api/formSteps', poststep)
    //         const stepUuid = response.data;
    //         for (let index = 0; index < element.droppedItems; index++) {
    //             const element = element.droppedItems[index];
    //             const associatedSubformData = {
    //                 "stepUuid": stepUuid,
    //                 "canvasUuid": element.canvasUuid,
    //                 "position": index+1,
    //             }
    //             try {
    //         // eslint-disable-next-line no-unused-vars
    //                 const response = await axios.post(' http://localhost:8080/api/formSteps', associatedSubformData)
    //             } catch (error) {
    //                 console.log(error)
    //             }    
    //         }
    //     } catch (error) {
    //         console.log(error)
    //     }
            
    // }
    // console.log(searchQuery.value)
    // const postdata = {
    //     status: 'NotStarted',
    //     comment: 'this need to be done asap',
    //     company: vendor_searchQuery.value,
    //     formName: searchQuery.value,
    //     currentStepNo: 1
    // }
    // try {
    //     // eslint-disable-next-line no-unused-vars
    //     const response = await axios.post('http://localhost:8080/api/applications', postdata)
    //     message.value = 'Successful Assigned!'
    //     textColor.value = 'text-success'
    //     selectValue.value = ''
    //     searchQuery.value = ''
    //     uuid.value = ''
    //     vendor_selectValue.value = ''
    //     vendor_searchQuery.value = ''
    //     vendor_uuid.value = ''
    //     setTimeout(() => {
    //         showMessage.value = false
    //         message.value = ''
    //     }, 5000)
    // } catch (error) {
    //     message.value = 'Error with assignment!'
    //     textColor.value = 'text-danger'
    //     setTimeout(() => {
    //         showMessage.value = false
    //         message.value = ''
    //     }, 5000)
    // }
}

onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/subformcanvas')
    subformcanvasData.value = response.data
})
</script>

<style>

</style>


                <!-- <div class="col rounded py-3" style="background-color: #1A263C;" @dragover.prevent @drop="onDrop">
                    <h1>drop here</h1>
                        <div v-for="item in droppedItems" :key="item.canvasUuid">
                            {{ item.name }}
                    </div>
                </div> -->

       <!-- <div class="row">
            <div class="rounded-4 p-3 mt-4 col-6" style="background-color: #0f1726; ">
                <div class="row">
                    <div class="col-10">
                        <textarea  v-model="workflowname" class="form-control" aria-label="With textarea" placeholder="Name a workflow..."></textarea>
                    </div>
                    <div class="col-2 d-flex flex-column">
                        <button type="button" class="btn btn-light">Create</button>
                        <br>
                        <button type="button" class="btn btn-light" @click="addStepcomponent()">Add</button>
                    </div>



                    <div v-for="(step, index) in steps" :key="index">
                        <div>   
                            Step {{ step.orderNo}}
                            <select v-model="step.assigneeType">
                                <option value="admin">admin</option>
                                <option value="vendor">vendor</option>
                            </select>
                            <select v-model="step.action">
                                <option value="Fill Up">Fill Up</option>
                                <option value="Check and Fill Up">Check and Fill Up</option>
                                <option value="Check and Approve">Check and Approve</option>
                            </select>
         
                        </div>
                        <div class="col rounded py-3" style="background-color: #1A263C;">
                            <h1>hello</h1>
                        </div>
                        
                   
                    </div>
                </div>



                
            </div>
            
            <div class="col-1" >
                
            </div>

            <div class="rounded-4 p-5 mt-4 col-5" style="background-color: #0f1726;">
                <div class="row">
                    <h6 class="col">All Form components</h6>
                    <button type="button" class="btn btn-light col-2">Create</button>
                </div>

                <div  v-for="item in subformcanvasData" :key="item.canvasUuid" class="row mt-3 text-center " >
                    <div class="col rounded py-3" style="background-color: #707279;">
                        {{ item.name }} <br>
                        <button @click="addtostep(item.canvasUuid)" type="button" class="btn btn-light col-2 mt-2">Add</button>
                    </div>
                </div>
            </div>
        </div> -->
