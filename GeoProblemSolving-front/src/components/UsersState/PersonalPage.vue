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
            <Tag color="primary" class="username">{{userDetail.userName}}</Tag>
          </div>
          <div class="user-desc">
            <Input type="textarea" :rows="4" :value="this.userDetail.introduction"/>
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
              <span>country:</span>
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
              <Drawer title="Basic Drawer" placement="left" :closable="false" v-model="editProfileModal" width="450px">
                  <p>Some contents...</p>
                  <p>Some contents...</p>
                  <p>Some contents...</p>
              </Drawer>
              <Button type="error" style="height:40px;float:right" @click="logOutModalShow()">logOut</Button>
            </div>

            <Modal
              v-model="logOutProfileModal"
              title="Logout Warning"
              @on-ok="logOutAccount()"
              @on-cancel="cancel"
              logOutAccount-text = "assure"
              cancel-text="cancel"
            >
              <p style="font-size:20px">If you choose logout your account,the personal information you have filled will be removed.</p>
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
            <div v-for="(mProject,index) in userManagerProjectList" v-show="userManagerProjectList!='None'">
              <Col span="11" offset="1">
                <Card style="min-height:200px;">
                  <p slot="title">{{mProject.title}}</p>
                  <Button type="primary" slot="extra" style="margin:-5px 5px 0 5px" @click="authorizeModalShow(index)">Authorize</Button>
                  <Modal
                    v-model="authorizeProjectModal"
                    @on-ok="authorize()"
                    @on-cancel="cancel()"
                    ok-text="assure"
                    cancel-text="cancel"
                    >
                    <p style="slot">Hand the project to others</p>
                    <div>
                      <RadioGroup v-model="selectManager">
                        <Radio v-for="(member,index) in projectMemberList" :key="member.index" :label="member.userName" :value="member.userId"></Radio>
                        <!-- <Radio label="金斑蝶" disabled></Radio>
                        <Radio label="爪哇犀牛"></Radio>
                        <Radio label="印度黑羚"></Radio> -->
                    </RadioGroup>
                      <!-- 用radio将用户表示出来 -->
                      <!-- <tag v-for="(member,index) in projectMemberList" @click="choose(index)" :key="member.index">{{member.userName}}</tag> -->
                    </div>
                  </Modal>
                  <Button type="success" slot="extra" style="margin:-5px 5px 0 5px" @click="editProjectModalShow(index)">Edit</Button>
                  <Button type="error" slot="extra" style="margin:-5px 5px 0 5px" @click="deleteProjectModalShow(index)">Remove</Button>
                  <Modal
                      v-model="editProjectModal"
                      title="Edit project info"
                      @on-ok="editProject"
                      @on-cancel="cancel"
                      width="600px"
                  >
                    <div style="flex">
                      <!-- <span>Category</span> -->

                      <div class="editStyle">
                        <span>Category</span>
                        <RadioGroup  style="margin-left:5%;width:450px">
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
                        <Input v-model="editTitle" placeholder="Enter something..." style="margin-left:5%;width: 400px" />
                      </div>

                      <div class="editStyle">
                        <span>Description</span>
                        <Input v-model="editDescription" placeholder="Enter something..." style="margin-left:5%;width: 400px" />
                      </div>
                      <div class="editStyle">
                        <span>Introduction</span>
                        <Input v-model="editIntroduction" type="textarea" placeholder="Enter something..." style="margin-left:5%;width: 400px" :rows="4"/>
                      </div>
                      <div class="editStyle">
                        <span>Tag</span>
                        <Input
                          placeholder="Enter some tag to introduce the project"
                          style="margin-left:5%;width: 200px"
                        />
                        <Button
                          icon="ios-add"
                          type="dashed"
                          size="small"
                          style="margin-left:2.5%"
                        >Add Tag</Button>
                        <div style="margin-left:5%">
                          <Tag
                            color="primary"
                            @on-close="deleteTag(index)"
                          >{{item}}</Tag>
                        </div>
                      </div>
                      <div class="editStyle">
                        <span>Privacy</span>
                        <RadioGroup style="margin-left:5%;width:450px">
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
      userDetail: {},
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
      editProjectModal:false,
      authorizeProjectModal:false,
      authorizeProjectIndex:'',
      DelelteProjectIndex:'',
      editProjectIndex:'',
      //编辑项目专用的字段
      editDescription:"",
      editTitle:"",
      editIntroduction:"",
      item:"",
      projectMemberList:[],
      selectManager:this.$store.state.userName,
      selectManagerId:this.$store.state.userId,
      currentProjectId:"",

    };
  },
  methods: {
    sendEmail() {
      var emailData = new URLSearchParams();
      emailData.append("email", "nnuxdw@163.com");
      emailData.append("mailTitle", "opengms-invitation-card");
      emailData.append(
        "mailContent",
        "this is my website , welcome to join us"
      );
      this.axios
        .post("http://localhost:8082/Team/sendEmailServlet", emailData)
        .then(res => {
          // console.log(res.data);
          // alert(res.data["result"]);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
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
        })
        .catch(err => {
          console.log(err.data);
        });
    },
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
    getManagerProjectList() {
      this.axios
        .get(
          "http://localhost:8081/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            localStorage.getItem("userId")
        )
        .then(res => {
          console.log(res.data);
          this.userManagerProjectList = res.data;
        })
        .catch(err => {});
    },

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
    editProfile() {
      //再填写一个表单，里面获取到注册信息的全部内容
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
          console.log(res.data);
          this.$router.push({ name: "Home" });
          // this.$router.router.push()
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    authorizeModalShow(index){
      this.authorizeProjectIndex = index;
      this.authorizeProjectModal = true;
      this.projectMemberList = this.userManagerProjectList[this.authorizeProjectIndex].members;
      console.log(this.projectMemberList);
    },
    authorize(){
      let list = this.projectMemberList;
      for(var i=0;i<list.length;i++){
        if(list[i].userName === this.selectManager){
          this.selectManagerId = list[i].userId;
          this.currentProjectId = this.userManagerProjectList[this.authorizeProjectIndex].projectId
        }
      }
      this.axios.get("http://localhost:8081/project/manager?"+"projectId=" + this.currentProjectId +"&newManagerId=" + this.selectManagerId)
      .then(res=> {
        console.log(res.data);
      })
      .catch(err=>{
      })
    },
    editProjectModalShow(index){
      this.editProjectIndex = index;
      this.editProjectModal = true;
      let editProjectInfo = this.userManagerProjectList[this.editProjectIndex];
    },

    cancel(){
      this.$Message.info("cancel");
    },
    //删除项目的函数
    deleteProjectModalShow(index){
      this.DelelteProjectIndex = index;
      let selectProjectId = this.userManagerProjectList[this.DelelteProjectIndex].projectId;
      this.axios.get("http://localhost:8081/project/delete?"+"projectId="+ selectProjectId)
      .then(res=> {
        if(res.data!=""){
           this.getManagerProjectList();
        }

      //   if(res.data==="Success"){
      //     this.$Message.info("Delete successfully");
      //   }else{
      //     this.$Message.info("Fail");
      //   }
       }
      )
      .catch(err=>{
      })
    },
    editProject(){
    },
    //获取历史事件轴的函数
    getHistoryEvent(){
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
    changeProfile(){

    },
    editModalShow(){
      this.editProfileModal = true;
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
  /* height: 650px; */
}
.username {
  /* width: 20%; */
  /* margin-left: 40%; */
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
  /* height: 650px; */
  /* background-color:lightgray; */
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
  /* justify-content: center; */
  /* text-align:center; */
  height: 40px;
}
/* .logOutBtn{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
} */
/* 表示空格间距的 */
.whitespace {
  height: 20px;
}
.editStyle{
  display: flex;
  align-items:center;
  margin-top:5px;
  /* justify-content: center; */
}
.editStyle span{
  width:20%;
}
</style>
