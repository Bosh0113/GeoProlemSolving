<template>
<div>
    <div style="padding:20px">
        <h3>输入接收者Id</h3>
        <Input v-model="recipientId" placeholder="Enter recipientId" style="width:200px"/>
        <Button type="info" @click="sendMessage">Send Notice</Button>
        <h1>未读消息数为：{{noticeCount}}</h1>
    </div>
</div>
</template>
<script>
export default {
  created() {
    this.initWebSocket();
  },
  data() {
    return {
      noticeSocket: null,
      noticeCount: 0,
      recipientId: ""
    };
  },
  methods: {
    initWebSocket() {
      var noticeSocketURL = "ws://localhost:8081/NoticeSocket";
      this.noticeSocket = new WebSocket(noticeSocketURL);
      this.noticeSocket.onopen = this.onOpen;
      this.noticeSocket.onmessage = this.onMessage;
      this.noticeSocket.onclose = this.onClose;
      this.noticeSocket.onerror = this.onError;
    },
    onOpen() {
      var connected = document.createElement("p");
      connected.innerHTML = "WebSocket连接成功！";
      $("body").append(connected);
    },
    onMessage(e) {
      if (e.data == "Notice") {
        let newCount = this.noticeCount + 1;
        this.$set(this, "noticeCount", newCount);
      } else {
        console.log(e.data);
      }
    },
    onClose(e) {
      var unConnect = document.createElement("p");
      unConnect.innerHTML = "WebSocket连接断开！";
      $("body").append(unConnect);
    },
    onError(e) {
      var connectError = document.createElement("p");
      connectError.innerHTML = "WebSocket连接错误！";
      $("body").append(connectError);
    },
    sendMessage() {
      this.noticeSocket.send(this.recipientId);
    }
  }
};
</script>

<style>
</style>
