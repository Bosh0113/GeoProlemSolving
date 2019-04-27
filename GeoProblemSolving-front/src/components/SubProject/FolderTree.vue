<style scoped>
.fileSpace{
    padding:100px
}
.folderContent{
    width: 300px;
    height: 300px;
    overflow-y: auto;
    border: 0.3px yellowgreen solid;
    padding: 5px;
    margin: 3px;
}
.folderDeleteBtn{
    cursor:pointer;
    color:red;
    float:right;
    margin: 0 5px;
}
.folderRenameBtn{
    cursor:pointer;
    color:blue;
    float:right;
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
                <p v-for="(file,index) in currentFolder.files" @click="getFileInfo(file)">{{file.name}}</p>
            </div>
        </div>
        <Button @click="addFolderModalShow">newFolder</Button>
        <Modal
            v-model="renameFolderModal"
            title="Rename folder"
        >
            <Form ref="renameValidate" :model="renameValidate" :rules="renameRuleValidate" :label-width="80">
                <FormItem label="New name" prop="newName">
                    <Input v-model="renameValidate.newName" type="textarea" :rows="4" placeholder="Enter the name for folder..." />
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
                    <Input v-model="newValidate.setName" type="textarea" :rows="4" placeholder="Enter the name for folder..." />
                </FormItem>
            </Form>
            <div slot="footer">
                <Button @click="newFolderModal=false">Cancel</Button>
                <Button type="success" @click="addFolder('newValidate')">New</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
export default {
    created(){
        this.getSubProjectInfo();
    },
    data(){
        return{
            subProjectFileStruct:{},
            subProjectId:"fbd438e0-b23b-49f6-93c8-36b678c70720",
            currentFolder:{},
            folderUIDStack:[],
            newFolderModal:false,
            setFolderName:"",
            newValidate:{
                setName:"",
            },
            newRuleValidate:{
                setName: [
                    {
                        required: true,
                        message: "The name can't be null.",
                        trigger: "blur"
                    }
                ]
            },
            renameFolderInfo:{},
            renameFolderModal:false,
            renameValidate:{
                newName:""
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
        }
    },
    methods:{
        getSubProjectInfo(){
            this.axios
            .get("/GeoProblemSolving/subProject/getFileStrcut"+"?subProjectId="+this.subProjectId)
            .then(res=>{
                var fileStruct = res.data;
                this.subProjectFileStruct = fileStruct;
                this.currentFolder = fileStruct;
                this.enterFolder(this.currentFolder);
            })
            .catch(err=>{
                console.log("Get sub-project's info fail.");
            });
        },
        enterFolder(folder){
            this.folderUIDStack.push(this.currentFolder.uid);
            this.currentFolder = folder;
        },
        backforeFolder(){
            if(this.folderUIDStack.length>1){
                var foreFolderUid= this.folderUIDStack.pop();
                this.refreshCurrentFolder(this.subProjectFileStruct,foreFolderUid);
            }else{
                this.$Message.warning("This is the root folder.");
            }
        },
        getFileInfo(file){
            confirm(file.uid);
        },
        addFolderModalShow(){
            this.newValidate.setName = "";
            this.newFolderModal = true;
        },
        addFolder(name){
            this.$refs[name].validate(valid => {
                if (valid) {
                    var currentFolderUid = this.currentFolder.uid;
                    var subProjectId = this.subProjectId;
                    var newFolderName = this.newValidate.setName;
                    this.axios
                    .post("/GeoProblemSolving/subProject/createFolder"+
                    "?subProjectId="+subProjectId+
                    "&parentId="+currentFolderUid+
                    "&folderName="+newFolderName)
                    .then(res=>{
                        if(res.data!="Fail"){
                            this.subProjectFileStruct = res.data;
                            this.refreshCurrentFolder(res.data,this.currentFolder.uid);
                            this.newFolderModal =false;
                        }else{
                            console.log("New folder fail.");
                        }
                    })
                    .catch(err=>{
                        console.log("New folder fail.");
                    });
                }
            });
        },
        refreshCurrentFolder(folder,uid){
            if(folder.uid == uid){
                this.currentFolder = Object.assign({},folder);
                return;
            }
            else{
                var subFolder = folder.folders;
                for(let i=0;i<subFolder.length;i++){
                    this.refreshCurrentFolder(subFolder[i],uid);
                }
            }
        },
        deleteFolder(folder){
            if(confirm("Are you sure to delete this folder?")){
                var currentFolderUid = this.currentFolder.uid;
                var subProjectId = this.subProjectId;
                var deleteFolderUid = folder.uid;
                this.axios
                .post("/GeoProblemSolving/subProject/deleteFolder"+
                "?subProjectId="+subProjectId+
                "&parentId="+currentFolderUid+
                "&folderUid="+deleteFolderUid)
                .then(res=>{
                    if(res.data!="Fail"){
                        this.subProjectFileStruct = res.data;
                        this.refreshCurrentFolder(res.data,this.currentFolder.uid);
                    }else{
                        console.log("Delete folder fail.");
                    }
                })
                .catch(err=>{
                    console.log("Delete folder fail.");
                });
            }
        },
        renameFolderModalShow(folder){
            this.renameFolderInfo = folder;
            this.renameValidate.newName = "";
            this.renameFolderModal = true;
        },
        renameFolder(name){
            this.$refs[name].validate(valid => {
                if (valid) {
                    var currentFolderUid = this.currentFolder.uid;
                    var subProjectId = this.subProjectId;
                    var oldFolderInfo = this.renameFolderInfo;
                    var newName = this.renameValidate.newName;
                    this.axios
                    .post("/GeoProblemSolving/subProject/renameFolder"+
                    "?subProjectId="+subProjectId+
                    "&parentId="+currentFolderUid+
                    "&folderName="+newName+
                    "&folderUid="+oldFolderInfo.uid)
                    .then(res=>{
                        if(res.data!="Fail"){
                            this.subProjectFileStruct = res.data;
                            this.refreshCurrentFolder(res.data,this.currentFolder.uid);
                        }else{
                            console.log("Rename fail.")
                        }
                    })
                    .catch(err=>{console.log("Rename fail.")});
                    this.renameFolderModal = false;
                }
            });
        }
    }
}
</script>
