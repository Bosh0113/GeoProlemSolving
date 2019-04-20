<template>
<div>
  <Upload
        multiple
        type="drag"
        :before-upload="handleUpload"
        action="-"
        :on-progress="test"
        >
        <div style="padding: 20px 0">
            <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
            <p>Click or drag files here to upload</p>
        </div>
        <div v-if="file.length!=0">
          <ul v-for="item in file">{{item.name}}</ul>
        </div>

    </Upload>
     <Button type="text" @click="upload">111</Button>
</div>

</template>
<script>
export default {
  data () {
            return {
                file: [],
            }
        },
        methods: {
            handleUpload (file) {
                this.file.push(file);
                console.table(this.file);
                return false;
            },
            test(event,file,fileList){
              console.log(event);
              console.log(file);
              console.log(fileList);
            },
            upload () {
               this.axios
                .post("/GeoProblemSolving/resource/upload", this.file)
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
                // this.loadingStatus = true;
                // setTimeout(() => {
                //     this.file = null;
                //     this.loadingStatus = false;
                //     this.$Message.success('Success')
                // }, 1500);
            }
        }
}
</script>
