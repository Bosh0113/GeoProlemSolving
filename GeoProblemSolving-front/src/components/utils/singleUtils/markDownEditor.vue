<template>
  <div style="display:flex">
    <div style="z-index:20">
      <toolStyle></toolStyle>
    </div>
    <div id="main" style="flex:4;z-index:10;margin-left:40px" :style="{height:toolHeight}">
      <Button @click="save">111</Button>
      <mavon-editor
        v-model="markDownValue"
        :toolbars="markdownOption"
        placeholder="please create your file here"
        style="height:100%"
        @save="saveMavon"
        ref="md"
      />
    </div>
    <Modal
        v-model="uploadMdFileModal"
        title="Upload txt file"
        @on-ok="uploadFile"
        @on-cancel="">
        <Form :model="mdFile" :label-width="80">
        <FormItem label="name">
            <Input v-model="mdFile.name" placeholder="Enter something..."></Input>
        </FormItem>
        <FormItem label="description">
            <Input v-model="mdFile.description" placeholder="Enter something..."></Input>
        </FormItem>
    </Form>
    </Modal>
  </div>
</template>
<script>
import mavonEditor from "mavon-editor";
import toolStyle from "./toolStyle";
import "mavon-editor/dist/css/index.css";
export default {
  components: { toolStyle },
  data() {
    return {
      markDownValue: "",
      markdownOption: {
        bold: true, // 粗体
                italic: true, // 斜体
                header: true, // 标题
                underline: true, // 下划线
                strikethrough: true, // 中划线
                mark: true, // 标记
                superscript: true, // 上角标
                subscript: true, // 下角标
                quote: true, // 引用
                ol: true, // 有序列表
                ul: true, // 无序列表
                link: true, // 链接
                imagelink: true, // 图片链接
                code: true, // code
                table: true, // 表格
                fullscreen: true, // 全屏编辑
                readmodel: true, // 沉浸式阅读
                htmlcode: true, // 展示html源码
                help: true, // 帮助
                /* 1.3.5 */
                undo: true, // 上一步
                redo: true, // 下一步
                trash: true, // 清空
                save: false, // 保存（触发events中的save事件）
                /* 1.4.2 */
                navigation: true, // 导航目录
                /* 2.1.8 */
                alignleft: true, // 左对齐
                aligncenter: true, // 居中
                alignright: true, // 右对齐
                /* 2.2.1 */
                subfield: true, // 单双栏模式
                preview: true, // 预览
      },
      toolHeight: "",
      uploadMdFileModal:false,
      mdFile:{
        name:"",
        description:"",
      },
      // test:{
      //   type:Object,
      // }
    };
  },
  methods: {
    init() {
      this.toolHeight = window.innerHeight + "px";
    },
    saveMavon(value, render) {
      var blob = new Blob([this.$refs.md.d_render],{type:" text/plain"});
      let fileOfBlob = new File([blob], "2.html");
      this.uploadMdFileModal = true;
      var formData = new FormData();
      formData.append("description", "111");
      formData.append("file", fileOfBlob);
      formData.append("type", "document");
      formData.append("uploaderId", this.$store.getters.userInfo.userId);
      formData.append("belong", this.$store.getters.userInfo.userName);
      let scopeObject = {
          projectId: sessionStorage.getItem("projectId"),
          subProjectId: sessionStorage.getItem("subProjectId"),
          moduleId: sessionStorage.getItem("moduleId")
        };
        formData.append("scope", JSON.stringify(scopeObject));
      this.uploadFile(formData);
    },
    uploadFile(formData){
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if (res.data != "Size over" && res.data.length > 0) {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File uploaded successfully",
              duration: 2
            });
          }
        })
        .catch(err => {});
      // console.log("this is render" + render);
      // console.log("this is value" + value);
      // console.log(this.$refs.md.d_render);
    },
    save(){
      console.log(this.markDownValue);
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style>
</style>
