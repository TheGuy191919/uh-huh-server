/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import io.github.theguy191919.udpft2.protocol.Protocol;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author evan__000
 */
public class Message implements Comparable{
    
    private long timeStemp;
    private Protocol message;
    //private String sender;
    
    public Message(Protocol message){
        this(message, Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    }
    
    public Message(Protocol message, long timeStemp){
        this.message = message;
        //this.sender = sender;
        this.timeStemp = timeStemp;
        //System.out.println("New Message");
    }

    /**
     * @return the timeStemp
     */
    public long getTimeStemp() {
        return timeStemp;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message.getData();
    }
    
    public String getSender(){
        return this.message.getOwnerName();
    }
    
    @Override
    public String toString(){
        return this.timeStemp + "|" + this.message.toString();
    }
    
    public Protocol getProtocol(){
        return this.message;
    }

    @Override
    public int compareTo(Object o) {
        if(o != null){
            long otherTime = Long.valueOf(o.toString().substring(0, o.toString().indexOf("|")));
            return (int)(this.timeStemp - otherTime);
        }
        return Integer.MAX_VALUE;
    }
    
}
