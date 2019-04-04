<template>
  <Row>
    <Col span="16" offset="4">
      <div style="margin-top:100px;bcakground-color:lightblue;height:500px">
        <Form label-position="left" :label-width="100">
          <FormItem label="ProjectId">
          <Input v-model="projectId"></Input>
        </FormItem>
        <FormItem label="Email">
          <Input v-model="email"></Input>
        </FormItem>
        <FormItem label="Password" v-show="passwordInputShow">
          <Input v-model="password" ></Input>
        </FormItem>
      </Form>
      </div>
    </Col>
  </Row>
</template>
<script>
export default {
  data() {
    return {
      email:"",
      password:"",
      projectId:"",
      passwordInputShow:false
    };
  },
  // created(){

  // },
  mounted(){
    this.projectId = this.$route.params.id;;
    // let id = this.$route.params.id;
    // console.log("id是"+id);
    this.email = this.$route.params.email;
    // console.log('email', email);
  },
  methods:{
      judgeMailRegiste(){  
        this.axios
        .get(
          "/GeoProblemSolving/user/isRegistered?" +
            "email=" +
            this.email
        )
        .then(res => {
          if (res.data === "true") {
            this.$Message.info("This email has been registerd");
            this.passwordInputShow = true;
            joinedRefresh();
          } else if (res.data === "false") {
            this.$Message.info("you have use your email to register an account");
          }
        })
        .catch(err => {
          this.$Message.danger("Join fail");
        }); 
    }
  }
};
</script>
