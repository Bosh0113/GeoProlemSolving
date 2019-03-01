<template>
  <div v-bind="userDetail">
    <Row>
      <Col span="4" offset="1" class="sidebar">
        <div class="user-img">
          <img v-bind:src="userDetail.avatar" class="u_img">
        </div>
        <div class="user-detail">
          <div class="user-name" style="text-align:center">
            <!-- {{userDetail.userName}} -->
            <div class="single-info">
                {{userDetail.userName}}
            </div>
            <br>
            <!-- <Tag color="primary" class="username"></Tag> -->
          </div>
          <div class="user-desc" style="min-hieght:60px;padding:20px;border:1px solid gray">
            <p>{{this.userDetail.introduction}}</p>
            <!-- <Input type="textarea" :rows="4" :value="this.userDetail.introduction"/> -->
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
              <Button type="success" style="height:40px;float:left" @click="editModalShow()">Update</Button>
              <Drawer
                title="Profile Edit Panel"
                placement="left"
                :closable="true"
                v-model="editProfileModal"
                width="600px"

              >
                <!-- 修改个人信息的表单 -->
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
                          <img v-bind:src="userDetail.avatar" >
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
              <Button type="error" style="height:40px;float:right" @click="logOutModalShow()">logOut</Button>
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
      <Col span="17" offset="1" class="user-project">
        <Tabs value="name1">
          <TabPane label="Participatory Project" name="Participatory">
            <Row>
              <Col span="23">
                <div style="display:flex">
                  <Row>
                    <Col span="11" offset="1" v-for="i in 3" :key="i">
                      <Card :bordered="false" class="project-card">
                        <div style="display:flex">
                          <p slot="title">111</p>
                        </div>
                        <p>Content of no border type. Content of no border type. Content of no border type. Content of no border type.</p>
                        <div style="height:40px;display:flex;align-items:center;float:right">
                          <Button type="primary" @click="quitProject">Quit</Button>
                        </div>
                      </Card>
                    </Col>
                  </Row>
                </div>
              </Col>
            </Row>
          </TabPane>
          <TabPane label="Management Project" name="Management">
            <div
              v-for="(mProject,index) in userManagerProjectList"
              v-show="userManagerProjectList!='None'"
            >
              <Col span="11" offset="1">
                <Card style="min-height:200px;">
                  <p slot="title" class="projectsTitle" @click="goSingleProject(mProject.projectId)">{{mProject.title}}</p>
                  <Button
                    type="primary"
                    slot="extra"
                    style="margin:-5px 5px 0 5px"
                    @click="authorizeModalShow(index)"
                    icon="md-happy"
                  ></Button>
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
                  <Button
                    type="success"
                    slot="extra"
                    style="margin:-5px 5px 0 5px"
                    @click="editProjectModalShow(index)"
                    icon="ios-brush"
                    title="edit"
                  ></Button>
                  <Button
                    type="error"
                    slot="extra"
                    style="margin:-5px 5px 0 5px"
                    @click="deleteProjectModalShow(index)"
                    icon="md-close"
                    title="remove"
                  ></Button>
                  <Modal
                    v-model="editProjectModal"
                    title="Edit project info"
                    ok-text="submit"
                    cancel-text="cancel"
                    @on-ok="editProjectSubmit"
                    @on-cancel="cancel"
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
                        </div> -->
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
                        <RadioGroup style="margin-left:0.5%">
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
                  <p>{{mProject.description}}</p>
                  <p>{{mProject.introduction}}</p>
                  <hr>
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
        <div>
          <div class="user-contribution">
            <Row>
              <Col span="23" offset="1"></Col>
              <br>
              <Col span="10" offset="1" class="user-history">
                <Timeline style="margin-top:20px;margin-left:5%">
                  <TimelineItem>
                    <p class="time">2018-7-11</p>
                    <p class="content">加入项目组</p>
                  </TimelineItem>
                  <TimelineItem>
                    <p class="time">2018-7-13</p>
                    <p class="content">进入准备工作阶段，负责收集数据</p>
                  </TimelineItem>
                  <TimelineItem>
                    <p class="time">2018-7-15</p>
                    <p class="content">收集数据工作完成，并上传至数据库</p>
                  </TimelineItem>
                  <TimelineItem>
                    <p class="time">2018-7-20</p>
                    <p class="content">参与建模流程，负责概念模型的绘制</p>
                  </TimelineItem>
                </Timeline>
              </Col>
              <Col span="10" offset="1" class="user-data">
                <Table :columns="columns1" :data="data1"></Table>
                <Button type="primary" style="float:right;margin:20px">More</Button>
              </Col>
            </Row>
          </div>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
export default {
  created() {
    this.$store.commit("getStorage");
    // this.$store.state.avatar = "";
    this.getUserProfile();
    this.getManagerProjectList();
  },
  computed: {
    username() {
      return this.$store.state.Username;
    },
    avatar() {
      return this.$store.state.Avatar;
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
      columns1: [
        {
          title: "Name",
          key: "name"
        },
        {
          title: "Type",
          key: "type"
        },
        {
          title: "Date",
          key: "date"
        }
      ],
      data1: [
        {
          name: "01.png",
          type: "png",
          address: "New York No. 1 Lake Park",
          date: "2018-07-12"
        },
        {
          name: "description.docx",
          type: "file",
          address: "London No. 1 Lake Park",
          date: "2018-07-12"
        },
        {
          name: "conceptModel.xml",
          type: "xml file",
          address: "Sydney No. 1 Lake Park",
          date: "2018-07-21"
        },
        {
          name: "conceptModel.png",
          type: "png",
          address: "Ottawa No. 2 Lake Park",
          date: "2018-07-22"
        }
      ],
      msg: "Welcome to PersonalPage",
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
      editType:"",
      editTags:"",
      inputTag:"",
      editDescription: "",
      editTitle: "",
      editIntroduction: "",
      editPrivacy:"",
      editProjectId:"",
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
      visible:false,
      //抽屉开启状态控制
      // drawerClose:false,
    };
  },
  methods: {
    // 获取用户的详细信息
    getUserProfile() {
      this.axios
        .get(
          "http://localhost:8081/user/inquiry" +
            "?key=userId" +
            "&value=" +
            localStorage.getItem("userId")
        )
        .then(res => {
          this.userDetail = res.data;
          //打印用户的具体信息
          // console.log(this.userDetail);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //获取用户参与的所有项目列表
    getParticipatoryProjectList() {
      this.axios
        .post(url, {
          params: {
            id: paramId
          }
        })
        .then(res => {})
        .catch(err => {});
    },
    //获取用户可管理支配的全部项目列表
    getManagerProjectList() {
      this.axios
        .get(
          "http://localhost:8081/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            localStorage.getItem("userId")
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
      quitData.append("UserName", this.state.username);
      this.axios
        .post("http://localhost:8081/TeamModeling/QuitProjectServlet", quitData)
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
          "http://localhost:8081/user/remove?" +
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
      // this.authorizeProjectIndex = index;
      this.currentProjectId = this.userManagerProjectList[index].projectId;
      this.authorizeProjectModal = true;
      this.projectMemberList = this.userManagerProjectList[
        index
      ].members;
      // 打印项目成员的列表
      // console.log(this.projectMemberList);
    },
    authorize() {
      // let list = this.projectMemberList;
      // for (var i = 0; i < list.length; i++) {
      //   if (list[i].userName === this.selectManager) {
      //     this.selectManagerId = list[i].userId;
      //     this.currentProjectId = this.userManagerProjectList[
      //       this.authorizeProjectIndex
      //     ].projectId;
      //   }
      // }
      this.axios
        .get(
          "http://localhost:8081/project/manager?" +
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
    editProjectModalShow(index) {
      this.editProjectIndex = index;
      this.editProjectModal = true;
      let editProjectInfo = this.userManagerProjectList[this.editProjectIndex];
      console.log("当前编辑的项目的详情信息是:" + editProjectInfo.projectId);
      this.editTitle = editProjectInfo.title;
      this.editIntroduction = editProjectInfo.introduction;
      this.editDescription = editProjectInfo.description;
      this.editType = editProjectInfo.category;
      this.editTags = editProjectInfo.tag.split(',');
      this.editPrivacy = editProjectInfo.privacy;
      this.editProjectId = editProjectInfo.projectId;
    },

    cancel() {
      this.$Message.info("cancel");
    },
    //删除项目的函数
    deleteProjectModalShow(index) {
      this.DelelteProjectIndex = index;
      let selectProjectId = this.userManagerProjectList[
        this.DelelteProjectIndex
      ].projectId;
      this.axios
        .get(
          "http://localhost:8081/project/delete?" +
            "projectId=" +
            selectProjectId
        )
        .then(res => {
          if (res.data != "") {
            this.getManagerProjectList();
          }

          //   if(res.data==="Success"){
          //     this.$Message.info("Delete successfully");
          //   }else{
          //     this.$Message.info("Fail");
          //   }
        })
        .catch(err => {});
    },
    //编辑项目的函数
    editProjectSubmit() {
      // 将项目变更的信息进行提交
      let projectEditForm = new URLSearchParams();
      //做一个判断
      projectEditForm.append("title",this.editTitle);
      projectEditForm.append("category",this.editType);
      projectEditForm.append("introduction",this.editIntroduction);
      projectEditForm.append("description",this.editDescription);
      projectEditForm.append("tag",this.editTags);
      projectEditForm.append("privacy",this.editPrivacy);
      projectEditForm.append("projectId",this.editProjectId);
      this.axios.post("http://localhost:8081/project/update", projectEditForm)
      .then(res=>{
        console.log(res.data);
        this.getManagerProjectList();
      })
      .catch(err=> {
        console.log(err.data);
      })
    },
    //获取历史事件轴的函数
    getHistoryEvent() {
      // this.axios.get(url, {
      //   params: {
      //     id:paramId
      //   }
      // })
      // .then(function (response) {
      // })
      // .catch(function (error) {
      // })
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
          .post("http://localhost:8081/user/update", changedProfile)
          .then(res => {
            if (res.data === "Success") {
              // this.drawerClose = true;
              this.$Notice.success({
                    title: 'notification',
                    desc: 'Profile update successfully'
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
    deleteTag(index){
      this.editTags.splice(index, 1);
    },
    //点击跳转到指定项目的函数
    goSingleProject(id) {
      this.$router.push({name: 'ProjectDetail',params:{id:id} });
    },
    //获取个人上传的全部资源的函数
    getUserFile(){

    }
  }
};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
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
  height: 300px;
}
.editBtn {
  display: flex;
  align-items: center;
  height: 40px;
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
  text-align: left
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
.projectsTitle:hover{
  color:red;
  cursor: pointer;
}
</style>
