/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.rooms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author evan__000
 */
@Controller
@RequestMapping(value = "api/rooms")
public class RoomController {
    
    @RequestMapping(value = "/{roomName}")
    public String messageRoom(@PathVariable String roomName, Model model){
        System.out.print("Fuck" + roomName);
        model.addAttribute("name", "thing");
        return "roomResponse";
    }
    
}
