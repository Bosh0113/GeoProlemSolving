<style scoped>
.detailContent {
  padding: 15px 10px;
  height: 500px;
  overflow-y: auto;
  background-color: #f7f7f7;
}
.noDetail h1 {
  color: darkgray;
  text-align: center;
}
.noticeDetail {
  min-width: 310px;
  width: 33%;
  float: left;
  height: 180px;
  padding: 5px;
}
.noticeDeleteBtn {
  float: right;
  color: #ff7800;
  cursor: pointer;
}
.noticeDescription {
  display: block;
  text-indent: 2em;
  height: 80px;
  margin: 5px 0;
  background-color: #f7f7f7;
}
.noticeReadBtn {
  float: right;
  color: cornflowerblue;
}
.approveApplyBtn {
  float: right;
  color: #19be6b;
  margin: 0px 2px;
}
.refuseApplyBtn {
  float: right;
  color: #ed4014;
  margin: 0px 2px;
}
</style>
<template>
<div>
  <Row>
    <Col span="22" offset="1">
      <h1>Notifications</h1>
  <Tabs type="card" value="notice" style="margin:20px 0">
    <TabPane :label="noticeTab" name="notice">
      <Card>
        <h3 slot="title">Notice Detail</h3>
        <div class="detailContent">
          <Card class="noDetail" v-if="this.noticeList.length<1">
            <h1>No Notice Notifications</h1>
          </Card>
          <template v-else-if="this.noticeList.length>0">
            <div class="noticeDetail" v-for="notice in this.noticeList" :key="notice.index">
              <template v-if="notice.type =='Work'">
                <Card style="height:100%">
                  <template v-if="notice.state=='unread'">
                    <Badge dot>
                      <h4>{{notice.content.title}}</h4>
                    </Badge>
                    <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                  </template>
                  <h4 v-else>{{notice.content.title}}
                    <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                  </h4>
                  <small class="noticeDescription">{{notice.content.description}}</small>
                  <small>{{notice.createTime}}</small>
                  <Button class="noticeReadBtn" v-if="notice.state=='unread'" @click="gotoWork(notice.noticeId, notice.content.subProjectId)">Go</Button>
                </Card>
              </template>
              <template v-else>
                <Card style="height:100%">
                  <template v-if="notice.state=='unread'">
                    <Badge dot>
                      <h4>{{notice.content.title}}</h4>
                    </Badge>
                    <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                  </template>
                  <h4 v-else>{{notice.content.title}}
                    <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                  </h4>
                  <small class="noticeDescription">{{notice.content.description}}</small>
                  <small>{{notice.createTime}}</small>
                  <Button class="noticeReadBtn" v-if="notice.state=='unread'" @click="readNotice(notice.noticeId)">Got it</Button>
                </Card>
              </template>
            </div>
          </template>
        </div>
      </Card>
    </TabPane>
    <TabPane :label="replyTab" name="reply">
      <Card>
        <h3 slot="title">Reply Detail</h3>
        <div class="detailContent">
          <Card class="noDetail" v-if="this.replyList.length<1">
            <h1>No Reply Notifications</h1>
          </Card>
          <template v-else-if="this.replyList.length>0">
            <div class="noticeDetail" v-for="reply in this.replyList" :key="reply.index">
              <Card style="height:100%">
              Reply Datail
              <!-- 此处添加回复类型通知的Card显示 -->
            </Card>
            </div>
          </template>
        </div>
      </Card>
    </TabPane>
    <TabPane :label="applyTab" name="apply">
      <Card>
        <h3 slot="title">Apply Detail</h3>
        <div class="detailContent">
          <Card class="noDetail" v-if="this.applyList.length<1">
            <h1>No Apply Notifications</h1>
          </Card>
          <template v-else-if="this.applyList.length>0">
            <div class="noticeDetail" v-for="apply in this.applyList" :key="apply.index">
              <Card style="height:100%">
                <template v-if="apply.state=='unread'">
                  <Badge dot>
                    <h4>{{apply.content.title}}</h4>
                  </Badge>
                  <span class="noticeDeleteBtn" @click="deleteNotice(apply)">×</span>
                </template>
                <h4 v-else>{{apply.content.title}}
                  <span class="noticeDeleteBtn" @click="deleteNotice(apply)">×</span>
                </h4>
                <small class="noticeDescription">{{apply.content.description}}</small>
                <small>{{apply.createTime}}</small>
                <template v-if="apply.content.approve=='unknow'">
                  <Button class="approveApplyBtn" v-if="apply.state=='unread'" @click="refuseApply(apply)">×</Button>
                  <Button class="refuseApplyBtn" v-if="apply.state=='unread'" @click="approveApply(apply)">√</Button>
                </template>
                <template v-else-if="apply.content.approve=='true'">
                  <Button disabled style="float:right;">√</Button>
                </template>
                <template v-else-if="apply.content.approve=='false'">
                  <Button disabled style="float:right;">×</Button>
                </template>
              </Card>
            </div>
          </template>
        </div>
      </Card>
    </TabPane>
  </Tabs>
    </Col>
  </Row>

</div>
</template>
<script>
export default {
  mounted() {
    this.loadNotifications();
  },
  data() {
    return {
      noticeUnreadCount: 0,
      replyUnreadCount: 0,
      applyUnreadCount: 0,
      noticeList: [],
      replyList: [],
      applyList: [],
      noticeTab: h => {
        return h("div", [
          h("span", "Notice"),
          h("Badge", {
            props: {
              count: this.noticeUnreadCount
            }
          })
        ]);
      },
      replyTab: h => {
        return h("div", [
          h("span", "Reply"),
          h("Badge", {
            props: {
              count: this.replyUnreadCount
            }
          })
        ]);
      },
      applyTab: h => {
        return h("div", [
          h("span", "Apply"),
          h("Badge", {
            props: {
              count: this.applyUnreadCount
            }
          })
        ]);
      }
    };
  },
  methods: {
    loadNotifications() {
      this.axios
        .get(
          "/GeoProblemSolving/notice/inquiry?" +
            "key=recipientId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data !== "Fail") {
            let noticeListTest = [];
            let noticeUnreadCount = 0;
            let replyListTest = [];
            let replyUnreadCount = 0;
            let applyListTest = [];
            let applyUnreadCount = 0;
            let notifications = res.data;
            for (let i = 0; i < notifications.length; i++) {
              if (notifications[i].type === "notice") {
                noticeListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  noticeUnreadCount++;
                }
              } else if (notifications[i].type === "reply") {
                replyListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  replyUnreadCount++;
                }
              } else if (notifications[i].type === "apply") {
                applyListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  applyUnreadCount++;
                }
              }
            }
            noticeListTest.reverse();
            replyListTest.reverse();
            applyListTest.reverse();
            this.$set(this, "noticeUnreadCount", noticeUnreadCount);
            this.$set(this, "noticeList", noticeListTest);
            this.$set(this, "replyUnreadCount", replyUnreadCount);
            this.$set(this, "replyList", replyListTest);
            this.$set(this, "applyUnreadCount", applyUnreadCount);
            this.$set(this, "applyList", applyListTest);
          }
          else{
            this.$Message.danger("load notifications fail");
          }
        })
        .catch(err => {
          this.$Message.danger("load notifications fail");
        });
    },
    deleteNotice(notice) {
      console.log("notice :" + notice.noticeId + " has been deleted.");
      this.axios
        .get("/GeoProblemSolving/notice/delete" + "?noticeId=" + notice.noticeId)
        .then(res => {
          if (res.data == "Success") {
            this.$Message.success("delete notification success.");
            if(notice.state=="unread"){
              this.$emit("readNotification");
            }
            this.loadNotifications();
          } else {
            this.$Message.danger("delete notification fail.");
          }
        })
        .catch(err => {
          this.$Message.danger("delete notification fail.");
        });
    },
    readNotice(noticeId) {
      this.axios
        .get("/GeoProblemSolving/notice/read" + "?noticeId=" + noticeId)
        .then(res => {
          if (res.data == "Success") {
            this.$emit("readNotification");
            this.$store.commit("getUserInfo");
            this.loadNotifications();
          } else {
            this.$Message.danger("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.danger("update notification fail.");
        });
    },
    gotoWork(noticeId,subProjectId){      
      this.axios
        .get("/GeoProblemSolving/notice/read" + "?noticeId=" + noticeId)
        .then(res => {
          if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
          } else {
            this.$Message.danger("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.danger("update notification fail.");
        });
      
      this.$router.push( `./project/${id}/workspace`);
    },
    refuseApply(apply) {
      console.log(apply.content.title + " has been refused.");
      let updateApply = new URLSearchParams();
      updateApply.append("noticeId", apply.noticeId);
      updateApply.append("content.approve", "false");
      updateApply.append("state", "read");
      this.axios
        .post("/GeoProblemSolving/notice/update", updateApply)
        .then(res => {
          if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
            let replyNotice = {};
            replyNotice["recipientId"] = apply.content.userId;
            replyNotice["type"] = "notice";
            replyNotice["content"] = {
              title: "Result for application",
              description:
                "Sorry, you were refused to join the project: " +
                apply.content.projectTitle +
                " ."
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", apply.content.userId);
                } else {
                  this.$Message.danger("reply fail.");
                }
              })
              .catch(err => {
                this.$Message.danger("reply fail.");
              });
          } else {
            this.$Message.danger("update notification fail.");
          };
        })
        .catch(err => {
          this.$Message.danger("update notification fail.");
        });
    },
    approveApply(apply) {
      console.log(apply.content.title + " has been passed.");
      let updateApply = new URLSearchParams();
      updateApply.append("noticeId", apply.noticeId);
      updateApply.append("content.approve", "true");
      updateApply.append("state", "read");
      this.axios
        .post("/GeoProblemSolving/notice/update", updateApply)
        .then(res => {
          if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
            //update project members
            this.axios
              .get(
                "/GeoProblemSolving/"+apply.content.scope+"/join?" +
                  apply.content.scope+"Id=" +
                  apply.content.projectId +
                  "&userId=" +
                  apply.content.userId
              )
              .then(res => {
                if (res.data === "Fail") {
                  this.$Message.danger("Join fail.");
                } else if (res.data === "Exist") {
                  this.$Message.info(
                    "he/her have already be a member in project."
                  );
                }
              })
              .catch(err => {
                this.$Message.danger("Join fail");
              });
            //reply to applicant
            let replyNotice = {};
            replyNotice["recipientId"] = apply.content.userId;
            replyNotice["type"] = "notice";
            replyNotice["content"] = {
              title: "Result for application",
              description:
                "Congratulations on joining the project: " +
                apply.content.projectTitle +
                " ."
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", apply.content.userId);
                } else {
                  this.$Message.danger("reply fail.");
                }
              })
              .catch(err => {
                this.$Message.danger("reply fail.");
              });
          } else {
            this.$Message.danger("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.danger("update notification fail.");
        });
    }
  }
};
</script>
