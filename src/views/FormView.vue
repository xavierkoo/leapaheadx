<!-- <template>
    <div>
      <component v-for="(type, index) in inputTypes"
                 :key="index"
                 :is="getComponentName(type)"
                 v-model="formData[type]"></component>
    </div>
</template>
  
<script>
    import TextInput from '../components/TextInput.vue'
    import SelectInput from '../components/SelectInput.vue'
    import RadioInput from '../components/RadioInput.vue'
    import CheckBoxInput from '../components/CheckBoxInput.vue'
    export default {
    components: {
        TextInput,
        SelectInput,
        RadioInput,
        CheckBoxInput
    },
    data() {
        return {
        inputTypes: ['text', 'select', 'checkbox', 'radio', 'integer'],
        formData: {}
        }
    },
    computed: {
        getComponentName() {
        return function(type) {
            switch (type) {
            case 'text':
                return 'TextInput';
            case 'integer':
                return 'TextInput';
            case 'select':
                return 'SelectInput';
            case 'checkbox':
                return 'CheckBoxInput';
            case 'radio':
                return 'RadioInput';
            default:
                return '';
            }
        }
        }
    }
    }
</script> -->
<!-- <template>
    <form>
      <div v-for="(input, index) in inputs" :key="index">
        <label :for="input.question">{{ input.question }}</label><br>
        <select v-if="input.type === 'select'" :name="input.question">
          <option v-for="(option, i) in input.options" :key="i" :value="option.value">{{ option.label }}</option>
        </select>
        <input v-else :type="input.type" :name="input.question" :value="input.value" />
      </div>
    </form>
  </template>
  
  <script>
  export default {
    data() {
      return {
        inputs: [
          { type: 'text', question: 'Name', options: null, value: '' },
          { type: 'email', question: 'Email', options: null, value: '' },
          { type: 'password', question: 'Password', options: null, value: '' },
          { 
            type: 'select', 
            question: 'Gender', 
            options: [
              { value: 'male', label: 'Male' },
              { value: 'female', label: 'Female' },
              { value: 'other', label: 'Other' },
            ], 
            value: '' 
          },
          { 
            type: 'radio', 
            question: 'Do you like Vite?', 
            options: [
              { value: 'yes', label: 'Yes' },
              { value: 'no', label: 'No' },
            ], 
            value: '' 
          },
        ],
      };
    },
  };
  </script> -->
<template>
    <form>
        <div v-for="(input, index) in inputs" :key="index">
            <label :for="input.question">{{ input.question }}</label> // add Heading tag to wrap
            input.question
            <select v-if="input.type === 'select'" :name="input.question">
                <option v-for="(option, i) in input.options" :key="i" :value="option.value">
                    {{ option.label }}
                </option>
            </select>
            <div v-else-if="input.type === 'radio'">
                <div v-for="(option, i) in input.options" :key="i">
                    <input
                        :id="`${input.question}-${i}`"
                        v-model="input.value"
                        type="radio"
                        :name="input.question"
                        :value="option.value"
                    />
                    <label :for="`${input.question}-${i}`">{{ option.label }}</label>
                </div>
            </div>
            <input v-else :type="input.type" :name="input.question" :value="input.value" />
        </div>
    </form>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            formName: '',
            applicationId: '79ec053a-bd58-11ed-afa1-0242ac120002',
            status: '',
            dateCreated: '',
            comments: '',
            inputs: []
            // inputs: [
            //   { type: 'text', question: 'Name', options: null, value: '' },
            //   { type: 'email', question: 'Email', options: null, value: '' },
            //   { type: 'password', question: 'Password', options: null, value: '' },
            //   {
            //     type: 'select',
            //     question: 'Gender',
            //     options: [
            //       { value: 'male', label: 'Male' },
            //       { value: 'female', label: 'Female' },
            //       { value: 'other', label: 'Other' },
            //     ],
            //     value: ''
            //   },
            //   {
            //     type: 'radio',
            //     question: 'Do you like Vite?',
            //     options: [
            //       { value: 'yes', label: 'Yes' },
            //       { value: 'no', label: 'No' },
            //     ],
            //     value: ''
            //   },
            // ],
        }
    },
    async beforeMount() {
        let aId = this.applicationId
        let application = await axios.get(
            `http://localhost:8080/api/applications/getFullForm/${aId}`
        )
        this.formName = application.data[0].formName
        this.status = application.data[0].applicationStatus
        this.dateCreated = application.data[0].date
        this.comments = application.data[0].comments
        let canvas = application.data[0].canva
        let input = []
        for (let canva of canvas) {
            let dict = {}
            dict['canvaId'] = canva.canvasId
            dict['canvaName'] = canva.canvasName
            let canvaComponents = canva.canvaComponent
            let inputComponent = []
            for (let canvaComponent of canvaComponents) {
                let inputComponentObject = {}
                inputComponentObject['componentId'] = canvaComponent.componentId
                inputComponentObject['question'] = canvaComponent.question
                inputComponentObject['type'] = canvaComponent.type
                let inputComponentOptions = canvaComponent.optionPrompt
                let inputComponentOptionArray = []
                for (let inputComponentOption of inputComponentOptions) {
                    let inputComponentOptionObject = {}
                    inputComponentOptionObject['value'] = inputComponentOption
                    inputComponentOptionArray.push(inputComponentOptionObject)
                }
                inputComponentObject['options'] = inputComponentOptionArray
                inputComponentObject['values'] = canvaComponent.value
                inputComponent.push(inputComponentObject)
            }
            dict['inputComponent'] = inputComponent
            input.push(dict)
        }
        this.inputs = input
        console.log(this.inputs)
    }
}
</script>
