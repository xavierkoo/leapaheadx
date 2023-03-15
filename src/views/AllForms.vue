<template>
    <div class="container p-4 text-white" style="background-color: #142442;">
        <div class="row">
            <h3 class="col-6">My Dashboard </h3>
            <button @click="()=>isAssign = !isAssign" type="button" class="col-2 me-5 btn btn-light">Assign</button>
            <button type="button" class="col-2 btn btn-info">New Flow</button>
        </div>
        <div class="overlay" v-if="isAssign">
            <AssignWorkflow @isClose = "()=>isAssign = !isAssign"/>
        </div>
        <div class="rounded-4 p-5 mt-4" style="background-color: #0F1726;">
  
          <h6 > All Form/Workflows Templates </h6>
          <table class="table text-white" border="0">
            <thead>
              <tr style="color: grey;">
                <th scope="col">Name</th>
                <th scope="col">Date Created</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in data" :key="item.id">
                <th>
                  <span style="font-weight: 500;">{{ item.name }}</span><br>
                  <span style="font-size: smaller; color: grey; font-weight: 400;">{{ item.formUuid }}</span>
                </th>
                <td class="align-middle" >{{item.dateCreated}}</td>
  
                <td class="align-middle">
                    <button class="rounded bg-white me-4" ><img  src="../assets/images/editIcon.png" height="25"></button>
                    <button class="rounded bg-white" ><img  src="../assets/images/deleteIcon.png" height="25"></button>
                </td>
              </tr>
            </tbody>
          </table>
  
        </div>
    </div>
  </template>

<script setup>

import axios from 'axios';
import { ref, onMounted } from 'vue';
import AssignWorkflow from './AssignWorkflow.vue';


const data = ref([]);
const isAssign = ref(false);


onMounted(async () => {
  const response = await axios.get('http://localhost:8080/api/formWorkflows');
  data.value = response.data;


});




</script>

<style>
  table, th, td {
    border: none;
  }
  .overlay {   

    position: absolute;
    top:0;
    left:0;
    height: 100%;
    width: 100%;
    background-color: rgba(255, 255, 255, 0.3); /* add tint as background */
    z-index: 1; /* make sure it's on top of other elements */
}
</style>