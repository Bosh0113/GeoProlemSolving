<style>
.outer-style {
  max-height: 600px;
  background-color: lightgray;
  margin-top: 5%;
}
.main_part {
  display: flex;
}
.member_panel {
  width: 20%;
  max-height: 600px;
  background-color: #515a6e;
  color: white;
  font-weight: bold;
}
.message_panel {
  width: 60%;
  background-color: cornsilk;
  /* max-height:600px; */
}
.search_panel {
  width: 20%;
  background-color: white;
  border: 1px solid lightgray;
  max-height: 600px;
}

.member_title {
  color: white;
  font-family: Tahoma;
  font-size: 20px;
  /* font-weight:bold; */
  padding: 10%;
}
.member_search {
  margin-top: 2.5%;
  padding-left: 2.5%;
}
.search_member {
  width: 70%;
  margin-left: 5%;
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
  padding-top: 10px;
}
.message_navbar {
  background-color: lightgray;
  height: 50px;
  display: flex;
}
.back-btn {
  width: 15%;
}
.view-btn {
  width: 15%;
}
.select_name {
  width: 60%;
  margin-left: 5%;
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
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 2px;
  padding-top: 2px;
  background-color: lightgray;
}
.message_bar {
  display: flex;
}
.send_panel {
  display: flex;
}
.u_avater {
  width: 2.5%;
  margin-right: 2.5%;
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
      <Col span="14" offset="5" class="outer-style">
        <div class="main_part">
          <div class="member_panel">
            <div class="member_title">Members</div>
            <div class="member_search" style="display:flex">
              <Input
                v-model="friend_search"
                placeholder="Enter name"
                clearable
                class="search_member"
              />
              <Button type="primary" shape="circle" icon="ios-search" class="search"></Button>
            </div>
            <div class="group">
              <div class="group_title">Groups</div>
              <div v-for="group in groups" class="f_list" @click="selectGroup(group.name)">
                <div>
                  <Avatar :style="{background: group.color}">{{group.avater}}</Avatar>
                </div>
                <div class="name">{{group.name}}</div>
              </div>
            </div>
            <div class="friends">
              <div class="friends_title">Friends</div>
              <div v-for="friend in friends" class="f_list" @click="select(friend.name)">
                <div>
                  <Avatar :style="{background: friend.color}">{{friend.avater}}</Avatar>
                </div>
                <div class="name">{{friend.name}}</div>
              </div>
            </div>
          </div>
          <div class="message_panel">
            <div class="message_navbar">
              <div class="back-btn">
                <Button class="top_btn">Back</Button>
              </div>
              <div class="select_name">
                <div class="s_name" v-show="this.select_name!==''">{{this.select_name}}</div>
                <div class="s_name" v-show="this.select_group!==''">{{this.select_group}}</div>
              </div>
              <div class="view-btn">
                <Button class="top_btn">Profile</Button>
              </div>
            </div>

            <div class="message_list_panel">
              <!-- 这里设计聊天的信息窗口 -->
              <div style="display:flex" v-for="list in msglist">
                <template v-if="list.fromid === thisUserId">
                  <div style="width:95%">
                    <div class="chat-bubble-r chat-bubble-right">{{list.content}}</div>
                  </div>
                  <div class="user_detail">
                    <div class="u_img">
                      <Avatar class="user_img">S</Avatar>
                    </div>
                    <div class="u_name">{{list.from}}</div>
                  </div>
                </template>
                <template v-else>
                  <div class="user_detail">
                    <div class="u_img">
                      <Avatar class="user_img">S</Avatar>
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
                  <Avatar class="me">M</Avatar>
                </div>
                <div class="input_panel">
                  <Input
                    placeholder="Enter something..."
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
          <div class="search_panel">
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
              <div style="display:flex" v-for="list in msglist">
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
export default {
  mounted() {
    this.getFriendlist();
  },
  methods: {
    getSocketConnect() {},
    getFriendlist() {
      // this.axios.post("http://localhost:8081/TeamModeling/FriendServlet", {
      // params: {
      //   // id:paramId
      // }
      // })
      // .then(function (res) {
      //   console.log(res.data);
      // // this.friends = res.data;
      // // this.friends =
      // })
      // .catch(function (error) {
      //   alert(error);
      // })
    },
    select(data) {
      // console.log(data);
      // this.select_group = "";
      // this.select_name = data;
      // //这里的逻辑还有待设计，不应当清空，应该缓存到session，点击切换回来时，对应再去找
      // // this.my_msglist = [];
    },
    selectGroup(data) {
      alert(data);
      this.select_name = "";
      this.select_group = data;
    },
    send(msg) {
      // console.log(msg);
      this.message = msg;
      let myDate = new Date();
      let current_time = myDate.toLocaleString(); //获取日期与时间

      this.send_msg = {
        from: this.$store.state.userName,
        fromid: this.$store.state.userId,
        content: this.message,
        time: current_time
      };

      // 消息不为空
      if (this.message !== "") {
        this.my_msglist.push(this.send_msg);
        this.msglist.push(this.send_msg);

        this.socketApi.sendSock(this.send_msg, this.socketCallback);
      }
      this.message = "";
    },
    socketCallback(data) {
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
    find(date) {
      let q_date = date.toLocaleDateString();
      alert(q_date);
    }
  },
  sortMsglist() {},
  created() {
    let roomId = localStorage.getItem("subProjectId");
    //截去随机位
    if (roomId !== null && roomId !== undefined) {
      roomId = roomId.slice(0, -4);
    }
    this.socketApi.initWebSocket("ChatServer/" + roomId);

    this.send_msg = {
      from: "Test",
      fromid: this.$store.state.userId,
      content: "TestChat"
    };
    this.socketApi.sendSock(this.send_msg, this.socketCallback);
  },
  mounted(){
    console.log(this.msglist);
  },
  destroyed() {
    this.socketApi.close();
  },
  data() {
    return {
      friend_search: "",
      friends: [
        { name: "lyc", avater: "L", color: "lightgreen" },
        { name: "zbc", avater: "Z", color: "lightblue" },
        { name: "mzy", avater: "M", color: "orange" }
      ],
      // friends:"",
      groups: [
        { name: "team1", avater: "1", color: "lightgreen" },
        { name: "team2", avater: "2", color: "lightblue" },
        { name: "team3", avater: "3", color: "orange" }
      ],
      select_name: "",
      select_group: "",
      message: "",
      my_msglist: [],
      other_msglist: [],
      msglist: [],
      send_msg: [],
      query_date: "",
      thisUserName: this.$store.state.userName,
      thisUserId: this.$store.state.userId
    };
  }
};
</script>

