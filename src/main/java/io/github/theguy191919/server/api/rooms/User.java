/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.theguy191919.server.api.rooms;

import java.util.Calendar;
import java.util.TimeZone;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author evan__000
 */
public class User {
    
    DeferredResult<byte[]> request;
    long lastMessageReceived = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
    
    public User(){
        
    }
    
    public void update(byte[] response){
        if(!request.isSetOrExpired()){
            request.setResult(response);
            this.lastMessageReceived = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        } else {
            
        }
    }
    
    public long getLastMessageReceived(){
        return this.lastMessageReceived;
    }
}
