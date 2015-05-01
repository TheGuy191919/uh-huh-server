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
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
//@Service
public class ChatRoom implements Runnable{
    private int nameHash = (int)(Math.random() * 10000);
    private LinkedBlockingDeque<DeferredResult<byte[]>> listener = new LinkedBlockingDeque<>();
    private LinkedBlockingDeque<byte[]> messages = new LinkedBlockingDeque<>();
    private Thread thread;
    private boolean running;

    public ChatRoom(){
        this.nameHash = this.hashCode();
    }
    
    public ChatRoom(String name){
        this.nameHash = name.hashCode();
        System.out.println("Created room" + name);
    }
    
    public void start(){
        if(!running){
            System.out.println("Starting server");
            running = true;
            thread = null;
            thread = new Thread(this, "Room-" + this.nameHash);
            thread.start();
        }
    }
    
    public void post(byte[] message){
        this.messages.add(message);
        System.out.println("Posing message");
        this.start();
    }
    
    @Override
    public void run() {
        while(!this.messages.isEmpty()){
            try {
                System.out.println("while Message");
                byte[] message = this.messages.take();
                while(!this.listener.isEmpty()){
                    System.out.println("While listener");
                    try {
                        DeferredResult<byte[]> listener = this.listener.take();
                        if(!listener.isSetOrExpired()){
                            listener.setResult(message);
                        }
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
        System.out.println("stopping");
        running = false;
        thread.interrupt();
        thread = null;
    }
    
    public void listen(DeferredResult<byte[]> deferred){
        System.out.println("Listening");
        this.listener.add(deferred);
    }
    
    public void setName(String name){
        this.nameHash = name.hashCode();
    }

    /**
     * @return the nameHash
     */
    public int getNameHash() {
        return nameHash;
    }
}
