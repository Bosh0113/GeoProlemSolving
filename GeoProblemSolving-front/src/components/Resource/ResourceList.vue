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
.menuClass{
  border:1px solid lightgray;
  z-index:0
}
</style>
<template>
  <Row>
    <Col span="22" offset="1">
      <div class="main" :style={height:contentHeight}>
        <div class="sidebarTree">
          <Menu
            :theme="sidebarTheme"
            active-name="1"
            width="auto"
            class="menuClass"
            :style="{height:sidebarTreeHeight}"
            @on-select="onMenuSelect"
          >
            <MenuGroup title="Resource Collection">
              <MenuItem name="image">
                <Icon type="md-image"/>Image
              </MenuItem>
              <MenuItem name="video">
                <Icon type="md-videocam"/>Video
              </MenuItem>
              <MenuItem name="data">
                <Icon type="md-analytics"/>Data
              </MenuItem>
              <!-- <Icon type="md-paper" /> -->
              <MenuItem name="paper">
                <Icon type="md-paper"/>Paper
              </MenuItem>
              <MenuItem name="document">
                <Icon type="md-document"/>Document
              </MenuItem>
              <!-- <Icon type="logo-dropbox" /> -->
              <MenuItem name="model">
                <Icon type="logo-dropbox"/>Model
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
                <Table :columns="resourceColumn" :data="specifiedResourceList">
                  <template slot-scope="{ row }" slot="name">
                    <strong>{{ row.name }}</strong>
                  </template>
                  <template slot-scope="{ row, index }" slot="action">
                     <a :href="specifiedResourceList[index].pathURL" :download="specifiedResourceList[index].name"><Icon type="md-download" :size="20" color="yellowgreen"/></a>
                     <a @click="download(index)" style="margin-left: 10px" title="View"><Icon type="md-eye" :size="20" color="orange"/></a>
                     <a @click="deleteResource(index)" :disabled="judgeDelete(index)" style="margin-left: 10px"><Icon type="md-close" :size="20" color="red"/></a>
                    <!-- <Button
                      type="success"
                      size="small"
                      style="margin-right: 5px"
                      @click="download(index)"
                    >
                      <Icon type="md-download"/>
                    </Button>
                    <Button
                      type="error"
                      size="small"
                      @click="deleteResource(index)"
                      :disabled="judgeDelete(index)"
                    >
                      <Icon type="md-close"/>
                    </Button> -->
                  </template>
                </Table>
                <div style="display:flex;margin-top:20px;justify-content:center">
                  <Button type="primary" title="upload resource" @click="fileUploadModalShow()">
                    <Icon type="ios-cloud-upload-outline" :size="20"/>
                  </Button>
                </div>
              </Col>
            </Row>
            <div style="height:20px"></div>
          </div>
        </div>
      </div>
    </Col>
    <Modal
      v-model="uploadModal"
      title="Upload resource"
      @on-ok="submitFile()"
      @on-cancel=""
      ok-text="submit"
      cancel-text="cancel"
      :mask-closable="false"
      width="600px"
    >
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <!-- 这里定义上传的几种资源类型供用户选择 -->
        <span style="width:20%">Type</span>
        <RadioGroup v-model="fileType" style="width:80%">
          <Radio label="image"></Radio>
          <Radio label="video"></Radio>
          <Radio label="data"></Radio>
          <Radio label="paper"></Radio>
          <Radio label="document"></Radio>
          <Radio label="model"></Radio>
          <Radio label="others"></Radio>
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
          type: "selection",
          width: 60,
          align: "center",
          sortable: true
        },
        {
          title: "Name",
          key: "name",
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          sortable: true
        },
        {
          title: "Belong",
          key: "belong",
          width:100,
        },
        {
          title: "Type",
          key: "type",
          sortable: true,
          width:100,
        },
        {
          title: "Uploader",
          key: "uploader"
        },
        {
          title: "Time",
          key: "uploadTime",
          sortable: true,
          width:150
        },
        {
          title: "Action",
          slot: "action",
          width:220,
        }
      ],
      uploaderArray: [],
      specifiedResourceList: [],
      // 上传文件的模态框
      uploadModal: false,
      file: "",
      fileDescription: "",
      fileType: "",
      contentHeight:""
    };
  },
  mounted() {
    this.initLayout();
    this.onMenuSelect("image");
  },
  methods: {
    initLayout() {
      this.sidebarTreeHeight = window.innerHeight - 180 + "px";
      this.contentHeight = window.innerHeight-120 + 'px';
    },
    onMenuSelect(name) {
      this.uploaderArray = [];
      this.specifiedResourceList = [];
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" + "?key=type" + "&value=" + name
        )
        // .get(
        //   "/GeoProblemSolving/resource/allPublic"
        // )
        .then(res => {
          if (res.data != "None") {
            let specifiedResourceListPre = res.data;
            specifiedResourceListPre.forEach(function(list) {
              list["uploader"] =
                list.uploaderName;
            });
            this.$set(this, "specifiedResourceList", specifiedResourceListPre);
          }
        });
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
    },
    fileUploadModalShow() {
      if(this.$store.getters.userState){
        this.uploadModal = true;
      }else{
        this.$Notice.open({
          title:"Login Please",
          desc: 'If you want to upload a file here, please login first.'
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
              duration: 2
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
    download(index) {
      window.open(this.specifiedResourceList[index].pathURL);
    },
    deleteResource(index) {
      if (this.specifiedResourceList[index].resourceId != "") {
        let id = this.specifiedResourceList[index].resourceId;
        this.axios
          .get("/GeoProblemSolving/resource/delete?" + "resourceId=" + id)
          .then(res => {
            if (res.data == "Success") {
              this.$Message.info("Delete successfully");
              this.specifiedResourceList.splice(index,1);
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
