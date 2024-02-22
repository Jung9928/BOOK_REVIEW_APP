<template>
  <div>
    <b-form @submit="submit" @reset="onReset" v-if="show">
      <b-form-group
          id="input-group-1"
          label="Email address:"
          label-for="input-1"
          description="We'll never share your email with anyone else."
      >
        <b-form-input
            id="input-1"
            v-model="form.email"
            type="email"
            placeholder="Enter email"
            required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Your ID:" label-for="input-2">
        <b-form-input
            id="input-2"
            v-model="form.id"
            placeholder="Enter ID"
            required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Your password:" label-for="input-2">
        <b-form-input
            id="input-2"
            v-model="form.password"
            placeholder="Enter Password"
            required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="nickName:" label-for="input-3">
        <b-form-select
            id="input-3"
            v-model="form.nickName"
            placeholder="Enter nickName"
            required
        ></b-form-select>
      </b-form-group>

      <b-button type="submit" variant="primary">Submit</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
    <b-card class="mt-3" header="Form Data Result">
      <pre class="m-0">{{ form }}</pre>
    </b-card>
  </div>
</template>

<script>
// import {reactive} from "vue";
// import axios from "axios";
// import store from "@/scripts/store";
// import router from "@/scripts/router";

// export default {
  // data() {
  //   return {
  //     form: {
  //       email: '',
  //       name: '',
  //       food: null,
  //       checked: []
  //     },
  //     foods: [{ text: 'Select One', value: null }, 'Carrots', 'Beans', 'Tomatoes', 'Corn'],
  //     show: true
  //   }
  // },
  // methods: {
  //   onSubmit(event) {
  //     event.preventDefault()
  //     alert(JSON.stringify(this.form))
  //   },
  //   onReset(event) {
  //     event.preventDefault()
  //     // Reset our form values
  //     this.form.email = ''
  //     this.form.name = ''
  //     this.form.food = null
  //     this.form.checked = []
  //     // Trick to reset/clear native browser form validation state
  //     this.show = false
  //     this.$nextTick(() => {
  //       this.show = true
  //     })
  //   }
  // }

  // setup() {
  //   const state = reactive({
  //     form :{
  //       email: "",
  //       id: "",
  //       password: "",
  //       nickName: ""
  //     }
  //   });
  //
  //   const submit = ()=> {
  //     axios.post("/api/account/signup", state.form).then((res) => {
  //       store.commit('setAccount', res.data);
  //       sessionStorage.setItem("id", res.data);
  //       router.push({path:"/"});
  //       window.alert("회원가입하셨습니다.");
  //     }).catch(() => {
  //       window.alert("로그인 정보가 존재하지 않습니다..");
  //     });
  //   }
  //
  //   return {state, submit}
  // }
// }
</script>