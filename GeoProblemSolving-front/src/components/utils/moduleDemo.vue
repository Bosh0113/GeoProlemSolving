<style>
</style>
<template>
    <Button type="info" @click="sendMessage('this is a message')">Send Message</Button>
</template>
<script>
export default {
  created() {
    this.initSocket();
  },
  data() {
    return {
      moduleSocket: null,
      timer: null
    };
  },
  methods: {
    initSocket() {
      var moduleSocketURL = "ws://localhost:8081/Module/" + "moduleId";
      this.moduleSocket = new WebSocket(moduleSocketURL);
      this.moduleSocket.onopen = this.onOpen;
      this.moduleSocket.onmessage = this.onMessage;
      this.moduleSocket.onclose = this.onClose;
      this.moduleSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("ModuleSocket连接成功！");
    },
    onMessage(e) {
      var messageJson = JSON.parse(e.data);
      if (messageJson.type == "message") {
        console.log(messageJson.message);
      } else if (messageJson.type == "members") {
        var members = messageJson.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");
        console.log("online members: ");
        for (let i = 0; i < members.length; i++) {
          console.log(members[i]);
        }
      } else if(messageJson.type == "online"){
        console.log(messageJson.userId+messageJson.createTime);
      } else if(messageJson.type == "offline"){
        console.log(messageJson.userId+messageJson.createTime);
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("ModuleSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("ModuleSocket连接错误！");
    },
    sendMessage(message) {
      var messageJson = {};
      messageJson["type"] = "message";
      messageJson["message"] = message;
      this.moduleSocket.send(JSON.stringify(messageJson));
    },
    setTimer() {
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        this.moduleSocket.send(JSON.stringify(messageJson));
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    }
  }
};
</script>
