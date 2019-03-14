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
.topPanel {
  margin-top: 20px;
  display: flex;
}
.btnPanel {
  flex: 1;
  /* justify-content:flex-start; */
}
.btnPanel button {
  margin-left: 20px;
}
.searchPanel {
  /* flex:1; */
  display: flex;
  float: right;
  /* justify-content:flex-end; */
}
</style>

<template>
  <Row>
    <Col span="22" offset="1">
      <div class="main">
        <div class="sidebarTree">
          <Menu
            :theme="sidebarTheme"
            active-name="1"
            width="auto"
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
          <div class="topPanel">
            <div class="btnPanel">
              <Button type="primary" title="upload resource">
                <Icon type="ios-cloud-upload-outline" :size="20"/>
              </Button>
              <Button type="success" title="download resource">
                <Icon type="ios-download-outline" :size="20"/>
              </Button>
            </div>
            <!-- <div class="searchPanel">
              <Input
                v-model="searchResourceInput"
                placeholder="Enter something..."
                style="width:auto"
              >
                <Button slot="append" icon="ios-search"></Button>
              </Input>
            </div> -->
          </div>
          <div class="resourcePanel">
            <div style="height:20px"></div>
            <!-- <Table border ref="selection" :columns="columns4" :data="data1"></Table> -->
            <Table border ref="selection" :columns="resourceColumn" :data="specifiedResourceList"></Table>
          </div>
        </div>
      </div>
    </Col>
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
          title: "Type",
          key: "type",
          sortable: true
        },
        {
          title: "FileSize",
          key: "fileSize",
          sortable: true
        },
        {
          title: "Uploader",
          key: "uploader"
        },
        {
          title: "Uploader Time",
          key: "uploadTime",
          sortable: true
        }
      ],
      specifiedResourceListPre: [],
      uploaderArray: [],
      specifiedResourceList: []
    };
  },
  created() {
    this.initLayout();
    this.onMenuSelect("image");
  },
  methods: {
    initLayout() {
      this.sidebarTreeHeight = window.innerHeight - 80 + "px";
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
            this.specifiedResourceListPre = res.data;
            // console.table(this.specifiedResourceListPre);
            let userIdList = this.specifiedResourceListPre.map(function(
              resource
            ) {
              return resource.uploaderId;
            });
            // console.log(userIdList);
            var that = this;
            if (userIdList.length != 0) {
              userIdList.forEach(function(value, k) {
                that.axios
                  .get(
                    "/GeoProblemSolving/user/inquiry" +
                      "?key=userId" +
                      "&value=" +
                      value
                  )
                  .then(res => {
                    var userInfoArray = [];
                    that.uploaderArray.push(res.data);
                    if (
                      that.specifiedResourceListPre.length ==
                      that.uploaderArray.length
                    ) {
                      // this.getList(that.uploaderArray);
                      that.uploaderArray.forEach(function(item) {
                        userInfoArray.push({
                          id: item.userId,
                          name: item.userName,
                          organization: item.organization
                        });
                      });
                      console.log(userInfoArray);
                      that.specifiedResourceListPre.forEach(function(list) {
                        userInfoArray.forEach(item => {
                          if (list.uploaderId == item.id) {
                            list["uploader"] = item.name + "(" +item.organization + ")";
                          }
                        });
                      });
                      that.specifiedResourceList = that.specifiedResourceListPre;                    }
                  })
                  .catch(err => {});
              });
            }
          }
        });
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
    },
  }
};
</script>
