<style scoped>
.videoPanel {
  margin: auto;
}
.ivu-btn-default {
  margin-left: 75px;
}
.ivu-menu-item {
  padding: 5px 5px 5px 5px;
}
</style>
<template>
  <div>
    <div :style="{height:windowHeight+'px'}" style="width:270px;float:left">
      <Menu theme="light" :open-names="['1']" :active-name="activeItem" :style="{height:windowHeight-50+'px'}" style="overflow-y:scroll">
        <Submenu name="1">
          <template slot="title">
            <span style="font-size:25px">Video</span>
          </template>
          <MenuItem v-for="(list,index) in videoList" :name="index" :key="index">
            <span
              @click="selectVideo(list.pathURL)"
              :title="list.description"
              style="word-break:break-all; width:150px;"
            >{{list.name}}</span>
          </MenuItem>
        </Submenu>
      </Menu>
      <Upload :before-upload="handleUpload" action="-" accept="video/mp4">
        <Button icon="ios-cloud-upload-outline">Upload</Button>
      </Upload>
    </div>
    <video
      :width="videoWidth"
      :height="videoHeight"
      :src="this.videoUrl"
      controls
      class="videoPanel"
      style="float:left;margin-top:40px"
    >
      <!-- <source :src="this.videoUrl" type="video/mp4"> -->
    </video>
  </div>
</template>
<script>
export default {
  data() {
    return {
      windowHeight: "",
      videoWidth: 100,
      videoHeight: 100,
      videoList: [],
      videoUrl: "",
      activeItem:0
    };
  },
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
    window.addEventListener("resize", this.initSize);
    this.initSize();
    this.getVideoResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.windowHeight = window.innerHeight;
      this.videoWidth = window.innerWidth - 240 - 60;
      this.videoHeight = window.innerHeight - 80;
    },
    getVideoResource() {
      var that = this;
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.moduleId" +
            "&value=" +
            sessionStorage.getItem("moduleId")
        )
        .then(res => {
          // 写渲染函数，取到所有资源
          if (res.data !== "None") {
            that.videoList = [];
            for (let i = 0; i < res.data.length; i++) {
              if (res.data[i].type == "video") {
                that.videoList.push(res.data[i]);
              }
            }
          } else {
            that.videoList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    selectVideo(url) {
      this.videoUrl = url;
    },
    handleUpload(file) {
      if (!/\.(mp4)$/.test(file.name.toLowerCase())) {
        this.$Message.error("上传格式不正确，请上传xls或者xlsx格式");
        return false;
      }

      //上传数据
      let formData = new FormData();
      let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      formData.append("file", file);
      formData.append("description", "video viewer tool");
      formData.append("type", "video");
      formData.append("uploaderId", userInfo.userId);
      formData.append("belong", userInfo.userName);
      let scopeObject = {
        projectId: "",
        subProjectId: "",
        moduleId: sessionStorage.getItem("moduleId")
      };
      formData.append("scope", JSON.stringify(scopeObject));
      let that = this;
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data != "Size over" && res.data.length > 0) {
            let videoName = res.data[0].fileName;
            that.videoUrl = "/GeoProblemSolving/resource/upload/" + videoName;

            let videoItem = {
              name: file.name,
              description: "video viewer tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + videoName
            };
            that.videoList.push(videoItem);
            that.activeItem = that.videoList.length - 1;
          }
        })
        .catch(err => {});
      return false;
    }
  }
};
</script>
