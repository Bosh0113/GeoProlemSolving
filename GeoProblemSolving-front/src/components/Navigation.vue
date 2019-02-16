<template>
    <div class="mainPart">
        <div class="header" >
            <img src="@/assets/OGMS.png" id="logo" class="pic">
            <div class="nav_part">
                <Menu mode="horizontal" theme="dark" active-name="home" @on-select="turnContent" style="min-width:900px;z-index:0">
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
                                <Icon type="md-log-in" size="25" title="Log in"></Icon>
                            </MenuItem>
                            <MenuItem name="register">
                                <Icon type="md-create" size="25" title="Register"></Icon>
                            </MenuItem>
                        </Menu>
                    </template>
                    <template v-else>
                        <Menu mode="horizontal" theme="dark" @on-select="logged" style="z-index:0">
                            <MenuItem name="personalPage">
                                <img v-bind:src="avatar" v-if="avatar!=''&&avatar!='undefined'" :title="userName" style="width:40px;height:40px;vertical-align:middle;">
                                <avatar :username="userName" :size="40" style="margin-top:10px" :title="userName" v-else></avatar>
                            </MenuItem>
                            <MenuItem name="userLogout">
                                <Icon type="md-log-out" size="25" title="Log out"></Icon>
                            </MenuItem>
                        </Menu>
                    </template>
                </div>
            </div>
        </div>
        <div class="content">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  name: "HelloWorld",
  data() {
    return {};
  },
  mounted() {
    // let headerWidth = window.innerWidth+'px';
    // console.log("浏览器窗口的宽度是："+ headerWidth);
    // $(".header").css("width",headerWidth);
    // $(".ivu-menu-item").css("margin-left", "1.5%");
    // $(".ivu-menu ivu-menu-dark ivu-menu-horizontal").css("z-index", "0");
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
      }
      else if (name == "help") {
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
      if (name === "personalPage") {
        this.$router.push({ name: "PersonalPage" });
      } else if (name == "userLogout") {
        this.$store.commit("userLogout");
      }
    },
    userLogout(){

    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
}
.header span {
  font-size: 15px;
}
.header {
  /* position: fixed; */
  z-index: 2;
  /* top:0; */
  /* width: 98%; */
  height: 60px;
}
.userState {
  position: absolute;
  margin-left: 90%;
  top: 0px;
  min-width: 200px;
  z-index: 1;
}
.content {
  padding-top: 5px;
  width:95%;
  margin-left: 2.5%;
  margin-right: 2.5%;
}
</style>
