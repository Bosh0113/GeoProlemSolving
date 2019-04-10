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
.tool-panel {
  display: flex;
  height: auto;
  /* justify-content: center; */
  align-items: center;
}
/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  cursor: pointer;
  width:20%;
  display: flex;
  justify-content:center;
}
.singl_tool_style:hover {
  transition: all 1s;
  background-color: lightgray;
}
.singl_tool_style span {
  display: flex;
  text-align: center;
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
          <p
            slot="title"
            style="height:40px;line-height:40px;font-size:20px;display: inline-block;cursor: pointer;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;max-width: 50%;"
            @click="backtoSubproject()"
          >{{subProjectInfo.title}}</p>
          <div
            slot="extra"
            style="height:40px;display:flex;align-items:center"
            class="operatePanel"
          >
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
              <template v-else-if="this.currentModule.activeStatus">
                <Button
                  type="default"
                  @click="addModal = true"
                  icon="md-add"
                  class="addBtn"
                  title="Add a new module"
                >Add</Button>
                <Button
                  type="default"
                  @click="editModalShow()"
                  icon="md-brush"
                  class="editBtn"
                  title="Edit this module"
                >Edit</Button>
              </template>
              <template v-else>
                <Button
                  type="default"
                  @click="copyModal = true"
                  icon="ios-copy"
                  class="addBtn"
                  title="Copy this module"
                >Copy</Button>
                <Button
                  type="default"
                  @click="activateModal = true"
                  icon="md-bulb"
                  class="editBtn"
                  title="Activate this module"
                >Activate</Button>
              </template>
              <Button
                type="default"
                @click="delModal = true"
                icon="md-remove"
                class="removeBtn"
                title="Remove this module"
              >Remove</Button>
            </template>
          </div>
          <Row>
            <Col span="22" offset="1" style="margin-top:10px;background-color:white;">
              <template v-if="this.$store.getters.userInfo.userId == this.subProjectInfo.managerId">
                <Steps :current="order">
                  <Step
                    v-for="(list,index) in moduleList"
                    :key="index"
                    @click.native="showDetail(index)"
                    :title="list.title"
                    :order="index"
                  ></Step>
                </Steps>
              </template>
              <template v-else>
                <Steps :current="order" style="pointer-events: none">
                  <Step
                    v-for="(list,index) in moduleList"
                    :key="index"
                    @click.native="showDetail(index)"
                    :title="list.title"
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
            <div @click="addModal = true" style="cursor:pointer">
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
            :style="{height:sidebarHeight+6+'px'}"
          >
            <div
              class="member-panel"
              :style="{height:sidebarHeight-6+'px'}"
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
                        <span style="padding:0 5px;float:right">{{member.userName}}</span>
                      </div>
                      <div class="organization">
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
              :xs="{span: 14, offset: 1}"
              :sm="{span: 15, offset: 1}"
              :md="{span: 16, offset: 1}"
              :lg="{span: 16, offset: 1}"
              :style="{height:sidebarHeight/5*2+'px'}"
              style="margin-bottom:20px"
            >
              <div style="height:100%;background-color:white">
                <h2
                  style="width:100%;padding:10px 10px 0 10px; display: inline-block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;max-width: 80%;"
                >{{this.currentModule.title}}</h2>
                <hr>
                <div style="width:100%;padding:10px">
                  <span
                    style="word-break: break-all;text-indent:2em;padding:10px"
                  >{{this.currentModule.description}}</span>
                </div>
              </div>
            </Col>
          </template>
          <template>
            <Col :xs="{span: 14, offset: 1}"
              :sm="{span: 15, offset: 1}"
              :md="{span: 16, offset: 1}"
              :lg="{span: 16, offset: 1}"
              >
              <Col span="12" >
                <div
                :style="{height:sidebarHeight/5*3 - 32 + 'px'}"
                style="border:1px solid lightgray;background-color:white;overflow-y:scroll"
              >
              <span
                  style="height:40px;line-height:40px;margin-left:20px;font-size:1.5em;font-weight: bold"
                >Timeline</span>
                <Timeline style="padding:10px">
                  <TimelineItem v-for="(item,index) in allRecords" :key="index">
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
              <Col span="11" offset="1">
                <div style="background-color:white" :style="{height:sidebarHeight/5*3 - 32 + 'px'}" class="resourcePanel">
                <span
                  style="height:40px;line-height:40px;margin-left:20px;font-size:1.5em;font-weight: bold"
                >Resource</span>
                <div style="float:right;margin:4px 10px 0 0" class="popCenter" >
                  <Button
                    id="upload"
                    type="default"
                    @click="uploadFileModalShow()"
                    class="uploadBtn"
                    title="upload resource"
                  >
                    <Icon type="md-cloud-upload" size="20"/>
                  </Button>
                  <Button
                    class="moreBtn"
                    type="default"
                    style="margin-left: 10px"
                    @click="toResourceList()"
                    title="more"
                  >
                    <Icon type="md-more"/>
                  </Button>
                </div>
                <div style="padding:0px 10px 10px 10px">
                  <Table
                    style="overflow:auto"
                    :columns="projectTableColName"
                    :data="this.projectResourceList"
                    v-show="this.projectResourceList!=[] && this.projectResourceList!='None'"
                  >
                    <template slot-scope="{ row }" slot="name">
                      <strong>{{ row.name }}</strong>
                    </template>
                    <template slot-scope="{ row, index }" slot="action">
                      <Button
                        type="success"
                        size="small"
                        style="margin-right: 5px"
                        :href="projectResourceList[index].pathURL"
                        @click="show(index)"
                        title="download"
                      >
                        <Icon type="md-download"/>
                      </Button>
                      <Button type="warning" size="small" style="margin-right: 5px" title="View">
                        <Icon type="md-eye"/>
                      </Button>
                    </template>
                  </Table>
                </div>
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
              <Drawer :closable="false" v-model="drawerOpen" width="640" style="font-size:30px">
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
                          type="md-analytics"
                          size="60"
                          @click.native="useModelingTool"
                          title="Modeling Tools"
                          color="orange"
                        />
                        <br>
                        <span>Analyze</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-brush"
                          size="60"
                          @click.native="toolPanel('draw')"
                          title="DrawBoard"
                          color="green"
                        />
                        <br>
                        <span>Draw</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('map')"
                          title="Map"
                          color="lightblue"
                        />
                        <br>
                        <span>Map</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('chart')"
                          title="Chart"
                          color="darkgreen"
                        />
                        <br>
                        <span>Chart</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('graphEditor')"
                          title="Graph Editor"
                          color="gray"
                        />
                        <br>
                        <span>Graph Editor</span>
                      </div>

                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('3DmodelViewer')"
                          title="3D model Viewer"
                          color="gray"
                        />
                        <br>
                        <span style="display:flex;justify-content:center">3D model Viewer</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-git-commit"
                          size="60"
                          @click.native="toolPanel('LogicalModel')"
                          title="Logical Model"
                          color="gray"
                        />
                        <br>
                        <span style="display:flex;justify-content:center">Logical Model</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-bonfire"
                          size="60"
                          @click.native="toolPanel('ConceptualModel')"
                          title="Conceptual Model"
                          color="gray"
                        />
                        <br>
                        <span style="display:flex;justify-content:center">Conceptual Model</span>
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-pulse"
                          size="60"
                          @click.native="toolPanel('ComputationalModel')"
                          title="Computational Model"
                          color="gray"
                        />
                        <br>
                        <span>Computational Model</span>
                      </div>
                    </div>
                    <h2>Non-collaborative Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline"/>
                      <span>This tool can't support collaborative functions.</span>
                    </div>
                  </TabPane>
                  <TabPane label="Special tools" name="Special">
                    <h2>Special Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline"/>
                      <span>For different solving process of geographic problem </span>
                    </div>
                    <br>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Preparation'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Analysis'"></div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Modeling'"></div>
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
        <Row style="margin-top:20px" :style="{height:sidebarHeight+6+'px'}">
          <template>
            <Col span="22" offset="1" :style="{height:sidebarHeight/5*2+'px'}">
              <div style="width:100%;height:100%;float:left;background-color:white">
                <h2
                  style="width:100%;padding:10px 10px 0 10px; display: inline-block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;max-width: 80%;"
                >{{currentModule.title}}</h2>
                <hr>
                <div style="width:100%;padding:10px">
                  <span
                    style="word-break: break-all;text-indent:2em;padding:10px"
                  >{{currentModule.description}}</span>
                </div>
              </div>
            </Col>
          </template>
          <template>
            <Col span="22" offset="1" style="margin-top:20px;">
            <Col span="11">
              <div
                style="border:1px solid lightgray;background-color:white;overflow-y:scroll"
                :style="{height:sidebarHeight/5*3 - 32 + 'px'}"
              >
              <span
                  style="height:40px;line-height:40px;margin-left:20px;font-size:1.5em;font-weight: bold"
                >Timeline</span>
                <Timeline style="padding:10px">
                  <TimelineItem v-for="(item,index) in historyRecords" :key="index">
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
            <Col span="12" offset="1">
              <div style="background-color:white" class="unique" :style="{height:sidebarHeight/5*3 - 32 + 'px'}">
                <span style="height:40px;line-height:40px;margin-left:20px;font-size:1.5em;font-weight: bold"
                >Resource</span>
                <div style="float:right;margin:4px 10px 0 0" class="popCenter">

                  <Button
                    id="upload"
                    type="default"
                    @click="uploadFileModalShow()"
                    class="uploadBtn"
                    title="upload resource"
                  >
                    <Icon type="md-cloud-upload" size="20"/>
                  </Button>
                  <Button
                    class="moreBtn"
                    type="default"
                    style="margin-left: 10px"
                    @click="toResourceList()"
                    title="more"
                  >
                    <Icon type="md-more"/>
                  </Button>
                </div>
                <div style="overflow-y:scroll;padding:0px 10px 10px 10px">
                  <Table
                    :columns="projectTableColName"
                    :data="this.projectResourceList"
                    v-show="this.projectResourceList!=[] && this.projectResourceList!='None'"
                  >
                    <template slot-scope="{ row }" slot="name">
                      <strong>{{ row.name }}</strong>
                    </template>
                    <template slot-scope="{ row, index }" slot="action">
                      <Button
                        type="success"
                        size="small"
                        style="margin-right: 5px"
                        :href="projectResourceList[index].pathURL"
                        @click="show(index)"
                        title="download"
                      >
                        <Icon type="md-download"/>
                      </Button>
                      <Button type="warning" size="small" style="margin-right: 5px" title="View">
                        <Icon type="md-eye"/>
                      </Button>
                    </template>
                  </Table>
                </div>
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
      @on-ok="delModule()"
      @on-cancel="cancel()"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal
      v-model="editModal"
      title="Update this process"
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
    <Modal
      width="600px"
      v-model="addModal"
      title="Start a new process"
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
    <Modal
      width="600px"
      v-model="copyModal"
      title="Copy this process"
      @on-ok="copyModule()"
      @on-cancel="cancel()"
    >
      <p>Do you want to activate this process?</p>
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
      title="upload file"
      @on-ok="submitFile()"
      ok-text="submit"
      cancel-text="cancel"
      width="600px"
      :mask-closable="false"
    >
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <span style="width:20%">File Type</span>
        <RadioGroup v-model="fileType" style="width:80%">
          <Radio label="image"></Radio>
          <Radio label="video"></Radio>
          <Radio label="data"></Radio>
          <Radio label="paper"></Radio>
          <Radio label="document"></Radio>
          <Radio label="model"></Radio>
          <Radio label="others"></Radio>
        </RadioGroup>
        <!-- 结束 -->
      </div>
      <br>
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <span style="width:20%">Description</span>
        <Input type="textarea" :rows="2" v-model="fileDescription"/>
      </div>
      <br>
      <input type="file" @change="getFile($event)" style="margin-left:20%">
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
        "validation",
        "Comparison"
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
      // web socket for module
      moduleSocket: null,
      timer: null,
      // 动态记录相关
      // 所有module的记录
      allRecords: [],
      historyRecords: [],
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
      },
      uploadFileModal: false,
      projectResourceList: [],
      projectTableColName: [
        {
          title: "Name",
          key: "name",
        },
        {
          title: "type",
          key: "type",
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          align: "center"
        }
      ],
      fileType: "",
      fileDescription: "",
      resourceHeight:400,
    };
  },
  created() {
    this.init();
    this.getAllModules("init");
    this.getAllResource();
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
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
    window.removeEventListener("resize", this.reSize);
    this.closeModuleSocket();
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 227;
      this.resourceHeight = this.sidebarHeight - 358;
      //通知栏的属性设置，top表示距离顶部的距离，duration表示持续的时间
      this.$Notice.config({
        top: 50,
        duration: 2
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
      this.currentModuleIndex = item;
      this.currentModule = this.moduleList[this.currentModuleIndex];
      sessionStorage.setItem("moduleId", this.currentModule.moduleId);
      sessionStorage.setItem("moduleName", this.currentModule.title);

      if (item != this.order) {
        this.closeModuleSocket();
        this.openModuleSocket(this.currentModule.moduleId);
      }
      this.order = item;

      this.olParticipants = [];
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
      if (this.currentModule.activeStatus) {
        var moduleSocketURL =
          "ws://localhost:8081/GeoProblemSolving/Module/" + moduleId;
        // var moduleSocketURL = "ws://202.195.237.252:8082/GeoProblemSolving/Module/" + moduleId;
        // var moduleSocketURL = "ws://172.21.212.7:8082/GeoProblemSolving/Module/" + moduleId;
        this.moduleSocket = new WebSocket(moduleSocketURL);
        this.moduleSocket.onopen = this.onOpen;
        this.moduleSocket.onmessage = this.onMessage;
        this.moduleSocket.onclose = this.onClose;
        this.moduleSocket.onerror = this.onError;
        this.setTimer();
      }
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

        this.allRecords.push(message);
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
            // init allRecords
            for (let i = 0; i < this.moduleList.length; i++) {
              this.allRecords = [];
            }

            let index = this.getActiveModule();
            if (state == "init") {
              this.showDetail(index);
            } else if (state == "add") {
              // this.showDetail(this.moduleList.length -1);
            } else if (state == "delete") {
              // this.showDetail(this.order - 1);
            } else if (state == "update") {
              this.showDetail(this.order);
            }
          } else if (res.data == "None") {
            this.moduleList = [];
          }
        })
        .catch(err => {});
    },
    addModule() {
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        let Module = {};
        Module["activeStatus"] = true;
        Module["subProjectId"] = this.$route.params.id;
        Module["title"] = this.moduleTitle;
        Module["description"] = this.moduleDescription;
        Module["creator"] = this.$store.getters.userId;
        Module["type"] = this.moduleType;
        if (this.moduleList.length !== 0) {
          Module["foreModuleId"] = this.moduleList[
            this.moduleList.length - 1
          ].moduleId;
        } else {
          Module["foreModuleId"] = "";
        }
        Module["nextModuleId"] = "";
        var this1 = this;
        var this2 = this;
        this.axios
          .post("/GeoProblemSolving/module/create", Module)
          .then(res => {
            if (res.data === "Fail") {
              this.$Message.info("Fail");
            } else {
              if (this1.moduleList.length > 0) {
                let updateObject = new URLSearchParams();
                updateObject.append("moduleId", this1.currentModule.moduleId);
                updateObject.append("activeStatus", false);
                this1.axios
                  .post("/GeoProblemSolving/module/update", updateObject)
                  .then(res => {
                    this2.getAllModules("update");
                  })
                  .catch(err => {
                    console.log(err.data);
                  });
              }

              this1.createModuleSuccess(Module["title"]);
              this1.moduleList.push(Module);
              this1.showDetail(this.moduleList.length - 1);
              this1.moduleTitle = "";
              this1.moduleDescription = "";
              this1.moduleType = "";
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    copyModule() {},
    activateModule() {
      var _this = this;
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        let activeModelIndex = this.getActiveModule();

        if (!this.currentModule.activeStatus) {
          let updateObject = new URLSearchParams();
          updateObject.append("moduleId", this.currentModule.moduleId);
          updateObject.append("activeStatus", true);
          this.axios
            .post("/GeoProblemSolving/module/update", updateObject)
            .then(res => {
              if (_this.moduleList[activeModelIndex].activeStatus) {
                let updateObject = new URLSearchParams();
                updateObject.append(
                  "moduleId",
                  _this.moduleList[activeModelIndex].moduleId
                );
                updateObject.append("activeStatus", false);
                this.axios
                  .post("/GeoProblemSolving/module/update", updateObject)
                  .then(res => {
                    _this.getAllModules("update");
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
    uploadFileModalShow() {
      this.uploadFileModal = true;
    },
    toResourceList() {
      this.$router.push({ path: "/resourceList" });
    },
    addUploadEvent(scopeId) {
      let form = {};
      let description =
        this.$store.getters.userName +
        " uploaded a " +
        this.fileType +
        " file in " +
        " project called " +
        this.currentProjectDetail.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      this.axios
        .post(
          "/GeoProblemSolving/history/save?",
          "description=" +
            description +
            "&scopeId=" +
            scopeId +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getResourceList() {
      this.axios
        .get(url, {
          params: {
            id: paramId
          }
        })
        .then(function(response) {})
        .catch(function(error) {});
    },
    getFile(event) {
      this.file = event.target.files[0];
    },
    submitFile() {
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("description", this.fileDescription);
      formData.append("type", this.fileType);
      formData.append("uploaderId", this.$store.getters.userId);
      // currentModule.title;
      // currentModule.moduleId
      // 添加字段属于那个项目
      formData.append("belong", this.currentModule.title);

      let scopeObject = {
        projectId: window.sessionStorage.getItem("projectId"),
        subprojectId: window.sessionStorage.getItem("subProjectId"),
        moduleId: this.currentModule.moduleId,
      };
      formData.append("scope", JSON.stringify(scopeObject));
      //这里还要添加其他的字段
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data != "Size over" && res.data.length > 0) {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File uploaded successfully",
              duration: 2
            });
            //这里重新获取一次该项目下的全部资源
            this.addUploadEvent(this.currentProjectDetail.projectId);
            this.getAllResource();
            // 创建一个函数根据pid去后台查询该项目下的资源
          }
          // console.log(res.data);
        })
        .catch(err => {});
    },
    //获取全部资源的方法
    getAllResource() {
      // url是请求的网址
      // 查询的形式是key-value格式
      // this.axios.get("/GeoProblemSolving/resource/inquiry",obj)
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.moduleId" +
            "&value=" +
            window.sessionStorage.getItem("moduleId")
        )
        .then(res => {
          // 写渲染函数，取到所有资源
          if (res.data !== "None") {
            this.$set(this, "projectResourceList", res.data);
            console.log(this.projectResourceList);
          } else {
            this.projectResourceList = [];
          }
          // 渲染函数，将列表展现出来，下载
          // this.showProjectResource(this.projectResourceList);
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
    //更新模块的函数
    updateModule() {
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        let updateObject = new URLSearchParams();
        updateObject.append("moduleId", this.currentModule.moduleId);
        updateObject.append("title", this.updateModuleTitle);
        updateObject.append("description", this.updateModuleDescription);
        updateObject.append("type", this.updateModuleType);
        updateObject.append("creater", this.$store.getters.userId);
        this.axios
          .post("/GeoProblemSolving/module/update", updateObject)
          .then(res => {
            this.getAllModules("update");
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {

    },
    show(index) {
      window.open(this.projectResourceList[index].pathURL);
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
    useModelingTool() {
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
      if (type == "map") {
        toolURL =
          '<iframe src="http://172.21.212.7:8082/GeoProblemSolving/map" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chatroom") {
        toolURL =
          '<iframe src="http://172.21.212.7:8082/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
      } else if (type == "draw") {
        toolURL =
          '<iframe src="http://172.21.212.7:8082/GeoProblemSolving/draw" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chart") {
        toolURL =
          '<iframe src="http://172.21.212.7:8082/GeoProblemSolving/charts" style="width: 100%;height:100%"></iframe>';
      } else if (type == "chat") {
        toolURL =
          '<iframe src="http://172.21.212.7:8082/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
      } else if (type == "graphEditor") {
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/GraphEditor/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }else if(type == "3DmodelViewer"){
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }
      else if(type == "LogicalModel"){
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/LogicalModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }
      else if(type == "ConceptualModel"){
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/ConceptualModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }
      else if(type == "ComputionalModel"){
        toolURL =
          '<iframe src="/GeoProblemSolving/Collaborative/ComputionalModel/index.html' +
          "?groupID=" +
          this.currentModule.moduleId +
          '" style="width: 100%;height:100%"></iframe>';
      }
      // 3d
      // computational
      // conceptual
      // logical

      var panel = jsPanel.create({
        theme: "primary",
        headerTitle: "Tools",
        contentSize: "1000 600",
        content: toolURL,
        disableOnMaximized: true,
        callback: function() {
          // this.content.style.padding = "20px";
        }
      });
      panel.resizeit("disable");
      $(".jsPanel-content").css("font-size", "0");
    }
  }
};
</script>
