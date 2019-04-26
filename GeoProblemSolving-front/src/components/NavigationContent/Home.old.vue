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
  border: solid 0.5px aqua;
  padding: 5px;
}
.single_feature_image {
  margin: 1%;
  width: 48%;
  border: solid 0.5px green;
  padding: 5px;
}
.function_card {
  height: 215px;
  /* background-color: lightcoral; */
  border: 1px solid black;
  margin-top: 10%;
  min-width: 300px;
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
.panel {
  width: 100%;
  height: 500px;
  display: inline-block;
}
.image {
  background-image: "./../../assets/images/1.jpg";
}

.title-text {
  color: white;
  font-size: 3rem;
  text-align: left;
  padding: 0 2.5rem;
  position: relative;
}

.title-des {
  color: #fff;
  font-size: 1rem;
  display: inline-block;
  padding: 0.5rem 2.5rem;
  text-align: left;
}

.imgContent {
  max-width: 550px;
  max-height: 550px;
  overflow: hidden;
  /* background-color: lightgray; */
}
.imgContent img {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
  border-radius: 6px;
}
.section {
  padding: 4rem 2rem;
}

.feature {
  text-align: left;
  display: flex;
  align-items: center;
  vertical-align: middle;
}

.feature.first {
  background: linear-gradient(135deg, #0079bf, #5067c5);
  padding: 4rem 0;
  margin-bottom: 2rem;
}

.feature .textContent {
  padding: 0 4rem;
}

.feature .textContent h3 {
  font-size: 2.1875rem;
}

.feature .textContent p {
  font-size: 1rem;
  padding: 20px 0;
}

.vertical-center {
  display: flex;
  align-items: center;
  vertical-align: middle;
}
.register-btn {
  display: inline-block;
  padding: 1rem 2.5rem;
}
.register-btn .ivu-btn{
  font-size: 1rem;
}
</style>
<template>
  <div class="home">
    <Row class="feature first section">

      <Col
        span="10"
        offset="2"
      >
      <div>
        <h3 class="title-text">
          A Platform for Solving Complex Geographic Problems
        </h3>
        <span class="title-des">This is a collaborative platform for solving complex problems, gathering people's efforts to promote the progress of scientific research.</span>
        <span class="register-btn"><Button type="success">Register for free！</Button></span>
      </div>
      </Col>
      <Col span="10">
      <div class="imgContent">
        <img src="./../../assets/images/first.png" />
      </div>
      </Col>

      <!-- <div style="width:40%;display:flex;justify-content:center;align-items:center">
          <div style="padding:100px;">
            <h1 style="color:white;font-size: 3rem;">
            A platform for solving complex geographic problems
            </h1>
          </div>
          </div> -->

    </Row>
    <!-- <Row>
      <Col span = "20" offset ="2" style="margin-top:60px">
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
    </Row> -->

    <Row class="feature section">

      <Col
        span="10"
        offset="2"
      >
      <div class="imgContent">
        <img src="./../../assets/images/projects1.png" />
      </div>
      </Col>
      <Col span="10">
      <div class="textContent">
        <div>
          <h3>{{this.createProjectsTitle}}</h3>
          <p>Choose proper area to create projects so as to solve complex geographical problems.</p>
        </div>
      </div>
      </Col>

    </Row>

    <Row class="feature section">
      <Col
        span="10"
        offset="2"
      >
      <div class="textContent">
        <div>
          <h3>{{this.manageProjectsTitle}}</h3>
          <p>Freely create to-do items, doing items and done items, update project progress freely.</p>
        </div>
      </div>
      </Col>
      <Col span="10">
      <div class="imgContent">
        <img src="./../../assets/images/task1.png" />
      </div>
      </Col>
    </Row>

    <Row class="feature section">
      <Col
        span="10"
        offset="2"
      >
      <div class="imgContent">
        <img src="./../../assets/images/tools1.png" />
      </div>
      </Col>
      <Col span="10">
      <div class="textContent ">
        <h3>{{this.toolsTitle}}</h3>
        <p>Variable tools help you process data or design geographical concepts in different scene.</p>
      </div>
      </Col>
    </Row>

    <Row class="feature section">
      <div class="Carsoule"></div>
    </Row>

    <Row class="section">
      <div class="function_part">
        <Col
          span="20"
          offset="2"
        >
        <div style="margin-top:20px">
          <div class="feature_title">Function Modules</div>
          <Row>
            <Col
              :lg="{ span: 7, offset: 1 }"
              :md="{ span: 11, offset: 1}"
              :sm="{ span: 22, offset: 1}"
              :xs="{ span: 22, offset: 1}"
              v-for="module in module_lists"
              :key="module.index"
            >
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
    <!-- <Row style="margin-top:20px">
      <Col
        span="20"
        offset="2"
      >
      <div class="function_part">
        <div class="function_title">Example lists</div>
        <Row>
          <Col
            :lg="{ span: 7, offset: 1 }"
            :md="{ span: 11, offset: 1}"
            :sm="{ span: 22, offset: 1}"
            :xs="{ span: 22, offset: 1}"
            v-for="list in example_show_lists"
            :key="list.index"
          >
          <div class="function_card">
            <div class="card_title">{{list.name}}</div>
            <div class="card_body">
              <div class="card_image">
                <img :src="list.src">
                
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
    </Row> -->

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      // msg: "Welcome to Home",
      manageProjectsTitle: "Manage projects easily",
      createProjectsTitle: "Create projects in different areas",
      toolsTitle: "Multi-purpose tools",
      imagelist: 0,
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


