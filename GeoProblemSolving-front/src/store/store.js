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
        getStorage: state => {
            if (localStorage.getItem('userState')) {
                state.userState = true;
                state.userName = localStorage.getItem('userName');
                state.avatar = localStorage.getItem('avatar');
                state.userId = localStorage.getItem('userId');
            }
        },
        userLogin: (state, data) => {
            state.userState = true;
            state.userName = data.userName;
            state.avatar = data.avatar;
            state.userId = data.userId;
            localStorage.setItem('userState', 'true');
            localStorage.setItem('userName', data.userName);
            localStorage.setItem('avatar', data.avatar);
            localStorage.setItem('userId', data.userId);
        },
        userLogout: (state) => {
            state.userState = false;
            state.userName = 'visitor';
            localStorage.removeItem('userState');
            localStorage.removeItem('userName');
            localStorage.removeItem('avatar');
            localStorage.removeItem('userId');
        },
        uploadAvatar: (state, avatar) => {
            state.avatar = avatar;
        }
    }
})
