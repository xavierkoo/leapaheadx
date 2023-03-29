<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">Sub Form Viewer</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div class="row">
            <div id="left-palette">
                <div class="rounded-3 p-5 mt-4" style="background-color: #0f1726">
                    <div class="row align-items-center">
                        <div class="col-md-9 col-lg-8">
                            <h4>{{ subFormName }}</h4>
                        </div>
                        <div class="col-md-9 col-lg-8">
                            <p class="my-3 pb-3">{{ formDesc }}</p>
                        </div>
                        <div class="col-md-3 col-lg-2">
                        </div>
                    </div>
                    <div 
                        class="rounded-3 p-3 form-container" 
                        style="background-color: #1A263C;" 
                    >
                        <div v-for="(inputComponent, index) in formComponents" :key="index">
                            <div class="form-component my-5" style="border-radius: 5px; padding: 10px; margin-top: 25px; margin-bottom: 25px; background-color: #EEEEEA;">
                                <label class="mb-2 text-dark"><strong>{{ inputComponent.type }}</strong></label>
                                <br/>
                                <label class="text-dark">Question:</label>
                                <input v-model="inputComponent.question" class="form-control" type="text" placeholder="Question" name="question" style="height: 50px;" disabled/>
                                <div v-if="['Drop-Down Menu', 'Check Box', 'Radio Button'].includes(inputComponent.type)">
                                    <div class="additional-inputs-container row">
                                        <div v-for="(option, optionIndex) in inputComponent.options.sort()" :key="optionIndex" class=" col-6 additional-input my-2" >
                                            <label class="text-dark">Option {{ optionIndex + 1 }}</label>
                                            <input v-model="option.split(',')[0]" class="form-control" type="text"  placeholder="Option" disabled/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-check mt-2">
                                    <input  v-model="inputComponent.required"  class="form-check-input" type="checkbox" name="required" disabled/>
                                    <label class="text-dark" for="required-checkbox">Required</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute()
const canvasUuid = route.params.canvasUuid
const data = ref([]);
const subFormName = ref("");
const formDesc = ref("");
const formComponents = ref([]);

const getSubForm = async () => {
    try {
        const response = await axios.get(
            'http://localhost:8080/api/subformcanvas/' + canvasUuid
        );
        data.value = response.data;
        subFormName.value = data.value.name;
        formDesc.value = data.value.description;
    } catch (error) {
        console.log(error);
    }
}

if (canvasUuid) {
    getSubForm();
}

const getInputComponent = async () => {
    try {
        const response = await axios.get(
            'http://localhost:8080/api/inputcomponents/parent-canvas/' + canvasUuid
        );
        data.value = response.data;
        for (let i = 0; i < data.value.length; i++) {
            const inputComponent = {
                question: data.value[i].question,
                type: data.value[i].type,
                required: data.value[i].isRequired,
                orderNumber: data.value[i].orderNo,
                options: [],
            };
            formComponents.value.push(inputComponent);

            // get options for input component with UUID
            const optionsResponse = await axios.get(
                'http://localhost:8080/api/options/input-component/' + data.value[i].componentUuid
            );
            const optionsData = optionsResponse.data;
            for (let j = 0; j < optionsData.length; j++) {
                formComponents.value[i].options.push(optionsData[j].optionPrompt);
            }
            console.log(formComponents.value)
        }
    } catch (error) {
        console.log(error);
    }
}

if (canvasUuid) {
    getInputComponent();
}

</script>