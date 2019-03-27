<style scoped>
.main {
  background-color: rgb(219, 213, 213);
  width: 100%;
}
.projectTitle {
  display: flex;
  justify-content: center;
  align-items: cenetr;
  height: auto;
}
.detail {
  height: auto;
}
.detail_image {
  display: flex;
  align-items: center;
  justify-content: center;
  height:300px;
}
.detail_image img {
  max-width: 100%;
  max-height: 300px;
}
.detail_description {
  padding: 20px;
  max-height: auto;
}
.projectDescription {
  word-break: break-all;
  text-indent: 2em;
  font-size: 16px;
  overflow-y: auto;
  word-break: break-all;
  padding: 0 10px;
}
.projectEditPanel {
  display: block;
  align-items: center;
  text-align: center;
  height: 60px;
  padding-right: 20px;
}
.projectEditPanel button {
  margin-top: 5px;
  height: 40px;
  float: right;
}
.projectMembersPanel {
  display: flex;
  /* justify-content: center; */
  align-items: center;
  padding: 20px;
  height: 100px;
  border: 1px solid gray;
  /* background-color: lightblue; */
}
.memberPanel {
  padding: 20px;
}
.subprojectPanel {
  height: auto;
  display: flex;
  align-items: center;
}
.subProjectListStyle {
  height: auto;
  align-items: center;
  width: 90%;
}
.subProjectTitle:hover {
  cursor: pointer;
}
.subProjectDescription {
  text-indent: 25px;
  min-height: 60px;
}
.subProjectCreate button {
  height: 40px;
  display: flex;
  align-items: center;
  line-height: 20px;
  font-size: 20px;
}
.whitespace {
  height: 20px;
}
.createSubProjectPanel {
  display: flex;
}
.createSubProjectPanelInput {
  display: flex;
  align-items: center;
  justify-content: center;
}
.subProjectStyle {
  height: auto;
  margin: 10px;
}
/* 邀请其他人的邮件表单样式 */
.form_style {
  display: flex;
  align-items: center;
  /* justify-content: center; */
}
.form_style span {
  min-width: 20%;
  text-align: center;
}
.emailOperate {
  display: flex;
  padding: 0 10px;
  justify-content: center;
}
.emailOperate button {
  /* padding:0 10px; */
  margin-left: 10px;
  margin-right: 10px;
}
/* 按钮样式 */
.subProjectBtn:hover,
.inviteBtn:hover,
.uploadBtn:hover {
  background-color: #47cb89;
  color: white;
}
.deleteBtn:hover {
  background-color: #ed4014;
  color: white;
}
/* .inviteBtn:hover {
  background-color: #47cb89;
  color: white;
} */
.subProjectBtn,
.uploadBtn,
.inviteBtn {
  color: black;
}
.popCenter {
  margin: 10px auto;
  overflow: hidden;
}
/* 编辑项目用到的样式 */
.editStyle {
  display: flex;
  align-items: center;
  margin-top: 5px;
  /* justify-content: center; */
}
.editStyle span {
  width: 20%;
  text-align: left;
}

/* 按钮样式统一 */
.authorBtn:hover {
  background-color: #57a3f3;
  color: white;
}
.editBtn:hover {
  background-color: #19be6b;
  color: white;
}
.deleteBtn:hover {
  background-color: #ed4014;
  color: white;
}
.moreBtn:hover{
  background-color: #57a3f3;
  color: white;
}
/* 按钮样式统一结束 */
</style>
<template>
  <div class="main">
    <Row>
      <Col span="22" offset="1">
        <div class="whitespace"></div>
        <div class="whitespace"></div>
        <div class="detail" style="padding: 0 0 20px 0">
          <div class="detail_description">
            <Card>
              <p
                slot="title"
                style="font-size:25px;height:40px;line-height:40px;max-width:50%"
              >{{currentProjectDetail["title"]}}</p>
              <div slot="extra" style="height:40px;line-height:40px;display:flex;width:50%">
                <Button
                  type="default"
                  icon="md-brush"
                  @click="editModalShow(currentProjectDetail['projectId'])"
                  v-show="judgeIsManager(projectManager.userId)"
                ></Button>
                <Button
                  class="deleteBtn"
                  type="default"
                  icon="md-close"
                  title="remove"
                  style="margin-left:20px"
                  @click="deleteProjectShow()"
                  v-show="judgeIsManager(projectManager.userId)"
                ></Button>
              </div>
              <Row>
                <Col :xs="12" :sm="10" :md="9" :lg="8">
                  <div class="detail_image">
                    <img :src="currentProjectDetail.picture" v-if="currentProjectDetail.picture!=''&&currentProjectDetail.picture!='undefined'">
                    <avatar
                      :username="currentProjectDetail.title"
                      :size="300"
                      :title="currentProjectDetail.title"
                      :rounded="false"
                      v-else>
                    </avatar>
                  </div>
                </Col>
                <Col :xs="12" :sm="14" :md="15" :lg="16">
                  <div style="display:flex;height:250px;">
                    <span
                      class="projectDescription"
                      v-if="currentProjectDetail"
                    >{{currentProjectDetail.introduction}}</span>
                  </div>
                </Col>
              </Row>
            </Card>
          </div>
        </div>
        <div class="memberPanel">
          <Card :bordered="false">
            <p slot="title" style="font-size:25px;height:40px;line-height:40px;">Members</p>
            <div slot="extra" style="height:40px" class="popCenter">
              <Poptip trigger="hover" content="Invite other members" placement="right">
                <Button class="inviteBtn" v-show="this.currentProjectDetail.isManager" @click="inviteModalShow()">
                  <Icon type="md-person-add" size="20"/>
                </Button>
              </Poptip>
            </div>
            <div style="display:flex;align-items:center">
              <Button
                type="success"
                style="margin-left:0.5%;margin-right:0.5%"
                @click="gotoPersonalPage(projectManager.userId)"
              >{{this.projectManager.userName}}</Button>
              <Button
                v-for="member in currentProjectDetail.members"
                type="primary"
                :key="member.index"
                style="margin-left:0.5%;margin-right:0.5%"
                @click="gotoPersonalPage(member.userId)"
              >{{member.userName}}</Button>
            </div>
          </Card>
        </div>

        <!-- 关于邀请其他成员的modal,禁用点击其他空白处误关模态框 -->
        <Modal
          v-model="inviteModal"
          @on-ok="invite()"
          @on-cancel="cancel()"
          ok-text="Assure"
          cancel-text="Cancel"
          title="Invite others join in the Project"
          :mask-closable="false"
          width="800"
        >
          <!-- <br> -->
          <div class="form_style">
            <span>Reciver</span>
            <!-- <Tag v-for="item in count" :key="item" :name="item" closable @on-close="handleClose2">标签{{ item + 1 }}</Tag> -->
            <Input v-model="inputEmail" style="width:200px"/>
            <Button
              icon="ios-add"
              type="dashed"
              @click="addEmail(inputEmail)"
              style="margin-left:20px"
            >Add</Button>
          </div>
          <div style="margin-left:20%;width:80%">
            <Tag
              v-for="(item,index) in inviteEmailList"
              :key="index"
              color="success"
              @on-close="delEmail(index)"
              closable
            >{{item}}</Tag>
          </div>
          <br>
          <div class="form_style">
            <span>Title</span>
            <Input style="width:75%" v-model="emailTitle"/>
          </div>
          <br>
          <div class="form_style">
            <span>Content</span>
            <Input
              type="textarea"
              :rows="4"
              style="width:75%"
              :placeholder="this.emailFormat + this.currentProjectDetail.title + ', you can copy the projectId ' + this.currentProjectDetail.projectId +'and apply for join in it'"
              v-model="emailContent"
            />
          </div>
          <br>
          <div style="display:flex" class="emailOperate">
            <Button @click="copyDefault()" type="success">Use default</Button>
            <!-- <Button type="primary" v-clipboard="copydata">Copy Project ID</Button> -->
            <Button
              type="primary"
              v-clipboard="copyProjectId"
              @success="handleSuccess('id')"
              @error="handleError('id')"
            >Copy Project ID</Button>
            <Button
              type="default"
              v-clipboard="copyProjectTitle"
              @success="handleSuccess('title')"
              @error="handleError('title')"
            >Copy Project Name</Button>
          </div>
        </Modal>
        <div class="whitespace"></div>
        <div style="padding: 20px">
          <Card :bordered="false">
            <p slot="title" style="font-size:25px;height:40px;line-height:40px">Sub Projects</p>
            <div slot="extra" style="height:40px" class="popCenter">
              <Poptip trigger="hover" content="add new subproject" placement="right">
                <Button
                  class="subProjectBtn"
                  @click="subProjectModal = true"
                  v-show="this.currentProjectDetail.isManager||this.currentProjectDetail.isMember"
                >
                  <Icon type="md-add" size="20"/>
                </Button>
              </Poptip>
              <Modal
                v-model="subProjectModal"
                ok-text="create"
                cancel-text="cancel"
                @on-ok="createSubProject()"
                @on-cancel="cancel()"
                title="Create sub project"
                width="800px"
                :mask-closable="false"
              >
                <div>
                  <div class="createSubProjectPanelInput">
                    <span style="width:20%;text-align:center">Title</span>
                    <Input
                      v-model="subProjectTitle"
                      placeholder="Sub project title"
                      style="width: 800px;"
                    />
                  </div>
                  <br>
                  <div class="createSubProjectPanelInput">
                    <span style="width:20%;text-align:center">Description</span>
                    <Input
                      v-model="subProjectDescription"
                      placeholder="Sub project description"
                      style="width: 800px;"
                      :rows="4"
                      type="textarea"
                    />
                  </div>
                </div>
              </Modal>
            </div>
            <div class="subprojectPanel">
              <div class="subProjectListStyle">
                <div class="whitespace"></div>
                <div v-for="(subProject,index) in subProjectList" :key="subProject.index" style>
                  <Col :lg="12" :md="24" :sm="24" :xs="24">
                    <Card class="subProjectStyle">
                      <Button
                        type="default"
                        slot="extra"
                        class="authorBtn"
                        style="margin:-5px 5px 0 5px"
                        @click="handOverSubProjectShow(index)"
                        v-show="subProject.isManager"
                        icon="md-happy"
                        title="Authorize"
                      ></Button>
                      <Button
                        type="default"
                        class="editBtn"
                        slot="extra"
                        style="margin:-5px 5px 0 5px"
                        @click="editSubProjectShow(index)"
                        v-show="subProject.isManager"
                        icon="ios-brush"
                        title="edit"
                      ></Button>
                      <Button
                        class="deleteBtn"
                        type="default"
                        slot="extra"
                        style="margin:-5px 5px 0 5px"
                        @click="deleteSubProjectShow(index)"
                        v-show="subProject.isManager"
                        icon="md-close"
                        title="remove"
                      ></Button>
                      <p
                        slot="title"
                        @click="goWorkspace(subProject.subProjectId)"
                        class="subProjectTitle"
                      >{{subProject["title"]}}</p>
                      <p class="subProjectDescription">{{subProject["description"]}}</p>
                      <!-- <hr> -->
                      <br>
                      <div>
                        <span
                          style="float:left;color:white;background-color:#2d8cf0;padding:2.5px;min-width:100px;text-align:center"
                        >manager</span>
                        <span style="float:right;padding:2.5px">{{subProject.managerName}}</span>
                      </div>
                      <br>
                      <div class="whitespace"></div>
                      <div>
                        <span
                          style="float:left;;color:white;background-color:#2d8cf0;padding:2.5px;min-width:100px;text-align:center"
                        >createtime</span>
                        <span
                          style="float:right;padding:2.5px"
                        >{{subProject["createTime"].split(" ")[0]}}</span>
                      </div>
                      <div class="whitespace"></div>
                    </Card>
                  </Col>
                </div>
              </div>
              <Modal
                v-model="handOverSubProjectModal"
                title="Appoint new manager"
                ok-text="ok"
                cancel-text="cancel"
                @on-ok="handOverSubProject()"
                @on-cancel="cancel"
                width="500px"
                :mask-closable="false"
              >
                <div style="height:100px;background:azure">
                  <RadioGroup v-model="newManagerId">
                    <Radio
                      v-for="(member,index) in subjectMembers"
                      :key="member.index"
                      :label="member.userId"
                    >
                      <span>{{member.userName}}</span>
                    </Radio>
                  </RadioGroup>
                </div>
              </Modal>
              <Modal
                v-model="editSubProjectModal"
                ok-text="submit"
                cancel-text="cancel"
                @on-ok="editSubProject()"
                @on-cancel="cancel()"
                title="edit sub project info"
                width="800px"
                :mask-closable="false"
              >
                <div>
                  <div class="createSubProjectPanelInput">
                    <span style="width:20%;text-align:center">Title</span>
                    <Input
                      v-model="subProjectTitleEdit"
                      style="width: 800px;"
                      :placeholder="subProjectTitleEdit"
                    />
                  </div>
                  <br>
                  <div class="createSubProjectPanelInput">
                    <span style="width:20%;text-align:center">Description</span>
                    <Input
                      v-model="subProjectDescriptionEdit"
                      :placeholder="subProjectDescriptionEdit"
                      style="width: 800px;"
                      :rows="4"
                      type="textarea"
                    />
                  </div>
                </div>
              </Modal>
              <Modal
                v-model="deleteSubProjectModal"
                title="Delete sub project"
                ok-text="assure"
                cancel-text="cancel"
                @on-ok="deleteSubProject()"
                @on-cancel="cancel"
                width="800px"
                :mask-closable="false"
              >
                <p>Once the deletion is confirmed, all module and resource information under the subsystem will be deleted. Please choose carefully.</p>
              </Modal>
            </div>
          </Card>
        </div>
        <div class="whitespace"></div>
        <div class="resourcePanel" style="padding:20px">
          <Card>
            <p slot="title" style="font-size:25px;height:40px;line-height:40px;">Resource</p>
            <div slot="extra" style="display:flex;align-items:center;height:40px" class="popCenter">
                <Button id="upload" type="default" @click="uploadFileModalShow()" class="uploadBtn" title="upload resource">
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
            <div style="height:300px;overflow-y:scroll">
              <Table
                :columns="projectTableColName"
                :data="this.projectResourceList"
                v-show="this.projectResourceList!=[]&&this.projectResourceList!='None'"
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
                  <Button
                    type="warning"
                    size="small"
                    style="margin-right: 5px"
                    @click=""
                    title="View"
                  >
                  <Icon type="md-eye" />
                  </Button>
                </template>
              </Table>
            </div>
          </Card>
        </div>
        <!-- 上传文件按钮的模态框 -->
        <Modal
          v-model="uploadFileModal"
          title="upload file"
          @on-ok="submitFile()"
          @on-cancel="cancel()"
          ok-text="submit"
          cancel-text="cancel"
          width="600px"
          :mask-closable="false"
        >
          <div style="display:flex;text-align:center;align-items:center;justify-content:center">
            <!-- 这里定义上传的几种资源类型供用户选择 -->
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
        <br>
      </Col>
      <Modal
        v-model="removeProjectModal"
        title="Delete warning "
        @on-ok="deleteProject"
        @on-cancel="cancel"
        ok-text="ok"
        cancel-text = "cancel"
        >
        <p>Do you want to delete this project? Please think twice before you choose.</p>
    </Modal>
      <!-- removeProjectModal -->
    </Row>
    <Modal
      v-model="editProjectModal"
      title="Edit Project"
      @on-ok="editProjectSubmit()"
      @on-cancel="cancel"
      :mask-closable="false"
      width="900px"
    >
      <div style="flex">
        <!-- <span>Category</span> -->
        <div class="editStyle">
          <span>Category</span>
          <RadioGroup style="margin-left:5%;width:100%" v-model="editType">
            <Radio label="Society"></Radio>
            <Radio label="Atmosphere"></Radio>
            <Radio label="Ecology"></Radio>
            <Radio label="Soil"></Radio>
            <Radio label="Water"></Radio>
            <Radio label="Others"></Radio>
          </RadioGroup>
        </div>
        <div class="editStyle">
          <span>Title</span>
          <Input
            v-model="editTitle"
            placeholder="Enter something..."
            style="margin-left:5%;width:100%"
          />
        </div>
        <div class="editStyle">
          <span>Description</span>
          <Input
            v-model="editDescription"
            placeholder="Enter something..."
            style="margin-left:5%;width:100%"
          />
        </div>
        <div class="editStyle">
          <span>Introduction</span>
          <Input
            v-model="editIntroduction"
            type="textarea"
            placeholder="Enter something..."
            style="margin-left:5%;width:100%"
            :rows="4"
          />
        </div>
        <div class="editStyle">
          <span>Tag</span>
          <Input
            v-model="inputTag"
            placeholder="Enter some tag to introduce the project"
            style="margin-left:0.5%;width: 200px"
            @keyup.enter.native="addTag(inputTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            style="margin-left:2.5%"
            @click="addTag(inputTag)"
          >Add Tag</Button>
          <!-- <div style="margin-left:5%">
            <Tag color="primary" @on-close="deleteTag(index)" v-show="editTags!=''">{{item}}</Tag>
          </div>-->
        </div>
        <div style="width:80%;margin-left:20%">
          <Tag
            color="primary"
            v-for="(tag,index) in editTags"
            :key="index"
            closable
            @on-close="deleteTag(index)"
          >{{tag}}</Tag>
        </div>
        <div class="editStyle">
          <span>Privacy</span>
          <RadioGroup style="margin-left:0.5%" v-model="editPrivacy">
            <Radio
              label="Public"
              title="Other users can find the group and see who has membership."
            ></Radio>
            <Radio
              label="Discover"
              title="Other users can find this group, but membership information is hidden."
            ></Radio>
            <Radio label="Private" title="Other users can not find this group."></Radio>
          </RadioGroup>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import VueClipboards from "./../../utils/VueClipboards";
export default {
  components: {
    Avatar
  },
  data() {
    return {
      //编辑项目的字段
      /*编辑项目专用的字段*/
      editType: "",
      editTags: "",
      inputTag: "",
      editDescription: "",
      editTitle: "",
      editIntroduction: "",
      editPrivacy: "",
      editProjectId: "",
      /* 编辑项目字段结束*/
      projectManager: {},
      //确定用户是否有更新项目的权限，控制是否显示编辑的按钮，只有创建者才有权对项目进行编辑
      copyProjectId: "",
      copyProjectTitle: "",
      copyProjectIdStatus: false,
      copyProjectTitleStatus: false,
      currentProjectDetail: {
        // members: [],
        // introduction: "",
        // projectId: ""
      },
      //确定用户是否有更新项目的权限，控制是否显示编辑的按钮，只有创建者才有权对项目进行编辑
      projectEditAble: false,
      //移交权限给新的管理者
      handOverSubProjectModal: false,
      newManagerId: "",
      subjectMembers: [],
      //编辑项目的按钮
      editinfoModal: false,
      //子项目的列表
      subProjectList: [],
      //创建子项目的模态框
      subProjectModal: false,
      subProjectTitle: "",
      subProjectDescription: "",
      subProjectTitleEdit: "",
      subProjectDescriptionEdit: "",
      //编辑子项目按钮的模态框
      editSubProjectModal: false,
      //删除子项目按钮的模态框
      deleteSubProjectModal: false,
      editSubProjectindex: 0,
      //邀请的modal
      inviteModal: false,
      inputEmail: "",
      inviteEmailList: [],
      //邮件格式
      emailTitle: "",
      emailContent: "",
      emailFormat:
        "hello,it's my pleasure to invite you join in the project called ",
      //上传文件相关的
      uploadFileModal: false,
      file: "",
      fileDescription: "",
      fileType: "",
      //获取的资源的列表
      projectResourceList: [],
      projectTableColName: [
        {
          title: "Name",
          key: "name",
        },
        {
          title: "Description",
          key: "description",
        },
        {
          title: "type",
          key: "type",
          sortable: true,
        },
        {
          title: "uploadTime",
          key: "uploadTime",
          sortable: true,
        },
        {
          title: "Action",
          slot: "action",
          width: 250,
          align: "center"
        }
      ],
      // 关于控制项目编辑的模态框
      editProjectModal: false,
      removeProjectModal :false,
    };
  },
  created(){
    this.getAllSubProject();
    this.getAllResource();
  },
  mounted() {
    this.$Message.config({
      top: 150,
      duration: 3
    });
    this.getProjectDetail();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (!(vm.currentProjectDetail.isManager || vm.currentProjectDetail.isMember)) {
          alert("No access");
          next("/projectlist");
          // vm.$router.go(-1);
        }
      }
    });
  },
  methods: {
    getProjectDetail() {
      let projectInfo=this.$store.getters.project;
      let pid = this.$route.params.id;
      var that = this;
      if(JSON.stringify(projectInfo)!="{}" && projectInfo.projectId==pid){
        that.currentProjectDetail=projectInfo;
        this.updateRelatedInfo();
      }
      else{
        let queryObject = { key: "projectId", value: pid };
        try {
          $.ajax({
            url:
              "/GeoProblemSolving/project/inquiry" +
              "?key=" +
              queryObject["key"] +
              "&value=" +
              queryObject["value"],
            type: "GET",
            async: false,
            success: function(data) {
              if (data === "None") {
                that.currentProjectDetail = {
                  members: [],
                  introduction: "",
                  projectId: ""
                };
              } else {
                let projectInfo = data[0];
                projectInfo.isManager=that.managerIdentity(projectInfo.managerId);
                projectInfo.isMember=that.memberIdentity(projectInfo.members);
                that.currentProjectDetail = projectInfo;
                that.$store.commit("setProjectInfo", projectInfo);
                that.updateRelatedInfo();
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
    updateRelatedInfo(){
      var that = this;
      // 邀请他人加入项目的form的复制项目id与项目名的按钮
      that.copyProjectId = that.currentProjectDetail.projectId;
      that.copyProjectTitle = that.currentProjectDetail.title;
      that.projectManager.userId = that.currentProjectDetail.managerId;
      that.projectManager.userName = that.currentProjectDetail.managerName;
      sessionStorage.setItem(
        "projectId",
        that.currentProjectDetail.projectId
      );
      //将tag进行分割
      that.currentProjectDetail.tag = that.currentProjectDetail.tag.split(
        ","
      );
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        return true;
      } else {
        return false;
      }
    },
    memberIdentity(members) {
      let isMember;
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          isMember = true;
          break;
        }
      }
      if (isMember) {
        return true;
      } else {
        return false;
      }
    },
    // 修改项目的按钮
    submit() {
      // 提交要修改的信息即可
      this.$Message.info("ok");
    },
    cancel() {
      this.$Message.info("cancel");
    },
    //前往工作空间
    goWorkspace(data) {
      let isManager, isMember;
      for (let i = 0; i < this.subProjectList.length; i++) {
        if (this.subProjectList[i]["subProjectId"] === data) {
          let subProjectInfo=this.subProjectList[i];
          isManager = subProjectInfo["isManager"];
          isMember = subProjectInfo["isMember"];
          this.$store.commit("setSubProjectInfo", subProjectInfo);
          break;
        }
      }
      if (this.$store.getters.userState) {
        if (isManager || isMember) {
          this.$router.push({ path: `${data}/workspace` });
        } else {
          this.$Message.error("You have no property to access it");
          // alert("No access.");
        }
      } else {
        // showMessage();
        this.$router.push({ path: "/login" });
      }
    },
    //创建子项目
    createSubProject() {
      let SubProject = {};
      SubProject["description"] = this.subProjectDescription;
      SubProject["title"] = this.subProjectTitle;
      SubProject["projectId"] = this.currentProjectDetail.projectId;
      SubProject["managerId"] = this.$store.getters.userId;
      console.log(SubProject);
      this.axios
        .post("/GeoProblemSolving/subProject/create", SubProject)
        .then(res => {
          if (res.data != "Fail") {
            this.$Message.info("create success");
            // console.log(res.data);
            this.subProjectTitle = "";
            this.subProjectDescription = "";
            this.getAllSubProject();
          } else {
            this.$Message.info("fail");
          }
        })
        .catch(err => {});
    },
    inviteModalShow() {
      this.inviteModal = true;
    },
    handOverSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.subProjectMembers = this.subProjectList[index].members;
      // if it is the member of the sub-project
      this.isSubMember = this.memberIdentity(this.subProjectMembers);
      this.handOverSubProjectModal = true;
    },
    addEmail(email) {
      this.inviteEmailList.push(email);
      this.inputEmail = "";
      console.log("邀请的成员邮箱数组如下:" + this.inviteEmailList);
    },
    delEmail(index) {
      this.inviteEmailList.splice(index, 1);
    },
    invite() {
      var emailFormBody = {};
      emailFormBody["recipient"] = this.inviteEmailList.toString();
      emailFormBody["mailTitle"] = this.emailTitle;
      emailFormBody["mailContent"] = this.emailContent;
      console.log(emailFormBody);
      this.axios
        .post("/GeoProblemSolving/email/send", emailFormBody)
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    handOverSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/manager?" +
            "subProjectId=" +
            this.subProjectList[this.editSubProjectindex].subProjectId +
            "&userId=" +
            this.newManagerId
        )
        .then(res => {
          console.log(res.data);
          this.getAllSubProject();
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    editSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.editSubProjectModal = true;
      this.subProjectTitleEdit = this.subProjectList[index].title;
      this.subProjectDescriptionEdit = this.subProjectList[index].description;
      // console.log(this.subProjectList[editSubProjectindex].projectId);
    },
    editSubProject() {
      this.editSubProjectModal = false;
      let obj = new URLSearchParams();
      obj.append(
        "subProjectId",
        this.subProjectList[this.editSubProjectindex].subProjectId
      );
      obj.append("title", this.subProjectTitleEdit);
      obj.append("description", this.subProjectDescriptionEdit);
      // console.log(obj.get("title"));
      this.axios
        .post("/GeoProblemSolving/subProject/update", obj)
        .then(res => {
          console.log(res.data);
          this.getAllSubProject();
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    deleteSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.deleteSubProjectModal = true;
    },
    deleteSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/delete?" +
            "subProjectId=" +
            this.subProjectList[this.editSubProjectindex].subProjectId
        )
        .then(res => {
          console.log(res.data);
          this.getAllSubProject();
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //获取所有子项目
    getAllSubProject() {
      let pid = this.$route.params.id;
      let queryObject = { key: "projectId", value: pid };
      var that = this;
      this.axios
        .get(
          "/GeoProblemSolving/subProject/inquiry" +
            "?key=" +
            queryObject["key"] +
            "&value=" +
            queryObject["value"]
        )
        .then(res => {
          if (res.data === "None") {
            console.log(res.data);
          } else {
            //改变this的指向，此时this需要赋值给其他变量
            that.subProjectList = res.data;
            that.identity(that.subProjectList);
            that.cutString(that.subProjectList, 200);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    identity(list) {
      for (var i = 0; i < list.length; i++) {
        list[i]["isManager"] = this.managerIdentity(list[i]["managerId"]);
        list[i]["isMember"] = this.memberIdentity(list[i]["members"]);
      }
      this.$set(this,"subProjectList",list);
    },
    showDetail(data) {
      alert(data);
    },
    copyDefault() {
      this.emailContent =
        this.emailFormat +
        this.currentProjectDetail.title +
        ", you can copy the projectId " +
        this.currentProjectDetail.projectId +
        "and apply for join in it";
    },
    //上传文件的模态框打开
    uploadFileModalShow() {
      this.uploadFileModal = true;
    },
    getFile(event) {
      this.file = event.target.files[0];
      // console.log(this.file);
    },
    //上传文件
    submitFile() {
      // event.preventDefault();
      //取消默认行为
      //创建 formData 对象
      let formData = new FormData();
      // 向 formData 对象中添加文件
      formData.append("file", this.file);
      formData.append("description", this.fileDescription);
      formData.append("type", this.fileType);
      formData.append("uploaderId", this.$store.getters.userId);
      // 添加字段属于那个项目
      formData.append("belong",this.currentProjectDetail.title);
      let scopeObject = {
        projectId:this.currentProjectDetail.projectId,
        subprojectId:"",
        moduleId:"",
      };
      formData.append("scope",JSON.stringify(scopeObject));
      //这里还要添加其他的字段
      // console.log(formData.get("file"));
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data == "Success") {
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
      //查询的形式是key-value格式
      // this.axios.get("/GeoProblemSolving/resource/inquiry",obj)
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.projectId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          //写渲染函数，取到所有资源
          if (res.data !== "None") {
            this.$set(this,"projectResourceList",res.data);
          } else {
            this.projectResourceList = [];
          }
          //渲染函数，将列表展现出来，下载
          // this.showProjectResource(this.projectResourceList);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    show(index) {
      window.open(this.projectResourceList[index].pathURL);
    },
    handleSuccess(data) {
      if (data == "id") {
        this.copyProjectIdStatus = true;
        // alert("success");
      } else if (data == "title") {
        this.copyProjectTitleStatus = true;
      }
      this.$Notice.success({
        title: "Notification title",
        desc: `Copy project ${data} success`,
        duration: 1
      });
    },
    handleError(data) {
      if (data == "id") {
        this.copyProjectIdStatus = false;
      } else if (data == "title") {
        this.copyProjectTitleStatus = false;
      }
      this.$Notice.error({
        title: "Notification title",
        desc: `Copy project ${data} fail`,
        duration: 1
      });
    },
    cutString(data, len) {
      // console.table(data);
      for (var i = 0; i < data.length; i++) {
        data[i].description = data[i].description.substring(0, len) + "...";
      }
      return data;
    },
    gotoPersonalPage(id) {
      console.log({ id });
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    editModalShow(id) {
      // this.
      this.editProjectModal = true;
      let editProjectInfo = this.currentProjectDetail;
      console.log(typeof editProjectInfo["tag"]);
      this.editTitle = editProjectInfo.title;
      this.editIntroduction = editProjectInfo.introduction;
      this.editDescription = editProjectInfo.description;
      this.editType = editProjectInfo.category;
      this.editTags = editProjectInfo["tag"];
      console.log("权限是：" + editProjectInfo.privacy);
      this.editPrivacy = editProjectInfo.privacy;
      this.editProjectId = editProjectInfo.projectId;
    },
    editProjectSubmit() {
      // 将项目变更的信息进行提交
      let projectEditForm = new URLSearchParams();
      //做一个判断
      projectEditForm.append("title", this.editTitle);
      projectEditForm.append("category", this.editType);
      projectEditForm.append("introduction", this.editIntroduction);
      projectEditForm.append("description", this.editDescription);
      projectEditForm.append("tag", this.editTags);
      projectEditForm.append("privacy", this.editPrivacy);
      projectEditForm.append("projectId", this.editProjectId);
      projectEditForm.append("managerId", this.$store.getters.userId);
      this.axios
        .post("/GeoProblemSolving/project/update ", projectEditForm)
        .then(res => {
          // console.log(res.data);
          // alert(res.data);
          this.getProjectDetail();
          // this.getManagerProjectList();
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    // 判断项目详情页面是否具备编辑权限，根据userId与projectId来比较
    judgeIsManager(projectManagerId) {
      if (projectManagerId === this.$store.getters.userId) {
        return true;
      } else {
        return false;
      }
    },
    // 编辑项目的添加tag标签
    addTag(tag) {
      this.editTags.push(tag);
      this.inputTag = "";
      // console.log("增添后的数组是：" + this.editTags);
    },
    // 删除指定的标签
    deleteTag(index) {
      this.editTags.splice(index, 1);
    },
    toResourceList(){
      this.$router.push({path:'/resourceList'})
    },
    addUploadEvent(scopeId){
      let form = {};
      let description = this.$store.getters.userName + ' uploaded a ' + this.fileType + ' file in ' + ' project called ' + this.currentProjectDetail.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      this.axios.post("/GeoProblemSolving/history/save?", "description="+ description + "&scopeId=" + scopeId + "&userId=" + this.$store.getters.userId)
          .then(res=> {
            console.log(res.data);
          })
          .catch(err=> {
            console.log(err.data);
          })
    },
    getResourceList(){
      this.axios.get(url, {
        params: {
          id:paramId
        }
      })
      .then(function (response) {
      })
      .catch(function (error) {
      })
    },
    removeProjectModalShow(){
      this.removeProjectModal = true;
    },
    //删除项目的函数
    deleteProjectShow(){
      this.removeProjectModal=true;
    },
    deleteProject() {
      // let selectProjectId = this.userManagerProjectList[
      //   this.DelelteProjectIndex
      // ].projectId;
      console.log(this.currentProjectDetail.projectId);
      this.axios
        .get(
          "/GeoProblemSolving/project/delete?" + "projectId=" + this.currentProjectDetail.projectId
        )
        .then(res => {
          if (res.data != "") {
            this.$Notice.success({
                    title: 'Notification title',
                    desc: "Delete successfully"
                });
            this.$router.push({name:"Project"});
          }
        })
        .catch(err => {});
    },
  }
};
</script>
