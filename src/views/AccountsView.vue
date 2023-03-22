<template>
    <div>
        <!-- This is the dashboard header -->
        <div class="row mx-2 mx-sm-5 pad-d">
            <div class="col-12 col-sm-8 col-lg-5 col-xl-3">
                <h4>My Dashboard</h4>
            </div>
            <div class="d-none d-lg-block col-lg col-xl" />
            <div class="col text-start col-sm-4 col-lg-5 col-xl-3 text-sm-end">
                <!-- TODO - Change this to variable based on login -->
                <h5>Admin1</h5>
            </div>
        </div>

        <div class="row mx-2 mx-sm-5 pad-d">
            <div class="col dark-container pb-5">
                <!-- This is the Title + Btns -->
                <div class="row mx-sm-2 mx-lg-5">
                    <div class="col col-lg-5 pad-e"><h5>All Accounts</h5></div>
                    <div class="col-lg-1 col-xl-2" />

                    <!-- Create Toggle-->
                    <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                        <!-- New Account -->
                        <button
                            :class="{ 'blue-button': isHidden, 'dark-button': !isHidden }"
                            @click="isHidden = !isHidden"
                        >
                            <div v-if="isHidden == true">New Account</div>
                            <div v-else>Close</div>
                        </button>
                    </div>
                    <div class="col-6 col-lg-3 col-xl-2 pt-3 pt-sm-4">
                        <!-- Submit -->
                        <button v-if="isHidden == false" class="blue-button" @click="createAccount">
                            Create
                        </button>
                    </div>
                    <div class="d-none d-xl-block col-xl-1"></div>
                </div>

                <!-- Creation Model-->
                <div
                    v-if="!isHidden"
                    class="row mx-0 mx-sm-2 mx-lg-5 my-5 py-5 lightblue-container"
                >
                    <h5>Create a New Account</h5>
                    <div class="col-lg-6">
                        <!-- Left -->
                        <!-- Email -->
                        <div class="form-group pt-3">
                            <label for="email">Email</label>
                            <input id="email" v-model="email" class="form-control" type="text" />
                            <div v-if="error.includes('email')" class="small error-input">
                                Please make sure your email is not blank and is valid
                            </div>
                        </div>
                        <!-- Password -->
                        <div class="form-group pt-3">
                            <label for="password">Password</label>
                            <input
                                id="password"
                                v-model="password"
                                class="form-control"
                                type="password"
                            />
                            <div v-if="error.includes('password')" class="small error-input">
                                Please make sure your password is not blank
                            </div>
                        </div>
                        <!-- Role -->
                        <div class="form-group pt-3">
                            <label for="role">Role</label>
                            <select
                                id="role"
                                v-model="role"
                                class="form-select"
                                aria-label="Select a Role"
                                @change="onChange()"
                            >
                                <option disabled value="None" selected>Select a Role</option>
                                <option value="vendor">Vendor</option>
                                <option value="admin">Admin</option>
                                <option value="approver">Approver</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <!-- Right -->
                        <!-- Phone Number -->
                        <div class="form-group pt-3">
                            <label for="name">Contact Name</label>
                            <input id="name" v-model="name" class="form-control" type="text" />
                            <div v-if="error.includes('name')" class="small error-input">
                                Please make sure your name is not blank
                            </div>
                        </div>
                        <!-- Phone Number -->
                        <div class="form-group pt-3">
                            <label for="phoneNumber">Phone Number</label>
                            <input
                                id="phoneNumber"
                                v-model="phoneNumber"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('phoneNumber')" class="small error-input">
                                Please make sure your phoneNumber is not blank and is 10 digits
                            </div>
                        </div>
                    </div>
                    <h6 v-if="role != ''" class="pt-4">Additional Fields</h6>
                    <div v-if="role == 'vendor'" class="col-lg-6">
                        <!-- Company Name -->
                        <div class="form-group pt-3">
                            <label for="company">Company Name</label>
                            <input
                                id="company"
                                v-model="company"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('company')" class="small error-input">
                                Please make sure your company name is not blank
                            </div>
                        </div>

                        <!-- Country Selection -->
                        <div class="form-group pt-3">
                            <label for="country">Country</label>
                            <select
                                id="country"
                                v-model="country"
                                class="form-select"
                                aria-label="Select a Country"
                            >
                                <option disabled selected>Select a Country</option>
                                <option
                                    v-for="(aCountry, index) in allCountry"
                                    :key="index"
                                    :value="aCountry"
                                >
                                    {{ aCountry }}
                                </option>
                            </select>
                            <div v-if="error.includes('country')" class="small error-input">
                                Please make sure your country is not blank
                            </div>
                        </div>
                        <!-- businessNature -->
                        <div class="form-group pt-3">
                            <label for="businessNature">Nature of Business</label>
                            <input
                                id="businessNature"
                                v-model="businessNature"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('businessNature')" class="small error-input">
                                Please make sure your nature of business is not blank and is valid
                            </div>
                        </div>
                    </div>
                    <div v-if="role == 'vendor'" class="col-lg-6">
                        <!-- companyRegistry -->
                        <div class="form-group pt-3">
                            <label for="companyRegistry">Company Registration Number</label>
                            <input
                                id="companyRegistry"
                                v-model="companyRegistry"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('companyRegistry')" class="small error-input">
                                Please make sure your registration number is not blank and is
                                numbers only
                            </div>
                        </div>
                        <!-- gstNumber -->
                        <div class="form-group pt-3">
                            <label for="gstNumber">GST Number</label>
                            <input
                                id="gstNumber"
                                v-model="gstNumber"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('gstNumber')" class="small error-input">
                                Please make sure your GST Number is not blank and is numbers only
                            </div>
                        </div>
                    </div>
                    <div v-if="role == 'admin'" class="col-lg-6">
                        <!-- country -->
                        <div class="form-group pt-3">
                            <label for="country">Country</label>
                            <select
                                id="country"
                                v-model="country"
                                class="form-select"
                                aria-label="Select a Country"
                            >
                                <option disabled selected>Select a Country</option>
                                <option
                                    v-for="(aCountry, index) in allCountry"
                                    :key="index"
                                    :value="aCountry"
                                >
                                    {{ aCountry }}
                                </option>
                            </select>
                            <div v-if="error.includes('country')" class="small error-input">
                                Please make sure your country is not blank
                            </div>
                        </div>
                    </div>
                    <div v-if="role == 'admin'" class="col-lg-6">
                        <!-- department -->
                        <div class="form-group pt-3">
                            <label for="department">Department</label>
                            <input
                                id="department"
                                v-model="department"
                                class="form-control"
                                type="text"
                            />
                            <div v-if="error.includes('department')" class="small error-input">
                                Please make sure your department is not blank
                            </div>
                        </div>
                    </div>
                </div>

                <!-- This is the Header Row -->
                <div class="row mx-0 mx-sm-2 mx-lg-5 pad-d">
                    <div class="col-6 col-sm-8 col-lg-6 tableHeader">Name</div>
                    <div class="d-none d-sm-block col-sm-4 col-lg-3 tableHeader">Role</div>
                    <div class="d-none d-xl-block col-xl-1" />
                    <div class="d-none d-lg-block col-lg-3 col-xl-2 tableHeader">Actions</div>
                </div>

                <!-- This is the For-loop of all the records-->
                <div
                    v-for="item in allAccounts"
                    :key="item.id"
                    class="row tableRow justify-content-center align-items-center mx-sm-2 mx-lg-5 pad-e"
                >
                    <div class="col-sm-8 col-lg-6">
                        <div class="row justify-content-center align-items-center">
                            <div class="col-12">{{ item.name }}</div>
                            <div class="col-12 tableCaption">{{ item.uid }}</div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-lg-3" style="text-transform: capitalize">
                        <p>{{ item.role }}</p>
                    </div>
                    <div class="d-none d-xl-block col-xl-1" />

                    <!-- Rendering of the action icons -->
                    <div
                        class="pt-2 col-sm-12 text-center py-sm-2 col-lg-3 col-xl-2 pt-y-0 text-lg-start"
                    >
                        <!-- !TODO - Insert method to update database of account state to disabled -->
                        <button class="btn-bg-outline mx-2" @click="sdf">
                            <!-- Delete Icon -->
                            <img
                                src="../assets/icons/delete-outline.svg"
                                alt=""
                                width="24"
                                height="24"
                            />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    data() {
        return {
            allAccounts: [],
            isHidden: false,
            roleValidFlag: false,
            vendorCreationValid: false,
            adminCreationValid: false,
            approverCreationValid: false,
            email: '', // User
            password: '', // User
            role: '', // User
            name: '', // User
            phoneNumber: '', // User
            company: '', // Vendor
            country: '', // Vendor & Admin
            companyRegistry: '', // Vendor
            businessNature: '', // Vendor
            gstNumber: '', // Vendor
            department: '', // Admin
            approvalTier: 1, // Approver
            error: '',
            allCountry: [
                'Afghanistan',
                'Albania',
                'Algeria',
                'Andorra',
                'Angola',
                'Anguilla',
                'Antigua &amp; Barbuda',
                'Argentina',
                'Armenia',
                'Aruba',
                'Australia',
                'Austria',
                'Azerbaijan',
                'Bahamas',
                'Bahrain',
                'Bangladesh',
                'Barbados',
                'Belarus',
                'Belgium',
                'Belize',
                'Benin',
                'Bermuda',
                'Bhutan',
                'Bolivia',
                'Bosnia &amp; Herzegovina',
                'Botswana',
                'Brazil',
                'British Virgin Islands',
                'Brunei',
                'Bulgaria',
                'Burkina Faso',
                'Burundi',
                'Cambodia',
                'Cameroon',
                'Cape Verde',
                'Cayman Islands',
                'Chad',
                'Chile',
                'China',
                'Colombia',
                'Congo',
                'Cook Islands',
                'Costa Rica',
                'Cote D Ivoire',
                'Croatia',
                'Cruise Ship',
                'Cuba',
                'Cyprus',
                'Czech Republic',
                'Denmark',
                'Djibouti',
                'Dominica',
                'Dominican Republic',
                'Ecuador',
                'Egypt',
                'El Salvador',
                'Equatorial Guinea',
                'Estonia',
                'Ethiopia',
                'Falkland Islands',
                'Faroe Islands',
                'Fiji',
                'Finland',
                'France',
                'French Polynesia',
                'French West Indies',
                'Gabon',
                'Gambia',
                'Georgia',
                'Germany',
                'Ghana',
                'Gibraltar',
                'Greece',
                'Greenland',
                'Grenada',
                'Guam',
                'Guatemala',
                'Guernsey',
                'Guinea',
                'Guinea Bissau',
                'Guyana',
                'Haiti',
                'Honduras',
                'Hong Kong',
                'Hungary',
                'Iceland',
                'India',
                'Indonesia',
                'Iran',
                'Iraq',
                'Ireland',
                'Isle of Man',
                'Israel',
                'Italy',
                'Jamaica',
                'Japan',
                'Jersey',
                'Jordan',
                'Kazakhstan',
                'Kenya',
                'Kuwait',
                'Kyrgyz Republic',
                'Laos',
                'Latvia',
                'Lebanon',
                'Lesotho',
                'Liberia',
                'Libya',
                'Liechtenstein',
                'Lithuania',
                'Luxembourg',
                'Macau',
                'Macedonia',
                'Madagascar',
                'Malawi',
                'Malaysia',
                'Maldives',
                'Mali',
                'Malta',
                'Mauritania',
                'Mauritius',
                'Mexico',
                'Moldova',
                'Monaco',
                'Mongolia',
                'Montenegro',
                'Montserrat',
                'Morocco',
                'Mozambique',
                'Namibia',
                'Nepal',
                'Netherlands',
                'Netherlands Antilles',
                'New Caledonia',
                'New Zealand',
                'Nicaragua',
                'Niger',
                'Nigeria',
                'Norway',
                'Oman',
                'Pakistan',
                'Palestine',
                'Panama',
                'Papua New Guinea',
                'Paraguay',
                'Peru',
                'Philippines',
                'Poland',
                'Portugal',
                'Puerto Rico',
                'Qatar',
                'Reunion',
                'Romania',
                'Russia',
                'Rwanda',
                'Saint Pierre &amp; Miquelon',
                'Samoa',
                'San Marino',
                'Satellite',
                'Saudi Arabia',
                'Senegal',
                'Serbia',
                'Seychelles',
                'Sierra Leone',
                'Singapore',
                'Slovakia',
                'Slovenia',
                'South Africa',
                'South Korea',
                'Spain',
                'Sri Lanka',
                'St Kitts &amp; Nevis',
                'St Lucia',
                'St Vincent',
                'St. Lucia',
                'Sudan',
                'Suriname',
                'Swaziland',
                'Sweden',
                'Switzerland',
                'Syria',
                'Taiwan',
                'Tajikistan',
                'Tanzania',
                'Thailand',
                "Timor L'Este",
                'Togo',
                'Tonga',
                'Trinidad &amp; Tobago',
                'Tunisia',
                'Turkey',
                'Turkmenistan',
                'Turks &amp; Caicos',
                'Uganda',
                'Ukraine',
                'United Arab Emirates',
                'United Kingdom',
                'Uruguay',
                'Uzbekistan',
                'Venezuela',
                'Vietnam',
                'Virgin Islands (US)',
                'Yemen',
                'Zambia',
                'Zimbabwe'
            ]
        }
    },
    computed: {
        isValidEmail() {
            return /^[^@]+@\w+(\.\w+)+\w$/.test(this.email)
        },
        isValidPhoneNumber() {
            return /^\d{10}$/.test(this.phoneNumber)
        }
    },
    async beforeMount() {
        await axios
            .get(`http://localhost:8080/api/users`)
            .then((res) => {
                let allAccounts = res.data
                this.allAccounts = allAccounts
                console.log(allAccounts)
            })
            .catch((err) => {
                console.error(err)
            })
    },
    methods: {
        onChange() {
            this.country = ''
            this.company = ''
            this.companyRegistry = ''
            this.businessNature = ''
            this.gstNumber = ''
            this.department = ''
            this.error = ''
        },
        containsOnlyNumbers(str) {
            const regex = /^[0-9]+$/

            if (!regex.test(str)) {
                return false
            }
            return true
        },

        onlyLettersAndSpaces(str) {
            return /^[A-Za-z\s]*$/.test(str)
        },

        async createAccount() {
            if (this.role != '') {
                if (this.validateByRole(this.role)) {
                    const url = 'http://localhost:8080/api/users'
                    const user_params = {
                        email: this.email,
                        password: this.password,
                        role: this.role,
                        name: this.name,
                        phoneNumber: this.phoneNumber
                    }
                    await axios
                        .post(url, user_params)
                        .then((res) => {
                            let createdUid = res.data

                            switch (this.role) {
                                case 'admin':
                                    this.createAdmin(createdUid)
                                    alert('Account is created successfully')
                                    this.$router.go()
                                    break

                                case 'approver':
                                    this.createApprover(createdUid)
                                    alert('Account is created successfully')
                                    this.$router.go()
                                    break

                                case 'vendor':
                                    this.createVendor(createdUid)
                                    alert('Account is created successfully')
                                    this.$router.go()
                                    break
                            }
                        })
                        .catch((err) => {
                            console.error(err)
                            alert(
                                'There seems to be an error with the creation of the account! Try again later'
                            )
                        })
                }
                alert(
                    'There seems to be an error with the creation of the account! Try again later'
                )
            }
        },

        createAdmin(uid) {
            let url = 'http://localhost:8080/api/admins'
            let adminParams = {
                uId: uid,
                country: this.country,
                department: this.department
            }
            axios
                .post(url, adminParams)
                .then(() => {
                    this.email = ''
                    this.password = ''
                    this.role = ''
                    this.name = ''
                    this.phoneNumber = ''
                    this.country = ''
                    this.department = ''
                })
                .catch((err) => {
                    console.error(err)
                    alert(
                        'There seems to be an error with the creation of the account! Try again later'
                    )
                })
        },

        createApprover(uid) {
            let url = 'http://localhost:8080/api/approvers'
            let approverParams = {
                uId: uid,
                approvalTier: this.approvalTier
            }
            axios
                .post(url, approverParams)
                .then(() => {
                    this.email = ''
                    this.password = ''
                    this.role = ''
                    this.name = ''
                    this.phoneNumber = ''
                })
                .catch((err) => {
                    console.error(err)
                    alert(
                        'There seems to be an error with the creation of the account! Try again later'
                    )
                })
        },

        createVendor(uid) {
            let url = 'http://localhost:8080/api/vendors'
            let vendorParams = {
                uId: uid,
                company: this.company,
                country: this.country,
                companyRegistry: this.companyRegistry,
                businessNature: this.businessNature,
                gstNumber: this.gstNumber
            }
            axios
                .post(url, vendorParams)
                .then(() => {
                    this.email = ''
                    this.password = ''
                    this.role = ''
                    this.name = ''
                    this.phoneNumber = ''
                    this.company = ''
                    this.country = ''
                    this.companyRegistry = ''
                    this.businessNature = ''
                    this.gstNumber = ''
                })
                .catch((err) => {
                    console.error(err)
                    alert(
                        'There seems to be an error with the creation of the account! Try again later'
                    )
                })
        },

        validateByRole(aRole) {
            let error = {}

            if (this.isValidEmail == false) {
                error['email'] = true
            }

            if (this.password == '') {
                error['password'] = true
            }

            if (this.name == '') {
                error['name'] = true
            }

            if ((this.phoneNumber == '') | (this.isValidPhoneNumber == false)) {
                error['phoneNumber'] = true
            }

            switch (aRole) {
                case 'admin':
                    if (this.country == '') {
                        error['country'] = true
                    }

                    if (this.department == '') {
                        error['department'] = true
                    }
                    break

                case 'approver':
                    if ((this.approvalTier == '') | this.containsOnlyNumbers(this.approvalTier)) {
                        error['approvalTier'] = true
                    }
                    break

                case 'vendor':
                    if (this.country == '') {
                        error['country'] = true
                    }

                    if (this.company == '') {
                        error['company'] = true
                    }

                    if (
                        (this.companyRegistry == '') |
                        this.containsOnlyNumbers(this.companyRegistry)
                    ) {
                        error['companyRegistry'] = true
                    }

                    if (
                        (this.businessNature == '') |
                        this.onlyLettersAndSpaces(this.businessNature)
                    ) {
                        error['businessNature'] = true
                    }

                    if ((this.gstNumber == '') | this.containsOnlyNumbers(this.gstNumber)) {
                        error['gstNumber'] = true
                    }
                    break
            }
            if (Object.keys(error).length > 0) {
                this.error = Object.keys(error)
                return false
            } else {
                return true
            }
        }
    }
}
</script>

<style lang="scss" scoped></style>
