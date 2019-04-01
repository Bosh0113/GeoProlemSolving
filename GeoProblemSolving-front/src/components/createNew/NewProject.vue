<style scoped>
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
/* .hintTitle {
  text-align: center;
  width: 20%;
} */
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
  /* height: 200px; */
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
<template>
  <div class="project form">
    <h1>New Project</h1>
    <div>
      <Form ref="formInline" :model="formInline" :rules="newProjectRule" class="projectForm">
      <!-- 选择类别 -->
      <FormItem prop="category" label="category" :label-width="100">
          <RadioGroup v-model="formInline.category" style="width:80%">
            <Radio label="Water"></Radio>
            <Radio label="Soil"></Radio>
            <Radio label="Ecology"></Radio>
            <Radio label="Atmosphere"></Radio>
            <Radio label="Society"></Radio>
            <Radio label="Others"></Radio>
          </RadioGroup>
      </FormItem>
      <FormItem prop="title" label="title" :label-width="100">
          <Input v-model="formInline.title" placeholder="Enter Title..."/>
      </FormItem>
      <FormItem prop="description" label="description" :label-width="100">
          <Input
            v-model="formInline.description"
            placeholder="Enter a brief introduction ..."
          />
      </FormItem>
      <FormItem prop="privacy" label="Privacy" :label-width="100">
          <RadioGroup v-model="formInline.privacy" style="width:80%;margin-left:10px" >
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

      </FormItem>
      <FormItem prop="tag" label="tag" :label-width="100">
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
      </FormItem>
      <FormItem prop="image" label="image" :label-width="100">
        <div class="inline_style">
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
      <FormItem prop="introduction" label="introduction" :label-width="100">
        <div class="inline_style">
            <Input v-model="formInline.introduction" type="textarea" placeholder="Enter introduction..." style="height:100%;width:100%"/>
        </div>
      </FormItem>
      <FormItem>
        <div class="inline_style">
          <Button type="success" @click='createProject("formInline")' class="create">Create</Button>
        </div>
      </FormItem>
    </Form>
    </div>

  </div>
</template>
<script>
export default {
  data() {
    return {
      formInline: {
        title: "",
        category: "",
        introduction: "",
        privacy: "Public",
        description: "",
        //tag列表
        tagList: []
      },
      newProjectRule: {
        title: [
          {
            required: true,
            message: "The title cannot be empty",
            trigger: "blur"
          }
        ],
        category: [
          {
            required: true,
            message: "Please select category",
            trigger: "change"
          }
        ],
        privacy: [
          {
            required: true,
            message: "Please select privacy",
            trigger: "change"
          }
        ],
        introduction:[
          {
            required: true,
            message: "give a detailed introduction about this project",
            trigger: "blur"
          }
        ],
        description:[
          {
            required: true,
            message: "The description cannot be empty and no more than 40 words",
            trigger: "blur"
          }
        ]
      },
      //用来存储输入的单个标签变量
      inputTag: "",
      visible: false,
      //表示图片
      img: "",
      createProjectId: ""
    };
  },

  methods: {
    createProject(name) {
      this.$refs[name].validate(valid => {
        if(valid){
          let createProjectForm = {};
          createProjectForm["category"] = this.formInline.category;
          createProjectForm["title"] = this.formInline.title;
          createProjectForm["tag"] = this.formInline.tagList.toString();
          createProjectForm["privacy"] = this.formInline.privacy;
          createProjectForm["picture"] = this.img;
          createProjectForm["introduction"] = this.formInline.introduction;
          createProjectForm["description"] = this.formInline.description;
          createProjectForm["managerId"] = this.$store.getters.userId;
           
          this.axios
            .post("/GeoProblemSolving/project/create", createProjectForm)
            .then(res => {
              if (res.data === "Fail") {
                this.$Message.success("Fail");
              } else {
                this.createProjectId = res.data;
                this.$Message.success("Success");
                this.addHistoryEvent(this.createProjectId);
              }
            })
            .catch(err => {
              console.log(err);
            });
        }else{

        }
      });
    },
    //创建历史纪录的函数
    addHistoryEvent(scopeId) {
       
      let form = {};
      let description =
        this.$store.getters.userName +
        " created a " +
        this.formInline.category +
        " project called " +
        this.formInline.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      this.axios
        .post(
          "/GeoProblemSolving/history/save?",
          "description=" +
            description +
            "&scopeId=" +
            scopeId +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
           
          if (res.data === "Success") {
            this.$router.push({ name: "Project" });
          }
        })
        .catch(err => {
          console.log(err.data);
        });
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
  },
  created() {
    // 加入判断，如果未登录自动跳转登录页面
    if (!this.$store.getters.userState) {
      this.$router.push({ name: "Login" });
    }
  }
};
</script>
