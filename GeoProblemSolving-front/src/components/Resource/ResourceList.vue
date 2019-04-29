<style scoped>
.main {
  display: flex;
}
.sidebarTree {
  padding-top: 20px;
  /* background-color: lightgreen; */
  min-width: 250px;
  margin-right: 20px;
}
.resourcePanel {
  flex: 1;
}

.btnPanel {
  flex: 1;
}
.btnPanel button {
  margin-left: 20px;
}
.menuClass {
  border: 1px solid lightgray;
  z-index: 0;
}
</style>
<template>
  <Row>
    <Col span="22" offset="1">
      <h2 style="margin-top: 20px;">Resource center</h2>
      <div class="main" :style="{height:contentHeight-60 + 'px'}">
        <div class="sidebarTree">
          <Menu
            :theme="sidebarTheme"
            active-name="1"
            width="auto"
            class="menuClass"
            :style="{height:sidebarTreeHeight}"
            @on-select="onMenuSelect"
          >
            <MenuGroup title="Resource type">
              <MenuItem name="image">
                <Icon type="md-image"/>Images
              </MenuItem>
              <MenuItem name="video">
                <Icon type="md-videocam"/>Videos
              </MenuItem>
              <MenuItem name="data">
                <Icon type="md-analytics"/>Data
              </MenuItem>
              <MenuItem name="paper">
                <Icon type="md-paper"/>Papers
              </MenuItem>
              <MenuItem name="document">
                <Icon type="md-document"/>Documents
              </MenuItem>
              <MenuItem name="model">
                <Icon type="logo-dropbox"/>Models
              </MenuItem>
              <MenuItem name="others">
                <Icon type="logo-xbox"/>Others
              </MenuItem>
            </MenuGroup>
          </Menu>
        </div>
        <div class="resourcePanel">
          <div style="height:20px"></div>
          <div class="resourcePanel">
            <Row>
              <Col span="22" offset="1">
                <Table :columns="resourceColumn" :data="showList">
                  <template slot-scope="{ row }" slot="name">
                    <strong>{{ row.name }}</strong>
                  </template>
                  <template slot-scope="{ row, index }" slot="action">
                    <a
                      :href="specifiedResourceList[index].pathURL"
                      :download="specifiedResourceList[index].name"
                      title="Download"
                    >
                      <Icon type="md-download" :size="20" color="yellowgreen"/>
                    </a>
                    <span @click="show(index)" style="margin-left: 10px" title="Preview">
                      <Icon type="md-eye" :size="20" color="orange"/>
                    </span>
                    <span
                      @click="deleteResource(index)"
                      :disabled="judgeDelete(index)"
                      style="margin-left: 10px"
                    >
                      <Icon type="md-close" :size="20" color="red"/>
                    </span>
                  </template>
                </Table>
                <div style="display:flex;margin-top:20px;justify-content:right">
                  <Button type="primary" title="upload resource" @click="fileUploadModalShow()">
                    <Icon type="ios-cloud-upload-outline" :size="20"/>
                  </Button>
                </div>
              </Col>
            </Row>
            <div style="display:flex;justify-content:center">
              <Page
                :total="dataCount"
                :page-size="pageSize"
                show-total
                @on-change="changepage"
                show-elevator
                style="position: absolute;top:600px"
              />
            </div>
          </div>
        </div>
      </div>
    </Col>
    <Modal
      v-model="uploadModal"
      title="Upload resource"
      @on-ok="submitFile()"
      ok-text="Submit"
      cancel-text="Cancel"
      :mask-closable="false"
      width="600px"
    >
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <!-- 这里定义上传的几种资源类型供用户选择 -->
        <span style="width:20%">Type</span>
        <RadioGroup v-model="fileType" style="width:80%">
          <Radio label="data">Data</Radio>
          <Radio label="image">Images</Radio>
          <Radio label="video">Videos</Radio>
          <Radio label="paper">Papers</Radio>
          <Radio label="document">Documents</Radio>
          <Radio label="model">Models</Radio>
          <Radio label="others">Others</Radio>
        </RadioGroup>
        <!-- 结束 -->
      </div>
      <br>
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <span style="width:20%">Description</span>
        <Input type="textarea" :rows="2" v-model="fileDescription"/>
      </div>
      <br>
      <input type="file" @change="getFile($event)" style="margin-left:20%" multiple="multiple">
    </Modal>
  </Row>
</template>
<script>
export default {
  data() {
    return {
      sidebarTreeHeight: "",
      searchResourceInput: "",
      // 侧边栏的颜色主题
      sidebarTheme: "light",
      resourceColumn: [
        {
          type: "index",
          maxWidth: 50,
          align: "center"
        },
        {
          title: "Name",
          key: "name",
          minWidth: 10,
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          width: 100,
          sortable: true
        },
        {
          title: "Size",
          key: "fileSize",
          width: 100,
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          minWidth: 30,
          tooltip: true
        },
        {
          title: "Upload time",
          key: "uploadTime",
          width: 160,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 150,
          align: "center"
        }
      ],
      uploaderArray: [],
      specifiedResourceList: [],
      // 上传文件的模态框
      uploadModal: false,
      file: "",
      fileDescription: "",
      fileType: "data",
      contentHeight: "",
      panel: null,
      // 分页
      allResourceList: [],
      allSelectedList: [],
      showList: [],
      dataCount: 0,
      pageSize: 10
    };
  },
  mounted() {
    this.initLayout();
    this.onMenuSelect("image");
  },
  beforeRouteLeave(to, from, next) {
    if (this.panel != null) {
      this.panel.close();
    }
    next();
  },
  methods: {
    initLayout() {
      this.sidebarTreeHeight = window.innerHeight - 200 + "px";
      this.contentHeight = window.innerHeight - 120;
    },
    onMenuSelect(name) {
      this.uploaderArray = [];
      this.specifiedResourceList = [];
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" + "?key=type" + "&value=" + name
        )
        .then(res => {
          if (res.data != "None") {
            let specifiedResourceListPre = res.data;
            specifiedResourceListPre.forEach(function(list) {
              list["uploader"] = list.uploaderName;
            });
            this.dataCount = specifiedResourceListPre.length;
            this.$set(this, "specifiedResourceList", specifiedResourceListPre);
            this.sliceList();
          }
        });
    },
    sliceList() {
      var tempResourceList = this.specifiedResourceList;
      if (this.dataCount < this.pageSize) {
        this.$set(this, "showList", tempResourceList);
      } else {
        this.$set(this, "showList", tempResourceList.slice(0, this.pageSize));
      }
    },
    changepage(index) {
      var _start = (index - 1) * this.pageSize;
      var _end = index * this.pageSize;
      this.showList = this.specifiedResourceList.slice(_start, _end);
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
    },
    fileUploadModalShow() {
      if (this.$store.getters.userState) {
        this.uploadModal = true;
      } else {
        this.$Notice.open({
          title: "Login Please",
          desc: "If you want to upload a file here, please login first."
        });
      }
    },
    //上传文件
    submitFile() {
      let formData = new FormData();
      // 向 formData 对象中添加文件
      formData.append("file", this.file);
      formData.append("description", this.fileDescription);
      formData.append("type", this.fileType);
      formData.append("uploaderId", this.$store.getters.userId);
      // 添加字段属于那个项目
      formData.append("belong", this.$store.getters.userName);
      let scopeObject = {
        projectId: "",
        subProjectId: "",
        moduleId: ""
      };
      formData.append("scope", JSON.stringify(scopeObject));
      //这里还要添加其他的字段
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data != "Size over" && res.data.length > 0) {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File uploaded successfully",
              duration: 0
            });
            //这里重新获取一次该项目下的全部资源
            this.addUploadEvent(this.currentProjectDetail.projectId);
            this.getAllResource();
            // 创建一个函数根据pid去后台查询该项目下的资源
          }
        })
        .catch(err => {});
    },
    getFile(event) {
      this.file = event.target.files[0];
    },
    judgeDelete(index) {
      if (
        this.specifiedResourceList[index].uploaderId ==
        this.$store.getters.userId
      ) {
        return false;
      } else {
        return true;
      }
    },
    show(index) {
      let name = this.specifiedResourceList[index].name;

      if (/\.(pdf|doc|docx|xls|xlsx|ppt|pptx|zip)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.7:8012/previewFile?url=" +'http://'+this.$store.state.IP_Port+
          this.specifiedResourceList[index].pathURL;
        var toolURL =
          '<iframe src=' + url + ' style="width: 100%;height:100%"></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else if(/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          'http://'+this.$store.state.IP_Port+this.specifiedResourceList[index].pathURL;
        var toolURL =
          '<video src=' + url + ' style="width: 100%;height:100%" controls></video>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else if(/\.(xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())){
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          'http://'+this.$store.state.IP_Port+ this.specifiedResourceList[index].pathURL;
        var toolURL =
          '<iframe src=' + url + ' style="width: 100%;height:100%" controls></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "light",
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }
      else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    },
    deleteResource(index) {
      if (this.specifiedResourceList[index].resourceId != "") {
        let id = this.specifiedResourceList[index].resourceId;
        this.axios
          .get("/GeoProblemSolving/resource/delete?" + "resourceId=" + id)
          .then(res => {
            if (res.data == "Success") {
              this.$Message.info("Delete successfully");
              this.specifiedResourceList.splice(index, 1);
            } else if (res.data == "Fail") {
              this.$Message.info("Failure");
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    }
  }
};
</script>
