<style scoped>

.sidebar {
  /* width: 260px; */
  margin: 0 10px;
}
.sidebarPanel {
  padding: 20px;
}
.rightPart {
  padding: 20px;
  display: flex;
  align-items: center;
}
.topPanel {
  display: flex;
  height: 60px;
  align-items: center;
}
.searchPanel {
  display: flex;
  height: 60px;
  align-items: center;
  float: right;
}
.topPanel button {
  margin: 0 2.5%;
}
.buttonPanel {
  padding: 0 20px;
  height: 60px;
  /* background-color: aqua; */
}
.resourceList{
  /* height:400px; */
  padding: 0 20px;
  /* background-color:lightblue; */
}
.whitespace{
  height:20px;
}
</style>
<template>
  <div class="">
    <Row>
      <Col :lg="5" :md="6" :sm="8" :xs="10">
        <div class="sidebar" >
          <div class="sidebarPanel">
            <Menu :theme="sidebarTheme" active-name="1" width="auto" :style="{height:sidebarHeight}">
              <MenuGroup title="Resource Collection">
                <MenuItem name="Image">
                  <Icon type="md-image"/>Image
                </MenuItem>
                <MenuItem name="Video">
                  <Icon type="md-videocam"/>Video
                </MenuItem>
                <MenuItem name="document">
                  <Icon type="md-document"/>Document
                </MenuItem>
                <MenuItem name="data">
                  <Icon type="md-analytics"/>Data
                </MenuItem>
                <MenuItem name="others">
                  <Icon type="logo-xbox"/>Others
                </MenuItem>
              </MenuGroup>
            </Menu>
          </div>
        </div>
      </Col>
      <Col :lg="{ span: 19 }" :md="{ span: 18}" :sm="{ span: 16}" :xs="{ span: 14}">
          <div class="rightPart">
            <Col :lg="{ span: 6 }" :md="{ span: 8}" :sm="{ span: 10}" :xs="{ span: 12}">
              <div class="topPanel">
                <Button type="primary" title="upload resource">
                  <Icon type="ios-cloud-upload-outline" :size="20"/>
                </Button>
                <Button type="success" title="download resource">
                  <Icon type="ios-download-outline" :size="20"/>
                </Button>
              </div>
            </Col>
            <Col :lg="{ span: 18 }" :md="{ span: 16}" :sm="{ span: 14}" :xs="{ span: 12}">
              <div class="searchPanel">
                <Input
                  v-model="searchResourceInput"
                  placeholder="Enter something..."
                  style="width:auto"
                >
                  <Button slot="append" icon="ios-search"></Button>
                </Input>
              </div>
            </Col>
            <br>
          </div>
          <div class="resourceList">
          <div class="whitespace"></div>
          <Table border ref="selection" :columns="columns4" :data="data1"></Table>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      sidebarHeight: "",
      sidebarTheme: "light",
      rightPartWidth: "",
      // 表格的高度
      tableHeight:"",
      //资源输入框输入的变量
      searchResourceInput: "",
      //关于表格的列表变量
      columns4: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center',
                        "sortable": true,
                    },
                    {
                        title: 'Name',
                        key: 'name',
                        "sortable": true,
                    },
                    {
                        title: 'Size',
                        key: 'size',
                        "sortable": true,
                    },
                    {
                        title: 'Time',
                        key: 'time',
                        "sortable": true,
                    }
                ],
                data1: [
                    {
                        name: 'lake_scene',
                        size: "1.2MB",
                        address: 'New York No. 1 Lake Park',
                        time: '2016-10-03'
                    },
                    {
                        name: 'park_scene',
                        size: "2.5MB",
                        address: 'London No. 1 Lake Park',
                        time: '2016-10-01'
                    },
                    {
                        name: 'park_scene2',
                        size: "3MB",
                        address: 'Sydney No. 1 Lake Park',
                        time: '2016-10-02'
                    },
                    {
                        name: 'park_scene3',
                        size: "1.5MB",
                        address: 'Ottawa No. 2 Lake Park',
                        time: '2016-10-04'
                    }
                ]
    };
  },
  methods: {
    initHeight(){
      this.sidebarHeight = window.innerHeight - 100 + "px";
      this.rightPartWidth = window.innerWidth - 260 + "px";
      // this.tableHeight = window.innerHeight - 180 + "px"
    },
    handleSelectAll (status) {
      this.$refs.selection.selectAll(status);
    }
  },
  created() {
    this.initHeight();
  }
};
</script>
