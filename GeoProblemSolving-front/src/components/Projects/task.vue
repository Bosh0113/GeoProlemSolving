<template>
  <Row>
    <Col span="22" style="margin-left:50px;padding: 5px 0;background-color:white">
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
                  <strong @click="editOneTask(index,taskTodo)" class="taskName">{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                    @click="taskRemove(index,taskTodo)"
                  >×</span>
                </div>
                <span style="word-break:break-word;">{{item.description}}</span>
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
                  <strong @click="editOneTask(index,taskDoing)" class="taskName">{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                    @click="taskRemove(index,taskDoing)"
                  >×</span>
                </div>
                <span style="word-break:break-word;">{{item.description}}</span>
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
                  <strong @click="editOneTask(index,taskDone)" class="taskName">{{item.taskName}}</strong>
                  <span
                    style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                    @click="taskRemove(index,taskDone)"
                  >×</span>
                </div>
                <span style="word-break:break-word;">{{item.description}}</span>
              </Card>
            </draggable>
          </Card>
        </Col>
      </Row>
    </Col>
    <Col span="1"></Col>
    <!-- createTaskModal -->
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
  </Row>
</template>
<style scoped>
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
  min-height: 50px;
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
.createTaskBtn:hover {
  color: white;
  background: #47cb89;
}
.editBtn .createTaskBtn {
  font-size: 10px;
}
</style>
<script>
import draggable from "vuedraggable";
export default {
  components: {
    draggable,
  },
  created() {
    this.init();
  },
  data() {
    return {
      // information of project
      projectInfo: {},
      // info of subproject --by mzy
      subProjectInfo: [],
      //登陆者身份
      // 关于邀请的模态框
      inviteModal: false,
      quitModal: false,
      sidebarHeight: 800,
      participants: [],
      candidates: [],
      inviteList: [],
      // current: 0,
      addModal: false,
      delModal: false,
      //编辑的模态框
      editModal: false,
      order: 0,
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
      MoveCount: 0,
      // web socket for module
      moduleSocket: null,
      timer: null,
      // 动态记录相关
      // 所有module的记录
      allRecords: [],
      // 当前参与者
      olParticipants: [],
      ofParticipants: [],
      // 消息
      socketMsg: {
        type: "",
        time: "",
        who: "",
        whoid: "",
        content: ""
      }
    };
  },


  methods: {
    init() {
      // this.initSize();
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.showMembers();
      } else {
      }
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
            sessionStorage.setItem("subProjectId",subProjectInfo.subProjectId);
            sessionStorage.setItem("subProjectName",subProjectInfo.title)

            this.managerIdentity(subProjectInfo.managerId);
            this.memberIdentity(subProjectInfo.members);
            this.$store.commit("setSubProjectInfo", subProjectInfo);
            this.showMembers();
          }
        },
        error: function(err) {
          console.log("Get manager name fail.");
        }
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
      taskForm["moduleId"] = this.currentModule.moduleId;
      taskForm["state"] = "todo";
      taskForm["order"] = "";
      console.log(taskForm);
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
          }else{
            console.log(res.data);
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
            "moduleId=" +
            this.currentModule.moduleId
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
            "moduleId=" +
            this.currentModule.moduleId
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
            "moduleId=" +
            this.currentModule.moduleId
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
    cancel(){
      this.$Message.info("you clicked cancel");
    },
    // 待做，统计各类项目的个数以及完成的个数，完成的则将其划定为已完成模式。在title上画横线
  }
};
</script>
