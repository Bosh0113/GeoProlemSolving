<style scoped>
.fileSpace{
    padding:100px
}
.folderContent{
    width: 300px;
    height: 100px;
    overflow-y: auto;
    border: 0.3px yellowgreen solid;
    padding: 5px;
    margin: 3px;
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
                    <span @click="deleteFolder(folder)" style="cursor:pointer;color:red;float:right">x</span>
                </div>
                <p v-for="(file,index) in currentFolder.files" @click="getFileInfo(file)">{{file.name}}</p>
            </div>
        </div>
        <Input v-model="newFolderName" placeholder="The name of new folder" style="width: 200px"/><Button @click="addFolder">newFolder</Button>
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
            folderStack:[],
            newFolderName:"",
        }
    },
    methods:{
        getSubProjectInfo(){
            this.axios
            .get("/GeoProblemSolving/subProject/inquiry"+"?key=subProjectId"+"&value="+this.subProjectId)
            .then(res=>{
                var subProjectInfo = res.data[0];
                this.subProjectFileStruct = JSON.parse(subProjectInfo.fileStruct);
                this.currentFolder =  JSON.parse(subProjectInfo.fileStruct);
                this.enterFolder(this.currentFolder);
            })
            .catch(err=>{
                console.log("Get sub-project's info fail.");
            });
        },
        enterFolder(folder){
            this.folderStack.push(this.currentFolder.uid);
            this.currentFolder = folder;
        },
        backforeFolder(){
            if(this.folderStack.length>1){
                var foreFolderUid= this.folderStack.pop();
                this.refreshCurrentFolder(this.subProjectFileStruct,foreFolderUid);
            }else{
                confirm("no fore folder.");
            }
        },
        getFileInfo(file){
            confirm(file.uid);
        },
        addFolder(){
            var currentFolderUid = this.currentFolder.uid;
            var subProjectId = this.subProjectId;
            var newFolderName = this.newFolderName;
            this.axios
            .post("/GeoProblemSolving/subProject/createFolder"+
            "?subProjectId="+subProjectId+
            "&parentId="+currentFolderUid+
            "&folderName="+newFolderName)
            .then(res=>{
                if(res.data!="Fail"){
                    this.subProjectFileStruct = res.data;
                    this.refreshCurrentFolder(res.data,this.currentFolder.uid);
                    this.newFolderName="";
                }else{
                    console.log("New folder fail.");
                }
            })
            .catch(err=>{
                console.log("New folder fail.");
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
    }
}
</script>
