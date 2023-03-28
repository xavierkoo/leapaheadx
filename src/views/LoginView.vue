<template>
    <div class="login">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-1 mb-2" />
                <img
                    src="#"
                    alt=""
                    style="object-fit: contain"
                    class="col-md-5 d-none d-md-block"
                />
                <div class="col-md-5 container back px-5 mb-2">
                    <div class="row display-6 mt-4">
                        <h3 class="fw-bold">Login</h3>
                    </div>
                    <div class="row my-2">
                        <form>
                            <div class="form-group my-3">
                                <label for="email">Email <span class="text-danger">*</span></label>
                                <input
                                    id="email"
                                    v-model="email"
                                    type="email"
                                    class="form-control"
                                />
                                <!-- <span v-if="v$.email.$error" class="text-danger">
                                    {{ invalidEmail }}
                                </span> -->
                                <!-- <span v-else-if="(isEmailIn == false) & (isInvalid == true)">{{
                                    errMsgEmail
                                }}</span> -->
                            </div>
                            <div class="form-group my-3">
                                <label for="password"
                                    >Password <span class="text-danger">*</span></label
                                >
                                <input
                                    id="password"
                                    v-model="password"
                                    type="password"
                                    class="form-control"
                                />
                                <!-- <span v-if="v$.password.$error" class="text-danger">
                                    {{ errMsgPassword }}
                                </span> -->
                                <!-- <span v-else-if="isInvalid == true">{{ errMsgPassword }}</span> -->
                            </div>
                        </form>
                    </div>
                    <div class="row mt-4">
                        <div class="col-3" />
                        <div class="col-6">
                            <button type="submit" class="col-12" @click="login()">Log In</button>
                        </div>
                        <div class="col-3" />
                    </div>
                </div>
                <div class="col-md-1" />
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return { email: '', password: '' }
    },
    computed: {
        isValidEmail() {
            return /^[^@]+@\w+(\.\w+)+\w$/.test(this.email)
        }
    },
    methods: {
        async login() {
            // TODO - check if the fields are blank first
            // If not blank:
            // Do the below

            const url = `http://localhost:8080/api/auth/${this.email}/${this.password}`
            axios
                .post(url)
                .then((res) => {
                    console.log(res)
                    const data = res.data
                    if (res.data == '') {
                        alert('The user details is incorrect')
                    } else {
                        localStorage.setItem('userID', data[0])
                        localStorage.setItem('roleID', data[1])

                        this.getRole(data[0]).then(() => {
                            let role = localStorage.getItem('role')
                            console.log('You are now logged in')
                            console.log(localStorage.getItem('role') == 'admin')
                            if (role == 'admin' || role == 'approver') {
                                this.$router.replace({ path: '/adminDashboard' })
                            } else {
                                this.$router.replace({ path: '/' })
                            }
                        })
                    }
                })
                .catch((err) => {
                    console.error(err)
                })
        },

        async getRole(uId) {
            await axios
                .get(`http://localhost:8080/api/users/role/${uId}`)
                .then((res) => {
                    console.log(res)
                    localStorage.setItem('role', res.data)
                })
                .catch((err) => {
                    console.error(err)
                })
        }
    }
}
</script>

<style lang="scss" scoped></style>
