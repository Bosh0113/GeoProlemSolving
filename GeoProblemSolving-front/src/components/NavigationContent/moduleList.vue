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
  height: 140px;
  width: 60px;
  box-shadow: 2px 2px 2px 2px gray;
  position: fixed;
  cursor: move;
  left: 0px;
  right: 200px;
  z-index: 100;
}
.util-btn-group {
  width: 60px;
  cursor: default;
}
.util-btn {
  /* 使按钮垂直居中即可 */
  display: block;
  margin: 20px auto;
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
  transform: scale(1.15);
  transition: all 0.5s;
}
.member-panel {
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
.tool-panel {
  display: flex;
  height: auto;
  flex-wrap: wrap;
  /* justify-content: center; */
  align-items: center;
}
/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  cursor: pointer;
  display: flex;
  justify-content: center;
}
.singl_tool_style:hover {
  transition: all 1s;
  background-color: lightgray;
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
.noModule h1 {
  color: darkgray;
  text-align: center;
}
</style>
<template>
  <div style="background-color:#dcdee2">
    <Row>
      <Col span="22" offset="1">
        <Card>
          <Row>
            <Col span="7" style="height:20px">
              <Breadcrumb>
                <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem>
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Working panel</BreadcrumbItem>
              </Breadcrumb>
            </Col>
            <Col span="10" style="text-align:center;font-size:1.5rem;height:20px;;margin-top:-10px">
              <strong>{{subProjectInfo.title}}</strong>
            </Col>
            <Col span="6" offset="1" style="height:20px;display:flex;align-items:center" class="operatePanel">
              <template v-if="this.$store.getters.userInfo.userId == this.subProjectInfo.managerId">
                <template v-if="this.moduleList.length == 0">
                  <Button
                    type="default"
                    @click="addModal = true"
                    icon="md-add"
                    class="addBtn"
                    title="Add a new module"
                  >Add</Button>
                </template>
                <template v-else>
                  <Button
                    type="default"
                    @click="chooseResource()"
                    icon="md-add"
                    class="addBtn"
                    title="Add a new module"
                  >Add</Button>
                </template>
                <template v-if="this.currentModule.activeStatus">
                  <Button
                    type="default"
                    @click="editModalShow()"
                    icon="ios-create"
                    class="editBtn"
                    title="Edit this module"
                  >Edit</Button>
                </template>
                <template v-else>
                  <Button
                    type="default"
                    @click="activateModal = true"
                    icon="md-bulb"
                    class="editBtn"
                    title="Activate this module"
                  >Activate</Button>
                  <Button
                    type="default"
                    @click="delModal = true"
                    icon="md-remove"
                    class="removeBtn"
                    title="Remove this module"
                  >Remove</Button>
                </template>
              </template>
            </Col>
          </Row>
          <Row>
            <Col span="22" offset="1" style="background-color:white;">
              <template v-if="this.$store.getters.userInfo.userId == this.subProjectInfo.managerId">
                <Steps :current="order">
                  <Step
                    v-for="(list,index) in moduleList"
                    :key="index"
                    @click.native="showDetail(index)"
                    :title="list.type"
                    :content="list.title"
                    :order="index"
                  ></Step>
                </Steps>
              </template>
              <template v-else>
                <Steps :current="order">
                  <Step
                    v-for="(list,index) in moduleList"
                    :key="index"
                    @click.native="showDetail(index)"
                    :title="list.type"
                    :content="list.title"
                    :order="index"
                  ></Step>
                </Steps>
              </template>
            </Col>
          </Row>
        </Card>
      </Col>
    </Row>
    <div v-if="this.moduleList.length == 0" class="workspaceContent">
      <Row style="margin-top:20px" :style="{height:sidebarHeight+30+'px'}">
        <Col span="22" offset="1">
          <Card class="noModule">
            <div
              @click="addModal = true"
              style="cursor:pointer"
              v-if="this.$store.getters.userInfo.userId == this.subProjectInfo.managerId"
            >
              <h1>Start your work!</h1>
            </div>
            <div v-else>
              <h1>Start your work!</h1>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
    <div v-else class="workspaceContent">
      <template v-if="this.currentModule.activeStatus">
        <Row style="margin-top:20px">
          <Col
            :xs="8"
            :sm="7"
            :md="6"
            :lg="5"
            v-bind="this.olParticipants"
            offset="1"
            :style="{height:sidebarHeight+8+'px'}"
          >
            <div :style="{height:sidebarHeight-6+'px'}">
              <Card style="background-color:white">
                <h2 slot="title">Online participants</h2>
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
                          <span style="padding:0 5px;float:right">{{member.userName}}</span>
                        </div>
                        <div class="organization">
                          <span style="padding:0 5px">{{member.organization}}</span>
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </Card>
            </div>
          </Col>
          <template>
            <Col
              :xs="15"
              :sm="16"
              :md="17"
              :lg="17"
              :style="{height:sidebarHeight/3*1+'px'}"
              style="margin-bottom:20px"
            >
              <Col span="16">
                <div style="height:100%;margin-left:30px">
                  <Card :style="{height:sidebarHeight/3*1 +'px'}">
                    <h2 slot="title">{{this.currentModule.title}}</h2>
                    <div style="width:100%;padding:10px">
                      <span
                        style="word-break: break-all;text-indent:2em;padding:10px"
                      >{{this.currentModule.description}}</span>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="8">
                <div style="height:100%;margin-left:30px">
                  <Card :style="{height:sidebarHeight/3*1 +'px'}">
                    <h2 slot="title">Announcement</h2>
                    <div
                      slot="extra"
                      style="display:flex;align-items:center"
                      v-if="this.$store.getters.userInfo.userId == this.subProjectInfo.managerId"
                    >
                      <span
                        @click="noticeModalShow=true"
                        style="cursor:pointer"
                        title="add a notice"
                      >
                        <Icon type="md-add"/>
                      </span>
                      <span
                        @click="noticeDetailShow()"
                        style="cursor:pointer;margin-left:10px"
                        title="edit"
                      >
                        <Icon type="ios-create"/>
                      </span>
                      <span
                        @click="deleteNotice()"
                        style="cursor:pointer;margin-left:10px"
                        title="remove"
                      >
                        <Icon type="ios-trash"/>
                      </span>
                    </div>
                    <div>
                      <div v-if="this.currentModuleNoticeList.length!=0">
                        <h3
                          style="text-align:center"
                        >{{this.currentModuleNoticeList[this.currentModuleNoticeList.length-1].title}}</h3>
                        <p
                          style="text-indent:2em;overflow:hidden;break-word:word-break"
                        >{{this.currentModuleNoticeList[this.currentModuleNoticeList.length-1].description}}</p>
                      </div>
                      <div v-if="this.currentModuleNoticeList.length==0">
                        <p
                          style="text-indent:2em;overflow:hidden;break-word:word-break"
                        >There is no notice recently</p>
                      </div>
                    </div>
                  </Card>
                  <Modal
                    width="400px"
                    v-model="noticeModalShow"
                    title="Create a new notice"
                    @on-ok="createNotice"
                    ok-text="Confirm"
                    cancel-text="Cancel"
                  >
                    <Form :model="formItem" :label-width="60">
                      <FormItem label="title">
                        <Input v-model="formItem.title" placeholder="Enter bulletin title"></Input>
                      </FormItem>
                      <FormItem label="content">
                        <Input
                          v-model="formItem.content"
                          placeholder="Enter bulletin content"
                          type="textarea"
                        ></Input>
                      </FormItem>
                    </Form>
                  </Modal>
                  <Modal
                    width="400px"
                    v-model="noticeDetailShowModal"
                    title="Notice detail update"
                    @on-ok="editNotice()"
                    ok-text="Confirm"
                    cancel-text="Cancel"
                  >
                    <Form :model="editFormItem" :label-width="60">
                      <FormItem label="title">
                        <Input v-model="editFormItem.title" placeholder="Enter bulletin title"></Input>
                      </FormItem>
                      <FormItem label="content">
                        <Input
                          v-model="editFormItem.description"
                          placeholder="Enter bulletin content"
                          type="textarea"
                        ></Input>
                      </FormItem>
                    </Form>
                  </Modal>
                </div>
              </Col>
            </Col>
          </template>
          <template>
            <Col :xs="15" :sm="16" :md="17" :lg="17">
              <Col span="11">
                <div style="border:1px solid lightgray;background-color:white;margin-left:30px">
                  <Card>
                    <h2 slot="title">Timeline</h2>
                    <div
                      class="recordLine"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                      style="overflow-y:auto"
                    >
                      <Timeline style="padding:10px">
                        <TimelineItem v-for="(item,index) in allRecords" :key="index">
                          <template v-if="item.type == 'participants'">
                            <span class="time" style="color:gray">{{item.time}}</span>
                            <span class="time" style="color:gray; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:gray; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                          </template>
                          <template v-if="item.type == 'resource'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2;margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2;margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <a
                              style="cursor:pointer;color:green;margin-left:5px"
                              :href="'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/resource/upload/'+item.file"
                              target="_blank"
                            >download</a>
                          </template>
                          <template v-if="item.type == 'tools'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <span
                              style="cursor:pointer;color:green;margin-left:5px"
                              @click="toolPanel(item.toolType)"
                            >check</span>
                          </template>
                        </TimelineItem>
                      </Timeline>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="13">
                <div style="background-color:white;margin-left:30px" class="resourcePanel">
                  <Card>
                    <h2 slot="title">Resource</h2>
                    <div slot="extra" style="display:flex;align-items:center">
                      <span
                        id="upload"
                        type="default"
                        @click="uploadFileModal = true"
                        class="uploadBtn"
                        title="upload resource"
                        style="cursor:pointer"
                      >
                        <Icon type="md-cloud-upload" size="20"/>
                      </span>
                      <span
                        slot="extra"
                        class="moreBtn"
                        type="default"
                        style="margin-left:15px;cursor:pointer"
                        @click="toResourceList()"
                        title="more"
                      >
                        <Icon type="md-more"/>
                      </span>
                    </div>
                    <div
                      style="overflow-y:auto;padding:0px 10px 10px 10px"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                    >
                      <Table
                        style="overflow:auto"
                        :columns="tableColName"
                        :data="this.resourceList"
                        v-show="this.resourceList!=[] && this.resourceList!='None'"
                      >
                        <template slot-scope="{ row }" slot="name">
                          <strong>{{ row.name }}</strong>
                        </template>
                        <template slot-scope="{ row, index }" slot="action">
                          <Button
                            type="success"
                            size="small"
                            style="margin-right: 5px"
                            :href="resourceList[index].pathURL"
                            @click="show(index)"
                            title="Download"
                          >
                            <Icon type="md-download"/>
                          </Button>
                          <Button
                            type="warning"
                            size="small"
                            style="margin-right: 5px"
                            title="Preview"
                            @click="previewRes(index)"
                          >
                            <Icon type="md-eye"/>
                          </Button>
                        </template>
                      </Table>
                    </div>
                  </Card>
                </div>
              </Col>
            </Col>
          </template>
          <div class="util-panel" @mousedown="toolContainerMove">
            <div class="util-btn-group">
              <Button type="info" class="util-btn" shape="circle" @click="toolPanel('chat')">
                <Icon type="md-contacts" size="20" class="util-btn-icon"/>
              </Button>
              <Button type="info" class="util-btn" shape="circle" @click="drawerOpen = true">
                <Icon type="ios-albums" size="20" class="util-btn-icon"/>
              </Button>
              <Drawer :closable="false" v-model="drawerOpen" width="360" style="font-size:30px">
                <!-- tab contains collaborative and non-collaborative -->
                <Tabs value="General">
                  <TabPane label="General tools" name="General">
                    <h2>Collaborative Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline"/>
                      <span>This tools support collaborative functions for multi-participants working together.</span>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('draw')"
                          title="DrawBoard"
                          color="green"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('map')"
                          title="Map"
                          color="lightblue"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-podium"
                          size="60"
                          @click.native="toolPanel('chart')"
                          title="Chart"
                          color="lightgreen"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('tableEditor')"
                          title="Table editor"
                          color="#2d8cf0"
                        />
                      </div>
                    </div>

                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('3DmodelViewer')"
                          title="3D model Viewer"
                          color="#561cec"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('graphEditor')"
                          title="Graph Editor"
                          color="#eca01c"
                        />
                      </div>
                    </div>
                    <h2>Non-collaborative Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline"/>
                      <span>This tool can't support collaborative functions.</span>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('nc-draw')"
                          title="DrawBoard"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('nc-map')"
                          title="Map"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-podium"
                          size="60"
                          @click.native="toolPanel('nc-chart')"
                          title="Chart"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('cn-tableEditor')"
                          title="Table editor"
                          color="gray"
                        />
                      </div>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('nc-3DmodelViewer')"
                          title="3D model viewer"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-videocam"
                          size="60"
                          @click.native="toolPanel('nc-video')"
                          title="Video player"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-book"
                          size="60"
                          @click.native="toolPanel('nc-pdf')"
                          title="PDF viewer"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-clipboard"
                          size="60"
                          @click.native="toolPanel('Doc Edit')"
                          title="doc editor"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="logo-youtube"
                          size="60"
                          @click.native="toolPanel('video Tool')"
                          title="video Tool"
                          color="gray"
                        />
                      </div>
                    </div>
                  </TabPane>
                  <TabPane label="Special tools" name="Special">
                    <h2>Special Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline"/>
                      <span>For different solving process of geographic problem</span>
                    </div>
                    <br>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Preparation'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Analysis'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Modeling'">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-bonfire"
                          size="60"
                          @click.native="toolPanel('ConceptualModel')"
                          title="Conceptual Modeling"
                          color="#f90"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-git-commit"
                          size="60"
                          @click.native="toolPanel('LogicalModel')"
                          title="Logical Modeling"
                          color="#19be6b"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-pulse"
                          size="60"
                          @click.native="toolPanel('ComputationalModel')"
                          title="Computational Modeling"
                          color="#2d8cf0"
                        />
                      </div>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Simulation'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'validation'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Comparison'"></div>
                  </TabPane>
                </Tabs>
              </Drawer>
            </div>
          </div>
        </Row>
      </template>
      <template v-else>
        <Row style="margin-top:20px" :style="{height:sidebarHeight+8+'px'}">
          <template>
            <Col span="22" offset="1" :style="{height:sidebarHeight/3*1+'px'}">
              <Card :style="{height:sidebarHeight/3*1 +'px'}">
                <h2 slot="title">{{this.currentModule.title}}</h2>
                <div style="width:100%;padding:10px">
                  <span
                    style="word-break: break-all;text-indent:2em;padding:10px"
                  >{{this.currentModule.description}}</span>
                </div>
              </Card>
            </Col>
          </template>
          <template>
            <Col span="22" offset="1" style="margin-top:20px;">
              <Col span="12">
                <div style="border:1px solid lightgray;background-color:white;margin-right:15px">
                  <Card>
                    <h2 slot="title">Timeline</h2>
                    <div
                      class="recordLine"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                      style="overflow-y:auto"
                    >
                      <Timeline style="padding:10px">
                        <TimelineItem v-for="(item,index) in historyRecords" :key="index">
                          <template v-if="item.type == 'participants'">
                            <span class="time" style="color:gray">{{item.time}}</span>
                            <span class="time" style="color:gray; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:gray; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                          </template>
                          <template v-if="item.type == 'resource'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2;margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2;margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <a
                              style="cursor:pointer;color:green;margin-left:5px"
                              :href="'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/resource/upload/'+item.file"
                              target="_blank"
                            >download</a>
                          </template>
                          <template v-if="item.type == 'tools'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2; margin-left:10px; word-break:break-word; cursor:pointer"
                              @click="toolPanel(item.toolType)"
                            >{{item.content}}</span>
                          </template>
                        </TimelineItem>
                      </Timeline>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="12">
                <div style="background-color:white;margin-left:15px">
                  <Card>
                    <h2 slot="title">Resource</h2>
                    <div slot="extra" style="display:flex;align-items:center">
                      <span
                        id="upload"
                        type="default"
                        @click="uploadFileModal = true"
                        class="uploadBtn"
                        title="upload resource"
                        style="cursor:pointer"
                      >
                        <Icon type="md-cloud-upload" size="20"/>
                      </span>
                      <span
                        slot="extra"
                        class="moreBtn"
                        type="default"
                        style="margin-left:15px;cursor:pointer"
                        @click="toResourceList()"
                        title="more"
                      >
                        <Icon type="md-more"/>
                      </span>
                    </div>
                    <div
                      style="overflow-y:auto;padding:0px 10px 10px 10px"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                    >
                      <Table
                        style="overflow:auto"
                        :columns="tableColName"
                        :data="this.resourceList"
                        v-show="this.resourceList!=[] && this.resourceList!='None'"
                      >
                        <template slot-scope="{ row }" slot="name">
                          <strong>{{ row.name }}</strong>
                        </template>
                        <template slot-scope="{ row, index }" slot="action">
                          <Button
                            type="success"
                            size="small"
                            style="margin-right: 5px"
                            :href="resourceList[index].pathURL"
                            @click="show(index)"
                            title="Download"
                          >
                            <Icon type="md-download"/>
                          </Button>
                          <Button
                            type="warning"
                            size="small"
                            style="margin-right: 5px"
                            title="Preview"
                            @click="previewRes(index)"
                          >
                            <Icon type="md-eye"/>
                          </Button>
                        </template>
                      </Table>
                    </div>
                  </Card>
                </div>
              </Col>
            </Col>
          </template>
        </Row>
      </template>
    </div>
    <Modal
      v-model="delModal"
      title="Delete this process"
      @on-ok="delModule"
      @on-cancel="cancel"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal
      width="600px"
      v-model="editModal"
      title="Update this process"
      @on-ok="updateModule('formValidate2')"
      @on-cancel="cancel()"
    >
      <Form
        ref="formValidate2"
        :model="formValidate2"
        :rules="ruleValidate2"
        :label-width="80"
        style="margin-left: 30px"
      >
        <FormItem label="Name" prop="updateModuleTitle">
          <Input
            v-model="formValidate2.updateModuleTitle"
            placeholder="Enter something..."
            style="width: 400px"
          />
        </FormItem>
        <FormItem label="Type" prop="updateModuleType">
          <Select
            v-model="formValidate2.updateModuleType"
            style="width:400px"
            placeholder="please select module type..."
          >
            <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
      </Form>
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
    <Modal
      width="600px"
      v-model="addModal"
      title="Start a new process"
      @on-ok="addModule('formValidate1')"
      @on-cancel="cancel()"
    >
      <Form
        ref="formValidate1"
        :model="formValidate1"
        :rules="ruleValidate1"
        :label-width="80"
        style="margin-left: 30px"
      >
        <FormItem label="Name" prop="moduleTitle">
          <Input
            v-model="formValidate1.moduleTitle"
            placeholder="Enter something..."
            style="width: 400px"
          />
        </FormItem>
        <FormItem label="Type" prop="moduleType">
          <Select
            v-model="formValidate1.moduleType"
            style="width:400px"
            placeholder="please select module type..."
          >
            <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div class="addNodeStyle">
        <span style="margin:0 10px 0 10px">Detail</span>
        <textarea v-model="moduleDescription" style="width:400px" :rows="6"></textarea>
      </div>
    </Modal>
    <Modal
      v-model="inheritData"
      title="Choose data to next process"
      @on-ok="createModule()"
      @on-cancel="cancel()"
    >
      <Transfer
        :data="inheritResource"
        :target-keys="targetKeys"
        :list-style="listStyle"
        :render-format="resourceRender"
        :titles="['This process', 'The next process']"
        filter-placeholder="Enter key words..."
        filterable
        :filter-method="filterMethod"
        @on-change="handleChange"
      ></Transfer>
    </Modal>
    <Modal
      width="600px"
      v-model="activateModal"
      title="Activate this process"
      @on-ok="activateModule()"
      @on-cancel="cancel()"
    >
      <p>Do you want to activate this process?</p>
    </Modal>
    <Modal
      v-model="uploadFileModal"
      title="Upload resource"
      @on-ok="submitFile('formValidate3')"
      ok-text="Submit"
      cancel-text="Cancel"
      width="600px"
      :mask-closable="false"
    >
      <Form
        ref="formValidate3"
        :model="formValidate3"
        :rules="ruleValidate3"
        :label-width="80"
        style="margin-left:20px"
      >
        <FormItem label="File type: " prop="fileType">
          <RadioGroup v-model="formValidate3.fileType">
            <Radio label="data">Data</Radio>
            <Radio label="image">Images</Radio>
            <Radio label="video">Videos</Radio>
            <Radio label="paper">Papers</Radio>
            <Radio label="document">Documents</Radio>
            <Radio label="model">Models</Radio>
            <Radio label="others">Others</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <span style="width:20%">Description</span>
        <Input type="textarea" :rows="2" v-model="fileDescription"/>
      </div>
      <br>
      <input type="file" @change="getFile($event)" style="margin-left:20px" multiple="multiple">
    </Modal>
  </div>
</template>
<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
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
      // current: 0,
      addModal: false,
      copyModal: false,
      delModal: false,
      inheritData: false,
      //编辑的模态框
      editModal: false,
      activateModal: false,
      order: -1,
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
        "Validation",
        "Comparison"
      ],
      // 添加/编辑module
      formValidate1: {
        moduleTitle: "",
        moduleType: ""
      },
      ruleValidate1: {
        moduleTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        moduleType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      formValidate2: {
        updateModuleTitle: "",
        updateModuleType: ""
      },
      ruleValidate2: {
        updateModuleTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        updateModuleType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      formValidate3: {
        fileType: "data"
      },
      ruleValidate3: {
        fileType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
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
      // web socket for module
      subprojectSocket: null,
      timer: null,
      // 动态记录相关
      // 所有module的记录
      allRecords: [],
      historyRecords: [],
      allHistRecords: [],
      // 当前参与者
      olParticipants: [],
      ofParticipants: [],
      uploadFileModal: false,
      resourceList: [],
      tableColName: [
        {
          title: "Name",
          key: "name",
          sortable: true,
          tooltip: true
        },
        {
          title: "Type",
          key: "type",
          sortable: true,
          width: 90
        },
        {
          title: "Action",
          slot: "action",
          align: "center",
          width: 120
        }
      ],
      fileDescription: "",
      resourceHeight: 400,
      //资源继承
      inheritResource: [],
      targetKeys: [],
      selectResource: [],
      listStyle: { width: "210px", height: "300px" },
      //通知相关的变量
      noticeModalShow: false,
      formItem: {
        title: "",
        content: ""
      },
      editFormItem: {
        title: "",
        description: ""
      },
      currentModuleNoticeList: [{ title: "", description: "" }],
      // 控制点击notice后模态框显示的modal
      noticeDetailShowModal: false,
      // 当前选中通知条目的详情
      currentNoticeDetail: [],
      panelList: [],
      panel: null,
      toProjectPage: "",
      toSubProjectPage: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
    this.openModuleSocket();
    this.getHistoryRecords();
    this.getAllModules("init");
    this.toProjectPage = "/project/" + sessionStorage.getItem("projectId");
    this.toSubProjectPage =
      "/project/" + sessionStorage.getItem("subProjectId") + "/subproject";
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
    this.removeTimer();
    this.closeModuleSocket();
    this.closePanel();
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.reSize);
    this.closeModuleSocket();
  },
  updated: function() {
    this.$nextTick(function() {
      var div = document.getElementsByClassName("recordLine");
      for (let i = 0; i < div.length; i++) {
        div[i].scrollTop = div[i].scrollHeight;
      }
    });
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 227;
      this.resourceHeight = this.sidebarHeight - 358;
      //通知栏的属性设置，top表示距离顶部的距离，duration表示持续的时间
      this.$Notice.config({
        top: 50,
        duration: 0
      });
    },
    reSize() {
      this.initSize();

      this.toolContainerLeft = window.innerWidth - 60;
      this.toolContainerTop = window.innerHeight - 140;
      document.getElementsByClassName("util-panel")[0].style.left = 0 + "px";
      document.getElementsByClassName("util-panel")[0].style.top = 200 + "px";
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
            sessionStorage.setItem("subProjectId", subProjectInfo.subProjectId);
            sessionStorage.setItem("subProjectName", subProjectInfo.title);

            this.managerIdentity(subProjectInfo.managerId);
            this.memberIdentity(subProjectInfo.members);
            this.$store.commit("setSubProjectInfo", subProjectInfo);
          }
        },
        error: function(err) {
          console.log("Get manager name fail.");
        }
      });
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
    getProjectInfo() {
      let projectInfo = this.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.subProjectInfo.projectId
      ) {
        this.projectInfo = projectInfo;
      } else {
        let _this = this;
        this.axios
          .get(
            "/GeoProblemSolving/project/inquiry" +
              "?key=projectId" +
              "&value=" +
              this.subProjectInfo.projectId
          )
          .then(res => {
            if (res.data != "None" && res.data != "Fail") {
              _this.projectInfo = res.data[0];

              sessionStorage.setItem("projectId", _this.projectInfo.projectId);
              sessionStorage.setItem("projectName", _this.projectInfo.title);
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
      let oldId = this.currentModule.moduleId;

      this.currentModuleIndex = item;
      this.order = item;
      this.currentModule = this.moduleList[this.currentModuleIndex];
      sessionStorage.setItem("moduleId", this.currentModule.moduleId);
      sessionStorage.setItem("moduleName", this.currentModule.title);

      if (oldId !== this.currentModule.moduleId) {
        //查询公告
        this.inquiryNotice();
        this.getAllResource();
        let records = [];
        if (!this.currentModule.activeStatus) {
          for (let i = 0; i < this.allHistRecords.length; i++) {
            if (
              this.currentModule.moduleId == this.allHistRecords[i].moduleId
            ) {
              let record = this.allHistRecords[i];
              records.push(record);
            }
          }
          this.historyRecords = records;
        } else {
          let noRecords = true;
          for (let i = 0; i < this.allRecords.length; i++) {
            if (this.allRecords[i].type != "participants") {
              noRecords = false;
              break;
            }
          }
          if (noRecords) {
            for (let i = 0; i < this.allHistRecords.length; i++) {
              if (
                this.currentModule.moduleId == this.allHistRecords[i].moduleId
              ) {
                let record = this.allHistRecords[i];
                records.push(record);
              }
            }
            let temp = records.concat(this.allRecords);
            this.allRecords = temp;
          }
        }
      }
    },
    closeModuleSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openModuleSocket() {
      if (this.subprojectSocket != null) {
        this.subprojectSocket = null;
      }
      let subProjectId = this.subProjectInfo.subProjectId;
      // var subprojectSocketURL = "ws://localhost:8081/GeoProblemSolving/Module/" + subProjectId;
      var subprojectSocketURL = "ws://"+this.$store.state.IP_Port+"/GeoProblemSolving/Module/" + subProjectId;
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

      // 资源记录
      if (messageJson.type == "resource") {
        let record = {};
        record["who"] = messageJson.who;
        record["content"] = messageJson.content;
        record["time"] = messageJson.time;
        this.allRecords.push(messageJson);
        this.getAllResource();
      }
      // 工具记录
      else if (messageJson.type == "tools") {
        let record = {};
        record["who"] = messageJson.who;
        record["content"] = messageJson.content;
        record["time"] = messageJson.time;
        this.allRecords.push(messageJson);
      }
      // module 更新
      else if (messageJson.type == "module") {
        this.getAllModules("init");
      } else if (messageJson.type == "members") {
        // 比较 判断人员动态 更新records

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
        if (
          that.subprojectSocket != null &&
          that.subprojectSocket != undefined
        ) {
          that.subprojectSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
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

          // 人员渲染
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
                  record.content = "enter this process module.";
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
          record.content = "leave this process module.";
          this.olParticipants.splice(userIndex, 1);
        }
      }
      //records 更新
      this.allRecords.push(record);
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
    getHistoryRecords() {
      this.allHistRecords = [];
      let that = this;
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry" +
            "?eventType=record" +
            "&key=scopeId" +
            "&value=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None") {
            for (let i = 0; i < res.data.length; i++) {
              let record = JSON.parse(res.data[i].description);
              that.allHistRecords.push(record);
            }
          }
        });
    },
    chooseResource() {
      this.inheritData = true;
      this.inheritResource = this.getMockData();
      // this.targetKeys = this.getTargetKeys();
    },
    createModule() {
      this.addModal = true;
      this.selectResource = [];
      this.selectResource = this.getTargetKeys();
    },
    getMockData() {
      let mockData = [];
      for (let i = 0; i < this.resourceList.length; i++) {
        mockData.push({
          key: i,
          name: this.resourceList[i].name,
          type: this.resourceList[i].type,
          resourceId: this.resourceList[i].resourceId
        });
      }
      return mockData;
    },
    getTargetKeys() {
      let mockData = [];
      if (this.inheritResource.length > 0) {
        for (let i = 0; i < this.targetKeys.length; i++) {
          mockData.push({
            key: this.targetKeys[i],
            name: this.inheritResource[this.targetKeys[i]].name,
            type: this.inheritResource[this.targetKeys[i]].type,
            resourceId: this.inheritResource[this.targetKeys[i]].resourceId
          });
        }
      }
      return mockData;
    },
    handleChange(newTargetKeys) {
      this.targetKeys = newTargetKeys;
    },
    filterMethod(data, query) {
      return data.type.indexOf(query) > -1;
    },
    resourceRender(item) {
      return item.type + " - " + item.name;
    },
    getAllModules(state) {
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

            if (state == "init") {
              let index = this.getActiveModule();
              this.showDetail(index);
            } else if (state == "update") {
              this.allRecords = [];
              this.showDetail(this.order);
            }
          } else if (res.data == "None") {
            this.moduleList = [];
          }
        })
        .catch(err => {});
    },
    addModule(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (
            this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
          ) {
            let Module = {};
            Module["activeStatus"] = true;
            Module["subProjectId"] = this.$route.params.id;
            Module["title"] = this.formValidate1.moduleTitle;
            Module["description"] = this.moduleDescription;
            Module["creator"] = this.$store.getters.userId;
            Module["type"] = this.formValidate1.moduleType;
            if (this.moduleList.length !== 0) {
              Module["foreModuleId"] = this.moduleList[
                this.moduleList.length - 1
              ].moduleId;
            } else {
              Module["foreModuleId"] = "";
            }
            let this1 = this;
            let this2 = this;
            this.axios
              .post("/GeoProblemSolving/module/create", Module)
              .then(res => {
                if (res.data === "Fail") {
                  this1.$Message.info("Fail");
                } else {
                  //更新新module的资源
                  this1.copyResource(res.data);

                  // 使其他module为非激活状态
                  if (this1.moduleList.length > 0) {
                    let moduleId = "";
                    if (this1.currentModule.activeStatus) {
                      moduleId = this1.currentModule.moduleId;
                    } else {
                      let index = this1.getActiveModule();
                      moduleId = this1.moduleList[index].moduleId;
                    }
                    let updateObject = new URLSearchParams();
                    updateObject.append("moduleId", moduleId);
                    updateObject.append("activeStatus", false);
                    this1.axios
                      .post("/GeoProblemSolving/module/update", updateObject)
                      .then(res => {
                        this2.getAllModules("update");

                        let socketMsg = { type: "module", operate: "update" };
                        this2.subprojectSocket.send(JSON.stringify(socketMsg));
                      })
                      .catch(err => {
                        console.log(err.data);
                      });
                  } else {
                    this1.getAllModules("update");
                  }

                  this1.createModuleSuccess(Module["title"]);
                  this1.moduleList.push(Module);
                  this1.showDetail(this.moduleList.length - 1);
                  this1.formValidate1.moduleTitle = "";
                  this1.moduleDescription = "";
                  this1.formValidate1.moduleType = "";
                }
              })
              .catch(err => {
                console.log(err.data);
              });
          }
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    activateModule() {
      var this1 = this;
      var this2 = this;
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        if (!this.currentModule.activeStatus) {
          let updateObject = new URLSearchParams();
          updateObject.append("moduleId", this.currentModule.moduleId);
          updateObject.append("activeStatus", true);
          this.axios
            .post("/GeoProblemSolving/module/update", updateObject)
            .then(res => {
              let activeModelIndex = this1.getActiveModule();
              if (this1.moduleList[activeModelIndex].activeStatus) {
                let updateObject = new URLSearchParams();
                updateObject.append(
                  "moduleId",
                  this1.moduleList[activeModelIndex].moduleId
                );
                updateObject.append("activeStatus", false);
                this.axios
                  .post("/GeoProblemSolving/module/update", updateObject)
                  .then(res => {
                    this2.getAllModules("update");

                    let socketMsg = { type: "module", operate: "update" };
                    this2.subprojectSocket.send(JSON.stringify(socketMsg));
                  })
                  .catch(err => {
                    console.log(err.data);
                  });
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      }
    },
    delModule() {
      let that = this;
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        // let foreModuleId = this.currentModule.foreModuleId;
        // let nextModuleId = this.currentModule.nextModuleId;
        this.axios
          .get(
            "/GeoProblemSolving/module/delete" +
              "?moduleId=" +
              this.currentModule.moduleId
          )
          .then(res => {
            if (res.data === "Success") {
              that.deleteModuleSuccess();
              // this.getAllModules("delete");
              if (that.moduleList.length > 1) {
                that.moduleList.splice(that.currentModuleIndex, 1);

                let index = that.getActiveModule();
                that.showDetail(index);

                let socketMsg = { type: "module", operate: "update" };
                that.subprojectSocket.send(JSON.stringify(socketMsg));
              } else {
                that.moduleList = [];
              }
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    updateModule(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (
            this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
          ) {
            var that = this;
            let updateObject = new URLSearchParams();
            updateObject.append("moduleId", this.currentModule.moduleId);
            updateObject.append("title", this.formValidate2.updateModuleTitle);
            updateObject.append("description", this.updateModuleDescription);
            updateObject.append("type", this.formValidate2.updateModuleType);
            updateObject.append("creater", this.$store.getters.userId);
            this.axios
              .post("/GeoProblemSolving/module/update", updateObject)
              .then(res => {
                that.getAllModules("init");

                let socketMsg = { type: "module", operate: "update" };
                that.subprojectSocket.send(JSON.stringify(socketMsg));
              })
              .catch(err => {
                console.log(err.data);
              });
          }
        } else {
          this.$Message.error("Please enter the necessary information !");
        }
      });
    },
    copyResource(newModuleId) {
      for (let i = 0; i < this.selectResource.length; i++) {
        for (let j = 0; j < this.resourceList.length; j++) {
          if (
            this.resourceList[j].resourceId == this.selectResource[i].resourceId
          ) {
            let resourceInfo = this.resourceList[j];
            let shareForm = new FormData();
            shareForm.append("name", resourceInfo.name);
            shareForm.append("description", resourceInfo.description);
            shareForm.append("belong", resourceInfo.belong);
            shareForm.append("type", resourceInfo.type);
            shareForm.append("fileSize", resourceInfo.fileSize);
            shareForm.append("pathURL", resourceInfo.pathURL);
            shareForm.append("uploaderId", resourceInfo.uploaderId);
            let scopeObject = {
              projectId: resourceInfo.scope.projectId,
              subProjectId: resourceInfo.scope.subProjectId,
              moduleId: newModuleId
            };
            shareForm.append("scope", JSON.stringify(scopeObject));

            if (
              newModuleId != null &&
              newModuleId != undefined &&
              newModuleId.length > 0
            ) {
              this.axios
                .post("/GeoProblemSolving/resource/share", shareForm)
                .then(res => {
                  if (res.data != "Fail") {
                    //...
                  }
                })
                .catch(err => {
                  console.log(err);
                });
            }
            break;
          }
        }
      }
    },
    getActiveModule() {
      var index = 0;
      for (let i = 0; i < this.moduleList.length; i++) {
        if (this.moduleList[i].activeStatus) {
          index = i;
          break;
        }
      }
      return index;
    },
    toResourceList() {
      this.$router.push({ path: "/resourceList" });
    },
    getFile(event) {
      this.file = event.target.files[0];
    },
    submitFile(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          let formData = new FormData();
          formData.append("file", this.file);
          formData.append("description", "");
          formData.append("type", this.formValidate3.fileType);
          formData.append("uploaderId", this.$store.getters.userId);
          // currentModule.title;
          // currentModule.moduleId
          // 添加字段属于那个项目
          formData.append("belong", this.currentModule.title);

          if (
            sessionStorage.getItem("projectId") == "" ||
            sessionStorage.getItem("projectId") == undefined ||
            sessionStorage.getItem("projectId") == null
          ) {
            this.getProjectInfo();
          }
          this.sleep(1000).then(() => {
            let scopeObject = {
              projectId: sessionStorage.getItem("projectId"),
              subProjectId: sessionStorage.getItem("subProjectId"),
              moduleId: this.currentModule.moduleId
            };
            formData.append("scope", JSON.stringify(scopeObject));
            //这里还要添加其他的字段
            let that = this;
            this.axios
              .post("/GeoProblemSolving/resource/upload", formData)
              .then(res => {
                if (res.data != "Size over" && res.data.length > 0) {
                  that.$Notice.open({
                    title: "Upload notification title",
                    desc: "File uploaded successfully",
                    duration: 0
                  });
                  //这里重新获取全部资源
                  that.getAllResource();

                  // 同步
                  let record = {
                    who: that.$store.getters.userName,
                    whoid: that.$store.getters.userId,
                    type: "resource",
                    content:
                      "upload a/an " +
                      that.formValidate3.fileType +
                      " : " +
                      that.file.name,
                    moduleId: this.currentModule.moduleId,
                    time: new Date().toLocaleString(),
                    file: res.data[0].fileName
                  };
                  that.subprojectSocket.send(JSON.stringify(record));
                  // that.allRecords.push(record);
                }
                // console.log(res.data);
              })
              .catch(err => {});
          });
        } else {
          this.$Message.error("Please enter the resource type!");
        }
      });
    },
    sleep(time) {
      return new Promise(resolve => setTimeout(resolve, time));
    },
    //获取全部资源的方法
    getAllResource() {
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.moduleId" +
            "&value=" +
            window.sessionStorage.getItem("moduleId")
        )
        .then(res => {
          if (res.data !== "None") {
            this.$set(this, "resourceList", res.data);
            //存入store
            sessionStorage.setItem("resources", JSON.stringify(res.data));
          } else {
            this.resourceList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    backtoSubproject() {
      this.$router.push(`./subproject`);
    },
    editModalShow() {
      this.editModal = true;
      let order = this.currentModuleIndex;
      this.formValidate2.updateModuleTitle = this.moduleList[order].title;
      this.formValidate2.updateModuleType = this.moduleList[order].type;
      this.updateModuleDescription = this.moduleList[order].description;
    },
    // 返回项目页
    backProject() {
      this.getProjectInfo();
      let id = this.projectInfo.projectId;
      this.$router.push(`../${id}`);
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    show(index) {
      window.open(this.resourceList[index].pathURL);
    },
    toolContainerMove(e) {
      let odiv = e.target;
      let disX = e.clientX - odiv.offsetLeft;
      let disY = e.clientY - odiv.offsetTop;
      document.onmousemove = e => {
        let left = e.clientX - disX;
        let top = e.clientY - disY;

        if (
          !(
            left < 0 ||
            top < 60 ||
            left > window.innerWidth - 60 ||
            top > window.innerHeight - 140
          )
        ) {
          //移动当前元素
          odiv.style.left = left + "px";
          odiv.style.top = top + "px";
        }
      };
      document.onmouseup = e => {
        document.onmousemove = null;
        document.onmouseup = null;
      };
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
      let toolName = "";

      if (type == "map") {
        toolURL = '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/map" style="width: 100%;height:100%"></iframe>';
        toolName = "Map";
      } else if (type == "draw") {
        toolURL =  '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/draw" style="width: 100%;height:100%"></iframe>';
        toolName = "Drawing";
      } else if (type == "chart") {
        toolURL = '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/charts" style="width: 100%;height:100%"></iframe>';
        toolName = "Chart";
      } else if (type == "chat") {
        toolURL = '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
        toolName = "Chatroom";
      } else if (type == "graphEditor") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/GraphEditor/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Sketchpad";
      } else if (type == "3DmodelViewer") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "3D model viewer";
      } else if (type == "LogicalModel") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/LogicalModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Logical modeling";
      } else if (type == "ConceptualModel") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/ConceptualModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Conceptual modeling";
      } else if (type == "ComputationalModel") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/ComputationalModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Computational modeling";
      } else if (type == "tableEditor") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/excelTool.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Table editor";
      } else if (type == "nc-map") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/nc/map" style="width: 100%;height:100%"></iframe>';
        toolName = "Map";
      } else if (type == "nc-draw") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/nc/draw" style="width: 100%;height:100%"></iframe>';
        toolName = "Drawing";
      } else if (type == "nc-chart") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/nc/charts" style="width: 100%;height:100%"></iframe>';
        toolName = "Chart";
      } else if (type == "cn-tableEditor") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/excelToolSingle.html' +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "Table editor";
      } else if (type == "nc-3DmodelViewer") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/indexSingle.html' +
          '" style="width: 100%;height:100%"></iframe>';
        toolName = "3D model viewer";
      } else if (type == "nc-video") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/video" style="width: 100%;height:100%"></iframe>';
        toolName = "Video player";
      } else if (type == "nc-pdf") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/pdfview" style="width: 100%;height:100%"></iframe>';
        toolName = "Pdf viewer";
      } else if (type == "Doc Edit") {
        toolURL =
          '<iframe src="'+'http://'+this.$store.state.IP_Port+'/GeoProblemSolving/tinymce" style="width: 100%;height:100%"></iframe>';
        toolName = "Text editor";
      } else if (type == "video Tool") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/vedioChat/WebRtcTest.html" style="width: 100%;height:100%"></iframe>';
        toolName = "Video Tool";
      }

      let panel = jsPanel.create({
        theme: "primary",
        headerTitle: toolName,
        contentSize: "1000 600",
        content: toolURL,
        disableOnMaximized: true,
        dragit: {
          containment: 5
        },
        callback: function() {
          // this.content.style.padding = "20px";
        }
      });
      // panel.resizeit("disable");
      $(".jsPanel-content").css("font-size", "0");
      this.panelList.push(panel);

      // 生成records, 同步
      let record = {
        who: this.$store.getters.userName,
        whoid: this.$store.getters.userId,
        type: "tools",
        toolType: type,
        content: "used a tool: " + type,
        moduleId: this.currentModule.moduleId,
        time: new Date().toLocaleString()
      };
      this.subprojectSocket.send(JSON.stringify(record));
      // this.allRecords.push(record);
    },
    closePanel() {
      for (let i = 0; i < this.panelList.length; i++) {
        this.panelList[i].close();
      }

      if (this.panel != null) {
        this.panel.close();
      }
    },
    createNotice() {
      let noticeForm = {};
      noticeForm["title"] = this.formItem.title;
      noticeForm["description"] = this.formItem.content;
      noticeForm["moduleId"] = this.currentModule.moduleId;
      this.axios
        .post("/GeoProblemSolving/bulletin/save", noticeForm)
        .then(res => {
          if (res.data != "Fail") {
            this.$Notice.success({
              title: "Create notice result",
              desc: "The notice has been created successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    inquiryNotice() {
      this.axios
        .get(
          "/GeoProblemSolving/bulletin/inquiry?key=moduleId&value=" +
            window.sessionStorage.getItem("moduleId")
        )
        .then(res => {
          if (res.data != "Fail") {
            this.currentModuleNoticeList = res.data;
          }
        })
        .catch(err => {});
    },
    editNotice() {
      let updateForm = new URLSearchParams();
      updateForm.append("bulletinId", this.currentNoticeDetail["bulletinId"]);
      updateForm.append("title", this.editFormItem["title"]);
      updateForm.append("description", this.editFormItem["description"]);
      this.axios
        .post("/GeoProblemSolving/bulletin/update", updateForm)
        .then(res => {
          if (res.data != "Fail") {
            this.$Notice.info({
              title: "update result",
              desc: "update announcement successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {});
    },
    deleteNotice() {
      var index = this.currentModuleNoticeList.length - 1;
      let bulletinId = this.currentModuleNoticeList[index]["bulletinId"];
      this.axios
        .get("/GeoProblemSolving/bulletin/delete" + "?bulletinId=" + bulletinId)
        .then(res => {
          if (res.data != "Fail") {
            this.$Notice.info({
              title: "delete result",
              desc: "delete announcement successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {});
    },
    noticeDetailShow() {
      this.noticeDetailShowModal = true;
      let index = this.currentModuleNoticeList.length - 1;
      this.currentNoticeDetail = this.currentModuleNoticeList[index];
      this.editFormItem.title = this.currentNoticeDetail.title;
      this.editFormItem.description = this.currentNoticeDetail.description;
      // this.currentModuleNoticeList[index];显示在模态框里面的内容
    },
    previewRes(index) {
      let name = this.resourceList[index].name;

      if (/\.(pdf|doc|docx|xls|xlsx|ppt|pptx|zip)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.7:8012/previewFile?url=" +'http://'+this.$store.state.IP_Port+
          this.resourceList[index].pathURL;
        var toolURL =
          '<iframe src=' + url + ' style="width: 100%;height:100%"></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else if(/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          'http://'+this.$store.state.IP_Port+ this.resourceList[index].pathURL;
        var toolURL =
          '<video src=' + url + ' style="width: 100%;height:100%" controls></video>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else if(/\.(xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())){
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          'http://'+this.$store.state.IP_Port+ this.resourceList[index].pathURL;
        var toolURL =
          '<iframe src=' + url + ' style="width: 100%;height:100%" controls></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    }
  }
};
</script>
