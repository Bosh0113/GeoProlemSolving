<template>
  <div style="display:flex">
    <div style="z-index:20">
      <toolStyle></toolStyle>
    </div>
    <div id="main" style="flex:4;z-index:10;margin-left:40px;overflow:hidden" :style="{height:toolHeight}">
      <tinymce id="d1" v-model="data"></tinymce>
      <Button type="primary" @click="saveModalShow">save
      </Button>
    </div>
    <Modal
        v-model="saveModal"
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
import tinymce from "vue-tinymce-editor";
import toolStyle from "./toolStyle";
export default {
  components: { tinymce, toolStyle },
  data() {
    return {
      data: "",
      toolHeight: "",
      saveModal:false,
      mdFile:{
        name:"",
        description:"",
      }
    };
  },
  methods: {
    init() {
      this.toolHeight = window.innerHeight + "px";
    },
    saveModalShow(){
      this.saveModal = true;
    },
    uploadFile(){
      var blob = new Blob(['<meta charset="UTF-8">'+this.data],{type:" text/plain;charset=UTF-8"});
      let fileOfBlob = new File([blob], this.mdFile.name +".html", {type: "text/plain; charset=UTF-8"});
      var formData = new FormData();
      formData.append("description", this.mdFile.description);
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
        this.axios.defaults.headers = {
          "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
        }
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
    }
  }
};
</script>
<style>
</style>
