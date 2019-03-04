<style scoped>
/* 注意css在哪里生效,需要全局使用就全局定义，仅在某个局部定义就在某个div或者元素下定义，在其子元素生效 */
.layout {
  --layout-background: lightgray;
  --headerHeight: 60px;
  --footerHeight: 60px;
  overflow: hidden;
}
.header {
  /* 计算属性，可以根据需求动态变化宽高 */
  height: calc(var(--headerHeight) * 1);
  background: #515a6e;
}

.content {
  /* background: url("./../../assets/back.png") no-repeat; */
  background-color: var(--layout-background);
  display:flex;
  /* align-items:center; */
  justify-content:center;
}
.loginDiv {
  border: var(--loginBorder);
  --loginContentColor: white;
  --loginFontColor: white;
  --loginTitleColor: #57a3f3;
  width:30%;
  --loginBtnFontSize:15px;
}
.login_title {
  background-color: var(--loginTitleColor);
  text-align: center;
  vertical-align: middle;
  line-height: 60px;
  color: var(--loginFontColor);
  font-size: 30px;
  font-weight: bold;
}
.login-content {
  background-color: var(--loginContentColor);
  padding: 10px;
}

#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
}
.loginBtn{
  font-size: var(--loginBtnFontSize)
}
.loginBtn:hover{
  color:white;
  background-color: var(--loginTitleColor);
  font-size: var(--loginBtnFontSize)
}

</style>
<template>
  <div class="layout">
    <Layout>
      <div class="header">
        <img src="@/assets/images/OGMS.png" id="logo" class="pic" @click="goHome">
      </div>
      <div class="content" ref="homePage" v-if="Userstate===false" v-bind:style="contentStyle">
        <div class="loginDiv" v-bind:style="loginStyle">
              <div class="login_title" style="padding:0">Log in</div>
              <div class="login-content">
                <Form ref="formInline" :model="formInline" :rules="ruleInline" style="margin:25px">
                  <div>
                    <FormItem prop="user" label="userName" :label-width="100">
                      <Input
                        placeholder="Please input your username"
                        style="width: 90%"
                        v-model="formInline.user"
                        type="text"
                      />
                    </FormItem>
                  </div>
                  <br>
                  <FormItem prop="password" label="password" :label-width="100">
                    <Input
                      placeholder="please input your password"
                      style="width: 90%"
                      v-model="formInline.password"
                      type="password"
                    />
                  </FormItem>
                  <br>
                  <FormItem>
                    <div style="display:flex;align-items:center;justify-content:center">
                      <Button
                        type="default"
                        @click="handleSubmit('formInline')"
                        v-model="formInline.State"

                        class="loginBtn"
                      >Log in</Button>
                    </div>
                  </FormItem>
                </Form>
              </div>
            </div>
      </div>
    </Layout>
  </div>
</template>
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
            message: "username is not allowed null",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "password is not allowed null",
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
      clientHeight: "",
      loginStyle:{
        marginTop:""
      },
      contentStyle:{
        height:""
      }
    };
  },
  mounted() {
    this.contentStyle.height = window.innerHeight - 60 + "px";
    this.loginStyle.marginTop = window.innerHeight/5+ "px";
    console.log("loginStyle.marginTop是：" + this.loginStyle.marginTop);
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.axios
            .get(
              "http://localhost:8081/user/login" +
                "?email=" +
                this.formInline.user +
                "&password=" +
                this.formInline.password
            )
            .then(res => {
              console.log(res.data);
              if (res.data === "Fail") {
                this.$Message.error("Invalid account or password.");
              } else {
                this.$Message.success("Success!");
                this.$store.commit("userLogin", {
                  userName: res.data.userName,
                  avatar: res.data.avatar,
                  userId: res.data.userId
                });
                this.$router.go(-1);

              }
            });
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
    goHome() {
      this.$router.push({ name: "Home" });
    }
  }
};
</script>

