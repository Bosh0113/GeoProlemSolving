import Vue from 'vue'
import Router from 'vue-router'
import 'mavon-editor/dist/css/index.css'
import mavonEditor from 'mavon-editor'
Vue.use(Router)
Vue.use(mavonEditor)


const routes = [{
  path: '/', name: 'Navigation', component: resolve=>(require(["@/components/Navigation"],resolve)), children: [
    { path: '', redirect: 'home', component: resolve=>(require(["@/components/NavigationContent/Home"],resolve)) },
    { path: 'home', name: 'Home', component: resolve=>(require(["@/components/NavigationContent/Home"],resolve)) },
    { path: 'projectlist', name: 'Project', component: resolve=>(require(["@/components/Projects/ProjectList"],resolve)) },
    { path: 'project/:id', name: 'ProjectDetail', component: resolve=>(require(["@/components/NavigationContent/ProjectDetail"],resolve)) },
    { path: 'project/:id/workspace', name: 'workspace', component: resolve=>(require(["@/components/NavigationContent/moduleList"],resolve)) },
    { path: 'project/:id/subproject', name: 'subproject', component: resolve=>(require(["@/components/NavigationContent/subprojectDetail"],resolve)) },
    { path: 'newproject', name: 'NewProject', component: resolve=>(require(["@/components/createNew/NewProject"],resolve)) },
    { path: 'participants', name: 'Participants', component: resolve=>(require(["@/components/NavigationContent/Participants"],resolve)) },
    { path: 'community', name: 'Community', component: resolve=>(require(["@/components/Community/Community"],resolve)) },
    { path: 'community/:id', name: 'Communityreply', component: resolve=>(require(["@/components/Community/CommunityReply"],resolve)) },
    { path: 'help', name: 'Help', component: resolve=>(require(["@/components/NavigationContent/Help"],resolve)) },
    { path: 'personalPage', name: 'PersonalPage', component: resolve=>(require(["@/components/UsersState/PersonalPage"],resolve)) },
    { path: 'notifications', name: 'Notifications', component: resolve=>(require(["@/components/UsersState/Notifications"],resolve)) },
    { path: 'memberPage/:id', name: 'MemberDetailPage', component: resolve=>(require(["@/components/UserPage/memberDetailPage"],resolve)) },
    { path: 'resourceList', name: 'resourceList', component: resolve=>(require(["@/components/Resource/ResourceList"],resolve)) },
  ]
},
{ path: '/login', name: 'Login', component: resolve=>(require(["@/components/UsersState/Login"],resolve)) },
{ path: '/register', name: 'Register', component: resolve=>(require(["@/components/UsersState/Register"],resolve)) },
{ path: '/chat', name: 'chatUtil', component: resolve=>(require(["@/components/utils/chatroom"],resolve)) },
{ path: '/draw', name: 'drawUtil', component: resolve=>(require(["@/components/utils/drawBoard"],resolve)) },
{ path: '/map', name: 'mapTool', component: resolve=>(require(["@/components/utils/mapTool"],resolve)) },
{ path: '/charts', name: 'dataCharts', component: resolve=>(require(["@/components/utils/charts"],resolve)) },
{ path: '/nc/draw', name: 'drawUtilNC', component: resolve=>(require(["@/components/utils/singleUtils/drawBoard"],resolve)) },
{ path: '/nc/map', name: 'mapToolNC', component: resolve=>(require(["@/components/utils/singleUtils/mapTool"],resolve)) },
{ path: '/nc/charts', name: 'dataChartsNC', component: resolve=>(require(["@/components/utils/singleUtils/charts"],resolve)) },
{ path: '/join/:id/:email', name: 'joinProject', component: resolve=>(require(["@/components/createNew/joinNewProject"],resolve)) },
]
export default new Router({
  routes,
  mode: 'history',
})
