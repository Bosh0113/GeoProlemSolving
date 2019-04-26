<style scoped>
.main {
  display: flex;
}
.sidebarTree {
  min-width: 150px;
  margin-top: 20px;
  border: 1px solid lightgray;
  z-index: 0;
  height: fit-content;
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
.searchPanel {
  display: flex;
  float: right;
}
</style>

<template>
  <Row>
    <Col span="22" offset="1">
      <div class="main" :style={height:contentHeight}>
        <div class="sidebarTree">
          <Menu
            theme="light"
            active-name="all"
            width="auto"
            @on-select="onMenuSelect"
          >
            <MenuGroup title="File Type">
              <MenuItem name="all">
                <Icon type="ios-list-box-outline"/>All
              </MenuItem>
              <MenuItem name="image">
                <Icon type="md-image"/>Image
              </MenuItem>
              <MenuItem name="video">
                <Icon type="md-videocam"/>Video
              </MenuItem>
              <MenuItem name="data">
                <Icon type="md-analytics"/>Data
              </MenuItem>
              <MenuItem name="paper">
                <Icon type="md-paper"/>Paper
              </MenuItem>
              <MenuItem name="document">
                <Icon type="md-document"/>Document
              </MenuItem>
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
                <Table :columns="resourceColumn" :data="showList" border>
                  <template slot-scope="{ row, index }" slot="action" v-show="showList.length>0">
                     <a :href="showList[index].pathURL" :download="showList[index].name"><Icon type="md-download" :size="20" color="yellowgreen"/></a>
                     <a @click="download(index)" style="margin-left: 10px" title="View"><Icon type="md-eye" :size="20" color="orange"/></a>
                  </template>
                </Table>
                <Page :total="dataCount" :page-size="pageSize" show-total @on-change="changepage" show-elevator style="position: absolute;top:550px"/>
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
      // 侧边栏的颜色主题
      resourceColumn: [
        {
          type:"index",
          maxWidth:50,
          align:"center"
        },
        {
          title: "Name",
          key: "name",
          minWidth:10,
          tooltip: true,
        },
        {
          title: "Description",
          key: "description",
          minWidth:30,
          tooltip: true
        },
        {
          title: "Belong",
          key: "belong",
          minWidth: 30,
          tooltip: true
        },
        {
          title: "Type",
          key: "type",
          minWidth: 10,
          tooltip: true,
          align:'center'
        },
        {
          title: "Uploader",
          key: "uploaderName",
          minWidth:20,
          tooltip: true,
          align:'center'
        },
        {
          title: "Time",
          key: "uploadTime",
          sortable: true,
          minWidth: 20,
          tooltip: true,
          align:'center'
        },
        {
          title: "Action",
          slot: "action",
          minWidth: 10,
          tooltip: true,
          align:'center'
        }
      ],
      allResourceList:[],
      allSelectedList:[],
      showList: [],
      dataCount:0,
      pageSize:10,
      // 上传文件的模态框
      uploadModal: false,
      file: "",
      fileDescription: "",
      fileType: "",
      contentHeight: ""
    };
  },
  mounted() {
    this.initLayout();
    this.readResource();
  },
  methods: {
    initLayout() {
      this.contentHeight = window.innerHeight - 120 + "px";
    },
    readResource(){
      this.allResourceList = [];
      this.showList = [];
      this.axios.get("/GeoProblemSolving/resource/allPublic").then(res => {
        if (res.data != "None") {
          var tempResourceList = res.data;
          tempResourceList.forEach(function(list) {
            var time = list.uploadTime;
            list.uploadTime = time.substring(0,10);
          });
          this.dataCount = tempResourceList.length;
          this.$set(this, "allResourceList", tempResourceList);
          this.$set(this, "allSelectedList", tempResourceList);
          this.sliceList();
        }
      });
    },
    sliceList(){
      var tempResourceList = this.allSelectedList;
      if(this.dataCount<this.pageSize){
        this.$set(this, "showList", tempResourceList);
      }
      else{
        this.$set(this, "showList", tempResourceList.slice(0,this.pageSize));
      }
    },
    onMenuSelect(name) {
      if(name=='all'){
        this.$set(this, "allSelectedList", this.allResourceList);
      }else{
        this.mapResourceList(name);
      }
      this.dataCount = this.allSelectedList.length;
      this.sliceList();
    },
    mapResourceList(name){
      var newList = [];
      newList = this.allResourceList.filter(function(e){
        if(e.type==name){
          return e;
        }
      });
      this.$set(this, "allSelectedList", newList);
    },
    changepage(index){
      var _start = (index - 1) * this.pageSize;
      var _end = index * this.pageSize;
      this.showList = this.allSelectedList.slice(_start,_end);
    }
  }
};
</script>
