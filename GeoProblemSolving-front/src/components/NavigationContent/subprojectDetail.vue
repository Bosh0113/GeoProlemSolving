<style scoped>
.detail {
  display: flex;
}
.sidebar {
  font-weight: bold;
  width: 15%;
}
.right {
  width: 90%;
  margin-left: 5%;
  height: 100%;
}
.single_part {
  margin-left: 2.5%;
  margin-right: 2.5%;
}
.addNodeStyle,
.editNodeStyle {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 2.5%;
}
.addBtn,
.removeBtn,
.editBtn .createTaskBtn {
  font-size: 10px;
}
.addBtn:hover {
  color: white;
  background: #47cb89;
}
.removeBtn:hover {
  color: white;
  background: #f16643;
}
.editBtn:hover {
  color: white;
  background: #2d8cf0;
}
.createTaskBtn:hover {
  color: white;
  background: #47cb89;
}
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}
.member-desc {
  height: 60px;
  margin: 0 20px 0 10px;
  display: flex;
}
.member-image {
  width: 60px;
  height: 60px;
  padding: 5px;
}
.memebr-work {
  width: 70%;
  height: 60px;
  margin: 0 20px;
}
.userName {
  height: 30px;
  display: flex;
  align-items: center;
}
.organization {
  height: 30px;
  display: flex;
  align-items: center;
}
.member-panel {
  border: 1px solid lightgray;
  transition: all 1s;
}
.subProjectDesc {
  text-indent: 2em;
  padding: 10px;
  word-break: break-all;
}
.resource {
  transition: all 1s;
}
.taskFormItem {
  display: flex;
  align-items: center;
}
.taskFormItem span {
  text-align: center;
}
.whiteSpace {
  height: 10px;
}
.taskList {
  min-height: 80px;
  background: #f7f7f7;
}
.taskName {
  display: inline-block;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 120px;
}
.operatePanel button {
  margin-right: 2.5%;
}
</style>
<template>
  <div style="background-color:#dcdee2">
    <Row>
      <Col span="22" offset="1">
        <Card>
          <p
            slot="title"
           style="height:40px;line-height:40px;font-size:20px;display: inline-block;cursor: pointer;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;max-width: 50%;"
          >{{subProjectInfo.title}}</p>
          <div
            slot="extra"
            style="height:40px;display:flex;align-items:center"
            class="operatePanel"
          >
              <Button
                type="default"
                @click="conveneWork()"
                icon="md-mail"
                title="Start to work"
              >Convene</Button>
              <Button
                type="default"
                @click="backProject()"
                icon="md-arrow-back"
                title="Back to project page"
              >Back</Button>
          </div>
          <Row>
            <Col span="22" offset="1" style="margin-top:10px;background-color:white;">
              <Steps :current="order">
                <Step title="Home" icon="ios-home" @click.native="showDetail(0)" :order="0"></Step>
                <Step title="Task" icon="ios-home" @click.native="showDetail(1)" :order="1"></Step>
                <Step title="Start working" icon="ios-home" @click.native="showDetail(2)" :order="2"></Step>
              </Steps>
            </Col>
          </Row>
        </Card>
      </Col>
      <div v-if="order == 0" class="workspaceContent">
        <Col
          :xs="8"
          :sm="7"
          :md="7"
          :lg="5"
          v-bind="this.participants"
          offset="1"
          style="margin-top:20px"
          :style="{height:sidebarHeight+4+'px'}"
        >
          <div
            class="member-panel"
            :style="{height:sidebarHeight-6+'px'}"
            style="background-color:white"
          >
            <div class="title">Participants</div>
            <div :style="{height:sidebarHeight-100+'px'}">
              <div
                class="member-desc"
                v-for="(member,index) in this.participants"
                :key="member.index"
              >
                <template v-if="index==0">
                  <Badge text="♔" type="warning" class="userAvatar">
                    <div
                      class="member-image"
                      @click="gotoPersonalSpace(member.userId)"
                      style="cursor:pointer"
                    >
                      <img
                        v-if="member.avatar != '' && member.avatar!='undefined'"
                        :src="member.avatar"
                        style="width:auto;height:100%"
                      >
                      <avatar
                        :username="member.userName"
                        :size="40"
                        style="margin-top:10px"
                        :title="member.userName"
                        v-else
                      ></avatar>
                    </div>
                  </Badge>
                  <div class="memebr-work">
                    <div class="userName">
                      <span style="padding:0 5px;float:right">{{member.userName}}</span>
                    </div>
                    <div class="organization">
                      <span style="padding:0 5px">{{member.organization}}</span>
                    </div>
                  </div>
                </template>
                <template v-else style="margin-top:5px">
                  <div
                    class="member-image"
                    @click="gotoPersonalSpace(member.userId)"
                    style="cursor:pointer"
                  >
                    <img
                      v-if="member.avatar != ''"
                      :src="member.avatar"
                      style="width:auto;height:100%"
                    >
                    <avatar
                      :username="member.userName"
                      :size="40"
                      style="margin-top:10px"
                      :title="member.userName"
                      v-else
                    ></avatar>
                  </div>
                  <div class="memebr-work">
                    <div class="userName">
                      <span style="padding:0 5px;float:right">{{member.userName}}</span>
                    </div>
                    <div class="organization">
                      <span style="padding:0 5px">{{member.organization}}</span>
                    </div>
                  </div>
                </template>
              </div>
            </div>
            <div
              class="member-invite"
              style="display:flex;justify-content:center;height:60px;align-items:center"
            >
              <Button
                type="success"
                style="text-align:center;width:100px"
                @click="inviteMembersModalShow()"
                :disabled="inviteAble"
                v-if="this.subProjectInfo.managerId == this.$store.getters.userId"
              >Invite</Button>
              <Button
                type="warning"
                style="text-align:center;width:100px"
                @click="quitModal=true"
                v-else-if="this.subProjectInfo.isMember"
              >Quit</Button>
              <Modal
                v-model="quitModal"
                width="400px"
                title="Quit subProject"
                @on-ok="quitSubProject()"
                @on-cancel="cancel"
              >
                <h2>Are you sure to quit this subproject?</h2>
              </Modal>
              <Modal
                v-model="inviteModal"
                width="400px"
                title="Invite group member join in the subProject"
                @on-ok="inviteMembers"
                ok-text="ok"
                cancel-text="cancel"
                @on-cancel="cancel"
              >
                <div>
                  <p>Members:</p>
                  <Tag
                    v-for="participant in this.participants"
                    :key="participant.index"
                  >{{participant.userName}}</Tag>
                  <p>Candidates:</p>
                  <CheckboxGroup v-model="inviteList">
                    <Checkbox
                      v-for="candidate in candidates"
                      :key="candidate.index"
                      :label="candidate.userId"
                    >
                      <span>{{candidate.userName}}</span>
                    </Checkbox>
                  </CheckboxGroup>
                </div>
              </Modal>
            </div>
          </div>
        </Col>
        <Col :xs="14" :sm="15" :md="16" :lg="16" offset="1" style="margin-top:20px">
          <div style="background-color:white;padding:20px">
            <h2 style="margin-bottom:5px">Description</h2>
            <hr style="margin-bottom:10px">
            <div
              :style="{height:sidebarHeight-140+'px'}"
              class="subProjectDesc"
            >{{subProjectInfo.description}}</div>
          </div>
          <div style="display:flex;align-items:center;justify-content:center;height:60px">
            <Button
              type="error"
              style="margin:auto"
              v-show="subProjectInfo.managerId == this.$store.getters.userId"
            >Delete this sub-project ?</Button>
          </div>
        </Col>
      </div>
      <template v-else-if="order == 1">
        <Col span="22" offset="1" style="margin-top:20px" :style="{height:sidebarHeight+4+'px'}">
          <div
            style="padding: 5px 0;background-color:white"
            :style="{height:sidebarHeight-6+'px'}"
          >
            <Row type="flex" justify="center">
              <Col span="7">
                <Card :padding="0" :border="false">
                  <h3 slot="title">Todo</h3>
                  <Button
                    slot="extra"
                    type="default"
                    class="createTaskBtn"
                    style="margin-top:-10px"
                    @click="createTaskModalShow()"
                    v-show="this.subProjectInfo.managerId == this.$store.getters.userId||this.subProjectInfo.isMember"
                  >Add</Button>
                  <draggable
                    class="taskList"
                    element="ul"
                    :options="{group:'task'}"
                    v-model="taskTodo"
                    @start="setMoveCount()"
                    @update="updateMoveTask(taskTodo,'todo')"
                    @add="addMoveTask(taskTodo,'todo')"
                    @remove="removeMoveTask(taskTodo,'todo')"
                  >
                    <Card v-for="(item,index) in taskTodo" :key="index" :padding="3">
                      <div>
                        <strong                         
                          class="taskName"
                        >{{item.taskName}}</strong>
                        <span
                          style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                          @click="taskRemove(index, taskTodo)"
                        >
                          <Icon type="ios-trash"/>
                        </span>
                      </div>
                      <p style="word-break:break-word;padding:5px;cursor:pointer" @click="editOneTask(index, taskTodo)">{{item.description}}</p>
                    </Card>
                  </draggable>
                </Card>
              </Col>
              <Col span="7" style="margin-left:20px">
                <Card :padding="0" :border="false">
                  <h3 slot="title">Doing</h3>
                  <draggable
                    class="taskList"
                    element="ul"
                    :options="{group:'task'}"
                    v-model="taskDoing"
                    @start="setMoveCount()"
                    @update="updateMoveTask(taskDoing,'doing')"
                    @add="addMoveTask(taskDoing,'doing')"
                    @remove="removeMoveTask(taskDoing,'doing')"
                  >
                    <Card v-for="(item,index)  in taskDoing" :key="index" :padding="3">
                      <div>
                        <strong                         
                          class="taskName"
                        >{{item.taskName}}</strong>
                        <span
                          style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                          @click="taskRemove(index,taskDoing)"
                        >
                          <Icon type="ios-trash"/>
                        </span>
                      </div>
                      <p style="word-break:break-word;padding:5px;cursor:pointer" @click="editOneTask(index,taskDoing)">{{item.description}}</p>
                    </Card>
                  </draggable>
                </Card>
              </Col>
              <Col span="7" style="margin-left:20px">
                <Card :padding="0" :border="false">
                  <h3 slot="title">Done</h3>
                  <draggable
                    class="taskList"
                    element="ul"
                    :options="{group:'task'}"
                    v-model="taskDone"
                    @start="setMoveCount()"
                    @update="updateMoveTask(taskDone,'done')"
                    @add="addMoveTask(taskDone,'done')"
                    @remove="removeMoveTask(taskDone,'done')"
                  >
                    <Card v-for="(item,index) in taskDone" :key="index" :padding="3">
                      <div>
                        <strong                         
                          class="taskName"
                        >{{item.taskName}}</strong>
                        <span
                          style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                          @click="taskRemove(index,taskDone)"
                        >
                          <Icon type="ios-trash"/>
                        </span>
                      </div>
                      <p style="word-break:break-word;padding:5px;cursor:pointer" @click="editOneTask(index,taskDone)">{{item.description}}</p>
                    </Card>
                  </draggable>
                </Card>
              </Col>
            </Row>
          </div>
        </Col>
      </template>
    </Row>
    <Modal
      v-model="createTaskModal"
      title="Create task panel"
      @on-ok="createTask()"
      @on-cancel="cancel()"
      ok-text="ok"
      cancel-text="cancel"
      width="800px"
    >
      <div class="taskFormItem">
        <span style="width:30%">Name</span>
        <Input
          style="width: 300px"
          :placeholder="this.taskPlaceHolder.name"
          v-model="taskInfo.taskName"
        />
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">Description</span>
        <Input
          style="width: 300px"
          :placeholder="this.taskPlaceHolder.description"
          type="textarea"
          :rows="4"
          v-model="taskInfo.description"
        />
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">Start Time</span>
        <DatePicker
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          :placeholder="this.taskPlaceHolder.startTime"
          style="width: 300px"
          v-model="taskInfo.startTime"
        ></DatePicker>
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">End Time</span>
        <DatePicker
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          :placeholder="this.taskPlaceHolder.endTime"
          style="width: 300px"
          v-model="taskInfo.endTime"
        ></DatePicker>
      </div>
      <div class="whiteSpace"></div>
    </Modal>
    <Modal
      v-model="editTaskModal"
      title="Edit task panel"
      @on-ok="updateTask()"
      @on-cancel="cancel()"
      width="800px"
    >
      <div class="taskFormItem">
        <span style="width:30%">taskName</span>
        <Input
          style="width: 300px"
          :placeholder="this.taskPlaceHolder.name"
          v-model="taskInfo.taskName"
        />
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">description</span>
        <Input
          style="width: 300px"
          :placeholder="this.taskPlaceHolder.description"
          type="textarea"
          :rows="4"
          v-model="taskInfo.description"
        />
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">start Time</span>
        <DatePicker
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          :placeholder="this.taskPlaceHolder.startTime"
          style="width: 300px"
          v-model="taskInfo.startTime"
        ></DatePicker>
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">end Time</span>
        <DatePicker
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          :placeholder="this.taskPlaceHolder.endTime"
          style="width: 300px"
          v-model="taskInfo.endTime"
        ></DatePicker>
      </div>
      <div class="whiteSpace"></div>
      <div class="taskFormItem">
        <span style="width:30%">state</span>
        <RadioGroup v-model="taskInfo.state" disabled>
          <Radio label="todo"></Radio>
          <Radio label="doing"></Radio>
          <Radio label="done"></Radio>
        </RadioGroup>
      </div>
    </Modal>
  </div>
</template>
<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import draggable from "vuedraggable";
import Avatar from "vue-avatar";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "15px 15px 0 0");
    $(".ivu-steps-title").css("cursor", "pointer");
    $(".ivu-steps-title").css("overflow", "hidden");
    $(".ivu-steps-title").css("white-space", "nowrap");
    $(".ivu-steps-title").css("text-overflow", "ellipsis");
    $(".ivu-steps-title").css("max-width", "120px");
  },
  components: {
    VueFlowy,
    draggable,
    Avatar
  },
  data() {
    return {
      // information of project
      projectInfo: {},
      // info of subproject
      subProjectInfo: [],
      //登陆者身份
      // 关于邀请的模态框
      inviteModal: false,
      quitModal: false,
      sidebarHeight: 800,
      participants: [],
      candidates: [],
      inviteList: [],
      inviteAble: true,
      // current: 0,
      addModal: false,
      delModal: false,
      //编辑的模态框
      editModal: false,
      order: 0,
      //typeList是选择模块种类的列表，select从这里渲染
      typeList: [
        "Preparation",
        "Analysis",
        "Modeling",
        "Simulation",
        "Comparison",
        "Verification"
      ],
      //
      moduleTitle: "",
      updateModuleTitle: "",
      // type是指选中后的列表
      moduleType: "",
      updateModuleType: "",
      // moduleDescription指的是节点的详情信息
      moduleDescription: "",
      updateModuleDescription: "",
      // 抽屉的控制开关
      drawerOpen: false,
      // 后台获取的module下的task列表
      taskList: [],
      // 后台拿到的Module集合，渲染成一条轴用的
      moduleList: [],
      // 创建任务的模态框
      createTaskModal: false,
      // 编辑任务的模态框
      editTaskModal: false,
      // task的placeHolder默认值
      taskPlaceHolder: {
        description: "please input the task description.",
        name: "please input task's name",
        startTime: "choose the task's start time",
        endTime: "set the time of the task's end time"
      },
      //task相关
      taskInfo: {},
      taskTodo: [],
      taskDoing: [],
      taskDone: [],
      MoveCount: 0,
      // 动态记录相关
      // 消息
      subprojectSocket: null,
      timer: null,
      socketMsg: {
        type: "",
        time: "",
        who: "",
        whoid: "",
        content: ""
      }
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.inquiryTask();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (!(vm.subProjectInfo.managerId == vm.$store.getters.userId || vm.subProjectInfo.isMember)) {
          alert("No access");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        }
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    // this.removeTimer();
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 227;
      //通知栏的属性设置，top表示距离顶部的距离，duration表示持续的时间
      this.$Notice.config({
        top: 50,
        duration: 2
      });
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.inviteAble = false;
        this.showMembers();
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "None") {
              subProjectInfo = data[0];
              this.$set(this, "subProjectInfo", subProjectInfo);
              sessionStorage.setItem("subProjectId", subProjectInfo.subProjectId);
              sessionStorage.setItem("subProjectName", subProjectInfo.title);

              // this.managerIdentity(subProjectInfo.managerId);
              this.memberIdentity(subProjectInfo.members);
              this.$store.commit("setSubProjectInfo", subProjectInfo);
              this.inviteAble = false;
              this.showMembers();
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        this.subProjectInfo.isManager = true;
      }
    },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          this.subProjectInfo.isMember = true;
          break;
        }
      }
    },
    showMembers() {
      let membersList = this.subProjectInfo.members;
      let manager = { userId: this.subProjectInfo.managerId };
      if (membersList.length == 0 || membersList[0].userId != this.subProjectInfo.managerId) {
        membersList.unshift(manager);
      }
      let participantsTemp = [];
      let index = membersList.length - 1;

        this.axios
          .get(
            "/GeoProblemSolving/user/inquiry" +
              "?key=" +
              "userId" +
              "&value=" +
            membersList[0].userId
        )
        .then(res => {
          participantsTemp.push(res.data);
          this.$set(this, "participants", participantsTemp);

          for (let i = 1; i < membersList.length; i++) {
            this.axios
              .get(
                "/GeoProblemSolving/user/inquiry" +
                  "?key=" +
                  "userId" +
                  "&value=" +
              membersList[i].userId
              )
              .then(res => {
                participantsTemp.push(res.data);
                if (index-- == 1) {
                  this.$set(this, "participants", participantsTemp);
                }
              })
              .catch(err => {});
          }
        })
        .catch(err => {});
    },
    showDetail(item) {
      this.order = item;
      if (item == 1) {        
        this.openModuleSocket(this.subProjectInfo.subProjectId);
      }
      else if( item == 2){
        this.closeModuleSocket();
        this.$router.push(`./workspace`);
      }
      else if(item == 0){
        this.closeModuleSocket();
      }
    },
    closeModuleSocket() {
      if (this.subprojectSocket != null) {
        this.subprojectSocket.close();
      }
    },
    openModuleSocket(subprojectId) {
      if (this.subprojectSocket != null) {
        this.subprojectSocket = null;
      }
      // var subprojectSocketURL = "ws://localhost:8081/GeoProblemSolving/Module/" + subprojectId;
      // var subprojectSocketURL = "ws://202.195.237.252:8082/GeoProblemSolving/Module/" + subprojectId;
      var subprojectSocketURL = "ws://172.21.212.7:8082/GeoProblemSolving/Module/" + subprojectId;
      this.subprojectSocket = new WebSocket(subprojectSocketURL);
      this.subprojectSocket.onopen = this.onOpen;
      this.subprojectSocket.onmessage = this.onMessage;
      this.subprojectSocket.onclose = this.onClose;
      this.subprojectSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("ModuleSocket连接成功！");
    },
    // 更新人员，更新数据，更新records
    onMessage(e) {
      let messageJson = JSON.parse(e.data);
      let record = {
        type: "",
        time: "",
        who: "",
        content: ""
      };
      
      if (messageJson.type == "message") {
        let message = messageJson.message;

        // 任务记录
        if (
          message.type == "tasks" &&
          message.whoid != this.$store.getters.userId
        ) {
          this.inquiryTask();
        }
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("ModuleSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("ModuleSocket连接错误！");
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (that.subprojectSocket != null && that.subprojectSocket != undefined) {
          that.subprojectSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    sendMessage(message) {
      var messageJson = {};
      messageJson["type"] = "message";
      messageJson["message"] = message;
      this.subprojectSocket.send(JSON.stringify(messageJson));
    },
    // 返回项目页
    backProject() {
      let projectInfo = this.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.subProjectInfo.projectId
      ) {
        let id = projectInfo.projectId;
        this.$router.push(`../${id}`);

      } else {
        this.axios
          .get(
            "/GeoProblemSolving/project/inquiry" +
              "?key=projectId" +
              "&value=" +
              this.subProjectInfo.projectId
          )
          .then(res => {
            if (res.data != "None" && res.data != "Fail") {
              this.projectInfo = res.data[0];
              this.$store.commit("setProjectInfo", res.data[0]);

              let id = this.projectInfo.projectId;
              this.$router.push(`../${id}`);
            } else {
              console.log(res.data);
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    // 召集参与者
    conveneWork() {
      for (let i = 0; i < this.participants.length; i++) {
        if (this.participants[i].userId != this.$store.getters.userId) {
          let notice = {};
          notice["recipientId"] = this.participants[i].userId;
          notice["type"] = "Work";
          notice["content"] = {
            subProjectId: this.subProjectInfo.subProjectId,
            title: "Work Notice",
            description:
              "The sub-project " +
              this.subProjectInfo.title +
              " of project " +
              this.projectInfo.title +
              " in which you participate has new progress!"
          };
          this.axios
            .post("/GeoProblemSolving/notice/save", notice)
            .then(res => {
              this.$Message.info("Apply Successfully");
              this.$emit("sendNotice", data.managerId);
            })
            .catch(err => {
              console.log("申请失败的原因是：" + err.data);
            });
        }
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {
      this.$Message.info("Clicked cancel");
      this.moduleTitle = "";
      this.moduleDescription = "";
      this.moduleType = "";
      this.editModuleTitle = "";
      this.editModuleDescription = "";
      this.editModuleType = "";
    },
    createModuleSuccess(title) {
      this.$Notice.success({
        title: "Create Notification",
        desc:
          "The module" +
          `<span style="color:lightblue"><strong>` +
          "&nbsp" +
          title +
          "&nbsp" +
          `</strong></span>` +
          "has been created successfully"
      });
    },
    //加载并打开成员邀请Modal
    inviteMembersModalShow() {
      let that = this;
      this.candidates = [];
      this.inviteList = [];
      let projectInfo = this.$store.getters.project;
      
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.subProjectInfo.projectId
      ) {        
        let allMembers = projectInfo.members;
        let manager = {
          userName: projectInfo.managerName,
          userId: projectInfo.managerId
        };
        allMembers.unshift(manager);
        for (let i = 0; i < allMembers.length; i++) {
          let exist = false;
          for (let j = 0; j < that.participants.length; j++) {
            if (allMembers[i].userId === that.participants[j].userId) {
              exist = true;
            }
          }
          if (!exist) {
            that.candidates.push(allMembers[i]);
          }
        }
      } else {
        this.axios
          .get(
            "/GeoProblemSolving/project/inquiry" +
              "?key=projectId" +
              "&value=" +
              this.subProjectInfo.projectId
          )
          .then(res => {
            if (res.data != "None" && res.data != "Fail") {
              this.projectInfo = res.data[0];
              this.$store.commit("setProjectInfo", res.data[0]);

              let allMembers = this.projectInfo.members;
              let manager = {
                userName: this.projectInfo.managerName,
                userId: this.projectInfo.managerId
              };
              allMembers.unshift(manager);
              for (let i = 0; i < allMembers.length; i++) {
                let exist = false;
                for (let j = 0; j < that.participants.length; j++) {
                  if (allMembers[i].userId === that.participants[j].userId) {
                    exist = true;
                  }
                }
                if (!exist) {
                  that.candidates.push(allMembers[i]);
                }
              }
            } else {
              console.log(res.data);
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }      
      this.inviteModal = true;
    },
    inviteMembers() {
      for (let i = 0; i < this.inviteList.length; i++) {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/join" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.inviteList[i],
          type: "GET",
          async: false,
          success: data => {
            if (data == "Exist") {
              this.$Message.error("Exist!");
            } else if (data == "None") {
              this.$Message.error("None!");
            } else if (data == "Fail") {
              this.$Message.error("Fail!");
            } else {
              // 告诉拉进去的人已经进项目
              // 把通知存库里
              //reply to applicant
              let replyNotice = {};
              // 改apply.content.userId
              replyNotice["recipientId"] = this.inviteList[i]; // 改apply.content.userId
              replyNotice["type"] = "notice";
              replyNotice["content"] = {
                // 改
                title: "Join subProject",
                description:
                  "You have been invited by " +
                  this.subProjectInfo.managerName +
                  " to join in the sub project: " +
                  this.subProjectInfo.title +
                  " , and now you are a member in this sub project."
              };
              this.axios
                .post("/GeoProblemSolving/notice/save", replyNotice)
                .then(result => {
                  if (result.data == "Success") {
                    this.$emit("sendNotice", this.inviteList[i]); // 改apply.content.userId
                  } else {
                    this.$Message.danger("reply fail.");
                  }
                })
                .catch(err => {
                  this.$Message.danger("reply fail.");
                });
              // this.$emit("sendNotice", this.subProjectInfo.managerId);
              //发给子项目的管理者
            }
          }
        });
      }
      this.init();
    },
    quitSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Success") {
            let projectId = sessionStorage.getItem("projectId");
            this.$router.push({
              name: "ProjectDetail",
              params: { id: projectId }
            });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //创建任务
    createTaskModalShow() {
      let taskDefult = {
        taskName: "",
        description: "",
        startTime: "",
        endTime: "",
        state: ""
      };
      this.$set(this, "taskInfo", taskDefult);
      this.createTaskModal = true;
    },
    createTask() {
      //RequestBody，所以是json格式
      let taskForm = {};
      taskForm["taskName"] = this.taskInfo.taskName;
      taskForm["description"] = this.taskInfo.description;
      taskForm["startTime"] = new Date(this.taskInfo.startTime);
      taskForm["endTime"] = new Date(this.taskInfo.endTime);
      taskForm["creatorId"] = this.$store.getters.userId;
      taskForm["subprojectId"] = this.subProjectInfo.subProjectId;
      taskForm["state"] = "todo";
      taskForm["order"] = "";
      this.axios
        .post("/GeoProblemSolving/task/save", taskForm)
        .then(res => {
          if (res.data == "Success") {
            // 任务更新socket
            this.socketMsg.whoid = this.$store.getters.userId;
            this.socketMsg.who = this.$store.getters.userName;
            this.socketMsg.type = "tasks";
            this.socketMsg.content = "created a new task.";
            this.socketMsg.time = new Date().toLocaleString();
            this.sendMessage(this.socketMsg);
            this.inquiryTask();
          }
        })
        .catch(err => {});
    },
    //打开task编辑器
    editOneTask(index, taskList) {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiry?" +
            "key=taskId" +
            "&value=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          if (res.data != "Fail") {
            let taskInfoRes = res.data[0];
            taskInfoRes.startTime = new Date(taskInfoRes.startTime);
            taskInfoRes.endTime = new Date(taskInfoRes.endTime);
            this.$set(this, "taskInfo", taskInfoRes);
            this.editTaskModal = true;
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    //更新某个task
    updateTask() {
      let taskForm = new URLSearchParams();
      taskForm.append("taskId", this.taskInfo.taskId);
      taskForm.append("taskName", this.taskInfo.taskName);
      taskForm.append("description", this.taskInfo.description);
      taskForm.append("startTime", new Date(this.taskInfo.startTime));
      taskForm.append("endTime", new Date(this.taskInfo.endTime));
      taskForm.append("state", this.taskInfo.state);
      this.axios
        .post("/GeoProblemSolving/task/update", taskForm)
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.inquiryTask(); // 任务更新socket
            this.socketMsg.whoid = this.$store.getters.userId;
            this.socketMsg.who = this.$store.getters.userName;
            this.socketMsg.type = "tasks";
            this.socketMsg.content = "edited a new task.";
            this.socketMsg.time = new Date().toLocaleString();
            this.sendMessage(this.socketMsg);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //查询task
    inquiryTask() {
      // /task/inquiry
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryTodo?" +
            "subprojectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskTodo", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryDoing?" +
            "subprojectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDoing", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryDone?" +
            "subprojectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDone", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    setMoveCount() {
      this.MoveCount = 2;
    },
    addMoveTask(taskList, type) {
      this.MoveCount--;
      this.taskOrderUpdate(taskList, type);
    },
    removeMoveTask(taskList, type) {
      this.MoveCount--;
      this.taskOrderUpdate(taskList, type);
    },
    updateMoveTask(taskList, type) {
      this.MoveCount -= 2;
      this.taskOrderUpdate(taskList, type);
    },
    taskOrderUpdate(taskList, type) {
      for (let i = 0; i < taskList.length; i++) {
        let thisTask = taskList[i];
        let taskUpdateObj = new URLSearchParams();
        taskUpdateObj.append("taskId", taskList[i]["taskId"]);
        taskUpdateObj.append("order", i);
        taskUpdateObj.append("state", type);
        this.axios
          .post("/GeoProblemSolving/task/update", taskUpdateObj)
          .then(res => {
            if (res.data != "Fail") {
              if (this.MoveCount == 0) {
                this.endMove();
              }
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    endMove() {
      // 任务更新socket
      this.socketMsg.whoid = this.$store.getters.userId;
      this.socketMsg.who = this.$store.getters.userName;
      this.socketMsg.type = "tasks";
      this.socketMsg.content = "changed the task schedule.";
      this.socketMsg.time = new Date().toLocaleString();
      this.sendMessage(this.socketMsg);
    },
    taskRemove(index, taskList) {
      this.axios
        .get(
          "/GeoProblemSolving/task/delete" +
            "?taskId=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          if (res.data == "Success") {
            taskList.splice(index, 1);
            // 任务更新socket
            this.socketMsg.whoid = this.$store.getters.userId;
            this.socketMsg.who = this.$store.getters.userName;
            this.socketMsg.type = "tasks";
            this.socketMsg.content = "removed a task.";
            this.socketMsg.time = new Date().toLocaleString();
            this.sendMessage(this.socketMsg);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    }
  }
};
</script>
