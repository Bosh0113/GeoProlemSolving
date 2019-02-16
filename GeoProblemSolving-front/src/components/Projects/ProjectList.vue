<template>
  <!-- tab栏分页 -->
  <!-- 外层div -->
  <div>
    <Row>
      <Col span="22" offset="1">
        <div class="topPanel" style="margin-top:50px;float:right;display:flex">
          <Button
            router-link
            :to="{path:'newproject'}"
            type="success"
            style="margin-right:2.5%"
            title="create"
            icon="md-add"

          >Create</Button>
           <Button
            type="primary"
            style="margin-right:2.5%"
            title="join"
            icon="md-person-add"
            @click="joinModal=true"
          >Join</Button>
        </div>
        <div class="topPanel" style="margin-top:50px;">
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
        <div v-for="(item,index) in currentProjectList" :data="currentProjectList" :key="item.index">
          <!-- Card卡片用来承载工程的信息，包含title，img，以及一些基本信息 -->
          <Col span="6" offset="1" v-if="item.privacy=='Public'">
            <Card style="height:620px;margin:20px 0 20px 0">
              <h1 style="text-align:center" @click="goSingleProject(item.projectId)">{{item.title}}</h1>
              <p style="height:auto;padding:0 40px 0 40px">{{item.description}}</p>
              <div style="height:300px;display:flex;justify-content:center">
                <img :src="item.picture" v-if="item.picture!=''&&item.picture!='undefined'">
                <avatar :username="item.title" :size="300" :title="item.title" :rounded="false" v-else></avatar>
              </div>
              <div class="whitespace"></div>
              <div style="height:40px;align-items:center;display:flex;padding:0 20px 0 20px">
                <span
                  style="height:20px;width:45%;color:white;text-align:center;"
                ><Tag color="primary">Creater</Tag></span>
                <span style="height:20px;margin-left:5%">{{item.creator}}</span>
              </div>
              <div style="height:40px;align-items:center;display:flex;padding:0 20px 0 20px">
                <span
                  style="height:20px;width:45%;color:white;text-align:center;"
                ><Tag color="primary">Create time</Tag></span>
                <span style="height:20px;margin-left:5%">{{item.createTime}}</span>
              </div>
              <div class="whitespace"></div>
              <div class="operateProject" style="display:flex;justify-content:center">
                <Button type="success" v-show="item.isMember===false&&item.isManager===false" @click="joinModal=true">Join</Button>
                <!-- <Button type="success" v-show="item.isManager===false" @click="joinModal=true">Join</Button> -->
                <br>
                <Button type="error" v-show="item.isMember===true||item.isManager===true" @click="quitModalShow(item.projectId)" :id="item.projectId">Quit</Button>
                <Modal
                v-model="joinModal"
                title="Join in project"
                ok-text="Submit"
                cancel-text="Cancel"
                @on-ok="join(currentTab)"
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
                @on-ok="quit(currentTab,index)"
                @on-cancel="cancel"
              >
                <p>Once you exit the project, you will not be able to participate in the collaborative process, confirm the exit?</p>
              </Modal>
              </div>

            </Card>
          </Col>
        </div>
      </div>
    </Row>
  </div>
</template>
<style>
img {
  /* padding:10px; */
  max-width: 100%;
  max-height: 100%;
}
.whitespace {
  height: 20px;
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
      // 输入id的input框是否显示
      inputIdShow: false,
      // join按钮点击后模态框
      joinModal: false,
      //quit按钮点击后弹出的模态框
      quitModal: false,
      //加入项目的Id号
      joinProjectId: "",
      currentProject: {},
      quitSubProjectId: ""
    };
  },
  methods: {
    //该方法负责将选中的类别传递给显示的div
    chooseCurrentType(data) {
      // console.log(data);
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
    joinProject(projectId) {
      this.inputIdShow = true;
    },
    getSpecificTypeProjects(data) {
      this.axios
        .get(
          "http://localhost:8081/project/inquiry" +
            "?key=" +
            data["key"] +
            "&value=" +
            data["value"]
        )
        .then(res => {
          if (res.data === "None") {
            this.currentProjectList = [];
          } else {
            let list = res.data;
            this.judgeMember(list);
            let projectsInfo=this.currentProjectList;
            console.log(projectsInfo);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      return this.currentProjectList;
    },
    //项目成员的id和name都要加进去
    join(tab) {
      console.log(tab);
      this.axios
        .get(
          "http://localhost:8081/project/join?" +
            "projectId=" +
            this.joinProjectId +
            "&userId=" +
            this.$store.state.userId
        )
        .then(res => {
          // console.log("结果是："+res.data);
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
        .catch(err => {});
    },
    quitModalShow(data) {
      this.quitModal = true;
      this.quitSubProjectId = data;
    },
    quit(tab, index) {
      this.axios
        .get(
          "http://localhost:8081/project/quit?" +
            "projectId=" +
            this.quitSubProjectId +
            "&userId=" +
            this.$store.state.userId
        )
        .then(res => {
          console.log(res.data);
          if (res.data === "Success") {
            this.$Message.info("Quit successfully");
          } else {
            this.$Message.danger("Fail");
          }
          let initObject = { key: "category", value: "Water" };
          this.getSpecificTypeProjects(initObject);
        })
        .catch(err => {});
    },
    cancel() {
      this.$Message.info("Clicked cancel");
    },
    joinRequest(data) {
      // console.log(data.length);
      // console.log(this.currentProject);
      for (var i = 0; i < data.length; i++) {
        if (data[i].userId === this.$store.state.userId) {
          this.currentProject["isMember"] = true;
        }
      }
    },
    // 判断是不是成员
    judgeMember(data) {
      //这样的话拿到了用户的id与name
      var that = this;
      if (data.length != 0) {
        that.currentProjectList = data;
        for (var i = 0, n = 0; i < that.currentProjectList.length; i++) {
            $.ajax({
              url:"http://localhost:8081/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                that.currentProjectList[i]["managerId"],
                type:"GET",
                async:false,
                success:function(data){
                  that.currentProjectList[n++]["creator"] =data.userName;
                }
            })
          let _creater = that.currentProjectList[i].managerId;
          let _member = that.currentProjectList[i].members;
          if (_creater == this.$store.state.userId) {
            that.currentProjectList[i]["isManager"] = true;
          } else {
            that.currentProjectList[i]["isManager"] = false;
          }
          if (_member.length != 0) {
            for (var j = 0; j < _member.length; j++) {
              if (_member[j].userId == this.$store.state.userId) {
                that.currentProjectList[i]["isMember"] = true;
              } else {
                that.currentProjectList[i]["isMember"] = false;
              }
            }
          } else {
            that.currentProjectList[i]["isMember"] = false;
          }
        }
      }
    },
    //进入项目详情页面的函数
    goSingleProject(id) {
      this.$router.push({ path: `project/${id}` });
      // console.log(id);
    }
  }
};
</script>

