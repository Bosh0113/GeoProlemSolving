<style scoped>
.header{
  height:60px;
  background:#515a6e;
  width:100%;
  color:white;top:0;
}
.footer{
  background:#515a6e;
  height:60px;
  width:100%;
  bottom:0;
  position:fixed;
}
.content{
  height:auto;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
  /* margin-left: 5%; */
}
.userState {
  position: absolute;
  margin-left: 80%;
  width: 20%;
  top: 0px;
  z-index: 1;
}
.navPart {
  width: 85%;
}
.footer{
  background-color:#515a6e;
  height:60px;
  width:100%;
  bottom:0;
}
</style>

<template>
<div>
  <div class="header">
    <img src="@/assets/images/OGMS.png" id="logo" class="pic" @click="goHome" style="cursor:pointer">
    <div class="navPart">
        <Menu
          mode="horizontal"
          theme="dark"
          active-name="home"
          @on-select="turnContent"
          style="z-index:0"
          width="auto"
        >
          <MenuItem name="home" class="menuItem" style="margin-left:30%">
            <span>Home</span>
          </MenuItem>
          <MenuItem name="project">
            <span>Project</span>
          </MenuItem>
          <MenuItem name="Public Resource">
            <span>Public Resource</span>
          </MenuItem>
          <MenuItem name="community">
            <span>Community</span>
          </MenuItem>
          <MenuItem name="help" class="menuItem">
            <span>Help</span>
          </MenuItem>
        </Menu>
        <div class="userState">
            <Menu mode="horizontal" theme="dark" @on-select="unlogin" style="z-index:0" v-show="!userState">
              <MenuItem name="login">
                <span>Login</span>
              </MenuItem>
              <MenuItem name="register">
                 <span>Sign up</span>
              </MenuItem>
            </Menu>
            <Menu mode="horizontal" theme="dark" @on-select="logged" style="z-index:0"  v-show="userState">
              <MenuItem name="notification">
                <Badge :count="unreadNoticeCount" id="noticeBadge">
                  <Icon type="ios-notifications-outline" size="25"></Icon>
                </Badge>
              </MenuItem>
              <MenuItem name="personal" style="width:100px">
                <Dropdown @on-click="changeSelect" placement="bottom-start">
                  <img
                    v-bind:src="avatar"
                    v-if="avatar!=''&&avatar!='undefined'"
                    :title="userName"
                    style="width:40px;height:40px;vertical-align:middle;"
                  >
                  <avatar
                    :username="userName"
                    :size="40"
                    style="margin-top:10px"
                    :title="userName"
                    v-else
                  ></avatar>
                  <DropdownMenu slot="list" >
                      <DropdownItem name="personalPage">User Space</DropdownItem>
                      <DropdownItem name="logout">Log out</DropdownItem>
                  </DropdownMenu>
                </Dropdown>
              </MenuItem>
            </Menu>
        </div>
      </div>
  </div>
  <Row>
    <Col span="16" offset="4" :style="{height:contentHeight}">
    <div style="display:flex;justify-content:center;height:100%;padding:100px 200px 100px 200px;">
      <Form :label-width="120">
          <FormItem label="ProjectId">
          <Input v-model="projectId" disabled style="width:500px"></Input>
        </FormItem>
        <FormItem label="Email" >
          <Input v-model="email" disabled style="width:500px"></Input>
        </FormItem>
        <div v-show="registeredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:500px"><Icon type="ios-information-circle-outline" :size="25" color="yellowGreen"/><span style="font-size:10px;">{{this.registeredHint}}</span></div>
          <div v-show="unregisteredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:500px"><Icon type="ios-information-circle-outline" :size="25" color="red"/><span style="font-size:10px;">{{this.unregisteredHint}}</span></div>
        <FormItem label="Password" v-show="passwordInputShow" style="margin-top:20px">
          <Input v-model="password" type="password" style="width:500px"></Input>
        </FormItem>
        <div style="display:flex;justify-content:center">
          <Button type="default" @click="joinByMail()">
            Submit
          </Button>
        </div>
      </Form>
    </div>
      <!-- <div style="margin-top:100px;bcakground-color:lightblue;height:500px;display:flex;justify-content:center" class="content">

      </div> -->
    </Col>
  </Row>
  <div class="footer">
      <h2 style="text-align:center;color:white;font-weight:bold;margin-top:15px"><i>Open Geographic Modeling and Simulation</i></h2>
      <h4 style="text-align:center;color:white">copyright@2013-2019 OpenGMS.all rights reserved</h4>
    </div>
</div>

</template>
<script>
import Avatar from "vue-avatar";
export default {
  data() {
    return {
      headerWidth: "",
      contentHeight:"",
      email:"",
      password:"",
      projectId:"",
      passwordInputShow:false,
      registeredHintShow:false,
      unregisteredHintShow:false,
      registeredHint:"This email has been registered,you have been a member in our platform, now you need fill the password in the blank and you can join in the project later.",
      unregisteredHint:"Sorry,you are not a user in our platform,this email will be used to create a new account for you,you only need to set a password for log in,and if you want to enrich your personal information,you can go to userSpace to enrich them.",
      // navaigation页面的变量
       //消息机制
      noticeSocket: null,
      unreadNoticeCount: 0,
      timer: null,
    };
  },
  // created(){

  // },
  mounted(){
    // navigation页面的
    if (this.$store.getters.userState) {
      this.setTimer();
      this.initWebSocket();
      this.getUnreadNoticeCount();
    }
    //
    this.headerWidth = window.innerWidth + "px";
    this.contentHeight = window.innerHeight - 120 + 'px';
    this.projectId = this.$route.params.id;;
    this.email = this.$route.params.email;
    this.judgeMailRegiste();
  },
  updated() {
    $(".userState sup").css("margin-top", "20px");
  },
  components: {
    Avatar
  },
  computed: {
    userState() {
      return this.$store.getters.userState;
    },
    userName() {
      return this.$store.getters.userName;
    },
    avatar() {
      return this.$store.getters.avatar;
    }
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
          this.$Message.error("Judge fail");
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
          this.$Message.error("Join fail");
        });
    },
    goHome() {
      this.$router.push({ name: "Home" });
    },
    turnContent(name) {
      if (name === "home") {
        this.$router.replace({ name: "Home" });
      } else if (name == "project") {
        this.$router.replace({ name: "Project" });
      } else if (name == "Public Resource") {
        this.$router.replace({ name: "resourceList" });
      } else if (name == "community") {
        this.$router.replace({ name: "Community" });
      } else if (name == "help") {
        this.$router.replace({ name: "Help" });
      }
    },
    unlogin(name) {
      if (name === "login") {
        this.$router.push({ name: "Login" });
      } else if (name == "register") {
        this.$router.push({ name: "Register" });
      }
    },
    logged(name) {
      if (name === "notification") {
        this.$router.push({ name: "Notifications" });
      } else if (name === "personal") {
      }
    },
    // navigation的函数
    initWebSocket() {
      if (this.noticeSocket != null) {
        this.noticeSocket = null;
      }
      var noticeSocketURL = "ws://localhost:8081/GeoProblemSolving/NoticeSocket";
      // var noticeSocketURL = "ws://"+this.$store.state.IP_Port+"/GeoProblemSolving/NoticeSocket";
      this.noticeSocket = new WebSocket(noticeSocketURL);
      this.noticeSocket.onopen = this.onOpen;
      this.noticeSocket.onmessage = this.onMessage;
      this.noticeSocket.onclose = this.onClose;
      this.noticeSocket.onerror = this.onError;
    },
    onOpen() {
      console.log("NoticeSocket连接成功！");
    },
    onMessage(e) {
      if (e.data == "Notice") {
        let newCount = this.unreadNoticeCount + 1;
        this.$set(this, "unreadNoticeCount", newCount);
        this.$Message.info("You have a new notice!");
      } else {
        console.log(e.data);
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("NoticeSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("NoticeSocket连接错误！");
    },
    sendMessage(recipientId) {
      this.noticeSocket.send(recipientId);
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        if (that.noticeSocket != null && that.noticeSocket != undefined) {
          that.noticeSocket.send("ping");
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    readNotification() {
      let newCount = this.unreadNoticeCount;
      if (newCount > 0) {
        this.unreadNoticeCount = newCount - 1;
      }
    },
    changeSelect(name) {
      if (name == "logout") {
        this.axios
          .get("/GeoProblemSolving/user/logout")
          .then(res => {
            this.$store.commit("userLogout");
            this.noticeSocket.close();
            this.$router.replace({ name: "Home" });
          })
          .catch(err => {
            confirm("logout fail!");
          });
      } else if (name == "personalPage") {
        this.$router.push({ name: "PersonalPage" });
      }
    },
  }
};
</script>
