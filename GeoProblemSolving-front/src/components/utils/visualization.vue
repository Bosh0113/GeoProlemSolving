<template>
  <Row>
    <Col span="22" offset="1">
      <div style="margin-top:60px">
        <input type="file" ref="upload" accept=".xls, .xlsx, .csv" class="outputlist_upload">
        <br>
        <span>Chart Catregory</span>
        <Select
          v-model="chooseType"
          style="width:200px;margin-left:20px"
          :placeholder="chartTypePlaceholder"
        >
          <Option v-for="item in normalChart" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <Button
          v-model="chooseType"
          type="primary"
          @click="configPanelShow(chooseType)"
          style="margin-left:20px"
        >Config Chart</Button>
        <Input
          v-model="hintText"
          type="textarea"
          :rows="3"
          style="width:600px;margin-left:20px"
          readonly
        />
        <div v-show="configPanelModal==true">
          <span>Choose X property</span>
          <br>
          <CheckboxGroup v-model="xData">
            <Checkbox :label="item" v-for="(item,index) in columnsName" :key="index"></Checkbox>
          </CheckboxGroup>
          <br>
          {{this.xData}}
          <br>
          <span>Choose Y property</span>
          <br>
          <CheckboxGroup v-model="yData">
            <Checkbox
              :label="item"
              v-for="(item,index) in columnsName"
              :key="index"
              :disabled="judgeSame(item)"
            ></Checkbox>
          </CheckboxGroup>
          <br>
          {{this.yData}}
          <Button type="default" @click="showChart">show</Button>
        </div>
      </div>
      <div style="margin-top:200px"></div>
      <ve-chart :data="chartData" :settings="chartSettings"></ve-chart>
      <ve-scatter :data="chartData2"></ve-scatter>
    </Col>
  </Row>
</template>
<style scoped>
</style>
<script>
import XLSX from "xlsx";
export default {
  data() {
    return {
      columnsName: [],
      columnsShow: [],
      cellValue: [],
      outputs: [],
      chartSettings: {
        // type: 'line'
      },
      chartData: [],
      selectProperty: [],

      chartData: {},
      //普通图表的候选项
      normalChart: [
        { value: "line", label: "line" },
        { value: "histogram", label: "histogram" },
        { value: "bar", label: "bar" },
        { value: "pie", label: "pie" },
        { value: "ring", label: "ring" },
        { value: "waterfall", label: "waterfall" },
        { value: "radar", label: "radar" },
        { value: "funnel", label: "funnel" },
        { value: "scatter", label: "scatter" }
      ],
      chartData2:{
        columns: ['日期', '访问用户','下单用户'],
          rows: [
            { '日期': '1/1', '访问用户': 120,  '下单用户': 100 },
            { '日期': '1/2', '访问用户': 101,  '下单用户': 50 },
            { '日期': '1/3', '访问用户': 200,  '下单用户': 30 },
            { '日期': '1/4', '访问用户': 112, '下单用户': 65 },
            { '日期': '1/5', '访问用户': 123,  '下单用户': 75 },
            { '日期': '1/6', '访问用户': 132,  '下单用户': 90 }
          ]
      },
      chartTypePlaceholder: "Choose a type to show",
      lineStyle: false,
      chooseType: "",
      dropDownShow: false,
      // x轴选中的值
      xData: [],
      // y轴选中的值
      yData: [],
      // 配置模态框
      configPanelModal: false,
      hintText:
        "Most of the charts in v-charts are single-dimension multiple metrics(such as name,date,etc), so the default first value is the dimension and the remaining values are metrics.You can choose by yourself."
    };
  },
  mounted() {
    this.$refs.upload.addEventListener("change", e => {
      this.readExcel(e);
    });
  },
  methods: {
    readExcel(e) {
      //表格导入
      this.xData = [];
      this.yData = [];
      this.chartData = [];
      this.chartSettings = {};
      var that = this;
      const files = e.target.files;
      // console.log(files);
      if (files.length <= 0) {
        //如果没有文件名
        return false;
      } else if (!/\.(xls|xlsx|csv)$/.test(files[0].name.toLowerCase())) {
        this.$Message.error("上传格式不正确，请上传xls或者xlsx,csv格式");
        return false;
      }

      const fileReader = new FileReader();
      fileReader.onload = ev => {
        try {
          const data = ev.target.result;
          const workbook = XLSX.read(data, {
            type: "binary"
          });
          const wsname = workbook.SheetNames[0]; //取第一张表
          const ws = XLSX.utils.sheet_to_json(workbook.Sheets[wsname]); //生成json表格内容
          that.outputs = ws; //清空接收数据
          console.log("数据的行数是：" + that.outputs.length);
          this.getExcelData();
          this.$refs.upload.value = "";
        } catch (e) {
          return false;
        }
      };
      fileReader.readAsBinaryString(files[0]);
    },
    getExcelData() {
      this.columnsName = Object.keys(this.outputs[0]);
      this.cellValue = this.outputs;
      console.log(JSON.stringify(this.cellValue));
      let _Array = [];
      this.columnsName.forEach(function(item) {
        _Array.push(item);
      });
      this.dropDownShow = true;
      this.columnsShow = _Array;
      console.log(JSON.stringify(this.columnsShow));

    },
    configPanelShow(data) {
      this.configPanelModal = true;
      this.xData = [];
      this.yData = [];
    },
    judgeSame(item) {
      if (this.xData.includes(item)) {
        return true;
      } else {
        return false;
      }
    },
    showChart() {
      var xDataString = this.xData.join("");
      var yDataString = this.yData.join("");
      if (this.chooseType === "waterfall") {
        this.chartSettings = {
          type: this.chooseType,
          metrics: yDataString,
          dimension: xDataString
        };
        this.chartData = {
          columns: [xDataString,yDataString],
          rows: this.cellValue
        };
        console.log(this.chartSettings);
      } else if (this.chooseType == "scatter") {
        this.chartSettings = {
          type: "scatter",
          // metrics: [this.columnsShow[0],this.columnsShow[1]],
          // dimension: xDataString
        };
        this.chartData = {
          columns: this.columnsShow,
          rows: this.cellValue
        };
      console.log(this.chartSettings);
      }else if(this.chooseType == "funnel"){
        this.chartSettings = {
          type: this.chooseType,
          metrics: yDataString,
          dimension: xDataString,
          ascending: true,
          useDefaultOrder:true,
        };
        this.chartData = {
        columns: this.columnsShow,
        rows: this.cellValue
      }
      }else {
        this.chartSettings = {
          type: this.chooseType,
          metrics: this.yData,
          dimension: this.xData
        };
        this.chartData = {
        columns: this.columnsShow,
        rows: this.cellValue
      };
      }
    }
  }
};
</script>
