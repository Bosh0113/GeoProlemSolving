<style scoped>

</style>
<template>
    <div class="fileSpace">

    </div>
</template>
<script>
export default {
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
                        "name":"last.txt",
                        "uid":"947dc95d-95c0-4315-88c0-a5b5211efb6c"
                    }],
                    "name": "文件夹1",
                    "uid": "4b09d875-b7d1-4617-b25f-4013377feabc"
                }],
                "uid": "4128126e-5aa9-4764-b13c-478715dd0f36"
            }
        }
    },
    methods:{
        lastFolderUid(){

        },
        getFileInfo(resourceId){

        },
        getFolderInfo(FolderUid){

        }
    }
}
</script>
