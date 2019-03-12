<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
  /* margin-left: 5%; */
}
.header span {
  font-size: 15px;
}
.header {
  height: 60px;
}
.userState {
  position: absolute;
  margin-left: 80%;
  width:20%;
  top: 0px;
  z-index: 1;
}
.navPart{
  width:85%;
}
</style>
<template>
  <div class="mainPart">
    <div class="header">
      <img src="@/assets/images/OGMS.png" id="logo" class="pic">
      <div class="navPart">
        <Menu
          mode="horizontal"
          theme="dark"
          active-name="home"
          @on-select="turnContent"
          style="width:100%;z-index:0"
        >
          <MenuItem name="home" class="menuItem" style="margin-left:30%">
            <span>Home</span>
          </MenuItem>
          <MenuItem name="project">
            <span>Project</span>
          </MenuItem>
          <MenuItem name="participants">
            <span>Participants</span>
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
    <div class="content">
      <router-view @sendNotice="sendMessage" @readNotification="readNotification"></router-view>
    </div>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  name: "HelloWorld",
  data() {
    return {
      //消息机制
      noticeSocket: null,
      unreadNoticeCount: 0,
      timer: null,
      //导航栏宽度
      headerWidth:"",
    };
  },
  created() {
    if (this.$store.state.userState) {
      this.setTimer();
      this.initWebSocket();
      this.getUnreadNoticeCount();
    };
    this.headerWidth = window.innerWidth + 'px';
    // alert(this.headerWidth);
  },
  updated() {
    $(".userState sup").css("margin-top", "20px");
  },
  components: {
    Avatar
  },
  computed: {
    userState() {
      return this.$store.state.userState;
    },
    userName() {
      return this.$store.state.userName;
    },
    avatar() {
      return this.$store.state.avatar;
    }
  },
  methods: {
    turnContent(name) {
      if (name === "home") {
        this.$router.replace({ name: "Home" });
      } else if (name == "project") {
        this.$router.replace({ name: "Project" });
      } else if (name == "participants") {
        this.$router.replace({ name: "Participants" });
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
    // 获取到通知的数量
    getUnreadNoticeCount() {
      this.unreadNoticeCount = 0;
      //get请求发送的是用户id
      this.axios
        .get(
          "/GeoProblemSolving/notice/inquiry" +
            "?key=recipientId" +
            "&value=" +
            this.$store.state.userId
        )
        .then(res => {
          let noticeList = res.data;
          let unreadCount = 0;
          for (let i = 0; i < noticeList.length; i++) {
            if (noticeList[i].state === "unread") {
              unreadCount++;
              continue;
            }
          }
          this.$set(this, "unreadNoticeCount", unreadCount);
        })
        .catch(err => {
          console.log("失败的原因是" + err.data);
        });
    },
    initWebSocket() {
      var noticeSocketURL = "ws://localhost:8081/GeoProblemSolving/NoticeSocket";
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
      this.timer = setInterval(() => {
        this.noticeSocket.send("ping");
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
    }
  }
};
</script>
