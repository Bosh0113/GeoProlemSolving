<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
body{
  min-width: 1200px;
  overflow-x: auto;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  /* margin-left: 2.5%; */
  margin-left: 5%;
}
.header span {
  font-size: 15px;
}
.header {
  /* z-index: 2;
  height: 60px; */
  position: absolute;
  width: 100%;
  height: 60px;
}
.userState {
  position: absolute;
  margin-left: 85%;
  top: 0px;
  min-width: 300px;
  z-index: 1;
}
.content {
  /* padding-top: 5px; */
  padding-top: 60px;
  /* width: 95%;
  margin-left: 2.5%;
  margin-right: 2.5%; */
  margin-left: 5%;
  margin-right: 5%;
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
          style="min-width:900px;z-index:0"
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
          <template v-if="state=== false">
            <Menu mode="horizontal" theme="dark" @on-select="unlogin" style="z-index:0">
              <MenuItem name="login">
                <span>Login</span>
              </MenuItem>
              <MenuItem name="register">
                 <span>Sign up</span>
                <!-- <Icon type="md-create" size="25" title="Register">Sign up</Icon> -->
              </MenuItem>
            </Menu>
          </template>
          <template v-else>
            <Menu mode="horizontal" theme="dark" @on-select="logged" style="z-index:0">
              <MenuItem name="notification">
                <Badge :count="unreadNoticeCount" id="noticeBadge">
                  <Icon type="ios-notifications-outline" size="25"></Icon>
                </Badge>
              </MenuItem>
              <MenuItem name="personalPage">
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
                      <Icon type="md-arrow-dropdown" />
                    <DropdownMenu slot="list" >
                        <DropdownItem name="personalPage">User Space</DropdownItem>
                        <DropdownItem name="userLogout">Log out</DropdownItem>
                    </DropdownMenu>
                </Dropdown>
              </MenuItem>
            </Menu>
          </template>
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
      unreadNoticeCount:0,
    };
  },
  created() {
    if (this.$store.state.userId != "") {
      this.initWebSocket();
      //获取到通知的总数并存储为vuex的变量
      this.getUnreadNoticeCount();
    }
  },
  updated(){
    $('.userState sup').css("margin-top","20px");
  },
  components: {
    Avatar
  },
  computed: {
    state() {
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
      if(name==="notification"){
        this.$router.push({ name: "Notifications" });
      }
    },
    // 获取到通知的数量
    getUnreadNoticeCount() {
      this.unreadNoticeCount=0;
      //get请求发送的是用户id
      this.axios
        .get(
          "http://localhost:8081/notice/inquiry?" +
            "key=recipientId" +
            "&value=" +
            this.$store.state.userId
        )
        .then(res => {
          let noticeList=res.data;
          let unreadCount=0;
          for(let i=0;i<noticeList.length;i++){
            if(noticeList[i].state==="unread"){
              unreadCount++;
              continue;
            }
          }
          this.$set(this,"unreadNoticeCount",unreadCount);
        })
        .catch(err => {
          console.log("失败的原因是" + err.data);
        });
    },
    initWebSocket() {
      var noticeSocketURL = "ws://localhost:8081/NoticeSocket";
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
      console.log("NoticeSocket连接断开！");
    },
    onError(e) {
      console.log("NoticeSocket连接错误！");
    },
    sendMessage(recipientId) {
      this.noticeSocket.send(recipientId);
    },
    readNotification(){
      let newCount=this.unreadNoticeCount;
      if(newCount>0){
        this.unreadNoticeCount=newCount-1;
      }
    },
    changeSelect(name){
      if(name =="userLogout"){
        this.$store.commit("userLogout");
        this.$router.replace({ name: "Home" });
      }else if (name =="personalPage") {
        this.$router.push({ name: "PersonalPage" });
      }
    }
  }
};
</script>
