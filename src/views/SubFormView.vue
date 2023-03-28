<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">Sub Form Viewer</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div class="row">
            <div id="left-palette">
                <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
                    <div class="row align-items-center">
                        <div class="col-md-9 col-lg-8">
                            <input 
                                v-model="subFormName" 
                                class="form-field-box" 
                                placeholder="Untitled Sub-Form" 
                                style="height: 60px; width: 400px;"
                            />
                        </div>
                        <div class="col-md-9 col-lg-8">
                            <input
                                v-model="formDesc"
                                class="form-field-box my-3"
                                placeholder="Enter Description"
                                style="height: 100px; width: 500px;"
                            />
                        </div>
                        <div class="col-md-3 col-lg-2">
                        </div>
                    </div>
                    <div 
                        class="rounded-4 p-5 form-container" 
                        style="background-color: #1A263C;" 
                    >
                        <div v-for="(inputComponent, index) in formComponents" :key="index">
                            <div class="form-component my-5" style="border-radius: 5px; padding: 10px; margin-top: 25px; margin-bottom: 25px; background-color: #598BAF;">
                                <label class="mb-2"><strong>{{ inputComponent.type }}</strong></label>
                                <input v-model="inputComponent.question" class="form-control custom-input-color" type="text" placeholder="Question" name="question" disabled/>
                                <div v-if="['Drop-Down Menu', 'Check Box', 'Radio Button'].includes(inputComponent.type)">
                                    <div class="additional-inputs-container">
                                        <div v-for="(option, optionIndex) in inputComponent.options.sort()" :key="optionIndex" class="additional-input my-2" >
                                            <label>Option {{ optionIndex + 1 }}</label>
                                            <input v-model="option.split(',')[0]" class="form-control" type="text"  placeholder="Option" disabled/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-check mt-2">
                                    <input  v-model="inputComponent.required"  class="form-check-input" type="checkbox" name="required" disabled/>
                                    <label class="form-check-label" for="required-checkbox">Required</label>
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