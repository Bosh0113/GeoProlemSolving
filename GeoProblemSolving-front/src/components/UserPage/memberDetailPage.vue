<template>
  <Row>
    <Col span="22" offset="1">
      <div class="mainPanel">
        <div class="detailSidebar2" :style="{height:detailSidebarHeight}">
          <div class="user-img">
            <img v-bind:src="userDetail.avatar" class="u_img">
          </div>
          <div style="text-align:center">
            <div class="single-info">{{userDetail.userName}}</div>
            <br>
          </div>
          <div class="user-desc" style="min-hieght:60px;padding:0 10px;border:1px dotted gray">
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
          </div>
        </div>
        <div class="rightContent">
        <Tabs value="Overview">
              <TabPane label="Overview" name="Overview">
                <Col :lg="{span:22,offset:1}" :md="{span:22,offset:1}" :sm="{span:22,offset:1}">
                  <Card>
                    <p slot="title">History Line</p>
                    <Timeline style="margin-top:20px;margin-left:5%">
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
                        style="height:200px;text-indent:2em;overflow-y:scroll"
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
                      <!-- 表头结束 -->
                      <p
                        style="height:200px;text-indent:2em;overflow-y:scroll"
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
      </div>

    </Col>
  </Row>
</template>
<style scoped>
.mainPanel {
  display: flex;
}
.detailSidebar2 {
  min-width: 250px;
  max-width: 300px;
  margin-right: 20px;
}
.rightContent {
  flex: 1;
}
img {
  width: 100%;
  height: auto;
}
h1 {
  font-weight: normal;
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
</style>
<script>
import Avatar from "vue-avatar";
export default {
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
      detailSidebarHeight: "",
      // 用户event列表
      userEventList: [],
      userResourceList: [],
      joinedProjectsNameArray: [],
      //加入的项目详情数组列表
      joinedProjectsList: [],
      userManagerProjectList: [],
    };
  },
  methods: {
    getMemberDetail() {
      this.axios
        .get(url, {
          params: {
            id: paramId
          }
        })
        .then(function(response) {})
        .catch(function(error) {});
    },
    getUserProfile() {
      this.axios
        .get(
          "/GeoProblemSolving/user/inquiry" +
            "?key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          this.userDetail = res.data;
          this.joinedProjectsNameArray = this.userDetail.joinedProjects;
          this.getParticipatoryList(this.joinedProjectsNameArray);
          //打印用户的具体信息
          console.log(this.userDetail);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    readPersonalEvent() {
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry?" +
            "key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          this.userEventList = res.data;
          console.table(result);
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
          this.userResourceList = res.data;
          console.table(this.userResourceList);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getManagerProjectList() {
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          // 打印用户所管理的项目
          // console.log(res.data);
          this.userManagerProjectList = res.data;
        })
        .catch(err => {});
    },
    getParticipatoryList(projectIds) {
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
  },
  mounted() {
    this.getUserProfile();
    this.getManagerProjectList();
    this.readPersonalEvent();
    this.getUserResource();
    this.detailSidebarHeight = window.innerHeight - 60 + "px";
  },
};
</script>

