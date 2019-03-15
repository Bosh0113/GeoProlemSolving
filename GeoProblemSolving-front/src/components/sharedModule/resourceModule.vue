<template>
  <Row>
    <div class="member_panel" :style="{maxHeight:resourceHeight}">
      <div class="title">Resource</div>
      <Collapse v-model="value1">
        <Panel name="1">Doc
          <p slot="content" v-for="file in  FileList">{{file.name}}</p>
        </Panel>
        <Panel name="2">Video/Picture
          <p slot="content" v-for="media in MediaList">{{media.name}}</p>
        </Panel>
        <Panel name="3">Result
          <p slot="content" v-for="result in ResultList">{{result.name}}</p></Panel>
      </Collapse>
    </div>
  </Row>
</template>
<style scoped>
.member_panel {
  border: 1px solid lightgray;
  /* margin-top: 20px; */
  /* 200px */
  /* margin-top: 200px; */
  /* background-color: lightblue; */
  /* height:100%; */
}
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  background-color: #2db7f5;
}
</style>
<script>
export default {
  methods: {
    init() {
      this.resourceHeight = window.innerHeight - 200 + "px";
    },
    getFileList(){
      //这个函数始到后台的文件服务器查找已经上传的文件名，展示列表
    }
  },
  data() {
    return {
      resourceHeight: "",
      value1: "1",
      FileList: [
        { name: "description.doc" },
        { name: "phenomenon.doc" },
        { name: "relatedPaperList.doc" }
      ],
      MediaList: [
        { name: "phenomenonscence.png" },
        { name: "mapscreen.png" },
        { name: "scene(10/11).png" },
        { name: "scene(10/21).png" },
        { name: "scene(11/1).png" },
        { name: "scene(11/11).png" },
        { name: "scene.mp4" }
      ],
      ResultList: [
        { name: "chatRecord(10-21).md" },
        { name: "chatRecord(10-30).md" },
        { name: "chatRecord(11-12).md" },
        { name: "chatRecord(11-28).md" }
      ]
    };
  },
  created() {
    this.init();
  }
};
</script>
