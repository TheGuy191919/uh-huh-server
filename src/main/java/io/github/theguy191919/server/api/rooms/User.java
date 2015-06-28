/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingDeque;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
public class User {
    
    private DeferredResult<byte[]> request;
    private long lastMessageTime = Long.MIN_VALUE;
    private LinkedBlockingDeque<Message> messageQue = new LinkedBlockingDeque<>();
    private String name;
    
    public User(String name){
        this.name = name;
        System.out.println("User " + name + "made");
    }
    
    @Override
    public String toString(){
        return "User: " + name;
    }
    
    /*
    the method called to set result to request by roomcontroller.
    */
    public void setResult(Message message){
        this.messageQue.add(message);
//        if(!request.isSetOrExpired() && !this.messageQue.isEmpty()){
//            Message message = this.messageQue.poll().getMessage();
//            request.setResult(message);
//            this.lastMessageTime = message.getTimeStemp();
//        } else {
//            
//        }
    }
    
    /*
    Called by either controller or main thread to add to the request pool
    */
    public void setRequest(DeferredResult<byte[]> request){
        this.request = request;
        if(!this.messageQue.isEmpty()){
            Message message = this.messageQue.poll();
            this.request.setResult(message.getMessage());
            this.lastMessageTime = message.getTimeStemp();
        }
    }
    
    public long getLastMessageReceived(){
        return this.lastMessageTime;
    }
    
    public String getName(){
        return this.name;
    }
}
