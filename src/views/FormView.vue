<template>
    <div class="formRender container">
        <!-- This is the dashboard header -->

        <div class="row mx-2 mx-sm-5 pad-d">
            <div class="col-12 col-sm-8 col-lg-5 col-xl-4">
                <h4>My Application</h4>
            </div>
            <div class="d-none d-lg-block col-lg col-xl" />
            <div class="col text-start col-sm-4 col-lg-5 col-xl-3 me-xl-4 text-sm-end">
                <h5>{{ companyName }} : {{ userName }}</h5>
            </div>
        </div>

        <div class="row mx-2 mx-sm-5 pad-d">
            <!-- Left Section (Form Components) -->
            <form
                id="htmlContent"
                :class="
                    hidden
                        ? 'col-12 dark-container rounded-0 pb-5 order-last order-xl-first mt-4 mt-xl-0'
                        : 'col-12 col-xl-8 me-xl-5 dark-container pb-5 order-last order-xl-first mt-4 mt-xl-0'
                "
            >
                <!-- This is the Title + Btns -->
                <div class="row mx-sm-2 mx-lg-5">
                    <div class="col-12 pad-e">
                        <h5 class="text-light">{{ formName }}</h5>
                        <div class="small text-light">{{ applicationId }}</div>
                    </div>

                    <div class="col-12 col-xl-6"></div>
                    <!-- Save Btn  -->
                    <div class="col-12 col-lg-3 col-xl-3 pt-3 pt-sm-4">
                        <button
                            class="light-button"
                            :disabled="submited"
                            :hidden="hidden || status == 'Completed'"
                            @click.prevent="save()"
                        >
                            Save
                        </button>
                    </div>

                    <!-- Submit Btn  -->
                    <div class="col-12 col-lg-3 col-xl-3 pt-3 pt-sm-4">
                        <button
                            class="blue-button"
                            type="submit"
                            :disabled="submited"
                            :hidden="hidden || status == 'Completed'"
                            @click.prevent="submitForm"
                        >
                            {{ userType != 'approver' ? 'Submit' : 'Approve' }}
                        </button>
                    </div>

                    <div class="d-none d-xl-block col-xl-1"></div>
                </div>

                <!-- This is the Form Display Area -->
                <div class="row pad-d">
                    <div v-if="isSave || isSubmit">
                        <div class="errorLabel mx-sm-5 p-0 h-100 py-2">
                            <h6
                                v-if="isSubmit && status == 'Escalated'"
                                class="px-3 text-center text-success m-0"
                            >
                                Application is successfully approved.
                            </h6>
                            <h6 v-else-if="isSave" class="px-3 text-center text-success m-0">
                                Application is successfully saved.
                            </h6>
                            <h6 v-else-if="isSubmit" class="px-3 text-center text-success m-0">
                                Application is successfully submitted.
                            </h6>
                        </div>
                    </div>
                    <div v-if="errorhandling.length != 0" id="errorHandling" class="mt-3">
                        <ul class="errorLabel mx-sm-5 p-0 h-100">
                            <h6 class="pt-3 px-3 text-danger">Rectify the following errors:</h6>
                            <li v-for="(error, index) in errorhandling" :key="index" class="ms-5">
                                {{ error }}
                            </li>
                        </ul>
                    </div>
                    <!-- iterate through an array of subcanvas object -->
                    <div v-for="(canva, index) in inputs" :id="canva.canvaId" :key="index">
                        <!-- iterate through an array of input components in each sub canvas -->
                        <h5 class="px-sm-5 mt-3 canvaLabel">{{ canva.canvaName }}</h5>
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
                                    :class="{
                                        fillUpRequired:
                                            required.includes(canva.canvaId) &&
                                            submited == false &&
                                            (!formData[
                                                `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                            ] ||
                                                formData[
                                                    `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                ][0] == null)
                                    }"
                                    :for="component.question"
                                    >{{ component.question }}
                                    <span
                                        :class="{
                                            fillUpRequiredTextOnly:
                                                required.includes(canva.canvaId) &&
                                                (!formData[
                                                    `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                ] ||
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ][0] == null),
                                            requiredStar:
                                                formData[
                                                    `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                ] &&
                                                formData[
                                                    `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                ][0] != null
                                        }"
                                        :hidden="!required.includes(canva.canvaId)"
                                        >{{ component.required ? '*' : '' }}
                                        {{
                                            required.includes(canva.canvaId) &&
                                            (!formData[
                                                `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                            ] ||
                                            formData[
                                                `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                            ][0] == null
                                                ? '(Please complete this field)'
                                                : '')
                                        }}</span
                                    >
                                </label>
                                <br />

                                <div class="innerInputs px-4 py-4">
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
                                        :disabled="
                                            !required.includes(canva.canvaId) ||
                                            submited ||
                                            status == 'Completed'
                                        "
                                        :required="component.required"
                                    >
                                        <option
                                            v-for="(option, i) in component.options.sort((a, b) => {
                                                const aValue = parseInt(a.value.split(',')[1])
                                                const bValue = parseInt(b.value.split(',')[1])
                                                return aValue - bValue
                                            })"
                                            :key="i"
                                            :value="option.value.split(',')[0]"
                                        >
                                            {{ option.value.split(',')[0] }}
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
                                                :value="option.value.split(',')[0]"
                                                :checked="
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ] != null &&
                                                    formData[
                                                        `${component.componentId},${canva.canvaId},${component.type},${component.question}`
                                                    ].includes(option.value)
                                                "
                                                :disabled="
                                                    !required.includes(canva.canvaId) ||
                                                    submited ||
                                                    status == 'Completed'
                                                "
                                            />
                                            <label
                                                class="optionLabel"
                                                :for="`${component.componentId}-${i}`"
                                                >{{ option.value.split(',')[0] }}</label
                                            >
                                        </div>
                                    </div>
                                    <!--Use radiobutton if component type is radio-->
                                    <div v-else-if="component.type === 'radio'">
                                        <div
                                            v-for="(option, i) in component.options.sort((a, b) => {
                                                const aValue = parseInt(a.value.split(',')[1])
                                                const bValue = parseInt(b.value.split(',')[1])
                                                return aValue - bValue
                                            })"
                                            :key="i"
                                        >
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
                                                :disabled="
                                                    !required.includes(canva.canvaId) ||
                                                    submited ||
                                                    status == 'Completed'
                                                "
                                            />
                                            <label
                                                class="custom-control-label optionLabel"
                                                :for="`${component.componentId}-${i}`"
                                                >{{ option.value.split(',')[0] }}</label
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
                                        :disabled="
                                            !required.includes(canva.canvaId) ||
                                            submited ||
                                            status == 'Completed'
                                        "
                                    />
                                </div>
                            </div>
                        </div>

                        <div
                            v-if="calculationForm"
                            class="px-sm-5 my-3"
                            :hidden="calculateSubform(canva.canvaId) == 0"
                        >
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >SubTotal Score</label
                            >
                            <input
                                class="innerInputs px-4 py-4 w-100 border-0"
                                type="number"
                                :value="calculateSubform(canva.canvaId)"
                                :disabled="true"
                            />
                        </div>
                    </div>
                    <div :hidden="status != 'Completed' && calculateTotalPoints()[0] == 0">
                        <h6 class="px-sm-5 mt-3 canvaLabel">EVALUATION</h6>
                    </div>

                    <div :hidden="calculateTotalPoints()[0] == 0">
                        <div v-if="calculationForm" class="px-sm-5 my-1">
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >Total Score</label
                            >
                            <input
                                v-if="calculationForm"
                                class="innerInputs px-4 py-4 w-100 border-0"
                                type="number"
                                :disabled="true"
                                :value="calculateTotalPoints()[0]"
                            />
                        </div>
                    </div>
                    <div :hidden="calculateTotalPoints()[0] == 0">
                        <div v-if="calculationForm" class="px-sm-5 my-1">
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >Rating & Performance</label
                            >
                            <input
                                v-if="calculationForm"
                                class="innerInputs px-4 py-4 w-100 border-0"
                                type="text"
                                :disabled="true"
                                :value="
                                    calculateTotalPoints()[1].toFixed(2) +
                                    ' (' +
                                    calculateTotalPoints()[2] +
                                    ')'
                                "
                            />
                        </div>
                    </div>
                    <div :hidden="status != 'Completed'">
                        <div class="px-sm-5 my-3">
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >Vendor Signature</label
                            >
                            <div class="innerInputs px-4 py-4 w-100 border-0 text-black">
                                <div>Sign here:</div>
                            </div>
                        </div>
                    </div>
                    <div :hidden="status != 'Completed'">
                        <div class="px-sm-5 my-2">
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >Approver Signature</label
                            >
                            <div class="innerInputs px-4 py-4 w-100 border-0 text-black">
                                <div>Sign here:</div>
                            </div>
                        </div>
                    </div>
                    <div :hidden="status != 'Completed'">
                        <div class="px-sm-5 my-3">
                            <label class="FormLabel mt-2 py-3 px-4 w-100 innerLabel"
                                >Evaluator Signature</label
                            >
                            <div class="innerInputs px-4 py-4 w-100 border-0 text-black">
                                <div>Sign here:</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mx-sm-2 mx-lg-5">
                    <div class="col-12 col-xl-6 pad-e" />

                    <!-- Save Btn  -->
                    <div class="col-12 col-lg-3 col-xl-3 pt-3 pt-sm-4">
                        <button
                            class="light-button"
                            :disabled="submited"
                            :hidden="hidden || status == 'Completed'"
                            @click.prevent="save()"
                        >
                            Save
                        </button>
                    </div>

                    <!-- Submit Btn  -->
                    <div class="col-12 col-lg-3 col-xl-3 pt-3 pt-sm-4">
                        <button
                            class="blue-button"
                            type="submit"
                            :disabled="submited"
                            :hidden="hidden || status == 'Completed'"
                            @click.prevent="submitForm"
                        >
                            {{ userType != 'approver' ? 'Submit' : 'Approve' }}
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

                    <!-- Reject Btn (Only visable to approver) -->
                    <div
                        class="mt-3"
                        :hidden="(userType != 'admin' && userType != 'approver') || hidden"
                    >
                        <button
                            type="button"
                            class="rejectBtn"
                            :hidden="status == 'Completed'"
                            :disabled="submited"
                            data-bs-toggle="modal"
                            data-bs-target="#reject"
                        >
                            Reject
                        </button>
                        <div
                            id="reject"
                            class="modal fade"
                            tabindex="-1"
                            role="dialog"
                            aria-labelledby="rejectModalLabel"
                            aria-hidden="true"
                        >
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 id="rejectModalLabel" class="modal-title text-dark">
                                            Reject Application
                                        </h5>
                                        <button
                                            type="button"
                                            class="close"
                                            data-bs-dismiss="modal"
                                            aria-label="Close"
                                            @click="closeRejectModal"
                                        >
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body text-dark">
                                        <div v-if="!isRejectSuccess" class="container">
                                            <textarea v-model="comments" class="w-100" rows="10" />
                                        </div>
                                        <div v-else>
                                            <div class="text-danger">
                                                Application has been rejected
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button
                                            type="button"
                                            class="light-button"
                                            data-bs-dismiss="modal"
                                            @click="closeRejectModal"
                                        >
                                            Close
                                        </button>
                                        <button
                                            type="button"
                                            class="blue-button"
                                            :disabled="isRejectSuccess"
                                            @click="reject()"
                                        >
                                            Reject
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Archive Btn (Only for Approver) -->
                    <div class="mt-3" :hidden="userType != 'approver' || status != 'Completed'">
                        <button
                            type="button"
                            class="btn light-button"
                            data-bs-toggle="modal"
                            data-bs-target="#archive"
                        >
                            Archive
                        </button>
                        <div
                            id="archive"
                            class="modal fade"
                            tabindex="-1"
                            role="dialog"
                            aria-labelledby="exampleModalLabel"
                            aria-hidden="true"
                        >
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 id="exampleModalLabel" class="modal-title text-dark">
                                            Archive Application
                                        </h5>
                                        <button
                                            type="button"
                                            class="close"
                                            data-bs-dismiss="modal"
                                            aria-label="Close"
                                            @click="isArchive = false"
                                        >
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body text-dark">
                                        <div v-if="!isArchive">
                                            Do you want to archive this application?
                                        </div>
                                        <div v-else class="text-success">Application Archived</div>
                                    </div>
                                    <div class="modal-footer">
                                        <button
                                            type="button"
                                            class="light-button"
                                            data-bs-dismiss="modal"
                                            @click="isArchive = false"
                                        >
                                            Close
                                        </button>
                                        <button
                                            type="button"
                                            class="blue-button"
                                            :disabled="isArchive"
                                            @click="archive()"
                                        >
                                            Archive
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Download Applciation -->
                    <div class="mt-3" :hidden="userType != 'admin' && userType != 'approver'">
                        <button
                            class="light-button"
                            data-bs-toggle="modal"
                            data-bs-target="#download"
                            @click="hidden = true"
                        >
                            Download Application
                        </button>

                        <div
                            id="download"
                            class="modal fade"
                            tabindex="-1"
                            role="dialog"
                            aria-labelledby="exampleModalLabel"
                            aria-hidden="true"
                        >
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 id="exampleModalLabel" class="modal-title text-dark">
                                            Download Application
                                        </h5>
                                        <button
                                            type="button"
                                            class="close"
                                            data-bs-dismiss="modal"
                                            aria-label="Close"
                                        >
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body text-dark">
                                        Do you want to download the application?
                                    </div>
                                    <div class="modal-footer">
                                        <button
                                            type="button"
                                            class="light-button"
                                            data-bs-dismiss="modal"
                                            @click="hidden = false"
                                        >
                                            Close
                                        </button>
                                        <button
                                            type="button"
                                            class="blue-button"
                                            data-bs-dismiss="modal"
                                            @click="generatePDF"
                                        >
                                            Download
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Send PDF to vendor -->
                    <div class="mt-3" :hidden="userType != 'approver' || status != 'Completed'">
                        <button
                            class="light-button"
                            :disabled="submited"
                            data-bs-toggle="modal"
                            data-bs-target="#export"
                            @click="hidden = true"
                        >
                            Send PDF
                        </button>

                        <div
                            id="export"
                            class="modal fade"
                            tabindex="-1"
                            role="dialog"
                            aria-labelledby="exampleModalLabel"
                            aria-hidden="true"
                        >
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 id="exampleModalLabel" class="modal-title text-dark">
                                            Send Application to Vendor
                                        </h5>
                                        <button
                                            type="button"
                                            class="close"
                                            data-bs-dismiss="modal"
                                            aria-label="Close"
                                            @click="closePDFModal"
                                        >
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body text-dark">
                                        <div class="container">
                                            <div v-if="!isSent">
                                                <h6>Message for the vendor</h6>
                                                <textarea
                                                    v-model="emailMessage"
                                                    class="w-100 mt-3 px-2"
                                                    rows="10"
                                                />
                                            </div>

                                            <h6
                                                v-else-if="
                                                    isSent && !isSuccessSent && !isFailedSent
                                                "
                                                class="text-center text-warning"
                                            >
                                                Sending the application. Please wait.
                                            </h6>

                                            <h6
                                                v-if="isSuccessSent"
                                                class="px-3 text-center text-success m-0"
                                            >
                                                PDF sent successfully!
                                            </h6>
                                            <h6
                                                v-if="isFailedSent"
                                                class="px-3 text-center text-danger m-0"
                                            >
                                                Failed to send PDF
                                            </h6>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button
                                            type="button"
                                            class="light-button"
                                            data-bs-dismiss="modal"
                                            @click="closePDFModal"
                                        >
                                            Close
                                        </button>
                                        <button
                                            type="button"
                                            class="blue-button"
                                            :disabled="isSent"
                                            @click="sendPDF"
                                        >
                                            Send
                                        </button>
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
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'
import 'jspdf-autotable'
import html2pdf from 'html2pdf.js'
export default {
    data() {
        return {
            userName: '',
            calculationForm: false,
            formName: '',
            formId: '',
            applicationId: '', //Swapped to dynamic (Accessible via the btn on adminDashboard) 79ec03aa-bd58-11ed-afa1-0242ac120002
            userId: '',
            userType: '',
            companyName: '',
            status: '',
            dateCreated: '',
            comments: 'No comments at the moment',
            inputs: [],
            required: [],
            formData: {},
            currentStep: '',
            submited: false,
            hidden: false,
            doc: '',
            email: '',
            isSave: false,
            isSubmit: false,
            isArchive: false,
            emailMessage: 'Hi vendor, this is the approved application.',
            errorhandling: [],
            isSuccessSent: false,
            isFailedSent: false,
            isSent: false,
            isRejectSuccess: false,
            optionsNum: 0
        }
    },
    async beforeMount() {
        // Retrieve userName by referencing LocalStorage
        const userId = localStorage.getItem('userID')
        this.userId = userId
        const userInfo = await (await axios.get(`http://localhost:8080/api/users/${userId}`)).data

        //userName
        this.userName = userInfo.name
        //userEmail
        this.email = userInfo.email

        //retrieve applicationId from route
        this.applicationId = this.$route.params.applicationId

        //get application
        let aId = this.applicationId
        let application = await axios.get(
            `http://localhost:8080/api/applications/getFullForm/${aId}`
        )
        //Application formId
        this.formId = application.data[0].formId
        //Application formname
        this.formName = application.data[0].formName
        //check if this is the subcontractor evaluation form
        if (
            this.formName == 'SUBCONTRACTOR’S SAFETY & HEALTH PERFORMANCE EVALUATION' ||
            this.formName == "SUBCONTRACTOR'S SAFETY & HEALTH PERFORMANCE EVALUATION"
        ) {
            this.calculationForm = true
        }
        //Application status
        this.status = application.data[0].applicationStatus
        //Application date created
        this.dateCreated = new Date(application.data[0].dateCreated).toLocaleString('en-SG', {
            day: 'numeric',
            month: 'numeric',
            year: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            hour12: false
        })
        //currentStep of Application
        this.currentStep = application.data[0].currentStep
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
                switch (canvaComponent.type) {
                    case 'Text Only':
                    case 'Address':
                    case 'text':
                        inputComponentObject['type'] = 'text'
                        canvaComponent.type = 'text'
                        break
                    case 'Numbers Only':
                    case 'Phone Number':
                    case 'integer':
                        inputComponentObject['type'] = 'integer'
                        canvaComponent.type = 'integer'
                        break
                    case 'Date Picker':
                        inputComponentObject['type'] = 'date'
                        canvaComponent.type = 'date'
                        break
                    case 'Time Picker':
                        inputComponentObject['type'] = 'time'
                        canvaComponent.type = 'time'
                        break
                    case 'Drop-Down Menu':
                    case 'dropdown':
                        inputComponentObject['type'] = 'dropdown'
                        canvaComponent.type = 'dropdown'
                        break
                    case 'Radio Button':
                        inputComponentObject['type'] = 'radio'
                        canvaComponent.type = 'radio'
                        break
                    case 'Check Box':
                    case 'checkbox':
                        inputComponentObject['type'] = 'checkbox'
                        canvaComponent.type = 'checkbox'
                        break
                    case 'Email':
                        inputComponentObject['type'] = 'email'
                        canvaComponent.type = 'email'
                        break
                }
                //calculate the total number of options
                if (canvaComponent.type == 'radio') {
                    this.optionsNum += canvaComponent.optionPrompt.length
                }
                inputComponentObject['required'] = canvaComponent.required
                let inputComponentOptions = canvaComponent.optionPrompt
                //add the possible choices of the input component in an array
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
                    let key = `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                    this.formData[key] =
                        value != null && value != '' && value != undefined
                            ? value.indexOf(',') != -1
                                ? canvaComponent.value.split(',')
                                : [value]
                            : []
                } else {
                    let key = `${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`
                    this.formData[key] = canvaComponent.value
                }
                inputComponent.push(inputComponentObject)
            }
            //append the inputcomponent to subcanvas and add subccanvas to array
            dict['inputComponent'] = inputComponent
            input.push(dict)
        }
        this.inputs = input

        // Dynamically retrieve usertype using uId
        let uId = this.userId
        let user = await axios.get(`http://localhost:8080/api/users/${uId}`)
        this.userType = user.data.role

        //check the current assignee user type of the application
        let assignedType = await axios.get(`http://localhost:8080/api/applications/assignee/${aId}`)
        if (assignedType.data != this.userType) {
            this.submited = true
        }

        //get unrestrcited subcanvas. returns an array of subcanvas that needs to be filled up
        let assignedApplications = await axios.get(
            `http://localhost:8080/api/applications/${this.currentStep}/${this.formId}`
        )
        let data = assignedApplications.data[0].canvaFillUpNotRestricted
        for (let canvaId of data) {
            this.required.push(canvaId)
        }
    },
    methods: {
        //send pdf to vendors
        async sendPDF() {
            this.isSent = true
            let companyName = this.companyName
            let formName = this.formName
            let email = this.email
            const today = new Date()
            const options = { year: 'numeric', month: 'numeric', day: 'numeric' }
            const formattedDate = today.toLocaleDateString('en-US', options)
            let documentName = formattedDate + '_' + companyName + '_' + formName
            const element = document.getElementById('htmlContent')
            const pdf = html2pdf().from(element)
            let pdfBlob = await pdf.output('blob')
            this.hidden = false
            let formDatas = new FormData()
            formDatas.append('pdf', pdfBlob, documentName + '.pdf')
            formDatas.append('email', 'cheng.wee1998@gmail.com')
            formDatas.append('message', this.emailMessage)
            let response = await fetch('http://localhost:8080/api/email/sendpdf', {
                method: 'POST',
                body: formDatas
            })
            console.log(response)
            if (response.ok) {
                this.isSuccessSent = true
            } else {
                this.isFailedSent = true
            }
        },
        //generate PDF for local download
        generatePDF() {
            let companyName = this.companyName
            let formName = this.formName
            const today = new Date()
            const options = { year: 'numeric', month: 'numeric', day: 'numeric' }
            const formattedDate = today.toLocaleDateString('en-US', options)
            let documentName = formattedDate + '_' + companyName + '_' + formName
            const element = document.getElementById('htmlContent')

            html2pdf()
                .from(element)
                .save(documentName + '.pdf')
            this.hidden = false
        },
        //real time calculate subcanvas score for subcontractor evaluation form
        calculateSubform(canvaId) {
            let formData = this.formData
            let result = 0
            for (let key in formData) {
                //key is the combination of canvasID and componentID
                let arraykey = key.split(',')
                if (canvaId == arraykey[1] && arraykey[2] == 'radio' && formData[key] != null) {
                    result += parseInt(formData[key].split(',')[1]) + 1
                }
            }

            return result
        },
        //real time calculate total score for subcontractor evaluation form
        calculateTotalPoints() {
            let formData = this.formData
            let totalPoints = 0
            let result = []
            let performance = ''
            for (let key in formData) {
                //key is the combination of canvasID and componentID
                let arraykey = key.split(',')
                if (arraykey[2] == 'radio' && formData[key] != null) {
                    totalPoints += parseInt(formData[key].split(',')[1]) + 1
                }
            }
            result.push(totalPoints)
            const rating = (totalPoints / this.optionsNum) * 100
            if (rating <= 39) {
                performance = 'Poor'
            } else if (rating <= 49) {
                performance = 'Below Average'
            } else if (rating <= 64) {
                performance = 'Average'
            } else if (rating <= 84) {
                performance = 'Above Average'
            } else {
                performance = 'Good'
            }
            result.push(rating)
            result.push(performance)
            return result
        },
        //reset status after send pdf modal is close
        closePDFModal() {
            this.hidden = false
            this.isSuccessSent = false
            this.isSent = false
            this.isFailedSent = false
        },
        //close reject modal after rejecting the application
        closeRejectModal() {
            this.isRejectSuccess = false
            location.reload()
        },
        //feedback to user after saving or submitting form
        showAndRemovePopUp(action) {
            if (action == 'save') {
                this.isSave = true
                window.scrollTo({
                    top: 200,
                    behavior: 'smooth'
                })
                setTimeout(() => {
                    this.isSave = false
                }, 3000) // 3 seconds
            } else {
                this.isSubmit = true
                window.scrollTo({
                    top: 200,
                    behavior: 'smooth'
                })
                setTimeout(() => {
                    this.isSubmit = false
                    location.reload()
                }, 3000) // 3 seconds
            }
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
                    if (
                        ((type === 'checkbox' && Object.keys(value).length === 0) ||
                            (type !== 'checkbox' && !(value ?? '').trim())) &&
                        !errorMsg.includes('Please enter all required fields')
                    ) {
                        errorMsg.push('Please enter all required fields')
                    } else if (type === 'email') {
                        const emailRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
                        if (!emailRegex.test(value)) {
                            errorMsg.push('Please enter a valid email address')
                        }
                    } else if (type === 'integer' || type === 'tel') {
                        if (!this.containsOnlyNumbers(value)) {
                            errorMsg.push(
                                'Please enter a numeric value in the ' + question + ' field'
                            )
                        }
                    }
                }
            }

            if (errorMsg.length != 0) {
                this.errorhandling = errorMsg
                window.scrollTo({
                    top: 250,
                    behavior: 'smooth'
                })
                errorMsg = []
                return false
            } else {
                errorMsg = []
                return true
            }
        },
        //generate value json to post to back end for saving and submiting
        generateJSONforSavingAndSubmit() {
            let formData = this.formData
            let result = []
            //craft json body to post value to responseValue table
            for (let key in formData) {
                let dict = {}
                //key is the combination of canvasID and componentID
                let arraykey = key.split(',')
                if (Array.isArray(formData[key])) {
                    let empty = formData[key].join(' ').trim()
                    let real = empty.split(' ')
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
            return result
        },
        //save application
        save() {
            let aId = this.applicationId
            const result = this.generateJSONforSavingAndSubmit()
            if (this.status == 'NotStarted') {
                //change status to InProgress if vendor and Pending if admin
                axios
                    .put(`http://localhost:8080/api/applications/Save/${aId}`)
                    .then(function (response) {
                        console.log(response)
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            }
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
            this.showAndRemovePopUp('save')
        },
        //submit application
        submitForm() {
            if (this.validateForm()) {
                let aId = this.applicationId
                const result = this.generateJSONforSavingAndSubmit()
                axios
                    .put(`http://localhost:8080/api/applications/Submit/${aId}`)
                    .then(function (response) {
                        //if next step requires vendor to take action, send an email
                        if (response.data[0] == 'vendor') {
                            const emaildata = {
                                toEmail: response.data[1],
                                subject:
                                    'Quantum Leap Application ' + aId + '(Requires further action)',
                                body:
                                    "Hi vendor, the application with the ID '" +
                                    aId +
                                    "' requires further action from you."
                            }
                            try {
                                const response = axios.post(
                                    'http://localhost:8080/api/emailrequest',
                                    emaildata
                                )
                                console.log('it works', response.data)
                            } catch (error) {
                                console.log(error)
                            }
                        }
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
                this.showAndRemovePopUp('submit')
            }
        },
        //reject application
        reject() {
            let aId = this.applicationId
            const comm = this.comments
            axios
                .put(`http://localhost:8080/api/applications/reject/${aId}`, {
                    comments: this.comments
                })
                .then(function (response) {
                    if (response.data[0] == 'vendor') {
                        const emaildata = {
                            toEmail: response.data[1],
                            subject:
                                'Quantum Leap Application ' + aId + '(Requires further action)',
                            body:
                                "Hi vendor, the application with the ID '" +
                                aId +
                                "' requires further action from you. Comments from admin: " +
                                comm
                        }
                        try {
                            const response = axios.post(
                                'http://localhost:8080/api/emailrequest',
                                emaildata
                            )
                            console.log(response.data)
                        } catch (error) {
                            console.log(error)
                        }
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })
            this.isRejectSuccess = true
        },
        //archive application
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
            this.isArchive = true
        }
    }
}
</script>
