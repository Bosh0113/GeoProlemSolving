<style>
img {
  /* padding:10px; */
  max-width: 100%;
  max-height: 100%;
}
.whitespace {
  height: 20px;
}
/* title标题悬浮时出现下划线且变色 */
.projectTitle:hover {
  text-decoration: underline;
  color: #c20c0c;
  cursor: pointer;
}
operateBtnGroup {
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
        <div class="topPanel" style="margin-top:50px">
          <div style="width:80%">
            <Input
              v-model="search"
              placeholder="Enter something to find project quickly"
              style="width: 100%"
            />
          </div>
          <!-- <div class="topPanel" style="margin-top:50px;float:right;display:flex"> -->
          <div
            style="float:right;width:20%;magrin-left:80%;display:flex;justify-content:center"
            class="operateBtnGroup"
          >
            <Button
              router-link
              :to="{path:'newproject'}"
              type="default"
              class="btnCreate"
              style="margin-right:2.5%;font-size:15px"
              icon="md-add"
            >Create</Button>
            <Button
              type="default"
              style="margin-right:2.5%;font-size:15px"
              icon="md-person-add"
              class="btnJoin"
              @click="joinModal=true"
            >Join</Button>
          </div>

          <!-- </div> -->
        </div>
        <br>
        <div class="Tabpane">
          <Tabs v-model="currentTab" @click.native="chooseCurrentType(currentTab)">
            <TabPane label="Water" name="Water" icon="ios-water"></TabPane>
            <TabPane label="Soil" name="Soil" icon="md-grid"></TabPane>
            <TabPane label="Ecology" name="Ecology" icon="md-leaf"></TabPane>
            <TabPane label="Atmosphere" name="Atmosphere" icon="md-cloud"></TabPane>
            <TabPane label="Society" name="Society" icon="md-bus"></TabPane>
            <TabPane label="Others" name="Others" icon="md-globe"></TabPane>
          </Tabs>
        </div>
      </Col>
      <div class="ProjectList">
        <div v-show="currentProjectList==''&&currentProjectsStatus==2">
          <Col class="demo-spin-col" span="22" offset="1">
            <Spin fix>
              <Icon type="ios-loading" size="100" class="demo-spin-icon-load" color="lightblue"></Icon>
              <div>Loading</div>
            </Spin>
          </Col>
        </div>
        <div v-show="currentProjectsStatus==0">
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
        <div v-for="(item,index) in filteredBlogs" :data="currentProjectList" :key="item.index">
          <Col
            :xs="{ span: 21, offset: 1 }"
            :md="{ span: 11, offset: 1 }"
            :lg="{ span: 7, offset: 1 }"
            v-if="item.privacy=='Public'"
          >
            <Card style="height:auto;margin:20px 0 20px 0">
              <h2
                slot="title"
                @click="goSingleProject(item.projectId)"
                class="projectTitle"
                style="height:40px;line-height:40px;font-size:20px;width:80%"
              >{{item.title}}</p>
              <div
                class="operate"
                slot="extra"
                style="height:40px;display:flex;align-items:center"
              >
                <Button
                  type="success"
                  v-show="item.isMember===false&&item.isManager===false"
                  @click="joinApply(item)"
                >
                  <Icon type="md-add"/>
                </Button>
                <br>
                <Button
                  type="default"
                  v-show="item.isMember===true||item.isManager===true"
                  :id="item.projectId"
                >
                <Icon type="md-person" />
                </Button>
              </div>

              <div style="display:flex;align-items:center;height:60px">
                <Tag color="primary">Description</Tag>
                <p style="padding: 0 10px">{{item.description}}</p>
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
              <div style="height:40px;align-items:center;display:flex;padding:0 20px 0 20px">
                <span style="height:20px;width:45%;color:white;text-align:left;">
                  <Tag color="primary" style="width:78.2px;text-align:center">Creator</Tag>
                </span>
                <span style="height:20px;margin-left:5%">{{item.creator}}</span>
              </div>
              <div style="height:40px;align-items:center;display:flex;padding:0 20px 0 20px">
                <span style="height:20px;width:45%;color:white;text-align:left;">
                  <Tag color="primary">Create time</Tag>
                </span>
                <span style="height:20px;margin-left:5%">{{item.createTime.split(' ')[0]}}</span>
              </div>
              <div class="whitespace"></div>
              <Modal
                v-model="joinModal"
                title="Join in project"
                ok-text="Submit"
                cancel-text="Cancel"
                @on-ok="joinProject(currentTab)"
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
              </Modal>
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
          </Col>
        </div>
      </div>
    </Row>
  </div>
</template>
<style>
img {
  max-width: 100%;
  max-height: 100%;
}
.whitespace {
  height: 20px;
}
/* title标题悬浮时出现下划线且变色 */
.projectTitle:hover {
  cursor: pointer;
}
.operate button:hover{
  cursor:default;
}
operateBtnGroup {
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
<script>
import Avatar from "vue-avatar";
export default {
  created() {
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
    }
  },
  mounted() {},
  components: {
    Avatar
  },
  data() {
    return {
      ecologyProjectList: [],
      atmosphereProjectList: [],
      societyProjectList: [],
      othersProjectList: [],
      //Tab栏当前选中的信息
      currentTab: "water",
      currentProjectList: [],
      quitShow: false,
      joinShow: false,
      isMember: false,
      isManager: false,
      // join按钮点击后模态框
      joinModal: false,
      //quit按钮点击后弹出的模态框
      quitModal: false,
      //加入项目的Id号
      joinProjectId: "",
      currentProject: {},
      quitSubProject: {},
      //搜索的输入框
      search: "",
      currentProjectsStatus: 2
    };
  },
  methods: {
    //该方法负责将选中的类别传递给显示的div
    chooseCurrentType(data) {
      switch (data) {
        case "Water":
          let waterObject = { key: "category", value: "Water" };
          this.getSpecificTypeProjects(waterObject);
          break;
        case "Soil":
          let soilObject = { key: "category", value: "Soil" };
          this.getSpecificTypeProjects(soilObject);
          break;
        case "Ecology":
          let ecologyObject = { key: "category", value: "Ecology" };
          this.getSpecificTypeProjects(ecologyObject);
          break;
        case "Atmosphere":
          let atmosphereObject = { key: "category", value: "Atmosphere" };
          this.getSpecificTypeProjects(atmosphereObject);
          break;
        case "Society":
          let societyObject = { key: "category", value: "Society" };
          this.getSpecificTypeProjects(societyObject);
          break;
        case "Others":
          let othersObject = { key: "category", value: "Others" };
          this.getSpecificTypeProjects(othersObject);
          break;
      }
    },
    getSpecificTypeProjects(data) {
      this.axios
        .get(
          "/api/project/inquiry" +
            "?key=" +
            data["key"] +
            "&value=" +
            data["value"]
        )
        .then(res => {
          if (res.data === "None") {
            this.currentProjectList = [];
            this.currentProjectsStatus = 0;
            // this.$Message.info("There are no projects in this category");
          } else {
            let list = res.data;
            this.judgeMember(list);
            this.currentProjectsStatus = 1;
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      return this.currentProjectList;
    },
    //项目成员的id和name都要加进去
    joinProject(tab) {
      this.axios
        .get(
          "/api/project/join?" +
            "projectId=" +
            this.joinProjectId +
            "&userId=" +
            this.$store.state.userId
        )
        .then(res => {
          if (res.data === "Success") {
            this.$Message.info("Join success");
            let initObject = { key: "category", value: "Water" };
            this.getSpecificTypeProjects(initObject);
          } else if (res.data === "Exist") {
            this.$Message.info("you have already be a member in project");
            var that = this;
            let initObject = { key: "category", value: "Water" };
            that.getSpecificTypeProjects(initObject);
            for (var i = 0; i < that.currentProjectList.length; i++) {
              if (that.currentProjectList[i].projectId === that.joinProjectId) {
                //把这个作为参数传递过去
                that.currentProject = that.currentProjectList[i];
                let obj = that.currentProjectList[i].members;
                this.joinRequest(obj);
              }
            }
          }
        })
        .catch(err => {
          this.$Message.danger("Join fail");
        });
    },

    cancel() {
      this.$Message.info("Clicked cancel");
    },
    joinRequest(data) {
      for (var i = 0; i < data.length; i++) {
        if (data[i].userId === this.$store.state.userId) {
          this.currentProject["isMember"] = true;
        }
      }
    },
    // 判断是不是成员
    judgeMember(list) {
      //这样的话拿到了用户的id与name
      let projectList = list;
      if (projectList.length != 0) {
        for (var i = 0, n = 0; i < projectList.length; i++) {
          $.ajax({
            url:
              "/api/user/inquiry" +
              "?key=" +
              "userId" +
              "&value=" +
              projectList[i]["managerId"],
            type: "GET",
            async: false,
            success: data => {
              projectList[n++]["creator"] = data.userName;
            }
          });
          let managerId = projectList[i].managerId;
          let members = projectList[i].members;
          if (managerId == this.$store.state.userId) {
            projectList[i]["isManager"] = true;
          } else {
            projectList[i]["isManager"] = false;
          }
          if (members.length != 0) {
            for (var j = 0; j < members.length; j++) {
              if (members[j].userId == this.$store.state.userId) {
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
          isManager = this.currentProjectList[i]["isManager"];
          isMember = this.currentProjectList[i]["isMember"];
        }
      }
      if (this.$store.getters.userState) {
        if (isManager || isMember) {
          this.$router.push({ path: `project/${id}` });
        } else {
          alert("No access.");
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    joinApply(data) {
      let joinForm = {};
      joinForm["recipientId"] = data.managerId;
      joinForm["type"] = "apply";
      joinForm["content"] = {
        userName: this.$store.state.userName,
        userId: this.$store.state.userId,
        title: "Group application",
        description:
          "User " +
          this.$store.state.userName +
          " apply to join in your project: " +
          data.title +
          " .",
        projectId: data.projectId,
        projectTitle: data.title,
        approve: "unknow"
      };
      this.axios
        .post("/api/notice/save", joinForm)
        .then(res => {
          this.$Message.info("Apply Successfully");
          this.$emit("sendNotice", data.managerId);
        })
        .catch(err => {
          console.log("申请失败的原因是：" + err.data);
        });
    }
  }
};
</script>

