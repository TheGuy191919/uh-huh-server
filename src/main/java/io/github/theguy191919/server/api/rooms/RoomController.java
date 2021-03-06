/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import io.github.theguy191919.udpft.encryption.SimpleCrypto;
import io.github.theguy191919.udpft2.protocol.Protocol;
import io.github.theguy191919.udpft2.protocol.ProtocolGoal;
//import io.github.theguy191919.udpft.protocol.Protocol;
//import io.github.theguy191919.udpft.protocol.Protocol3;
//mport io.github.theguy191919.udpft.protocol.Protocol4;
import java.util.AbstractQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    //private LinkedBlockingDeque<byte[]> que = new LinkedBlockingDeque();

    public RoomController() {
        this.mapOfRooms = new ConcurrentHashMap<>();
    }
    
    @RequestMapping(value="/{roomName}/post")
    @ResponseBody
    public String roomPost(@RequestParam(value = "goal", required = false) String goal, @RequestParam(value = "data", required = false) String data, @RequestParam(value = "username", required = false, defaultValue = "Random User") String username, @PathVariable String roomName, Model model){
        if(this.mapOfRooms.containsKey(roomName.hashCode())){
            this.mapOfRooms.get(roomName.hashCode()).addMessage(new Protocol(username, ProtocolGoal.valueOf(goal), data));
        } else {
            Protocol error = new Protocol("Server", ProtocolGoal.POST, "No room found");
            System.out.println("Owner: " + error.getOwnerName());
            System.out.println("Goal: " + error.getGoal().toString());
            System.out.println("Data: " + error.getData());
            //error.setContent("No room found");
            return error.toString();
        }
        
        Protocol reply = new Protocol("Server", ProtocolGoal.POST, "");
        //System.out.println("Owner: " + reply.getOwnerName());
        //System.out.println("Goal: " + reply.getGoal().toString());
        //System.out.println("Data: " + reply.getData());
        return data.toString();
        //this.que.add(body);
    }
    
    @RequestMapping(value="/{roomName}/info")//, consumes="text/plain")
    @ResponseBody
    public Protocol roomInfo(@RequestParam(value = "data", required = false) String data, @PathVariable String roomName){
        //saved for future api
//        System.out.println("Fuck " + roomName);
//        System.out.println("The request body: " + data);
//        System.out.println("The length of the message is: " + data);
//        System.out.println("Message is: " + new SimpleCrypto(roomName.hashCode()).decrypt(data));
        //model.addAttribute("name", "thing");
        return new Protocol("UserRandom", ProtocolGoal.POST, "This is a test");
    }
    
    @RequestMapping(value="/{roomName}/listen")
    @ResponseBody
    public DeferredResult<Protocol> roomListen(@RequestBody(required = false) byte[] body, @RequestParam(value = "username", required = false, defaultValue = "Random User") String username, @PathVariable String roomName){
        final DeferredResult<Protocol> result = new DeferredResult<>((long)60000, new Protocol("server", ProtocolGoal.POST, ""));
        if(this.mapOfRooms.containsKey(roomName.hashCode())){
            this.mapOfRooms.get(roomName.hashCode()).addRequest(username, result); //be replaced with decodeed protocol username
        } else {
            ChatRoom room = new ChatRoom(roomName.hashCode());
            this.mapOfRooms.put(room.getNameHash(), room);
            this.mapOfRooms.get(roomName.hashCode()).addRequest(username, result);
        }
        return result;
    }
    
    @RequestMapping(value="/{roomName}/create")
    @ResponseBody
    public String roomCreate(@PathVariable String roomName){
        //unneeded
        ChatRoom room = new ChatRoom(roomName.hashCode());
        this.mapOfRooms.put(room.getNameHash(), room);
        return "Created";
    }
    
    @RequestMapping(value="/{roomName}/regester")//, produces="text/plain")
    @ResponseBody
    public byte[] roomRegester(@PathVariable String roomName){
        //unneeded
        return new String("OK " + roomName).getBytes();
    }
    
    @RequestMapping(value="/{roomName}/manager")
    public String roomManager(@RequestParam(value = "username", required = false) String username, @PathVariable String roomName, ModelMap model){
        if(username == null){
            username = "RamdomUser" + (int)(Math.random() * 10000);
        }
        model.addAttribute("roomName", roomName);
        model.addAttribute("username", username);
        return "./Rooms/RoomManager";
    }
    
    @RequestMapping(value = "/*")
    public String allThings(Model model){
        //unneeded
        //System.out.println("FallBack Used");
        return "all";
    }
    
    @PreDestroy
    public void stop(){
        for(int a = 0; a < this.mapOfRooms.size(); a++){
            this.mapOfRooms.get(a).stop();
        }
    }
}