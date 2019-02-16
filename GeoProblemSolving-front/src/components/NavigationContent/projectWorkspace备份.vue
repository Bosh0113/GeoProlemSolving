<template>
<div class = "detail">
  <div class = "right">
    <!-- <div class = "module-edit-group">
        <Button type="success" class="util-btn" shape="circle"><Icon type="md-contacts" size="20"  class="util-btn-icon"/></Button>
        <Button type="danger" class="util-btn" shape="circle"><Icon type="ios-albums" size="20"  class="util-btn-icon"/></Button>
    </div> -->
    <br>
    <Button type="info" @click="modal1 = true">Add</Button>
    <Modal v-model="modal1" title="add new task node" @on-ok="submit()" @on-cancel="cancel()">
      <Input v-model="node_name" placeholder="Enter something..." style="width: 100px" />
    </Modal>
    <!-- <Button type="error" @click="delData"  = true">Del</Button> -->
    <Button type="error" @click="del_modal = true">Del</Button>
    <Modal v-model="del_modal" title="delete task" @on-ok="delData()" @on-cancel="cancel()">
      <p>Do you really want to delete this step?</p>
    </Modal>
    <br>

    <Steps :current="order" size="small">
        <Step v-for="(list,index) in datalist2" :key="index" @click.native="showDetail(index)" :title="list.name" :order="index"></Step>
    </Steps>
    <Tabs :value="taborder">
        <TabPane v-for="(list,index) in datalist2" :key="index" @click="change(index)" :name="String(index)" animated="false">
          <Row>
            <Col span = "4">
              <div class = "member_panel" :style="{maxHeight:sidebarHeight}" >
                <div class="title">Participants</div>
                <div class="member-desc" v-for = "member in partipants" :key="member.index">
                  <div class = "member-image">{{member.name}}</div>
                  <div class = "memebr-work">
                    <div class = "area">{{member.area}}</div>
                    <div class = "task" >{{member.work}}</div>
                  </div>
                </div>
              </div>
            </Col>
            <Col span = "4" offset="1">
              <resource class="resource"></resource>
            </Col>
            <Col span = "1" offset="13" class="util-panel">
              <div class = "util-btn-group">
              <Button type="info" class="util-btn" shape="circle"><Icon type="md-contacts" size="20"  class="util-btn-icon"/></Button>
              <Button type="info" class="util-btn" shape="circle"><Icon type="ios-albums" size="20"  class="util-btn-icon"/></Button>
              </div>
            </Col>
          </Row>
        </TabPane>
    </Tabs>
  </div>
</div>
</template>
<style>
.detail{
  display:flex;
}
.sidebar{
  background-color:#515a6e;
  /* height:100%; */
  color:white;
  font-weight:bold;
  width:15%;
}
.right{
  width:90%;
  margin-left:5%;
  height:100%;
}
.single_part{
  margin-left:2.5%;
  margin-right:2.5%;

}
.member_panel {
  border:1px solid lightgray;
  margin-top:50px
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
}
.member-work{
  padding:10px;
}
.task{
  height:40px
}
.area{
  height:20px
}
.util-panel{
  height:200px;
  box-shadow: 5px 5px 3px 2px rgba(0,0,0,.3);
  /* border: 1px solid yellow; */
  /* background-color: lightgreen; */
}
.util-panel:hover{

  transition: all 1s;
}
.util-btn{
  /* 使按钮垂直居中即可 */
  display:block;
  margin:40px auto;
  width: 40px;
  height: 40px;
  text-align:center;
  transition: all 1s;
}
.util-btn-icon{
      width: 40px;
    /* height: 40px; */
    margin-left: -15px;

}
.util-btn:hover{
    transform: scale(1.25);
    transition: all 1s;

}
.resource:hover{
  box-shadow: 2px 2px 2px 2px rgba(0,0,0,.3);
  transform: scale(1.1);
  transition: all 1s;
}
.resource{
  transition: all 1s;
}
</style>
<script>
import {VueFlowy, FlowChart} from 'vue-flowy'
import taskParticipateModule from './.././sharedModule/taskParticipateModule'
import resourceModule from './.././sharedModule/resourceModule'
import * as socketApi from './../../api/socket'
function test1(){
  alert("success");
}
export default {
  components:{
    VueFlowy,
    memberPart :taskParticipateModule,
    resource :resourceModule
  },
  data(){
    return{
        sidebarHeight: '',
        datalist:["1","2","3"],
        datalist2:[
          {name:"Water resource collection"},
          {name:"Water data analysement"},
          {name:"water models building"},
          {name:"water models simulation"}
        ],
        partipants:[
        {name:'lyc',work:"collect data",area:'gis'},
        {name:'xdw',work:"collect data",area:'gis'},
        {name:'mzy',work:"collect data",area:'gis'},
        {name:'zbc',work:"collect data",area:'gis'},
        {name:'wls',work:"collect data",area:'gis'},
      ],
        order: 0,
        current: 0,
        modal1:false,
        del_modal:false,
        node_name:'',
        order: 0,
        taborder: 0,
        // chart适用
        chart: new FlowChart()
    }
  },
  created(){
    this.init();
  },
  methods:{
    init(){
      this.sidebarHeight = window.innerHeight-60+'px';
      console.log(this.sidebarHeight);
      // this.$Message.config({
      //     top: 300,
      //     duration: 3
      // });
    },
    showDetail(item){
      // alert(item);
      this.order = item;
      console.log(typeof(this.order));
      let change = String(item);
      console.log(typeof(change))
      this.taborder = change;
    },

    addData(){
      let obj = {name:this.node_name}
      this.datalist2.push(obj);

      // this.socketApi.sendSock(this.node_name,test())
    },
    delData(){
      let length = this.datalist2.length;
      this.datalist2.splice(this.order,1);
      this.$Message.info('Deleted');
    },



    //自己设置的手工添加的函数
    submit(){
      let obj = {order:"5",name:this.node_name}
      this.datalist2.splice(this.order,0,obj);
      // this.datalist2.push(obj);

    },
    cancel(){
      this.$Message.info('Clicked cancel');
    },

  },
  mounted() {

  },
}
</script>

