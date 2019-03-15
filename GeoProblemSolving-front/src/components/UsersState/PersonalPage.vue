<template>
  <div>
    <Row>
      <Col span="22" offset="1">
        <Row>
          <Col :lg="5" :md="8" :sm="10" :xs="12">
             <div class="detailSidebar" :style="{height:detailSidebarHeight}">
            <div class="user-img">
              <img v-bind:src="userDetail.avatar" class="u_img">
            </div>
            <div style="text-align:center">
              <div class="single-info">{{userDetail.userName}}</div>
              <br>
            </div>
            <div class="user-desc" style="min-hieght:60px;padding:0 10px;border:1px dotted gray">
              <p>{{this.userDetail.introduction}}</p>
            </div>
            <div class="user-info">
              <div class="single-info">
                <span>email:</span>
                <span style="float:right">{{userDetail.email}}</span>
              </div>
              <div class="single-info">
                <span>phone</span>
                <span style="float:right">{{userDetail.mobilePhone}}</span>
              </div>
              <div class="single-info">
                <span>job:</span>
                <span style="float:right">{{userDetail.jobTitle}}</span>
              </div>
              <div class="single-info">
                <span>location:</span>
                <span style="float:right">{{userDetail.country}}&nbsp{{userDetail.city}}</span>
              </div>
              <div class="single-info">
                <span>organization</span>
                <span style="float:right">{{userDetail.organization}}</span>
              </div>
              <div class="single-info">
                <span>direction</span>
                <span style="float:right">{{userDetail.direction}}</span>
              </div>
              <div class="single-info">
                <span>homepage</span>
                <span style="float:right">{{userDetail.homePage}}</span>
              </div>
              <div class="whitespace"></div>
              <div class="display:flex">
                <Button
                  type="success"
                  style="height:40px;float:left"
                  @click="editModalShow()"
                >Update</Button>
                <Drawer
                  title="Profile Edit Panel"
                  placement="left"
                  :closable="true"
                  v-model="editProfileModal"
                  width="600px"
                >
                  <Form :model="personalInfoItem" :label-width="80">
                    <FormItem label="Name" prop="userName">
                      <Input
                        v-model="personalInfoItem.userName"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.userName"
                      ></Input>
                    </FormItem>
                    <FormItem label="Password" prop="password">
                      <Input
                        v-model="personalInfoItem.password"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.password"
                      ></Input>
                    </FormItem>
                    <FormItem label="Job Title" prop="jobTitle">
                      <Input
                        v-model="personalInfoItem.jobTitle"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.jobTitle"
                      ></Input>
                    </FormItem>
                    <FormItem label="E-mail" prop="email">
                      <Input
                        v-model="personalInfoItem.email"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.email"
                      ></Input>
                    </FormItem>
                    <FormItem label="Phone" prop="mobilePhone">
                      <Input
                        v-model="personalInfoItem.mobilePhone"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.mobilePhone"
                      ></Input>
                    </FormItem>
                    <FormItem label="Country" prop="country">
                      <Input
                        v-model="personalInfoItem.country"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.country"
                      ></Input>
                    </FormItem>
                    <FormItem label="City" prop="city">
                      <Input
                        v-model="personalInfoItem.city"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.city"
                      ></Input>
                    </FormItem>
                    <FormItem label="Agency" prop="organization">
                      <Input
                        v-model="personalInfoItem.organization"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.organization"
                      ></Input>
                    </FormItem>
                    <FormItem label="Direction" prop="direction">
                      <Input
                        v-model="personalInfoItem.direction"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.direction"
                      ></Input>
                    </FormItem>
                    <FormItem label="Url" prop="homePage">
                      <Input
                        v-model="personalInfoItem.homePage"
                        :class="{InputStyle: inputstyle}"
                        :placeholder="userDetail.homePage"
                      ></Input>
                    </FormItem>
                    <FormItem label="Introduce" prop="introduction">
                      <Input
                        v-model="personalInfoItem.introduction"
                        type="textarea"
                        :autosize="{minRows: 2,maxRows: 5}"
                        :placeholder="userDetail.introduction"
                      ></Input>
                    </FormItem>
                    <FormItem label="Avatar" prop="avatar">
                      <div>
                        <div class="demo-upload-list" v-if="userDetail.avatar!=''">
                          <template>
                            <img v-bind:src="userDetail.avatar">
                            <div class="demo-upload-list-cover">
                              <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                              <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                            </div>
                          </template>
                        </div>
                        <div class="uploadBox">
                          <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
                          <input @change="uploadPhoto($event)" type="file" class="uploadAvatar">
                        </div>
                        <Modal title="View Image" v-model="visible">
                          <img :src="userDetail.avatar" v-if="visible" style="width: 100%">
                        </Modal>
                      </div>
                    </FormItem>
                    <FormItem>
                      <Button type="success" @click="submitProfileEdit(personalInfoItem)">Submit</Button>
                      <Button @click style="margin-left: 50%" type="primary">Reset</Button>
                    </FormItem>
                  </Form>
                </Drawer>
                <Button
                  type="error"
                  style="height:40px;float:right"
                  @click="logOutModalShow()"
                >logOut</Button>
              </div>

              <Modal
                v-model="logOutProfileModal"
                title="Logout Warning"
                @on-ok="logOutAccount()"
                @on-cancel="cancel"
                logOutAccount-text="assure"
                cancel-text="cancel"
              >
                <p
                  style="font-size:20px"
                >If you choose logout your account,the personal information you have filled will be removed.</p>
              </Modal>
            </div>
          </div>
          </Col>
          <!-- <Col span="18" offset="1"> -->
          <Col :lg="{span:18,offset:1}" :md="{span:15,offset:1}" :sm="{span:13,offset:1}" :xs="{span:11,offset:1}">
            <div class="rightContent">
                <Tabs value="Overview">
              <TabPane label="Overview" name="Overview">
                <Col :lg="{span:22,offset:1}" :md="{span:22,offset:1}" :sm="{span:22,offset:1}">
                  <Card >
                    <p slot="title">History Line</p>
                    <Timeline style="margin-top:20px;margin-left:5%;max-height:300px;overflow-y:auto">
                      <TimelineItem v-for="(item,index) in userEventList" :key="index">
                        <strong>
                          <p class="time">{{item.createTime}}</p>
                        </strong>
                        <p class="content">{{item.description}}</p>
                      </TimelineItem>
                    </Timeline>
                  </Card>
                  <br>
                  <div>
                    <Card>
                      <p slot="title">Resource List</p>
                      <Table :data="userResourceList" :columns="resourceColumn" class="table"></Table>
                    </Card>
                  </div>
                </Col>
              </TabPane>
              <TabPane label="Participatory Project" name="Participatory">
                <div
                  v-for="(item,index) in joinedProjectsList"
                  :key="index"
                  v-show="joinedProjectsList!='None'"
                >
                  <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                    <Card style="height:320px;margin-top:20px;">
                      <p
                        slot="title"
                        style="height:40x"
                        class="projectsTitle"
                        @click="goSingleProject(item.projectId)"
                      >{{item.title}}</p>
                      <p
                        style="height:200px;text-indent:2em;overflow-y:auto;word-break:break-word"
                      >{{item.introduction}}</p>
                      <br>
                      <div style="height:40px">
                        <span style="float:left">CreateTime:</span>
                        <span style="float:right">{{item.createTime}}</span>
                      </div>
                    </Card>
                  </Col>
                </div>
              </TabPane>
              <TabPane label="Management Project" name="Management">
                <div
                  v-for="(mProject,index) in userManagerProjectList"
                  v-show="userManagerProjectList!='None'"
                >
                  <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                    <Card style="height:320px;margin-top:20px">
                      <p
                        slot="title"
                        class="projectsTitle"
                        @click="goSingleProject(mProject.projectId)"
                      >{{mProject.title}}</p>
                      <Button
                        class="authorBtn"
                        type="default"
                        slot="extra"
                        title="edit"
                        style="margin:-5px 5px 0 5px"
                        @click="authorizeModalShow(index)"
                        icon="md-happy"
                      ></Button>
                      <Button
                        class="deleteBtn"
                        type="default"
                        slot="extra"
                        style="margin:-5px 5px 0 5px"
                        @click="deleteProjectModalShow(index)"
                        icon="md-close"
                        title="remove"
                      ></Button>
                      <!-- 表头结束 -->
                      <p
                       style="height:200px;text-indent:2em;overflow-y:auto;word-break:break-word"
                      >{{mProject.introduction}}</p>
                      <!-- <hr> -->
                      <br>
                      <div>
                        <span style="float:left">CreateTime:</span>
                        <span style="float:right">{{mProject.createTime}}</span>
                      </div>
                    </Card>
                  </Col>
                </div>
              </TabPane>
            </Tabs>
          </div>
          </Col>
        </Row>
        <div>


        </div>
      </Col>
    </Row>
    <!-- 授权的modal -->
    <Modal
      v-model="authorizeProjectModal"
      @on-ok="authorize()"
      @on-cancel="cancel()"
      ok-text="assure"
      cancel-text="cancel"
    >
      <p style="slot">Hand the project to others</p>
      <div>
        <RadioGroup v-model="selectManagerId">
          <Radio
            v-for="(member,index) in projectMemberList"
            :key="member.index"
            :label="member.userId"
          >
            <span>{{member.userName}}</span>
          </Radio>
        </RadioGroup>
        <!-- 用radio将用户表示出来 -->
        <!-- <tag v-for="(member,index) in projectMemberList" @click="choose(index)" :key="member.index">{{member.userName}}</tag> -->
      </div>
    </Modal>


  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  created() {
    this.getUserProfile();
    this.getManagerProjectList();
    this.readPersonalEvent();
    this.getUserResource();
    this.detailSidebarHeight = window.innerHeight - 60 + "px";
    // this.rightContentWidth = window.innerWidth - 270 + "px";
  },
  computed: {
    username() {
      return this.$store.state.userName;
    },
    avatar() {
      return this.$store.state.avatar;
    },
    userId() {
      return this.$store.state.userId;
    }
  },
  mounted() {
    this.getUserProfile();
    this.getManagerProjectList();
  },
  components: {
    Avatar
  },
  data() {
    return {
      userDetail: {
        userName: "",
        email: "",
        password: "",
        jobTitle: "",
        mobilePhone: "",
        gender: "",
        country: "",
        city: "",
        organization: "",
        introduction: "",
        direction: "",
        homePage: "",
        avatar: ""
      },
      errors: {},
      resourceColumn: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "Name",
          key: "name",
          sortable: true
        },
        {
          title: "Type",
          key: "type"
        },
        {
          title: "Time",
          key: "uploadTime",
          sortable: true
        }
      ],
      userManagerProjectList: [
        {
          title: "",
          description: "",
          type: "",
          createTime: "",
          tag: ""
        }
      ],
      editProfileModal: false,
      logOutProfileModal: false,
      editProjectModal: false,
      // 项目授权的模态框
      authorizeProjectModal: false,
      authorizeProjectIndex: "",
      // 删除项目在项目列表中的索引值
      DelelteProjectIndex: "",
      // 编辑的项目在项目列表中的索引值
      editProjectIndex: "",
      //编辑项目专用的字段
      editType: "",
      editTags: "",
      inputTag: "",
      editDescription: "",
      editTitle: "",
      editIntroduction: "",
      editPrivacy: "",
      editProjectId: "",
      //编辑项目专用的字段结束
      item: "",
      projectMemberList: [],
      selectManager: this.$store.state.userName,
      selectManagerId: this.$store.state.userId,
      //当前项目的id
      currentProjectId: "",
      //当前用户信息的表单
      personalInfoItem: {
        userName: "",
        email: "",
        password: "",
        jobTitle: "",
        mobilePhone: "",
        gender: "",
        country: "",
        city: "",
        organization: "",
        introduction: "",
        direction: "",
        homePage: "",
        avatar: ""
      },
      //用来验证个人信息维护表单填写规范的依据
      //输入框的样式
      inputstyle: true,
      //控制填写表单时用户头像显示的样式
      visible: false,
      //抽屉开启状态控制
      // drawerClose:false,
      //加入的项目的名字id数组
      joinedProjectsNameArray: [],
      //加入的项目详情数组列表
      joinedProjectsList: [],
      // 关于样式的变量定义
      detailSidebarHeight: "",
      // 用户event列表
      userEventList: [],
      userResourceList: []
      // rightContentWidth:"",
    };
  },
  methods: {
    // 获取用户的详细信息
    getUserProfile() {
      this.axios
        .get(
          "/GeoProblemSolving/user/inquiry" +
            "?key=userId" +
            "&value=" +
            this.userId
        )
        .then(res => {
          this.userDetail = res.data;
          //打印用户的具体信息
          // console.log(this.userDetail);
          this.joinedProjectsNameArray = this.userDetail.joinedProjects;
          this.getParticipatoryList(this.joinedProjectsNameArray);
          // console.log("joinedProjectsList是" + this.joinedProjectsList);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //获取用户参与的所有项目列表
    //获取用户参与的项目列表
    getParticipatoryList(projectIds) {
      var count = projectIds.length;
      let participatoryProjectListTemp = [];
      for (let i = 0; i < projectIds.length; i++) {
        this.axios
          .get(
            "http://localhost:8081/GeoProblemSolving/project/inquiry" +
              "?key=projectId" +
              "&value=" +
              projectIds[i].projectId
          )
          .then(res => {
            participatoryProjectListTemp.push(res.data[0]);
            if (--count == 0) {
              this.$set(
                this,
                "joinedProjectsList",
                participatoryProjectListTemp
              );
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    //获取用户可管理支配的全部项目列表
    getManagerProjectList() {
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            this.userId
        )
        .then(res => {
          // 打印用户所管理的项目
          // console.log(res.data);
          this.userManagerProjectList = res.data;
        })
        .catch(err => {});
    },
    //退出项目的函数
    quitProject() {
      let quitData = new URLSearchParams();
      //包含项目id与用户名字段
      quitData.append("ProjectID", "");
      quitData.append("UserName", this.$store.state.username);
      this.axios
        .post("/GeoProblemSolving/TeamModeling/QuitProjectServlet", quitData)
        .then(res => {
          if (res.data === "Success") {
            this.$Message.success("Quit Success");
            this.getProjectList();
          } else if (res.data === "Fail") {
          }
        })
        .catch(err => {});
    },
    //注销的模态框按钮
    logOutModalShow() {
      this.logOutProfileModal = true;
    },
    //注销账户的函数
    logOutAccount() {
      this.axios
        .get(
          "/GeoProblemSolving/user/remove?" +
            "userId=" +
            this.$store.state.userId
        )
        .then(res => {
          // 打印注销之后的结果返回值
          // console.log("注销之后的返回值:"+ res.data);
          this.$router.push({ name: "Home" });
          // this.$router.router.push()
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    authorizeModalShow(index) {
      this.currentProjectId = this.userManagerProjectList[index].projectId;
      this.authorizeProjectModal = true;
      this.projectMemberList = this.userManagerProjectList[index].members;
    },
    authorize() {
      this.axios
        .get(
          "/GeoProblemSolving/project/manager?" +
            "projectId=" +
            this.currentProjectId +
            "&newManagerId=" +
            this.selectManagerId
        )
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {});
    },
    // editProjectModalShow(index) {
    //   this.editProjectIndex = index;
    //   this.editProjectModal = true;
    //   let editProjectInfo = this.userManagerProjectList[this.editProjectIndex];
    //   console.log("当前编辑的项目的详情信息是:" + editProjectInfo.projectId);
    //   this.editTitle = editProjectInfo.title;
    //   this.editIntroduction = editProjectInfo.introduction;
    //   this.editDescription = editProjectInfo.description;
    //   this.editType = editProjectInfo.category;
    //   this.editTags = editProjectInfo.tag.split(",");
    //   this.editPrivacy = editProjectInfo.privacy;
    //   this.editProjectId = editProjectInfo.projectId;
    // },

    cancel() {
      this.$Message.info("cancel");
    },

    //更改个人信息的函数
    changeProfile() {},
    editModalShow() {
      this.editProfileModal = true;
    },
    //不用该方法的话就使用placeholder
    useDefault() {
      alert(111);
    },
    //处理修改用户头像信息相关的函数
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      var filename = file.name;
      var imgcode = "";
      // 2,621,440   2M
      if (filesize > 2101440) {
        // 图片大于2MB
        this.$Message.error("size > 2MB");
      }
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = e => {
        // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
        imgcode = e.target.result;
        console.log(imgcode);
        this.$store.commit("uploadAvatar", imgcode);
      };
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.$store.commit("uploadAvatar", "");
    },
    submitProfileEdit(data) {
      // var changedProfile = {};
      var changedProfile = new URLSearchParams();
      changedProfile.append("userId", this.$store.state.userId);
      //筛选出需要修改的信息
      for (var item in data) {
        if (data[item] != "") {
          changedProfile.append(item, data[item]);
        }
      }
      this.axios
        .post("/GeoProblemSolving/user/update", changedProfile)
        .then(res => {
          if (res.data === "Success") {
            // this.drawerClose = true;
            this.$Notice.success({
              title: "notification",
              desc: "Profile update successfully"
            });
            this.getUserProfile();
          }
        })
        .catch(err => {});
    },
    addTag(tag) {
      this.editTags.push(tag);
      this.inputTag = "";
    },
    //处理tag的测试函数
    deleteTag(index) {
      this.editTags.splice(index, 1);
    },
    //点击跳转到指定项目的函数
    goSingleProject(id) {
      this.$router.push({ name: "ProjectDetail", params: { id: id } });
    },
    //获取个人上传的全部资源的函数
    getUserFile() {},
    readPersonalEvent() {
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry?" +
            "key=userId" +
            "&value=" +
            this.$store.state.userId
        )
        .then(res => {
          if(res.data!="None"){
            this.userEventList = res.data;
            console.table(result);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getUserResource() {
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=uploaderId" +
            "&value=" +
            this.$store.state.userId
        )
        .then(res => {
          if(res.data!="None"){
            this.userResourceList = res.data;
            console.table(this.userResourceList)
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* .mainPanel {
  display: flex;
} */
.detailSidebar {
  /* min-width: 250px;
  max-width: 300px; */
  /* width:300px; */
  margin-right: 20px;
}
.rightContent {
  flex: 1;
}
img {
  padding: 10px;
  max-width: 100%;
  max-height: 100%;
}
h1 {
  font-weight: normal;
}
body {
  overflow-x: hidden;
}
.sidebar {
  margin-top: 20px;
}
.username {
  text-align: center;
  height: 30px;
  margin-bottom: 20px;
  line-height: 30px;
  font-size: 15px;
}
.user-img {
  margin-top: 20px;
  width: 100%;
  max-height: 100%;
  text-align: center;
}
.u_img {
  max-width: 100%;
}
.single-info {
  padding: 20px;
  height: 20px;
  font-size: 10px;
  line-height: 20px;
}
.user-project {
  margin-top: 20px;
}
.project-card {
  margin-top: 5%;

  height: 200px;
  background-color: lightgray;
}
.user-contribution {
  margin-top: 20px;
}
.user-history {
  margin-top: 20px;
  border: 1px solid black;
}
.user-data {
  margin-top: 20px;
  border: 1px solid black;
  max-height: 300px;
  overflow-y: scroll;
}

/* 表示空格间距的 */
.whitespace {
  height: 20px;
}
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

/* 关于提交用户更改头像信息的样式 */
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  overflow: hidden;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
/* 2-25 add 实现的效果是旋浮上去出现下划线且变红 */
.projectsTitle:hover {
  color: red;
  cursor: pointer;
}

/* 新定义的样式 */
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
.table table {
  table-layout: auto;
  width: 100% !important;
}
</style>
