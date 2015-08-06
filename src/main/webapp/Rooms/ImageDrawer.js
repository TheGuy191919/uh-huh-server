/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ImageDrawer = function(canvas){
    this.frameBuffer = [];
    this.canvas = canvas;
    this.taskID;
};

ImageDrawer.prototype.addToBuffer = function(array) {
    if (this.frameBuffer.length > 75) {
        this.frameBuffer = [];
    }
    if (this.frameBuffer.length > 30) {
        for (a = 0; a < this.frameBuffer.length - 1; a = a + 2) {
            this.frameBuffer.splice(a, 1);
        }
    }
    this.frameBuffer = this.frameBuffer.concat(array);
};

ImageDrawer.prototype.drawFromBuffer = function() {
    //canvas = document.getElementById("canvas-other");
    var canvas = this.canvas;
    var CANVAS_WIDTH = canvas.width;
    var CANVAS_HEIGHT = canvas.height;
    if (this.frameBuffer.length >= 1 && typeof this.frameBuffer[0] !== 'undefined') {
        var image = new Image(64, 36);
        //var random = Math.random();
        //log("init: " + random);
        image.onload = function () {
            canvas.getContext("2d").drawImage(image, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            //log("load: " + random);
        };
        image.src = this.frameBuffer[0];
        this.frameBuffer.splice(0, 1);
    }
};

ImageDrawer.prototype.start = function(){
    this.taskID = setInterval(this.drawFromBuffer, 4);
};

ImageDrawer.prototype.stop = function(){
    clearInterval(this.taskID);
};