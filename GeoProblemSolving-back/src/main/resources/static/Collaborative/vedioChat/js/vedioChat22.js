/**
 * Webrtc相关
 */
//选择的人的Id
var userID;
//在线的成员
var olParticipants=[];

$(document).ready(function () {

    var localView = document.getElementById('vedio-box1');
    var remoteView = document.getElementById('vedio-box2');
    var requestBtn = $("#send-request");
    var cancelBtn = $("#send-cancel");
    var responseBtn = $("#send-response");
    var rejectBtn = $("#send-reject");
    var hangupBtn = $("#send-hangup");
    var membersList = $("#memberList");
    var localStream;
    var remoteStream;
    var userID;

    //A端：是否收到了B端接受视频的应答；B端：是否应达了A端的视频请求
    var isResponsed = false;
    var isAvailable = true;

    const configuration = {"iceServers": [
            {"url": "stun:stun.l.google.com:19302"},
            // TURN 一般需要自己去定义
            {
                'url': 'turn:223.2.40.150:3478?transport=udp',
                'credential': 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
                'username': '28224511:1379330808'
            }
        ]};

    var peerConnection;
    var constraints = { audio: false, video: true };
    var usrId = newGuid();
    var ws;
    if(WebSocket)
    {

        // let  roomID = sessionStorage.getItem("moduleId");
        let roomID = "123";
        //vedioSocket/{usrId}
        ws = new WebSocket("wss://223.2.44.124:8083/GeoProblemSolving/VideoChatServer/" + roomID);
        // ws = new WebSocket("wss://223.2.44.124:8083/VideoChatServer/" + roomID);
        // ws = new WebSocket("wss://172.21.212.72:8082/GeoProblemSolving/VideoChatServer/"+roomID);
        // ws = new WebSocket("wss://172.21.213.185:8080/GeoProblemSolving/VideoChatServer/"+roomID);

    }


    $("#send-request").on("click",function () {
        displayBtn(cancelBtn);
        isAvailable = false;
        var request = JSON.stringify({"type": "request","to":userID});
        ws.send(request);
        //超过20秒无应答，用户可重新发起请求
        setTimeout(function reset() {
            if (!isResponsed)
            {
                isAvailable = true;
                consoleLog("Request no answer, please send again!");
            }
        },8000);

    });

    $("#send-response").on("click",function () {
        var response = JSON.stringify({"type": "response","to":userID});
        //将B端应答状态设置为true，并返回应答
        isResponsed = true;
        hideBtn(responseBtn);
        hideBtn(rejectBtn);
        ws.send(response);
    });

    $("#send-cancel").on("click",function () {
        var cancel = JSON.stringify({"type": "cancel","to":userID});
        //将A端可用状态设置为true
        isAvailable = true;
        hideBtn(cancelBtn);
        ws.send(cancel);
    });

    $("#send-reject").on("click",function () {
        var reject = JSON.stringify({"type": "reject","to":userID});
        isAvailable = true;
        hideBtn(responseBtn);
        hideBtn(rejectBtn);
        ws.send(reject);
    });

    $("#send-hangup").on("click",function () {
        closeConnection();
        resetAll();
        var hangup = JSON.stringify({"type": "hangup","to":userID});
        isAvailable = true;
        ws.send(hangup);
    });

    function closeConnection() {
        const tracks = localStream.getTracks().concat(remoteStream.getTracks());
        tracks.forEach((track) => {
            track.stop();
        });
        peerConnection.close();
        clearVedio();
    }

    async function start() {
        try {
            // get local stream, show it in self-view and add it to be sent
            localStream = await navigator.mediaDevices.getUserMedia(constraints);
            localStream.getTracks().forEach((track) =>
                peerConnection.addTrack(track, localStream));
            if ('srcObject' in localView) { // 判断是否支持 srcObject 属性
                localView.srcObject = localStream;
            } else {
                localView.src = window.URL.createObjectURL(localStream);
            }
        } catch (err) {
            alert("系统未检测到摄像头!");
            console.error(err);
        }
        await peerConnection.createOffer().then( offer => {
            return peerConnection.setLocalDescription(new RTCSessionDescription(offer));
        })
        ws.send(JSON.stringify({
            "type": "offer",
            "data": {
                "desc": peerConnection.localDescription
            },
            "to":userID
        }));
    }

    function initiate() {
        // 浏览器适配
        let audioContext;
        if (typeof AudioContext === 'function') {
            audioContext = new AudioContext();
        } else if (typeof webkitAudioContext === 'function') {
            audioContext = new webkitAudioContext(); // eslint-disable-line new-cap
        } else {
            console.log('Sorry! Web Audio not supported.');
        }

        // 视频过滤节点
        var filterNode = audioContext.createBiquadFilter();
        // see https://dvcs.w3.org/hg/audio/raw-file/tip/webaudio/specification.html#BiquadFilterNode-section
        filterNode.type = 'highpass';  //高通
        // cutoff frequency: for highpass, audio is attenuated below this frequency 截止频率
        filterNode.frequency.value = 10000;

        // create a gain node (to change audio volume) 控制音量 小于1表示音量衰减，反之亦然
        var gainNode = audioContext.createGain();
        // default is 1 (no change); less than 1 means audio is attenuated
        // and vice versa
        gainNode.gain.value = 0.5;


        // 判断是否有 navigator.mediaDevices，没有赋成空对象
        if (navigator.mediaDevices === undefined) {
            navigator.mediaDevices = {};
        }

        navigator.getUserMedia  = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
        peerConnection = new RTCPeerConnection(configuration);
        peerConnection.ontrack = (event) => {
            // don't set srcObject again if it is already set.
            remoteStream = event.streams[0];
            if ('srcObject' in remoteView)
            {
                remoteView.srcObject = remoteStream;
            }
            else {
                remoteView.src = window.URL.createObjectURL(remoteStream);
            }
            remoteView.play();
        };

        peerConnection.onicecandidate = ({candidate}) =>
        {
            console.log("[发送的candidate] " + JSON.stringify({"type":"candidate","value":candidate}))
            ws.send(JSON.stringify({"type":"candidate","value":candidate,"to":userID}));
        };

        peerConnection.oniceconnectionstatechange = function (event) {
            if (peerConnection.iceConnectionState === "failed" ||
                peerConnection.iceConnectionState === "disconnected" ||
                peerConnection.iceConnectionState === "closed") {
                console.error(event);
                console.error("连接中断请刷新");
            }
        };
    }


    ws.onopen = function(){
      var userInfo = sessionStorage.getItem("userInfo");
      var userInfoObject = JSON.parse(userInfo);
      var userId = userInfoObject.userId;
      var message = {
        "type":"connect",
        "userId": userId
      };
        ws.send(JSON.stringify(message));
        initiate();
    }

    ws.onmessage = async function(evt){
        var message = JSON.parse(evt.data) ;
        if (message!=null)
        {
            switch (message.type) {
                case "request":
                {
                    //请求到来时，如果B端已处于忙碌状态，返回忙碌
                    if (isAvailable === false)
                    {
                        ws.send(JSON.stringify({"type": "busy","to":userID}));
                    }
                    //如果B是空闲状态
                    else
                    {
                        //否则将B设置为忙碌状态
                        isAvailable = false;
                        consoleLog("有视频聊天请求...");
                        displayBtn(responseBtn);
                        displayBtn(rejectBtn);
                        hideBtn(requestBtn);
                        //超过20秒，如果B不应答，默认取消，将B重设为空闲状态
                        await setTimeout(function resetRespBtn() {
                            if (!isResponsed)
                            {
                                $("#send-response").css("display","none");
                                isAvailable = true;
                            }
                             },8000);
                    }
                    break;
                }
                case "response":
                {
                    //如果A端接收到B端的response,则A端开启视频聊天
                    start();
                    //将A的被应答状态设置为true
                    isResponsed = true;
                    consoleLog("正在连接请等待...");
                    displayBtn(hangupBtn);
                    hideBtn(cancelBtn);
                    hideBtn(requestBtn);
                    break;
                }
                case "busy":
                {
                    consoleLog("对方忙线...");break;
                }
                case "cancel":
                {
                    consoleLog("对方已取消...");
                    hideBtn(responseBtn);
                    hideBtn(rejectBtn);
                    isResponsed = true;
                    isAvailable = true;
                    break;
                }
                case "reject":
                {
                    consoleLog("对方已拒绝...");
                    isResponsed = true;
                    isAvailable = true;
                    break;
                }
                case "hangup":
                {
                    consoleLog("对方已挂断...");
                    resetAll();
                    break;
                }
                case "offer":
                {
                    if (message.data.desc != null )
                    {
                        await peerConnection.setRemoteDescription(message.data.desc);
                    }
                    try {
                        localStream = await navigator.mediaDevices.getUserMedia(constraints);
                        if ('srcObject' in localView) { // 判断是否支持 srcObject 属性
                            localView.srcObject = localStream;
                        } else {
                            localView.src = window.URL.createObjectURL(localStream);
                        }
                        //将本地视频流加入remoteConnection
                        await localStream.getTracks().forEach(track => peerConnection.addTrack(track, localStream));
                    }
                    catch (e) {
                        alert("系统未检测到摄像头！");
                        console.error(e);
                    }

                    await peerConnection.createAnswer().then(function (answer) {
                        peerConnection.setLocalDescription(new RTCSessionDescription(answer))
                    });
                    ws.send(JSON.stringify({
                        "type": "answer",
                        "data": {
                            "desc": peerConnection.localDescription
                        },
                        "to":userID
                    }));

                    remoteView.onloadedmetadata = function(){
                        remoteView.play();
                    }
                    displayBtn(hangupBtn);
                    break;
                }
                case "answer":
                {
                    if (message.data.desc != null) {
                        await peerConnection.setRemoteDescription(message.data.desc);
                    }
                    break;
                }
                case "candidate":
                {
                    console.log("[接收candidate]: " + message.value);
                    if (message.value != null) {
                        await peerConnection.addIceCandidate(await new RTCIceCandidate(message.value));
                    }
                    break;
                }
                case "members":
                {
                    var members = message.message
                        .replace("[", "")
                        .replace("]", "")
                        .replace(/\s/g, "")
                        .split(",");

                    olParticipantChange(members);
                }
                default:
                    break;
            }

        }
    }


    ws.onclose = function () {
        closeConnection();
        resetAll();
    }
    
    ws.onerror = function (event) {
        closeConnection();
        resetAll();
        console.error("WebSocket error observed:", event);
    }

    function consoleLog(log) {
        // $("#ipt-console").val( $("#ipt-console").val()  + log);
        $("#ipt-console").val(log);
    }

    function displayBtn(e) {
        e.css("display","");
    }
    function hideBtn(e) {
        e.css("display","none")
    }
    function clearVedio() {
        if (localView.srcObject)
        {
            localView.srcObject = null;
        }
        else if (localView.src)
        {
            localView.src = null;
        }
        localView.pause();
        
        if (remoteView.srcObject)
        {
            remoteView.srcObject = null;
        }
        else if (remoteView.src)
        {
            remoteView.src = null;
        }
        remoteView.pause();
    }
    function resetAll() {
        clearVedio();
        initiate();
        displayBtn(requestBtn);
        hideBtn(cancelBtn);
        hideBtn(responseBtn);
        hideBtn(rejectBtn);
        hideBtn(hangupBtn);
    }
    
    function newGuid()
    {
        var guid = "";
        for (var i = 1; i <= 32; i++){
            var n = Math.floor(Math.random()*16.0).toString(16);
            guid +=   n;
            if((i==8)||(i==12)||(i==16)||(i==20))
                guid += "-";
        }
        return guid;
    }

});

function olParticipantChange(members){
    let userIndex = -1;

    // 自己刚上线，olParticipants空
    if (olParticipants.length == 0) {
        for (var i = 0; i < members.length; i++) {
                $.ajax({
                    url:"/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    members[i],
                    type:"GET",
                    success: data => {
                        if (data != "None" && data != "Fail") {
                            olParticipants.push(data);
                            createMmeberList(olParticipants);

                        } else if (data == "None") {
                        }
                    },
                    error: err=>{
                        console.log(err);
                    }
                });
        }
    } else {
        // members大于olParticipants，有人上线；小于olParticipants，离线
        if (members.length > olParticipants.length) {
            for (var i = 0; i < members.length; i++) {
                for (var j = 0; j < olParticipants.length; j++) {
                    if (members[i] === olParticipants[j].userId) {
                        break;
                    }
                }
                if (j === olParticipants.length) {
                    userIndex = i;
                    break;
                }
            }

            // 人员渲染
            $.ajax({
                url:"/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                members[userIndex],
                type:"GET",
                success: data => {
                    if (data !== "None" && data !== "Fail") {
                        olParticipants.push(data);
                    } else if (data === "None") {
                    }
                }
            });
        } else if (members.length < olParticipants.length) {
            for (var i = 0; i < olParticipants.length; i++) {
                for (var j = 0; j < members.length; j++) {
                    if (olParticipants[i].userId === members[j]) {
                        break;
                    }
                }
                if (j === members.length) {
                    userIndex = i;
                    break;
                }
            }
            olParticipants.splice(userIndex, 1);
        }
    }
};
function createMmeberList(list){
    console.table(list);
}