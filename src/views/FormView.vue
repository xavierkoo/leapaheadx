<template>
    <form>
        <!-- iterate through an array of subcanvas object -->
        <div v-for="(canva, index) in inputs" :id="canva.canvaId" :key="index">
            <br />
            <label :for="canva.name">{{ canva.canvaName }}</label>
            <!-- iterate through an array of input components in each sub canvas -->
            <div v-for="(component, ind) in canva.inputComponent" :key="ind">
                <br />
                <label :for="component.question">{{ component.question }}</label> <br />
                <!--Use dropdown if component type is dropdown-->
                <select
                    v-if="component.type === 'dropdown'"
                    :id="component.componentId"
                    v-model="formData[`${component.componentId},${canva.canvaId}`]"
                    :name="component.question"
                    :disabled="!required.includes(canva.canvaId)"
                    @change="log()"
                >
                    <option v-for="(option, i) in component.options" :key="i" :value="option.value">
                        {{ option.value }}
                    </option>
                </select>
                <!--Use checkbox if component type is checkbox-->
                <div v-else-if="component.type === 'checkbox'">
                    <div v-for="(option, i) in component.options" :key="i">
                        <input
                            :id="`${component.componentId}-${i}`"
                            v-model="formData[`${component.componentId},${canva.canvaId}`]"
                            type="checkbox"
                            :name="component.question"
                            :value="option.value"
                            :checked="formData[`${component.componentId},${canva.canvaId}`]!=null && formData[`${component.componentId},${canva.canvaId}`].includes(option.value)" 
                            :disabled="!required.includes(canva.canvaId)"
                        />
                        <label :for="`${component.componentId}-${i}`">{{ option.value }}</label>
                    </div>
                </div>
                <!--Use radiobutton if component type is radio-->
                <div v-else-if="component.type === 'radio'">
                    <div v-for="(option, i) in component.options" :key="i">
                        <input
                            :id="`${component.componentId}-${i}`"
                            v-model="formData[`${component.componentId},${canva.canvaId}`]"
                            type="radio"
                            :name="component.question"
                            :value="option.value"
                            :disabled="!required.includes(canva.canvaId)"
                        />
                        <label :for="`${component.componentId}-${i}`">{{ option.value }}</label>
                    </div>
                </div>
                <!--Use normal input if component type is integer or text-->
                <input
                    v-else
                    v-model="formData[`${component.componentId},${canva.canvaId}`]"
                    :type="component.type"
                    :name="component.question"
                    :disabled="!required.includes(canva.canvaId)"
                />
            </div>
        </div>
        <button :disabled="submited" @click="submit()">Submit</button>
        <button :disabled="submited" @click="save()">Save</button>
        <button
            :disabled="submited"
            :hidden="usertype != 'admin' && usertype != 'approver'"
            @click="reject()"
        >
            Reject
        </button>
    </form>
</template>

<script>
import axios from 'axios'
import { useRoute } from 'vue-router'
export default {
    data() {
        return {
            formName: '',
            applicationId: '', //Swapped to dynamic (Accessible via the btn on adminDashboard)
            userId: '79ebaad6-bd58-11ed-afa1-0242ac120002', //supposed to be dynamic
            usertype: 'vendor', //supposed to be dynamic
            status: '',
            dateCreated: '',
            comments: '',
            inputs: [],
            required: [],
            formData: {},
            submited: false
        }
    },
    async beforeMount() {
        //determine if form is editable by current user based on current step

        // Dynamically retrieve applicationId from routing
        const route = useRoute()
        this.applicationId = route.params.applicationId

        let aId = this.applicationId
        let uId = this.userId
        let assignedType = await axios.get(`http://localhost:8080/api/applications/assignee/${aId}`)
        if (assignedType.data != this.usertype) {
            console.log(assignedType)
            this.submited = true
        }

        //get unrestrcited subcanvas returns an array of subcanvas that needs to be filled up
        let assignedApplications = await axios.get(
            `http://localhost:8080/api/applications/user/${uId}`
        )
        let data = assignedApplications.data
        for (let app of data) {
            if (app.applicationID === this.applicationId) {
                for (let canvaId of app.canvaFillUpNotRestricted) {
                    this.required.push(canvaId)
                }
            }
        }


	//get application
	let application = await axios.get(
	`http://localhost:8080/api/applications/getFullForm/${aId}`
	);
	//Application formname
	this.formName = application.data[0].formName;
	//Application status
	this.status = application.data[0].applicationStatus;
	//Application date created
	this.dateCreated = application.data[0].date
	//Application comments
	this.comments = application.data[0].comments;
	//Array of canvas
	let canvas = application.data[0].canva;
	let input = [];
	//iterate the subcanvases in the application
	for(let canva of canvas){
		let dict ={};
		//for each subcanvas, get canvasId, canvasName and the input components in the subcanvas
		dict["canvaId"] = canva.canvasId;
		dict["canvaName"] = canva.canvasName;
		let canvaComponents = canva.canvaComponent;
		let inputComponent = [];
		//iterate the input components in the subcanvas
		for(let canvaComponent of canvaComponents){
		let inputComponentObject ={};
		//for each input component, get componentId, qomponent question, component type and the options for each component if it exist (checkbox, dropdown and radio)
		inputComponentObject["componentId"] = canvaComponent.componentId;
		inputComponentObject["question"] = canvaComponent.question;
		inputComponentObject["type"] = canvaComponent.type;
		let inputComponentOptions = canvaComponent.optionPrompt;
		//add the possible choices in an array 
		let inputComponentOptionArray =[];
		for(let inputComponentOption of inputComponentOptions){
			let inputComponentOptionObject = {};
			inputComponentOptionObject["value"]=inputComponentOption;
			inputComponentOptionArray.push(inputComponentOptionObject);
		}
		inputComponentObject["options"]= inputComponentOptionArray;
		//get the value saved or submited. If component type is a checkbox, need to return an array
		if(canvaComponent.type == "checkbox"){
			let value = canvaComponent.value;
            if(value!=null){
                if(value.indexOf(",")!=-1){
                    let checkBoxArray = canvaComponent.value.split(",");
                    console.log(checkBoxArray);
                    this.formData[`${canvaComponent.componentId},${canva.canvasId}`]=checkBoxArray;
                }else{
                    this.formData[`${canvaComponent.componentId},${canva.canvasId}`]=[value];
                }
            }
		}else{
			this.formData[`${canvaComponent.componentId},${canva.canvasId}`]=canvaComponent.value;
		}
		inputComponent.push(inputComponentObject);
		}
		//append the inputcomponet to subcanvas and add subccanvas to array 
		dict["inputComponent"]=inputComponent;
		input.push(dict);
	}
	this.inputs=input;
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
	//save application
	save(){
		let aId = this.applicationId;
		let formData = this.formData;
		let result = [];
			//craft json body to post value to responseValue table
			for(let key in formData){
				let dict={};
				//key is the combination of canvasID and componentID
				let arraykey = key.split(",");
				if(Array.isArray(formData[key])){
					let checkboxString = formData[key].join();
					dict["value"] = checkboxString;
					console.log(checkboxString)
				}else{
					dict["value"] = formData[key];
					console.log(formData[key])
				}
				dict["componentUuid"]=arraykey[0];
				dict["applicationUuid"]=this.applicationId;
				dict["canvasUuid"]=arraykey[1];
				result.push(dict);
			}
			console.log(result);
		if(this.usertype == "vendor"){
			//change status of application only
			axios.put(`http://localhost:8080/api/applications/vendorSave/${aId}`)
			.then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
			
			//post crafted json to database
			axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
				.then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
		}else{
			//post crafted json to database
			axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
				.then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
		}
	},
	submit(){
		let aId = this.applicationId;
		let formData = this.formData;
			let result = [];
			//craft json body to post value to responseValue table
			for(let key in formData){
				let dict={};
				//key is the combination of canvasID and componentID
				let arraykey = key.split(",");
				if(Array.isArray(formData[key])){
					let checkboxString = formData[key].join();
					dict["value"] = checkboxString;
				}else{
					dict["value"] = formData[key];
				}
				dict["value"] = formData[key];
				dict["componentUuid"]=arraykey[0];
				dict["applicationUuid"]=this.applicationId;
				dict["canvasUuid"]=arraykey[1];
				console.log(dict);
				result.push(dict);
			}
		if(this.usertype == "vendor"){
			//change status and step number of application and update values in response value table
			axios.put(`http://localhost:8080/api/applications/vendorSubmit/${aId}`)
			.then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
			axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
		}else if(this.usertype == "admin"){
			//change status and step number of application and update values in response value table
			axios.put(`http://localhost:8080/api/applications/adminSubmit/${aId}`)
			.then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
			axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
		}else{
			//change status and step number of application and update values in response value table
			axios.put(`http://localhost:8080/api/applications/approverApprove/${aId}`).then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
			axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
		}
	},
	reject(){
		let aId = this.applicationId;
		if(this.usertype == "admin"){
			axios.put(`http://localhost:8080/api/applications/adminReject/${aId}`).then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
		}else{
			axios.put(`http://localhost:8080/api/applications/approverReject/${aId}`).then(function (response) {
					console.log(response);
				})
				.catch(function (error) {
					console.log(error);
				});
		}
	}
}
}
</script>
