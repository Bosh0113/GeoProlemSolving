<template>
<Row>
  <Col span="22" offset="1">
    <div class="help">
    <ul>
    	<li v-for="(item,index) in showList" :key='index'>
    		<h1>{{item.personName}}:<span>{{item.viewName}}</span>,{{item.Date}},{{item.reason}}{{item.adress}}</h1>
    	</li>
    </ul>
    <Page :total="dataCount" :page-size="pageSize" show-total @on-change="changepage" show-elevator />
    <br>
    <br>
  </div>
  </Col>
</Row>

</template>

<script>

export default {
    name: "Help",

    data() {
        return {
            msg: "Welcome to Help",
            datalist2:[],
            pageSize:10,
            dataCount:0,
            showList:[],
            // totalProblemList:[]
        };
    },

    mounted(){
      // this.showdata()
    },

    methods:{
      showdata(){
        this.axios.get('/users')
        .then((res) =>{
          console.log(res.data.data);
          this.datalist2 = res.data.data;
          this.dataCount = this.datalist2.length
          if(this.dataCount<this.pageSize){
            this.showList = this.datalist2;
          }else{
            this.showList = this.datalist2.slice(0,this.pageSize)
          }
          // console.log(this.datalist2.length)
        })
        .catch(function (err) {
          cosnole.log('调用失败',err);
        })
      },
      changepage(index){
        var _start = (index - 1) * this.pageSize;
        var _end = index * this.pageSize;
        this.showList = this.datalist2.slice(_start,_end);
      }
    }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
    font-weight: normal;
}
</style>
