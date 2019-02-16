<template>
  <div id="map" class="map"></div>
</template>
<style>
@import "../../../static/css/Control.MiniMap.css";
@import "../../../static/css/leaflet.pm.css";
#map {
  width: 100%;
  height: 600px;
}
</style>
<script>
// import { LMap, LTileLayer, LMarker, LPopup } from "vue2-leaflet";
import minimap from "../../../static/js/Control.MiniMap.min.js";
import pm from "../../../static/js/leaflet.pm.min.js";
export default {
  components: {},
  methods: {
    initMap() {
      this.map = L.map("map").setView(this.center, this.zoom);
    },
    initLayer() {
      this.baseLayer = L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        maxZoom: 18,
        attribution:
          '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      });
      this.baseLayer.addTo(this.map);
    },
    initControl() {
      // 图层控件
      var satellite = L.tileLayer(
        "http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}",
        {
          maxZoom: 18,
          attribution:
            '&copy; <a href="http://www.google.com/maps/">GoogleMap</a> contributors'
        }
      );
      var baseLayers = {
        basemap: this.baseLayer,
        satellite: satellite
      };
      L.control.layers(baseLayers).addTo(this.map);

      // 比例尺
      L.control
        .scale({
          position: "bottomleft"
        })
        .addTo(this.map);

      // 鹰眼
      var normal = L.tileLayer(
        "http://www.google.cn/maps/vt?lyrs=m@189&gl=cn&x={x}&y={y}&z={z}",
        {
          maxZoom: 18,
          attribution:
            '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        }
      );
      var miniMap = new L.Control.MiniMap(normal, {
        toggleDisplay: true,
        minimized: false,
        position: "bottomleft"
      }).addTo(this.map);

      // 绘图控件
      var options = {
        position: "topright", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
        drawMarker: true, // adds button to draw markers
        drawPolyline: true, // adds button to draw a polyline
        drawRectangle: true, // adds button to draw a rectangle
        drawPolygon: true, // adds button to draw a polygon
        drawCircle: true, // adds button to draw a cricle
        cutPolygon: false, // adds button to cut a hole in a polygon
        editMode: false, // adds button to toggle edit mode for all layers
        removalMode: true // adds a button to remove layers
      };
      // add leaflet.pm controls to the map
      this.map.pm.addControls(options);
    },
    listenDraw() {
      this.map.on("pm:create", e => {
        console.log(e);
        let points = null;
        this.shape = e.shape;
        switch (this.shape) {
          case "Marker":
            this.traces = [];
            points = e.layer._latlng;
            this.traces.push(points);
            console.log(this.traces);
            break;
          case "Line":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            console.log(this.traces);
            break;
          case "Rectangle":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            console.log(this.traces);
            break;
          case "Poly":
            this.traces = [];
            points = e.layer._latlngs;
            this.traces.push(points);
            console.log(this.traces);
            break;
          case "Circle":
            this.traces = [];
            points = e.layer._latlng;
            this.traces.push(points);
            let radius = e.layer._radius;
            this.traces.push(radius);
            console.log(this.traces);
            break;
        }
      });

      this.map.on("pm:remove", e => {
          console.log(e);
        });
    }
  },
  created() {},
  mounted() {
    this.initMap();
    this.initLayer();
    this.initControl();
    this.listenDraw();
  },
  data() {
    return {
      map: null,
      baseLayer: null,
      zoom: 13,
      center: L.latLng(32.07, 118.78),
      shape: null,
      traces: [],
      send_content: []
    };
  }
};
</script>
