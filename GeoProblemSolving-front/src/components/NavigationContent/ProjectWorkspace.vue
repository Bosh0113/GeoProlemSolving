<style>
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
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  background-color: #2db7f5;
}
.member-desc {
  height: 80px;
  /* width:100%; */
  display: flex;
  border: 1px dotted lightgray;
}
.member-image {
  width: 25%;
  margin: 10px;
  border: 1px dotted black;
}
.memebr-work {
  width: 75%;
  margin: 10px;
  border: 1px dotted black;
}
.member-work {
  padding: 10px;
}
.task {
  height: 40px;
}
.area {
  height: 20px;
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
.member_panel:hover,
.resource:hover {
  box-shadow: 1px 1px 2px 2px rgba(0, 0, 0, 0.2);
  transform: scale(1.05);
  transition: all 1s;
}
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
/*  */
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
      <Col span="18" offset="1">
        <Steps :current="order" size="small" v-show="stepShow">
          <Step
            v-for="(list,index) in moduleList"
            :key="index"
            @click.native="showDetail(index,list.Title,list.ModuleID)"
            :title="list.title"
            :order="index"
          ></Step>
        </Steps>
      </Col>
      <Col span="4" offset="1">
        <Button type="info" @click="addModal = true">Add</Button>
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
              <Option v-for="(item,index) in typeList" :key="item.index" :value="item">{{ item }}</Option>
            </Select>
          </div>
          <div class="addNodeStyle">
            <span style="width:10%">Detail</span>
            <textarea v-model="moduleDescription" style="width:400px" :rows="6"></textarea>
          </div>
        </Modal>
        <Button type="error" @click="delModal = true">Delete</Button>
        <Modal v-model="delModal" title="delete task" @on-ok="delModule()" @on-cancel="cancel()">
          <p>Do you really want to delete this step?</p>
        </Modal>
        <Button type="success" @click="editModalShow()">Edit</Button>
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
              :placeholder="moduleList[currentModuleIndex].title"
            />
          </div>
          <div class="editNodeStyle">
            <span style="width:10%">Type</span>
            <Select v-model="updateModuleType" style="width:400px" placeholder="please select type">
              <Option
                v-for="(item,index) in typeList"
                :key="item.index"
                :value="moduleList[currentModuleIndex].type"
              >{{ item }}</Option>
            </Select>
          </div>
          <div class="editNodeStyle">
            <span style="width:10%">Detail</span>
            <textarea
              v-model="updateModuleDescription"
              style="width:400px"
              :rows="6"
              :placeholder="moduleList[currentModuleIndex].description"
            ></textarea>
          </div>
        </Modal>
      </Col>
    </Row>
    <br>
    <p style="margin:10px 5%;text-indent:25px">{{currentModule.description}}</p>
    <Row>
      <Col span="4" offset="1" v-bind="this.participants">
        <div class="member_panel" :style="{maxHeight:sidebarHeight}">
          <div class="title">Participants</div>
          <div class="member-desc" v-for="member in participants" :key="member.index">
            <div class="member-image">{{member.userName}}</div>
            <div class="memebr-work">
              <div class="area">{{member.organization}}</div>
              <div class="task">{{member.jobTitle}}</div>
            </div>
          </div>
          <div
            class="member-invite"
            style="display:flex;justify-content:center;height:60px;align-items:center"
          >
            <Button
              type="success"
              style="text-align:center;width:100px"
              @click="getModuleMembers()"
            >Invite</Button>
            <Modal
              v-model="inviteModal"
              width="800px"
              title="Invite group member join in the module"
              @on-ok="ok"
              @on-cancel="cancel"
            >
              <div style="display:flex;justify-content:center;align-items:center">

              </div>
            </Modal>
          </div>
        </div>
      </Col>
      <Button type="success" @click="createTaskModalShow()">Create Task</Button>
      <!-- createTaskModal -->
      <Modal
        v-model="createTaskModal"
        title="Create task panel"
        @on-ok="createTask()"
        @on-cancel="cancel()"
        width="800px">
        <div class="taskFormItem">
          <span style="width:30%">taskName</span>
          <Input style="width: 300px" :placeholder="this.taskPlaceHolder.name" v-model="taskInfo.taskName"/>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">description</span>
          <Input style="width: 300px" :placeholder="this.taskPlaceHolder.description" type="textarea" :rows="4" v-model="taskInfo.description"/>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">start Time</span>
          <DatePicker type="datetime" :placeholder="this.taskPlaceHolder.startTime" style="width: 300px" v-model="taskInfo.startTime"></DatePicker>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">end Time</span>
          <DatePicker type="datetime" :placeholder="this.taskPlaceHolder.endTime"style="width: 300px" v-model="taskInfo.endTime"></DatePicker>
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
        width="800px">
        <div class="taskFormItem">
          <span style="width:30%">taskName</span>
          <Input style="width: 300px" :placeholder="this.taskPlaceHolder.name" v-model="taskInfo.taskName"/>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">description</span>
          <Input style="width: 300px" :placeholder="this.taskPlaceHolder.description" type="textarea" :rows="4" v-model="taskInfo.description"/>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">start Time</span>
          <DatePicker type="datetime" :placeholder="this.taskPlaceHolder.startTime" style="width: 300px" v-model="taskInfo.startTime"></DatePicker>
        </div>
        <div class="whiteSpace"></div>
        <div class="taskFormItem">
          <span style="width:30%">end Time</span>
          <DatePicker type="datetime" :placeholder="this.taskPlaceHolder.endTime"style="width: 300px" v-model="taskInfo.endTime"></DatePicker>
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
      <Col span="3" offset="1">
      <template>
        <h3>Todo</h3>
        <draggable class="taskList" element="ul" :options="{group:'task'}" v-model="taskTodo"
        @update="taskOrderUpdate(taskTodo,'todo')"
        @add="taskOrderUpdate(taskTodo,'todo')"
        @remove="taskOrderUpdate(taskTodo,'todo')">
          <li v-for="(item,index) in taskTodo" class="taskItem">
            <strong @click="editOneTask(index,taskTodo)" style="cursor: pointer;">{{item.taskName}}</strong>
            <span style="float:right;margin-right:3px;cursor: pointer;" @click="taskRemove(index,taskTodo)">X</span>
            <p>{{item.description}}</p>
            </li>
        </draggable>
      </template>
      </Col>
      <Col span="3" offset="1">
        <template>
        <h3>Doing</h3>
        <draggable class="taskList" element="ul" :options="{group:'task'}" v-model="taskDoing"
        @update="taskOrderUpdate(taskDoing,'doing')"
        @add="taskOrderUpdate(taskDoing,'doing')"
        @remove="taskOrderUpdate(taskDoing,'doing')">
          <li v-for="(item,index)  in taskDoing" class="taskItem">
            <strong @click="editOneTask(index,taskDoing)" style="cursor: pointer;">{{item.taskName}}</strong>
            <span style="float:right;margin-right:3px;cursor: pointer;" @click="taskRemove(index,taskDoing)">X</span>
            <p>{{item.description}}</p>
            </li>
        </draggable>
      </template>
      </Col>
      <Col span="3" offset="1">
      <template>
        <h3>Done</h3>
        <draggable class="taskList" element="ul" :options="{group:'task'}" v-model="taskDone"
        @update="taskOrderUpdate(taskDone,'done')"
        @add="taskOrderUpdate(taskDone,'done')"
        @remove="taskOrderUpdate(taskDone,'done')">
          <li v-for="(item,index)  in taskDone" class="taskItem">
            <strong @click="editOneTask(index,taskDone)" style="cursor: pointer;">{{item.taskName}}</strong>
            <span style="float:right;margin-right:3px;cursor: pointer;" @click="taskRemove(index,taskDone)">X</span>
            <p>{{item.description}}</p>
            </li>
        </draggable>
      </template>
      </Col>
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
      options: {
        dropzoneSelector: "ul",
        draggableSelector: "li",
        handlerSelector: null,
        multipleDropzonesItemsDraggingEnabled: true,
        showDropzoneAreas: true,
        onDrop: function(event) {},
        onDragstart: function(event) {},
        onDragend: function(event) {}
      },
      // 关于邀请的模态框
      inviteModal: false,
      sidebarHeight: "",
      datalist: ["1", "2", "3"],
      datalist2: [
        {
          name: "Water resource collection",
          participants: [
            { name: "lyc", work: "collect data", area: "gis" },
            { name: "xdw", work: "collect data", area: "gis" },
            { name: "mzy", work: "collect data", area: "gis" },
            { name: "zbc", work: "collect data", area: "gis" }
          ]
        },
        {
          name: "Water data analysement",
          participants: [
            { name: "lyc", work: "collect data", area: "gis" },
            { name: "xdw", work: "collect data", area: "gis" }
          ]
        },
        {
          name: "water models building",
          participants: [
            { name: "xdw", work: "collect data", area: "gis" },
            { name: "zbc", work: "collect data", area: "gis" }
          ]
        },
        {
          name: "water models simulation",
          participants: [
            { name: "lyc", work: "collect data", area: "gis" },
            { name: "zbc", work: "collect data", area: "gis" }
          ]
        }
      ],
      participants: [],
      memberlist: [],
      order: 0,
      current: 0,
      addModal: false,
      delModal: false,
      //编辑的模态框
      editModal: false,
      order: 0,
      taborder: 0,
      // chart适用
      chart: new FlowChart(),
      //现在点击的module
      currentModule: {},
      //typeList是选择模块种类的列表，select从这里渲染
      typeList: [
        "Preparatory",
        "Analysis",
        "Modeling",
        "Simulation",
        "Comparison",
        "Verification"
      ],
      //
      moduleTitle: "",
      updateModuleTitle: "",
      //type是指选中后的列表
      moduleType: "",
      updateModuleType: "",
      //moduleDescription指的是节点的详情信息
      moduleDescription: "",
      updateModuleDescription: "",
      //抽屉的控制开关
      drawerOpen: false,
      //后台获取的module下的task列表
      taskList: [],
      //后台获取的单个task的内容
      singleTask: [],
      //后台拿到的Module集合，渲染成一条轴用的
      moduleList: [
        {
          title: "",
          description: "",
          type: ""
        }
      ],
      //Step是否展示
      stepShow: false,
      //当前模块的索引
      currentModuleIndex: 0,
      //创建任务的模态框
      createTaskModal: false,
      //编辑任务的模态框
      editTaskModal:false,
      //task的placeHolder默认值
      taskPlaceHolder: {
        description: "please input the task description.",
        name: "please input task's name",
        startTime: "choose the task's start time",
        endTime: "set the time of the task's end time"
      },
      //task相关
      taskInfo:{
        taskName:"",
        description:"",
        startTime:"",
        endTime:"",
        state:""
      },
      taskTodo: [
        { taskName: "task1", description: "description" },
        { taskName: "task2", description: "description" },
        { taskName: "task3", description: "description" },
        { taskName: "task4", description: "description" },
        { taskName: "task5", description: "description" }
      ],
      taskDoing: [
        { taskName: "task6", description: "description" },
        { taskName: "task7", description: "description" },
        { taskName: "task8", description: "description" },
        { taskName: "task9", description: "description" },
        { taskName: "task10", description: "description" }
      ],
      taskDone: [
        { taskName: "task11", description: "description" },
        { taskName: "task12", description: "description" },
        { taskName: "task13", description: "description" },
        { taskName: "task14", description: "description" },
        { taskName: "task15", description: "description" }
      ]
    };
  },
  created() {
    this.init();
    this.stepShow = false;
    this.getAllModules();
    // this.inquiryTask();
  },
  updated(){
    console.log(this.taskInfo.startTime);
  },
  methods: {
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 60 + "px";
      //通知栏的属性设置，top表示距离顶部的距离，duration表示持续的时间
      this.$Notice.config({
        top: 50,
        duration: 2
      });
      this.axios
        .get(
          "http://localhost:8081/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            localStorage.getItem("subProjectId")
        )
        .then(res => {
          if (res.data != "None") {
            let membersList = res.data[0]["members"];
            let manager = { userId: res.data[0]["managerId"] };
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
            this.$set(this, "participants", participantsTemp);
          }
        })
        .catch(err => {});
    },
    showDetail(item, title, id) {
      this.currentModuleIndex = item;
      this.currentModule = this.moduleList[item];
      // console.log(this.currentModule);
      this.inquiryTask();
      this.order = item;
      let change = String(item);
      this.taborder = change;
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
          // console.log(res.data);
          if (res.data != "None") {
            this.moduleList = res.data;
            this.currentModule = this.moduleList[0];
            if (this.currentModule != "") {
              this.inquiryTask();
            }
            this.stepShow = true;
          } else if (res.data == "None") {
            this.stepShow = false;
            // this.$Message.info("There are no moduldes in this sub project,click create button to cerate one")
          }
        })
        .catch(err => {});
    },
    addModule() {
      // 更换格式写成json字符串的形式提交到后台
      let Module = {};
      Module["projectId"] = localStorage.getItem("projectId");
      Module["subProjectId"] = localStorage.getItem("subProjectId");
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
      let delObject = new URLSearchParams();
      delObject.append(
        "moduleId",
        this.moduleList[this.currentModuleIndex].moduleId
      );
      this.axios
        .post("http://localhost:8081/module/delete", delObject)
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
    //获取项目下的所有成员
    getModuleMembers() {
      this.inviteModal = true;
      projectId = localStorage.getItem("projectId");
    },
    //创建任务
    createTaskModalShow() {
      //定义一个控制创建任务模态框开启的函数
      let taskDefult={
        taskName:"",
        description:"",
        startTime:"",
        endTime:"",
        state:""
      }
      this.taskInfo=taskDefult;
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
        .catch(err => {
        });
    },
    //打开task编辑器
    editOneTask(index,taskList){
      this.axios
        .get(
          "http://localhost:8081/task/inquiry?" +
            "key=taskId" +"&value="+
            taskList[index]["taskId"]
        )
        .then(res => {
          let result=res.data;
          this.$set(this, "taskInfo", result[0]);
          // console.log(this.taskInfo);
          this.editTaskModal = true;
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //更新某个task
    updateTask(){
      let taskForm =  new URLSearchParams();
      taskForm.append("taskId",this.taskInfo.taskId);
      taskForm.append("taskName",this.taskInfo.taskName);
      taskForm.append("description", this.taskInfo.description);
      taskForm.append("startTime", this.taskInfo.startTime);
      taskForm.append("endTime", this.taskInfo.endTime);
      taskForm.append("state",this.taskInfo.state);
      this.axios
          .post("http://localhost:8081/task/update", taskForm)
          .then(res => {
            if(res.data!="None"&&res.data!="Fail"){
              this.inquiryTask();
            }
            else{
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
          // console.log("---load todo list---");
          // console.log(res.data);
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
          // console.log("---load doing list---");
          // console.log(res.data);
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
          // console.log("---load done list---");
          // console.log(res.data);
          this.$set(this, "taskDone", res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    taskOrderUpdate(taskList, type) {
      // console.log("------" + type + " task updated------");
      // console.log(taskList);
      for (let i = 0; i < taskList.length; i++) {
        let thisTask = taskList[i];
        let taskUpdateObj = new URLSearchParams();
        taskUpdateObj.append("taskId", taskList[i]["taskId"]);
        taskUpdateObj.append("order", i);
        taskUpdateObj.append("state", type);
        this.axios
          .post("http://localhost:8081/task/update", taskUpdateObj)
          .then(res => {
            // console.log("---force---");
            // console.log(thisTask);
            // console.log("---updated---");
            // console.log(res.data);
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    taskRemove(index, taskList) {
      this.axios
        .get("http://localhost:8081/task/delete"+"?taskId="+taskList[index]["taskId"])
        .then(res => {
          if(res.data=="Success"){
            taskList.splice(index, 1);
          }
          else{
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    }
  }
};
</script>

