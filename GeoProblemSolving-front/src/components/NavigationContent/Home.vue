<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
  font-weight: normal;
}
.image_carousel {
  margin-top: 20px;
  width: 100%;
  height: 550px;
  border: 1px solid red;
}
.image_carousel img {
  margin: 5%;
  max-width: 100%;
  /* width:90%; */
  max-height: 440px;
}
.feature_part {
  margin-top: 20px;
}
.feature_title,
.function_title {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}
.single_feature {
  display: flex;
  height: 400px;
}
.single_feature_content {
  margin: 1%;
  width: 48%;
  margin-top: 100px;
  margin-bottom: 100px;
  min-height: 200px;
  background-color: aqua;
}
.single_feature_image {
  margin: 1%;
  width: 48%;

  background-color: chartreuse;
}
.function_card {
  height: 200px;
  /* background-color: lightcoral; */
  border: 1px solid black;
  margin-top: 10%;
}
.card_title {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid black;
}
.card_image {
  width: 50%;
  padding: 2.5%;
  /* height:140px; */
}
img {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
}
.card_content {
  width: 50%;
  height: auto;
  padding: 2.5%;
}
.card_body {
  display: flex;
}
.card_content p {
  /* margin-top:2%; */
  font-size: 15px;
  font-weight: bold;
}
.panel{
  width:100%;
  height:500px;
  display:inline-block;

}
.image{
  background-image:"./../../assets/images/1.jpg"
}
</style>
<template>
  <div class="home">
    <Row>
      <Col span = "20" offset ="2" style="margin-top:10px">
        <Carousel v-model="imagelist" loop >
        <CarouselItem>
            <div class="demo-carousel">
              <img class = "panel" src="./../../assets/images/1.jpg"/>
            </div>
        </CarouselItem>
        <CarouselItem>
            <div class="demo-carousel ">
              <img class = "panel" src="./../../assets/images/2.jpg"/>
            </div>
        </CarouselItem>
        <CarouselItem>
            <div class="demo-carousel ">
              <img class = "panel" src="./../../assets/images/3.jpg"/>
            </div>
        </CarouselItem>
        <CarouselItem>
            <div class="demo-carousel ">
              <img class = "panel" src="./../../assets/images/4.jpg"/>
            </div>
        </CarouselItem>
    </Carousel>
      </Col>
    </Row>
    <Row>
      <div class="feature_part">
        <Col span="20" offset="2">
          <div>
            <div class="feature_title">Features Page</div>
            <div class="feature_content">
              <div class="single_feature">
                <div class="single_feature_image">image</div>
                <div class="single_feature_content">Introduction</div>
              </div>
              <div class="single_feature">
                <div class="single_feature_image">image</div>
                <div class="single_feature_content">Introduction</div>
              </div>
              <div class="single_feature">
                <div class="single_feature_image">image</div>
                <div class="single_feature_content">Introduction</div>
              </div>
              <div class="single_feature">
                <div class="single_feature_image">image</div>
                <div class="single_feature_content">Introduction</div>
              </div>
            </div>
          </div>
        </Col>
      </div>
    </Row>
    <Row>
        <div class="function_part">
          <Col span = "20" offset = "2">
          <div style="margin-top:20px">
             <div class="feature_title">Function Modules</div>
             <Row>
               <Col span = "7" offset="1" v-for="module in module_lists" :key="module.index">
                <div class="function_card">
                <div class="card_title">{{module.name}}</div>
                <div class="card_body">
                  <div class="card_image">
                    <img :src="module.src">
                    <!-- <img src="./../../assets/module/prepare.png"> -->
                  </div>
                  <div class="card_content">
                    <p>The preparation module includes collecting resources, sharing resources, and determining research objects.</p>
                  </div>
                </div>
              </div>
               </Col>
            </Row>
          </div>

          </Col>
        </div>

    </Row>
    <Row style="margin-top:20px">
      <Col span="20" offset="2">
        <div class="function_part">
          <div class="function_title">Example lists</div>
          <Row>
            <Col span="7" offset="1" v-for="list in example_show_lists" :key="list.index">
              <div class="function_card">
                <div class="card_title">{{list.name}}</div>
                <div class="card_body">
                  <div class="card_image">
                    <img :src="list.src">
                    <!-- <img src="./../../assets/module/prepare.png"> -->
                  </div>
                  <div class="card_content">
                    <p>The preparation module includes collecting resources, sharing resources, and determining research objects.</p>
                  </div>
                </div>
              </div>
            </Col>
          </Row>
        </div>
      </Col>
    </Row>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      // msg: "Welcome to Home",
      imagelist:0,
      image_index: 0,
      example_show_lists: [
        {
          name: "Water System",
          desc: "",
          src: require("./../../assets/example/water.png")
        },
        {
          name: "Soil System",
          desc: "",
          src: require("./../../assets/example/soil.png")
        },
        {
          name: "Ecology System",
          desc: "",
          src: require("./../../assets/example/ecology.png")
        },
        {
          name: "Human Society System",
          desc: "",
          src: require("./../../assets/example/society.png")
        },
        {
          name: "Atomsphere System",
          desc: "",
          src: require("./../../assets/example/atmosphere.png")
        }
      ],
      module_lists: [
        {
          name: "Preparatory Module",
          desc: "",
          src: require("./../../assets/module/prepare.png")
        },
        {
          name: "Analysis Module",
          desc: "",
          src: require("./../../assets/module/analyze.png")
        },
        {
          name: "Modeling Module",
          desc: "",
          src: require("./../../assets/module/modeling.png")
        },
        {
          name: "Simulation Module",
          desc: "",
          src: require("./../../assets/module/simulation.png")
        },
        {
          name: "Comparison Module",
          desc: "",
          src: require("./../../assets/module/comparation.png")
        },
        {
          name: "Verification Module",
          desc: "",
          src: require("./../../assets/module/vertification.png")
        }
      ]
    };
  }
};
</script>


