<template>
    <div class="sidebar" :style="{height:bodyHeight}" style="width:60px;float:left">
      <div id="toolMembers" style="display:flex;justify-content:center;margin-top:20px" title="Online participants">
        <span @click="membersDrawer=true" style="cursor:pointer"><Icon type="ios-contacts" :size="40" color="white"/>
        </span>
        <Drawer title="Online participants" :closable="false" v-model="membersDrawer" placement="left">
        <ul>
          <li v-for="(participant,index) in participants" :key="index" >{{participant.userName}}</li>
        </ul>
        </Drawer>
      </div>
      <div id="toolResources" style="display:flex;justify-content:center;margin-top:20px" title="Resources">
        <span @click="resourceDrawer=true" style="cursor:pointer"><Icon type="md-folder" :size="40" color="white"/></span>
        </span>
        <Drawer title="Resources" :closable="false" v-model="resourceDrawer" placement="left">
        <ul>
          <li v-for="(resource,index) in resources" :key="index" @click="selectResource(resource.pathURL)" style="cursor:pointer" :title="resource.description">{{resource.name}}</li>
        </ul>
        </Drawer>
      </div>
    </div>
</template>
<style scoped>
.sidebar{
  width:60px;
  background-color:#515a6e;
  height:100%;
  justify-content:center;
  position: absolute
}
.toolContent{
  flex:1;
  height:100%
}
</style>
<script>
export default {
  props:{
    participants: Array,
    resources: Array
  },
  data(){
    return{
      bodyHeight:window.innerHeight +'px',
      membersDrawer:false,
      resourceDrawer:false,
      // olParticipants: this.participants,
      // resources: this.resource,
    }
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.reSize);
  },
  methods:{
    reSize() {
      this.bodyHeight = window.innerHeight +'px';
    },
    selectResource(url){
      this.$emit("resourceUrl",url);
    }
  }
}
</script>
