<template>
  <div id="map" class="map" :style="{height:windowHeight+'px'}">
    <Modal
        v-model="modalExport"
        title="Export GeoJSON">
        <h3>Save this data into resouce center.<Button style="margin-left:50px">OK</Button></h3>
        <br>
        <h3>Download this data directly.<Button style="margin-left:50px" @click="downloadGeoJson">Download</Button></h3>
    </Modal>
    <Modal
        v-model="modalImport"
        title="Import GeoJSON"
        @on-ok="viewData">
        <Upload type="drag" :before-upload="handleUpload" action="-">
          <div style="padding: 20px 0">
              <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
              <p>Click or drag files here to upload</p>
          </div>
        </Upload>
        <div v-show="showFile">
          <span id="show-file">
            <i class="ivu-icon ivu-icon-md-document"></i>{{uploadDataName}}
          </span>
        </div>
        <br>
        <h3>Import data from resource center.<Button style="margin-left:20px">Go</Button></h3>
    </Modal>
  </div>
</template>
<style>
@import "../../../static/css/Control.MiniMap.css";
@import "../../../static/css/leaflet.pm.css";
#map {
  width: 100%;
}
#import-data{
  background-color:white;
  width:30px;
  height:30px;
  border-bottom:1px solid lightgray;
  cursor: pointer;
}
#import-data:hover{
  background-color:#f3f3f3;
  width:30px;
  height:30px;
  border-bottom:1px solid lightgray;
  cursor: pointer;
}
#export-data{  
  background-color:white;
  width:30px;
  height:30px;
  cursor: pointer;
}
#export-data:hover{  
  background-color:#f3f3f3;
  width:30px;
  height:30px;
  cursor: pointer;
}
</style>
<script>
import minimap from "../../../static/js/Control.MiniMap.min.js";
import pm from "../../../static/js/leaflet.pm.min.js";
import * as socketApi from "./../../api/socket.js";
//leaflet
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
// this part resolve an issue where the markers would not appear
delete L.Icon.Default.prototype._getIconUrl;
//请求带上cookie以防session丢失
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});
export default {
  components: {},
  methods: {
    initSize(){
      this.windowHeight = window.innerHeight;
    },
    initMap() {
      this.tdtVectorMap = "http://t0.tianditu.gov.cn/vec_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtVectorAno = "http://t0.tianditu.gov.cn/cva_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtImgMap = "http://t0.tianditu.gov.cn/img_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtImgAno = "http://t0.tianditu.gov.cn/cia_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtTerrMap = "http://t0.tianditu.com/ter_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
      this.tdtTerrAno = "http://t0.tianditu.com/cta_w/wmts?tk=d6b0b78f412853967d91042483385d2c"+
        "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";

      this.map = L.map("map",{
        crs:L.CRS.EPSG3857,
        center:L.latLng(32.07, 118.78),
        zoom:13
      });
    },
    initLayer() {      
      this.drawingLayerGroup = L.layerGroup([]);
      this.drawingLayerGroup.addTo(this.map);
    },
    initControl() {
      // 图层控件 
      var vectorMap = L.tileLayer(this.tdtVectorMap, { 
        maxZoom: 18,
        attribution:
            '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
        });
      var vectorAno = L.tileLayer(this.tdtVectorAno, { maxZoom: 18 });
      var vector = L.layerGroup([vectorMap,vectorAno]);

      var satelliteMap = L.tileLayer(this.tdtImgMap, { 
        maxZoom: 18,
        attribution:
            '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
        });      
      var satelliteAno = L.tileLayer(this.tdtImgAno, { maxZoom: 18 } );
      var satellite = L.layerGroup([satelliteMap,satelliteAno]);

      var terrainMap = L.tileLayer(this.tdtTerrMap, { 
        maxZoom: 18,
        attribution:
            '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
        });
      var terrainAno = L.tileLayer(this.tdtTerrAno, { maxZoom: 18 });
      var terrain = L.layerGroup([terrainMap,terrainAno]);

      this.baseLayers = {
        "Vector map": vector,
        "Satellite map":satellite,
        "Terrain map":terrain
        // "Google satellite map": googleSatellite
      };
      var overlayLayers = {};
      L.control.layers(this.baseLayers,overlayLayers).addTo(this.map);
      this.baseLayers["Vector map"].addTo(this.map);

      // 比例尺
      L.control
        .scale({
          position: "bottomleft"
        })
        .addTo(this.map);

      // 鹰眼
      var normal = L.tileLayer( this.tdtVectorMap, { maxZoom: 18 } );
      var miniMap = new L.Control.MiniMap(normal, {
        toggleDisplay: true,
        minimized: false,
        position: "bottomleft"
      }).addTo(this.map);

      // 绘图控件
      var options = {
        position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
        drawMarker: true, // adds button to draw markers
        drawPolyline: true, // adds button to draw a polyline
        drawRectangle: true, // adds button to draw a rectangle
        drawPolygon: true, // adds button to draw a polygon
        drawCircle: true, // adds button to draw a cricle
        cutPolygon: false, // adds button to cut a hole in a polygon
        editMode: false, // adds button to toggle edit mode for all layers
        dragMode:false,
        removalMode: true // adds a button to remove layers
      };
      this.map.pm.addControls(options);

      this.diyDataControl();
    },
    diyDataControl(){
      var that = this;
      L.Control.Data = L.Control.extend({
          //在此定义参数    
          options: {
            position: 'topright'
          },
          //在此初始化
          initialize: function (map) {
          },
          onAdd: function(map){
              this._container = L.DomUtil.create('div', 'leaflet-exportData');
              this._container.style= 'border:2px solid rgba(128,128,128,0.5);border-radius:6px';

              let importData = document.createElement('div');
              importData.id = 'import-data';
              importData.title = 'Import GeoJSON';
              importData.onclick = this._importData;
              let iconImport = document.createElement('img');
              iconImport.src="../../../static/Images/import.png";
              iconImport.style='margin-left: 3.5px;margin-top: 3px';
              importData.appendChild(iconImport);
      
              let exportData = document.createElement('div');
              exportData.id = 'export-data';
              exportData.title = 'Export GeoJSON';
              exportData.onclick = this._exportData;
              let iconExport = document.createElement('img');
              iconExport.src="../../../static/Images/export.png";
              iconExport.style='margin-left: 3.5px;margin-top: 3px';
              exportData.appendChild(iconExport);

              this._container.appendChild(importData);
              this._container.appendChild(exportData);
              return this._container;
          },
          _exportData(){
            var featuresSet= {"type":"FeatureCollection","features":[]};
            that.map.eachLayer(function(layer){
              try{
                var json = layer.toGeoJSON();
                if(json.type == "Feature"){
                  featuresSet.features.push(json);
                }
              }
              catch(e){}
            });
            if(featuresSet.features.length>0){
              that.geojsonBlob = new Blob([JSON.stringify(featuresSet,null,2)],{type:'application/json'});
              that.modalExport = true;
            }
          },
          _importData(){
            that.modalImport = true;
          },
          
      });
      L.control.data = function(){
        return new L.Control.Data();
      }
      L.control.data().addTo(this.map);
    },
    downloadGeoJson(){
      var reader = new FileReader();
      if(this.geojsonBlob != null){

        reader.readAsDataURL(this.geojsonBlob);
        reader.onload = function (e) {
          var a = document.createElement('a');
          a.download = 'geodata.json';
          a.href = e.target.result;
          $("body").append(a);
          a.click();
          $(a).remove();
        }
      }
    },
    handleUpload (file) {
      var that = this;   
      var reader = new FileReader();
      this.uploadDataName = file.name;
      reader.readAsText(file, "UTF-8");
      reader.onload = function(e) {
        let json = e.target.result;
        try{
          that.uploadGeoJson = JSON.parse(json);
          that.showFile = true;
        }
        catch(e) {
          that.showFile = false;
          that.uploadGeoJson = null;
          alert("Data format error.");
        }
      };
      reader.onerror = function () {
        alert("Input data error.");
        that.showFile = false;
        that.uploadGeoJson = null;
      };
      return false;
    },
    viewData(){
      // 协同  
      this.send_content={
        type:"uploaddata",
        layer: this.uploadGeoJson
      }
      this.socketApi.sendSock(this.send_content, this.getSocketConnect);

      let geoJsonLayer = L.geoJSON(this.uploadGeoJson, {
        style: function (feature) {
            return {color: "green"};
        }
      }).bindPopup(function (layer) {
          return layer.feature.properties.description;
      });      
      this.drawingLayerGroup.addLayer(geoJsonLayer);      

      this.showFile = false;
      this.uploadGeoJson = null;
    },
    listenDraw() {
      this.send_content = {};
      let isMouseDown = false;
      let isZoomControl = false;
      let isDoubleClick = false;
      let isLayerCtrlClick = false;

      this.map.on("mousedown",e=>{
        isMouseDown = true;
      });

      this.map.on("mouseup",e=>{
        isMouseDown = false;
        isLayerCtrlClick = true;
      });

      this.map.on("dblclick",e=>{
        isDoubleClick = true;
      });

      //缩放控件事件
      var element = document.querySelector('a.leaflet-control-zoom-in');
      L.DomEvent.addListener(element, 'click', function (e) {
          isZoomControl = true;
      });
      element = document.querySelector('a.leaflet-control-zoom-out');
      L.DomEvent.addListener(element, 'click', function (e) {
          isZoomControl = true;
      });

      // 图层控件
      this.map.on("baselayerchange",e=>{
        if(isLayerCtrlClick){
          this.send_content={
              type:"overlay",
              layer: e.name
            }
          this.socketApi.sendSock(this.send_content, this.getSocketConnect);
        }
        isLayerCtrlClick = false;
      });
     
      //缩放事件 与 鼠标事件同时发生
      this.map.on("zoomend", e=>{
        if(this.map.scrollWheelZoom || isZoomControl || isDoubleClick){      
          this.send_content={
            type:"zoom",
            zoom: this.map.getZoom()
          }
          this.socketApi.sendSock(this.send_content, this.getSocketConnect);
          isZoomControl = false;
          isDoubleClick = false;
        }
      });

      //地图拖拽事件
      this.map.on("moveend", e=>{
        if(isMouseDown){
          this.send_content={
            type:"move",
            center:this.map.getCenter()
          }
          this.socketApi.sendSock(this.send_content, this.getSocketConnect);
        }
      });

      // 裁剪事件
      this.map.on("pm:cut",e=>{
        // this.drawingLayerGroup.removeLayer(e.originalLayer);
        // this.drawingLayerGroup.addLayer(e.layer);
        // this.send_content={
        //     type:"cut",
        //     layer: this.drawingLayerGroup.toGeoJSON()
        //   }
        // this.socketApi.sendSock(this.send_content, this.getSocketConnect);
      });
      
      // 删除事件
      this.map.on("pm:remove",e=>{
        this.drawingLayerGroup.removeLayer(e.layer);
        this.send_content={
            type:"remove",
            layer: this.drawingLayerGroup.toGeoJSON()
          }
        this.socketApi.sendSock(this.send_content, this.getSocketConnect);
      });

      // 画图事件
      this.map.on("pm:create", e => {
        this.drawingLayerGroup.addLayer(e.layer);
        let points = null;
        switch (e.shape) {
          case "Marker":
            this.traces = [];
            points = e.layer._latlng;
            this.traces.push(points);
            break;
          case "Line":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            break;
          case "Rectangle":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            break;
          case "Poly":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            break;
          case "Circle":
            this.traces = [];
            points = e.layer._latlng;
            this.traces.push(points);
            let radius = e.layer._mRadius;
            this.traces.push(radius);            
            break;
        }
        
        this.send_content={
          type:"add",
          shape:e.shape,
          layer:this.traces
        }
        this.socketApi.sendSock(this.send_content, this.getSocketConnect);
      });
    },
    getSocketConnect(data) {
      let socketMsg = data;

      if (socketMsg.type === "test") {
        console.log(socketMsg.content);
      }
      else if(socketMsg.type === "members") {}
      else {
        //判断消息的发出者
        
          switch (socketMsg.type){            
            case "zoom":{
              this.map.setZoom(socketMsg.zoom);
              break;
            }
            case "move":{              
              this.map.panTo(socketMsg.center);
              break;
            }
            case "overlay":{
              try{
                this.map.removeLayer(this.baseLayers["Terrain map"]);
              }
              catch (e){}
              try{
                this.map.removeLayer(this.baseLayers["Satellite map"]);
              }
              catch (e){}
              try{
                this.map.removeLayer(this.baseLayers["Vector map"]);
              }
              catch (e){}
              this.baseLayers[socketMsg.layer].addTo(this.map);
              break;
            }
            case "remove": {
              this.drawingLayerGroup.clearLayers();
              let geoJson = socketMsg.layer;
              let geoJsonLayer = L.geoJSON(geoJson, {
                style: function (feature) {
                }
              }).bindPopup(function (layer) {
                  // return layer.feature.properties.description;
              });
              this.drawingLayerGroup.addLayer(geoJsonLayer);
              break;
            }
            case "add":{
              let drawingLayer = null;
              switch (socketMsg.shape) {
                case "Marker":
                  drawingLayer= L.marker(socketMsg.layer[0]);
                  break;
                case "Line":
                  drawingLayer = L.polyline(socketMsg.layer);
                  break;
                case "Rectangle":
                  drawingLayer = L.rectangle(socketMsg.layer);
                  break;
                case "Poly":
                  drawingLayer = L.polygon(socketMsg.layer);
                  break;
                case "Circle":
                  drawingLayer = L.circle(socketMsg.layer[0],{radius:socketMsg.layer[1]});
                  break;
              }
              this.drawingLayerGroup.addLayer(drawingLayer);

              break;
            }
            case "cut": {
              // this.drawingLayerGroup.clearLayers();
              // let geoJson = socketMsg.layer;
              // let geoJsonLayer = L.geoJSON(geoJson, {
              //   style: function (feature) {
              //   }
              // }).bindPopup(function (layer) {
              //     return layer.feature.properties.description;
              // });
              // this.drawingLayerGroup.addLayer(geoJsonLayer);
              // break;
            }
            case "uploaddata":{
              let geoJson = socketMsg.layer;
              let geoJsonLayer = L.geoJSON(geoJson, {
                style: function (feature) {
                    return {color: "green"};
                }
              }).bindPopup(function (layer) {
                  return layer.feature.properties.description;
              });
              this.drawingLayerGroup.addLayer(geoJsonLayer);
              break;
            }
          }
      }
    },
    startWebSocket() {
      let roomId = sessionStorage.getItem("moduleId");
      this.socketApi.initWebSocket("MapServer/" + roomId);

      this.send_content= {
        type:"test",
        from: "Test",
        content: "TestChat"
      };
      this.socketApi.sendSock(this.send_content, this.getSocketConnect);
    }
  },
  mounted() {
    this.initSize();
    this.initMap();
    this.initLayer();
    this.initControl();
    this.startWebSocket();
    this.listenDraw();
    window.addEventListener("resize", this.initSize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.initSize);
    this.socketApi.close();
  },
  beforeRouteEnter: (to, from, next) => {
    // alert(this.isSubProjectMember);
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({name:"Login"});
      } else {

      }
    });
  },
  data() {
    return {
      windowHeight:800,
      modalExport:false,
      modalImport:false,
      map: null,   
      baseLayers: null,
      traces: {},
      send_content: {},
      //geojson blob
      geojsonBlob: null,
      showFile:false,
      uploadDataName:"",
      uploadGeoJson:null,
      //存储绘制的图像layer
      drawingLayerGroup:null

    };
  }
};
</script>
