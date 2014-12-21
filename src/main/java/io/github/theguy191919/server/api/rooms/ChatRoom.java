/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author evan__000
 */
public class ChatRoom implements Runnable{
    private int nameHash = (int)(Math.random() * 10000);
    private List<>

    public ChatRoom(String name){
        this.nameHash = name.hashCode();
    }
    
    public void start(){
        
    }
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void stop(){
        
    }

    /**
     * @return the nameHash
     */
    public int getNameHash() {
        return nameHash;
    }
}
