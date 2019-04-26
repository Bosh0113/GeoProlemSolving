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
  background-color: var(--layout-background);
  display: flex;
  justify-content: center;
}
.loginDiv {
  border: var(--loginBorder);
  --loginContentColor: white;
  --loginFontColor: white;
  --loginTitleColor: #57a3f3;
  width: 40%;
  --loginBtnFontSize: 15px;
}
.login_title {
  background-color: var(--loginTitleColor);
  text-align: center;
  vertical-align: middle;
  line-height: 60px;
  color: var(--loginFontColor);
  font-size: 30px;
  font-weight: bold;
  padding: 0;
}
.login-content {
  background-color: var(--loginContentColor);
  padding: 50px 20px 20px 20px;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
}
.loginBtn {
  font-size: var(--loginBtnFontSize);
}
.loginBtn:hover {
  color: white;
  background-color: var(--loginTitleColor);
  font-size: var(--loginBtnFontSize);
}
img {
  cursor: pointer;
}
.reUseDiv {
  display: flex;
  text-align: center;
  justify-content: center;
}
.reciverSpan {
  width: 100px;
  text-align: center;
}
.resetReuseDiv {
  display: flex;
  align-items: center;
  padding: 10px;
}
.resetReuseDiv p,
.resetReuseDiv input {
  width: 100%;
}
</style>
<template>
  <div class="layout">
    <div class="header">
      <img src="@/assets/images/OGMS.png" id="logo" @click="goHome">
    </div>
    <div class="content" ref="homePage" v-if="Userstate===false" v-bind:style="contentStyle">
      <div class="loginDiv" v-bind:style="loginStyle">
        <div class="login_title">Log in</div>
        <div class="login-content">
          <Form ref="loginForm" :model="loginForm" :rules="loginFormRule">
            <FormItem prop="user" label="E-mail" :label-width="100">
              <Input
                placeholder="Please input your username"
                style="width: 90%"
                v-model="loginForm.user"
                type="text"
              />
            </FormItem>
            <FormItem prop="password" label="Password" :label-width="100">
              <Input
                placeholder="please input your password"
                style="width: 90%"
                v-model="loginForm.password"
                @keyup.enter.native="login('loginForm')"
                type="password"
              />
            </FormItem>
            <div class="reUseDiv">
              <Checkbox v-model="checked">Automatic login within one week</Checkbox>
            </div>
            <div class="reUseDiv">
              <p>
                Forget password?
                <a @click="resetModalSHow=true">Reset</a>
              </p>
            </div>
            <br>
            <FormItem>
              <div class="reUseDiv">
                <Button
                  type="default"
                  @click="login('loginForm')"
                  v-model="loginForm.State"
                  class="loginBtn"
                >Log in</Button>
              </div>
            </FormItem>
            <div class="reUseDiv">
              <span>
                If you don't hava an account,click
                <a @click="goRegister">Here</a> to register.
              </span>
            </div>
          </Form>
        </div>
      </div>
    </div>
    <Modal
      v-model="resetModalSHow"
      @on-ok="sendResetEmail"
      @on-cancel
      ok-text="Confirm"
      cancel-text="cancel"
      title="Reset password board"
    >
      <div class="resetReuseDiv">
        <span class="reciverSpan">reciever:</span>
        <Input v-model="loginForm.user"/>
      </div>
      <div class="resetReuseDiv">
        <Icon type="ios-information-circle-outline" :size="20" color="lightblue"/>
        <p>We will send you an email with a url you can visit it and reset your password,if you agree,you can click the Confirm button and you will get an email soon.</p>
      </div>
    </Modal>
  </div>
</template>
<script>
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
  data() {
    return {
      loginForm: {
        user: "",
        password: ""
      },
      loginFormRule: {
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
      loginStyle: {
        marginTop: ""
      },
      contentStyle: {
        height: ""
      },
      checked: false,
      changePwdEmailStyle:
        "This email is used for help you reset your password,you can click this url ",
      urlAddress: "http://172.21.212.7:8082/GeoProblemSolving/resetPassword/",
      resetModalSHow: false
    };
  },
  mounted() {
    this.$Notice.config({
      top: 100,
      duration: 1
    });
    this.contentStyle.height = window.innerHeight - 60 + "px";
    this.loginStyle.marginTop = window.innerHeight / 5 + "px";
    this.getlocalStorage();
  },
  methods: {
    login(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          if (this.checked == true) {
            localStorage.setItem("user", this.loginForm.user);
            var password = this.loginForm.password;
            password = this.encrypto(password);
            localStorage.setItem("password", password);
            localStorage.setItem("statusRecord", this.checked);
          } else if (this.checked == false) {
            localStorage.setItem("user", "");
            localStorage.setItem("password", "");
            localStorage.setItem("statusRecord", false);
          }
          var email = this.loginForm.user;
          var passwordFro = this.loginForm.password;
          var passwordAES = this.encrypto(passwordFro);
          var passwordAESURI = window.encodeURIComponent(passwordAES);
          this.axios
            .get(
              "/GeoProblemSolving/user/login" +
                "?email=" +
                email +
                "&password=" +
                passwordAESURI
            )
            .then(res => {
              if (res.data === "Email") {
                this.$Message.error("Email does not exist.");
              } else if (res.data === "Password" || res.data === "Fail") {
                this.$Message.error("Invalid account or password.");
              } else {
                this.$store.commit("userLogin", res.data);
                setTimeout(this.goBack, 100);
              }
            });
        } else {
          this.$Message.error(
            "If you don't have an account, you can regisetr one."
          );
        }
      });
    },
    goBack() {
      this.$router.go(-1);
    },
    register() {
      this.$router.push({ name: "Register" });
    },
    goHome() {
      this.$router.push({ name: "Home" });
    },
    getlocalStorage() {
      this.loginForm.user = localStorage.getItem("user");
      this.loginForm.password = this.decrypto(localStorage.getItem("password"));
      // 将字符串格式的true转换为boolean模式的true
      if (localStorage.getItem("statusRecord")) {
        this.checked = eval(localStorage.getItem("statusRecord"));
      }
    },
    encrypto(context) {
      var CryptoJS = require("crypto-js");
      var key = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var iv = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var encrypted = "";
      if (typeof context == "string") {
      } else if (typeof context == "object") {
        context = JSON.stringify(context);
      }
      var srcs = CryptoJS.enc.Utf8.parse(context);
      encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      });
      return encrypted.toString();
    },
    decrypto(context) {
      var CryptoJS = require("crypto-js");
      var key = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var iv = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var decrypt = CryptoJS.AES.decrypt(context, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      });
      var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
      return decryptedStr.toString();
    },
    goRegister() {
      this.$router.push({ name: "Register" });
    },
    sendResetEmail() {
      var emailFormBody = {};
      emailFormBody["recipient"] = this.loginForm.user;
      emailFormBody["mailTitle"] = "Reset password notification";
      emailFormBody["mailContent"] =
        this.changePwdEmailStyle +
        this.urlAddress +
        this.loginForm.user +
        " to change your password, thanks.";
      this.axios
        .post("/GeoProblemSolving/email/send", emailFormBody)
        .then(res => {
          if (res.data == "Success") {
            this.$Notice.success({
              title: "Email send success",
              desc: "The email has been sent successfully."
            });
          } else {
            this.$Notice.error({
              title: "Email send fail",
              desc: "Maybe you input a wrong email , please check it out."
            });
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>

