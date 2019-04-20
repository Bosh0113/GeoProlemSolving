<style>
  /* .ivu-card-head{
    height:55px !important;
    padding:10px !important;
    display:flex;
    align-items:center
  } */
</style>
<style scoped>
img {
  width: 100%;
  height:auto;
}
.whitespace {
  height: 20px;
}
.projectTitle {
  height: 30px;
  line-height: 30px;
  font-size: 20px;
  max-width: 200px;
  padding-left:5px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.btnCreate:hover,.joinProjectBtn:hover {
  background-color: #19be6b;
  color: white;
}
.joinProjectBtn{
  /* margin-top:-4px; */
}
/* Loading动画的特效 */
.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}
</style>
<template>
  <!-- tab栏分页 -->
  <!-- 外层div -->

    <Row>
      <Col span="22" offset="1">
        <Spin fix v-show="getFinish">
          <Icon type="ios-loading" size="100" class="demo-spin-icon-load" color="yellowgreen"></Icon>
          <div>Loading</div>
        </Spin>
        <div style="display:flex;height:60px;justify-content:center"></div>
        <div class="Tabpane" style="display:flex">
          <Tabs v-model="currentTab" @click.native="chooseCurrentType(currentTab)">
            <TabPane label="All" name="All" icon="ios-list"></TabPane>
            <TabPane label="Terrestrial System" name="Terrestrial System" icon="md-globe"></TabPane>
            <TabPane label="Coastal System" name="Coastal System" icon="ios-boat"></TabPane>
            <TabPane label="Marine System" name="Marine System" icon="ios-water"></TabPane>
            <TabPane label="Climate System" name="Climate System" icon="ios-partly-sunny"></TabPane>
            <TabPane label="Ecological System" name="Ecological System" icon="ios-leaf"></TabPane>
            <TabPane label="Geological System" name="Geological System" icon="ios-analytics"></TabPane>
            <TabPane label="Human-Activity" name="Human-Activity" icon="ios-people"></TabPane>
            <TabPane label="GIS & RS" name="GIS & RS" icon="ios-globe"></TabPane>
            <TabPane label="General" name="General" icon="ios-grid"></TabPane>
          </Tabs>
          <div style="width:40%;display:flex;justify-content:flex-end">
            <Button
              @click="newProject"
              type="default"
              class="btnCreate"
              style="margin-right:2.5%;font-size:15px;height:40px"
              icon="md-add"
            >Create</Button>
          </div>
        </div>
      </Col>
      <div class="ProjectList">
        <div v-show="!getFinish">
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
              :lg="{ span: 5,  offset: 1 }"
              v-if="item.privacy=='Public'"
            >
              <div @click="goSingleProject(item.projectId)" style="cursor:pointer">
                <Card style="height:auto;margin:20px 0 20px 0">
                  <span slot="title" class="projectTitle">{{item.title}}</span>
                  <div
                    class="operate"
                    slot="extra"
                    style="display:flex;align-items:center"
                  >
                      <Button
                        class="joinProjectBtn"
                        type="default"
                        title="join in project"
                        v-show="!item.isMember&&!item.isManager&&UserState"
                        @click.stop="joinApplyModalShow(item)"
                      >
                        <Icon type="md-add" :size="20"/>
                      </Button>
                      <br>
                      <Icon
                        type="md-person"
                        :size="20"
                        v-show="item.isMember||item.isManager"
                        :id="item.projectId"
                      />
                    </div>
                    <div style="display:flex;align-items:center;height:20px">
                      <strong style="text-align: center"><Poptip trigger="hover" :content="item.description" placement="bottom-right" width="250" word-wrap  >Description</Poptip></strong>
                        <p style="padding: 0 10px;word-break:break-word;overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            max-width: 400px;">
                      {{item.description}}</p>
                    </div>
                    <div style="height:200px;display:flex;justify-content:center;margin-top:10px">
                      <img :src="item.picture" v-if="item.picture!=''&&item.picture!='undefined'">
                      <avatar
                        :username="item.title"
                        :size="150"
                        :title="item.title"
                        :rounded="false"
                        v-else
                      ></avatar>
                    </div>
                    <div style="height:15px;margin-top:10px;align-items:center;display:flex;justify-content:flex-start">
                      <Icon type="md-body" :size="15"/>Manager
                      <span style="height:20px;margin-left:5%">
                        <strong>{{item.managerName}}</strong>
                      </span>
                    </div>
                    <div style="height:15px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px">
                      <Icon type="md-clock" :size="15"/>Time
                      <span style="height:20px;margin-left:5%">
                        <strong>{{item.createTime.split(' ')[0]}}</strong>
                      </span>
                    </div>
                    <div style="height:15px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px">
                      <Icon type="md-pricetags" :size="15"/>Tags
                      <span style="height:20px;margin-left:5%">
                        <strong v-for="tag in item.tag">{{tag}}</strong>
                      </span>
                    </div>
                  </Card>
                </div>
              </Col>
            </div>
            <Modal
              v-model="applyJoinModal"
              title="Apply to join the project"
              ok-text="Apply"
              cancel-text="Cancel"
              @on-ok="joinApply('applyValidate')"
              @on-cancel=""
            >
              <Form ref="applyValidate" :model="applyValidate" :rules="applyRuleValidate" :label-width="80">
                <FormItem label="Reason" prop="reason">
                  <Input v-model="applyValidate.reason" type="textarea" :rows="4" placeholder="Enter The Reason For Application ..." />
                </FormItem>
              </Form>
            </Modal>
          </div>
        </div>
      </div>
    </Row>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  mounted() {
    // 作用是在一开始就到后台获取water资源类型的项目，作为默认值
    this.getAllProjects();
  },
  computed: {
    filteredBlogs: function() {
      return this.currentProjectList.filter(item => {
        return item.title.match(this.search);
      });
    },
    UserState() {
      return this.$store.getters.userState;
    }
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
      //加入项目
      applyProjectInfo: {},
      applyValidate: {
        reason: ""
      },
      applyRuleValidate: {
        reason: [
          {
            required: true,
            message: "Please enter the reason for application",
            trigger: "blur"
          },
          {
            type: "string",
            min: 5,
            message: "Introduce no less than 5 words",
            trigger: "blur"
          }
        ]
      },
      applyJoinModal: false,
      //搜索的输入框
      search: "",
      // 记录已经申请的情况
      haveApplied: false,
      getFinish: false,
      // 申请加入项目的模态框
      applyJoinModal: false
    };
  },
  methods: {
    //该方法负责将选中的类别传递给显示的div
    chooseCurrentType(data) {
      if (data === this.justChooseTab) {
      } else {
        switch (data) {
          case "All":
            this.justChooseTab = "All";
            this.getAllProjects();
            break;
          case "Terrestrial System":
            let Terrestrial = { key: "category", value: "Terrestrial System" };
            this.getSpecificTypeProjects(Terrestrial);
            this.justChooseTab = "Terrestrial System";
            break;
          case "Coastal System":
            let Coastal = { key: "category", value: "Coastal System" };
            this.getSpecificTypeProjects(Coastal);
            this.justChooseTab = "Coastal System";
            break;
          case "Marine System":
            let Marine = { key: "category", value: "Marine System" };
            this.getSpecificTypeProjects(Marine);
            this.justChooseTab = "Marine System";
            break;
          case "Climate System":
            let Climate = { key: "category", value: "Climate System" };
            this.getSpecificTypeProjects(Climate);
            this.justChooseTab = "Climate System";
            break;
          case "Ecological System":
            let Ecological = { key: "category", value: "Ecological System" };
            this.getSpecificTypeProjects(Ecological);
            this.justChooseTab = "Ecological System";
            break;
          case "Geological System":
            let Geological = { key: "category", value: "Geological System" };
            this.getSpecificTypeProjects(Geological);
            this.justChooseTab = "Geological System";
            break;
          case "Human-Activity":
            let Human = { key: "category", value: "Human-Activity" };
            this.getSpecificTypeProjects(Human);
            this.justChooseTab = "Human-Activity";
            break;
          case "GIS & RS":
            let GIS = { key: "category", value: "GIS & RS" };
            this.getSpecificTypeProjects(GIS);
            this.justChooseTab = "GIS & RS";
            break;
          case "General":
            let General = { key: "category", value: "General" };
            this.getSpecificTypeProjects(General);
            this.justChooseTab = "General";
            break;
        }
      }
    },
    getAllProjects(){
      this.getFinish = true;
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiryAll")
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
    },
    getSpecificTypeProjects(data) {
      //显示等待
      this.getFinish = true;
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiryByType" +
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
    },
    newProject() {
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "NewProject" });
      }
    },
    showJoinApplyBtn(item) {
      var state = this.$store.getters.userState;
      if (!item.isMember && !item.isManager && state) {
        return true;
      } else {
        return false;
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {
      this.$Message.info("Clicked cancel");
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
              "You need to click + button at the north right corner to apply join the project",
            duration: 5
          });
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    joinApplyModalShow(applyProjectInfo) {
      this.$set(this, "applyProjectInfo", applyProjectInfo);
      this.applyValidate.reason = "";
      this.applyJoinModal = true;
    },
    joinApply(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var data = this.applyProjectInfo;
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
                  " apply to join your project: " +
                  data.title +
                  " ." +
                  " The reason for application is: " +
                  this.applyValidate.reason,
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

              let emailObject = {
                recipient: joinForm.recipientId,
                mailTitle:  "Group application",
                mailContent: joinForm.content.description+'<br>'
              };
              this.axios
              .post("/GeoProblemSolving/project/applyByMail",emailObject)
              .then(res=>{
                if(res.data=="Success"){
                  console.log("Email Success.");
                }else{
                  console.log("Email fail.");
                }
              })
              .catch(err=>{
                console.log("Email fail.");
              })
            } else {
              this.$Message.error("you must have an account before you apply");
              this.$router.push({ name: "Login" });
            }
          }
        } else {
          this.$Message.error("Fail!");
        }
      });
    }
  }
};
</script>

