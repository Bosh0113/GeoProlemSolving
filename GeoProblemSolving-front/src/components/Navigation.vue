<template>
  <div class="mainPart">
    <div class="header">
      <img src="@/assets/OGMS.png" id="logo" class="pic">
      <div class="nav_part">
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
                <MenuItem name="notification">
                  <div class="bottom">
                    <Poptip
                      placement="bottom-end"
                      width="750"
                    >
                      <Badge :count="messageCount" v-if="avatar!=''&&avatar!='undefined'"></Badge>
                      <div slot="content" style="margin:0 auto">
                        <Tabs>
                            <TabPane :label="invitation" name="invitation" :data="invicationList">
                              <div v-for="(single,index) in invicationList" :key="index" style="width:100%;height:40px;border-bottom:1px dotted gray"><span style="float:left;line-height:40px">{{single.title}}</span><span style="float:right;line-height:40px">211</span>
                              </div>
                              </TabPane>
                            <TabPane :label="notification" name="notification">Comment</TabPane>
                            <TabPane :label="comment" name="comment">Comment</TabPane>
                            <TabPane :label="reply" name="reply">Comment</TabPane>
                            <TabPane :label="personal" name="personal" :data="applicationList">
                              <div v-for="(single,index) in applicationList" :key="index" style="width:100%;height:40px;display:flex">
                                <div style="line-height:40px;padding:0;width:80%";display:flex>
                                  <span>{{single.content.userName}} applied join in the project {{single.content.title}}.
                                  </span>
                                  <span>
                                    111
                                  </span>
                                </div>
                                <div style="width:20%">
                                  <!-- 绑定已阅的显示样式 -->
                                  <Button type="success" @click="allowJoin(single)"><Icon type="md-checkmark" /></Button>
                                  <Button type="warning" @click="disallowJoin"><Icon type="md-hand"  /></Button>
                                  <Button type="error" @click=""><Icon type="md-close" /></Button>
                                </div>
                                <!-- <span style="float:right;line-height:40px">{{single.createTime}}</span> -->
                              </div>
                            </TabPane>
                        </Tabs>
                        <br>
                        <Button>More</Button>
                      </div>
                      <!-- <div class="api" slot="content">
                        111
                      </div> -->
                      <!-- <Badge :count="10" v-if="avatar!=''&&avatar!='undefined'"></Badge> -->
                    </Poptip>
                  </div>
                </MenuItem>
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
    return {
      invitation: (h) =>{
        return h('div',[
          h('span','invitation'),
          h('span',' '),
          h('Badge',{
            props:{
              count: this.invitationCount
            }
          })
        ])
      },
      notification: (h) =>{
        return h('div',[
          h('span','notification'),
          h('span',' '),
          h('Badge',{
            props:{
              count: this.notificationCount
            }
          })
        ])
      },
      reply: (h) =>{
        return h('div',[
          h('span','reply'+ ''),
          h('span',' '),
          h('Badge',{
            props:{
              count: this.replyCount
            }
          })
        ])
      },
      personal: (h) =>{
        return h('div',[
          h('span','personal'),
          h('span',' '),
          h('Badge',{
            props:{
              count: this.applicationList.length
            }
          })
        ])
      },
      comment: (h) =>{
        return h('div',[
          h('span','comment'),
          h('span',' '),
          h('Badge',{
            props:{
              count: this.commentCount
            }
          })
        ])
      },
      // information:[1,2,3,4,5],
      //关于所有类型信息的数目统计
       notificationCount: 0,
       invitationCount:0,
       replyCount:0,
       commentCount:0,
       personalCount:0,
      //信息列表类型变量
      invicationList:[],
      applicationList:[],
      //消息列表
      messageList:[],
      //消息统计值
      messageCount: 0,
      //控制申请的模态框
      processNotificationModal:false,
      //当亲处理的通知
      currentProcessNotification:{},
      ProcessNotificationTemplate:"",
      //通知的状态
      messageStatus:false
    };
  },
  created(){
    // this.invicationList = [
    //   {title:'一路走来，感谢有你'},{title:'新年快乐，龙马精神'},{title:'家庭和睦，事业有成'},{title:'健康美丽，开心久久'}
    // ];
    if(this.$store.state.userId!=""){
      //获取到通知的总数并存储为vuex的变量
    this.getNotification();
    }
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
    },
    // judgeShow(){
    //   return messageStatus;
    // }
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
      if (name === "personalPage") {
        this.$router.push({ name: "PersonalPage" });
      } else if (name == "userLogout") {
        this.$store.commit("userLogout");
      }
    },
    userLogout() {},
    // 获取到通知的数量
    getNotification(){
      //get请求发送的是用户id
      this.axios.get( "http://localhost:8081/notice/inquiry?"+"key=recipientId"+ "&value=" + this.$store.state.userId)
      .then(res=> {
        this.messageList = res.data;
        let data = [];
        // 这里需要后台过滤一下未读的消息，显示总数要做一个变化
        this.messageCount = this.messageList.length;
        if(this.messageCount!=""){
           this.processNotification(this.messageList);
        }
        // console.log(this.messageList);
      })
      .catch(err=>{
        console.log("失败的原因是"+ err.data);
      })
    },
    processNotification(note){
      for(var i=0;i<note.length;i++){
        if(note[i].type=="Join application"&&note[i].state=="unread"){
          this.applicationList.push(note[i]);
          this.messageCount = this.applicationList.length;
        }
      }
      // console.log("结果是："+ note[0]);
    },
    processNotificationModalShow(note){
      this.currentProcessNotification = note;
      this.processNotificationModal = true;
      this.ProcessNotificationTemplate = note.content.userName + " applied to join in the project called " + note.content.title + " at " + note.createTime, "do you allow he/her join in this project?"
      console.log("点击的项目名是:"+ this.currentProcessNotification.content.title);
    },
    allowJoin(data){
      this.axios
        .get(
          "http://localhost:8081/project/join?" +
            "projectId=" +
            data.content.projectId +
            "&userId=" +
            data.content.userId
        )
        .then(res => {
          if (res.data === "Success") {
            this.$Message.info("Join success");
            // 注册成功了以后马上到后台进行更新，将通知的状态由未读变为已读
            this.axios.get("http://localhost:8081/notice/read?"+"noticeId="+data.noticeId)
            .then(res=>{
              console.log("更新的结果是"+ res.data);
              this.messageCount = this.messageCount - 1;
              this.messageStatus = true;
              console.log("此时信息总数是："+ this.messageCount);
            })
            .catch(err=>{
              console.log(err.data);
            })
            // data.state = "read"
          } else if (res.data === "Exist") {
            this.$Message.info("you have already be a member in project");
            this.messageCount = this.messageCount - 1;
          }
        })
        .catch(err => {});
      // alert(data.createTime);
    },
    disallowJoin(){
      alert("disallow")
    },
    // judgeShow(state){
    //   if(state=="read"){
    //     return false
    //   }else if(state=="unread"){
    //     return true
    //   }
    // }
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
  margin-left: 85%;
  top: 0px;
  min-width: 300px;
  z-index: 1;
}
.content {
  padding-top: 5px;
  width: 95%;
  margin-left: 2.5%;
  margin-right: 2.5%;
}
/* 关于通知的徽标面板显示 */
.bottom {
  text-align: center;
}
.readCss{
  background-color: grey
  }
</style>
