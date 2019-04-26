<template>
  <div class="home">
    <div class="home-screen">
      <!-- <img src="../../assets/images/new/bg.jpg"> -->
      <div class="bg"></div>
      <div class="mask"></div>
      <div class="content">
        <div>
          <span class="title">一站式地理问题解决平台</span>
          <span class="desc">Build,Explore,Connect</span>
          <span class="desc"> 助你解决地理相关问题(小一点做跳转)</span>
        </div>
      </div>
    </div>

    <div class="main-container">

      <div>
        Geo-problemSloving is a live Paltform.
        Our work let public or researchers build a new wolrd to explore by 密切的交流。
        Real-time Updates，Total 自主可控 and develop 快速。
      </div>

      流程图

      <div class="cases">
        <Row class="case-content">
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img1">

          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-planet" />
              <div>Planet</div>
            </div>
          </div>
          </Col>
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img2">
          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-flame" />
              <div>Fire</div>
            </div>
          </div>
          </Col>
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img3">
          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-leaf" />
              <div>Leaf</div>
            </div>
          </div>
          </Col>
        </Row>
      </div>

      <div class="tools">
        探索更多功能
        <div class="card">
          <img src="#">
          <span>Data Visualization</span>
          <span>MAP， chart XXXXXX The fastest, most flexible tools for visualizing geospatial data — available on the web, desktop, and mobile.</span>
          <span>了解更多</span>
        </div>
        <div class="card">
          <img src="#">
          <span>Real-time  协同交流</span>
          <span>ChatRoom 视频 协同</span>
          <span>了解更多</span>
        </div>
        <div class="card">
          <img src="#">
          <span>Make better descisions</span>
          <span>在线模型计算，结果预览，方便决策。</span>
          <span>了解更多</span>
        </div>
        <div class="card">
          <img src="#">
          <span>Edit</span>
          <span>Blablalba,blalablabalbabla,balbalbalbal.</span>
          <span>了解更多</span>
        </div>
        <div class="card">
          <img src="#">
          <span>TODO</span>
          <span>Blablalba,blalablabalbabla,balbalbalbal.</span>
          <span>了解更多</span>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
$firstHeight: 700px;

.home-screen {
  img {
    max-width: 100%;
    min-height: 100%;
  }
  .bg {
    background-image: url("../../assets/images/new/bg.jpg");
    background-size: cover;
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgb(139, 139, 139);
    left: 0;
    top: 0;
  }
  .mask {
    background-size: cover;
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgb(139, 139, 139);
    opacity: 0.5;
    left: 0;
    top: 0;
  }
}

.content,
.flow,
.cases,
.tools {
  position: relative;
  text-align: center;
  margin-bottom: 3rem;
}

.content {
  min-height: $firstHeight;
  display: flex;
  justify-content: center;
  align-items: center;
  align-content: center;
  color: white;
  .title {
    font-size: 4rem;
  }

  .desc {
    display: block;
  }
}

.main-container {
  .flow,
  .cases,
  .tools {
    // border: solid 1px red;
    min-height: 400px;
  }
  .tools {
  }
  .cases {
    .case-content {
      min-height: inherit;
      .case-item {
        min-height: inherit;
        position: relative;
        .case-img {
          position: absolute;
          width: 100%;
          height: 100%;
          background-size: cover;
        }
        .img1 {
          background-image: url("../../assets/images/new/planet.jpg");
        }

        .img2 {
          background-image: url("../../assets/images/new/fire.jpg");
        }

        .img3 {
          background-image: url("../../assets/images/new/leaft.jpg");
        }
        .mask {
          .bg {
            position: absolute;
            width: 100%;
            height: 100%;
            background-color: #161a1d;
            opacity: 0.5;
            color: white;
          }
          .case-text {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate3d(-50%, -50%, 0);
            color: white;
            i {
              font-size: 6rem;
            }
            div {
              font-size: 1.2rem;
            }
          }
        }
      }
    }
  }
  .flow {
    border: solid 1px red;
    margin: 5rem auto;
    width: 80%;
    .color-cover {
      position: absolute;
      width: 50%;
      height: 100%;
      background-image: url("../../assets/images/new/guoqu.svg");
      background-position: -6rem 0;
      background-size: cover;
      overflow: hidden;
      .flow-text {
        position: relative;
        width: 1264px;
        height: 100%;
        color: red;
      }
    }
    .gray-cover {
      position: absolute;
      background-image: url("../../assets/images/new/meiguoqu.svg");
      background-size: cover;
      background-position: -6rem 0;
      width: 100%;
      height: 100%;
      .flow-text {
        color: black;
      }
    }
  }
}
</style>
