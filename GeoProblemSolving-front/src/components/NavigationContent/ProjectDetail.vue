<style scoped>
.projectTitle {
  display: flex;
  justify-content: center;
  align-items: cenetr;
  height: auto;
}
.detail {
  height: auto;
  display: flex;
}
.detail_image img {
  width: 100%;
}
.detail_description {
  padding: 0 20px;
  min-width: 60%;
}
.detail_description p {
  padding: 0 20px 0 20px;
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
.subprojectPanel {
  min-height: 200px;
  height: auto;
  /* background-color:lightblue; */
  border: 1px solid gray;
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
.subProjectCreate {
  height: 100px;
  display: flex;
  align-items: center;
  padding: 0 20px 0 20px;
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
</style>
<template>
  <div>
    <Row>
      <Col span="22" offset="1">
        <div class="whitespace"></div>
        <!-- <Button
          type="primary"
          style="float:right;height:60px;margin-right:40px;line-height:30px;font-size:30px;"
          @click="goWorkspace(subProjectList[0].subProjectId)"
          icon="md-home"
          title="workspace"
        ></Button> -->
        <div class="projectTitle">
          <h1
            style="text-align:center"
            v-if="currentProjectDetail"
          >{{currentProjectDetail["title"]}}</h1>
        </div>
        <div class="whitespace"></div>
        <div class="detail">
          <div class="detail_image">
            <img
              :src="currentProjectDetail.picture"
            >
          </div>
          <div class="detail_description">
            <p
              style="font-size:20px"
              v-if="currentProjectDetail"
            >{{currentProjectDetail.introduction}}</p>
            <div class="projectEditPanel">
              <Button
                v-show="this.isProjectManager"
                type="primary"
                style="font-size:20px;"
                @click="editinfoModal=true"
              >Edit projcet Info</Button>
            </div>
            <Modal
              v-model="editinfoModal"
              ok-text="submit"
              cancel-text="cancel"
              @on-ok="submit"
              @on-cancel="cancel"
              title="edit Project Infomation"
            ></Modal>
          </div>
        </div>
        <div class="whitespace"></div>
        <h1 style="text-align:center">Members</h1>
        <div class="projectMembersPanel">
          <div style="width:80%">
            <Tag
              color="success"
              style="height:40px;line-height:40px;font-size:20px;margin-left:0.5%;margin-right:0.5%">
            {{this.projectManager.userName}}
            </Tag>
            <Tag
              v-for="member in currentProjectDetail.members"
              color="primary"
              :key="member.index"
              style="height:40px;line-height:40px;font-size:20px;margin-left:0.5%;margin-right:0.5%"
            >{{member.userName}}</Tag>
          </div>
          <div style="width:20%">
            <Button
              v-show="this.isProjectManager"
              type="success"
              style="display:flex;justify-content:center;align-items:center;height:40px;;float:right"
            >
              <span
                style="height:30px;line-height:30px;font-size:20px"
                @click="inviteModalShow()"
              >Invite</span>
            </Button>
            <Modal
              v-model="inviteModal"
              @on-ok="invite()"
              @on-cancel="cancel()"
              ok-text="Assure"
              cancel-text="Cancel"
              title="Invite others join in the Project"
            >
              <!-- <br> -->
              <div class="form_style">
                <span>Reciver</span><!-- <Tag v-for="item in count" :key="item" :name="item" closable @on-close="handleClose2">标签{{ item + 1 }}</Tag> -->
                <Input v-model="inputEmail" style="width:200px"/>
                <Button icon="ios-add" type="dashed" size="small" @click="addEmail(inputEmail)">Add</Button>
              </div>
              <div style="margin-left:20%;width:80%">
                <Tag v-for="(item,index) in inviteEmailList" :key="index" color="success" @on-close="delEmail(index)" closable>{{item}}</Tag>
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
                <Button type="primary" v-clipboard="copyProjectId" @success="handleSuccess('id')" @error="handleError('id')">Copy Project ID</Button>
                <Button type="default" v-clipboard="copyProjectTitle" @success="handleSuccess('title')" @error="handleError('title')">Copy Project Name</Button>
              </div>
            </Modal>
          </div>
        </div>
        <div class="whitespace"></div>
        <h1 style="text-align:center">Sub projects</h1>
        <div class="subprojectPanel">
          <div class="subProjectListStyle">
            <div class="whitespace"></div>
            <div
              v-for="(subProject,index) in subProjectList"
              :key="subProject.index"
            >
              <Col span="6" offset="1">
                <Card class="subProjectStyle">
                  <Button
                    type="primary"
                    slot="extra"
                    style="margin:-5px 5px 0 5px"
                    @click="handOverSubProjectShow(index)"
                    v-show="subProject.isManager"
                    icon="md-happy"
                    title="Authorize"
                  ></Button>
                  <Button
                    type="success"
                    slot="extra"
                    style="margin:-5px 5px 0 5px"
                    @click="editSubProjectShow(index)"
                    v-show="subProject.isManager"
                    icon="ios-brush"
                    title="edit"
                  ></Button>
                  <Button
                    type="error"
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
                  <p>{{subProject["description"]}}</p>
                  <!-- <hr> -->
                  <br>
                  <div>
                    <span
                      style="float:left;color:white;background-color:#2d8cf0;padding:2.5px"
                    >Manager</span>
                    <span style="float:right;padding:2.5px">{{subProject.manager}}</span>
                  </div>
                  <br>
                  <div class="whitespace"></div>
                  <div>
                    <span
                      style="float:left;;color:white;background-color:#2d8cf0;padding:2.5px"
                    >createtime</span>
                    <span style="float:right;padding:2.5px">{{subProject["createTime"]}}</span>
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
          >
            <div style="height:100px;background:azure">
              <RadioGroup v-model="newManagerId">
                <Radio v-for="(member,index) in subjectMembers" :key="member.index" :label="member.userId">
                  <span>
                    {{member.userName}}
                  </span>
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
          >
            <p>Once the deletion is confirmed, all module and resource information under the subsystem will be deleted. Please choose carefully.</p>
          </Modal>
          <div class="subProjectCreate">
            <Button type="success" @click="subProjectModal = true" v-show="this.isProjectManager||this.isProjectMember">Create</Button>
            <Modal
              v-model="subProjectModal"
              ok-text="create"
              cancel-text="cancel"
              @on-ok="createSubProject()"
              @on-cancel="cancel()"
              title="Create sub project"
              width="800px;"
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
        </div>
        <div class="whitespace"></div>
        <h1 style="text-align:center">Resource</h1>
        <div class="resourceCard"></div>
        <div class="resourcePanel" style="height:50px;float:right">
          <!-- <input id="uploadFile" type="file" class="model file" data-show-preview="false" data-show-upload="false"> -->
          <Button id="upload" type="primary" @click="uploadFileModalShow()"style="height:60px"><Icon type="md-cloud-upload" size="40"/></Button>
          <Modal
            v-model="uploadFileModal"
            title="upload file"
            @on-ok="submitFile()"
            @on-cancel="cancel()"
            ok-text="submit"
            cancel-text="cancel"
            width="600px"
          >
            <div style="display:flex;text-align:center;align-items:center;justify-content:center">
              <span style="width:20%">Type</span>
              <Input v-model="fileType"/>
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
        <Col span="20" offset="2">
          <Table :columns="projectTableColName" :data="this.projectResourceList" v-show="this.projectResourceList!=[]&&this.projectResourceList!='None'">
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
              >
                <Icon type="md-download"/>
              </Button>
            </template>
          </Table>
          <!-- 需要两部分的值，一个是表头，一个是列表项 -->
        </Col>
      </Col>
    </Row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      projectManager:{},
      //确定用户是否有更新项目的权限，控制是否显示编辑的按钮，只有创建者才有权对项目进行编辑
      isProjectManager: false,
      isProjectMember:false,
      copyProjectId:"",
      copyProjectTitle:"",
      copyProjectIdStatus:false,
      copyProjectTitleStatus:false,
      currentProjectDetail: {
        members: [],
        introduction: "",
        projectId: ""
      },
      //确定用户是否有更新项目的权限，控制是否显示编辑的按钮，只有创建者才有权对项目进行编辑
      projectEditAble: false,
      //移交权限给新的管理者
      handOverSubProjectModal:false,
      newManagerId:"",
      subjectMembers:[],
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
      inputEmail:"",
      inviteEmailList:[],
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
          width:250,
        },
        {
          title: "Description",
          key: "description",
          width:300,
        },
        {
          title: "type",
          key: "type",
          sortable: true,
          width:100
        },
        {
          title: "uploadTime",
          key: "uploadTime",
          sortable: true,
          width:200
        },
        {
          title: "uploaderId",
          key: "uploaderId",
          width:200
        },
        {
          title: "Action",
          slot: "action",
          width: 250,
          align: "center"
        }
      ]
      // projectTableColName:[{},{},{},{}]
    };
  },
  created: function() {
    // alert(111);
    this.$Message.config({
    top: 150,
    duration: 3
    });
    this.getProjectDetail();
    this.getAllSubProject();
    this.getAllResource();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (!(vm.isProjectManager || vm.isProjectMember)) {
          alert("No access");
          next("/project");
          // vm.$router.go(-1);
        }
      }
    });
  },
  methods: {
    getProjectDetail() {
      let pid = this.$route.params.id;
      let queryObject = { key: "projectId", value: pid };
      var that = this;
      try {
        $.ajax({
          url:
            "http://localhost:8081/project/inquiry" +
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
              let obj = data;
              that.currentProjectDetail = obj[0];
              // 邀请他人加入项目的form的复制项目id与项目名的按钮
              that.copyProjectId = that.currentProjectDetail.projectId;
              that.copyProjectTitle = that.currentProjectDetail.title;
              that.projectManager.userId =
                that.currentProjectDetail["managerId"];
              $.ajax({
                url:
                  "http://localhost:8081/user/inquiry" +
                  "?key=" +
                  "userId" +
                  "&value=" +
                  that.projectManager.userId,
                type: "GET",
                async: false,
                success: function(data) {
                  that.projectManager.userName = data.userName;
                },
                error: function(err) {
                  console.log("Get manager name fail.");
                }
              });
              localStorage.setItem(
                "projectId",
                that.currentProjectDetail.projectId
              );
              //将tag进行分割
              that.currentProjectDetail.tag = that.currentProjectDetail.tag.split(
                ","
              );
              that.isProjectManager = that.managerIdentity(that.currentProjectDetail.managerId);
              that.isProjectMember = that.memberIdentity(that.currentProjectDetail.members);
              // this.getAllSubProject(queryObject);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      } catch (err) {
        console.log(err);
      }
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.state.userId) {
        return true;
      } else {
        return false;
      }
    },
    memberIdentity(members) {
      let isMember;
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.state.userId) {
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
          isManager = this.subProjectList[i]["isManager"];
          isMember = this.subProjectList[i]["isMembers"];
        }
      }
      if (this.$store.getters.userState) {
        if (isManager || isMember) {
          this.$router.push({ path: `${data}/workspace` });
        } else {
          this.$Message.error('You have no property to access it');
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
      SubProject["managerId"] = this.$store.state.userId;
      console.log(SubProject);
      this.axios
        .post("http://localhost:8081/subProject/create", SubProject)
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
      this.isSubMember = memberIdentity(this.subProjectMembers);
      this.handOverSubProjectModal = true;
    },
    addEmail(email){
      this.inviteEmailList.push(email);
      this.inputEmail = "";
      console.log("邀请的成员邮箱数组如下:"+ this.inviteEmailList);
    },
    delEmail(index){
      this.inviteEmailList.splice(index, 1);
    },
    invite(){
      var emailFormBody={};
      emailFormBody["recipient"] = this.inviteEmailList.toString();
      emailFormBody["mailTitle"] = this.emailTitle;
      emailFormBody["mailContent"] = this.emailContent;
      console.log(emailFormBody);
      this.axios.post("http://localhost:8081//email/send", emailFormBody)
      .then(res=> {
        console.log(res.data);
      })
      .catch(err=> {
        console.log(err.data);
      })
    },
    handOverSubProject(){
      this.axios
        .get(
          "http://localhost:8081/subProject/manager?" +
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
        .post("http://localhost:8081/subProject/update", obj)
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
          "http://localhost:8081/subProject/delete?" +
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
          "http://localhost:8081/subProject/inquiry" +
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
            that.subProjectList = that.identity(that.subProjectList);
            for (let i = 0, n = 0; i < that.subProjectList.length; i++) {
              $.ajax({
                url:
                  "http://localhost:8081/user/inquiry" +
                  "?key=" +
                  "userId" +
                  "&value=" +
                  that.subProjectList[i]["managerId"],
                type: "GET",
                async: false,
                success: function(data) {
                  that.subProjectList[n++]["manager"] = data.userName;
                }
              });
            }
            console.log(that.subProjectList);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    identity(list) {
      for (var i = 0; i < list.length; i++) {
        let isSubManager=this.managerIdentity(list[i]["managerId"]);
        list[i]["isManager"]=isSubManager;
        let isSubMember=this.memberIdentity(list[i]["members"]);
        list[i]["isMember"]=isSubMember;
      }
      return list;
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
      console.log(this.file);
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
      formData.append("uploaderId", this.$store.state.userId);
      formData.append("scopeId", this.currentProjectDetail.projectId);
      //这里还要添加其他的字段
      console.log(formData.get("file"));
      this.axios
        .post("http://localhost:8081/resource/upload", formData)
        .then(res => {
          if (res != "None") {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File uploaded successfully",
              duration: 2
            });
            //这里重新获取一次该项目下的全部资源
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
      // this.axios.get("http://localhost:8081/resource/inquiry",obj)
      this.axios
        .get(
          "http://localhost:8081/resource/inquiry" +
            "?key=scopeId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          console.log(res.data);
          //写渲染函数，取到所有资源
          if (res.data !== "None") {
            this.projectResourceList = res.data;
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
    // showProjectResource(resource){
    // }
    show(index) {
      // alert(this.projectResourceList[index].pathURL);
      window.open(this.projectResourceList[index].pathURL);
      // 做一个判断，是否有权限下载此文件
      // this.$Modal.info({
      // title: "User Info",
      // content:"111"
      // content: `Name：${this.data6[index].name}<br>Age：${
      //   this.data6[index].age
      // }<br>Address：${this.data6[index].address}`
      // });
    },
    handleSuccess(data){
      if(data == "id"){
        this.copyProjectIdStatus = true;
        // alert("success");
      }else if(data == "title"){
        this.copyProjectTitleStatus = true;
      }
      this.$Notice.success({
                    title: 'Notification title',
                    desc: `Copy project ${data} success`,
                    duration: 1
                });
    },
    handleError(data){
      if(data == "id"){
        this.copyProjectIdStatus = false;
      }else if(data == "title"){
        this.copyProjectTitleStatus = false;
      }
      this.$Notice.error({
                    title: 'Notification title',
                    desc: `Copy project ${data} fail`,
                    duration: 1
                });
    },
  },
};
</script>
