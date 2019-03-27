import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  // strict:true,
  state: {
    //data
    userState: false,
    userName: "visitor",
    userId: "",
    avatar: '',
    projectImg: ''
  },
  getters: {
    userState: state => {
      return state.userState;
    },
    userName: state => {
      return state.userName;
    },
    userId: state => {
      return state.userId;
    }
  },
  mutations: {
    getUserInfo: state => {
      // this.axios.get("/GeoProblemSolving/user/state")
      //   .then(res=> {
      //     if (res.data) {
      //       var userInfo = data;
      //       state.userName = userInfo.userName;
      //       state.avatar = userInfo.avatar;
      //       state.userId = userInfo.userId;
      //       state.userState = true;
      //     }
      //   })
      //   .catch(err=> {
      //     if (err.data) {
      //       console.log("Get user info fail.");
      //     }
      //     console.log(err.data);
      //   })
      $.ajax({
          url: "/GeoProblemSolving/user/state",
          type: "GET",
          async: false,
          success: function (data) {
              if (data) {
                  var userInfo = data;
                  state.userName = userInfo.userName;
                  state.avatar = userInfo.avatar;
                  state.userId = userInfo.userId;
                  state.userState = true;
              }
          },
          error: function (err) {
              console.log("Get user info fail.");
          }
      });
    },
    userLogin: (state, data) => {
      state.userState = true;
      state.userName = data.userName;
      state.avatar = data.avatar;
      state.userId = data.userId;
    },
    userLogout: (state) => {
      state.userState = false;
      state.userName = 'visitor';
      state.userId = '';
    },
    uploadAvatar: (state, avatar) => {
      state.avatar = avatar;
    }
  }
})
