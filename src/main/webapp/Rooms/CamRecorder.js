/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var CamRecorder = function(video){
    this.updateCID;
    this.updateSVID;
    this.frames = [];
    
    this.vidCanvasSelf = document.createElement('canvas');
    this.vidSelf = video;
    this.stream;
};

CamRecorder.prototype.updateCanvas = function() {
    //var canvas = document.querySelector('canvas');
    //var video = document.querySelector('video');
    var CANVAS_WIDTH = this.vidCanvasSelf.width;
    var CANVAS_HEIGHT = this.vidCanvasSelf.height;
    //rafId = requestAnimationFrame(updateCanves);
    this.vidCanvasSelf.getContext("2d").drawImage(this.vidSelf, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    this.frames.push(this.vidCanvasSelf.toDataURL('image/png', 1));
};

//rafId = requestAnimationFrame(updateCanves);

CamRecorder.prototype.getVideo = function() {
    var jsonFrames = JSON.stringify(this.frames);
    var compressedData = LZString.compress(jsonFrames);

    //log("-----------------------------------------------------------");
    //log("Char Length: " + jsonFrames.length);
    //log("Compressed Length: " + LZString.compress(jsonFrames).length);
    //log(jsonFrames);
    //log("-----------------------------------------------------------");

    //addToBuffer(JSON.parse(jsonFrames));

    this.frames = [];
    return compressedData;
};

CamRecorder.prototype.setUpVideo =  function() {
    //console.log("Running1");
    navigator.getUserMedia = navigator.getUserMedia ||
            navigator.mozGetUserMedia;

    var video = this.vidSelf;
    var vidStream;
    if (navigator.getUserMedia) {
        //console.log("Running2");
        
        navigator.getUserMedia({audio: true, video: {
                mandatory: {
                    //maxWidth: 640,
                    //maxHeight: 360
                }}},
        function (stream) {

            //console.log("Running3");
            video.src = window.URL.createObjectURL(stream);
            video.controls = true;
            vidStream = stream;
            //console.log(JSON.stringify(stream));

            //for (var element in stream.getTracks()[1]) {
            //    console.log(element);
            //}
            //console.log(Object.getOwnPropertyNames(stream));
            video.onloadedmetadata = function (e) {
                video.play();
            };
        },
                function (err) {
                    console.log("The following error occured: " + err.name);
                }
        );
    } else if (navigator.webkitGetUserMedia) {
        //console.log("Running4");
        //console.log(Object.getOwnPropertyNames(navigator.webkitGetUserMedia));
        navigator.webkitGetUserMedia({video: true}, function (stream) {
            //console.log("Running5");
            video.src = window.URL.createObjectURL(stream);
            video.controls = true;
            
            vidStream = stream;
            //for (var element in stream) {
            //    console.log(element);
            //}
            //console.log(Object.getOwnPropertyNames(stream));
        },
                function (err) {
                    console.log("The following error occured webkit: " + err.name);
                });
    } else {
        console.log("getUserMedia not supported");
    }
    this.stream = vidStream;
};

CamRecorder.prototype.start = function() {
    this.setUpVideo();
    this.updateCID = setInterval(this.updateCanves, 4);
    this.updateSVID = setInterval(this.getVideo, 100);
};

CamRecorder.prototype.stop = function() {
    this.stream.stop();
    clearInterval(this.updateCID);
    clearInterval(this.updateSVID);
};