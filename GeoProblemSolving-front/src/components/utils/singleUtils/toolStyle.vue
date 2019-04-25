<template>
  <div style="display:flex" :style="{height:bodyHeight}">
    <div class="sidebar">
      <div style="display:flex;justify-content:center;margin-top:20px">
        <span @click="membersDrawer=true" style="cursor:pointer"><Icon type="ios-contacts" :size="40" color="white"/>
        </span>
        <Drawer title="Group members" :closable="false" v-model="membersDrawer" placement="center">
          <div class="memberList">
            <span style="font-size:1rem">Lyc</span>
          </div>
          <div class="memberList">
            <span style="font-size:1rem">Mzy</span>
          </div>
          <div class="memberList">
            <span style="font-size:1rem">Zbc</span>
          </div>
          <div class="memberList">
            <span style="font-size:1rem">Xdw</span>
          </div>
        </Drawer>
      </div>
      <div style="display:flex;justify-content:center;margin-top:20px">
        <span @click="resourceDrawer=true" style="cursor:pointer"><Icon type="md-folder" :size="40" color="white"/></span>
        </span>
        <Drawer title="Resource collection" :closable="false" v-model="resourceDrawer" placement="left">
        <p>Some contents...</p>
        <p>Some contents...</p>
        <p>Some contents...</p>
        </Drawer>
      </div>
    </div>
  </div>
</template>
<style scoped>
.sidebar{
  width:60px;
  background-color:#515a6e;
  height:100%;
  justify-content:center
}
.toolContent{
  flex:1;
  height:100%
}
.memberList{
  margin-bottom:10px;
  height:30px;
  padding-left:10px;
  line-height:30px;
}
.memberList:hover{
  background-color:lightgray
}
</style>
<script>
export default {
  data(){
    return{
      bodyHeight:window.innerHeight +'px',
      membersDrawer:false,
      resourceDrawer:false,
    }
  },
  methods:{

  }
}
</script>
