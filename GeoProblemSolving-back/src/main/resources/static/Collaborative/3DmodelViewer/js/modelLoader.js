var model = null, material = null, texture = [];
var modelType = "";
var modelURL = "", materialURL = "";

var wsTModel = null;
var socket_content = null;

var reader = new FileReader();

$("#modelPath").change(function () {

    model = document.getElementById("modelPath").files[0];

    modelType = model.name.split('.').pop().toLowerCase();
});

$("#materialPath").change(function () {


    material = document.getElementById("materialPath").files[0];
});

$("#texturePath").change(function () {

    texture = document.getElementById("texturePath").files;

});

function uploadFun() {

    var formData = new FormData();
    var file = texture;
    file.unshift(model);
    file.unshift(material);
    formData.append("file", file);
    formData.append("description", "3D viewer tool");
    formData.append("type", "data");
    formData.append("uploaderId", userInfo.userId);
    formData.append("belong", userInfo.userName);
    var scopeObject = {
        projectId: sessionStorage.getItem("projectId"),
        subprojectId: sessionStorage.getItem("subprojectId"),
        moduleId: sessionStorage.getItem("moduleId")
    };
    formData.append("scope", JSON.stringify(scopeObject));

    $.ajax({
        type: "POST",
        url: "/GeoProblemSolving/resource/upload",
        data: formData,
        // enctype: text / plain,
        cache: false,         //不设置缓存
        processData: false,  // 不处理数据
        contentType: false,  // 不设置内容类型
        success: function (data) {

            socket_content = {"from":[],"ModelType":[],"modelDataName":[],"MaterialUrl":[]};
            if (data != "Size over" && data.length > 0) {

                for(var i = 0;i <data.length;i++){

                }


                var dataName = data[0];
                socket_content["dataName"] = dataName;
                socket_content["operate"] = "dataupload";
                wsTModel.send(socket_content);



            }


            var urlJson = JSON.parse(path);
            modelURL = urlJson.model;
            materialURL = urlJson.material;


            //定义路径Json字符串
            var urlJson = {"from":[],"ModelType":[],"ModelUrl":[],"MaterialUrl":[]};
            // urlJson.ModelType.push(modelType.replace(/localhost/g,"223.2.46.117"));
            // urlJson.ModelUrl.push(modelURL.replace(/localhost/g,"223.2.46.117"));
            // urlJson.MaterialUrl.push(materialURL.replace(/localhost/g,"223.2.46.117"));

            //发送路径
            wsTModel.send(JSON.stringify(urlJson));
        },
        error: function () {
            alert("fail");
        }
    });

}

$(document).ready(function() {

    if (WebSocket) {
        // wsTModel = new WebSocket("ws://localhost:8081/GeoProblemSolving/3DviewerTool/");
        wsTModel = new WebSocket("ws://172.21.212.7:8082/GeoProblemSolving/3DviewerTool/");
        wsTModel.binaryType = "arraybuffer";
    }
    else {
        alert("浏览器不支持websocket！");
    }

    wsTModel.onopen = function () {
    };

    wsTModel.onmessage = function (ev) {
        var urlReceived = JSON.parse(ev.data);

        //........
        if(localStorage.getItem("username") !== urlReceived.from[0]){

            modelType = urlReceived.ModelType[0];
            modelURL = urlReceived.ModelUrl[0];
            materialURL = urlReceived.MaterialUrl[0];
        }

        addModelFun();
    }
});

var actions = [];
var Models = [];
var manager;

function addModelFun() {

    if (modelType !== "") {

        if (modelType === "3ds") {
            loadTDSmodel();
        }
        else if (modelType === "obj") {
            loadOBJmodel();
        }
        else if (modelType === "x") {
            loadXmodel();
        }
        else if (modelType === "stl") {
            loadSTLmodel();
        }
        else {
            alert("The type of " + modelType + " is not supported!");
        }
        // editor.loader.loadFile(model);
    }
    else {
        alert("Please input model!");
    }

    if(materialURL === "" || modelURL === "") {
        alert("Please upload model!");
    }
}

var onProgress = function (xhr) {

    if (xhr.lengthComputable) {

        var percentComplete = xhr.loaded / xhr.total * 100;
        console.log(Math.round(percentComplete, 2) + '% downloaded');

    }

};

var onError = function (xhr) {
};

var onLoad = function (object) {
    for (var i = 0; i < object.models.length; i++) {

        var model = object.models[i];

        model.scale.x *= -1;

        Models.push(model);

    }
    //
    // loadAnimation("", 0, function () {
    //
    //     scene.add(Models[0]);
    //
    //     if (Models[0] instanceof THREE.SkinnedMesh) {
    //
    //         skeletonHelper = new THREE.SkeletonHelper(Models[0]);
    //         scene.add(skeletonHelper);
    //
    //     }
    //
    //     actions[0]['stand'].play();
    //
    // });
    //
    // object = null;
};

function loadTDSmodel() {
    var loader = new THREE.TDSLoader();

}

function loadSTLmodel() {

    reader.addEventListener('load', function (event) {

        var contents = event.target.result;

        var object = new THREE.STLLoader().parse(contents);
        // object.name = filename;

        var material = new THREE.MeshPhongMaterial();

        var mesh = new THREE.Mesh(object, material);

        scene.add(mesh);

    }, false);
    reader.readAsText(model);

}

function loadOBJmodel() {

    // var loader = new THREE.OBJLoader();
    // loader.load(modelURL, function (obj) {
    //     obj.traverse(function (child) {
    //         if (child instanceof THREE.Mesh) {
    //             child.material.side = THREE.DoubleSide;
    //         }
    //     });
    //     scene.add(obj);
    // });

    var mtlLoader = new THREE.MTLLoader();
    var index = materialURL.lastIndexOf('/');
    var mtlPath = materialURL.substring(0, index + 1);
    var mtlName = materialURL.substring(index + 1);
    mtlLoader.setPath(mtlPath);
    mtlLoader.load(mtlName, function (materials) {

        materials.preload();
        materials.transparent = true;

        var objLoader = new THREE.OBJLoader();
        objLoader.setMaterials(materials);
        index = modelURL.lastIndexOf('/');
        var objPath = modelURL.substring(0, index + 1);
        var objName = modelURL.substring(index + 1);
        objLoader.setPath(objPath);
        objLoader.load(objName, function (object) {

            // object.rotateX(-Math.PI/2);
            scene.add(object);

        },onProgress,onError);

    });
}

function loadXmodel() {

    // model loading

    manager = new THREE.LoadingManager();
    manager.onProgress = function (item, loaded, total) {

        console.log(item, loaded, total);

    };

    var Texloader = new THREE.TextureLoader();
    var loader = new THREE.XLoader(manager, Texloader);

    reader.addEventListener('load', function (event) {

        var contents = event.target.result;

        var object = loader._parse(contents, onLoad);
        // object.name = filename;

        scene.add(object);

    }, false);
    reader.readAsText(model);
}

function loadAnimation(animeName, modelId, _callback) {

    if (actions[modelId][animeName]) {

        if (_callback) {

            _callback();

        }

    } else {

        var loader2 = new THREE.XLoader(manager, Texloader);
        loader2.load(['models/xfile/' + animeName + '.x', {putPos: false, putScl: false}], function () {

            // !! important!
            // associate divided model and animation.
            loader2.assignAnimation(Models[modelId]);
            if (!animates[modelId]) {

                animates[modelId] = Models[modelId].animationMixer;

            }

            actions[modelId][animeName] = Models[modelId].animationMixer.clipAction(animeName);

            if (animeName == 'stand') {

                actions[modelId][animeName].setLoop(THREE.LoopOnce);

            }

            actions[modelId][animeName].clampWhenFinished = true;

            if (_callback) {

                _callback();
                return;

            }

            actions[modelId][animeName].play();

        }, onProgress, onError);

    }

}
