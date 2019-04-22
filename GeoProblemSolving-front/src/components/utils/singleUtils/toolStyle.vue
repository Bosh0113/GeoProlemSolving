<template>
  <div style="display:flex" :style="{height:bodyHeight}">
    <div class="sidebar">
      <div style="display:flex;justify-content:center;margin-top:20px">
        <span @click="membersDrawer=true" style="cursor:pointer"><Icon type="ios-contacts" :size="40" color="white"/>
        </span>
        <Drawer title="Group members" :closable="false" v-model="membersDrawer" placement="left">
        <p>Some contents...</p>
        <p>Some contents...</p>
        <p>Some contents...</p>
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
    <div class="toolContent">
      <h1 style="text-align:center">
        Tools float here.
      </h1>
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
