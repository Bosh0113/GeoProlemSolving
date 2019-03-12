<template>
  <div class="project form">
    <h1>New Project</h1>
    <Form ref="formInline" :model="formInline" class="projectForm">
      <!-- 选择类别 -->
      <FormItem prop="category">
        <div style="display:flex" class="inline_style">
          <div class="hintTitle">Category</div>
          <RadioGroup v-model="formInline.category" style="width:80%">
            <Radio label="Water"></Radio>
            <Radio label="Soil"></Radio>
            <Radio label="Ecology"></Radio>
            <Radio label="Atmosphere"></Radio>
            <Radio label="Society"></Radio>
            <Radio label="Others"></Radio>
          </RadioGroup>
        </div>
      </FormItem>
      <FormItem prop="title">
        <div class="inline_style">
          <div class="hintTitle">Title</div>
          <Input v-model="formInline.title" placeholder="Enter Title..." style="width:80%"/>
        </div>
      </FormItem>
      <FormItem prop="desc">
        <div class="inline_style">
          <div class="hintTitle">Description</div>
          <Input
            v-model="formInline.description"
            placeholder="Enter a brief introduction ..."
            style="width:80%"
          />
        </div>
      </FormItem>
      <FormItem prop="Privacy">
        <div class="inline_style">
          <div class="hintTitle">Privacy</div>
          <RadioGroup v-model="formInline.privacy" style="width:80%">
            <Radio
              label="Public"
              title="Other users can find the group and see who has membership."
            ></Radio>
            <Radio
              label="Discover"
              title="Other users can find this group, but membership information is hidden."
            ></Radio>
            <Radio label="Private" title="Other users can not find this group."></Radio>
          </RadioGroup>
        </div>
      </FormItem>
      <FormItem prop="Tag">
        <div class="inline_style">
          <div class="hintTitle">Tag</div>
          <Input
            v-model="inputTag"
            placeholder="Enter some tag to introduce the project"
            style="width: 200px"
            @keyup.enter.native="addTag(inputTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addTag(inputTag)"
            style="margin-left:2.5%"
          >Add Tag</Button>
          <div style="margin-left:5%">
            <Tag
              color="primary"
              v-for="(item,index) in this.formInline.tagList"
              :key="index"
              closable
              @on-close="deleteTag(index)"
            >{{item}}</Tag>
          </div>
        </div>
      </FormItem>
      <FormItem prop="image">
        <div class="inline_style">
          <div class="hintTitle">Image</div>
          <div class="demo-upload-list" v-if="img!=''">
            <template>
              <img v-bind:src="img">
              <div class="demo-upload-list-cover">
                <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
              </div>
            </template>
          </div>
          <div class="uploadBox">
            <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
            <input @change="uploadPhoto($event)" type="file" class="uploadAvatar">
          </div>
          <br>
          <!-- <span>{{this.imagename}}</span> -->
          <Modal title="View Image" v-model="visible">
            <img :src="img" v-if="visible" style="width: 100%">
          </Modal>
        </div>
      </FormItem>
      <FormItem prop="introduction">
        <div class="inline_style">
          <div class="hintTitle">Introduction</div>
          <div id="editor">
            <mavon-editor
              style="height: 100%"
              v-model="formInline.introduction"
              :toolbars="toolbars"
              @keydown
            ></mavon-editor>
          </div>
        </div>
      </FormItem>
      <FormItem>
        <div class="inline_style">
          <Button type="success" @click="createProject()" class="create">Create</Button>
        </div>
      </FormItem>
    </Form>
  </div>
</template>
<style>
h1 {
  text-align: center;
  margin-top: 2.5%;
}
.projectForm {
  width: 60%;
  margin-top: 0.5%;
  margin-left: 20%;
  margin-right: 20%;
}
.inline_style {
  display: flex;
}
.hintTitle {
  text-align: center;
  width: 20%;
}
.create {
  width: 20%;
  margin-right: 40%;
  margin-left: 40%;
  margin-top: 10%;
}
#editor {
  /* position:fixed; */
  margin: 20px auto;
  bottom: 0;
  width: 80%;
  height: 200px;
}
/* 上传图片 */
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  /* overflow-x: hidden; */
  /* overflow-y: scroll; */
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}

/* 结束 */
</style>
<script>
export default {
  data() {
    return {
      formInline: {
        title: "",
        category: "",
        introduction: "",
        privacy: "",
        description: "",
        //tag列表
        tagList: []
      },
      //这是操控mavon-editor上工具条的配置列表
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        mark: true, // 标记
        superscript: true, // 上角标
        quote: true, // 引用
        ol: true, // 有序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        help: true, // 帮助
        code: true, // code
        subfield: true, // 是否需要分栏
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        /* 1.3.5 */
        undo: true, // 上一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true // 导航目录
      },
      //用来存储输入的单个标签变量
      inputTag: "",
      visible: false,
      //表示图片
      img: "",
      createProjectId:"",
    };
  },

  methods: {
    createProject() {
      let createProjectForm = {};
      createProjectForm["category"] = this.formInline.category;
      createProjectForm["title"] = this.formInline.title;
      createProjectForm["tag"] = this.formInline.tagList.toString();
      createProjectForm["privacy"] = this.formInline.privacy;
      createProjectForm["picture"] = this.img;
      createProjectForm["introduction"] = this.formInline.introduction;
      createProjectForm["description"] = this.formInline.description;
      createProjectForm["managerId"] = this.$store.state.userId;
      console.log(createProjectForm);
      this.axios
        .post("/GeoProblemSolving/project/create", createProjectForm)
        .then(res => {
          if (res.data === "Fail") {
            this.$Message.success("Fail");
          } else {
            console.log(res.data);
            this.createProjectId = res.data;
            // console.log(this.createProjectId);
            this.$Message.success("Success");
            this.addHistoryEvent(this.createProjectId);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    //创建历史纪录的函数
    addHistoryEvent(scopeId){
      // console.log("scopeId是"+scopeId);
      let form = {};
      let description = this.$store.state.userName + ' created a ' + this.formInline.category + ' project called ' + this.formInline.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      this.axios.post("/GeoProblemSolving/history/save?", "description="+ description + "&scopeId=" + scopeId)
          .then(res=> {
            console.log(res.data);
            if(res.data === 'Success'){
              this.$router.push({name: "Project"});
            }
          })
          .catch(err=> {
            console.log(err.data);
          })
    },
    addTag(tag) {
      this.formInline.tagList.push(tag);
      this.inputTag = "";
      // alert(111);
    },
    deleteTag(index) {
      this.formInline.tagList.splice(index, 1);
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      var imgcode = "";
      // 2,621,440   2M
      if (filesize > 2101440) {
        // 图片大于2MB
        this.$Message.error("size > 2MB");
      }
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = e => {
        // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
        imgcode = e.target.result;
        this.img = imgcode;
      };
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.img = "";
    }
  }
};
</script>
