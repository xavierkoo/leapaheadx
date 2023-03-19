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
                                class="custom-input" 
                                placeholder="ENTER SUB-FORM TITLE" 
                                style="height: 60px; width: 400px;"
                            />
                        </div>
                        <div class="col-md-3 col-lg-2">
                        </div>
                        <div class="col-md-3 col-lg-2 text-end">
                            <button class="btn btn-success w-100" style="font-size: 25px;">
                                Save
                            </button>
                        </div>
                    </div>
                    <div 
                        class="rounded-4 p-5 mt-4" 
                        style="background-color: #1A263C;" 
                        @drop="dropHandler" 
                        @dragover="dragOverHandler"
                        @dragend="dragEndHandler"
                    >
                    </div>
                </div>
            </div>
            <div id="right-palette" class="col-md-4">
                <div class="rounded-4 p-5 mt-4" style="background-color: #0f1726">
                    <h4 class="mb-5">Form Field Components</h4>
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
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'

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
    formComponent.setAttribute('draggable', true)
    formComponent.innerHTML = `
        <label class="mb-2">${component.label}</label><br/>
        <input class="form-control" type="text"" placeholder="Question" />
        <div class="btn-container d-flex justify-content-end"><button class="btn btn-danger btn-sm mt-2" type="button">Remove</button></div>
    `
    const button = formComponent.querySelector('button');
    button.addEventListener('click', removeComponent);
    formComponent.style.cssText = 
        'border-radius: 5px; padding: 10px; margin-top: 25px; margin-bottom: 25px; background-color: #5EBBE9;';

    // Check if target element is a form-component
    if (event.target.classList.contains('form-component') || event.target.closest('.btn-container')) {
        // Get the target element and its parent
        const targetElement = event.target.closest('.form-component');
        const parentElement = targetElement.parentElement;
        
        // Create a new form component and insert it before the target element
        const newFormComponent = document.createElement('div');
        newFormComponent.classList.add('form-component');
        newFormComponent.setAttribute('draggable', true);
        newFormComponent.innerHTML = `
            <label class="mb-2">${component.label}</label><br/>
            <input class="form-control" type="text" placeholder="Question" />
            <div class="btn-container d-flex justify-content-end"><button class="btn btn-danger btn-sm mt-2" type="button">Remove</button></div>
        `;
        const newButton = newFormComponent.querySelector('button');
        newButton.addEventListener('click', removeComponent);
        newFormComponent.style.cssText = 
            'border-radius: 5px; padding: 10px; margin-top:25px; margin-bottom: 25px; background-color: #5EBBE9;';
        parentElement.insertBefore(newFormComponent, targetElement);
        
        // Cancel the drop event and prevent the form component from being added to the target element
        event.dataTransfer.dropEffect = 'none';
        return;
    }
    
    event.target.appendChild(formComponent)
}

// Prevent the default behavior of the browser,
const dragOverHandler = (event) => {
    event.preventDefault()
}

// Get the new index of the component being dragged
const dragEndHandler = (event) => {
    const newIndex = data.value.findIndex((component) => component.id === event.target.id)
    const movedComponent = data.value.splice(event.dataTransfer.getData('index'), 1)[0]
    data.value.splice(newIndex, 0, movedComponent)
}

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
    event.target.parentNode.parentNode.remove()
}

</script>

<style>
table,
th,
td {
    border: none;
}
</style>
