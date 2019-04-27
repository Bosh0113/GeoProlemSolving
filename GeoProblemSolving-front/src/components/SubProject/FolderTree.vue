<style scoped>
.fileSpace {
  padding: 100px;
}
.folderContent {
  width: 500px;
  height: 300px;
  overflow-y: auto;
  border: 0.3px yellowgreen solid;
  padding: 5px;
  margin: 3px;
}
.folderDeleteBtn {
  cursor: pointer;
  color: red;
  float: right;
  margin: 0 5px;
}
.folderRenameBtn {
  cursor: pointer;
  color: blue;
  float: right;
  margin: 0 5px;
}
</style>
<template>
    <div class="fileSpace">
        <h1>Folder Tree</h1>
        <Button @click="backforeFolder">back</Button>
        <div class="folderContent">
            <div v-show="currentFolder!='{}'">
                <div v-for="(folder,index) in currentFolder.folders" :key="index">
                    <a @click="enterFolder(folder)" class="folderItem">{{folder.name}}</a>
                    <span @click="deleteFolder(folder)" class="folderDeleteBtn">x</span>
                    <span @click="renameFolderModalShow(folder)" class="folderRenameBtn">=</span>
                </div>
                <p v-for="(file,index) in currentFileList" @click="getFileInfo(file)">{{file.name}}------{{file.fileSize}}</p>
            </div>
        </div>
        <Button @click="addFolderModalShow">newFolder</Button>
        <Button @click="uploadModalShow" title="upload resource"><Icon type="md-cloud-upload" size="20"/></Button>
        <Modal
            v-model="renameFolderModal"
            title="Rename folder"
        >
            <Form ref="renameValidate" :model="renameValidate" :rules="renameRuleValidate" :label-width="80">
                <FormItem label="New name" prop="newName">
                    <Input v-model="renameValidate.newName" :rows="4" placeholder="Enter the name for folder..." />
                </FormItem>
            </Form>
            <div slot="footer">
                <Button @click="renameFolderModal=false">Cancel</Button>
                <Button type="success" @click="renameFolder('renameValidate')">Rename</Button>
            </div>
        </Modal>
        <Modal
            v-model="newFolderModal"
            title="New folder"
        >
            <Form ref="newValidate" :model="newValidate" :rules="newRuleValidate" :label-width="80">
                <FormItem label="Set name" prop="setName">
                    <Input v-model="newValidate.setName" :rows="4" placeholder="Enter the name for folder..." />
                </FormItem>
            </Form>
            <div slot="footer">
                <Button @click="newFolderModal=false">Cancel</Button>
                <Button type="success" @click="addFolder('newValidate')">New</Button>
            </div>
        </Modal>
        <Modal v-model="uploadModal" title="Upload file" width="600">
            <Form ref="uploadValidate" :model="uploadValidate" :rules="uploadRuleValidate" :label-width="100"  label-position="left" >
                <FormItem label="Privacy" prop="privacy">
                    <RadioGroup v-model="uploadValidate.privacy" style="width:80%">
                        <Radio label="private">Private</Radio>
                        <Radio label="public">Public</Radio>
                    </RadioGroup>
                </FormItem>
                <FormItem label="Type" prop="type">
                    <RadioGroup v-model="uploadValidate.type">
                        <Radio label="data"></Radio>
                        <Radio label="paper"></Radio>
                        <Radio label="document"></Radio>
                        <Radio label="model"></Radio>
                        <Radio label="image"></Radio>
                        <Radio label="video"></Radio>
                        <Radio label="others"></Radio>
                    </RadioGroup>
                </FormItem>
                <FormItem label="Description" prop="description">
                    <Input
                        type="textarea"
                        :rows="4"
                        v-model="uploadValidate.description"
                    />
                </FormItem>
            </Form>
            <Upload
                :max-size="1024*1024"
                multiple
                type="drag"
                :before-upload="gatherFile"
                action="-"
                >
                <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>Click or drag file here to upload</p>
                </div>
            </Upload>
            <div style="padding:0 10px 0 10px">
                <ul v-for="(list,index) in toUploadFiles" :key="index">
                    <li style="display:flex">
                    filename:
                    <span style="font-size:10px;width:60%">{{ list.name }}</span>
                    size:
                    <span
                        style="font-size:10px;width:70%"
                    >{{list.fileSize}}</span>
                    <Icon
                        type="ios-close"
                        size="20"
                        @click="delFileList(index)"
                        style="display:flex;justify-content:flex-end;cursor:pointer"
                    ></Icon>
                    </li>
                </ul>
            </div>
            <div slot="footer">
                <Button @click="uploadModal=false">Cancel</Button>
                <Button type="success" @click="subProjectUpload('uploadValidate')">Upload</Button>
            </div>
        </Modal>
        <Modal
            v-model="progressModalShow"
            title="Upload Progress"
            :mask-closable="false"
            :closable="false"
        >
            <Progress :percent="uploadProgress"></Progress>
        </Modal>
    </div>
</template>
<script>
export default {
  created() {
    this.getSubProjectfileStruct();
  },
  data() {
    return {
      subProjectFileStruct: {},
      subProjectId: "fbd438e0-b23b-49f6-93c8-36b678c70720",
      currentFolder: {},
      currentFileList: [],
      folderUIDStack: [],
      newFolderModal: false,
      setFolderName: "",
      newValidate: {
        setName: ""
      },
      newRuleValidate: {
        setName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur"
          }
        ]
      },
      renameFolderInfo: {},
      renameFolderModal: false,
      renameValidate: {
        newName: ""
      },
      renameRuleValidate: {
        newName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur"
          }
        ]
      },
      uploadModal: false,
      uploadValidate: {
        privacy: "private",
        type: "data",
        description: ""
      },
      uploadRuleValidate: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur"
          }
        ],
        type: [
          {
            required: true,
            message: "file type cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ]
      },
      toUploadFiles: [],
      progressModalShow: false,
      uploadProgress: 0
    };
  },
  methods: {
    getSubProjectfileStruct() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/getFileStrcut" +
            "?subProjectId=" +
            this.subProjectId
        )
        .then(res => {
          if (res.data != "Fail") {
            var fileStruct = res.data;
            this.subProjectFileStruct = fileStruct;
            this.currentFolder = fileStruct;
            this.enterFolder(this.currentFolder);
          } else {
            this.$Message.warning("Get sub-project's info fail.");
          }
        })
        .catch(err => {
          this.$Message.warning("Get sub-project's info fail.");
        });
    },
    enterFolder(folder) {
      this.folderUIDStack.push(this.currentFolder.uid);
      this.currentFolder = folder;
      this.getCurrentFilesInfo();
    },
    getCurrentFilesInfo() {
      var files = this.currentFolder.files;
      var count = files.length;
      var filesInfoList = [];
      if(files.length>0){
        for (var i = 0; i < files.length; i++) {
          var fileId = files[i].uid;
          this.axios
            .get(
              "/GeoProblemSolving/resource/inquiry" +
                "?key=resourceId" +
                "&value=" +
                fileId
            )
            .then(res => {
              if (res != "Fail") {
                var fileInfo = res.data[0];
                filesInfoList.push(fileInfo);
                if (--count == 0) {
                  this.$set(this, "currentFileList", filesInfoList);
                }
              } else {
                console.log("Get file info fail.");
              }
            })
            .catch(err => {
              console.log("Get file info fail.");
            });
        }
      }else{
        this.$set(this, "currentFileList", filesInfoList);
      }
    },
    backforeFolder() {
      if (this.folderUIDStack.length > 1) {
        var foreFolderUid = this.folderUIDStack.pop();
        this.refreshCurrentAll(this.subProjectFileStruct, foreFolderUid);
      } else {
        this.$Message.warning("This is the root folder.");
      }
    },
    getFileInfo(file) {
      confirm(file.pathURL);
    },
    addFolderModalShow() {
      this.newValidate.setName = "";
      this.newFolderModal = true;
    },
    addFolder(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var currentFolderUid = this.currentFolder.uid;
          var subProjectId = this.subProjectId;
          var newFolderName = this.newValidate.setName;
          this.axios
            .post(
              "/GeoProblemSolving/subProject/createFolder" +
                "?subProjectId=" +
                subProjectId +
                "&parentId=" +
                currentFolderUid +
                "&folderName=" +
                newFolderName
            )
            .then(res => {
              if (res.data != "Fail") {
                this.subProjectFileStruct = res.data;
                this.refreshCurrentAll(res.data, this.currentFolder.uid);
                this.newFolderModal = false;
              } else {
                this.$Message.warning("New folder fail.");
              }
            })
            .catch(err => {
              this.$Message.warning("New folder fail.");
            });
        }
      });
    },
    refreshCurrentFolder(folder, uid) {
      if (folder.uid == uid) {
        this.currentFolder = Object.assign({}, folder);
        return;
      } else {
        var subFolder = folder.folders;
        for (let i = 0; i < subFolder.length; i++) {
          this.refreshCurrentFolder(subFolder[i], uid);
        }
      }
    },
    refreshCurrentAll(folder, uid){
      this.refreshCurrentFolder(folder, uid);
      this.getCurrentFilesInfo();
    },
    deleteFolder(folder) {
      if (confirm("Are you sure to delete this folder?")) {
        var currentFolderUid = this.currentFolder.uid;
        var subProjectId = this.subProjectId;
        var deleteFolderUid = folder.uid;
        this.axios
          .post(
            "/GeoProblemSolving/subProject/deleteFolder" +
              "?subProjectId=" +
              subProjectId +
              "&parentId=" +
              currentFolderUid +
              "&folderUid=" +
              deleteFolderUid
          )
          .then(res => {
            if (res.data != "Fail") {
              this.subProjectFileStruct = res.data;
              this.refreshCurrentAll(res.data, this.currentFolder.uid);
            } else {
              this.$Message.warning("Delete folder fail.");
            }
          })
          .catch(err => {
            this.$Message.warning("Delete folder fail.");
          });
      }
    },
    renameFolderModalShow(folder) {
      this.renameFolderInfo = folder;
      this.renameValidate.newName = "";
      this.renameFolderModal = true;
    },
    renameFolder(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var currentFolderUid = this.currentFolder.uid;
          var subProjectId = this.subProjectId;
          var oldFolderInfo = this.renameFolderInfo;
          var newName = this.renameValidate.newName;
          this.axios
            .post(
              "/GeoProblemSolving/subProject/renameFolder" +
                "?subProjectId=" +
                subProjectId +
                "&parentId=" +
                currentFolderUid +
                "&folderName=" +
                newName +
                "&folderUid=" +
                oldFolderInfo.uid
            )
            .then(res => {
              if (res.data != "Fail") {
                this.subProjectFileStruct = res.data;
                this.refreshCurrentAll(res.data, this.currentFolder.uid);
              } else {
                this.$Message.warning("Rename fail.");
              }
            })
            .catch(err => {
              this.$Message.warning("Rename fail.");
            });
          this.renameFolderModal = false;
        }
      });
    },
    uploadModalShow() {
      this.toUploadFiles = [];
      this.uploadModal = true;
    },
    gatherFile(file) {
      let that = this;
      if (that.toUploadFiles.length >= 5) {
        this.$Message.info("最多只能上传5个文件");
      } else {
        var fileSize = file.size;
        if (fileSize < 1024) {
          file.fileSize = fileSize + "b";
        } else if (fileSize < 1024 * 1024) {
          file.fileSize = Math.round((fileSize / 1024) * 100) / 100 + "Kb";
        } else {
          file.fileSize =
            Math.round((fileSize / (1024 * 1024)) * 100) / 100 + "Mb";
        }
        that.toUploadFiles.push(file);
      }
      return false;
    },
    delFileList(index) {
      this.toUploadFiles.splice(index, 1);
    },
    subProjectUpload(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.uploadModal = false;
          var uploadFiles = this.toUploadFiles;
          if (uploadFiles.length > 0) {
            var formData = new FormData();
            for (var i = 0; i < uploadFiles.length; i++) {
              formData.append("file", uploadFiles[i]);
            }
            formData.append("description", this.uploadValidate.description);
            formData.append("type", this.uploadValidate.type);
            formData.append("uploaderId", this.$store.getters.userInfo.userId);
            formData.append("belong", sessionStorage.getItem("subProjectName"));
            let scopeObject = {
              projectId: sessionStorage.getItem("projectId"),
              subProjectId: sessionStorage.getItem("subProjectId"),
              moduleId: ""
            };
            formData.append("scope", JSON.stringify(scopeObject));
            formData.append("privacy", this.uploadValidate.privacy);
            formData.append(
              "subProjectId",
              sessionStorage.getItem("subProjectId")
            );
            formData.append("parentId", this.currentFolder.uid);
            this.progressModalShow = true;
            this.axios({
              url: "/GeoProblemSolving/resource/subProjectUpload",
              method: "post",
              onUploadProgress: progressEvent => {
                this.uploadProgress =
                  ((progressEvent.loaded / progressEvent.total) * 100) | 0;
              },
              data: formData
            })
              .then(res => {
                if (res.data != "Fail") {
                  this.subProjectFileStruct = res.data;
                  this.refreshCurrentAll(res.data, this.currentFolder.uid);
                  this.progressModalShow = false;
                } else {
                  this.$Message.warning("Upload fail.");
                }
              })
              .catch(err => {
                this.$Message.warning("Upload fail.");
              });
          }
        }
      });
    }
  }
};
</script>
