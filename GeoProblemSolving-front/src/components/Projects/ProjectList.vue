<style scoped>
img {
  /* padding:10px; */
  max-width: 100%;
  max-height: 100%;
}
.whitespace {
  height: 20px;
}
.projectTitle {
  height: 40px;
  line-height: 40px;
  font-size: 20px;
  max-width: 200px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.operateBtnGroup {
  --btnSize: 15px;
}
.operateBtnGroup button {
  font-size: 15px;
  margin-left: 2.5%;
}
.operateBtnGroup button:hover {
  font-size: var(--btnSize);
}
.btnCreate:hover {
  background-color: #19be6b;
  color: white;
}
.btnJoin:hover {
  background-color: #57a3f3;
  color: white;
}
/* Loading动画的特效 */
.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}
</style>
<template>
  <!-- tab栏分页 -->
  <!-- 外层div -->
  <div>
    <Row>
      <Col span="22" offset="1">
        <div style="display:flex;height:60px;justify-content:center">
          <!-- <div style="width:70%;display:flex;justify-content:center;align-items:center">
            <div style="width:80%">
              <Input
                v-model="search"
                placeholder="Enter something to find project quickly"
                style="width: 80%"
              />
            </div>
          </div> -->
        </div>
        <div class="Tabpane" style="display:flex">
          <Tabs v-model="currentTab" @click.native="chooseCurrentType(currentTab)" style="width:60%">
            <TabPane label="Water" name="Water" icon="ios-water">
            </TabPane>
            <TabPane label="Soil" name="Soil" icon="md-grid">
            </TabPane>
            <TabPane label="Ecology" name="Ecology" icon="md-leaf"></TabPane>
            <TabPane label="Atmosphere" name="Atmosphere" icon="md-cloud"></TabPane>
            <TabPane label="Society" name="Society" icon="md-bus"></TabPane>
            <TabPane label="Others" name="Others" icon="md-globe"></TabPane>
          </Tabs>
          <div style="width:40%;display:flex;justify-content:flex-end">
            <Button
              @click="newProject"
              type="default"
              class="btnCreate"
              style="margin-right:2.5%;font-size:15px;height:40px"
              icon="md-add"
            >Create</Button>
            <!-- <Button
              type="default"
              style="margin-right:2.5%;font-size:15px;height:40px"
              icon="md-person-add"
              class="btnJoin"
              @click="joinModalShow"
            >Join</Button> -->

          </div>
        </div>
      </Col>
      <div class="ProjectList">
          <Spin fix  v-if="getFinish">
              <Icon type="ios-loading" size="100" class="demo-spin-icon-load" color="yellowgreen"></Icon>
              <div>Loading</div>
          </Spin>
          <div v-else>
            <div v-if="currentProjectList.length < 1">
              <Col span="22" offset="1">
                <Card :bordered="false">
                  <!-- <p slot="title">No Projects in this category</p> -->
                  <div style="display:flex;justify-content:center">
                    <Icon type="md-alert" size="40" color="gray"/>
                  </div>
                  <br>
                  <div style="display:flex;justify-content:center">
                    <h2 style="text-align:center;width:50%">No projects in this category.</h2>
                  </div>
                  <br>
                  <div style="display:flex;justify-content:center">
                    <h4
                      style="text-align:center;width:50%;color:lightblue"
                    >You can click the button right top called create to add a new project. Enriching your description of the project and attracting more people to join in.</h4>
                  </div>
                </Card>
              </Col>
            </div>
            <div
              v-for="(item,index) in filteredBlogs"
              :data="currentProjectList"
              :key="item.index"
              style="width:95%;margin-right:5%"
              v-else
            >
              <Col
                :xs="{ span: 21, offset: 1 }"
                :md="{ span: 11, offset: 1 }"
                :lg="{ span: 7,  offset: 1 }"
                v-if="item.privacy=='Public'"
              >
                <div @click="goSingleProject(item.projectId)" style="cursor:pointer">
                  <Card style="height:auto;margin:20px 0 20px 0">
                    <span slot="title" class="projectTitle">{{item.title}}</span>
                    <div
                      class="operate"
                      slot="extra"
                      style="height:40px;display:flex;align-items:center"
                    >
                      <Button
                        type="success"
                        v-show="!item.isMember&&!item.isManager&&UserState"
                        @click.stop="joinApply(item)"
                      >
                        <Icon type="md-add"/>
                      </Button>
                      <br>
                      <Icon
                        type="md-person"
                        :size="30"
                        v-show="item.isMember||item.isManager"
                        :id="item.projectId"
                      />
                    </div>
                    <div style="display:flex;align-items:center;height:60px">
                      <strong>Description</strong>
                      <span style="padding: 0 10px;word-break:break-word;overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            max-width: 400px;">
                      {{item.description}}</span>
                    </div>
                    <div style="height:300px;display:flex;justify-content:center">
                      <img :src="item.picture" v-if="item.picture!=''&&item.picture!='undefined'">
                      <avatar
                        :username="item.title"
                        :size="300"
                        :title="item.title"
                        :rounded="false"
                        v-else
                      ></avatar>
                    </div>
                    <div class="whitespace"></div>
                    <div style="height:30px;align-items:center;display:flex;justify-content:flex-start">

                      <Icon type="md-body" :size="20"/>Manager
                      <span style="height:20px;margin-left:5%">
                        <strong>{{item.managerName}}</strong>
                      </span>
                    </div>
                    <div style="height:30px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px">
                      <Icon type="md-clock" :size="20"/>Creation Time
                      <span style="height:20px;margin-left:5%">
                        <strong>{{item.createTime.split(' ')[0]}}</strong>
                      </span>
                    </div>
                    <div style="height:30px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px">
                      <Icon type="md-pricetags" :size="20"/>Tag
                      <span style="height:20px;margin-left:5%">
                        <strong v-for="tag in item.tag">{{tag}}</strong>
                      </span>
                    </div>

                    <!-- <Modal
                      v-model="joinModal"
                      title="Join in project"
                      ok-text="Submit"
                      cancel-text="Cancel"
                      @on-ok="joinProject()"
                      @on-cancel="cancel"
                    >
                      <div style="display:flex;align-items:center">
                        <span style="margin-right:5%">ProjectId:</span>
                        <input
                          v-model="joinProjectId"
                          placeholder="Enter ProjectId you want to participate ..."
                          style="width: 400px"
                        >
                      </div>
                    </Modal> -->
                    <Modal
                      v-model="quitModal"
                      title="Quit Project"
                      ok-text="Assure"
                      cancel-text="cancel"
                      @on-ok="quitProject()"
                      @on-cancel="cancel"
                    >
                      <p>Once you exit the project, you will not be able to participate in the collaborative process, confirm the exit?</p>
                    </Modal>
                  </Card>
                </div>
              </Col>
            </div>
          </div>
      </div>
    </Row>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  mounted() {
    // 作用是在一开始就到后台获取water资源类型的项目，作为默认值
    let initObject = { key: "category", value: "Water" };
    this.getSpecificTypeProjects(initObject);
  },
  computed: {
    //   filteredBlogs:function(){
    //   return this.blogs.filter((blog)=>{
    //     return blog.title.match(this.search);
    //   })
    // },
    filteredBlogs: function() {
      return this.currentProjectList.filter(item => {
        return item.title.match(this.search);
      });
    },
    UserState() {
      return this.$store.getters.userState;
    },
  },
  components: {
    Avatar
  },
  data() {
    return {
      //Tab栏当前选中的信息
      currentTab: "water",
      //上一次点击选中的类别
      justChooseTab: false,
      currentProjectList: [],
      isMember: false,
      isManager: false,
      // join按钮点击后模态框
      // joinModal: false,
      //quit按钮点击后弹出的模态框
      quitModal: false,
      //加入项目的Id号
      joinProjectId: "",
      //搜索的输入框
      search: "",
      // 记录已经申请的情况
      haveApplied: false,
      getFinish: false
    };
  },
  methods: {
    //该方法负责将选中的类别传递给显示的div
    chooseCurrentType(data) {
      if (data === this.justChooseTab) {
      } else {
        switch (data) {
          case "Water":
            let waterObject = { key: "category", value: "Water" };
            this.getSpecificTypeProjects(waterObject);
            this.justChooseTab = "Water";
            break;
          case "Soil":
            let soilObject = { key: "category", value: "Soil" };
            this.getSpecificTypeProjects(soilObject);
            this.justChooseTab = "Soil";
            break;
          case "Ecology":
            let ecologyObject = { key: "category", value: "Ecology" };
            this.getSpecificTypeProjects(ecologyObject);
            this.justChooseTab = "Ecology";
            break;
          case "Atmosphere":
            let atmosphereObject = { key: "category", value: "Atmosphere" };
            this.getSpecificTypeProjects(atmosphereObject);
            this.justChooseTab = "Atmosphere";
            break;
          case "Society":
            let societyObject = { key: "category", value: "Society" };
            this.getSpecificTypeProjects(societyObject);
            this.justChooseTab = "Society";
            break;
          case "Others":
            let othersObject = { key: "category", value: "Others" };
            this.getSpecificTypeProjects(othersObject);
            this.justChooseTab = "Others";
            break;
        }
      }
    },
    getSpecificTypeProjects(data) {
      //显示等待
      this.getFinish = true;
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiry" +
            "?key=" +
            data["key"] +
            "&value=" +
            data["value"]
        )
        .then(res => {
          //结束等待
          this.getFinish = false;
          if (res.data === "None") {
            this.currentProjectList = [];
          } else {
            this.judgeMember(res.data);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      return this.currentProjectList;
    },
    newProject() {
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "NewProject" });
      }
    },
    //项目成员的id和name都要加进去
    // joinModalShow() {
    //   if (this.$store.getters.userId != "") {
    //     this.joinModal = true;
    //   } else {
    //     this.$Message.info("please login.");
    //   }
    // },
    // joinProject() {
    //   var projectId = this.joinProjectId;
    //   this.axios
    //     .get(
    //       "/GeoProblemSolving/project/join?" +
    //         "projectId=" +
    //         projectId +
    //         "&userId=" +
    //         this.$store.getters.userId
    //     )
    //     .then(res => {
    //       if (res.data === "Success") {
    //         this.$Message.info("Join Success");
    //         //跳转
    //         this.$router.push({ path: `project/${projectId}` });
    //       } else if (res.data === "Exist") {
    //         this.$Message.info("you have already be a member in project");
    //       }
    //     })
    //     .catch(err => {
    //       this.$Message.danger("Join fail");
    //     });
    // },
    showJoinApplyBtn(item) {
      var state = this.$store.getters.userState;
      if (!item.isMember && !item.isManager && state) {
        return true;
      } else {
        return false;
      }
    },
    cancel() {
      this.$Message.info("Clicked cancel");
    },
    joinedRefresh() {
      let projectList = this.currentProjectList;
      let length = this.currentProjectList.length;
      for (var i = 0; i < length; i++) {
        if (projectList[i].projectId === this.joinProjectId) {
          this.currentProjectList[i]["isMember"] = true;
          break;
        }
      }
    },
    // 判断是不是成员
    judgeMember(list) {
      //这样的话拿到了用户的id与name
      let projectList = list;
      if (projectList.length != 0) {
        for (var i = 0; i < projectList.length; i++) {
          let managerId = projectList[i].managerId;
          let members = projectList[i].members;
          if (managerId == this.$store.getters.userId) {
            projectList[i]["isManager"] = true;
          } else {
            projectList[i]["isManager"] = false;
          }
          if (members.length != 0) {
            for (var j = 0; j < members.length; j++) {
              if (members[j].userId == this.$store.getters.userId) {
                projectList[i]["isMember"] = true;
                break;
              } else {
                projectList[i]["isMember"] = false;
              }
            }
          } else {
            projectList[i]["isMember"] = false;
          }
        }
        this.$set(this, "currentProjectList", projectList);
      }
    },
    //进入项目详情页面的函数
    goSingleProject(id) {
      let isManager, isMember;
      for (let i = 0; i < this.currentProjectList.length; i++) {
        if (this.currentProjectList[i]["projectId"] === id) {
          let projectInfo = this.currentProjectList[i];
          isManager = projectInfo["isManager"];
          isMember = projectInfo["isMember"];
          this.$store.commit("setProjectInfo", projectInfo);
          break;
        }
      }
      if (this.$store.getters.userState) {
        if (isManager || isMember) {
          this.$router.push({ path: `project/${id}` });
        } else {
          this.$Notice.error({
            title: "No access",
            desc:
              "You need to click + button at the north right corner to apply join in the project",
            duration: 5
          });
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    joinApply(data) {
      if (
        this.haveApplied == true &&
        data.projectId == sessionStorage.getItem("applyId")
      ) {
        this.$Notice.warning({
          title: "repeat apply warning",
          desc: "You have apply success, no need to click again!"
        });
      } else {
        if (this.$store.getters.userState) {
          let joinForm = {};
          sessionStorage.setItem("applyId", data.projectId);
          joinForm["recipientId"] = data.managerId;
          joinForm["type"] = "apply";
          joinForm["content"] = {
            userName: this.$store.getters.userName,
            userId: this.$store.getters.userId,
            title: "Group application",
            description:
              "User " +
              this.$store.getters.userName +
              " apply to join in your project: " +
              data.title +
              " .",
            projectId: data.projectId,
            projectTitle: data.title,
            scope: "project",
            approve: "unknow"
          };
          this.axios
            .post("/GeoProblemSolving/notice/save", joinForm)
            .then(res => {
              this.$Notice.open({
                title: "Apply Successfully",
                desc:
                  "The project's manager will process your apply in time,you can get a notification later to tell you the result.",
                duration: 5
              });
              this.$emit("sendNotice", data.managerId);
              this.haveApplied = true;
            })
            .catch(err => {
              console.log("申请失败的原因是：" + err.data);
            });
        } else {
          this.$Message.error("you must have an account before you apply");
          this.$router.push({ name: "Login" });
        }
      }
    }
  }
};
</script>

