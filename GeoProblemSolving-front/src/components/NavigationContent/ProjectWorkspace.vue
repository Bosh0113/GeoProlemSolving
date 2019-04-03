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
  right: 2%;
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
.member-panel {
  border: 1px solid lightgray;
  transition: all 1s;
}
.subProjectDesc{
  text-indent:2em;
  padding:10px;
  word-break:break-all;
}
/* .member-panel:hover,
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
  /* justify-content: center; */
  align-items: center;
}
/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  padding: 10px;
}
.singl_tool_style span {
  display: flex;
  text-align: center;
  /* justify-content: center; */
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
          <Button type="default" @click="addModal = true" icon="md-add" class="addBtn" title="Add a new module">Add</Button>
          <template v-if="moduleList.length <= 0 || currentModuleIndex == -1 || order == 0">
            <Button type="default" @click="conveneWork()" icon="md-mail" title="Start to work">Convene</Button>
            <Button type="default" @click="backProject()" icon="md-arrow-back" title="Back to project page">Back</Button>
          </template>
          <template v-else>
            <Button type="default" @click="delModal = true" icon="md-remove" class="removeBtn" title="Remove this module">Remove</Button>
            <Button type="default" @click="editModalShow()" icon="md-brush" class="editBtn" title="Edit this module">Edit</Button>
          </template>
          </div>
          <Row>
            <Col span="22" offset="1" style="margin-top:10px;background-color:white;">
              <Steps :current="order">
                <Step title="start" icon="ios-home" @click.native="showDetail(0)" :order="0"></Step>
                <!-- <Step title="tasks" icon="md-list"  @click.native="" :order="1"></Step> -->
                <Step
                  v-for="(list,index) in moduleList"
                  :key="index+1"
                  @click.native="showDetail(index+1)"
                  :title="list.title"
                  :order="index+1"
                ></Step>
              </Steps>
            </Col>
          </Row>
        </Card>
      </Col>
    </Row>
    <div
      v-if="moduleList.length <= 0 || currentModuleIndex == -1 || order == 0"
      class="workspaceContent"
    >
      <Row style="margin-top:20px">
        <Col :xs="8" :sm="7" :md="7" :lg="5" v-bind="this.participants" offset="1">
          <div
            class="member-panel"
            :style="{height:sidebarHeight+'px'}"
            style="background-color:white"
          >
            <div class="title">Participants</div>
            <div :style="{height:sidebarHeight-100+'px'}">
              <div class="member-desc" v-for="(member,index) in this.participants" :key="member.index">
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
                :disabled="inviteAble"
                v-if="this.subProjectInfo.isManager"
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
        <Col :xs="14" :sm="15" :md="16" :lg="16" offset="1">
          <div style="background-color:white;padding:20px">
            <h2 style="margin-bottom:5px">Description</h2>
            <hr style="margin-bottom:10px">
            <div :style="{height:sidebarHeight-140+'px'}" class="subProjectDesc">{{subProjectInfo.description}}</div>
          </div>
          <div style="display:flex;align-items:center;justify-content:center;height:60px">
            <Button type="error" style="margin:auto" v-show="subProjectInfo.managerId == this.$store.getters.userId">Delete this sub-project ?</Button>
          </div>
        </Col>
      </Row>
    </div>
    <div v-else class="workspaceContent">
      <Row style="margin-top:20px">
        <Col :xs="8" :sm="7" :md="6" :lg="5" v-bind="this.olParticipants" offset="1">
          <div
            class="member-panel"
            :style="{height:sidebarHeight+'px'}"
            style="background-color:white"
          >
            <div class="title">Online participants</div>
            <div :style="{height:sidebarHeight-100+'px'}">
              <div class="member-desc" v-for="member in olParticipants" :key="member.id">
                <template style="margin-top:5px">
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
          <Col
            :xs="14"
            :sm="15"
            :md="16"
            :lg="16"
            :style="{height:sidebarHeight/5*3+'px'}"
            style="margin-bottom:20px;margin-left:50px;"
          >
            <div style="width:45%;height:100%;float:left;background-color:white">
              <h2 style="width:100%;padding:10px 10px 0 10px;display: inline-block;cursor: pointer;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;max-width: 80%;">{{currentModule.title}}</h2>
              <hr>
              <div style="width:100%;padding:10px">
                <span style="word-break: break-all;text-indent:2em;padding:10px">
                  {{currentModule.description}}
                </span>
              </div>
            </div>
            <div
              style="width:50%;height:100%;float:right;border:1px solid lightgray;background-color:white;overflow-y:scroll"
            >
              <Timeline style="padding:10px">
                <TimelineItem v-for="(item,index) in allRecords[currentModuleIndex]" :key="index">
                  <template v-if="item.type == 'participants'">
                    <span class="time" style="color:blue">{{item.time}}</span>
                    <span class="time" style="color:blue; margin-left:10px">{{item.who}}</span>
                    <span
                      class="content"
                      style="color:blue; margin-left:10px; word-break:break-word"
                    >{{item.content}}</span>
                  </template>
                  <template v-if="item.type == 'resources'">
                    <span class="time">{{item.time}}</span>
                    <span class="time" style="margin-left:10px">{{item.who}}</span>
                    <span
                      class="content"
                      style="margin-left:10px; word-break:break-word"
                    >{{item.content}}</span>
                  </template>
                  <template v-if="item.type == 'tasks'">
                    <span class="time" style="color:gray">{{item.time}}</span>
                    <span class="time" style="color:gray; margin-left:10px">{{item.who}}</span>
                    <span
                      class="content"
                      style="color:gray; margin-left:10px; word-break:break-word"
                    >{{item.content}}</span>
                  </template>
                </TimelineItem>
              </Timeline>
            </div>
          </Col>
        </template>
        <template>
          <Col :xs="14" :sm="15" :md="16" :lg="17">
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
                        v-show="this.subProjectInfo.isManager||this.subProjectInfo.isMember"
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
                              @click="editOneTask(index,taskTodo)"
                              class="taskName"
                            >{{item.taskName}}</strong>
                            <span style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                              @click="taskRemove(index,taskTodo)">
                              <Icon type="ios-trash" />
                            </span>
                          </div>
                          <p style="word-break:break-word;padding:5px">{{item.description}}</p>
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
                              @click="editOneTask(index,taskDoing)"
                              class="taskName"
                            >{{item.taskName}}</strong>
                            <span style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                              @click="taskRemove(index,taskDoing)">
                              <Icon type="ios-trash" />
                            </span>
<!--
                            <span
                              style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                              @click="taskRemove(index,taskDoing)"
                            >×</span> -->
                          </div>
                          <p style="word-break:break-word;padding:5px">{{item.description}}</p>
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
                              @click="editOneTask(index,taskDone)"
                              class="taskName"
                            >{{item.taskName}}</strong>
                            <span style="float:right;margin-right:3px;cursor: pointer;color:gray;"
                              @click="taskRemove(index,taskDone)">
                              <Icon type="ios-trash" />
                            </span>
                          </div>
                          <p style="word-break:break-word;padding:5px">{{item.description}}</p>
                        </Card>
                      </draggable>
                    </Card>
                  </Col>
                </Row>
              </Col>
              <Col span="1"></Col>
            </Row>
          </Col>
        </template>
        <Col span="1" class="util-panel">
          <div class="util-btn-group">
            <Button type="info" class="util-btn" shape="circle" @click="toolPanel('chat')">
              <Icon type="md-contacts" size="20" class="util-btn-icon" />
            </Button>
            <Button type="info" class="util-btn" shape="circle" @click="drawerOpen = true">
              <Icon type="ios-albums" size="20" class="util-btn-icon"/>
            </Button>
            <!-- <Button type="info" class="util-btn" shape="circle" @click="resourcedrawerOpen = true">
              <Icon type="md-cloud-upload" size="20" class="util-btn-icon"/>
            </Button>-->
            <Drawer :closable="false" v-model="drawerOpen" width="640" style="font-size:30px">
              <h1>General Tools</h1>
              <div class="tool-panel">
                <div class="singl_tool_style">
                  <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools" color="orange"/>
                  <br>
                  <span style="display:flex;justify-content:center">Analyze</span>
                </div>
                <!-- <Icon type="md-brush" /> -->
                <div class="singl_tool_style">
                  <Icon type="md-brush" size="60" @click.native="toolPanel('draw')" title="DrawBoard" color="green"/>
                  <br>
                  <span style="display:flex;justify-content:center">Draw</span>
                </div>
                <!-- <Icon type="md-map" /> -->
                <div class="singl_tool_style">
                  <Icon type="md-map" size="60" @click.native="toolPanel('map')" title="Map" color="lightblue"/>
                  <br>
                  <span style="display:flex;justify-content:center">Map</span>
                </div>
                <!-- <Icon type="ios-book-outline" /> -->
                <div class="singl_tool_style">
                  <Icon type="md-grid" size="60" @click.native="toolPanel('chart')" title="Chart" color="darkgreen"/>
                  <br>
                  <span style="display:flex;justify-content:center">Chart</span>
                </div>
                <div class="singl_tool_style">
                  <Icon type="ios-create" size="60" @click.native="toolPanel('graphEditor')" title="Graph Editor" color="gray"/>
                  <br>
                  <span style="display:flex;justify-content:center">Graph Editor</span>
                </div>
                <!-- <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools"/>
                <Icon type="md-analytics" size="60" @click.native="show" title="Modeling Tools"/> -->
              </div>
              <hr>
              <h1>Special Tools</h1>
            </Drawer>
          </div>
        </Col>
      </Row>
    </div>
    <Modal v-model="delModal" title="delete module" @on-ok="delModule()" @on-cancel="cancel()">
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal v-model="editModal" title="update task" @on-ok="updateModule()" @on-cancel="cancel()">
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
    <Modal
      width="600px"
      v-model="addModal"
      title="add new task node"
      @on-ok="addModule()"
      @on-cancel="cancel()"
    >
      <div class="addNodeStyle">
        <span style="width:10%">Name</span>
        <Input
          v-model="moduleTitle"
          placeholder="Enter something..."
          style="width: 400px"
        />
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
  </div>
</template>
<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import taskParticipateModule from "./.././sharedModule/taskParticipateModule";
import resourceModule from "./.././sharedModule/resourceModule";
import * as socketApi from "./../../api/socket";
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
    memberPart: taskParticipateModule,
    resource: resourceModule,
    draggable,
    Avatar
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
      inviteAble: true,
      // current: 0,
      addModal: false,
      delModal: false,
      //编辑的模态框
      editModal: false,
      order: 0,
      // chart适用
      chart: new FlowChart(),
      //现在点击的module
      foreModuleId: "",
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
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.getAllModules();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (!(vm.subProjectInfo.isManager || vm.subProjectInfo.isMember)) {
          alert("No access");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        }
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    // this.removeTimer();
    this.closeModuleSocket();
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
    this.closeModuleSocket();
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 232;
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
              sessionStorage.setItem(
                "subProjectId",
                subProjectInfo.subProjectId
              );
              sessionStorage.setItem("subProjectName", subProjectInfo.title);
              this.managerIdentity(subProjectInfo.managerId);
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
      if (membersList[0].userId != this.subProjectInfo.managerId) {
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
    getProjectInfo() {
      let projectInfo = this.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.subProjectInfo.projectId
      ) {
        this.projectInfo = projectInfo;
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
            } else {
              console.log(res.data);
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    showDetail(item) {
      if (item <= 0) {
        this.closeModuleSocket();
        this.order = item;
        this.currentModuleIndex = item - 1;
      } else {
        this.currentModuleIndex = item - 1;
        this.currentModule = this.moduleList[this.currentModuleIndex];
        sessionStorage.setItem("moduleId", this.currentModule.moduleId);
        sessionStorage.setItem("moduleName", this.currentModule.title);

        if (this.currentModule.moduleId != this.foreModuleId) {
          this.closeModuleSocket();
          this.foreModuleId = this.currentModule.moduleId;
          this.inquiryTask();
          this.order = item;
          this.openModuleSocket(this.currentModule.moduleId);
          this.olParticipants = [];
        }
      }
    },
    closeModuleSocket() {
      if (this.moduleSocket != null) {
        this.moduleSocket.close();
      }
    },
    openModuleSocket(moduleId) {
      if (this.moduleSocket != null) {
        this.moduleSocket = null;
      }
      // var moduleSocketURL = "ws://localhost:8081/GeoProblemSolving/Module/" + moduleId;
      // var moduleSocketURL = "ws://202.195.237.252:8082/GeoProblemSolving/Module/" + moduleId;
      var moduleSocketURL =
        "ws://172.21.212.7:8082/GeoProblemSolving/Module/" + moduleId;
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
      let record = {
        type: "",
        time: "",
        who: "",
        content: ""
      };
      // if (messageJson.type == "online") {
      //   this.record.time = messageJson.createTime;
      //   this.record.content = "enter this module.";
      // } else if (messageJson.type == "offline") {
      //   this.record.time = messageJson.createTime;
      //   this.record.content = "leave this module.";
      // }
      if (messageJson.type == "message") {
        let message = messageJson.message;

        // 任务记录
        if (
          message.type == "tasks" &&
          message.whoid != this.$store.getters.userId
        ) {
          this.inquiryTask();
        }
        // 资源记录
        if (
          message.type == "resources" &&
          message.whoid != this.$store.getters.userId
        ) {
        }
        // 工具记录
        if (
          message.type == "tools" &&
          message.whoid != this.$store.getters.userId
        ) {
        }

        // 更新records --by mzy
        this.allRecords[this.currentModuleIndex].push(message);
      } else if (messageJson.type == "members") {
        // 比较 判断人员动态 更新records --by mzy

        let members = messageJson.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");

        this.olParticipantChange(members, this.ofParticipant);
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
        if (that.moduleSocket != null && that.moduleSocket != undefined) {
          that.moduleSocket.send(JSON.stringify(messageJson));
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
      this.moduleSocket.send(JSON.stringify(messageJson));
    },
    olParticipantChange(members, callback) {
      let userIndex = -1;
      var record = {
        type: "participants",
        time: new Date().toLocaleString(),
        who: "",
        content: ""
      };

      // 自己刚上线，olParticipants空
      if (this.olParticipants.length == 0) {
        var that = this;
        for (let i = 0; i < members.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                members[i]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.olParticipants.push(res.data);
                record.content =
                  "welcome to here, " + that.$store.getters.userName;
              } else if (res.data == "None") {
              }
            });
        }
      } else {
        // members大于olParticipants，有人上线；小于olParticipants，离线
        if (members.length > this.olParticipants.length) {
          for (var i = 0; i < members.length; i++) {
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

          // 人员渲染 --by mzy
          var that = this;
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                members[userIndex]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.olParticipants.push(res.data);
                if (userIndex != -1) {
                  record.who = res.data.userName;
                  record.content = "enter this module.";
                }
              } else if (res.data == "None") {
              }
            });
        } else if (members.length < this.olParticipants.length) {
          for (var i = 0; i < this.olParticipants.length; i++) {
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
          record.who = this.olParticipants[userIndex].userName;
          record.content = "leave this module.";
          this.olParticipants.splice(userIndex, 1);
        }
      }
      //records 更新
      this.allRecords[this.currentModuleIndex].push(record);
      // callback(members);
    },
    ofParticipant(olPersons) {
      // this.ofParticipants = [];
      // for (let i = 0; i < this.participants.length; i++) {
      //   for (var j = 0; j < olPersons.length; j++) {
      //     this.participants[i].userId == olPersons[j];
      //     break;
      //   }
      //   if (j == olPersons.length) {
      //     this.ofParticipants.push(this.participants[i]);
      //   }
      // }
    },
    getAllModules() {
      //这里重写以下获取module
      let subProjectId = this.$route.params.id;
      this.axios
        .get(
          "/GeoProblemSolving/module/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId
        )
        .then(res => {
          if (res.data != "None") {
            this.moduleList = res.data;
            // this.currentModule = this.moduleList[0];
            // if (this.currentModule != "") {
            //   this.inquiryTask();
            // }
            // init allRecords
            for (let i = 0; i < this.moduleList.length; i++) {
              let records = [];
              this.allRecords.push(records);
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
      Module["creator"] = this.$store.getters.userId;
      Module["type"] = this.moduleType;
      this.axios
        .post("/GeoProblemSolving/module/create", Module)
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
          "/GeoProblemSolving/module/delete" +
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
      let order = this.currentModuleIndex;
      this.updateModuleTitle = this.moduleList[order].title;
      this.updateModuleType = this.moduleList[order].type;
      this.updateModuleDescription = this.moduleList[order].description;
    },
    // 返回项目页
    backProject() {
      this.getProjectInfo();
      let id = this.projectInfo.projectId;
      this.$router.push(`../${id}`);
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
      updateObject.append("creater", this.$store.getters.userId);
      this.axios
        .post("/GeoProblemSolving/module/update", updateObject)
        .then(res => {
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
      // let mId = sessionStorage.getItem("moduleId");
      // sessionStorage.setItem("username","123");
      // window.open("http://localhost:8081/GeoProblemSolving/Collaborative/ConceptualModel/index.html?groupID=" +
      //   mId +
      //   "-ConceptualModel") ;
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
      this.getProjectInfo();
      let that = this;
      this.candidates = [];
      this.inviteList = [];
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
            replyNotice["recipientId"] = this.inviteList[i];// 改apply.content.userId
            replyNotice["type"] = "notice";
            replyNotice["content"] = {// 改
              title: "Join subProject",
              description:
                "You have been invited by " + this.subProjectInfo.managerName +  " to join in the sub project: " +
                this.subProjectInfo.title + " , and now you are a member in this sub project."
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", this.inviteList[i]);// 改apply.content.userId
                } else {
                  this.$Message.danger("reply fail.");
                }
              // this.$emit("sendNotice", this.subProjectInfo.managerId);
              //发给子项目的管理者
            });
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
    createTaskModalShow(){
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
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    // jspanel工具
    toolPanel(type) {
      var toolURL = "";
      if (type == "map") {
        toolURL =
          '<iframe src="/GeoProblemSolving/map" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chatroom") {
        toolURL =
          '<iframe src="/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
      } else if (type == "draw") {
        toolURL =
          '<iframe src="/GeoProblemSolving/draw" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chart") {
        toolURL =
          '<iframe src="/GeoProblemSolving/charts" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chat") {
        toolURL =
          '<iframe src="/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
      } else if (type == "graphEditor") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/GraphEditor/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }
      var panel = jsPanel.create({
        theme: "primary",
        headerTitle: "Tools",
        contentSize: "1000 600",
        content: toolURL,
        disableOnMaximized: true,
      });
      panel.resizeit("disable");
      $(".jsPanel-content").css("font-size", "0");
    },
    //task独立页面
    goToTask(){
      this.$router.push({name:"taskList"}) ;
        disableOnMaximized: true
      }
    }
};
</script>
