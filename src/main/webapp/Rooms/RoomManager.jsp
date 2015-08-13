<%-- 
    Document   : RoomManager
    Created on : Jun 30, 2015, 11:36:18 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Testing</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../../../Rooms/ImageDrawer.js" async></script>
        <script src="../../../Rooms/lz-string.js" async></script>
        <script src="../../../Rooms/CamRecorder.js" async></script>
        <script type="text/javascript">

            var roomName = "${roomName}";
            var username = "${username}";
        </script>
        <script src="../../../Rooms/webScript.js" async></script>
        <link rel="stylesheet" href="../../../Rooms/roomStyle.css">
    </head>
    <body>
        <div id="leftDiv">
            <div id="chatDiv">
                <h1>Welcome to room ${roomName}</h1>
                <hr>
                <table id="chat">
                    <tr>
                        <td>Time</td>
                        <td>Message</td>
                    </tr>
                </table>
            </div>
            <div id="submitDiv">
                <input id="messageBox" type="text" placeholder="Enter Message Here" width="90%" onkeyup="onKeyPress(event)" required>
                <input type="button" value="Send" width="10%" onclick="post(getMessage())">
            </div>
        </div>
        <div id="rightDev">
            <button onclick="startVideoCall()">Start Video Chat</button>
            <table id="videoTabel">
                <tr>
                    <td>
                        <p>Your Video</p>
                        <video id='selfVideo' autoplay></video>
                    </td>
                </tr>
                <tr>
                    <td>List of Users</td>
                </tr>
            </table>>
        </div>

        <script>

            function getMessage() {
                var message = document.getElementById("messageBox").value;
                document.getElementById("messageBox").value = "";
                return message;
            }
            function onKeyPress(event) {
                //console.log("Key pressed:" + event.keyCode);
                if (event.keyCode == 13) {
                    //console.log("Enter Pressed");
                    var message = getMessage();
                    post(message);
                    createRow((new Date()).getHours() + ":" + (new Date()).getMinutes(), username + ": " + message);
                    return false;
                }
                return true;
            }

            function startVideoCall() {
                var recorder = new CamRecorder(document.getElementById("selfVideo"));
                recorder.onVideoEvent(function (compressedVideo) {
                    var keyValuePair = {};
                    keyValuePair.username = username;
                    keyValuePair.goal = "IMAGESET";
                    keyValuePair.data = compressedVideo;
                    httpPost("./post", keyValuePair, function (data, textStatus, jqXHR){
                        
                    });
                });
                recorder.start();
            }

            var arrayOfImageDrawer = [];

            function getUser(username) {
                for (imgDrawer in arrayOfImageDrawer) {
                    if (arrayOfImageDrawer[imgDrawer].username == username) {
                        //console.log("Found User");
                        return arrayOfImageDrawer[imgDrawer];
                    }
                }
                var table = document.getElementById("videoTabel");
                var row = table.insertRow(table.rows.length);
                var cell1 = row.insertCell(0);
                var nameElement = document.createElement("p");
                nameElement.innerHTML = username;
                var canvasElement = document.createElement("canvas");
                //console.
                cell1.appendChild(nameElement);
                cell1.appendChild(canvasElement);
                var newUser = new ImageDrawer(canvasElement);
                arrayOfImageDrawer.push(newUser);
                newUser.username = username;
                newUser.start();
                return newUser;
            }

        </script>
    </body>
</html>
