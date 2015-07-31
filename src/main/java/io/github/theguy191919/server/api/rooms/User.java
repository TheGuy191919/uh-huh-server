/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

//import io.github.theguy191919.udpft.protocol.Protocol2;
import io.github.theguy191919.udpft2.protocol.Protocol;
import io.github.theguy191919.udpft2.protocol.ProtocolGoal;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingDeque;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
public class User {
    
    private DeferredResult<Protocol> request;
    //private long lastMessageTime = Long.MIN_VALUE;
    private LinkedBlockingDeque<Message> messageQue = new LinkedBlockingDeque<>();
    private String name;
    private Message lastMessage;
    
    public User(String name){
        this.name = name;
        Message firstMessage = new Message(new Protocol("Server", ProtocolGoal.POST, "Welcome to Uh-huh Server"));
        this.lastMessage = firstMessage;
        this.newMessage(firstMessage);
        //System.out.println("User " + name + "made");
    }
    
    public User(String name, DeferredResult<Protocol> request){
        this(name);
        this.request = request;
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
        this.setResponse();
    }
    
    /*
    Called by either controller or main thread to add to the request pool
    */
    public void newRequest(DeferredResult<Protocol> request){
        //System.out.println("New Request");
        this.request = request;
        this.setResponse();
    }
    
    private void setResponse() {
        //System.out.println("Attempting to Message");
        if (this.request != null) {
            //System.out.println("Request is not null");
            if (!this.messageQue.isEmpty() && !this.request.isSetOrExpired()) {
                Message message = this.messageQue.poll();
                if(!message.getSender().equals(this.name)){
                    this.request.setResult(message.getProtocol());
                }
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
