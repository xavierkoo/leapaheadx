<template>
    <div class="formRender">
        <!-- This is the dashboard header -->
        <div class="row mx-2 mx-sm-5 pad-d">
            <div class="col-12 col-sm-8 col-lg-5 col-xl-3">
                <h4>My Application</h4>
            </div>
            <div class="d-none d-lg-block col-lg col-xl" />
            <div class="col text-start col-sm-4 col-lg-5 col-xl-3 text-sm-end">
                <!-- TODO - Change this to variable based on login -->
                <h5>{{ companyName }}</h5>
            </div>
        </div>

        <div class="row mx-2 mx-sm-5 pad-d">
            <!-- Left Section (Form Components) -->
            <form
                id="htmlContent"
                class="col-12 col-xl-8 me-xl-5 dark-container pb-5 order-last order-xl-first mt-4 mt-xl-0" 
    
            >
                <!-- This is the Title + Btns -->
                <div class="row mx-sm-2 mx-lg-5">
                    <div class="col-12 col-xl-8 pad-e">
                        <h5>{{ formName }}</h5>
                        <div class="small">{{ applicationId }}</div>
                    </div>

                    <!-- Reject Btn (Only visable to approver) -->
                    <div
                        class="col-6 col-lg-6 col-xl-2 pt-3 pt-sm-4"
                        :hidden="(userType != 'admin' && userType != 'approver') || hidden"
                    >
                    <button type="button" class="light-button" :disabled="submited" data-bs-toggle="modal" data-bs-target="#rejects">
                        Reject
                    </button>
                    <!-- Modal to add comments before rejection -->
                    <div id="rejects" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabels" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 id="rejectModalLabels" class="modal-title text-dark">Archive Application</h5>
                              <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body text-dark">
                                <div class="container">
                                    <textarea v-model="comments" class="w-100" rows="10"/>
                                </div>
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="light-button" data-bs-dismiss="modal">Cancel</button>
                              <button type="button" class="blue-button" data-bs-dismiss="modal" @click="reject()">Reject</button>
                            </div>
                          </div>
                        </div>
                    </div>
                    </div>

                    <!-- Save Btn  -->
                    <div class="col-6 col-lg-6 col-xl-2 pt-3 pt-sm-4">
                        <button class="light-button" :disabled="submited" :hidden="hidden" @click="save()">
                            Save
                        </button>
                    </div>

                    <!-- Submit Btn  -->
                    <div class="col-6 col-lg-6 col-xl-2 pt-3 pt-sm-4">
                        <button class="blue-button" type="submit" :disabled="submited" :hidden="hidden" @click.prevent="submitForm">
                            {{userType != 'approver' ? 'Submit' : 'Approve'}}
                        </button>
                    </div>

                    <div class="d-none d-xl-block col-xl-1"></div>
                </div>

                <!-- This is the Form Display Area -->
                <div class="row pad-d">
                    <!-- iterate through an array of subcanvas object -->
                    <div v-for="(canva, index) in inputs" :id="canva.canvaId" :key="index">
                        <!-- iterate through an array of input components in each sub canvas -->
                        <div
                            v-for="(component, ind) in canva.inputComponent"
                            :key="ind"
                            class="mt-3"
                        >
                            <!-- This is the question card -->
                            <div class="px-sm-5">
                                <!-- This is the question header it. changes the color of the label if the required input is not filled yet. -->
                                <label
                                    class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                    :class="{'fillUpRequired':required.includes(canva.canvaId) && (!formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`] || formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`][0] ==null )}"
                                    :for="component.question"
                                    >{{ component.question }}
                                    <span
                                    :class="{
                                        'fillUpRequiredTextOnly':required.includes(canva.canvaId) && ( !formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`] || formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`][0] == null),
                                        'requiredStar': formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`] && formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`][0] != null
                                      }"
                                    :hidden="!required.includes(canva.canvaId)"
                                        >{{ component.required ? '*' : '' }} {{required.includes(canva.canvaId) && (!formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`] || formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`][0] ==null?"(Please complete this field)":"")}}</span
                                    >
                                </label>
                                <br />

                                <div class="innerInputs px-4 py-4" >
                                    <!--Use dropdown if component type is dropdown-->
                                    <select
                                        v-if="component.type === 'dropdown'"
                                        :id="component.componentId"
                                        v-model="
                                            formData[
                                                `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                            ]
                                        "
                                        class="w-100"
                                        :name="component.question"
                                        :disabled="!required.includes(canva.canvaId) || submited"
                                        :required="component.required"
                                        @change="log()"
                                    >
                                        <option
                                            v-for="(option, i) in component.options"
                                            :key="i"
                                            :value="option.value"
                                        >
                                            {{ option.value }}
                                        </option>
                                    </select>

                                    <!--Use checkbox if component type is checkbox-->
                                    <div v-else-if="component.type === 'checkbox'">
                                        <div v-for="(option, i) in component.options" :key="i">
                                            <input
                                                :id="`${component.componentId}-${i}`"
                                                v-model="
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ]
                                                "
                                                type="checkbox"
                                                :name="component.question"
                                                :value="option.value"
                                                :checked="formData[
                                                    `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                ] != null &&
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ].includes(option.value)
                                                "
                                                :disabled="!required.includes(canva.canvaId) || submited"
                                            />
                                            <label
                                                class=" optionLabel"
                                                :for="`${component.componentId}-${i}`"
                                                >{{ option.value }}</label
                                            >
                                        </div>
                                    </div>
                                    <!--Use radiobutton if component type is radio-->
                                    <div v-else-if="component.type === 'radio'">
                                        <div v-for="(option, i) in component.options" :key="i">
                                            <input
                                                :id="`${component.componentId}-${i}`"
                                                v-model="
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ]
                                                "
                                                class="custom-control-input"
                                                type="radio"
                                                :name="component.question"
                                                :value="option.value"
                                                :disabled="!required.includes(canva.canvaId) || submited"
                                            />
                                            <label
                                                class="custom-control-label optionLabel"
                                                :for="`${component.componentId}-${i}`"
                                                >{{ option.value }}</label
                                            >
                                        </div>
                                    </div>
                                    <!--Use normal input if component type is integer or text-->
                                    <input
                                        v-else
                                        v-model="
                                            formData[
                                                `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                            ]
                                        "
                                        class="form-control"
                                        :type="component.type"
                                        :name="component.question"
                                        :disabled="!required.includes(canva.canvaId) || submited"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mx-sm-2 mx-lg-5">
                    <div class="col-12 col-xl-8 pad-e" />

                    <!-- Reject Btn (Only visable to approver) -->
                    <div
                        class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4"
                        :hidden="(userType != 'admin' && userType != 'approver') || hidden"
                    >
                        <button type="button" class="light-button" :disabled="submited" data-bs-toggle="modal" data-bs-target="#reject">
                            Reject
                        </button>
                        <div id="reject" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 id="rejectModalLabel" class="modal-title text-dark">Archive Application</h5>
                                  <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                  </button>
                                </div>
                                <div class="modal-body text-dark">
                                    <div class="container">
                                        <textarea v-model="comments" class="w-100" rows="10"/>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="light-button" data-bs-dismiss="modal">Cancel</button>
                                  <button type="button" class="blue-button" data-bs-dismiss="modal" @click="reject()">Reject</button>
                                </div>
                              </div>
                            </div>
                        </div>
                    </div>

                    <!-- Save Btn  -->
                    <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                        <button class="light-button" :disabled="submited" :hidden="hidden"  @click="save()">
                            Save
                        </button>
                    </div>

                    <!-- Submit Btn  -->
                    <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                        <button class="blue-button" type="submit" :disabled="submited" :hidden="hidden"  @click.prevent="submitForm">
                            {{userType != 'approver' ? 'Submit' : 'Approve'}}
                        </button>
                    </div>

                    <div class="d-none d-xl-block col-xl-1"></div>
                </div>
            </form>
            <!-- Right Section (Application Information) -->
            <div
                class="col-12 col-xl-3 dark-container pb-5 order-first order-xl-last"
                style="min-height: 40vh"
            >
                <div class="row mx-2 pad-d">
                    <div class="col"><h6>Application Info</h6></div>

                    <!-- Status -->
                    <div class="mt-3">
                        <div class="mt-3 rightBarLabel">Status</div>
                        <div>{{ status }}</div>
                    </div>

                    <!-- Date Created -->
                    <div class="mt-3">
                        <div class="mt-3 rightBarLabel">Date Created</div>
                        <div>{{ dateCreated }}</div>
                    </div>

                    <!-- Comment -->
                    <div class="mt-3">
                        <div class="mt-3 rightBarLabel">Comments</div>
                        <div>{{ comments }}</div>
                    </div>

                    <!-- Remove Btn (Only for Approver) -->
                    <div class="mt-3" :hidden="userType != 'approver'">
                        <button type="button" class="btn light-button" data-bs-toggle="modal" data-bs-target="#archive">
                            Archive
                        </button>
                        <div id="archive" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 id="exampleModalLabel" class="modal-title text-dark">Archive Application</h5>
                                  <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                  </button>
                                </div>
                                <div class="modal-body text-dark">
                                  Do you want to archive this application?
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="light-button" data-bs-dismiss="modal">Cancel</button>
                                  <button type="button" class="blue-button" data-bs-dismiss="modal" @click="archive()">Archive</button>
                                </div>
                              </div>
                            </div>
                        </div>
                    </div>

                    <!-- View Form -->
                    <div class="mt-3" :hidden="userType != 'admin' && userType != 'approver'">
                        <button class="light-button" :disabled="submited">View Form</button>
                    </div>

                    <!-- Export to PDF -->
                    <div class="mt-3" :hidden="userType != 'admin' && userType != 'approver'">
                        <button class="light-button" :disabled="submited" data-bs-toggle="modal" data-bs-target="#export" @click="hidden=true"
                        >Export PDF</button>

                        <div id="export" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                              <div class="modal-content">
                                <div class="modal-header">
                                  <h5 id="exampleModalLabel" class="modal-title text-dark">Export Application</h5>
                                  <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                  </button>
                                </div>
                                <div class="modal-body text-dark">
                                  Do you want to export the applciation to PDF?
                                </div>
                                <div class="modal-footer">
                                  <button type="button" class="light-button" data-bs-dismiss="modal">Cancel</button>
                                  <button type="button" class="blue-button" data-bs-dismiss="modal" @click="generatePDF">Archive</button>
                                </div>
                              </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import { useRoute } from 'vue-router'
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import html2canvas from 'html2canvas';

export default {
    data() {
        return {
            formName: '',
            formId:'',
            applicationId: '', //Swapped to dynamic (Accessible via the btn on adminDashboard) 79ec03aa-bd58-11ed-afa1-0242ac120002
            userId: '79eb9fd2-bd58-11ed-afa1-0242ac120002', //supposed to be dynamic vendor:79ebaad6-bd58-11ed-afa1-0242ac120002 admin:79eb9b5e-bd58-11ed-afa1-0242ac120002 approver:79eb9fd2-bd58-11ed-afa1-0242ac120002
            userType: '',
            companyName: '',
            status: '',
            dateCreated: '',
            comments: 'None at the moment',
            inputs: [],
            required: [],
            formData: {},
            currentStep:"",
            submited: false,
            hidden:false
        }
    },
    async beforeMount() {
        //determine if form is editable by current user based on current step

        // Dynamically retrieve applicationId from routing
        const route = useRoute()
        this.applicationId = route.params.applicationId

        //get application
        let aId = this.applicationId;
        let application = await axios.get(
            `http://localhost:8080/api/applications/getFullForm/${aId}`
        )
        //Application formId
        this.formId = application.data[0].formId
        //Application formname
        this.formName = application.data[0].formName
        //Application status
        this.status = application.data[0].applicationStatus
        //Application date created
        let date = new Date(application.data[0].dateCreated)
        const options = { hour12: false, timeZone: 'Asia/Singapore' };
        const formattedDate = date.toLocaleString('en-US', options);
        this.dateCreated = formattedDate
        //currentStep of Application
        this.currentStep = application.data[0].currentStep;
        //  = date.toLocaleDateString('en-GB', { day: '2-digit', month: '2-digit', year: 'numeric' });
        //Company Name
        this.companyName = application.data[0].companyName
        //Application comments
        if (application.data[0].comments == null) {
            this.comments = 'No comments at the moment'
        } else {
            this.comments = application.data[0].comments
        }
        //Array of canvas
        let canvas = application.data[0].canva
        let input = []
        //iterate the subcanvases in the application
        for (let canva of canvas) {
            let dict = {}
            //for each subcanvas, get canvasId, canvasName and the input components in the subcanvas
            dict['canvaId'] = canva.canvasId
            dict['canvaName'] = canva.canvasName
            let canvaComponents = canva.canvaComponent
            let inputComponent = []
            //iterate the input components in the subcanvas
            for (let canvaComponent of canvaComponents) {
                let inputComponentObject = {}
                //for each input component, get componentId, component question, required, component type and the options for each component if it exist (checkbox, dropdown and radio)
                inputComponentObject['componentId'] = canvaComponent.componentId
                inputComponentObject['question'] = canvaComponent.question
                inputComponentObject['type'] = canvaComponent.type // if canva component type  == xavier's label change it to respective plain type.
                inputComponentObject['required'] = canvaComponent.required
                let inputComponentOptions = canvaComponent.optionPrompt
                //add the possible choices in an array
                let inputComponentOptionArray = []
                for (let inputComponentOption of inputComponentOptions) {
                    let inputComponentOptionObject = {}
                    inputComponentOptionObject['value'] = inputComponentOption
                    inputComponentOptionArray.push(inputComponentOptionObject)
                }
                inputComponentObject['options'] = inputComponentOptionArray
                //get the value saved or submited. If component type is a checkbox, need to return an array
                if (canvaComponent.type == 'checkbox') {
                    let value = canvaComponent.value
                    if (value != null || value !="" || value!=undefined) {
                        if (value.indexOf(',') != -1) {
                            let checkBoxArray = canvaComponent.value.split(',')
                            this.formData[
                                `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                            ] = checkBoxArray
                        } else if(value !="") {
                            this.formData[
                                `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                            ] = [value]
                        }
                        else{
                                this.formData[
                                        `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                                    ]=[]
                        }
                    }
                } else {
                    this.formData[
                        `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                    ] = canvaComponent.value
                }
                inputComponent.push(inputComponentObject)
            }
            //append the inputcomponet to subcanvas and add subccanvas to array
            dict['inputComponent'] = inputComponent
            input.push(dict)
        }
        this.inputs = input
        

        // Dynamically retrieve usertype using uId
        let uId = this.userId
        let user = await axios.get(`http://localhost:8080/api/users/${uId}`)
        this.userType = user.data.role;
        

        //check the current assignee type of the application
        let assignedType = await axios.get(`http://localhost:8080/api/applications/assignee/${aId}`)
        if (assignedType.data != this.userType) {
            this.submited = true
        }
        
        //get unrestrcited subcanvas returns an array of subcanvas that needs to be filled up
        let assignedApplications = await axios.get(
            `http://localhost:8080/api/applications/${this.currentStep}/${this.formId}`
        )
        let data = assignedApplications.data[0].canvaFillUpNotRestricted
        for (let canvaId of data) {
            this.required.push(canvaId)
        }
    },
    methods: {
        log() {
            // let aId = this.applicationId;
            // let formData = this.formData;
            // let result = [];
            // 	//craft json body to post value to responseValue table
            // 	for(let key in formData){
            // 		let dict={};
            // 		//key is the combination of canvasID and componentID
            // 		let arraykey = key.split(",");
            // 		if(Array.isArray(formData[key])){
            // 			console.log(formData[key])
            // 			let checkboxString = formData[key].join();
            // 			dict["value"] = checkboxString;
            // 			console.log(checkboxString)
            // 		}else{
            // 			dict["value"] = formData[key];
            // 		}
            // 		dict["componentUuid"]=arraykey[0];
            // 		dict["applicationUuid"]=this.applicationId;
            // 		dict["canvasUuid"]=arraykey[0];
            // 		result.push(dict);
            // 	}
            // 	console.log(result);
        },
        generatePDF() {
            
            var doc = new jsPDF();

            // select the HTML element that contains the form
            var element = document.getElementById('htmlContent');

            // convert the form element to a canvas element using html2canvas
            html2canvas(element).then(function(canvas) {
            // calculate the height and width of the form element
            var formHeight = element.offsetHeight;
            var formWidth = element.offsetWidth;

            // add the canvas element to the PDF document with a custom size based on form size
            var imgData = canvas.toDataURL('image/png');
            var pdfWidth = 190;
            var pdfHeight = (formHeight / formWidth) * pdfWidth;
            doc.addImage(imgData, 'PNG', 10, 10, pdfWidth, pdfHeight);

            // save the PDF document
            doc.save('my-form.pdf');
            });
            this.hidden=false;
        },
        containsOnlyNumbers(str) {
            return /^\d+$/.test(str)
        },
        //validate form
        validateForm() {
            let errorMsg = []
            let data = this.formData
            for (let keys in data) {
                let keysArray = keys.split(',')
                let canvaId = keysArray[1]
                let type = keysArray[2]
                let question = keysArray[3]
                let value = data[keys]
                
                if (this.required.includes(canvaId)) {
                    // Check if the required fields are  empty
                    if(type=="checkbox" && Object.keys(value).length==0){
                        if (!errorMsg.includes('Please enter all required fields')) {
                                errorMsg.push('Please enter all required fields')
                            }
                    }else if(type!="checkbox" && (value ?? '').trim()==0 ){
                        if (!errorMsg.includes('Please enter all required fields')) {
                                errorMsg.push('Please enter all required fields')
                            }
                    }

                    if (!errorMsg.includes('Please enter all required fields')) {
                        // Check if the email is valid
                        if (type == 'email') {
                            const emailRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
                            if (!emailRegex.test(value)) {
                                if (!errorMsg.includes('Please enter a valid email address')) {
                                    errorMsg.push('Please enter a valid email address')
                                }
                            }
                        }
                        //check if input is numeric
                        if (type == 'integer') {
                            if (!this.containsOnlyNumbers(value)) {
                                errorMsg.push(
                                    'Please enter a numeric value in the ' + question + ' field'
                                )
                            }
                        }
                        if (type == 'tel') {
                            if (!this.containsOnlyNumbers(value)) {
                                errorMsg.push(
                                    'Please enter a numeric value in the ' + question + ' field'
                                )
                            }
                        }
                    }
                }
            }

            if (errorMsg.length != 0) {
                let errorStr = ''
                for (let msg of errorMsg) {
                    errorStr += msg + '\n'
                }
                alert(errorStr)
                errorMsg = [];
                return false
            } else {
                errorMsg = [];
                return true
            }
            
        },
        //save application
        save() {
            let aId = this.applicationId
            let formData = this.formData
            let result = []
            //craft json body to post value to responseValue table
            for (let key in formData) {
                let dict = {}
                //key is the combination of canvasID and componentID
                let arraykey = key.split(',')
                if (Array.isArray(formData[key])) {
                    let empty = formData[key].join(" ").trim()
                    let real = empty.split(" ")
                    let checkboxString = real.join()
                    dict['value'] = checkboxString
                } else {
                    dict['value'] = formData[key]
                }
                dict['componentUuid'] = arraykey[0]
                dict['applicationUuid'] = this.applicationId
                dict['canvasUuid'] = arraykey[1]
                result.push(dict)
            }
            if (this.userType == 'vendor') {
                //change status of application only
                axios
                    .put(`http://localhost:8080/api/applications/vendorSave/${aId}`)
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })

                //post crafted json to database
                axios
                    .post(
                        'http://localhost:8080/api/applicationResponseValues/saveSpecificResponse',
                        result
                    )
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            } else {
                //post crafted json to database
                axios
                    .post(
                        'http://localhost:8080/api/applicationResponseValues/saveSpecificResponse',
                        result
                    )
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            }
            alert('Form successfully saved')
        },
        submitForm() {
            if (this.validateForm()) {
                let aId = this.applicationId
                let formData = this.formData
                let result = []
                //craft json body to post value to responseValue table
                for (let key in formData) {
                    let dict = {}
                    //key is the combination of canvasID and componentID
                    let arraykey = key.split(',')
                    if (Array.isArray(formData[key])) {
                        let checkboxString = formData[key].join()
                        dict['value'] = checkboxString
                    } else {
                        dict['value'] = formData[key]
                    }
                    dict['componentUuid'] = arraykey[0]
                    dict['applicationUuid'] = this.applicationId
                    dict['canvasUuid'] = arraykey[1]
                    result.push(dict)
                }
                if (this.userType == 'vendor') {
                    //change status and step number of application and update values in response value table
                    axios
                        .put(`http://localhost:8080/api/applications/vendorSubmit/${aId}`)
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                    axios
                        .post(
                            'http://localhost:8080/api/applicationResponseValues/saveSpecificResponse',
                            result
                        )
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                } else if (this.userType == 'admin') {
                    //change status and step number of application and update values in response value table
                    axios
                        .put(`http://localhost:8080/api/applications/adminSubmit/${aId}`)
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                    axios
                        .post(
                            'http://localhost:8080/api/applicationResponseValues/saveSpecificResponse',
                            result
                        )
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                } else {
                    //change status and step number of application and update values in response value table
                    axios
                        .put(`http://localhost:8080/api/applications/approverApprove/${aId}`)
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                    axios
                        .post(
                            'http://localhost:8080/api/applicationResponseValues/saveSpecificResponse',
                            result
                        )
                        .then(function (response) {
                            console.log(response)
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                }
                alert('Form successfully submitted')
            }
        },
        reject() {
            let aId = this.applicationId
            if (this.userType == 'admin') {
                axios
                    .put(`http://localhost:8080/api/applications/adminReject/${aId}`, {comments:this.comments})
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            } else {
                axios
                    .put(`http://localhost:8080/api/applications/approverReject/${aId}`, {comments:this.comments})
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            }
        },
        archive() {
            let aId = this.applicationId
            axios
                .put(`http://localhost:8080/api/applications/archive/${aId}`)
                .then(function (response) {
                    console.log(response)
                })
                .catch(function (error) {
                    console.log(error)
                })
        }
    }
}
</script>
