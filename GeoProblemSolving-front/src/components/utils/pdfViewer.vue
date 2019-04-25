<style scoped>
.pfdPanel {
  margin: auto;
  float: left;
}
.ivu-btn-default {
  margin-left: 10px;
}
.ivu-menu-item {
  padding: 5px 5px 5px 5px;
}
</style>
<template>
  <div style="display:flex">
    <div style="z-index:20">
      <toolStyle></toolStyle>
    </div>
    <div :style="{height:windowHeight+'px'}" style="width:240px;flex:1;float:left;z-index:10;margin-left:40px">
      <Menu theme="light" :open-names="['1','2']" :active-name="activeItem" :style="{height:windowHeight-50+'px'}" style="overflow-y:scroll">
        <Submenu name="1">
          <template slot="title">
            <span style="font-size:25px">Paper</span>
          </template>
          <MenuItem v-for="(list,index) in paperList" :name="'1-'+index" :key="index">
            <span
              @click="selectPDF(list.pathURL,list.name)"
              :title="list.description"
              style="word-break:break-all; width:150px;"
            >{{list.name}}</span>
          </MenuItem>
        </Submenu>
        <Submenu name="2">
          <template slot="title">
            <span style="font-size:25px">Document</span>
          </template>
          <MenuItem v-for="(list,index) in documentList" :name="'2-'+index" :key="index">
            <span
              @click="selectPDF(list.pathURL,list.name)"
              :title="list.description"
            >{{list.name}}</span>
          </MenuItem>
        </Submenu>
      </Menu>
      <Checkbox v-model="isPaper" style="float:left;margin:5px 0px 0px 45px">Paper</Checkbox>
      <Upload :before-upload="handleUpload" action="-" accept="application/pdf" style="float:left">
        <Button icon="ios-cloud-upload-outline">Upload</Button>
      </Upload>
    </div>
    <iframe :width="pdfWidth" :height="pdfHeight" :src="pdfUrl" class="pfdPanel">
      This browser does not support PDFs. Please download the PDF to view it:
      <a
        :href="pdfUrl"
      >Download PDF</a>
    </iframe>
  </div>
</template>
<script>
import toolStyle from "./toolStyle";
export default {
  components:{toolStyle},
  data() {
    return {
      windowHeight: "",
      pdfWidth: 100,
      pdfHeight: 100,
      paperList: [],
      documentList: [],
      pdfUrl: "",
      isPaper: false,
      activeItem:""
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
    this.getPdfResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.windowHeight = window.innerHeight;
      this.pdfWidth = window.innerWidth - 240-20;
      this.pdfHeight = window.innerHeight;
    },
    getPdfResource() {
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
            that.paperList = [];
            that.documentList = [];
            for (let i = 0; i < res.data.length; i++) {
              if (res.data[i].type == "paper") {
                that.paperList.push(res.data[i]);
              } else if (res.data[i].type == "document") {
                that.documentList.push(res.data[i]);
              }
            }
          } else {
            that.paperList = [];
            that.documentList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    selectPDF(url, name) {
      if (!/\.(pdf)$/.test(name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }
      this.pdfUrl = url;
    },
    handleUpload(file) {
      if (!/\.(pdf)$/.test(file.name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }
      let fileType = "";
      if (this.isPaper) {
        fileType = "paper";
      } else {
        fileType = "document";
      }

      //上传数据
      let formData = new FormData();
      let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      formData.append("file", file);
      formData.append("description", "Pdf file viewer tool");
      formData.append("type", fileType);
      formData.append("uploaderId", userInfo.userId);
      formData.append("belong", userInfo.userName);
      let scopeObject = {
        projectId: sessionStorage.getItem("projectId"),
        subProjectId: sessionStorage.getItem("subProjectId"),
        moduleId: sessionStorage.getItem("moduleId")
      };
      formData.append("scope", JSON.stringify(scopeObject));
      let that = this;
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data != "Size over" && res.data.length > 0) {
            let pdfName = res.data[0].fileName;
            that.pdfUrl = "/GeoProblemSolving/resource/upload/" + pdfName;

            let pdfItem = {
              name: file.name,
              description: "Pdf file viewer tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + pdfName
            };
            if (that.isPaper) {
              that.paperList.push(pdfItem);
              that.activeItem="1-"+(that.paperList.length-1);
            } else {
              that.documentList.push(pdfItem);
              that.activeItem="2-"+(that.documentList.length-1);
            }

          }
        })
        .catch(err => {});
      return false;
    }
  }
};
</script>
