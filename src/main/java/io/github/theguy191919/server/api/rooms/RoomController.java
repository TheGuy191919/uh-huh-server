/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author evan__000
 */
@Controller
//@RequestMapping(value="api/rooms")
public class RoomController {
    
    private Map<String, ChatRoom> mapOfRooms;

    public RoomController() {
        this.mapOfRooms = new ConcurrentHashMap<>();
    }
    
    @RequestMapping(value="/{roomName}/post")
    @ResponseBody
    public String roomPost(@PathVariable String roomName, Model model){
        System.out.println("Fuck" + roomName);
        model.addAttribute("name", "thing");
        return "index";
    }
    
    @RequestMapping(value="/{roomName}/info", consumes="text/plain")
    @ResponseBody
    public String roomInfo(@RequestBody byte[] body, @PathVariable String roomName, Model model){
        System.out.println("Fuck" + roomName);
        System.out.println("The request body: " + new String(body));
        model.addAttribute("name", "thing");
        return "index";
    }
    
    @RequestMapping(value="/{roomName}/listen")
    @ResponseBody
    public String roomListen(@PathVariable String roomName){
        return "";
    }
    
    @RequestMapping(value="/{roomName}/create")
    @ResponseBody
    public String roomCreate(@PathVariable String roomName){
        this.mapOfRooms.put(roomName, new ChatRoom());
        return "Created";
    }
    
    @RequestMapping(value="/{roomName}/regester", produces="text/plain")
    @ResponseBody
    public byte[] roomRegester(@PathVariable String roomName){
        return new String("OK " + roomName).getBytes();
    }
    
    @RequestMapping(value = "/*")
    public String allThings(Model model){
        //System.out.println("FallBack Used");
        return "index";
    }
}