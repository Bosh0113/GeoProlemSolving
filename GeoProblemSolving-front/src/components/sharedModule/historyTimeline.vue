<template>
  <div>
    <div class="title"></div>
    <div class = "history">
      <Timeline>
        <TimelineItem color="green">发布1.0版本</TimelineItem>
        <TimelineItem color="green">发布2.0版本</TimelineItem>
        <TimelineItem color="red">严重故障</TimelineItem>
        <TimelineItem color="blue">发布3.0版本</TimelineItem>
    </Timeline>

    </div>
  </div>
</template>
<style>

</style>
<script>
export default {
  methods:{
    setColor(data){
      //这里根据去后台查询,主要包含文件上传历史，所在项目组变化的历史，从事工作的历史纪录。
      switch(data){

      }
    }
  },
  data(){
    return{
      History:
      [

      ],

    }
  }
}
</script>
