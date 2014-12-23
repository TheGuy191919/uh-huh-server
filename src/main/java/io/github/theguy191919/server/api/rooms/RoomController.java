/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import io.github.theguy191919.udpft.protocol.Protocol;
import io.github.theguy191919.udpft.protocol.Protocol3;
import io.github.theguy191919.udpft.protocol.Protocol4;
import java.util.AbstractQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
@Controller
//@RequestMapping(value="api/rooms")
public class RoomController {
    
    private Map<Integer, ChatRoom> mapOfRooms;
    private LinkedBlockingDeque<byte[]> que = new LinkedBlockingDeque();

    public RoomController() {
        this.mapOfRooms = new ConcurrentHashMap<>();
    }
    
    @RequestMapping(value="/{roomName}/post")
    @ResponseBody
    public byte[] roomPost(@RequestBody byte[] body, @PathVariable String roomName, Model model){
        if(this.mapOfRooms.containsKey(roomName.hashCode())){
            this.mapOfRooms.get(roomName.hashCode()).post(body);
        } else {
            Protocol error = new Protocol4();
            error.setContent("No room found");
            return error.returnByteArray();
        }
        return new Protocol3().returnByteArray();
        //this.que.add(body);
    }
    
    @RequestMapping(value="/{roomName}/info")//, consumes="text/plain")
    @ResponseBody
    public byte[] roomInfo(@RequestBody byte[] body, @PathVariable String roomName, Model model){
        System.out.println("Fuck" + roomName);
        System.out.println("The request body: " + new String(body));
        System.out.println("The length of the message is: " + body.length);
        for(int a = 0; a < body.length; a++){
            System.out.println(a + ": " + body[a]);
        }
        model.addAttribute("name", "thing");
        return body;
    }
    
    @RequestMapping(value="/{roomName}/listen")
    @ResponseBody
    public DeferredResult<byte[]> roomListen(@RequestBody byte[] body, @PathVariable String roomName){
        final DeferredResult<byte[]> result = new DeferredResult<>();
        if(this.mapOfRooms.containsKey(roomName.hashCode())){
            this.mapOfRooms.get(roomName.hashCode()).listen(result);
        } else {
            Protocol error = new Protocol4();
            error.setContent("No room found");
            result.setResult(error.returnByteArray());
        }
        return result;
    }
    
    @RequestMapping(value="/{roomName}/create")
    @ResponseBody
    public String roomCreate(@PathVariable String roomName){
        ChatRoom room = new ChatRoom(roomName);
        this.mapOfRooms.put(room.getNameHash(), room);
        return "Created";
    }
    
    @RequestMapping(value="/{roomName}/regester")//, produces="text/plain")
    @ResponseBody
    public byte[] roomRegester(@PathVariable String roomName){
        return new String("OK " + roomName).getBytes();
    }
    
    @RequestMapping(value = "/*")
    public String allThings(Model model){
        //System.out.println("FallBack Used");
        return "all";
    }
}