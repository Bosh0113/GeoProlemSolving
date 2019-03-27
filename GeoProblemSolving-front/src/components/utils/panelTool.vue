<template>
  <div style="display:flex;align-items:center;justify-content:center">
    <Button type="default" @click="init()">create</Button>
  </div>
</template>
<style>
</style>
<script>
export default {
  methods: {
    init() {
      jsPanel.create({
        theme: "primary",
        headerTitle: "Tools",
        contentSize: "450 250",
        animateIn: "animated bounceInLeft", // animation from animate.css
        content:'<div><Button type="warning">choose</Button></div>',
        callback: function() {
          this.content.style.padding = "20px";
        },
        onbeforeclose: function() {
          return confirm("Do you really want to close the panel?");
        }
      });
    }
  }
};
</script>
