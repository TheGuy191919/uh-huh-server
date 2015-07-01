/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    httpGet("http://localhost:8080/uh-huh-server/api/rooms/test/listen?username=TheGuy&random=" + Math.random(), function load() {
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