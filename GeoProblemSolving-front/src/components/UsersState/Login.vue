<style scoped>
.Content {
  background: url("./../../assets/back.png") no-repeat;
  background-size: cover;
}
</style>
<template>
  <div>
    <div ref="homePage" v-if="Userstate===false">
      <Row class="Content" id="background">
        <Col :xs="{ span: 12, offset: 6 }" :lg="{ span: 12, offset: 6 }">
          <div style="padding:20px;margin:125px 0 50px 0">
            <Card>
              <div slot="title" class="login_title">Log in</div>
              <Form ref="formInline" :model="formInline" :rules="ruleInline" style="margin:50px">
                <FormItem prop="user">
                  <Input
                    placeholder="Username"
                    style="width: 100%"
                    v-model="formInline.user"
                    type="text"
                  />
                </FormItem>
                <br>
                <br>
                <FormItem prop="password">
                  <Input
                    placeholder="password"
                    style="width: 100%"
                    v-model="formInline.password"
                    type="password"
                  />
                </FormItem>
                <br>
                <FormItem>
                  <Button
                    type="primary"
                    @click="handleSubmit('formInline')"
                    v-model="formInline.State"
                  >Sign In</Button>
                  <Button
                    type="success"
                    @click="register()"
                    style="float:right"
                  >Register</Button>
                </FormItem>
                <FormItem>
                  <!-- <Button type="success" @click="handleWithoutAccount()">Register</Button> -->
                </FormItem>
              </Form>
            </Card>
          </div>
        </Col>
      </Row>
    </div>
    <!-- <app-sidebar v-if="this.Userstate===true"></app-sidebar>     -->
  </div>
</template>
<style>
.login_title {
  background-color: rgb(34, 167, 240);
  text-align: center;
  vertical-align: middle;
  line-height: 60px;
  color: white;
  font-size: 30px;
  font-weight: bold;
}
</style>
<script>
import axios from "axios";
export default {
  components: {},
  computed: {
    Userstate() {
      return this.$store.getters.userState;
    },
    Username() {
      return this.$store.getters.userName;
    }
  },
  created() {
    this.$store.commit("getStorage");
  },
  data() {
    return {
      formInline: {
        user: "",
        password: ""
      },
      ruleInline: {
        user: [
          {
            required: true,
            message: "Please fill in the email",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "Please fill in the password.",
            trigger: "blur"
          },
          {
            type: "string",
            min: 6,
            message: "The password length cannot be less than 6 bits",
            trigger: "blur"
          }
        ]
      },
      clientHeight: ""
    };
  },
  mounted() {
    var heightFinal = $(document).height();
    heightFinal = heightFinal + "px";
    $("#background").height(heightFinal);
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.axios
            .get(
              "http://localhost:8081/user/login"+"?email=" + this.formInline.user +"&password=" + this.formInline.password,
            )
            .then(
              res => {
                if (res.data === "Fail") {
                  this.$Message.error("Invalid account or password.");
                } else{
                  this.$Message.success("Success!");
                  this.$store.commit("userLogin", {
                    userName: res.data.userName,
                    avatar: res.data.avatar,
                    userId: res.data.userId,
                  });
                  // 这里利用userId发送请求去获取所有的通知

                  this.$router.go(-1);
                  // this.$router.push({name:"notice"});
                }
              },
            );
        } else {
          this.$Message.error(
            "If you don't have an account, you can regisetr one."
          );
        }
      });
    },
    register: function() {
      this.$router.push({ name: "Register" });
    },

  }
};
</script>

