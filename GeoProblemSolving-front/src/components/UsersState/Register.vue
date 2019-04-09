<style scoped>
.Content {
  /* background: url("./../../assets/back.png") no-repeat; */
  background-size: cover;
}
.header {
  height: 60px;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
}
.InputStyle {
  width: 300px;
}
.registerForm {
  padding: 20px;
  /* margin: 125px 0 50px 0; */
  min-width: 900px;
}
.register_title {
  background-color: rgb(34, 167, 240);
  text-align: center;
  vertical-align: middle;
  line-height: 60px;
  color: white;
  font-size: 30px;
  font-weight: bold;
}
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  overflow: hidden;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
</style>
<template>
  <div>
    <Layout>
      <Header class="header">
        <img src="@/assets/images/OGMS.png" id="logo" class="pic" @click="goHome" style="cursor:pointer">
      </Header>
      <Content>
        <Row class="Content" id="register">
          <Col :xs="{ span: 12, offset: 6 }" :lg="{ span: 12, offset: 6 }">
            <div class="registerForm">
              <Card>
                <h2 slot="title" class="register_title">Register</h2>
                <!-- 实现注册的样式 -->
                <Form
                  ref="formValidate"
                  :model="formValidate"
                  :rules="ruleValidate"
                  :label-width="300"
                  inline
                >
                  <FormItem label="Name" prop="userName">
                    <Input
                      v-model="formValidate.userName"
                      placeholder="Plase enter username"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Password" prop="password">
                    <Input
                      v-model="formValidate.password"
                      placeholder="Plase enter password"
                      :class="{InputStyle: inputstyle}"
                      :type="pwdType"
                    >
                      <Button slot="append" @click="changeType()">
                        <Icon type="ios-eye" size="20" v-show="pwdType=='text'"/>
                        <Icon type="ios-eye-off" size="20" v-show="pwdType=='password'"/>
                      </Button>
                    </Input>
                  </FormItem>
                  <!-- confimPassword -->
                  <FormItem label="Confim password" prop="confimPassword">
                    <Input
                      v-model="formValidate.confimPassword"
                      placeholder="Plase enter password again"
                      :class="{InputStyle: inputstyle}"
                      :type="pwdType"
                    >
                    </Input>
                  </FormItem>
                  <FormItem label="Job title" prop="jobTitle">
                    <Input
                      v-model="formValidate.jobTitle"
                      placeholder="Plase enter your job title"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="E-mail" prop="email">
                    <Input
                      v-model="formValidate.email"
                      placeholder="Plase enter your e-mail"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Mobile phone" prop="mobilePhone">
                    <Input
                      v-model="formValidate.mobilePhone"
                      placeholder="Plase enter your mobilePhone"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Country" prop="country">
                    <Input
                      v-model="formValidate.country"
                      placeholder="Plase enter your country"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="City" prop="city">
                    <Input
                      v-model="formValidate.city"
                      placeholder="Plase enter your city"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Affiliation" prop="organization">
                    <Input
                      v-model="formValidate.organization"
                      placeholder="Plase enter your affiliation"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Field" prop="field">
                    <Input
                      v-model="formValidate.field"
                      placeholder="Plase enter your research field"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <FormItem label="Home page" prop="homePage">
                    <Input
                      v-model="formValidate.homePage"
                      placeholder="Plase enter your homepage url"
                      :class="{InputStyle: inputstyle}"
                    ></Input>
                  </FormItem>
                  <br>
                  <FormItem label="Gender" prop="gender">
                    <RadioGroup v-model="formValidate.gender">
                      <Radio label="male">Male</Radio>
                      <Radio label="female">Female</Radio>
                    </RadioGroup>
                  </FormItem>
                  <br>
                  <FormItem label="Avatar" prop="avatar">
                    <div>
                      <div class="demo-upload-list" v-if="avatar!=''">
                        <template>
                          <img v-bind:src="avatar">
                          <div class="demo-upload-list-cover">
                            <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                            <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                          </div>
                        </template>
                      </div>
                      <div class="uploadBox">
                        <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
                        <input @change="uploadPhoto($event)" type="file" class="uploadAvatar">
                      </div>
                      <Modal title="View Image" v-model="visible">
                        <img :src="avatar" v-if="visible" style="width: 100%">
                      </Modal>
                    </div>
                  </FormItem>
                  <FormItem label="Introduce" prop="introduction" style="width:80%">
                    <Input
                      v-model="formValidate.introduction"
                      type="textarea"
                      :autosize="{minRows: 2,maxRows: 5}"
                      placeholder="Plase introduce yourself"
                    ></Input>
                  </FormItem>
                  <FormItem>
                    <Button type="primary" @click="handleSubmit('formValidate')">Submit</Button>
                    <Button @click="handleReset('formValidate')" style="margin-left: 8px">Reset</Button>
                  </FormItem>
                </Form>
                <!-- 注册样式结束 -->
              </Card>
            </div>
          </Col>
        </Row>
      </Content>
      <Footer>Footer</Footer>
    </Layout>
    <!-- <app-header></app-header> -->
  </div>
</template>
<script>
export default {
  components: {},
  computed: {
    avatar() {
      return this.$store.getters.avatar;
    }
  },
  mounted() {
    this.$store.getters.avatar = "";
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please enter your password again"));
      } else if (value !== this.formValidate.password) {
        callback(new Error("The two passwords are inconsistent!"));
      } else {
        callback();
      }
    };
    return {
      //data预设
      country: "",
      region: "",
      //input的样式设定
      inputstyle: true,
      //表单验证
      formValidate: {
        userName: "",
        email: "",
        password: "",
        confimPassword: "",
        jobTitle: "",
        mobilePhone: "",
        gender: "",
        country: "",
        city: "",
        organization: "",
        introduction: "",
        field: "",
        homePage: "",
        avatar: ""
      },
      ruleValidate: {
        userName: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        email: [
          {
            required: true,
            message: "Mailbox cannot be empty",
            trigger: "blur"
          },
          {
            type: "email",
            message: "Incorrect email format",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            min: 6,
            message: "Password must more than 6 words",
            trigger: "blur"
          }
        ],
        confimPassword: [
          { 
            required: true,
            validator: validatePass,
            trigger: "blur" 
          }
        ],
        jobTitle: [
          {
            required: true,
            message: "Job Title cannot be empty",
            trigger: "blur"
          }
        ],
        gender: [
          {
            required: true,
            message: "Please select gender",
            trigger: "change"
          }
        ],
        mobilePhone: [
          {
            required: false,
            message: "Please enter your phone number",
            trigger: "blur"
          }
        ],
        country: [
          {
            required: false,
            message: "Please enter your country",
            trigger: "blur"
          }
        ],
        city: [
          {
            required: false,
            message: "Please enter your city",
            trigger: "blur"
          }
        ],
        organization: [
          {
            required: false,
            message: "Please enter your affiliation",
            trigger: "blur"
          }
        ],
        introduction: [
          {
            required: false,
            message: "Please enter a personal introduction",
            trigger: "blur"
          },
          {
            type: "string",
            min: 20,
            message: "Introduce no less than 20 words",
            trigger: "blur"
          }
        ],
        field: [
          {
            required: false,
            message: "Please enter your research field",
            trigger: "blur"
          }
        ],
        homePage: [
          {
            required: false,
            message: "Please enter your home page url",
            trigger: "blur"
          }
        ]
      },
      visible: false,
      // 隐藏密码图标样式
      pwdType: "password" // 密码类型
    };
  },
  mounted() {},
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var userJson = {};
          userJson["userName"] = this.formValidate.userName;
          userJson["email"] = this.formValidate.email;
          userJson["password"] = this.formValidate.password;
          userJson["mobilePhone"] = this.formValidate.mobilePhone;
          userJson["gender"] = this.formValidate.gender;
          userJson["jobTitle"] = this.formValidate.jobTitle;
          userJson["country"] = this.formValidate.country;
          userJson["city"] = this.formValidate.city;
          userJson["organization"] = this.formValidate.organization;
          userJson["introduction"] = this.formValidate.introduction;
          userJson["direction"] = this.formValidate.field;
          userJson["homePage"] = this.formValidate.homePage;
          userJson["avatar"] = this.formValidate.avatar;
          this.axios
            .post(
              "/GeoProblemSolving/user/register",
              // params
              userJson
            )
            .then(res => {
              if (res.data == "Email") {
                this.$Message.success("Email has been used!");
              } else if (res.data == "MobilePhone") {
                this.$Message.success("Phone number has been used!");
              } else {
                this.axios
                  .get(
                    "/GeoProblemSolving/user/login" +
                      "?email=" +
                      this.formValidate.email +
                      "&password=" +
                      this.formInline.password
                  )
                  .then(res => {
                    if (res.data === "Fail") {
                      this.$Message.error("Invalid account or password.");
                    } else {
                      this.$Message.success("Success!");
                      this.$store.commit("userLogin", res.data);
                      this.$router.push({ path: "/" });

                    }
                  });
              }
            }),
            function(res) {
              this.$Message.error("Fail!");
            };
        } else {
          this.$Message.error("Form fill error!");
        }
      });
    },
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      var filename = file.name;
      var imgcode = "";
      // 2,621,440   2M
      if (filesize > 2101440) {
        // 图片大于2MB
        this.$Message.error("size > 2MB");
      }
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = e => {
        // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
        imgcode = e.target.result;
         
        this.$store.commit("uploadAvatar", imgcode);
      };
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.$store.commit("uploadAvatar", "");
    },
    //点击图标片跳转到主页
    goHome() {
      this.$router.push({ name: "Home" });
    },
    //输入密码时
    changeType() {
      this.pwdType = this.pwdType === "password" ? "text" : "password";
      // this.eyeIconType = 'ios-eye-on';
    }
  }
};
</script>

