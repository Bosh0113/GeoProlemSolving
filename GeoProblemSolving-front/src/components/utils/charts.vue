<style>
@import "../../../static/css/jquery.jexcel.css";
</style>
<template>
  <Row>
    <Col span="24" >
      <Col span="4" style="padding:30px" :style="{height:sidebarHeight}">
        <input type="file" ref="upload" accept=".xls, .xlsx .csv" class="outputlist_upload" style="padding:20px 0 20px 0">
        <h3 style="padding-top:20px">Type of chart:</h3>
        <Select v-model="chooseType" style="width:200px;padding:20px 0 20px 0" :placeholder="chartTypePlaceholder">
          <Option v-for="item in normalChart" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <Button>Visualization</Button>
        
      </Col>
      <Col span="19" offset="1" style="padding-top:30px" :style="{height:sidebarHeight}">
        <div id="mytable" style="height:400px"></div>
      </Col>
    </Col>
  </Row>
</template>
<script>
import csv from "../../../static/js/jquery.csv.min.js";
import jexcel from "../../../static/js/jquery.jexcel.js";
import XLSX from "xlsx";
export default {
  data() {
    return {
      sidebarHeight: "",
      testData: [
      ],
      columnHeader: [],
      excelData: [],
      columnNameList: [],
      // 选中的参考值
      selectName: [],  
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
      chooseType: "",
      chartTypePlaceholder: "Choose one type of charts"
    };
  },
  methods: {
    init() {
      // this.sidebarHeight = window.innerHeight - 100 + "px";
      $("#mytable").jexcel({
        data: this.testData,
        minDimensions: [20, 20]
      });
    },
    download() {
      $("#mytable").jexcel("download");
    },
    destroy() {
      $("#mytable").jexcel({
        data: $.fn.jexcel("helper", {
          action: "createEmptyData",
          cols: 10,
          rows: 10
        })
      });
    },
    readExcel(e) {
      //表格导入
      var that = this;
      const files = e.target.files;
      if (files.length <= 0) {
        //如果没有文件名
        return false;
      } else if (!/\.(xls|xlsx|csv)$/.test(files[0].name.toLowerCase())) {
        this.$Message.error("上传格式不正确，请上传xls或者xlsx格式");
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
          const ws = XLSX.utils.sheet_to_json(workbook.Sheets[wsname]);
          let arr = Object.keys(ws[0]);
          this.columnNameList = arr;
          //生成json表格内容
          let list1 = [];
          let list2 = [];
          for (var i = 0; i < ws.length; i++) {
            for (var key in ws[i]) {
              list1.push(ws[i][key]);
            }
            list2[i] = list1;
            list1 = [];
          }
          that.columnHeader = arr;
          that.excelData = list2;
          $("#mytable").jexcel({
            data: that.excelData,
            colHeaders: arr,
            tableOverflow: false,
            minDimensions: [20, 20],
            csvHeaders: true,
          });
          this.$refs.upload.value = "";
        } catch (e) {
          return false;
        }
      };
      fileReader.readAsBinaryString(files[0]);
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
          type: this.chooseType,
          metrics: [this.columnsShow[0],this.columnsShow[1]],
          dimension: xDataString
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
  },
  mounted() {
    this.$refs.upload.addEventListener("change", e => {
      //绑定监听表格导入事件
      this.readExcel(e);
    });
    this.init();
  }
};
</script>



