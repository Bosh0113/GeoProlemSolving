<template>
  <div id="app">
    <router-view/>
    <BackTop></BackTop>
  </div>
</template>
<style>
body{
  overflow-x: hidden;
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
