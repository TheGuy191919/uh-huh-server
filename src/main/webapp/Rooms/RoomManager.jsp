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
                    
                </div>
                
                <script>
                    function getMessage(){
                        var message = document.getElementById("messageBox").value;
                        document.getElementById("messageBox").value = "";
                        return message;
                    }
                    function onKeyPress(event){
                        //console.log("Key pressed:" + event.keyCode);
                        if(event.keyCode == 13){
                            //console.log("Enter Pressed");
                            var message = getMessage();
                            post(message);
                            createRow((new Date()).getHours() + ":" + (new Date()).getMinutes(), username + ": " + message);
                            return false;
                        }
                        return true;
                    }
                </script>
    </body>
</html>
