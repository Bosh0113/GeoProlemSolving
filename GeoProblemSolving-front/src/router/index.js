import Vue from 'vue'
import Router from 'vue-router'
import Navigation from '@/components/Navigation.vue'
import Home from '@/components/NavigationContent/Home.vue'
// import Project from '@/components/NavigationContent/Project.vue'
import ProjectDetail from '@/components/NavigationContent/ProjectDetail.vue'
import NewProject from '@/components/createNew/NewProject.vue'
import Participants from '@/components/NavigationContent/Participants.vue'
import Community from '@/components/Community/Community.vue'
import Communityreply from '@/components/Community/CommunityReply'
import Help from '@/components/NavigationContent/Help.vue'
import Login from '@/components/UsersState/Login.vue'
import Register from '@/components/UsersState/Register.vue'
import PersonalPage from '@/components/UsersState/PersonalPage.vue'
import Notifications from '@/components/UsersState/Notifications.vue'
import chatUtil from '@/components/utils/chatroom.vue'
import drawUtil from '@/components/utils/drawBoard.vue'
import mapTool from '@/components/utils/mapTool.vue'
import projectworkspace from '@/components/NavigationContent/ProjectWorkspace.vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import projectList from '@/components/Projects/ProjectList.vue'
import MemberDetailPage from '@/components/UserPage/memberDetailPage.vue'
import ResourceList from '@/components/Resource/ResourceList.vue'
Vue.use(Router)
Vue.use(mavonEditor)


const routes = [{
  path: '/', name: 'Navigation', component: Navigation, children: [
    { path: '', redirect: 'home', component: Home },
    { path: 'home', name: 'Home', component: Home },
    { path: 'project', name: 'Project', component: projectList },
    { path: 'project/:id',name:'ProjectDetail',component:ProjectDetail},
    { path: 'project/:id/workspace', name:'projectworkspace', component:projectworkspace},
    { path: 'newproject',name:'NewProject',component:NewProject},
    { path: 'participants', name: 'Participants', component: Participants },
    { path: 'community', name: 'Community', component: Community },
    { path: 'community/:id', name: 'Communityreply', component:Communityreply},
    { path: 'help', name: 'Help', component: Help },
    { path: 'personalPage', name: 'PersonalPage', component: PersonalPage },
    { path: 'notifications', name: 'Notifications', component:Notifications},
    { path: 'memberPage/:id', name: 'MemberDetailPage', component:MemberDetailPage},
    { path: 'resourceList', name:'resourceList', component:ResourceList},
  ]
},
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/chat' ,name :'chatUtil',component: chatUtil},
    { path: '/draw', name: 'drawUtil', component:drawUtil},
    { path: '/map', name: 'mapTool', component:mapTool},
    { path: '/moduleDemo', name: 'ModuleDemo', component:ModuleDemo},
]
export default new Router({
  routes,
  mode: 'history',
})
