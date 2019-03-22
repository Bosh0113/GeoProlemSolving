<style scoped>
.outer-style {
  max-height: 600px;
  background-color: lightgray;
  margin-top: 5%;
}
.main_part {
  display: flex;
  height:600px;
}
.members_panel {
  width: 20%;
  height: 600px;
  background-color: #515a6e;
  color: white;
  font-weight: bold;
}
/* .message_panel {
  width: 60%;
  background-color: cornsilk;
} */

.search_panel {
  width: 30%;
  background-color: white;
  border: 1px solid lightgray;
  max-height: 600px;
}
.member_search {
  margin-top: 2.5%;
  padding-left: 2.5%;
}
.member_search button {
  margin-left: 5%;
}
.f_list {
  display: flex;
  padding-left: 10%;
  padding-top: 15px;
  color: white;
}
.name {
  height: 28px;
  margin-top: 2px;
  line-height: 28px;
  padding-left: 5%;
  padding-right: 5%;
  margin-bottom: 2px;
  width: 100%;
  font-size: 18px;
  font-family: Helvetica;
}
.friends_title,
.group_title {
  padding-left: 20px;
  padding-top: 20px;
}
.message_navbar {
  background-color: lightgray;
  height: 50px;
  display: flex;
}
.view-btn {
  width: 15%;
}
.select_name {
  width: 60%;
  margin-left: 15%;
  margin-right: 5%;
}
.top_btn {
  margin: 10px;
  height: 30px;
}
.s_name {
  height: 30px;
  margin-top: 10px;
  margin-bottom: 10px;
  width: 20%;
  margin-left: 40%;
  line-height: 30px;
  font-size: 18px;
}
.message_list_panel {
  padding: 25px;
  height: 490px;
  overflow-y: auto;
  background-color: white;
}
.message_send_panel {
  /* overflow-y:hidden; */
  height: 60px;
  padding: 4px 20px 4px 10px;
  background-color: lightgray;
}
.message_bar {
  display: flex;
}
.send_panel {
  display: flex;
}
.u_avater {
  padding-top:5px;
  width: 2.5%;
  margin-right: 5%;
}
.send_message_btn {
  height: 32px;
  margin-top: 10px;
  margin-bottom: 18px;
}
.message_input {
  height: 40px;
  margin-top: 10px;
  margin-bottom: 10px;
  margin-right: 15px;
  margin-left: 15px;
}
.input_panel {
  width: 75%;
  margin-right: 5%;
}
.me {
  background-color: lightcoral;
  height: 32px;
  width: 32px;
  margin-top: 10px;
  margin-bottom: 18px;
}
.message_style {
  height: 40px;
  margin: 5px;
  /* background-color:lightgray; */
  display: flex;
}

.user_img {
  background-color: lightblue;
  margin-top: 2px;
  height: 32px;
  width: 32px;
  margin-left: 5px;
}

.chat-bubble-r {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-all;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: right;
  /* 这两句让文字从右面开始显示 */
  /* direction: rtl;
  unicode-bidi: bidi-override; */
  /* float:right; */
}

.chat-bubble-l {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-all;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: left;
}
.user_detail {
  height: 48px;
}
.u_name {
  height: 6px;
  margin-left: 10px;
}

.chat-bubble-left:before {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  left: -20px;
  top: 5px;
  border: 10px solid;
  border-color: transparent #989898 transparent transparent;
}
.chat-bubble-left:after {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  left: -16px;
  top: 7px;
  border: 8px solid;
  border-color: transparent #989898 transparent transparent;
}

.chat-bubble-right:before {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  right: -20px;
  top: 5px;
  border: 10px solid;
  border-color: transparent transparent transparent lightgray;
}
.chat-bubble-right:after {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  right: -16px;
  top: 7px;
  border: 8px solid;
  border-color: transparent transparent transparent lightgray;
}
.search_msg {
  padding: 10px;
  /* background-color: lightblue; */
  width: 90%;
  margin-left: 2.5%;
}
.date_pick {
  padding: 10px;
  width: 60%;
  margin-left: 2.5%;
  margin-right: 2.5%;
}
.date_pick_btn {
  width: 20%;
  margin-left: 5%;
  margin-right: 5%;
  height: 32px;
  margin-top: 10px;
}
.message_record_board {
  margin-left: 5%;
  margin-right: 5%;
  max-height: 496px;
  overflow-y: auto;
}
.single_record {
  padding: 5px;
}
</style>
<template>
  <div>
    <Row>
      <Col span="18" offset="3" class="outer-style">
        <div class="main_part">
          <div class="members_panel">
            <div class="group">
              <h3 class="group_title">Groups</h3>
              <div v-for="(group,index) in groups" :key="index" class="f_list" >
                <div>
                  <avatar :username= group.scope :size="35" :rounded="false" :title= group.title></avatar>
                </div>
                <div class="name" :title= group.scope style="cursor:pointer" @click="changeChatroom(index)">{{group.name}}</div>
              </div>
            </div>
            <div class="participants">
              <h3 class="friends_title">Participants</h3>
              <div v-for="(participant,index) in participants" :key="index" class="f_list">
                <div>
                  <img v-if="participant.avatar != '' && participant.avatar!='undefined'" :src="participant.avatar" style="width:auto;height:100%">
                  <avatar v-else :username= participant.userName :size="35" :rounded="false"></avatar>
                </div>
                <div class="name">{{participant.userName}}</div>
              </div>
            </div>
          </div>
          <div class="message_panel" :style="message_panelObj">
            <div class="message_navbar">
              <div class="select_name">
                <div class="s_name" v-show="this.select_group!==''">{{this.select_group}}</div>
              </div>
              <div class="view-btn">
                <Button class="top_btn" @click="showRecords">Records</Button>
              </div>
            </div>
            <div class="message_list_panel" >
              <!-- 这里设计聊天的信息窗口 -->
              <div style="display:flex" v-for="(list,index) in msglist" :key="index">
                <template v-if="list.fromid === thisUserId">
                  <div style="width:95%">
                    <div class="chat-bubble-r chat-bubble-right">{{list.content}}</div>
                  </div>
                  <div class="user_detail">
                    <div class="u_img">
                      <avatar class="user_img" :username= list.from :size="35"></avatar>
                    </div>
                    <div class="u_name">{{list.from}}</div>
                  </div>
                </template>
                <template v-else>
                  <div class="user_detail">
                    <div class="u_img">
                      <avatar class="user_img" :username= list.from :size="35"></avatar>
                    </div>
                    <div class="u_name">{{list.from}}</div>
                  </div>
                  <div style="width:95%">
                    <div class="chat-bubble-l chat-bubble-left">{{list.content}}</div>
                  </div>
                </template>
              </div>
            </div>
            <div class="message_send_panel">
              <div class="message_bar">
                <div class="u_avater">
                  <avatar class="user_img" username="Me" :size="40"></avatar>
                </div>
                <div class="input_panel">
                  <Input
                    placeholder="Enter message..."
                    icon="ios-link"
                    class="message_input"
                    v-model="message"
                  />
                </div>
                <div class="send_panel">
                  <Button class="send_message_btn" @click="send(message)">Send</Button>
                </div>
              </div>
            </div>
          </div>
          <div class="search_panel" v-show="recordsPanel">
            <div style="display:flex">
              <DatePicker
                type="date"
                placeholder="Select date"
                class="date_pick"
                v-model="query_date"
              ></DatePicker>
              <Button class="date_pick_btn" type="success" @click="find(query_date)">ok</Button>
            </div>
            <div class="search_msg">
              <Input search placeholder="Enter something..."/>
            </div>
            <div class="message_record_board">
              <div style="display:flex" v-for="(list,index) in msglist" :key="index">
                <div class="single_record">
                  <span style="color:red;margin-right:2%">{{list.from}}:</span>
                  {{list.content}}
                </div>
              </div>
            </div>
          </div>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
import * as socketApi from "./../../api/socket.js";
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  methods: {
    init(){
      this.moduleId = sessionStorage.getItem("moduleId");
      this.subProjectId = sessionStorage.getItem("subProjectId");
      this.projectId = sessionStorage.getItem("projectId");
      this.moduleName = sessionStorage.getItem("moduleName");
      this.subProjectName = sessionStorage.getItem("subProjectName");
      this.projectName = sessionStorage.getItem("projectName");
      //groups
      this.groups = [
        { name: this.moduleName, id: this.moduleId, scope:"Module", title: "Chat in the module"},
        { name: this.subProjectName, id:this.subProjectId, scope:"Subproject", title:"Chat in the subproject"},
        { name: this.projectName, id:this.projectId, scope:"Project", title:"Chat in the project"}
      ];
      //participants
      this.getParticipants(this.moduleId,"module");
    },
    changeChatroom(index){
      this.socketApi.close();
      if(index == 0){
        this.getParticipants(this.moduleId,"module");
        this.select_group = this.moduleName;
        this.startWebSocket(this.moduleId);
      }
      else if(index == 1){
        this.getParticipants(this.subProjectId,"subproject");
        this.select_group = this.subProjectName;
        this.startWebSocket(this.subProjectId);
      }
      else if(index == 2){
        this.getParticipants(this.projectId,"project");
        this.select_group = this.projectName;
        this.startWebSocket(this.projectId);
      }
    },
    getParticipants(id,scope) {
      if(scope == "module"){

      }
      else if(scope=="subproject"){
        let that = this;
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            that.subProjectId,
          type: "GET",
          success: data => {
            if (data != "None") {
              let subProjectInfo = data[0];
              let membersList = subProjectInfo["members"];
              let manager = { userId: subProjectInfo["managerId"] };
              membersList.unshift(manager);
              let participantsTemp = [];
              for (let i = 0; i < membersList.length; i++) {
                $.ajax({
                  url:
                    "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId,
                  type: "GET",
                  async: false,
                  success: function(data) {
                    participantsTemp.push(data);
                  }
                });
              }
              that.$set(that, "participants", participantsTemp);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
      else if(scope=="project"){
        let that = this;
        let queryObject = { key: "projectId", value: that.projectId };
        try {
          $.ajax({
            url:
              "/GeoProblemSolving/project/inquiry" +
              "?key=" +
              queryObject["key"] +
              "&value=" +
              queryObject["value"],
            type: "GET",
            success: function(data) {
              if (data != "None" && data != "Fail") {
                let projectInfo = data[0];
                let membersList = projectInfo["members"];
                let manager = projectInfo["managerId"];
                membersList.unshift(manager);
                let participantsTemp = [];
                for (let i = 0; i < membersList.length; i++) {
                  $.ajax({
                    url:
                      "/GeoProblemSolving/user/inquiry" +
                      "?key=" +
                      "userId" +
                      "&value=" +
                      membersList[i],
                    type: "GET",
                    async: false,
                    success: function(data) {
                      participantsTemp.push(data);
                    },
                    error: function(err) {
                      console.log("Get manager name failed.");
                    }
                  });
                }
                that.$set(that, "participants", participantsTemp);
              }
            },
            error: function(err) {
              console.log("Get manager name fail.");
            }
          });
        } catch (err) {
          console.log(err);
        }
      }
    },
    showRecords(){
      this.recordsPanel = !this.recordsPanel;
      this.message_panelObj.width= '80%';
      this.message_panelObj['borderRight']='1px solid lightgray';
    },
    send(msg) {
      // console.log(msg);
      this.message = msg;
      let myDate = new Date();
      let current_time = myDate.toLocaleString(); //获取日期与时间

      this.send_msg = {
        type:"message",
        from: this.$store.state.userName,
        fromid: this.$store.state.userId,
        content: this.message,
        time: current_time
      };

      // 消息不为空
      if (this.message !== "") {
        this.my_msglist.push(this.send_msg);
        this.msglist.push(this.send_msg);

        this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
      }
      this.message = "";
    },
    getSocketConnect(data) {
      var chatMsg = JSON.parse(data);
      if (chatMsg.from === "Test") {
        console.log(chatMsg.content);
      } else {
        //判断消息的发出者
        var uid = chatMsg.fromid;
        if (
          uid !== this.$store.state.userId &&
          uid !== undefined
          // uid !== ""
        ) {
          this.other_msglist.push(chatMsg);
          this.msglist.push(chatMsg);
        }
      }
    },
    startWebSocket(id) {
      this.socketApi.initWebSocket("ChatServer/" + id);

      this.send_msg = {
        type:"test",
        from: "Test",
        fromid: this.$store.state.userId,
        content: "TestChat"
      };
      this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
    },
    find(date) {
      let q_date = date.toLocaleDateString();
      alert(q_date);
    },
    sortMsglist() {}
  },
  created() {
  },
  mounted(){
    this.init();
    this.startWebSocket(this.moduleId);
  },
  beforeDestroy() {
    this.socketApi.close();
  },
  beforeRouteEnter: (to, from, next) => {
    // alert(this.isSubProjectMember);
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.state.userId == "") {
        vm.$router.push({name:"Login"});
      } else {

      }
    });
  },
  data() {
    return {
      recordsPanel: false,
      projectId: "",
      subProjectId: "",
      moduleId: "",
      participants: [],
      groups: [],
      select_group: "",
      message: "",
      my_msglist: [],
      other_msglist: [],
      msglist: [],
      send_msg: [],
      query_date: "",
      thisUserName: this.$store.state.userName,
      thisUserId: this.$store.state.userId,
      // 关于折叠栏
      // collapsePanel:true,
      message_panelObj: {
        width: "80%" ,
        borderRight:'1px solid lightgray',
        // backgroundColor:'cornsilk'
      }
    };
  }
};
</script>

