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
        <script type="text/javascript"> 
            var roomName = "${roomName}";
            var username = "${username}";
        </script>
        <script src="../../../Rooms/webScript.js" async></script>
    </head>
    <body>
        <div>
            <div style="overflow-y: auto">
                <h1>Welcome to room ${roomName}</h1>
                <hr>
                <table id="chat">
                    <tr>
                        <td>Time</td>
                        <td>Message</td>
                    </tr>
                </table>
            </div>
            <div>
                <form>
                    <input id="messageBox" type="text" placeholder="Enter Message Here" width="90%" required>
                    <input type="button" value="Send" width="10%" onclick="post(getMessage())">
                </form>
            </div>
        </div>
                <div>
                    
                </div>
                
                <script>
                    function getMessage(){
                        return document.getElementById("messageBox").value;
                    }
                </script>
    </body>
</html>
