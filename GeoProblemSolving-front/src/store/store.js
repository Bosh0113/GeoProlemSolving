import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
    // strict:true,
    state: {
        //data
        userState: false,
        userName: "visitor",
        userId:"",
        avatar: '',
        projectImg:''
    },
    getters: {
        userState: state => {
            return state.userState;
        },
        userName: state => {
            return state.userName;
        },
        userId: state =>{
            return state.userId;
        }
    },
    mutations: {
        getStorage: state => {
            if (sessionStorage.getItem('userState')) {
                state.userState = true;
                state.userName = sessionStorage.getItem('userName');
                state.avatar = sessionStorage.getItem('avatar');
                state.userId = sessionStorage.getItem('userId');
            }
        },
        userLogin: (state, data) => {
            state.userState = true;
            state.userName = data.userName;
            state.avatar = data.avatar;
            state.userId = data.userId;
            sessionStorage.setItem('userState', 'true');
            sessionStorage.setItem('userName', data.userName);
            sessionStorage.setItem('avatar', data.avatar);
            sessionStorage.setItem('userId', data.userId);

        },
        userLogout: (state) => {
            state.userState = false;
            state.userName = 'visitor';
            sessionStorage.removeItem('userState');
            sessionStorage.removeItem('userName');
            sessionStorage.removeItem('avatar');
            sessionStorage.removeItem('userId');
        },
        uploadAvatar: (state, avatar) => {
            state.avatar = avatar;
        }
    }
})
