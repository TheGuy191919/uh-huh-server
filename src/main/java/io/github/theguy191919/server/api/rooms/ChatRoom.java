/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
public class ChatRoom implements Runnable{
    private int nameHash = (int)(Math.random() * 10000);
    private LinkedBlockingDeque<DeferredResult<byte[]>> listener = new LinkedBlockingDeque<>();
    private LinkedBlockingDeque<byte[]> messages = new LinkedBlockingDeque<>();
    private Thread thread;
    private boolean running;

    public ChatRoom(String name){
        this.nameHash = name.hashCode();
    }
    
    public void start(){
        if(!running){
            thread = null;
            thread = new Thread(this, "Room-" + this.nameHash);
        }
    }
    
    public void post(byte[] message){
        this.messages.add(message);
        this.start();
    }
    
    @Override
    public void run() {
        while(!this.messages.isEmpty()){
            try {
                byte[] message = this.messages.take();
                while(!this.listener.isEmpty()){
                    try {
                        this.listener.take().setResult(message);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.stop();
    }
    
    public void stop(){
        running = false;
        thread.interrupt();
        thread = null;
    }
    
    public void listen(DeferredResult<byte[]> deferred){
        this.listener.add(deferred);
    }

    /**
     * @return the nameHash
     */
    public int getNameHash() {
        return nameHash;
    }
}
