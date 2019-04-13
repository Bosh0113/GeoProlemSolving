<template>
  <div>
    <Row>
      <Col span="22" offset="1">
        <Row>
          <Col :lg="5" :md="8" :sm="10" :xs="12">
            <div class="detailSidebar" :style="{height:detailSidebarHeight}">
              <div class="user-img">
                <img v-bind:src="userDetail.avatar" class="u_img"
                  v-if="userDetail.avatar!=''&&userDetail.avatar!='undefined'">
                <avatar
                  style="width:100%"
                  :username="userDetail.userName"
                  :size="200"
                  :rounded="false"
                  v-else>
                </avatar>
              </div>
              <div class="single-info">
                <Icon type="ios-contact-outline" :size="20"/>
                <span>{{userDetail.userName}}</span>
              </div>
              <div class="user-info">
                <div class="single-info" >
                  <!-- <span>email:</span> -->
                  <Icon type="ios-mail-outline" :size="20"/>
                  <span>{{userDetail.email}}</span>
                </div>
                <div class="single-info">
                  <Icon type="ios-call-outline" :size="20"/>
                  <span>{{userDetail.mobilePhone}}</span>
                </div>
                <div class="single-info">
                  <Icon type="ios-hammer-outline" :size="20"/>
                  <span>{{userDetail.jobTitle}}</span>
                </div>
                <div class="single-info">
                  <Icon type="ios-compass-outline" :size="20"/>
                  <span>{{userDetail.country}}&nbsp{{userDetail.city}}</span>
                </div>
                <div class="single-info">
                  <Icon type="ios-home-outline" :size="20"/>
                  <span>{{userDetail.organization}}</span>
                </div>
                <div class="single-info">
                  <Icon type="ios-contract" :size="20"/>
                  <span>{{userDetail.direction}}</span>
                </div>
                <div class="single-info">
                  <Icon type="md-link" :size="20"/>
                  <span>{{userDetail.homePage}}</span>
                </div>
                <br>
                <div style="padding:10px;font-size:12px;border:1px dotted lightgray">
                  {{this.userDetail.introduction}}
                </div>
                <div class="whitespace"></div>
                <div style="display:flex;justify-content:center">
                  <Button
                    type="success"
                    style="height:40px"
                    @click="editModalShow()"
                    title="Edit"
                  ><Icon type="md-create" :size="20"/></Button>
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
                          style="word-wrap:break-word"
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
                            <Icon
                              type="ios-camera"
                              size="20"
                              style="position:absolute;margin:18px;"
                            ></Icon>
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
                </div>
              </div>
            </div>
          </Col>
          <Col
            :lg="{span:18,offset:1}"
            :md="{span:15,offset:1}"
            :sm="{span:13,offset:1}"
            :xs="{span:11,offset:1}"
          >
            <div class="rightContent">
              <Tabs value="Overview">
                <TabPane label="Overview" name="Overview">
                  <Col :lg="{span:22,offset:1}" :md="{span:22,offset:1}" :sm="{span:22,offset:1}">
                    <Card>
                      <p slot="title">History Line</p>
                      <Timeline
                        style="margin-left:5%;max-height:300px;overflow-y:auto"
                      >
                        <TimelineItem v-for="(item,index) in userEventList" :key="index">
                          <strong>
                            <p class="time">{{item.createTime}}</p>
                          </strong>
                          <p class="content">{{item.description}}</p>
                        </TimelineItem>
                      </Timeline>
                    </Card>
                    <br>
                    <div style="margin-bottom:40px">
                      <Card>
                        <p slot="title">Resource List</p>
                        <div style="overflow-y:scroll;height:500px">
                          <Table :data="userResourceList" :columns="resourceColumn" class="table">
                            <template slot-scope="{ row }" slot="name">
                              <strong>{{ row.name }}</strong>
                            </template>
                            <template slot-scope="{ row, index }" slot="action">
                              <Button
                                type="success"
                                size="small"
                                style="margin-right: 10px"
                                :href="userResourceList[index].pathURL"
                                title="download"
                                @click="download(index)"
                              >
                                <Icon type="md-download"/>
                              </Button>
                              <Button type="warning" size="small" style="margin-right: 10px">
                                <Icon type="md-share" @click="processResourceModalShow(index)" title="share"/>
                              </Button>
                              <Button
                                type="error"
                                size="small"
                                @click="deleteResource(userResourceList[index].resourceId)"
                              >
                                <Icon type="md-close"/>
                              </Button>
                            </template>
                          </Table>
                        </div>
                      </Card>
                    </div>
                  </Col>
                </TabPane>
                <TabPane label="Participatory Project" name="Participatory">
                  <Card :bordered="false" v-if="joinedProjectsList.length == 0">
                      <div style="display:flex;justify-content:center">
                        <Icon type="md-alert" size="40" color="gray"/>
                      </div>
                      <br>
                      <div style="display:flex;justify-content:center" >
                        <h2 style="text-align:center;width:50%">Sorry,you don't participate in any projects.</h2>
                      </div>
                  </Card>
                  <div
                    v-for="(item,index) in joinedProjectsList"
                    :key="index"
                    v-show="joinedProjectsList!=[]"
                  >
                    <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                      <div class="participatoryProjectCard" @click="goSingleProject(item.projectId)">
                        <Card style="height:320px;margin-top:20px;">
                        <p
                          slot="title"
                          style="height:40x"
                          class="projectsTitle"
                        >{{item.title}}</p>
                        <Button slot="extra" @click.stop="quitModalShow(item.projectId)">Quit</Button>
                        <p
                          style="height:200px;text-indent:2em;overflow-y:auto;word-break:break-word"
                        >{{item.introduction}}</p>
                        <br>
                        <div style="height:40px">
                          <span style="float:left">CreateTime:</span>
                          <span style="float:right">{{item.createTime}}</span>
                        </div>
                      </Card>
                      </div>

                    </Col>
                  </div>
                </TabPane>
                <TabPane label="Management Project" name="Management">
                  <Card :bordered="false" v-if="userManagerProjectList.length == 0">
                      <div style="display:flex;justify-content:center">
                        <Icon type="md-alert" size="40" color="gray"/>
                      </div>
                      <br>
                      <div style="display:flex;justify-content:center" >
                        <h2 style="text-align:center;width:50%">Sorry,until now you don't create any projects.</h2>
                      </div>
                  </Card>
                  <div
                    v-for="(mProject,index) in userManagerProjectList"
                    v-show="userManagerProjectList!='None'"
                    :key="index"
                  >
                    <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                    <div class="manageProjectsCard" @click="goSingleProject(mProject.projectId)">
                      <Card style="height:320px;margin-top:20px">
                        <p
                          slot="title"
                          class="projectsTitle"
                        >{{mProject.title}}</p>
                        <Button
                          class="authorBtn"
                          type="default"
                          slot="extra"
                          title="Privilege change"
                          style="margin:-5px 5px 0 5px"
                          @click.stop="authorizeModalShow(index)"
                          icon="md-happy"
                        ></Button>
                        <Button
                          class="deleteBtn"
                          type="default"
                          slot="extra"
                          style="margin:-5px 5px 0 5px"
                          @click.stop="deleteProjectModalShow(mProject.projectId)"
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
                    </div>

                    </Col>
                  </div>
                </TabPane>
              </Tabs>
            </div>
          </Col>
        </Row>
        <div></div>
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
          <Radio v-for="member in projectMemberList" :key="member.index" :label="member.userId">
            <span>{{member.userName}}</span>
          </Radio>
        </RadioGroup>
        <!-- 用radio将用户表示出来 -->
        <!-- <tag v-for="(member,index) in projectMemberList" @click="choose(index)" :key="member.index">{{member.userName}}</tag> -->
      </div>
    </Modal>
    <Modal
      title="Quit Project"
      v-model="quitModal"
      @on-ok="quitProject()"
      @on-cancel="cancel()"
      ok-text="ok"
      cancel-text="cancel"
    >
      <p>Once you exit the project, you will not be able to participate in the collaborative process, confirm the exit?</p>
    </Modal>
    <Modal
        v-model="processResourceModal"
        title="share resource in other projects"
        @on-ok="processResource()"
        @on-cancel="cancel"
        ok-text="assure"
        cancel-text="cancel"
        >
        <!-- todo:这里需要过滤参与的项目以及管理的项目重新渲染 -->
        <div>
          <h3>Participatory Projects</h3>
          <!-- 多选框 -->
          <RadioGroup v-model="selectShareProject">
          <span @click="selectPID(item.projectId)" v-for="(item,index) in joinedProjectsNameList" >
           <Radio :key="item.index" :label="item.title" ></Radio>
          </span>
        </RadioGroup>
          <!-- <div v-for="(item,index)in joinedProjectsNameArray" :key="item.index">{{item.title}}</div> -->
        </div>
        <div>
          <h3>Management Projects</h3>
          <RadioGroup v-model="selectShareProject">
            <span @click="selectPID(item.projectId)" v-for="(item,index) in userManagerProjectList">
              <Radio :key="item.index" :label="item.title"></Radio>
            </span>
          </RadioGroup>
        </div>
        <div style="margin-top:5px">
          <Icon type="ios-information-circle-outline" color="lightblue"/>
          <span>
            the resource will be shared in the project you choosed.
          </span>
        </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        next();
      }
    });
  },
  mounted() {
    this.getUserProfile();
    // 获取用户资源
    this.getUserResource();
    // 获取用户管理列表
    this.getManagerProjectList();
    this.getParticipatoryList(this.joinedProjectsNameArray);
    // 获取用户的历史event
    this.readPersonalEvent();
    this.detailSidebarHeight = window.innerHeight - 60 + "px";
    this.joinedProjectsNameList = this.$store.getters.userInfo["joinedProjects"];
  },
  computed: {
    username() {
      return this.$store.getters.userName;
    },
    avatar() {
      return this.$store.getters.avatar;
    },
    userId() {
      return this.$store.getters.userId;
    },

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
        },
        {
          title: "Action",
          slot: "action",
          width: 200,
          align: "center"
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
      selectManager: this.$store.getters.userName,
      selectManagerId: this.$store.getters.userId,
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
      userResourceList: [],
      // rightContentWidth:"",
      // 退出项目的modal
      quitModal: false,
      quitPid: "",
      // 处理资源的模态框激活
      processResourceModal:false,
      // 选中资源的索引
      selectResourceIndex:"",
      // 参与的项目的名称列表
      joinedProjectsNameList:[],
      // 选中的将要分享资源的项目名
      selectShareProject:"",
      selectShareProjectId:"",
    };
  },
  methods: {
    //获取用户的详细信息
    getUserProfile() {
      this.userDetail = this.$store.getters.userInfo;
      //打印用户的具体信息
      this.joinedProjectsNameArray = this.userDetail.joinedProjects;
      // this.getParticipatoryList(this.joinedProjectsNameArray);
    },
    //获取用户参与的项目列表
    getParticipatoryList(projectIds) {
      if(projectIds != null) {
        var count = projectIds.length;
        let participatoryProjectListTemp = [];
        for (let i = 0; i < projectIds.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/project/inquiry" +
                "?key=projectId" +
                "&value=" +
                projectIds[i].projectId
            )
            .then(res => {
              if (res.data != "None") {
                participatoryProjectListTemp.push(res.data[0]);
              }
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
          this.userManagerProjectList = res.data;
        })
        .catch(err => {});
    },
    //退出项目的函数
    quitProject() {
      this.axios
        .get(
          "/GeoProblemSolving/project/quit" +
            "?projectId=" +
            this.quitPid +
            "&userId=" +
            this.$store.getters.userId
        )
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
            this.$store.getters.userId
        )
        .then(res => {
          this.$router.push({ name: "Home" });
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
    deleteProjectModalShow(pid) {
      if(confirm("Are you sure to delete this project?")){
        this.axios
          .get("/GeoProblemSolving/project/delete?" + "projectId=" + pid)
          .then(res => {
            this.$store.commit("getUserInfo");
            var newManageProjects =[];
            var oldManageProjects=this.userManagerProjectList;
            for(var i=0;i<oldManageProjects.length;i++){
              if(oldManageProjects[i].projectId!=pid){
                newManageProjects.push(oldManageProjects[i]);
              }
            }
            this.$set(this,"userManagerProjectList",newManageProjects);
          })
          .catch(err => {
            alert(err.data);
          });
      }
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
          if(res.data!="Fail"){
            let notice = {};
            notice["recipientId"] = this.selectManagerId;
            notice["type"] = "Notice";
            notice["content"] = {
              title: "Manager change",
              description:
                "You have been the manager of project " +
                this.userManagerProjectList[index].title +
                " !"
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
        })
        .catch(err => {});
    },
    cancel() {
      this.$Message.info("cancel");
    },
    editModalShow() {
      this.editProfileModal = true;
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
      }else{
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = e => {
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          imgcode = e.target.result;
          this.$store.commit("uploadAvatar", imgcode);
        };
      }

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
      changedProfile.append("userId", this.$store.getters.userId);
      //筛选出需要修改的信息
      for (var item in data) {
        if (data[item] != "") {
          changedProfile.append(item, data[item]);
        }
      }
      this.axios
        .post("/GeoProblemSolving/user/update", changedProfile)
        .then(res => {
          if (res.data !== "Fail") {
            // this.drawerClose = true;
            this.$Notice.success({
              title: "notification",
              desc: "Profile update successfully"
            });
            let userInfo = res.data;
            userInfo.userState = true;
            this.$store.commit("setUserInfo", userInfo);
            this.$set(this, "userDetail", userInfo);
            sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
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
            "eventType=project" +
            "&key=userId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data != "None") {
            this.userEventList = res.data;
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
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.userResourceList = res.data;

          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    download(index) {
      window.open(this.userResourceList[index].pathURL);
    },
    quitModalShow(pId) {
      this.quitModal = true;
      this.quitPid = pId;
    },
    deleteResource(id) {
      if (id != "") {
        this.axios
          .get("/GeoProblemSolving/resource/delete?" + "resourceId=" + id)
          .then(res => {
            if (res.data == "Success") {
              this.$Message.info("Delete successfully");
              this.getUserResource();
            } else if (res.data == "Fail") {
              this.$Message.info("Failure");
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    // 处理个人的资源可以选择copy到参与的项目，也可以选择copy到管理的项目
    processResource(){
      let resourceInfo = {};
      this.joinedProjectsNameList = this.$store.getters.userInfo["joinedProjects"];
      resourceInfo = this.userResourceList[this.selectResourceIndex];
      let shareForm = new FormData();
      shareForm.append("name",resourceInfo.name);
      shareForm.append("description",resourceInfo.description);
      shareForm.append("belong",this.selectShareProject);
      shareForm.append("type",resourceInfo.type);
      shareForm.append("fileSize",resourceInfo.fileSize);
      shareForm.append("pathURL",resourceInfo.pathURL);
      shareForm.append("uploaderId",resourceInfo.uploaderId);
      // 还有一个获取到选中的项目的id
      let scopeObject = {
        projectId: this.selectShareProjectId,
        subProjectId: "",
        moduleId: ""
      };
      shareForm.append("scope", JSON.stringify(scopeObject));
      if(scopeObject.projectId!=""){
        this.axios
        .post("/GeoProblemSolving/resource/share", shareForm)
        .then(res => {
          if (res.data != "Fail") {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File shared to " + this.selectShareProject + " successfully.",
              duration: 2
            });
            // 保存记录
            // addUploadEvent( this.selectShareProjectId);
          }
        })
        .catch(err => {});
      }
      // uploaderId
    },
    cancel(){},    
    addUploadEvent(scopeId) {
      let form = {};
      let description =
        this.$store.getters.userName +
        " share a " +
        this.fileType +
        " file to " +
        " project called " +
        this.currentProjectDetail.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      form["eventType"] = "project";
      form["userId"] = this.$store.getters.userId;
      this.axios
        .post("/GeoProblemSolving/history/save", form)
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    processResourceModalShow(index){
      this.processResourceModal = true;
      this.selectResourceIndex = index;
    },
    selectPID(id){
      this.selectShareProjectId = id;
      console.log(this.selectShareProjectId);
    }
  }
};
</script>
<style scoped>
.detailSidebar {
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
.userDescription {
  height: auto;
  line-height: 10px;
  font-size: 10px;
  /* max-width: 200px; */
  /* display: inline-block; */
  overflow: hidden;
  word-wrap: break-word;
  word-break: break-all;
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
  /* color: red; */
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
.participatoryProjectCard:hover,{
  cursor:pointer;
}
.manageProjectsCard:hover{
  cursor:pointer;
}
</style>
