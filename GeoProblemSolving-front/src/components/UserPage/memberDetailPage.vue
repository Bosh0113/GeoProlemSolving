<template>
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
          </div>
        </div>
      </Col>
</Row>
</template>
<style scoped>
img {
  width:100%;
  height:auto;
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
</style>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  data(){
    return{
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
    }
  },
  methods: {
    getMemberDetail(){
      this.axios.get(url, {
        params: {
          id:paramId
        }
      })
      .then(function (response) {
      })
      .catch(function (error) {
      })
    },
    getUserProfile() {
      this.axios
        .get(
          "http://localhost:8081/GeoProblemSolving/user/inquiry" +
            "?key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          this.userDetail = res.data;
          //打印用户的具体信息
          console.log(this.userDetail);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
  },
  created() {
    this.getUserProfile();
  },
  mounted() {

  },
}
</script>

