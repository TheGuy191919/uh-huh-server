/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

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
    
    public ChatRoom(String name){
        this.nameHash = name.hashCode();
        this.listOfUsers = new LinkedList<>();
        this.listOfMessages = new LinkedList<>();
        this.start();
    }
    
    private void addUser(String name){
        System.out.println("New User " + name + " added");
        this.listOfUsers.add(new User(name));
    }
    
    public void addMessage(byte[] message){
        this.listOfMessages.add(new Message(message));
    }
    
    public void addRequest(String userName, DeferredResult<byte[]> request){
        System.out.println("New Request by " + userName);
        boolean foundUser = false;
        for(int a = 0; a < this.listOfUsers.size(); a++){
            if(this.listOfUsers.get(a).getName().equals(userName)){
                this.listOfUsers.get(a).newRequest(request);
                foundUser = true;
                System.out.println("User Found");
            }
        }
        if(foundUser == false){
            System.out.println("User not found... Creating new User");
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
                System.out.println("User " + user.getName() + " updating");
                for(int b =0; b < this.listOfMessages.size(); b++){
                    Message message = this.listOfMessages.get(b);
                    if(message.compareTo(user.getLastMessage()) > 0){
                        System.out.println("Adding new Message to user Que");
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
//    private int nameHash = (int)(Math.random() * 10000);
//    //private LinkedBlockingDeque<DeferredResult<byte[]>> listener = new LinkedBlockingDeque<>();
//    //private LinkedBlockingDeque<byte[]> messages = new LinkedBlockingDeque<>();
//    private List<User> listOfUsers = Collections.synchronizedList(new LinkedList<User>());
//    private List<Message> listOfMessages = Collections.synchronizedList(new LinkedList<Message>());
//    private Thread thread;
//    private boolean running;
//
//    public ChatRoom(){
//        this.nameHash = this.hashCode();
//    }
//    
//    public ChatRoom(String name){
//        this.nameHash = name.hashCode();
//        System.out.println("Created room" + name);
//    }
//    
//    public void start(){
//        if(!running){
//            System.out.println("Starting server");
//            running = true;
//            thread = null;
//            thread = new Thread(this, "Room-" + this.nameHash);
//            thread.start();
//        }
//    }
//    
//    public void post(Message message){
//        //this.messages.add(message);
//        this.listOfMessages.add(message);
//        System.out.println("Posing message");
//        this.start();
//    }
//    
//    public void registerUser(User user){
//        this.listOfUsers.add(user);
//        this.start();
//    }
//    
//    @Override
//    public void run() {
////        while(!this.messages.isEmpty()){
////            try {
////                System.out.println("while Message");
////                byte[] message = this.messages.take();
////                while(!this.listener.isEmpty()){
////                    System.out.println("While listener");
////                    try {
////                        DeferredResult<byte[]> listener = this.listener.take();
////                        if(!listener.isSetOrExpired()){
////                            listener.setResult(message);
////                        }
////                    } catch (InterruptedException ex) {
////                        Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
////            } catch (InterruptedException ex) {
////                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            try {
////                Thread.sleep(500);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
////            }
////        }
//        int idleTimer = 0;
//        while(idleTimer < 10){
//            //while(this.haveTasks){
//                for (User user : this.listOfUsers) {
//                    Message lastMessage = user.getLastMessageReceived();
//                    for(Message message : this.listOfMessages){
//                        if(lastMessage == null || message.compareTo(lastMessage) > 0){
//                            user.setResult(message);
//                            idleTimer = 0;
//                        }
//                    }
//                }
//            //}
//                idleTimer++;
//        }
//        this.stop();
//    }
//    
//    public void stop(){
//        System.out.println("stopping");
//        running = false;
//        thread.interrupt();
//        thread = null;
//    }
//    
//    public void listen(String userName, DeferredResult<byte[]> deferred){
//        boolean haveUser = false;
//        for(User user : this.listOfUsers){
//            if(user.getName().equals(userName)){
//                user.setRequest(deferred);
//                haveUser = true;
//            }
//        }
//        if(!haveUser){
//            User user = new User(userName);
//            this.registerUser(user);
//            user.setRequest(deferred);
//        }
//        //System.out.println("Listening");
//        //this.listOfUsers.add(deferred);
//    }
//    
//    public void setName(String name){
//        this.nameHash = name.hashCode();
//    }
//
//    /**
//     * @return the nameHash
//     */
//    public int getNameHash() {
//        return nameHash;
//    }
}
