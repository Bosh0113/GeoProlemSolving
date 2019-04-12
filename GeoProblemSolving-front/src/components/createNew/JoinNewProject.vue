<template>
  <Row>
    <Col span="16" offset="4">
      <div style="margin-top:100px;bcakground-color:lightblue;height:500px;display:flex;justify-content:center">
        <Form label-position="left" :label-width="100">
          <FormItem label="ProjectId">
          <Input v-model="projectId" disabled></Input>
        </FormItem>
        <FormItem label="Email">
          <Input v-model="email" disabled></Input>
        </FormItem>
        <div v-show="registeredHintShow==true" style="margin-left:100px;display:flex;align-items:center"><Icon type="ios-information-circle-outline" :size="25" color="yellowGreen"/><span style="font-size:15px;">{{this.registeredHint}}</span></div>
          <div v-show="unregisteredHintShow==true" style="margin-left:100px;display:flex;align-items:center"><Icon type="ios-information-circle-outline" :size="25" color="red"/><span style="font-size:15px;">{{this.unregisteredHint}}</span></div>
        <FormItem label="Password" v-show="passwordInputShow">
          <Input v-model="password" type="password"></Input>
        </FormItem>
        <div style="display:flex;justify-content:center">
          <Button type="default" @click="joinByMail()">
            Send Email
          </Button>
        </div>
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
      passwordInputShow:false,
      registeredHintShow:false,
      unregisteredHintShow:false,
      registeredHint:"This email has been registered,you have been a member in our platform, now you need fill the password in the blank and you can join in the project later.",
      unregisteredHint:"Sorry,you are not a user in our platform,this email will be used to create a new account for you,you only need to set a password for log in,and if you want to enrich your personal information,you can go to userSpace to enrich them."
    };
  },
  // created(){

  // },
  mounted(){
    this.projectId = this.$route.params.id;;
    // let id = this.$route.params.id;
    // console.log("id是"+id);
    this.email = this.$route.params.email;
    this.judgeMailRegiste();
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
          if (res.data === true) {
            this.registeredHintShow = true;
            this.$Message.info("This email has been registerd");
            this.passwordInputShow = true;
          } else if (res.data === false) {
            this.unregisteredHintShow = true;
            this.$Message.info("you have use your email to register an account");
            this.passwordInputShow = true;
          }
        })
        .catch(err => {
          this.$Message.danger("Judge fail");
        });
    },
    joinByMail(){
      this.axios
        .get(
          "/GeoProblemSolving/project/joinByMail" +
            "?projectId="+
            this.projectId+
            "&email=" +
            this.email+
            "&password="+
            this.password
        )
        .then(res => {
          if (res.data === "Success") {
            this.$Message.info("Join successfuly");
            let gotoProjectId = this.projectId
            this.$router.push({ name: "ProjectDetail", params: { id: gotoProjectId } });
          } else if (res.data === "Fail") {
            this.$Message.info("Fail to join in this project");
          }else if(res.data === "Exist"){
            this.$Message.info("you have been in this group,no need to apply again");
            this.$router.push({ name: "ProjectDetail", params: { id: gotoProjectId } });
          }else if(res.data === "None"){
            this.$Message.info("this group doesn't exist");
          }else if(res.data === "Password"){
            this.$Message.info("password might input error, try it again");
          }
        })
        .catch(err => {
          this.$Message.danger("Join fail");
        });
    }
  }
};
</script>
