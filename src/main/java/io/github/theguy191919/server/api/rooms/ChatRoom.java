/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import io.github.theguy191919.udpft2.protocol.Protocol;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
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
    
    private final int nameHash;
    private List<User> listOfUsers;
    private List<Message> listOfMessages;
    private Thread thread;
    private boolean running = false;
    
    public ChatRoom(int name){
        System.out.println("New Room" + name);
        this.nameHash = name;
        this.listOfUsers = new LinkedList<>();
        this.listOfMessages = new LinkedList<>();
        this.start();
    }
    
    private void addUser(String name){
        //System.out.println("New User " + name + " added");
        this.listOfUsers.add(new User(name));
    }
    
    public void addMessage(Protocol message){
        this.listOfMessages.add(new Message(message));
    }
    
    public void addRequest(String userName, DeferredResult<Protocol> request){
        System.out.println("New Request by " + userName);
        boolean foundUser = false;
        for(int a = 0; a < this.listOfUsers.size(); a++){
            if(this.listOfUsers.get(a).getName().equals(userName)){
                this.listOfUsers.get(a).newRequest(request);
                foundUser = true;
                //System.out.println("User Found");
            }
        }
        if(foundUser == false){
            //System.out.println("User not found... Creating new User");
            User user = new User(userName);
            user.newRequest(request);
            this.listOfUsers.add(user);
        }
    }
    
    public void start(){
        this.thread = new Thread(this, "Room: " + this.nameHash);
        this.running = true;
        this.thread.start();
    }

    @Override
    public void run() {
        while(running){
            for(int a = 0; a < this.listOfUsers.size(); a++){
                User user = this.listOfUsers.get(a);
                //System.out.println("User " + user.getName() + " updating");
                for(int b =0; b < this.listOfMessages.size(); b++){
                    Message message = this.listOfMessages.get(b);
                    if(message.compareTo(user.getLastMessage()) > 0){
                        //System.out.println("Adding new Message to user Que");
                        user.newMessage(message);
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stop(){
        this.running = false;
        thread.interrupt();
        this.thread = null;
    }
    
    public int getNameHash(){
        return this.nameHash;
    }
}
