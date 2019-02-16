<template>
  <div id="app">
    <router-view/>
    <BackTop></BackTop>
  </div>
</template>
<style>
body{
  overflow: hidden;
  overflow-y: scroll;
}
</style>
<script>
export default {
  name: "App",
  created() {
    this.$store.commit("getStorage");
  }
};
</script>

<style>
</style>
