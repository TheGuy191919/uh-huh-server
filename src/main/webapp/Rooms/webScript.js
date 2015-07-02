/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var roomName;// = "test";
//var username;// = "RadomUser" + Math.random();

function createRow(time, message) {
    var table = document.getElementById("chat");
    var row = table.insertRow(table.rows.length);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.innerHTML = time;
    cell2.innerHTML = message;
}

function deleteRow() {
    document.getElementById("chat").deleteRow(0);
}

function httpGet(theUrl, loadFunc, errFunc) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", theUrl, true);
    xmlHttp.onload = loadFunc;
    xmlHttp.onerror = errFunc;
    xmlHttp.send(null);
    //return xmlHttp.responseText;
}

function listen(){
    httpGet("./listen?username=" + username + "&random=" + Math.random(), function load() {
    var message = this.responseText;
    console.log(message);
    if (message && message !== "") {
        createRow((new Date()).getHours() + ":" + (new Date()).getMinutes(), message);
    }
    listen();
}, function error() {
    var message = this.responseText;
    console.log(message);
    if (message && message !== "") {
        createRow((new Date()).getHours() + ":" + (new Date()).getMinutes(), message);
    }
    listen();
});
}

function post(message){
    httpGet("./post?username=" + username + "&data=" + username + ": " + message + "&random=" + Math.random(), function load(){
        console.log("Posted " + message);
    }, function error(){
        createRow((new Date()).getHours() + ":" + (new Date()).getMinutes(), "\"" + message + "\" failed to send");
    });
}

var dots = 0;
function updateTitle(){
    var title = "Testing";
    if(dots > 4){
        dots = 0;
    } else {
        dots++;
    }
    for (a = 0; a < dots; a++) {
        title = title + ".";
    }
    document.title = title;
}

window.setInterval(updateTitle, 1000);
listen();