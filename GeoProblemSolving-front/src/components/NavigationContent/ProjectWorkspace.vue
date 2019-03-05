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

.util-panel {
  height: 200px;
  box-shadow: 5px 5px 3px 2px rgba(0, 0, 0, 0.3);
  position: fixed;
  right: 5%;
  bottom: 5%;
}
.util-panel:hover {
  transition: all 1s;
}
.util-btn {
  /* 使按钮垂直居中即可 */
  display: block;
  margin: 40px auto;
  width: 40px;
  height: 40px;
  text-align: center;
  transition: all 1s;
  /* 使按钮中的元素垂直居中 */
  display: flex;
  justify-content: center;
  align-content: center;
}
.util-btn:hover {
  transform: scale(1.25);
  transition: all 1s;
}
.member_panel {
  border: 1px solid lightgray;
  transition: all 1s;
}
/* .member_panel:hover,
.resource:hover {
  box-shadow: 1px 1px 2px 2px rgba(0, 0, 0, 0.2);
  transform: scale(1.05);
  transition: all 1s;
} */
.resource {
  transition: all 1s;
}
.tool-panel {
  display: flex;
  height: auto;
  justify-content: center;
  align-items: center;
}
/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  padding: 10px;
}
.singl_tool_style span {
  display: flex;
  text-align: center;
  justify-content: center;
  align-items: center;
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
  min-height: 100px;
  background: azure;
}
.taskItem {
  list-style: none;
  border: solid 0.5px green;
  padding: 1px;
  margin: 1px;
}
</style>
<template>
  <div>
    <Row>
      <Col
        span="18"
        offset="1"
        style="margin-top:10px;background-color:white;padding-top:20px;padding-bottom:20px"
      >
        <Steps :current="order">
          <Step title="start" icon="ios-home" @click.native="showDetail(0)" :order="0"></Step>
          <Step
            v-for="(list,index) in moduleList"
            :key="index+1"
            @click.native="showDetail(index+1)"
            :title="list.title"
            :order="index+1"
          ></Step>
        </Steps>
      </Col>
      <Col span="4" offset="1" v-show="isSubProjectManager" style="margin-top:10px">
        <Button type="default" @click="addModal = true" icon="md-add" class="addBtn">Add</Button>
        <Modal
          width="600px"
          v-model="addModal"
          title="add new task node"
          @on-ok="addModule()"
          @on-cancel="cancel()"
        >
          <div class="addNodeStyle">
            <span style="width:10%">Name</span>
            <Input v-model="moduleTitle" placeholder="Enter something..." style="width: 400px"/>
          </div>
          <div class="addNodeStyle">
            <span style="width:10%">Type</span>
            <Select v-model="moduleType" style="width:400px" placeholder="please select type">
              <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
            </Select>
          </div>
          <div class="addNodeStyle">
            <span style="width:10%">Detail</span>
            <textarea v-model="moduleDescription" style="width:400px" :rows="6"></textarea>
          </div>
        </Modal>
        <Button type="default" @click="delModal = true" icon="md-remove" class="removeBtn">Remove</Button>
        <Modal v-model="delModal" title="delete task" @on-ok="delModule()" @on-cancel="cancel()">
          <p>Do you really want to delete this step?</p>
        </Modal>
        <Button type="default" @click="editModalShow()" icon="md-brush" class="editBtn">Edit</Button>
        <Modal
          v-model="editModal"
          title="update task"
          @on-ok="updateModule()"
          @on-cancel="cancel()"
        >
          <div class="editNodeStyle">
            <span style="width:10%">Name</span>
            <Input
              v-model="updateModuleTitle"
              placeholder="Enter something..."
              style="width: 400px"
              :placeholder="moduleTitle"
            />
          </div>
          <div class="editNodeStyle">
            <span style="width:10%">Type</span>
            <Select v-model="updateModuleType" style="width:400px" placeholder="please select type">
              <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
            </Select>
          </div>
          <div class="editNodeStyle">
            <span style="width:10%">Detail</span>
            <textarea
              v-model="updateModuleDescription"
              style="width:400px"
              :rows="6"
              :placeholder="moduleDescription"
            ></textarea>
          </div>
        </Modal>
      </Col>
    </Row>
    <div
      v-if="moduleList.length <= 0 || currentModuleIndex == -1 || order == 0"
      class="workspaceContent"
    >
      <!-- <h1>No module have been created!</h1> -->
      <h1 style="margin-top: 0px;margin-bottom: 0px;text-align:left">{{subProjectInfo.title}}</h1>
      <hr>
      <Row style="margin-top:20px">
        <Col :xs="8" :sm="7" :md="6" :lg="5" v-bind="this.participants">
          <div class="member_panel" :style="{height:sidebarHeight+'px'}" style="background-color:white">
            <div class="title">Participants</div>
            <div :style="{height:sidebarHeight-100+'px'}">
              <div class="member-desc" v-for="(member,index) in participants" :key="member.index">
                <template v-if="index==0">
                  <div class="member-image">
                    <img
                      v-if="member.avatar != ''"
                      :src="member.avatar"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else-if="member.gender == 'female'"
                      src="@/assets/images/female.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else
                      src="@/assets/images/male.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                  </div>
                  <div class="memebr-work">
                    <div class="userName">
                      <!-- <Tag>name</Tag> -->
                      <span style="padding:0 5px;float:right">{{member.userName}}</span>
                    </div>
                    <div class="organization">
                      <!-- <Tag>organization</Tag> -->
                      <span style="padding:0 5px">{{member.organization}}</span>
                    </div>
                  </div>
                </template>
                <template v-else style="margin-top:5px">
                  <div class="member-image">
                    <img
                      v-if="member.avatar != ''"
                      :src="member.avatar"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else-if="member.gender == 'female'"
                      src="@/assets/images/female.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else
                      src="@/assets/images/male.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                  </div>
                  <div class="memebr-work">
                    <div class="userName">
                      <!-- <Tag>name</Tag> -->
                      <span style="padding:0 5px;float:right">{{member.userName}}</span>
                    </div>
                    <div class="organization">
                      <!-- <Tag>organization</Tag> -->
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
                v-if="isSubProjectManager"
              >Invite</Button>
              <Button
                type="warning"
                style="text-align:center;width:100px"
                @click="quitModal=true"
                v-else-if="isSubProjectMember"
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
                @on-cancel="cancel"
              >
                <div>
                  <p>Members:</p>
                  <Tag
                    v-for="participant in participants"
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
        <Col :xs="15" :sm="16" :md="17" :lg="18" offset="1">
          <div style>
            <h2 style="margin-bottom:5px">Description</h2>
            <hr style="margin-bottom:10px">
            <div :style="{height:sidebarHeight-80+'px'}">{{subProjectInfo.description}}</div>
          </div>
          <div style="display:flex;align-items:center;justify-content:center;height:60px">
            <Button type="error" style="margin:auto">Quit this sub-project ?</Button>
          </div>
        </Col>
      </Row>
    </div>
    <div v-else class="workspaceContent">
      <h1 style="margin-top: 0px;margin-bottom: 0px;text-align:left">{{subProjectInfo.title}}</h1>
      <hr>
      <Row style="margin-top:20px">
        <Col :xs="8" :sm="7" :md="6" :lg="5" v-bind="this.olParticipants">
          <div class="member_panel" :style="{height:sidebarHeight+'px'}">
            <div class="title">Online participants</div>
            <div :style="{height:sidebarHeight-100+'px'}">
              <div class="member-desc" v-for="member in olParticipants" :key="member.id">                
                <template style="margin-top:5px">
                  <div class="member-image">
                    <img
                      v-if="member.avatar != ''"
                      :src="member.avatar"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else-if="member.gender == 'female'"
                      src="@/assets/images/female.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                    <img
                      v-else
                      src="@/assets/images/male.png"
                      style="width:auto;height:100%"
                      @click="gotoWorkSpace(member.userId)"
                    >
                  </div>
                  <div class="memebr-work">
                    <div class="userName">
                      <!-- <Tag>name</Tag> -->
                      <span style="padding:0 5px;float:right">{{member.userName}}</span>
                    </div>
                    <div class="organization">
                      <!-- <Tag>organization</Tag> -->
                      <span style="padding:0 5px">{{member.organization}}</span>
                    </div>
                  </div>
                </template>
              </div>
            </div>
          </div>
        </Col>
        <template>
          <Col :xs="15" :sm="16" :md="17" :lg="18" offset="1" style="height:300px;margin-bottom:20px">
            <div style="width:45%;height:100%;float:left;background-color:white">
              <h2 style="width:100%;padding:10px 10px 0 10px">{{currentModule.title}}</h2>
              <hr>
              <div style="width:100%;padding:10px">{{currentModule.description}}</div>
            </div>
            <div style="width:50%;height:100%;float:right;border:1px solid lightgray">
              <Timeline>
                <!-- <TimelineItem v-if="records.length > 3"><a href="#">More</a></TimelineItem> -->
                <TimelineItem v-for="(item,index) in records" :key="index">
                  <template v-if="item.type == 'participants'">
                    <span class="time" style="color:blue">{{item.time}}</span>
                    <span class="time" style="color:blue">{{item.who}}</span>
                    <span class="content" style="color:blue">{{item.content}}</span>
                  </template>
                  <template v-if="item.type == 'resources'">
                    <span class="time">{{item.time}}</span>
                    <span class="time">{{item.who}}</span>
                    <span class="content">{{item.content}}</span>
                  </template>
                  <template v-if="item.type == 'tasks'">
                    <span class="time" style="color:gray">{{item.time}}</span>
                    <span class="time" style="color:gray">{{item.who}}</span>
                    <span class="content" style="color:gray">{{item.content}}</span>
                  </template>
                </TimelineItem>
              </Timeline>
            </div>
          </Col>
        </template>
        <template>
          <Col span="3" offset="1">
            <template>
              <h3>Todo</h3>
              <draggable
                class="taskList"
                element="ul"
                :options="{group:'task'}"
                v-model="taskTodo"
                @update="taskOrderUpdate(taskTodo,'todo')"
                @add="taskOrderUpdate(taskTodo,'todo')"
                @remove="taskOrderUpdate(taskTodo,'todo')"
              >
                <li v-for="(item,index) in taskTodo" class="taskItem" :key="index">
                  <strong
                    @click="editOneTask(index,taskTodo)"
                    style="cursor: pointer;"
                  >{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;"
                    @click="taskRemove(index,taskTodo)"
                  >X</span>
                  <p>{{item.description}}</p>
                </li>
              </draggable>
            </template>
          </Col>
          <Col span="3" offset="1">
            <template>
              <h3>Doing</h3>
              <draggable
                class="taskList"
                element="ul"
                :options="{group:'task'}"
                v-model="taskDoing"
                @update="taskOrderUpdate(taskDoing,'doing')"
                @add="taskOrderUpdate(taskDoing,'doing')"
                @remove="taskOrderUpdate(taskDoing,'doing')"
              >
                <li v-for="(item,index)  in taskDoing" class="taskItem" :key="index">
                  <strong
                    @click="editOneTask(index,taskDoing)"
                    style="cursor: pointer;"
                  >{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;"
                    @click="taskRemove(index,taskDoing)"
                  >X</span>
                  <p>{{item.description}}</p>
                </li>
              </draggable>
            </template>
          </Col>
          <Col span="3" offset="1">
            <template>
              <h3>Done</h3>
              <draggable
                class="taskList"
                element="ul"
                :options="{group:'task'}"
                v-model="taskDone"
                @update="taskOrderUpdate(taskDone,'done')"
                @add="taskOrderUpdate(taskDone,'done')"
                @remove="taskOrderUpdate(taskDone,'done')"
              >
                <li v-for="(item,index) in taskDone" class="taskItem" :key="index">
                  <strong
                    @click="editOneTask(index,taskDone)"
                    style="cursor: pointer;"
                  >{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;"
                    @click="taskRemove(index,taskDone)"
                  >X</span>
                  <p>{{item.description}}</p>
                </li>
              </draggable>
            </template>
          </Col>
          <Col span="1" offset="1">
            <Button
              type="default"
              class="createTaskBtn"
              @click="createTaskModalShow()"
              v-show="isSubProjectManager||isSubProjectMember"
            >Create Task</Button>
          </Col>
        </template>
        <Col span="1" class="util-panel">
          <div class="util-btn-group">
            <Button type="info" class="util-btn" shape="circle">
              <Icon type="md-contacts" size="20" class="util-btn-icon"/>
            </Button>
            <Button type="info" class="util-btn" shape="circle" @click="drawerOpen = true">
              <Icon type="ios-albums" size="20" class="util-btn-icon"/>
            </Button>
            <Drawer :closable="false" v-model="drawerOpen" width="640" style="font-size:30px">
              <h1>General Tools</h1>
              <div class="tool-panel">
                <div class="singl_tool_style">
                  <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools"/>
                  <br>
                  <span style="display:flex;justify-content:center">11</span>
                </div>

                <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools"/>
                <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools"/>
              </div>
              <hr>
              <h1>Special Tools</h1>
            </Drawer>
          </div>
        </Col>
      </Row>
    </div>
    <!-- createTaskModal -->
    <Modal
      v-model="createTaskModal"
      title="Create task panel"
      @on-ok="createTask()"
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
import taskParticipateModule from "./.././sharedModule/taskParticipateModule";
import resourceModule from "./.././sharedModule/resourceModule";
import * as socketApi from "./../../api/socket";
import draggable from "vuedraggable";
export default {
  components: {
    VueFlowy,
    memberPart: taskParticipateModule,
    resource: resourceModule,
    draggable
  },
  data() {
    return {
      // info of subproject --by mzy
      subProjectInfo: [],
      //登陆者身份
      isSubProjectManager: false, //为管理者
      isSubProjectMember: false, //为成员
      // 关于邀请的模态框
      inviteModal: false,
      quitModal: false,
      sidebarHeight: "",
      participants: [],
      candidates: [],
      inviteList: [],
      // current: 0,
      addModal: false,
      delModal: false,
      //编辑的模态框
      editModal: false,
      order: 0,
      // chart适用
      chart: new FlowChart(),
      //现在点击的module
      currentModule: {},
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
      // 当前模块的索引
      currentModuleIndex: 0,
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
      // web socket for module
      moduleSocket: null,
      timer: null,
      // 动态记录相关
      record:{
        type: "",
        time: "",
        who: "",
        content: ""
      },
      records: [],
      // 现在参与者
      olParticipants: []
    };
  },
  created() {
    this.init();
    this.getAllModules();
    // this.inquiryTask();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    // alert(this.isSubProjectMember);
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (!(vm.isSubProjectManager || vm.isSubProjectMember)) {
          alert("No access");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        }
      }
    });
  },
  mounted: function() {
    window.addEventListener("resize", this.initSize);
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
    this.moduleSocket.close();
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 250;
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
      $.ajax({
        url:
          "http://localhost:8081/subProject/inquiry" +
          "?key=subProjectId" +
          "&value=" +
          this.$route.params.id,
        type: "GET",
        async: false,
        success: data => {
          if (data != "None") {
            let subProjectInfo = data[0];
            this.$set(this, "subProjectInfo", subProjectInfo);
            this.managerIdentity(subProjectInfo.managerId);
            this.memberIdentity(subProjectInfo["members"]);
            let membersList = subProjectInfo["members"];
            let manager = { userId: subProjectInfo["managerId"] };
            membersList.unshift(manager);
            let participantsTemp = [];
            for (let i = 0; i < membersList.length; i++) {
              $.ajax({
                url:
                  "http://localhost:8081/user/inquiry" +
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
            that.$set(this, "participants", participantsTemp);
          }
        },
        error: function(err) {
          console.log("Get manager name fail.");
        }
      });
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.state.userId) {
        this.isSubProjectManager = true;
      }
    },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.state.userId) {
          this.isSubProjectMember = true;
          break;
        }
      }
    },
    showDetail(item) {
      this.closeModuleSocket();

      if (item <= 0) {
        this.order = item;
        this.currentModuleIndex = item - 1;
      } else {
        this.currentModuleIndex = item - 1;
        this.currentModule = this.moduleList[this.currentModuleIndex];
        // console.log(this.currentModule);
        this.inquiryTask();
        this.order = item;
        this.openModuleSocket(this.currentModule.moduleId);
      }
    },
    closeModuleSocket() {
      if (this.moduleSocket != null) {
        this.moduleSocket.close();
      }
    },
    openModuleSocket(moduleId) {
      var moduleSocketURL = "ws://localhost:8081/Module/" + moduleId;
      this.moduleSocket = new WebSocket(moduleSocketURL);
      this.moduleSocket.onopen = this.onOpen;
      this.moduleSocket.onmessage = this.onMessage;
      this.moduleSocket.onclose = this.onClose;
      this.moduleSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("ModuleSocket连接成功！");
    },
    // 更新人员，更新数据，更新records
    onMessage(e) {
      let messageJson = JSON.parse(e.data);
      let userIndex = "";

      if (messageJson.type == "online") {

        this.record.time = messageJson.createTime;
        this.record.content = "enter this module.";

      } else if (messageJson.type == "offline") {

        this.record.time = messageJson.createTime;
        this.record.content = "leave this module.";

      } else if (messageJson.type == "message") {

        console.log(messageJson.message);
        // 更新数据 --by mzy

        // 获取history记录 --by mzy

        // 更新records --by mzy

      } else if (messageJson.type == "members") {
        // 比较 判断人员动态 更新records --by mzy

        let members = messageJson.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");          
          
        if (members.length > this.olParticipants.length) {
          for (let i = 0; i < members.length; i++) {
            for (var j = 0; j < this.olParticipants.length; j++) {
              if (members[i] == this.olParticipants[j].userId) {
                break;
              }
            }
            if (j == this.olParticipants.length) {
              userIndex = i;
              break;
            }
          }
        } else if (members.length < this.olParticipants.length) {
          for (let i = 0; i < this.olParticipants.length; i++) {
            for (var j = 0; j < members.length; j++) {
              if (this.olParticipants[i].userId == members[j]) {
                break;
              }
            }
            if (j == members.length) {
              userIndex = i;
              break;
            }
          }
        }
        // 人员渲染 --by mzy
        let participantsTemp = [];
        for (let i = 0; i < members.length; i++) {
          $.ajax({
            url:
              "http://localhost:8081/user/inquiry" +
              "?key=" +
              "userId" +
              "&value=" +
              members[i],
            type: "GET",
            async: false,
            success: function(data) {
              participantsTemp.push(data);
            }
          });          
        }
        
        //records 更新
        this.record.type = "participants";
        this.record.who =  participantsTemp[userIndex].userName;
        this.records.push(this.record);
        this.$set(this, "olParticipants", participantsTemp);
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
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        this.moduleSocket.send(JSON.stringify(messageJson));
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    sendMessage(message) {
      var messageJson = {};
      messageJson["type"] = "message";
      messageJson["message"] = message;
      this.moduleSocket.send(JSON.stringify(messageJson));
    },
    getAllModules() {
      //这里重写以下获取module
      let subProjectId = this.$route.params.id;
      localStorage.setItem("subProjectId", this.$route.params.id);
      this.axios
        .get(
          "http://localhost:8081/module/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId
        )
        .then(res => {
          if (res.data != "None") {
            this.moduleList = res.data;
            this.currentModule = this.moduleList[0];
            if (this.currentModule != "") {
              this.inquiryTask();
            }
          } else if (res.data == "None") {
            // this.$Message.info("There are no moduldes in this sub project,click create button to create one")
          }
        })
        .catch(err => {});
    },
    addModule() {
      // 更换格式写成json字符串的形式提交到后台
      let Module = {};
      Module["subProjectId"] = this.$route.params.id;
      Module["title"] = this.moduleTitle;
      Module["description"] = this.moduleDescription;
      Module["creator"] = this.$store.state.userId;
      Module["type"] = this.moduleType;
      this.axios
        .post("http://localhost:8081/module/create", Module)
        .then(res => {
          if (res.data === "Fail") {
            this.$Message.info("Fail");
          } else {
            this.createModuleSuccess(Module["title"]);
            this.moduleTitle = "";
            this.moduleDescription = "";
            this.moduleType = "";
            this.getAllModules();
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    delModule() {
      this.axios
        .get(
          "http://localhost:8081/module/delete" +
            "?moduleId=" +
            this.moduleList[this.currentModuleIndex].moduleId
        )
        .then(res => {
          if (res.data === "Success") {
            this.deleteModuleSuccess();
            this.getAllModules();
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    editModalShow() {
      this.editModal = true;
      console.log(this.currentModuleIndex);
      let order = this.currentModuleIndex;
      console.log(this.moduleList[order].title);
      this.updateModuleTitle = this.moduleList[order].title;
      this.updateModuleType = this.moduleList[order].type;
      this.updateModuleDescription = this.moduleList[order].description;
    },
    //更新模块的函数
    updateModule() {
      let updateObject = new URLSearchParams();
      updateObject.append(
        "moduleId",
        this.moduleList[this.currentModuleIndex].moduleId
      );
      updateObject.append("title", this.updateModuleTitle);
      updateObject.append("description", this.updateModuleDescription);
      updateObject.append("type", this.updateModuleType);
      updateObject.append("creater", this.$store.state.userId);
      this.axios
        .post("http://localhost:8081/module/update", updateObject)
        .then(res => {
          console.log(res.data);
          this.getAllModules();
        })
        .catch(err => {
          console.log(err.data);
        });
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
    show() {
      let mId = localStorage.getItem("moduleId");
      window.location.href =
        "http://202.195.237.252:8088/TeamWorking/Collaborative/ConceptualModel/index.html?groupID=" +
        mId +
        "-ConceptualModel";
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
    deleteModuleSuccess() {
      this.$Notice.info({
        title: "Delete Notification",
        desc: "The module has been deleted successfully"
      });
    },
    //加载并打开成员邀请Modal
    inviteMembersModalShow() {
      let that = this;
      this.candidates = [];
      this.inviteList = [];
      this.axios
        .get(
          "http://localhost:8081/project/inquiry" +
            "?key=projectId" +
            "&value=" +
            localStorage.getItem("projectId")
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            let allMembers = res.data[0].members;
            $.ajax({
              url:
                "http://localhost:8081/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                res.data[0].managerId,
              type: "GET",
              async: false,
              success: function(data) {
                let manager = { userName: data.userName, userId: data.userId };
                allMembers.unshift(manager);
              }
            });
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
            this.inviteModal = true;
          } else {
            console.log(res.data);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    inviteMembers() {
      for (let i = 0; i < this.inviteList.length; i++) {
        $.ajax({
          url:
            "http://localhost:8081/subProject/join" +
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
            }
          }
        });
      }
      this.init();
    },
    quitSubProject() {
      this.axios
        .get(
          "http://localhost:8081/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.$store.state.userId
        )
        .then(res => {
          if (res.data == "Success") {
            let projectId = localStorage.getItem("projectId");
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
      //定义一个控制创建任务模态框开启的函数
      let taskDefult = {
        taskName: "",
        description: "",
        startTime: "",
        endTime: "",
        state: ""
      };
      this.taskInfo = taskDefult;
      this.createTaskModal = true;
    },
    createTask() {
      //RequestBody，所以是json格式
      let taskForm = {};
      console.log("当前模块是：" + this.currentModule.moduleId);
      taskForm["taskName"] = this.taskInfo.taskName;
      taskForm["description"] = this.taskInfo.description;
      taskForm["startTime"] = this.taskInfo.startTime;
      taskForm["endTime"] = this.taskInfo.endTime;
      taskForm["creatorId"] = this.$store.state.userId;
      taskForm["moduleId"] = this.currentModule.moduleId;
      taskForm["state"] = this.taskInfo.state;
      taskForm["order"] = "";
      console.log(taskForm);
      this.axios
        .post("http://localhost:8081/task/save", taskForm)
        .then(res => {
          this.inquiryTask();
        })
        .catch(err => {});
    },
    //打开task编辑器
    editOneTask(index, taskList) {
      this.axios
        .get(
          "http://localhost:8081/task/inquiry?" +
            "key=taskId" +
            "&value=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          let result = res.data;
          this.$set(this, "taskInfo", result[0]);
          console.log(this.taskInfo);
          this.editTaskModal = true;
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //更新某个task
    updateTask() {
      let taskForm = new URLSearchParams();
      taskForm.append("taskId", this.taskInfo.taskId);
      taskForm.append("taskName", this.taskInfo.taskName);
      taskForm.append("description", this.taskInfo.description);
      taskForm.append("startTime", this.taskInfo.startTime);
      taskForm.append("endTime", this.taskInfo.endTime);
      taskForm.append("state", this.taskInfo.state);
      this.axios
        .post("http://localhost:8081/task/update", taskForm)
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.inquiryTask();
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
          "http://localhost:8081/task/inquiryTodo?" +
            "moduleId=" +
            this.currentModule.moduleId
        )
        .then(res => {
          this.$set(this, "taskTodo", res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
      this.axios
        .get(
          "http://localhost:8081/task/inquiryDoing?" +
            "moduleId=" +
            this.currentModule.moduleId
        )
        .then(res => {
          this.$set(this, "taskDoing", res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
      this.axios
        .get(
          "http://localhost:8081/task/inquiryDone?" +
            "moduleId=" +
            this.currentModule.moduleId
        )
        .then(res => {
          this.$set(this, "taskDone", res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    taskOrderUpdate(taskList, type) {
      for (let i = 0; i < taskList.length; i++) {
        let thisTask = taskList[i];
        let taskUpdateObj = new URLSearchParams();
        taskUpdateObj.append("taskId", taskList[i]["taskId"]);
        taskUpdateObj.append("order", i);
        taskUpdateObj.append("state", type);
        this.axios
          .post("http://localhost:8081/task/update", taskUpdateObj)
          .then(res => {})
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    taskRemove(index, taskList) {
      this.axios
        .get(
          "http://localhost:8081/task/delete" +
            "?taskId=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          if (res.data == "Success") {
            taskList.splice(index, 1);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    gotoWorkSpace(data) {
      this.$router.push({ name: "PersonalPage" });
    }
  }
};
</script>
