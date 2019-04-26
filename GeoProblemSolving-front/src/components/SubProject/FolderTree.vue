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
            <a v-for="(folder,index) in currentFolder.folders" :key="index" @click="enterFolder(folder)">{{folder.name}}</a>
            <p v-for="(file,index) in currentFolder.files" @click="getFileInfo(file)">{{file.name}}</p>
        </div>
    </div>
</template>
<script>
export default {
    created(){
        this.currentFolder = this.subProjectFiles;
        this.enterFolder(this.currentFolder);
    },
    data(){
        return{
            currentFolder:{},
            folderStack:[],
            subProjectFiles:{
                "files": [{
                    "name": "0_readme412098.txt",
                    "uid": "4b09d875-b7d1-4617-b25f-4013377feabb"
                }],
                "folders": [{
                    "files": [{
                        "name": "NSA_SSA_DEM1033912.txt",
                        "uid": "cba382e6-ac83-485a-86cf-73a9828f02bf"
                    },
                    {
                        "name": "NSA_SSA_DEM893872.txt",
                        "uid": "47336869-d4b5-4d87-b3e4-8ba4358bcf73"
                    }],
                    "folders": [{
                        "files": [],
                        "floders":[],
                        "name":"last",
                        "uid":"947dc95d-95c0-4315-88c0-a5b5211efb6c"
                    }],
                    "name": "文件夹1",
                    "uid": "4b09d875-b7d1-4617-b25f-4013377feabc"
                }],
                "uid": "4128126e-5aa9-4764-b13c-478715dd0f36"
            },
        }
    },
    methods:{
        enterFolder(folder){
            this.folderStack.push(this.currentFolder);
            this.currentFolder = folder;
        },
        backforeFolder(){
            if(this.folderStack.length>1){
                this.currentFolder = this.folderStack.pop();
            }else{
                confirm("no fore folder.");
            }
        },
        getFileInfo(file){
            confirm(file.uid);
        }
    }
}
</script>
