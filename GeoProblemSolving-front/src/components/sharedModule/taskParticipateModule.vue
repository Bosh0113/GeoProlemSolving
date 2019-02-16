<template>
  <Row>
    <!-- <Col span="4" class="Resource"> -->
      <div class = "member_panel" :style="{maxHeight:resourceHeight}" >
        <div class="title">Participants</div>
        <div class="member-desc" v-for = "member in partipants" :key="member.index">
          <div class = "member-image">{{member.name}}</div>
          <div class = "memebr-work">
            <div class = "area">{{member.area}}</div>
            <div class = "task" >{{member.work}}</div>
          </div>
        </div>
      </div>
    <!-- </Col> -->
  </Row>
</template>
<style scoped>
.member_panel {
  border:1px solid lightgray;
  margin-top:50px
  /* 200px */
  /* margin-top: 200px; */
  /* background-color: lightblue; */
  /* height:100%; */
}
.title{
  height:40px;
  line-height:40px;
  text-align:center;
  font-size:20px;
  font-weight:bold;
  background-color: #2db7f5;
}
.member-desc{
  height:80px;
  /* width:100%; */
  display:flex;
  border:1px dotted lightgray
}
.member-image{
  width:25%;
  margin:10px;
  border:1px dotted black
  /* background-color: chartreuse; */
  /* height:100%; */
  /* text-align:center; */
}
.memebr-work{
  width:75%;
  margin:10px;
  border:1px dotted black

  /* background-color: lightcoral; */
}
.member-work{
  padding:10px;
}
.task{
  /* padding:2.5px 5px 2.5px 5px; */
  height:40px
}
.area{
  /* padding:2.5px 5px 2.5px 5px; */
  height:20px
}
</style>
<script>
export default {
  methods: {
    init() {
      this.resourceHeight = window.innerHeight - 250 + "px";
    }
  },
  data() {
    return {
      resourceHeight: "",

    };
  },
  created() {
    this.init();
  }
};
</script>
