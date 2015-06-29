/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import io.github.theguy191919.udpft.protocol.Protocol2;
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
    //private long lastMessageTime = Long.MIN_VALUE;
    private LinkedBlockingDeque<Message> messageQue = new LinkedBlockingDeque<>();
    private String name;
    private Message lastMessage;
    
    public User(String name){
        this.name = name;
        Message firstMessage = new Message(new Protocol2().returnByteArray());
        this.lastMessage = firstMessage;
        this.newMessage(firstMessage);
        //System.out.println("User " + name + "made");
    }
    
    public User(String name, DeferredResult<byte[]> request){
        this.name = name;
        this.request = request;
        Message firstMessage = new Message(new Protocol2().returnByteArray());
        this.lastMessage = firstMessage;
        this.newMessage(firstMessage);
        //System.out.println("User " + name + "made");
    }
    
    @Override
    public String toString(){
        return "User: " + name;
    }
    
    /*
    the method called to set result to request by roomcontroller.
    */
    public void newMessage(Message message){
        this.messageQue.add(message);
        this.lastMessage = message;
        //if(!request.isSetOrExpired() && !this.messageQue.isEmpty()){
            this.setResponse();
//            Message message = this.messageQue.poll().getMessage();
//            request.setResult(message);
//            this.lastMessageTime = message.getTimeStemp();
//        } else {
//            
        //}
    }
    
    /*
    Called by either controller or main thread to add to the request pool
    */
    public void newRequest(DeferredResult<byte[]> request){
        this.request = request;
        this.setResponse();
        //if(!this.messageQue.isEmpty()){
            //Message message = this.messageQue.poll();
            //this.request.setResult(message.getMessage());
            //this.lastMessageTime = message.getTimeStemp();
        //}
    }
    
    private synchronized void setResponse() {
        if (this.request != null) {
            if (!this.messageQue.isEmpty() && !this.request.isSetOrExpired()) {
                Message message = this.messageQue.poll();
                this.request.setResult(message.getMessage());
                //this.lastMessage = message;
            }
        }
    }

    public Message getLastMessage(){
        return this.lastMessage;
    }
    
//    public long getLastMessageReceived(){
//        return this.lastMessageTime;
//    }
    
    public String getName(){
        return this.name;
    }
}
