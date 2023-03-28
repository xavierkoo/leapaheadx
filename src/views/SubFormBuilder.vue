<template>
    <div class="container p-4 text-white" style="background-color: #142442">
        <div class="header-container">
            <h4 class="dashboard-title">Sub Form Builder</h4>
            <h5 class="login-name">Admin1</h5>
            <!-- mock data, to be changed  -->
        </div>
        <div class="row">
            <div id="left-palette" class="col-md-8">
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
                        <div class="col-md-3 col-lg-2 text-end">
                            <router-link to="/subFormDashboard">
                                <button class="btn btn-success w-100"  style="font-size: 25px;" @click="$event => savingSubForm()">
                                    Save
                                </button>
                            </router-link>
                        </div>
                    </div>
                    <div 
                        class="rounded-4 p-5 form-container" 
                        style="background-color: #1A263C;" 
                        @drop="dropHandler" 
                        @dragover="dragOverHandler"
                    >
                        Drag Form Components Here
                    </div>
                </div>
            </div>
            <div id="right-palette" class="col-md-4">
                <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726; position: sticky; top: 20px;">
                    <h4 class="mb-5">Form Field Components</h4>
                    <div class="form-field-container" style="height: 660px; overflow-y: scroll;">
                        <div
                            v-for="(fieldType, index) in allFormFieldTypes"
                            :key="index"
                            :draggable="true"
                            class="form-field-box text-center rounded-3 mb-4 p-3"
                            @dragstart="dragStartHandler($event, fieldType)"
                        >
                            <strong>{{ fieldType }}</strong>
                            <br />
                            <span style="color: #5EBBE9;">Drag & Drop</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';

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

const formComponents = ref([])
const formDesc = ref('')

// Drag and Drop ------------------------------------------------------------------------------------------------------

// Allows form components to be dragged from right-palette
const dragStartHandler = (event, fieldType) => {
    event.dataTransfer.setData('text/plain', fieldType)
    event.dataTransfer.effectAllowed = 'move'
}

// Allows form components to be dropped into left-palette with styling and editable properties
const dropHandler = (event) => {
    event.preventDefault()
    const fieldType = event.dataTransfer.getData('text/plain', event.target.id)
    const component = createFormFieldComponent(fieldType)

    const formComponent = document.createElement('div')
    formComponent.classList.add('form-component')
    formComponent.classList.add('my-5')
    formComponent.setAttribute('draggable', true)
    formComponent.innerHTML = `
        <label class="mb-2"><strong>${component.label}</strong></label>
        <input class="form-control" type="text" placeholder="Question" name="question"/>
        ${['Drop-Down Menu', 'Check Box', 'Radio Button'].includes(fieldType)
            ? `
            <div class="additional-inputs-container">
                <div class="additional-input">
                </div>
            </div>
            <button class="btn btn-primary add-input-btn mt-2" type="button">Add Option</button>
            <button class="btn btn-danger remove-input-btn mt-2" type="button">Remove Option</button>
            `
            : ''
        }
        <div class="form-check mt-2">
            <input class="form-check-input" type="checkbox" id="required-checkbox" required name="required">
            <label class="form-check-label" for="required-checkbox">
                Required
            </label>
        </div>
        <div class="btn-container d-flex justify-content-end"><button class="btn btn-danger btn-sm mt-2" type="button">Remove</button></div>
    `;

    // Selects the remove button element inside a form component
    // and adds an event listener for a click event to trigger the removeComponent function.
    // Additionally, it adds another event listener for a dragover event on the same button,
    // which prevents the default behavior of the dragover event from occurring.
    const removeButton = formComponent.querySelector('.btn-container button');
        removeButton.addEventListener('click', removeComponent);
        removeButton.addEventListener('dragover', (event) => {
            event.preventDefault();
    });

    //  Allows users to add or remove input options for Drop-Down Menu, Check Box, or 
    // Radio Button fields. Initializes the optionIndex variable to 2 and sets up event 
    // listeners for the "Add Input" and "Remove Input" buttons. "Add Input" button creates 
    // a new input element in the container when clicked, while "Remove Input" button removes 
    // the last input element from the container.
    if (['Drop-Down Menu', 'Check Box', 'Radio Button'].includes(fieldType)) {
        const addInputButton = formComponent.querySelector('.add-input-btn');
        const removeInputButton = formComponent.querySelector('.remove-input-btn');
        const additionalInputsContainer = formComponent.querySelector('.additional-inputs-container');

        let optionIndex = 0;

        const addInput = formComponent.querySelector('.add-input-btn');
        addInput.addEventListener('click', () => {
            optionIndex++;
            const additionalInputsContainer = formComponent.querySelector('.additional-inputs-container');
            const newAdditionalInput = document.createElement('div');
            newAdditionalInput.classList.add('additional-input');
            newAdditionalInput.dataset.index = optionIndex - 1;
            newAdditionalInput.innerHTML = `
                <label class="mt-2" for="option-${optionIndex}">Option ${optionIndex}:</label>
                <input class="form-control" type="text" id="option-${optionIndex}" name="option"/>
            `;
            additionalInputsContainer.appendChild(newAdditionalInput);
        });

        const removeInput = () => {
            if (optionIndex > 0) {
                optionIndex--;
                additionalInputsContainer.removeChild(additionalInputsContainer.lastElementChild);
            }
        };

        addInputButton.addEventListener('click', addInput);
        removeInputButton.addEventListener('click', removeInput);
    }

    formComponent.style.cssText = 
        'border-radius: 5px; padding: 10px; margin-top: 25px; margin-bottom: 25px; background-color: #598BAF;';

    if (
        event.target.classList.contains('form-component') || // already a form component
        event.target.closest('.btn-container') || // a remove button
        event.target.closest('.additional-inputs-container') || // an option input
        event.target.closest('.add-input-btn') || // an add option button
        event.target.closest('.remove-input-btn') || // a remove option button
        event.target.closest('label') || // a label element
        event.target.classList.contains('form-check') // a form check element
    ) {
        // Cancel the drop event and prevent the form component from being added to the target element
        event.dataTransfer.dropEffect = 'none';
        return;
    } else {
        event.target.appendChild(formComponent)

        // Check if form component is added to the DOM before pushing to formComponents array
        if (document.contains(formComponent)) {
            // Generate a unique ID for the form component
            const id = `form-component-${Date.now()}`;

            // Add form component to formComponents array
            formComponents.value.push({
                id,
                question: "",
                required: false,
                options: [],
                orderNumber: formComponents.value.length + 1,
                type: component.label
            });

            // Attach input event listener to form component
            formComponent.addEventListener('input', (event) => {
                // Find the index of the form component in the formComponents array using its ID
                const index = formComponents.value.findIndex(c => c.id === id);

                // Update the form component's values based on the input event
                if (event.target.name === 'question') {
                    formComponents.value[index].question = event.target.value
                } else if (event.target.name === 'required') {
                    formComponents.value[index].required = event.target.checked
                } else if (event.target.name === 'option') {
                    const optionIndex = event.target.parentElement.dataset.index;
                    formComponents.value[index].options[optionIndex] = event.target.value + "," + optionIndex;
                }
            });
        }
    }
}

// Prevent the default behavior of the browser,
const dragOverHandler = (event) => {
    event.preventDefault()
}

// Drag and Drop End ------------------------------------------------------------------------------------------------------

// Form Component Creation -------------------------------------------------------------------------------------------------

// Creates a form component based on the type of form component
const createFormFieldComponent = (fieldType) => {
    switch (fieldType) {
        case 'Text Only':
            return {
                type: 'text',
                label: 'Text Only',
                value: '',
                editable: true,
            }
        case 'Numbers Only':
            return {
                type: 'number',
                label: 'Numbers Only',
                value: '',
                editable: true,
            }
        case 'Email':
            return {
                type: 'email',
                label: 'Email',
                value: '',
                editable: true,
            }
        case 'Phone Number':
            return {
                type: 'tel',
                label: 'Phone Number',
                value: '',
                editable: true,
            }
        case 'Address':
            return {
                type: 'text',
                label: 'Address',
                value: '',
                editable: true,
            }
        case 'Date Picker':
            return {
                type: 'date',
                label: 'Date Picker',
                value: '',
                editable: true,
            }
        case 'Time Picker':
            return {
                type: 'time',
                label: 'Time Picker',
                value: '',
                editable: true,
            }
        case 'Drop-Down Menu':
            return {
                type: 'select',
                label: 'Drop-Down Menu',
                value: '',
                options: ['Option 1', 'Option 2', 'Option 3'],
                editable: true,
            }
        case 'Radio Button':
            return {
                type: 'radio',
                label: 'Radio Button',
                value: '',
                options: ['Option 1', 'Option 2', 'Option 3'],
                editable: true,
            }
        case 'Check Box':
            return {
                type: 'checkbox',
                label: 'Check Box',
                value: '',
                editable: true,
            }
        case 'File Upload':
            return {
                type: 'file',
                label: 'File Upload',
                value: '',
                editable: true,
            }
        case 'Signature':
            return {
                type: 'signature',
                label: 'Signature',
                value: '',
                editable: true,
            }
        default:
            return null
    }
}

const removeComponent = (event) => {
    const indexToRemove = event.target.parentNode.parentNode.dataset.index;
    formComponents.value.splice(indexToRemove, 1);
    event.target.parentNode.parentNode.remove();
}

// Form Component Creation End -------------------------------------------------------------------------------------------------

// SubForm Component Saving -------------------------------------------------------------------------------------------------------
const savingSubForm = async () => {
    const returnedSubFormId = ref('');
    const returnedComponentId = ref('');

    const subFormData = {
        //TODO: hard coded for now, retrieve from local storage
        createdBy: '79ebaf36-bd58-11ed-afa1-0242ac120002',
        name: subFormName.value,
        description: formDesc.value,
    }

    if (subFormData) {
        try {
            const response = await axios.post(
                'http://localhost:8080/api/subformcanvas',
                subFormData
            );
            returnedSubFormId.value = response.data;
            console.log("Sub Form ID returned: " + response.data);
        } catch (error) {
            console.log(error);
        }
    }

    if (formComponents.value) {
        try {
            for (let i = 0; i < formComponents.value.length; i++) {
                const inputComponentData = {
                    question: formComponents.value[i].question,
                    type: formComponents.value[i].type,
                    isRequired: formComponents.value[i].required,
                    orderNo: formComponents.value[i].orderNumber,
                    parentCanvas: returnedSubFormId.value,
                };

                const response = await axios.post(
                    'http://localhost:8080/api/inputcomponents',
                    inputComponentData
                );
                returnedComponentId.value = response.data;
                console.log("Input Component ID returned: " + response.data);

                if (formComponents.value[i].options) {
                    for (let j = 0; j < formComponents.value[i].options.length; j++) {
                        const optionData = {
                            optionPrompt: formComponents.value[i].options[j],
                            parentInputComponent: returnedComponentId.value,
                        };

                        const response = await axios.post(
                            'http://localhost:8080/api/options',
                            optionData
                        );
                        console.log("Option ID returned: " + response.data);
                    }
                }
            }
        } catch (error) {
            console.log(error);
        }
    }
    window.location.reload();
}
// SubForm Component Saving End -------------------------------------------------------------------------------------------------------

</script>

<style>
    table,
    th,
    td {
        border: none;
    }
</style>
