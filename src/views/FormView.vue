<template>
	<div class="container formRender">
		<div class="row pt-5 pb-3">
		  <h4 class="col-sm-3 text-center text-sm-start">{{formName}}</h4>
		  <div class="col-4"></div>
		  <h5 class="col-sm-5 text-center text-sm-end">{{companyName}}</h5>
		</div>
		<div class="row mx-1">
			<div class="col-lg-3 pt-4 outerBlack order-lg-3">
				<div class="row mx-3">
					<h6 class="text-center">Application Info</h6>
					<div class="px-3 mt-3 mb-2 rightBarLabel">Status</div>
					<h6 class="px-3 ">{{status}}</h6>
					<div class="px-3 mt-3 mb-2 rightBarLabel">Date Created</div>
					<h6 class="px-3 ">{{dateCreated}}</h6>
					<div class="px-3 mt-3 mb-2 rightBarLabel">Comment</div>
					<h6 class="px-3 ">{{comments}}</h6>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="text-center my-1 col-6 col-sm-4 col-lg-12">
						<button
							class="rightBarBtn"
							:disabled="submited"
							:hidden="usertype != 'admin' && usertype != 'approver'"
							@click="remove()"
						>
							Delete
						</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="text-center my-1 col-6 col-sm-4 col-lg-12">
						<button
							class="rightBarBtn"
							:disabled="submited"
							:hidden="usertype != 'admin' && usertype != 'approver'"
						>
							View Form
						</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="text-center mt-1 mb-3 col-6 col-sm-4 col-lg-12 ">
						<button
							class="rightBarBtn"
							:disabled="submited"
							:hidden="usertype != 'admin' && usertype != 'approver'"
						>
							Export PDF
						</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
				</div>
			</div>
			<div class="col-12 col-lg-1 my-3 order-lg-2"></div>
			<form class=" col-lg-8 pt-4 pb-4 outerBlack order-lg-1" @submit.prevent="submitForm">
				<div class="row mx-3 py-4">
					<div class="col-sm-12 col-lg-6 text-center text-lg-start my-1">{{applicationId}}</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button
							class="normalBtn my-sm-1"
							:disabled="submited"
							:hidden="usertype != 'admin' && usertype != 'approver'"
							@click="reject()"
							>
							Reject
						</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button class="normalBtn my-1" :disabled="submited" @click="save()">Save</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button class="submitBtn my-1" type="submit" :disabled="submited">Submit</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
				</div>
				<div class=" row mx-sm-4 py-4 innerBlue">
					<!-- iterate through an array of subcanvas object -->
					<div v-for="(canva, index) in inputs" :id="canva.canvaId" :key="index">
						<!-- iterate through an array of input components in each sub canvas -->
						<div v-for="(component, ind) in canva.inputComponent" :key="ind" class="mx-sm-3 mt-3">
							<div class="px-sm-5 ">
								<label class="FormLabel mt-2 py-3 px-4 innerLabel w-100" :for="component.question">{{ component.question }}<span class="text-danger" :hidden="!required.includes(canva.canvaId)">{{component.required?"*":""}}</span></label> <br/>
								<!--Use dropdown if component type is dropdown-->
								<div class="innerInputs px-4 py-4">
								<select
									v-if="component.type === 'dropdown'"
									:id="component.componentId"
									v-model="formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`]"
									class="w-100"
									:name="component.question"
									:disabled="!required.includes(canva.canvaId)"
									:required="component.required"
									@change="log()"
								>
									<option v-for="(option, i) in component.options" :key="i" :value="option.value">
										{{ option.value }}
									</option>
								</select>
								
								<!--Use checkbox if component type is checkbox-->
								<div v-else-if="component.type === 'checkbox'" >
									<div v-for="(option, i) in component.options" :key="i">
										<input
											:id="`${component.componentId}-${i}`"
											v-model="formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`]"
											type="checkbox"
											class="custom-control-input"
											:name="component.question"
											:value="option.value"
											:checked="formData[`${component.componentId},${canva.canvaId}`]!=null && formData[`${component.componentId},${canva.canvaId}`].includes(option.value)" 
											:disabled="!required.includes(canva.canvaId)"
											:required="component.required"
										/>
										<label class="custom-control-label optionLabel" :for="`${component.componentId}-${i}`">{{ option.value }}</label>
									</div>
								</div>
								<!--Use radiobutton if component type is radio-->
								<div v-else-if="component.type === 'radio'">
									<div v-for="(option, i) in component.options" :key="i">
										<input
											:id="`${component.componentId}-${i}`"
											v-model="formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`]"
											class="custom-control-input"
											type="radio"
											:name="component.question"
											:value="option.value"
											:disabled="!required.includes(canva.canvaId)"
											:required="component.required"
										/>
										<label class="custom-control-label optionLabel" :for="`${component.componentId}-${i}`">{{ option.value }}</label>
									</div>
								</div>
								<!--Use normal input if component type is integer or text-->
									<input
										v-else
										v-model="formData[`${component.componentId},${canva.canvaId},${component.type},${component.question}`]"
										class="form-control"
										:type="component.type"
										:name="component.question"
										:disabled="!required.includes(canva.canvaId)"
										:required="component.required"
									/>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row mx-3 py-3">
					<div class="col-lg-6"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button
							class="normalBtn my-sm-1"
							:disabled="submited"
							:hidden="usertype != 'admin' && usertype != 'approver'"
							@click="reject()"
							>
							Reject
						</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button class="normalBtn my-1" :disabled="submited" @click="save()">Save</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
					<div class="col-6 col-sm-4 col-lg-2 text-end">
						<button class="submitBtn my-1" type="submit" :disabled="submited">Submit</button>
					</div>
					<div class="col-3 col-sm-4 d-lg-none"></div>
				</div>
			</form>
		</div>
	</div>
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
            companyName: '',
			status: '',
            dateCreated: '',
            comments: 'None at the moment',
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
	this.dateCreated = application.data[0].dateCreated;
	//Company Name 
	this.companyName = application.data[0].companyName;
	//Application comments
	if(application.data[0].comments == null){
		this.comments = "No comments at the moment";
	}else{
		this.comments =application.data[0].comments;
	}
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
			//for each input component, get componentId, qomponent question, required, component type and the options for each component if it exist (checkbox, dropdown and radio)
			inputComponentObject["componentId"] = canvaComponent.componentId;
			inputComponentObject["question"] = canvaComponent.question;
			inputComponentObject["type"] = canvaComponent.type;
			inputComponentObject["required"] = canvaComponent.required;
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
						this.formData[`${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`]=checkBoxArray;
					}else{
						this.formData[`${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`]=[value];
					}
				}
			}else{
				this.formData[`${canvaComponent.componentId},${canva.canvasId},${canvaComponent.type},${canvaComponent.question}`]=canvaComponent.value;
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
	containsOnlyNumbers(str) {
  		return /^\d+$/.test(str);
	},
	//validate form
	validateForm() {
		let errorMsg = [];
		let data = this.formData;
		for(let keys in data){
			let keysArray = keys.split(",");
			let canvaId = keysArray[1];
			let type = keysArray[2];
			let question = keysArray[3];
			let value = data[keys];
			// Check if the required fields are not empty
			if(this.required.includes(canvaId)){
				// Check if the email is valid
				if(type=="email"){
					const emailRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
					if (!emailRegex.test(value)) {
						if(!errorMsg.includes("Please enter a valid email address")){
							errorMsg.push('Please enter a valid email address');
						}
					}
				}
				//check if input is numeric
				if(type=="integer"){
					if(!this.containsOnlyNumbers(value)){
						errorMsg.push('Please enter a numeric value in the '+ question +" field");
					}
				}
			}
		}
				
		if(errorMsg.length!=0){
			let errorStr = "";
			for(let msg of errorMsg){
				errorStr += msg +"\n";
			}
			alert(errorStr);
			return false;
		}else{
			return true;
		}
		
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
		alert("Form successfully saved")
	},
	submitForm(){	
		if (this.validateForm()) {
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
				.then(function (response) {
						console.log(response);
					})
					.catch(function (error) {
						console.log(error);
					});
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
				.then(function (response) {
						console.log(response);
					})
					.catch(function (error) {
						console.log(error);
					});
			}else{
				//change status and step number of application and update values in response value table
				axios.put(`http://localhost:8080/api/applications/approverApprove/${aId}`)
				.then(function (response) {
						console.log(response);
					})
				.catch(function (error) {
						console.log(error);
					});
				axios.post('http://localhost:8080/api/applicationResponseValues/saveSpecificResponse', result)
				.then(function (response) {
						console.log(response);
					})
					.catch(function (error) {
						console.log(error);
					});
			}
			alert("Form successfully submitted")
		}
	},
	reject(){
		let aId = this.applicationId;
		if(this.usertype == "admin"){
			axios.put(`http://localhost:8080/api/applications/adminReject/${aId}`)
			.then(function (response) {
					console.log(response);
				})
			.catch(function (error) {
					console.log(error);
				});
		}else{
			axios.put(`http://localhost:8080/api/applications/approverReject/${aId}`)
			.then(function (response) {
					console.log(response);
				})
			.catch(function (error) {
					console.log(error);
				});
		}
	},
	remove(){
		let aId = this.applicationId;
		axios.delete(`http://localhost:8080/api/applicationResponseValues/application/${aId}`)
		.then(function (response){
			console.log(response);
		})
		.catch(function (error) {
			console.log(error);
		});

		axios.delete(`http://localhost:8080/api/applications/${aId}`)
		.then(function (response){
			console.log(response);
		})
		.catch(function (error) {
			console.log(error);
		});
	}
}
}
</script>
