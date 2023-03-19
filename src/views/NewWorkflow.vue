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
                    <div v-for="(step, index) in steps" :key="index" class="my-3">
                        <div class="d-flex justify-content-between">   
                            Step {{ index + 1}}
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
                        <div class="col rounded p-3" style="background-color: #1A263C;" @dragover.prevent @drop="onDrop($event, index)" >
                            <h5 v-if="step.droppedItems.length == 0" class="text-secondary" >Start by dragging here</h5>
                            <div v-for="item, itemIndex in step.droppedItems" :key="item.canvasUuid" class="col rounded py-3 m-2 text-center row" :style="{ backgroundColor: item.flash == itemIndex+item.name+index  ? '#307279' : '#707279' }">
                                <div class='col-10 ps-5'>
                              
                                {{ item.name }}<br>
                                <button type="button" class="btn btn-danger" @click="removeDroppedItem(index,itemIndex)">remove</button>
                                </div>
                                <div class='col-2 '>
                                    <button v-if ="itemIndex!=0" type="button" class="btn btn-light mb-1" @click="moveStepUp(index,itemIndex)">▲</button>
                                    <div  v-if ="itemIndex==0" class="p-3"></div>
                                    <button v-if ="itemIndex!= step.droppedItems.length-1" type="button" class="btn btn-light" @click="moveStepDown(index,itemIndex)">▼</button>
                                </div>
                            </div>
                            <button type="button" class="btn btn-danger" @click="removeStep(index)">Remove step</button>
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
import { useRoute , useRouter } from 'vue-router';
const subformcanvasData = ref([])
const workflowname = ref("")
const route = useRoute();
const formUuid = route.params.formUuid
const steps = ref([]);
const router = useRouter()

async function loadFormData(formUuid) {
  try {
    const response = await axios.get('http://localhost:8080/api/formSteps/byParentForm/'+formUuid);
    const oldFormData = response.data;
    if (!oldFormData || !oldFormData[0]) {
      console.error('Error: no data found');
      return;
    }
    
    workflowname.value = oldFormData[0].workflowName
        for (const step of oldFormData[0].formSteps) {
            const droppedItems = ref([]) // later add to steps.value
            // console.log(step.associatedSubform)
            for (const associatedSubform of step.associatedSubform) {
                // (associatedSubform.name)
                const subformcanvasDatatemp = subformcanvasData.value;
                for (const subCanData of subformcanvasDatatemp) {
                    if (subCanData.canvasUuid ==associatedSubform.name){
                        droppedItems.value.push(subCanData)
                    }
                }
            }
            steps.value.push({
                assigneeType: step.assigneeType, 
                orderNo: step.orderNo, 
                action: step.action,
                droppedItems:droppedItems, 
            })
        }
  } catch (error) {
    console.error('Error loading form data', error);
  }
}

if (formUuid) {
  loadFormData(formUuid);
}


// end of editing

function onDragStart(canvasUuid) {
  event.dataTransfer.setData('text/plain', canvasUuid);
}

function moveStepUp(stepIndex, itemIndex) {
  if (itemIndex > 0) {

    const temp =  steps.value[stepIndex].droppedItems[itemIndex];
    steps.value[stepIndex].droppedItems[itemIndex] = steps.value[stepIndex].droppedItems[itemIndex-1];
    steps.value[stepIndex].droppedItems[itemIndex-1] = temp;

    steps.value[stepIndex].droppedItems[itemIndex-1].flash = itemIndex-1+steps.value[stepIndex].droppedItems[itemIndex-1].name+stepIndex;
        setTimeout(() => {
            steps.value[stepIndex].droppedItems[itemIndex-1].flash =  false;
        }, 500);

  }
}

function moveStepDown(stepIndex, itemIndex) {
  if (itemIndex < steps.value[stepIndex].droppedItems.length - 1) {
    const temp = steps.value[stepIndex].droppedItems[itemIndex];
    steps.value[stepIndex].droppedItems[itemIndex] = steps.value[stepIndex].droppedItems[itemIndex+1];
    steps.value[stepIndex].droppedItems[itemIndex+1] = temp;
    steps.value[stepIndex].droppedItems[itemIndex+1].flash = itemIndex+1+steps.value[stepIndex].droppedItems[itemIndex+1].name+stepIndex;
        setTimeout(() => {
            steps.value[stepIndex].droppedItems[itemIndex+1].flash =  false;
        }, 500);

  }
}

function removeStep(stepIndex) {
    steps.value.splice(stepIndex, 1);
}

function removeDroppedItem(stepIndex, itemIndex) {
    steps.value[stepIndex].droppedItems.splice(itemIndex, 1);
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
        orderNo: "", 
        action:"",
        droppedItems:[], 
    };
    steps.value.push(newStep);
}

function save() {
  let isValid = true;
  var message = '';

  // Check if workflowname is not empty
  if (workflowname.value.trim() === "") {
    message = "Workflow name is required";
    isValid = false;
  } else {
    // Check if each step has valid assigneeType and action values,
    // and if droppedItems array is not empty
    if (steps.value.length == 0){
        isValid = false;
        message = "Workflow required to have atleast one step";

    }
    steps.value.forEach((step) => {
        if (step.assigneeType === "" || step.action === "" || step.droppedItems.length === 0) {
        isValid = false;
        message = "Please fill all required fields and add at least one form component to each step";
        }
    });


  }
  if (isValid) {
        // Perform save operation
        if (formUuid == null){
            saving()
        }else{
            deleting()
        }
    } else {
        // Show validation error message
        alert(message);
    }


}
const deleting = async () => {
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
                        saving();
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


const saving = async () => {
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
            "orderNo": index+1,
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
    alert("Sucessfully added workflow");
    router.push({ name: 'allforms' })
    }catch (error){
        alert("Workflow name is taken")

    }
}
onMounted(async () => {
    const response = await axios.get('http://localhost:8080/api/subformcanvas')
    subformcanvasData.value = response.data

})
</script>

<style>


</style>


         