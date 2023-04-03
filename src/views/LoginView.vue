<template>
    <div class="login">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-3 mb-2" />
                <div class="col-md-6 px-5 mb-2">
                    <div class="mt-5">
                        <div class="row display-6 mt-4">
                            <h3 class="fw-bold">Login</h3>
                        </div>
                        <div class="row my-2">
                            <form>
                                <div class="form-group my-3">
                                    <label for="email"
                                        >Email <span class="text-danger">*</span></label
                                    >
                                    <input
                                        id="email"
                                        v-model="email"
                                        type="email"
                                        class="form-control"
                                    />
                                    <span v-if="emailErrorMessage" class="text-danger">{{
                                        emailErrorMessage
                                    }}</span>
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
                                    <span v-if="passwordErrorMessage" class="text-danger">{{
                                        passwordErrorMessage
                                    }}</span>
                                </div>
                                <span v-if="mainError" class="text-danger">{{ mainError }}</span>
                            </form>
                        </div>
                        <div class="row mt-4">
                            <div class="col-3" />
                            <div class="col-6">
                                <button type="submit" class="col-12 blue-button" @click="login()">
                                    Log In
                                </button>
                            </div>
                            <div class="col-3" />
                        </div>
                    </div>
                </div>
                <div class="col-md-3" />
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            email: '',
            password: '',
            emailErrorMessage: '',
            passwordErrorMessage: '',
            mainError: ''
        }
    },
    computed: {
        isValidEmail() {
            return /^[^@]+@\w+(\.\w+)+\w$/.test(this.email)
        }
    },
    watch: {
        email: function () {
            if (!this.isValidEmail) {
                this.emailErrorMessage = 'Invalid Email'
                setTimeout(() => {
                    this.emailErrorMessage = ''
                }, 2000)
            } else {
                this.emailErrorMessage = ''
            }
        },
        password: function () {
            if (this.password.trim() === '') {
                this.passwordErrorMessage = 'Password cannot be blank'
                setTimeout(() => {
                    this.emailErrorMessage = ''
                }, 2000)
            } else {
                this.passwordErrorMessage = ''
            }
        }
    },
    methods: {
        async login() {
            // TODO - check if the fields are blank first
            // If not blank:
            // Do the below
            if (this.isValidEmail == false || this.password == '') {
                this.mainError = 'Please enter the details correctly'
            } else {
                const url = `http://localhost:8080/api/auth/${this.email}/${this.password}`
                axios
                    .post(url)
                    .then((res) => {
                        console.log(res)
                        const data = res.data
                        if (res.data == '') {
                            this.mainError =
                                'The user details is incorrect or Your account is disabled'
                        } else {
                            localStorage.setItem('userID', data[0])
                            localStorage.setItem('roleID', data[1])

                            this.getRole(data[0]).then(() => {
                                let role = localStorage.getItem('role')
                                console.log('You are now logged in')
                                console.log(localStorage.getItem('role') == 'admin')
                                if (role == 'admin' || role == 'approver') {
                                    this.$router.replace({ path: '/adminDashboard' })
                                    setTimeout(() => {
                                        location.reload()
                                    }, 1000)
                                } else {
                                    this.$router.replace({ path: '/' })
                                    setTimeout(() => {
                                        location.reload()
                                    }, 1000)
                                }
                            })
                        }
                    })
                    .catch((err) => {
                        console.error(err)
                    })
            }
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
